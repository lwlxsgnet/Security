package lwl.controller;

import lwl.domain.ResponseResult;
import lwl.domain.User;
import lwl.service.LoginServcie;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class LoginController {
    @Resource
    private LoginServcie loginServcie;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return loginServcie.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        System.out.println("开始登出");
        return loginServcie.logout();
    }
}
