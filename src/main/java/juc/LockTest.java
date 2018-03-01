package juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 关于Lock测试
 *
 * @author 唐国翔
 * @date 2018-01-15 13:17
 * <p>
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　  ┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 **/
public class LockTest {

    private static Lock lock = new ReentrantLock();

    private static Condition condition1 = lock.newCondition();
    private static Condition condition2 = lock.newCondition();
    private static Condition condition3 = lock.newCondition();

    private static int num = 1;

    synchronized

    /**
     * 顺序打印
     * @param args
     */

    public static void main(String[] args) {

        ExecutorService service = Executors.newFixedThreadPool(9);
        for (int i = 0; i < 5; i++) {
            service.submit(()->{
                while (num == 1){
                    try {
                        lock.lock();
                        Thread.sleep(500);
                        System.out.println(Thread.currentThread().getName()+"-->A");
                        num = 2;
                        condition2.signal();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
                while (num == 2){
                    try {
                        lock.lock();
                        Thread.sleep(500);
                        System.out.println(Thread.currentThread().getName()+"-->B");
                        num = 3;
                        condition3.signal();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
                while (num == 3){
                    try {
                        lock.lock();
                        Thread.sleep(500);
                        System.out.println(Thread.currentThread().getName()+"-->C");
                        num = 1;
                        condition1.signal();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            });
        }

        service.shutdown();

       /* new Thread(()->{
            try {
                lock.lock();
                System.out.println("A");
                condition2.signal();
            }finally {
                lock.unlock();
            }
        }).start();
        new Thread(()->{
            try {
                lock.lock();
                System.out.println("B");
                condition3.signal();
            }finally {
                lock.unlock();
            }
        }).start();
        new Thread(()->{
            try {
                lock.lock();
                System.out.println("C");
                condition1.signal();
            }finally {
                lock.unlock();
            }
        }).start();*/
    }




}


