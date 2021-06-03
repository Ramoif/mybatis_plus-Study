package com.ycsx;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ycsx.mapper.UserMapper;
import com.ycsx.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class WrapperTest {
    @Autowired
    private UserMapper userMapper;

    //查询全部
    @Test
    void contextLoads() {
        //selectList方法中有一个参数Wrapper（条件构造器），查询全部填null
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    //查询name不为空，邮箱不为空，年龄>12
    @Test
    void wrapperTest01(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //可使用链式编程
        wrapper.isNotNull("name")
                .isNotNull("email")
                .ge("age",12);
        userMapper.selectList(wrapper).forEach(System.out::println);
    }

    //查询名字=XXX的用户（单一）
    @Test
    void test02(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name","Tom");
        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
    }

    //查询年龄范围在20~30
    @Test
    void test03(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.between("age",20,30); //区间
        Integer count = userMapper.selectCount(wrapper); //结果数
        System.out.println(count);
    }

    //模糊查询，名字中不包含e，邮箱首字母是t
    @Test
    void test04(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.notLike("name","e")
                .likeRight("email","t");
        List<Map<String, Object>> maps = userMapper.selectMaps(wrapper);
        maps.forEach(System.out::println);
    }

    //连接查询（内查询）
    @Test
    void test05() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // id 在子查询中查询出来，查询自行拼接条件。（id<3）
        wrapper.inSql("id","select id from user where id<3");
        List<Object> obj = userMapper.selectObjs(wrapper);
        obj.forEach(System.out::println);
    }

    //根据id排序，Asc升序，Desc降序
    @Test
    void test06(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        userMapper.selectList(wrapper).forEach(System.out::println);
    }
}
