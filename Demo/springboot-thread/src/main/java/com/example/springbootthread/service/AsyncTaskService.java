package com.example.springbootthread.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class AsyncTaskService {

    private Logger logger = LoggerFactory.getLogger(AsyncTaskService.class);

    /**
     * 异步保存到数据库的方法
     * @param msg
     */
    @Async
    public void executeAsyncTask(String msg){
        System.out.println(Thread.currentThread().getName() + "开启新线程执行" + msg);
    }

    @Async
    public Future<String> doReturn(int i){
        logger.info(">>>>>>>>>>>>>>线程名>>>>>>>>>>>>>>"+Thread.currentThread().getName());
        try {
            // 这个方法需要调用500毫秒
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 消息汇总
        return new AsyncResult<>(String.format("这个是第{%s}个异步调用的证书", i));
    }


    /**
     * 异常调用返回Future
     * @return
     * @throws InterruptedException
     */
    @Async
    public Future<Long> asyncInvokeReturnFuture() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread.sleep(5000);
        Future<Long> future = new AsyncResult<Long>((System.currentTimeMillis()-start));// Future接收返回值，这里是String类型，可以指明其他类型
        return future;
    }

    @Async
    public Future<Long> asyncInvokeReturnFuture2() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread.sleep(3500);
        Future<Long> future = new AsyncResult<Long>((System.currentTimeMillis()-start));// Future接收返回值，这里是String类型，可以指明其他类型
        return future;
    }

    @Async
    public Future<Long> asyncInvokeReturnFuture3() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread.sleep(2500);
        Future<Long> future = new AsyncResult<Long>((System.currentTimeMillis()-start));// Future接收返回值，这里是String类型，可以指明其他类型
        return future;
    }

}
