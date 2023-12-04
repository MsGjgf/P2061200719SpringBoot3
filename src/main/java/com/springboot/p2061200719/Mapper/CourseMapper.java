package com.springboot.p2061200719.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.p2061200719.Entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
