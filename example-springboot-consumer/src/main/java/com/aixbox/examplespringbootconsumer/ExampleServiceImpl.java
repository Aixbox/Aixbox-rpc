package com.aixbox.examplespringbootconsumer;

import com.aixbox.example.common.model.User;
import com.aixbox.example.common.service.UserService;
import com.aixbox.rpc.fault.tolerant.TolerantStrategyKeys;
import com.aixbox.rpcspringbootstarter.annotation.RpcReference;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 下午2:50
 */
@Service
public class ExampleServiceImpl {

    @RpcReference(tolerantStrategy = TolerantStrategyKeys.FAIL_BACK, fallback = ExampleServiceFallbackImpl.class)
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("aixbox");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }

}



















