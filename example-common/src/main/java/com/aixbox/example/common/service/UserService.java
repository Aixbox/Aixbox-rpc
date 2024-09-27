package com.aixbox.example.common.service;

import com.aixbox.example.common.model.User;

/**
 * Description: 用户服务
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/10 下午8:35
 */
public interface UserService {

    /**
     * 获取用户
     * @param user
     * @return
     */
    User getUser(User user);

    default User getNumber() {
        return new User();
    }

}
