package com.ycsx;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ycsx.mapper.CityMapper;
import com.ycsx.mapper.UserMapper;
import com.ycsx.pojo.City;
import com.ycsx.pojo.User;
import com.ycsx.utils.CsvUtil;
import com.ycsx.utils.TimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;

@SpringBootTest
class MybatisPlusApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CityMapper cityMapper;

    //测试双引号
    @Test
    void testcity05() {
        String str1 = new String("\"新疆\",\"伊宁\",\"0999\",\"835000\"");
        System.out.println(str1);
        String str2 = str1.replace("\"", "");
        System.out.println(str2);
    }


    /*读取csv测试*/
    @Test
    void citytest03() {
        //需要用到的初始化类
        ArrayList<City> cities = new ArrayList<>();
        ArrayList<String> arrayList = CsvUtil.readCsv("d://in.csv");
        String str = null;
        String teleTemp = null;

        //读取测试
        //System.out.println(arrayList.get(1));

        //打印输出
        System.out.println("读取到的文件长度为：" + arrayList.size());
        for (int i = 1; i < arrayList.size(); i++) {
            //打印每一行
            //System.out.println(arrayList.get(i));

            //处理 字符串
            str = arrayList.get(i).replace("\"", "");

            //打印去掉双引号的字符串
            //System.out.println(str);

            //分隔 字符串
            String[] splitStr = str.split(",");

            for (int i1 = 0; i1 < splitStr.length; i1++) {
                //判断当前值是否非空，用上一次非空值填充
                if (i1 == 2) {
                    if (splitStr[i1] == null || splitStr[i1].equals("")) {
                        splitStr[i1] = teleTemp;
                        //打印出现空值的位置
                        //System.out.println("发现空值，已经填充完成。");
                    }
                    //不足4位补足4位
                    if (splitStr[i1].length() < 4) {
                        //左填充使用方法
                        StringBuffer sb = new StringBuffer();
                        sb.append(0).append(splitStr[i1]);
                        splitStr[i1] = sb.toString();
                    }
                }
                //打印每一行已经分隔的字符串
                //System.out.println(i1 + "号元素是：" + splitStr[i1]);
            }

            City cityTemp = new City();
            //装入暂存对象
            //  private Long rowNo;
            //  private String prov;
            //  private String city;
            //  private String tele;
            //  private String post;
            //  private Date currentTime;
            cityTemp.setRowNo((long) i);
            cityTemp.setProv(splitStr[0]);
            cityTemp.setCity(splitStr[1]);
            cityTemp.setTele(splitStr[2]);
            cityTemp.setPost(splitStr[3]);
            cityTemp.setCurTime(new Date());
            //写入对象数组
            cities.add(cityTemp);

            //保存当前的tele值
            teleTemp = splitStr[2];
        }

        //打印保存的对象数组
        for (int i = 0; i < cities.size(); i++) {
            System.out.println(cities.get(i));
        }
        System.out.println("新建对象数组长度为：" + cities.size());

        /*保存到数据库中*/
        //使用插入操作
        for (int i = 0; i < cities.size(); i++) {
            int insert = cityMapper.insert(cities.get(i));
        }
    }

    //测试双引号
    @Test
    void testcity04() {
        String str1 = new String("\"新疆\",\"伊宁\",\"0999\",\"835000\"");
        System.out.println(str1);
        String str2 = str1.replace("\"", "");
        System.out.println(str2);
    }


    /*输出csv文件测试*/
    //读取数据库city表，修改内容后，保存到桌面文件csv
    @Test
    void cityTest01() {
        List<City> citys = cityMapper.selectList(null);
        String last = citys.get(0).getTele();
        //判断空值
        if (last == null) {
            System.out.println("error!");
        }
        //邮编保留
        String strNo = null;
        //逐行扫描，保存上一行值
        for (int i = 0; i < citys.size(); i++) {
            //填充编码
            if (citys.get(i).getTele() == null) {
                citys.get(i).setTele(last);
            }
            strNo = citys.get(i).getTele();
            //填充区号0
            if (strNo.length() < 4) {
                //左填充使用方法
                StringBuffer sb = new StringBuffer();
                sb.append(0).append(strNo);
                strNo = sb.toString();
                citys.get(i).setTele(strNo);
            }
            last = citys.get(i).getTele();
        }
        for (int i = 0; i < citys.size(); i++) {
            System.out.println(citys.get(i));
        }
        System.out.println("开始打印输出csv文件 . . . . . .");
        String[] titles = new String[]{"编号", "省", "城市", "区号", "邮编", "当前时间"};
        //  private Long rowNo;
        //  private String prov;
        //  private String city;
        //  private String tele;
        //  private String post;
        //  private Date currentTime;
        String[] propertys = new String[]{"rowNo", "prov", "city", "tele", "post", "currentTime"};
        try {
            CsvUtil.exportCsv(titles, propertys, citys);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("打印结束，桌面生成了test.csv文件。");

    }

    //导出数据

    /**
     * 导出csv文件
     *
     * @param titles    csv格式头文
     * @param propertys 需要导出的数据实体的属性，注意与title一一对应
     * @param list      需要导出的对象集合
     */

/*      @Test
        public static <T> String exportCsv(String[] titles,String[] propertys,List<T> list) throws IOException, IllegalArgumentException, IllegalAccessException{
            File file = new File("d:\\test.csv");
            //构建输出流，同时指定编码
            OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(file), "gbk");

            //csv文件是逗号分隔，除第一个外，每次写入一个单元格数据后需要输入逗号
            for(String title : titles){
                ow.write(title);
                ow.write(",");
            }
            //写完文件头后换行
            ow.write("\r\n");
            //写内容
            for(Object obj : list){
                //利用反射获取所有字段
                Field[] fields = obj.getClass().getDeclaredFields();
                for(String property : propertys){
                    for(Field field : fields){
                        //设置字段可见性
                        field.setAccessible(true);
                        if(property.equals(field.getName())){
                            ow.write(field.get(obj).toString());
                            ow.write(",");
                            continue;
                        }
                    }
                }
                //写完一行换行
                ow.write("\r\n");
            }
            ow.flush();
            ow.close();
            return "0";
        }*/


    //City 测试类
    @Test
    void cityTest02() {

    }


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
