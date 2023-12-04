package com.springboot.p2061200719.Controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.p2061200719.Entity.JsonResult;
import com.springboot.p2061200719.Entity.Score;
import com.springboot.p2061200719.Service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @GetMapping("findPage")
    public JsonResult<Page<Map<String,Object>>> findPage(
            @RequestParam Integer currentPage,
            @RequestParam Integer pageSize,
            @RequestParam String courseName,
            @RequestParam String studentName
    ) throws JsonProcessingException {
        return scoreService.findPage(currentPage,pageSize,courseName,studentName);
    }

    @PostMapping("/add")
    public JsonResult<Map<String,Object>> add(@RequestBody Score score){
        return scoreService.add(score);
    }

    @PutMapping("/upd")
    public JsonResult<Map<String,Object>> upd(
            @RequestParam Integer studentNumber,
            @RequestParam Integer courseNumber,
            @RequestParam Float score
    ){
        return scoreService.upd(studentNumber,courseNumber,score);
    }

    @DeleteMapping("/del")
    public JsonResult<Map<String,Object>> del(@RequestParam Integer studentNumber,@RequestParam Integer courseNumber){
        return scoreService.del(studentNumber,courseNumber);
    }
}
