package java8;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author 唐国翔
 * @date 2018-01-18 11:17
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
public class EmployeeTest {

    private List<Employee> employees;

    @Before
    public void init(){
         employees = Lists.newArrayList(
                new Employee(1,"张安",18,3000.00),
                new Employee(2,"李四",22,5650.00),
                new Employee(3,"王五",44,3900.00),
                new Employee(4,"赵六",22,2900.00)
        );
    }

    /**
     * 获取年龄大于30
     */
    @Test
    public void test1(){
        List<Employee> employees = Employee.filterEmployee(this.employees, employee -> employee.getAge() > 30);
    }

    @Test
    public void test2(){
        List<Employee> list = employees.stream()
                .filter(employee -> employee.getAge() > 30)
                .limit(1).collect(Collectors.toList());
        ThreadLocalRandom current = ThreadLocalRandom.current();
    }

    @Test
    public void test3(){
        employees.sort((e1, e2) -> {
            if (e1.getAge().equals(e2.getAge())) {
                return e1.getName().compareTo(e2.getName());
            }
            return Integer.compare(e1.getAge(), e2.getAge());
        });

        employees.forEach(System.out::println);
    }

    @Test
    public void test4(){
        employees.stream().sorted((e1,e2)->{
            if (e1.getAge().equals(e2.getAge())) {
                return e1.getName().compareTo(e2.getName());
            }
            return Integer.compare(e1.getAge(), e2.getAge());
        }).forEach(System.out::println);
    }

    @Test
    public void test5(){
        Optional<Employee> employee = employees.parallelStream()
                .filter(e -> e.getAge() < 30).min(Comparator.comparingInt(Employee::getAge));
        System.out.println(employee.get());
    }

    @Test
    public void test6(){
         employees.stream()
                .map(Employee::getName)
                .collect(Collectors.toCollection(Lists::newLinkedList)).forEach(System.out::println);
    }

    @Test
    public void test7(){
        Double sum = employees.stream().mapToDouble(Employee::getSalary).sum();
        System.out.println(sum);
        double d = employees.stream().collect(Collectors.summarizingDouble(Employee::getSalary)).getAverage();
        System.out.println(d);
        String s = employees.stream().map(Employee::getName).collect(Collectors.joining());
        System.out.println(s);
    }

    @Test
    public void test8(){


        ExecutorService pool = Executors.newFixedThreadPool(4);
        ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(()->0);

        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return null;
            }
        };


        for (int i = 0; i < 10; i++) {
            pool.submit(()->{
                threadLocal.set(threadLocal.get()+1);
                System.out.println(Thread.currentThread().getName()+" : "+threadLocal.get());
            });
        }


        pool.shutdown();
    }

   // private volatile static int ticket = 100;

    private static AtomicInteger ticket = new AtomicInteger(100);

    private static Lock lock = new ReentrantLock();

    @Test
    public void test9(){

    }

    public static void main(String[] args) {
        Runnable runnable = ()->{
           try {
               lock.lock();
               while (ticket.get() > 0){
                   Thread.sleep(10);
                   System.out.println(Thread.currentThread().getName()+"售票，票号为："+ticket.getAndDecrement());
               }
           } catch (InterruptedException e) {
               e.printStackTrace();
           }finally {
               lock.unlock();
           }
        };
        new Thread(runnable,"窗口一").start();
        new Thread(runnable,"窗口二").start();
        new Thread(runnable,"窗口三").start();
    }
}
