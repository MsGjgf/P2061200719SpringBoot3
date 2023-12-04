package com.springboot.p2061200719.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.p2061200719.Entity.JsonResult;
import com.springboot.p2061200719.Entity.Score;

import java.util.Map;

public interface ScoreService extends IService<Score> {
    JsonResult<Page<Map<String, Object>>> findPage(Integer currentPage, Integer pageSize, String courseName, String studentName) throws JsonProcessingException;

    JsonResult<Map<String, Object>> add(Score score);

    JsonResult<Map<String, Object>> upd(Integer studentNumber, Integer courseNumber, Float score);

    JsonResult<Map<String, Object>> del(Integer studentNumber, Integer courseNumber);
}
