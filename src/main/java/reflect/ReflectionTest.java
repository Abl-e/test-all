package reflect;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author 唐国翔
 * @date 2018-02-02 15:51
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
public class ReflectionTest {

    @Test
    public void test1() throws Exception {
        Class<Person> aClass = Person.class;

        Person person = aClass.newInstance();

        Field name = aClass.getDeclaredField("name");
        name.setAccessible(true);
        name.set(person,"张三");

        Method show = aClass.getMethod("show");
        show.invoke(person);

        Method display = aClass.getMethod("display", String.class);
        display.invoke(person,"usa");

        System.out.println(person);

    }


    @Test
    public void test2() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Class<?> aClass = classLoader.loadClass("reflect.Person");
        Person person = (Person) aClass.newInstance();
        System.out.println(person);
    }

    @Test
    public void test3(){
        String str = "a,b,c,,";
        String[] ary = str.split(",");
        System.out.println(ary.length);

    }
}
