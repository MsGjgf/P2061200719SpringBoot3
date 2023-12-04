package com.springboot.p2061200719.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.p2061200719.Entity.Admin;
import com.springboot.p2061200719.Mapper.AdminMapper;
import com.springboot.p2061200719.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin findByAdmin(Object admin) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("admin",admin));
    }
}
