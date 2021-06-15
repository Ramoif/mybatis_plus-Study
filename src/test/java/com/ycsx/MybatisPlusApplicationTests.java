package com.ycsx;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ycsx.mapper.UserMapper;
import com.ycsx.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class MybatisPlusApplicationTests {
    @Autowired
    private UserMapper userMapper;

    //查询全部
    @Test
    void contextLoads() {
        //selectList方法中有一个参数Wrapper（条件构造器），查询全部填null
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    //插入
    @Test
    void test1() {
        User user = new User();
        user.setName("赵六");
        user.setAge(21);
        user.setEmail("123123123@123.com");
        int insert = userMapper.insert(user);
        System.out.println("insert = " + insert + ";\nuser = " + user);
    }

    //更新
    @Test
    void test2() {
        User user = new User();
        user.setId(5L);
        user.setName("5号待修改-version 4");
        int i = userMapper.updateById(user);
        System.out.println("i = " + i);
        System.out.println("user = " + user);
    }

    //测试乐观锁在并发情况下失败的情况
    @Test
    void test3() {
        User user = userMapper.selectById(2L);
        user.setName("2号乐观锁更新111");
        user.setEmail("test11111@test.com");

        User user2 = userMapper.selectById(2L);
        user2.setName("2号乐观锁更新222");
        user2.setEmail("test22222@test.com");
        userMapper.updateById(user2);

        userMapper.updateById(user);
    }

    //批量查询
    @Test
    void test4() {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }

    //条件查询
    @Test
    void test5() {
        //自定义条件
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Tom");
        map.put("age", 28);
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    //分页查询
    @Test
    void test6() {
        //参数为(当前页*1开始, 页面大小)
        Page<User> page = new Page<>(1, 5);
        userMapper.selectPage(page, null);
        page.getRecords().forEach(System.out::println);
        System.out.println(page.getTotal()); //Total
    }

    //测试删除
    @Test
    void test07() {
        userMapper.deleteById(5L);
    }

}
