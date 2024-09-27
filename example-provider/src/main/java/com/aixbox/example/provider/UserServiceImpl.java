package com.aixbox.example.provider;

import com.aixbox.example.common.model.User;
import com.aixbox.example.common.service.UserService;

/**
 * Description: 用户服务实现类
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/10 下午8:40
 */
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
