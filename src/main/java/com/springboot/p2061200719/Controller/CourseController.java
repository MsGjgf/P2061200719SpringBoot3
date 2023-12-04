package com.springboot.p2061200719.Controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.p2061200719.Entity.Course;
import com.springboot.p2061200719.Entity.JsonResult;
import com.springboot.p2061200719.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/getCourseInfo")
    public JsonResult<List<Course>> getCourseInfo() throws JsonProcessingException {
        return courseService.getCourseInfo();
    }

    @GetMapping("/findPage")
    public JsonResult<Page<Course>> findPage(@RequestParam Integer currentPage, @RequestParam Integer pageSize, @RequestParam String search) throws JsonProcessingException {
        return courseService.findPage(currentPage,pageSize,search);
    }

    @PostMapping("/add")
    public JsonResult<Map<String,Object>> add(@RequestBody Course course){
        return courseService.add(course);
    }

    @PutMapping("/upd")
    public JsonResult<Map<String,Object>> upd(@RequestBody Course course){
        return courseService.upd(course);
    }

    @DeleteMapping("/del")
    public JsonResult<Map<String,Object>> del(@RequestParam Integer courseNumber){
        return courseService.del(courseNumber);
    }
}
