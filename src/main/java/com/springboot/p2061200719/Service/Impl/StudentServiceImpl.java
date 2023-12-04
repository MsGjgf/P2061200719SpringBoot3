package com.springboot.p2061200719.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.p2061200719.Entity.JsonResult;
import com.springboot.p2061200719.Entity.Student;
import com.springboot.p2061200719.Mapper.StudentMapper;
import com.springboot.p2061200719.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取所有学生信息
     * @return
     */
    @Override
    public JsonResult<List<Student>> getStudentInfo() throws JsonProcessingException {
        //1.定义一个key字段
        String key = "studentInfo";
        //2.从redis中查找key
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        String listInfoStudentJson = valueOperations.get(key);
        //3.如果key存在则直接将value值返回（数据取自Redis）
        if (!Objects.equals(listInfoStudentJson, null)){
            System.out.println("==============================数据来自Redis缓存==================================");
            List<Student> list = new ObjectMapper().readValue(listInfoStudentJson,List.class);
            return new JsonResult<>(list);
        }
        //4.如果业务key不存在，则查询数据库
        List<Student> list = studentMapper.selectList(null);
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
     * 分页查询学生信息
     * @param currentPage
     * @param pageSize
     * @param search
     * @return
     * @throws JsonProcessingException
     */
    public JsonResult<Page<Student>> findPage(Integer currentPage,Integer pageSize,String search) throws JsonProcessingException {
        //1.定义一个数组用于存放业务+key字段
        String[] studentKeyArr = new String[]{
                "student:currentPage_"+currentPage+":pageSize_"+pageSize+":",
                "Name_:"
        };
        //2.定义条件构造器，用于后面的字段查询
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        //3.判断关键字字段是否为空，不为空则将关键字拼接在相应的key后面，并添加至条件构造器
        if (!search.isEmpty()){
            studentKeyArr[1] = "Name_"+search+":";
            queryWrapper.like("name",search);
        }
        //4.将数组进行拼接成字符串业务+key
        String studentListKey = String.join("", studentKeyArr);
        //5.从redis中查找业务+key
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        String pageInfoStudentJson = valueOperations.get(studentListKey);
        //6.如果业务key存在则直接将value值返回（数据取自Redis）
        if (!Objects.equals(pageInfoStudentJson, null)){
            System.out.println("==============================数据来自Redis缓存==================================");
            Page<Student> page = new ObjectMapper().readValue(pageInfoStudentJson, Page.class);
            return new JsonResult<>(page);
        }
        //7.如果业务key不存在，则查询数据库
        Page<Student> page = new Page<>(currentPage,pageSize);
        studentMapper.selectPage(page,queryWrapper);
        //8.如果数据库没有对应的数据，则将空数据写入redis，并设置60s有效期（将空数据写入redis为了防止频繁访问数据库）
        if (page.getRecords().isEmpty()){
            valueOperations.set(studentListKey,new ObjectMapper().writeValueAsString(page),60, TimeUnit.SECONDS);
        }else {
            //9.如果数据库有对应的数据，则将数据写入redis
            valueOperations.set(studentListKey,new ObjectMapper().writeValueAsString(page));
        }
        //10.返回数据（数据取自MySQL）
        System.out.println("==============================数据来自MySQL数据库==================================");
        return new JsonResult<>(page);
    }

    /**
     * 添加学生信息
     * @param student
     * @return
     */
    public JsonResult<Map<String,Object>> add(Student student){
        //清除redis缓存
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("student:*")));
        //清除redis 学生所有信息
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("studentInfo")));
        try {
            studentMapper.insert(student);
            return new JsonResult<>("200","添加成功！");
        }catch (Exception e){
            return new JsonResult<>("0","添加失败！");
        }
    }

    /**
     * 更新学生信息
     * @param student
     * @return
     */
    public JsonResult<Map<String,Object>> upd(Student student){
        //清除redis student：*
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("student:*")));
        //清除redis score：的信息（存在外键）
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("score:*")));
        //清除redis 学生所有信息
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("studentInfo")));
        try {
            studentMapper.updateById(student);
            return new JsonResult<>("200","修改成功！");
        }catch (Exception e){
            return new JsonResult<>("0","修改失败！");
        }
    }

    /**
     * 删除学生信息
     * @param studentNumber
     * @return
     */
    public JsonResult<Map<String,Object>> del(Integer studentNumber){
        //清除redis student：*
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("student:*")));
        //清除redis score：的信息（存在外键）
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("score:*")));
        //清除redis 学生所有信息
        stringRedisTemplate.delete(Objects.requireNonNull(stringRedisTemplate.keys("studentInfo")));
        try {
            studentMapper.deleteById(studentNumber);
            return new JsonResult<>("200","删除成功！");
        }catch (Exception e){
            return new JsonResult<>("0","删除失败！");
        }
    }
}