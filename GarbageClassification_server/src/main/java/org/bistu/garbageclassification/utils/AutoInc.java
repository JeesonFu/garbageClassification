package org.bistu.garbageclassification.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识数据库集中需要自增的field字段
 * @author 付金振
 *（未使用）
 */
@Target(ElementType.FIELD) //用于描述域（被描述的注解@autoinc可以用在field）
@Retention(RetentionPolicy.RUNTIME)  //运行时保留（被描述的注解在运行时有效）
public @interface AutoInc {

}
