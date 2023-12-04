package com.springboot.p2061200719.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.p2061200719.Entity.Admin;

public interface AdminService extends IService<Admin> {
    Admin findByAdmin(Object admin);
}
