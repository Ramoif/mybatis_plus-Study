package com.ycsx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ycsx.pojo.User;
import org.springframework.stereotype.Repository;

//注解@代表持久层Dao，注意在Main中添加@MapperScan
//直接继承基本类BaseMapper<T>，并且自动完成所有的CRUD编写。
@Repository
public interface UserMapper extends BaseMapper<User> {
}
