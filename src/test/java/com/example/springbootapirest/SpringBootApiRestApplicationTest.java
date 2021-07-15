package com.example.springbootapirest;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootApiRestApplicationTest {

    @Test
    public void contextLoads() {
        Assert.assertTrue(true);
    }

}
