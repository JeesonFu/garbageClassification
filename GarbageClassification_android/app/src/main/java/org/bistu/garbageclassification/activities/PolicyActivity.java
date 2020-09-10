package org.bistu.garbageclassification.activities;

import android.app.DownloadManager;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.tencent.smtt.sdk.TbsReaderView;

import org.bistu.garbageclassification.R;
import org.bistu.garbageclassification.entities.Policy;

import java.io.File;

public class PolicyActivity extends AppCompatActivity implements TbsReaderView.ReaderCallback{

    private Intent intent;

    private Policy policy = new Policy();

    private RelativeLayout rl;
    private TbsReaderView tbsReaderView;

    private DownloadManager downloadManager;
    private long requestId;
    private DownloadObserver downloadObserver;

    private String fileUrl = "";
    private String fileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //初始化X5内核
        initX5();

        intent = getIntent();
        policy.setTitle(intent.getStringExtra("title"));
        policy.setContent(intent.getStringExtra("path"));

        fileUrl = policy.getContent();
        fileName = getFileName(fileUrl);

        getSupportActionBar().setTitle(policy.getTitle());

        //初始化TBS组件，动态创建TBS对象
        tbsReaderView = new TbsReaderView(this, this);
        rl = (RelativeLayout) findViewById(R.id.file_view);
        rl.addView(tbsReaderView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //确认本地文件是否已存在/下载
        if(isFileExist()) {
            //显示文件
            displayFile();
        } else {
            downloadFile();
        }
    }

    //下载文件
    private void downloadFile() {
        downloadObserver = new DownloadObserver(new Handler());
        getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, downloadObserver);  //注册监听器，查询下载状态和进度

        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);//设置文件下载路径为download文件夹
        //request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);//设置通知栏不显示下载状态
        requestId = downloadManager.enqueue(request);

    }

    //退出时必须停止TBS组件，取消下载监听。否则下次无法打开
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tbsReaderView.onStop();
        if(downloadObserver != null) {
            getContentResolver().unregisterContentObserver(downloadObserver);
        }
    }

    //显示文件
    private void displayFile() {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", getLocalFile().getPath());  //文件本地地址
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());  //另一临时文件路径，必须设置否则报错
        boolean result = tbsReaderView.preOpen(getFileType(fileName), false);
        if (result) {
            tbsReaderView.openFile(bundle);
        }
    }

    //检查文件是否已下载到本地
    private boolean isFileExist() {
        return getLocalFile().exists();
    }
    private File getLocalFile() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
    }

    //截取文件格式
    private String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    //截取文件名称
    private String getFileName(String url) {
        String fName = null;
        try {
            fName = url.substring(url.lastIndexOf("/") + 1);
        } finally {
            if (TextUtils.isEmpty(fName)) {
                Toast.makeText(this,"文件路径错误",Toast.LENGTH_SHORT).show();
            }
        }
        return fName;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化X5内核
    private void initX5() {
        //初始化X5内核
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。
            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("@@","加载内核是否成功:"+b);
            }
        });

        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                //tbs内核下载完成回调
            }

            @Override
            public void onInstallFinish(int i) {
                //内核安装完成回调，
            }

            @Override
            public void onDownloadProgress(int i) {
                //下载进度监听
            }
        });
    }


    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {
    }


    /**
     * 下载进度监听类
     */
    private class DownloadObserver extends ContentObserver {

        private DownloadObserver(Handler handler) {
            super(handler);
        }

        //实时下载进度
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Log.i("downloadUpdate: ", "onChange(boolean selfChange, Uri uri)");
            queryDownloadStatus();
        }
    }

    //查询下载状态
    private void queryDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(requestId);
        Cursor cursor = null;
        try {
            cursor = downloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载的字节数
                int currentBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //总需下载的字节数
                int totalBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //状态所在的列索引
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                Log.i("downloadUpdate: ", currentBytes + " " + totalBytes + " " + status);
                if (DownloadManager.STATUS_SUCCESSFUL == status) {
                    displayFile();
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
