package com.springboot.p2061200719.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("student")
public class Student implements Serializable {
    @TableId(value = "student_number",type = IdType.AUTO)
    private Integer studentNumber;     //学号
    private String name;               //姓名
    private Character sex;             //性别
    private String grade;              //分数
    private String phone;              //手机号码
    private String major;              //专业
}
