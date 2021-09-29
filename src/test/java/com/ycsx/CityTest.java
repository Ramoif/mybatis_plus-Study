package com.ycsx;

import com.ycsx.mapper.CityMapper;
import com.ycsx.pojo.City;
import com.ycsx.utils.CsvUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;

public class CityTest {
    @Autowired
    private CityMapper cityMapper;

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
}
