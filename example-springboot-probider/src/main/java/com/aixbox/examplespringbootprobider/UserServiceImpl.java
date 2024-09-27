package com.aixbox.examplespringbootprobider;

import com.aixbox.example.common.model.User;
import com.aixbox.example.common.service.UserService;
import com.aixbox.rpcspringbootstarter.annotation.RpcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description:
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 下午2:48
 */
@Service
@RpcService
public class UserServiceImpl implements UserService {

    @Resource
    private UserServiceRely userServiceRely;

    @Override
    public User getUser(User user) {
        userServiceRely.rely();
        System.out.println("用户名" + user.getName());
        return user;
    }
}
