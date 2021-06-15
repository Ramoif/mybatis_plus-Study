package com.ycsx.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j      //日志
@Component  //组件 - 注解作用：添加到IOC容器中
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override   //插入填充策略
    public void insertFill(MetaObject metaObject) {
        log.info("执行插入操作......");
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    @Override   //更新填充策略
    public void updateFill(MetaObject metaObject) {
        log.info("执行更新操作......");
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
