package lwl.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lwl.domain.LoginUser;
import lwl.domain.User;
import lwl.mapper.MenuMapper;
import lwl.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    UserMapper userMapper;

    @Resource
    MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>().eq(User::getUserName, username);
        User user = userMapper.selectOne(wrapper);

        //如果没有该用户就抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 查询权限信息封装到LoginUser中
//        List<String> list = new ArrayList<>()
//        list.add("user")
        List<String> list = menuMapper.selectPermsByUserId(user.getId());


        // 将用户信息封装到UserDetails实现类中
        return new LoginUser(user, list);
    }
}
