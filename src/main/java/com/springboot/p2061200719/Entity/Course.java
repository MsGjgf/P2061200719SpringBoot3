package com.springboot.p2061200719.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("course")
public class Course implements Serializable {
    @TableId(value = "course_number",type = IdType.AUTO)
    private Integer courseNumber;      //课程编号
    private String courseName;         //课程名称
    private Integer credit;            //学分
    private String teacher;            //任课老师
    private Integer period;            //学时数
}
