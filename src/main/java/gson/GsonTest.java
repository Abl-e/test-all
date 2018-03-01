package gson;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author 唐国翔
 * @date 2018-01-31 15:06
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
public class GsonTest {

    @Test
    public void test1(){
        User user = new User("张三",12,"1234@qq.com");
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        System.out.println(userJson);

        userJson = userJson.replace("email_address","email");
        System.out.println(userJson);

        User user1 = gson.fromJson(userJson, User.class);
        System.out.println(user1);

    }

    @Test
    public void test2(){
        Gson gson = new Gson();
        String jsonArray = "[\"Android\",\"Java\",\"PHP\"]";
        List<String> list = gson.fromJson(jsonArray, new TypeToken<List<String>>(){}.getType());
        list.forEach(System.out::println);
    }

    @Test
    public void test3(){
        Gson gson = new Gson();
        Map<String,Object> map = Maps.newHashMapWithExpectedSize(32);
        map.put("name","张三");
        map.put("age",23);
        map.put("email","111.qq@com");
        List<Integer> list = Lists.newArrayListWithCapacity(16);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        map.put("data",list);
        String json = gson.toJson(map);
        System.out.println(json);

        Map<String,Object> result = gson.fromJson(json,new TypeToken<Map<String,Object>>(){}.getType());

        map.entrySet().forEach(System.out::println);

        List<Integer> data = (List<Integer>) map.get("data");

        System.out.println(data.get(0));

        // System.out.println(result);

    }

}
