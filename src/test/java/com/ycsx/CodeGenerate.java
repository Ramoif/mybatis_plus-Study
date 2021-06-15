package com.ycsx;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;

public class CodeGenerate {
    public static void main(String[] args) {
        //1.新建一个自动生成器
        AutoGenerator mpg = new AutoGenerator();
        //2.全局配置，注意是generator包
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setAuthor("ycsx");
        globalConfig.setOpen(false);

        //3.数据源配置

        //4.包配置

        //执行
        mpg.execute();
    }
}
