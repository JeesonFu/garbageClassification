package org.bistu.garbageclassification.utils;

/**
 * 图像识别
 * token授权请求链接
 */
public class Token {

    /**
     * 获取权限token
     */
    public static String getAuthUrl() {
        // 官网获取的 API Key
        String clientId = "XZYx22IiGFRxDWB6HR6TrkNG";
        // 官网获取的 Secret Key
        String clientSecret = "pZ3ZQ1LlDXGlNzzy4UNEZ1O7KqQDbcAn";
        return getAuthUrl(clientId, clientSecret);
    }

    /**
     * 获取API访问权限链接
     */
    public static String getAuthUrl(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                + "grant_type=client_credentials"
                + "&client_id=" + ak
                + "&client_secret=" + sk;
        return getAccessTokenUrl;
    }
}