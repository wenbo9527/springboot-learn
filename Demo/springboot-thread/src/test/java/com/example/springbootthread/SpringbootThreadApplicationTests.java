package com.example.springbootthread;

import com.example.springbootthread.service.AsyncTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.Future;

@SpringBootTest
class SpringbootThreadApplicationTests {

    @Autowired
    AsyncTaskService asyncTaskService;

    @Test
    void contextLoads() {
        try {
            long start = System.currentTimeMillis();
            Future<Long> future = asyncTaskService.asyncInvokeReturnFuture();
            Future<Long> future2 = asyncTaskService.asyncInvokeReturnFuture2();
            Future<Long> future3 = asyncTaskService.asyncInvokeReturnFuture3();
            Long l1 = future.get();//5000
            Long l2 = future2.get();//3500
            Long l3 = future3.get();//2500
            System.out.println(Thread.currentThread().getName()+"*************************************");
            System.out.println(Thread.currentThread().getName()+(l1+l2+l3 ));
            System.out.println(Thread.currentThread().getName()+"-------------------------------------");
            System.out.println(Thread.currentThread().getName()+(System.currentTimeMillis()-start));
            System.out.println(Thread.currentThread().getName()+"+++++++++++++++++++++++++++++++++++++");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
