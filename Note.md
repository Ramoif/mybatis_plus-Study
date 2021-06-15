# Mybatis-Plus 快速开始

#### 写在前面

本笔记大部分参考[尚硅谷](https://www.bilibili.com/video/av27212529)和[学相伴](https://www.kuangstudy.com/)的视频教程而来，仅供参考。

为了构建方便，将**数据库操作的语句**整合在前面，下面过程不受影响：

```sql
create database mybatis_plus;
use mybatis_plus;

DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id BIGINT(20) NOT NULL COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age INT(11) NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
);

DELETE FROM user;

INSERT INTO user (id, name, age, email) VALUES
(1, 'Jone', 18, 'test1@baomidou.com'),
(2, 'Jack', 20, 'test2@baomidou.com'),
(3, 'Tom', 28, 'test3@baomidou.com'),
(4, 'Sandy', 21, 'test4@baomidou.com'),
(5, 'Billie', 24, 'test5@baomidou.com');

ALTER TABLE `user`
    ADD COLUMN `create_time`  datetime NULL DEFAULT CURRENT_TIMESTAMP AFTER `email`,
    ADD COLUMN `update_time`  datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER `create_time`;

ALTER TABLE `user`
    MODIFY COLUMN `create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间' AFTER `email`,
    MODIFY COLUMN `update_time`  datetime NULL DEFAULT NULL COMMENT '修改时间' AFTER `create_time`,
    ADD COLUMN `version`  int(10) NULL DEFAULT 1 COMMENT '乐观锁' AFTER `email`;

ALTER TABLE `user`
    ADD COLUMN `deleted`  int(1) NULL DEFAULT 0 COMMENT '逻辑删除' AFTER `version`;
```



***

#### 开始创建项目

 引用自 - [Mybatis-Plus官网](https://mp.baomidou.com/)

1. 创建数据库（字符集**utf8**和排序规则**utf8_general_ci**），我这里在mybatis_plus目录下新建了user表。

2. 创建User表给案例使用，这里直接使用快速开始中提供的表快速构建。

   [快速开始](https://mp.baomidou.com/guide/quick-start.html#%E5%88%9D%E5%A7%8B%E5%8C%96%E5%B7%A5%E7%A8%8B)

   ```sql
   DROP TABLE IF EXISTS user;
   
   CREATE TABLE user
   (
   	id BIGINT(20) NOT NULL COMMENT '主键ID',
   	name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
   	age INT(11) NULL DEFAULT NULL COMMENT '年龄',
   	email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
   	PRIMARY KEY (id)
   );
   ```

3. 添加User表的内容。

   ```sql
   DELETE FROM user;
   
   INSERT INTO user (id, name, age, email) VALUES
   (1, 'Jone', 18, 'test1@baomidou.com'),
   (2, 'Jack', 20, 'test2@baomidou.com'),
   (3, 'Tom', 28, 'test3@baomidou.com'),
   (4, 'Sandy', 21, 'test4@baomidou.com'),
   (5, 'Billie', 24, 'test5@baomidou.com');
   ```

4. 创建SpringBoot项目
   * 创新新项目，选择**Spring Initalizr**，直接下一步；
   * 修改**Group**，Artifact，**Java**版本，**Package**代表src包的目录，下一步；
   * 勾选上Web中的Spring Web，下一步；
   * 修改保存的路径，删除目录下多余的文件.gitignore，mvnw，mvnw.cmd 。

5. 在pom.xml中导入依赖。

   ```xml
   <!--数据库驱动-->
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <version>5.1.47</version>
   </dependency>
   <!--Lombok-->
   <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
       <version>1.18.18</version>
   </dependency>
   <!--mybatis-plus 注：这里使用了老版本3.0.5，和新版本的案例有些地方不同 -->
   <dependency>
       <groupId>com.baomidou</groupId>
       <artifactId>mybatis-plus-boot-starter</artifactId>
       <version>3.0.5</version>
   </dependency>
   ```

6. 在resource目录下的**application.properties**文件中添加数据库连接的配置。**注意url后面的参数**。

   ```properties
   # mysql 5
   spring.datasource.username=root
   spring.datasource.password=1234
   spring.datasource.url=jdbc:mysql://localhost:3306/mybatis_plus?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
   spring.datasource.driver-class-name=com.mysql.jdbc.Driver
   ```

7. 开始使用，使用了mybatis-plus后步骤非常简单：

   - 编写pojo实体类，并且使用lombok简化代码。

     ```java
     @Data
     @AllArgsConstructor
     @NoArgsConstructor
     public class User {
         private Long id;
         private String name;
         private Integer age;
         private String email;
     }
     ```

   - 完整的pojo实体类举例：

     ```java
     @Data
     @AllArgsConstructor
     @NoArgsConstructor
     public class User {
         @TableId(type = IdType.AUTO)
         private Long id;
         private String name;
         private Integer age;
         private String email;
     
         @Version
         private Integer version;
     
         @TableLogic
         private Integer deleted;
     
         @TableField(fill = FieldFill.INSERT)
         private Date createTime;
         @TableField(fill = FieldFill.INSERT_UPDATE)
         private Date updateTime;
     }
     ```
   
     
   
   - 创建一个mapper接口，然后继承基础类**BaseMapper<T>**，他代替了实体类，简化了大量查询的代码。
   
     ```java
     //注解@代表持久层Dao，注意在Main中添加@MapperScan
     @Repository
     public interface UserMapper extends BaseMapper<User> {
         //直接继承基本类BaseMapper<T>，并且自动完成所有的CRUD编写。
     }
     ```
   
   - 在**主启动类**中添加**包扫描**注解！
   
     ```java
     @MapperScan("com.ycsx.mapper")
     //不然会报错Positive matches:...
     ```
   
     
   
   - 直接在测试类中测试！这里演示查询全部：
   
     ```java
     @SpringBootTest
     class MybatisPlusApplicationTests {
         @Autowired
         private UserMapper userMapper;
     
         @Test
         void contextLoads() {
             //selectList方法中有一个参数Wrapper（条件构造器），查询全部填null
             List<User> users = userMapper.selectList(null);
             users.forEach(System.out::println);
         }
     }
     ```
   
     查询结果（控制台），成功：
   
     ```text
     User(id=1, name=Jone, age=18, email=test1@baomidou.com)
     User(id=2, name=Jack, age=20, email=test2@baomidou.com)
     User(id=3, name=Tom, age=28, email=test3@baomidou.com)
     User(id=4, name=Sandy, age=21, email=test4@baomidou.com)
     User(id=5, name=Billie, age=24, email=test5@baomidou.com)
     ```

简单的Mybatis-Plus项目测试就完成了。



------

## 使用Mybatis-Plus的便利功能

#### 配置日志 - 查看调试

​	为了可以看到我们的sql语句怎么执行的，需要添加配置日志。

​	配置日志为StdOutImpl，在application中添加如下内容：

```properties
# 配置日志
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```

再次执行上面的查询全部操作，可以看到控制台多了一些内容：

```text
Creating a new SqlSession
SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@221b77d7] was not registered for synchronization because synchronization is not active
JDBC Connection [HikariProxyConnection@104105431 wrapping com.mysql.jdbc.JDBC4Connection@23ea8830] will not be managed by Spring
==>  Preparing: SELECT id,name,age,email FROM user 
==> Parameters: 
<==    Columns: id, name, age, email
<==        Row: 1, Jone, 18, test1@baomidou.com
<==        Row: 2, Jack, 20, test2@baomidou.com
<==        Row: 3, Tom, 28, test3@baomidou.com
<==        Row: 4, Sandy, 21, test4@baomidou.com
<==        Row: 5, Billie, 24, test5@baomidou.com
<==      Total: 5
Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@221b77d7]
```

可以看到我们的执行的sql语句具体是什么，方便调试。





***

#### CRUD 插入 - 填充策略

​	插入新用户不设置主键id，选择让其自动填充。打印出来控制台的结果是：

```text
insert = 1;
user = User(id=1386931786126209026, name=张三, age=19, email=123456@123.com)
```

​	可以看到user.id自动填充上了id=1386931786126209026。再次插入查看id，得到id=1386932333336113153，发现id都会自动回填。他使用了**雪花算法（snowflake）**。

​	想在这种情况下自增id，只需要在实体类的id字段上添加注解`@TableId(type=IdType.AUTO)`，但是需要先在配置数据库中的主键id自增。（使用工具勾选即可），否则会报错`org.springframework.dao.DataIntegrityViolationException`。

​	再次添加，发现id=1386932333336113154，自增成功。

​	TypeId = 的枚举类型如下：

```java
public enum IdType {
    AUTO(0),			//自增
    NONE(1),			//无主键
    INPUT(2),			//手动输入
    ID_WORKER(3),		//默认的全局id
    UUID(4),			//全局唯一id => uuid
    ID_WORKER_STR(5);	//字符串表示法
}
```





***

#### CRUD 更新 - 自适应

测试代码：

```java
@Test
void test2() {
    User user = new User();
    user.setId(5L);
    user.setName("5号待修改");
    int i = userMapper.updateById(user);
    System.out.println("i = " + i);
    System.out.println("user = " + user);
}
```

控制台结果：

```text
i = 1
user = User(id=5, name=5号待修改, age=null, email=null)
```

可以看到虽然user信息不完善，但更新语句根据id修改了填写的name。

**他会根据条件自动拼接动态SQL。**

注意这里的UpdateById虽然名称是根据id更新，但是传参数传入的是一个泛型（对象）。





***

#### 自动填充 - 时间规范

阿里巴巴开发手册中关于时间的操作要求一定是自动化完成的。

所有的数据库的表都需要配置上下面两个：

- gmt_create  - 创建时间
- gmt_modified - 修改时间

**传统规范化的步骤：**

1. 在数据库中添加如上的两个字段（可以使用别的名字，自己辨识）

```sql
ALTER TABLE `user`
ADD COLUMN `create_time`  datetime NULL DEFAULT CURRENT_TIMESTAMP AFTER `email`,
ADD COLUMN `update_time`  datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER `create_time`;
```

2. 添加实体类新的字段，测试内容。
3. 测试完成后删除默认值和自动更新，下面使用代码注解的方式。

**代码方式实现（注3.0.5版本）：**

4. 修改上面新增的实体类，添加注解：

```java
public class User {
    //***其余成员省略了
	//INSERT代表插入时，INSERT_UPDATE代表插入和更新时。
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
```

5. 编写一个自定义**实现类** MyMetaObjectHandler，继承**接口**MetaObjectHandler并重写方法：

```java
@Slf4j      //日志
@Component  //组件 - 注解作用：添加到IOC容器中
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override   //插入填充策略
    public void insertFill(MetaObject metaObject) {
        log.info("执行插入操作......");
        this.setFieldValByName("createTime",new Date(),metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }

    @Override   //更新填充策略
    public void updateFill(MetaObject metaObject) {
        log.info("执行更新操作......");
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
}
```

6. 测试插入和更新。看到数据库中的内容都能根据当前时间更新，成功。





***

#### 乐观锁 - 并发

[引用官网](https://mp.baomidou.com/guide/interceptor-optimistic-locker.html#optimisticlockerinnerinterceptor)关于乐观锁的描述：

> 当要更新一条记录的时候，希望这条记录没有被别人更新
> 乐观锁实现方式：
>
> > - 取出记录时，获取当前version
> > - 更新时，带上这个version
> > - 执行更新时， set version = newVersion where version = oldVersion
> > - 如果version不对，就更新失败

乐观锁相对于悲观锁，他总是乐观的，任何时候都认为没有问题，不上锁处理。出现问题再次更新值测试。

**实现步骤（3.0.5版本）：**

1. 数据库中添加version字段，设置默认值为1，这里补上了上面内容的备注：

```sql
ALTER TABLE `user`
MODIFY COLUMN `create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间' AFTER `email`,
MODIFY COLUMN `update_time`  datetime NULL DEFAULT NULL COMMENT '修改时间' AFTER `create_time`,
ADD COLUMN `version`  int(10) NULL DEFAULT 1 COMMENT '乐观锁' AFTER `email`;
```

2. 实体类添加version字段，@Version代表他是一个乐观锁。

```java
@Version
private Integer version;
```

3. 注册组件
   - 创建一个config包，创建MybatisPlusConfig类；
   - 添加注解；
   - 注册乐观锁插件

```java
@MapperScan("com.ycsx.mapper")  //自动扫描
@EnableTransactionManagement    //自动管理事务（默认开启）
@Configuration                  //代表配置类的注解
public class MybatisPlusConfig {
    //注册乐观锁插件
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }
}
```

4. 测试乐观锁应用的情形：

```java
//测试乐观锁在并发情况下失败的情况
@Test
void test3(){
    User user = userMapper.selectById(2L);
    user.setName("2号乐观锁更新111");
    user.setEmail("test11111@test.com");
    User user2 = userMapper.selectById(2L);
    user2.setName("2号乐观锁更新222");
    user2.setEmail("test22222@test.com");
    userMapper.updateById(user2);
    userMapper.updateById(user);
}
```

这样user2会成功，而user不会被更新。他会在操作的时候检查version是否一致，如果有其他更新先提交，他不会进行操作，控制台信息如下：

```text
==>  Preparing: UPDATE user SET name=?, age=?, email=?, version=?, create_time=?, update_time=? WHERE id=? AND version=? 
==> Parameters: 2号乐观锁更新222(String), 20(Integer), test22222@test.com(String), 2(Integer), 2021-04-27 16:21:37.0(Timestamp), 2021-04-28 09:01:13.744(Timestamp), 2(Long), 1(Integer)
<==    Updates: 1
```

```text
==>  Preparing: UPDATE user SET name=?, age=?, email=?, version=?, create_time=?, update_time=? WHERE id=? AND version=? 
==> Parameters: 2号乐观锁更新111(String), 20(Integer), test11111@test.com(String), 2(Integer), 2021-04-27 16:21:37.0(Timestamp), 2021-04-28 09:01:13.874(Timestamp), 2(Long), 1(Integer)
<==    Updates: 0
```

在操作之前会获取version数值，并且+1，在更新操作时，提交更新数据的时候同时使version的数值+1，这样如果有未完成提交的更新操作，这时候去进行提交的话，语句中的`where id=? and version=?`会进行判断，会发现version并不相同，所以不会成功。





***

#### CRUD 查询 - 批量和条件

1. **批量查询**

```java
//批量查询
@Test
void test4(){
    List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
    users.forEach(System.out::println);
}
```

控制台的SQL语句

```text
==>  Preparing: SELECT id,name,age,email,version,create_time,update_time FROM user WHERE id IN ( ? , ? , ? ) 
==> Parameters: 1(Integer), 2(Integer), 3(Integer)
```



2. **条件查询**

```java
//条件查询
@Test
void test5(){
    //自定义条件
    HashMap<String, Object> map = new HashMap<>();
    map.put("name","Tom");
    map.put("age",28);
    List<User> users = userMapper.selectByMap(map);
    users.forEach(System.out::println);
}
```

```text
==>  Preparing: SELECT id,name,age,email,version,create_time,update_time FROM user WHERE name = ? AND age = ? 
==> Parameters: Tom(String), 28(Integer)
```

他会自动的添加上`WHERE name = ? AND age = ? `条件，**严格条件匹配**。



3. **分页查询**

- limit 分页
- pageHelper 插件
- **<u>Mybatis - Plus 分页插件</u>**

使用 Mybatis-Plus 提供的插件之前，需要先去config类中注册插件，他会提供一个Page<T>对象给我们使用。

```java
//在 MybatisPlusConfig 中注册分页插件
@Bean
public PaginationInterceptor paginationInterceptor(){
    return new PaginationInterceptor();
}
```

然后直接使用BaseMapper提供的selectPage方法就可以了：

```java
//分页查询
@Test
void test6(){
    //参数为(当前页*1开始, 页面大小)
    Page<User> page = new Page<>(1,5);
    userMapper.selectPage(page,null);
    page.getRecords().forEach(System.out::println);
    System.out.println(page.getTotal()); //总条数
}
```

```text
==>  Preparing: SELECT id,name,age,email,version,create_time,update_time FROM user LIMIT 0,5 
```

Sql语句如上。



4. **模糊查询**

会使用到之前提到的参数Wrapper，在之后讲解。





***

#### CRUD 删除 - 逻辑删除

- **通过编号删除**

  最基本的操作，直接调用`deleteById`方法即可。

- **批量删除**

  调用`deleteBetchIds`，参数借助Arrays.asList(1,2,3,...)实现。

- **通过map删除**

  和查询类似使用的是`deleteByMap`方法，例如`map.put("name","Tom")`他会添加上`where name = ?`去严格匹配有无相关的内容。



**逻辑删除**

	* 物理删除：从数据库中直接移除。
	* 逻辑删除：在数据库中没有被移除，通过一个变量deleted来让它失效。

​	相对于物理删除（直接删除）的直接从数据库中移除，他起到一个类似于回收站的作用，用于确保数据不会给误操作删除，管理员可以查看被删除的记录！

​	通过在数据库中添加字段`deleted`实现，下面是具体步骤：

1. 数据库中添加对应deleted字段（int(1)，默认0）：

```sql
ALTER TABLE `user`
ADD COLUMN `deleted`  int(1) NULL DEFAULT 0 COMMENT '逻辑删除' AFTER `version`;
```

2. 实体类添加对应成员，使用@TableLogic代表逻辑删除：

```java
@TableLogic
private Integer deleted;
```

3. 在config类中注册逻辑删除组件。（3.0.5版本，3.3.0版本之后无需，注意参考官网）

```java
//注册逻辑删除
@Bean
public ISqlInjector sqlInjector(){
    return new LogicSqlInjector();
}
```

4. 在资源类中配置默认值

```properties
# 配置逻辑删除
mybatis-plus.global-config.db-config.logic-delete-value=1
mybatis-plus.global-config.db-config.logic-not-delete-value=0
```

5. 测试删除

使用之前测试类中的删除操作的例子，可以看到运行结果： 

```text
==>  Preparing: UPDATE user SET deleted=1 WHERE id=? AND deleted=0 
==> Parameters: 5(Long)
<==    Updates: 1
```

可以看到执行的并不是**删除**操作而是**更新**操作，记录依旧在数据库中了，（被删除的deleted字段数值为1）。

这次执行查询操作来看看结果：

```text
==>  Preparing: SELECT id,name,age,email,version,deleted,create_time,update_time FROM user WHERE deleted=0 
```

可以看到他自动拼接了`where deleted = 0`这个字段。



***

#### 性能分析 - 解决慢sql

有些慢sql，需要在超过运行时间的时候停止sql语句。

**性能分析插件**（3.0.5版本）

```java
//SQL执行效率插件
@Bean
@Profile({"dev","test"})//设置只有test测试环境，dev开发环境下才会开启。
public PerformanceInterceptor performanceInterceptor(){
    PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
    performanceInterceptor.setMaxTime(1);   //设置最大sql时间
    performanceInterceptor.setFormat(true); //显示格式化
    return performanceInterceptor;
}
```

```pro
# 设置开发环境
spring.profiles.active=dev
```

设置最大sql时间的单位是ms，可以看到上面设置的是1ms。我们执行查询全部：

```text
 Time：26 ms - ID：com.ycsx.mapper.UserMapper.selectList
Execute SQL：
    SELECT
        id,
        name,
        age,
        email,
        version,
        deleted,
        create_time,
        update_time 
    FROM
        user 
    WHERE
        deleted=0
```

这是格式化显示的结果，可以看到我们的执行时间在最前面标明是26ms。于是下面报红/报错了，我们可以根据需要调整成1000ms（1秒）。

  

***

#### 条件构造器 Wrapper

复杂的sql需要用到wrapper。

首先我们在test包下新建一个WrapperTest类来进行相关的使用测试：

```java
@SpringBootTest
public class WrapperTest {
    @Autowired
    private UserMapper userMapper;
    //以上从MybatisPlusApplicationTests复制即可
}
```

以下是举例：



**条件查询**

```java
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
```

其中`isNotNull`和`ge`方法是**非空**和**大于等于**判断条件。

控制台输出的部分信息：

```text
 Time：29 ms - ID：com.ycsx.mapper.UserMapper.selectList
Execute SQL：
    SELECT
        id,
        name,
        age,
        email,
        version,
        deleted,
        create_time,
        update_time 
    FROM
Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@18907af2]
        user 
    WHERE
        deleted=0 
        AND name IS NOT NULL 
        AND email IS NOT NULL 
        AND age >= 12
```

可以看到在sql语句后拼接上了where语句选择条件。



**单一查询**

```java
//查询名字=XXX的用户（单一）
@Test
void test02(){
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.eq("name","Tom");
    User user = userMapper.selectOne(wrapper);
    System.out.println(user);
}
```

eq = equal，相等条件。

selectOne()，查询单一用户，如果要查询多个使用selectList方法。

控制台结果：

```text
(...)
	FROM
        user 
    WHERE
        deleted=0 
        AND name = 'Tom'
User(id=3, name=Tom, age=5, email=...
```

可以看到拼接了name=‘Tom’的条件。



**查询范围**

```java
//查询年龄范围在20~30
@Test
void test03(){
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.between("age",20,30); //区间
    Integer count = userMapper.selectCount(wrapper); //结果数
    System.out.println(count);
}
```

between，查询区间。

selectCount()，查询结果数。

控制台结果：

```text
Execute SQL：
    SELECT
        COUNT(1) 
    FROM
        user 
    WHERE
        deleted=0 
        AND age BETWEEN 20 AND 30
```



**模糊查询**

```java
//模糊查询，名字中不包含e，邮箱首字母是t
@Test
void test04(){
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.notLike("name","e")
            .likeRight("email","t");
    List<Map<String, Object>> maps = userMapper.selectMaps(wrapper);
    maps.forEach(System.out::println);
}
```

notLike，不包含xxx，等同于`not like '%(xxx)%'`；

likeRight，左Like，等同于`like 'xxx%'`。

selectMaps()，(某评论言：名字是Map，本质是List，自行甄别...)

控制台部分结果：

```text
...
FROM
    user 
WHERE
    deleted=0 
    AND name NOT LIKE '%e
    AND email LIKE 't%'
...
```



**连接查询（内查询）**

一个sql里嵌一个sql：

```java
//连接查询（内查询）
@Test
void test05() {
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    // id 在子查询中查询出来，查询自行拼接条件。（id<3）
    wrapper.inSql("id","select id from user where id<3");
    List<Object> obj = userMapper.selectObjs(wrapper);
    obj.forEach(System.out::println);
}
```

控制台输出sql语句：

```text
Execute SQL：
    SELECT
        id,
        name,
        age,
        email,
        version,
        deleted,
        create_time,
        update_time 
    FROM
        user 
    WHERE
        deleted=0 
        AND id IN (
            select
                id 
            from
                user
            where
                id<3
        )
```

可以看到使用inSql方法他在后面拼接了AND in ...语句内查询。



**排序**

```java
//根据id排序，Asc升序，Desc降序
@Test
void test06(){
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.orderByDesc("id");
    userMapper.selectList(wrapper).forEach(System.out::println);
}
```



还有更多的Wrapper用法，可以查看官方文档。

[Mybatis-Plus官网](https://mp.baomidou.com/)



***

#### 代码生成器

[Mybatis-Plus：代码生成器](https://mp.baomidou.com/guide/generator.html#%E4%BD%BF%E7%94%A8%E6%95%99%E7%A8%8B)



至此，Mybatis_Plus基本功能完成！

