package com.aixbox.examplespringbootconsumer;

import com.aixbox.example.common.model.User;
import com.aixbox.example.common.service.UserService;

/**
 * Description:
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/17 下午12:50
 */
public class ExampleServiceFallbackImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("降级容错返回测试");
        return new User();
    }
}
