package com.springboot.p2061200719.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.p2061200719.Entity.Course;
import com.springboot.p2061200719.Entity.JsonResult;
import com.springboot.p2061200719.Mapper.CourseMapper;
import com.springboot.p2061200719.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取所有课程信息
     * @return
     */
    @Override
    public JsonResult<List<Course>> getCourseInfo() throws JsonProcessingException {
        //1.定义一个key字段
        String key = "courseInfo";
        //2.从redis中查找key
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        String listInfoCourseJson = valueOperations.get(key);
        //3.如果key存在则直接将value值返回（数据取自Redis）
        if (!Objects.equals(listInfoCourseJson, null)){
            System.out.println("==============================数据来自Redis缓存==================================");
            List<Course> list = new ObjectMapper().readValue(listInfoCourseJson,List.class);
            return new JsonResult<>(list);
        }
        //4.如果业务key不存在，则查询数据库
        List<Course> list = courseMapper.selectList(null);
        //5.如果数据库没有对应的数据，则将空数据写入redis，并设置60s有效期（将空数据写入redis为了防止频繁访问数据库）
        if (list.isEmpty()){
            valueOperations.set(key,new ObjectMapper().writeValueAsString(list),60, TimeUnit.SECONDS);
        }else {
            //6.如果数据库有对应的数据，则将数据写入redis
            valueOperations.set(key,new ObjectMapper().writeValueAsString(list));
        }
        //7.返回数据（数据取自MySQL）
        System.out.println("==============================数据来自MySQL数据库==================================");
        return new JsonResult<>(list);
    }

    /**
     * 分页查询课程信息
     * @param currentPage
     * @param pageSize
     * @param search
     * @return
     * @throws JsonProcessingException
     */
    public JsonResult<Page<Course>> findPage(Integer currentPage,Integer pageSize,String search) throws JsonProcessingException {
        //1.定义业务key数组
        String[] courseKeyArr = new String[]{
                "course:currentPage_"+currentPage+":pageSize_"+pageSize+":",
                "Name_:"
        };
        //2.定义条件构造器
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        //3.对搜索字段判空
        if (!search.isEmpty()){
            courseKeyArr[1] = "Name_"+search+":";
            queryWrapper.like("course_name",search);
        }
        //4.拼接业务key
        String courseListKey = String.join("",courseKeyArr);
        //5.查找redis是否存在key
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        String pageInfoCourseJson = valueOperations.get(courseListKey);
        //6.如果key存在则直接将value返回（数据来自Redis缓存）
        if (!Objects.equals(pageInfoCourseJson,null)){
            System.out.println("==============================数据来自Redis缓存==================================");
            Page<Course> page = new ObjectMapper().readValue(pageInfoCourseJson,Page.class);
            return new JsonResult<>(page);
        }
        //7.如果key不存在，则查询数据库
        Page<Course> page = new Page<>(currentPage,pageSize);
        courseMapper.selectPage(page,queryWrapper);
        //8.如果数据库也没有数据，则将整个空数据写入redis，设置有效期
        if (page.getRecords().isEmpty()){
            valueOperations.set(courseListKey,new ObjectMapper().writeValueAsString(page),60, TimeUnit.SECONDS);
        }else {
        //9.如果数据库有数据，则将数据写入redis
            valueOperations.set(courseListKey,new ObjectMapper().writeValueAsString(page));
        }
        //10.返回数据（数据来自MySQL数据库）
        System.out.println("==============================数据来自MySQL数据库==================================");
        return new JsonResult<>(page);
    }

    /**
     * 添加课程信息
     * @param course
     * @return
     */
    public JsonResult<Map<String,Object>> add(Course course){
        //清除redis缓存
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("course:*")));
        //清除redis 课程所有信息
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("courseInfo")));
        try {
            courseMapper.insert(course);
            return new JsonResult<>("200","添加成功！");
        }catch (Exception e){
            return new JsonResult<>("0","添加失败！");
        }
    }

    /**
     * 更新课程信息
     * @param course
     * @return
     */
    public JsonResult<Map<String,Object>> upd(Course course){
        //清除redis course：*
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("course:*")));
        //清除redis score：的信息（存在外键）
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("score:*")));
        //清除redis 课程所有信息
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("courseInfo")));
        try {
            courseMapper.updateById(course);
            return new JsonResult<>("200","修改成功！");
        }catch (Exception e){
            return new JsonResult<>("0","修改失败！");
        }
    }

    /**
     * 删除课程信息
     * @param courseNumber
     * @return
     */
    public JsonResult<Map<String,Object>> del(Integer courseNumber){
        //清除redis course:*
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("course:*")));
        //清除redis score：的信息（存在外键）
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("score:*")));
        //清除redis 课程所有信息
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("courseInfo")));
        try {
            courseMapper.deleteById(courseNumber);
            return new JsonResult<>("200","删除成功！");
        }catch (Exception e){
            return new JsonResult<>("0","删除失败！");
        }
    }
}
