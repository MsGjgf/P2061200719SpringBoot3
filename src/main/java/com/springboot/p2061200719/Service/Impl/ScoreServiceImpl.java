package com.springboot.p2061200719.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.p2061200719.Entity.*;
import com.springboot.p2061200719.Mapper.ScoreMapper;
import com.springboot.p2061200719.Service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询成绩信息
     * @param currentPage
     * @param pageSize
     * @param courseName
     * @param studentName
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public JsonResult<Page<Map<String, Object>>> findPage(Integer currentPage, Integer pageSize, String courseName, String studentName) throws JsonProcessingException {
        //1.定义一个数组用于存放业务+key字段
        String[] scoreKeyArr = new String[]{
                "score:currentPage_"+currentPage+":pageSize_"+pageSize+":",
                "studentName_:",
                "courseName_:"
        };
        //2.定义条件构造器，用于后面的字段查询
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        //3.判断关键字字段是否为空，不为空则将关键字拼接在相应的key后面，并添加至条件构造器
        if (!studentName.isEmpty()){
            scoreKeyArr[1] = "studentName_"+studentName+":";
            queryWrapper.like("name",studentName);
        }
        if (!courseName.isEmpty()){
            scoreKeyArr[2] = "courseName_"+courseName+":";
            queryWrapper.like("course_name",courseName);
        }
        //4.将数组进行拼接成字符串业务+key
        String scoreListKey = String.join("", scoreKeyArr);
        //5.从redis中查找业务+key
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        String pageInfoScoreJson = valueOperations.get(scoreListKey);
        //6.如果业务key存在则直接将value值返回（数据取自Redis）
        if (!Objects.equals(pageInfoScoreJson, null)){
            System.out.println("==============================数据来自Redis缓存==================================");
            Page<Map<String,Object>> page = new ObjectMapper().readValue(pageInfoScoreJson, Page.class);
            return new JsonResult<>(page);
        }
        //7.如果业务key不存在，则查询数据库
        Page<Map<String,Object>> page = new Page<>(currentPage,pageSize);   //1).创建分页
        scoreMapper.findPage(page,courseName,studentName);                  //2).调用mapper接口执行xml查询数据库
        //8.如果数据库没有对应的数据，则将空数据写入redis，并设置60s有效期（将空数据写入redis为了防止频繁访问数据库）
        if (page.getRecords().isEmpty()){
            valueOperations.set(scoreListKey,new ObjectMapper().writeValueAsString(page),60, TimeUnit.SECONDS);
        }else {
            //9.如果数据库有对应的数据，则将数据写入redis
            valueOperations.set(scoreListKey,new ObjectMapper().writeValueAsString(page));
        }
        //10.返回数据（数据取自MySQL）
        System.out.println("==============================数据来自MySQL数据库==================================");
        return new JsonResult<>(page);
    }

    /**
     * 添加成绩信息
     * @param score
     * @return
     */
    @Override
    public JsonResult<Map<String, Object>> add(Score score) {
        //清除redis缓存
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("score:*")));
        try {
            scoreMapper.insert(score);
            return new JsonResult<>("200","添加成功！");
        }catch(Exception e){
            return new JsonResult<>("0","添加失败！");
        }
    }

    /**
     * 更新成绩信息
     * @param studentNumber
     * @param courseNumber
     * @param score
     * @return
     */
    @Override
    public JsonResult<Map<String, Object>> upd(Integer studentNumber, Integer courseNumber, Float score) {
        //清除redis缓存
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("score:*")));
        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("s_id",studentNumber)
                .eq("c_id",courseNumber);
        Score score1 = new Score();
        score1.setScore(score);
        try {
            int result = scoreMapper.update(score1,queryWrapper);
            System.out.println(result);
            return new JsonResult<>("200","修改成功！");
        }catch (Exception e){
            return new JsonResult<>("0","修改失败！");
        }
    }

    /**
     * 删除成绩信息
     * @param studentNumber
     * @param courseNumber
     * @return
     */
    @Override
    public JsonResult<Map<String, Object>> del(Integer studentNumber, Integer courseNumber) {
        //清除redis缓存
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("score:*")));
        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("c_id",courseNumber)
                .eq("s_id",studentNumber);
        try {
            scoreMapper.delete(queryWrapper);
            return new JsonResult<>("200","删除成功！");
        }catch(Exception e){
            return new JsonResult<>("0","删除失败！");
        }
    }
}
