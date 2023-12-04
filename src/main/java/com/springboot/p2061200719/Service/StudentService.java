package com.springboot.p2061200719.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.p2061200719.Entity.JsonResult;
import com.springboot.p2061200719.Entity.Student;

import java.util.List;
import java.util.Map;

public interface StudentService extends IService<Student> {

    JsonResult<Page<Student>> findPage(Integer currentPage, Integer pageSize, String search) throws JsonProcessingException;

    JsonResult<Map<String, Object>> add(Student student);

    JsonResult<Map<String, Object>> upd(Student student);

    JsonResult<Map<String, Object>> del(Integer studentNumber);

    JsonResult<List<Student>> getStudentInfo() throws JsonProcessingException;
}
