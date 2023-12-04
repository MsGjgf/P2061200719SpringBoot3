package com.springboot.p2061200719.Mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.jeffreyning.mybatisplus.base.MppBaseMapper;
import com.springboot.p2061200719.Entity.Score;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface ScoreMapper extends MppBaseMapper<Score> {

    @MapKey("")
    Page<Map<String,Object>> findPage(IPage<?> page,String courseName,String studentName);
}
