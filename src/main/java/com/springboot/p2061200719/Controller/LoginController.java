package com.springboot.p2061200719.Controller;

import com.springboot.p2061200719.Entity.Admin;
import com.springboot.p2061200719.Entity.JsonResult;
import com.springboot.p2061200719.Service.AdminService;
import com.springboot.p2061200719.Utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public JsonResult<String> login(@RequestBody Map<String,Object> loginData){
        log.info("loginData：{}",loginData);
        //根据用户名查询用户
        Admin admin = adminService.findByAdmin(loginData.get("admin"));
        //判断用户名是否存在
        if (admin == null){
            return new JsonResult<>("0","用户名不存在！");
        }

        //判断密码是否正确
        if (admin.getPassword().equals(loginData.get("password"))){
            //登录成功
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",admin.getId());
            claims.put("admin",admin.getAdmin());
            //生成token并返回给用户
            String token = JwtUtil.genToken(claims);
            return new JsonResult<>(token);
        }
        return new JsonResult<>("0","密码错误！");
    }
}
