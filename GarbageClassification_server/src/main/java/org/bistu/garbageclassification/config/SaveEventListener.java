package org.bistu.garbageclassification.config;

import java.lang.reflect.Field;

import org.bistu.garbageclassification.bean.AutoIncInfo;
import org.bistu.garbageclassification.utils.AutoInc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 当向数据库新增数据项时，在保存前确认是否有需要自增字段，实现id字段自增
 * @author 付金振
 *（未使用）
 */
@Slf4j
@Component
public class SaveEventListener extends AbstractMongoEventListener<Object> {
 
    @Autowired
    private MongoTemplate mongo;
 
    /**
     * insert、insertList和save操作进行前执行
     */
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
    	log.info(""+event.getSource().toString());
    	final Object source = event.getSource();
        if (source != null) {
            ReflectionUtils.doWithFields(source.getClass(), new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    ReflectionUtils.makeAccessible(field);
                    // 如果字段已添加自定义的AutoInc注解
                    if (field.isAnnotationPresent(AutoInc.class) 
                            //判断注解的字段是否为数字且值是否等于0, =0即新的
                    		//如果大于0说明是已存在的，有ID不需要生成ID(即不需自增)
                    		//&& field.get(source) instanceof Number
                            //&& field.getInt(source) == 0
                            ) {
                        // 设置自增ID
                        field.set(source, getNextId(source.getClass().getSimpleName()));
                        log.debug("increase key, source = {} , nextId = {}", source, field.get(source));
                    }
                }
            });
        }
    }
 
    /**
	* 获取下一个自增ID（找集合名，自增id+1，设置若无则增/若有则+1，并返回+1后的值为新的自增后的id）
     * @param collName
     * @return 序列值
     */
    private Integer getNextId(String collName) {
        Query query = new Query(Criteria.where("collName").is(collName));
        Update update = new Update();
        update.inc("incId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        AutoIncInfo autoinc = mongo.findAndModify(query, update, options, AutoIncInfo.class);
        return autoinc.getIncId();
    }
}