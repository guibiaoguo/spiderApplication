package com.spider.collection.util;

/**
 * Created by Bill on 2015/11/19.
 */
public class ProxyUtil {

    public static void main(String[] args) {
//        for (int i = 0; i < 1000; i++) {
//            System.out.println(Thread.currentThread().getName() + " " + i);
//            if (i == 300) {
//                Thread myThread1 = new MyThread();     // 创建一个新的线程  myThread1  此线程进入新建状态
//                Thread myThread2 = new MyThread();     // 创建一个新的线程 myThread2 此线程进入新建状态
//                myThread1.start();                     // 调用start()方法使得线程进入就绪状态
//                myThread2.start();                     // 调用start()方法使得线程进入就绪状态
//            }
//        }
        MyRunnable my1 = new MyRunnable("my1");
        MyRunnable my2 = new MyRunnable("my2");
        Thread thread1 = new Thread(my1);
        Thread thread2 = new Thread(my2);
        thread1.start();
        thread2.start();
    }

}

class MyThread extends Thread {

    private int i = 0;

    @Override
    public void run() {
        for (i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() + " "  + i);
        }
    }
}

class MyRunnable implements Runnable {

    private String name;

    public MyRunnable(String name){
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            for (long j = 0; j < 100000000; j++) ;
                System.out.println(name + ": " + i);

        }
    }
}