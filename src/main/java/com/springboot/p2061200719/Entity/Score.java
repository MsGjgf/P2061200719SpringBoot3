package com.springboot.p2061200719.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("score")
public class Score implements Serializable {
    @MppMultiId
    @TableField(value = "c_id")
    private Integer courseNumber;            //课程编号
    @MppMultiId
    @TableField(value = "s_id")
    private Integer studentNumber;            //学号
    private Float score;            //分数
}
