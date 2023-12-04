package com.springboot.p2061200719.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.p2061200719.Entity.Course;
import com.springboot.p2061200719.Entity.JsonResult;

import java.util.List;
import java.util.Map;

public interface CourseService extends IService<Course> {
    JsonResult<Page<Course>> findPage(Integer currentPage, Integer pageSize, String search) throws JsonProcessingException;

    JsonResult<Map<String, Object>> add(Course course);

    JsonResult<Map<String, Object>> upd(Course course);

    JsonResult<Map<String, Object>> del(Integer courseNumber);

    JsonResult<List<Course>> getCourseInfo() throws JsonProcessingException;
}
