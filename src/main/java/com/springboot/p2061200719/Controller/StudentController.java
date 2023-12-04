package com.springboot.p2061200719.Controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.p2061200719.Entity.*;
import com.springboot.p2061200719.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/getStudentInfo")
    public JsonResult<List<Student>> getStudentInfo() throws JsonProcessingException {
        return studentService.getStudentInfo();
    }

    @GetMapping("/findPage")
    public JsonResult<Page<Student>> findPage(@RequestParam Integer currentPage, @RequestParam Integer pageSize, @RequestParam String search) throws JsonProcessingException {
        return studentService.findPage(currentPage,pageSize,search);
    }

    @PostMapping("/add")
    public JsonResult<Map<String,Object>> add(@RequestBody Student student){
        return studentService.add(student);
    }

    @PutMapping("/upd")
    public JsonResult<Map<String,Object>> upd(@RequestBody Student student){
        return studentService.upd(student);
    }

    @DeleteMapping("/del")
    public JsonResult<Map<String,Object>> del(@RequestParam Integer studentNumber){
        return studentService.del(studentNumber);
    }
}
