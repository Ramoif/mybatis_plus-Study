package com.ycsx.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
    //若数据库中字段前有_下划线，则后一个字母大写

    private Long rowNo;

    private String prov;
    private String city;
    private String tele;
    private String post;

    private Date curTime;
}
