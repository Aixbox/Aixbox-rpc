package com.aixbox.examplespringbootconsumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Description:
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 下午2:53
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ExampleServiceImplTest {

    @Autowired
    private ExampleServiceImpl exampleService;

    @Test
    public void test1() {
        exampleService.test();
    }

}