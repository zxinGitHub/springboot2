package com.example.springboot2.test;

/**
 *
 * 研究下多线程相关的问题
 * Created by zx9035 on 2018/5/25.
 */
public class LockTest {

    private static Object object = new Object();

    public static void main(String[] args) {
        LockTest lockTest = new LockTest();
        for (int i = 0; i < 5; i++) {
            lockTest.new MyThread().start();
        }

    }


    class MyThread extends Thread {

        @Override
        public void run() {
            int count = 0;
            for (; ; ) {
                count++;
                System.out.println(Thread.currentThread()+"1#####:" + count);
                try {
                    synchronized (object) {
                        //System.out.println(Thread.currentThread()+"2#####:" + count);
                        if (count%100 == 0) {
                            object.wait(50000);
                            //System.out.println(Thread.currentThread()+"3#####:" + count);
                        }
                    }
                    //System.out.println(Thread.currentThread()+"4#####:" + count);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


                if (count == 10000000) {
                    break;
                }
        }
    }

    }

}
