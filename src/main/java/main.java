import com.Dream.entity.Role;
import com.Dream.filiter.SimpleSerializeFilter;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

public class main {

    public static void main(String[] args) {
        HashMap<Class, HashSet<String>> includes=new HashMap<>();
        HashSet<String> hashSet=new HashSet<>();
        hashSet.add("id");
        hashSet.add("roleName");
        Role role=new Role();
        role.setId(1L);
        role.setRoleName("wyn");
        role.setNote("lala");
        includes.put(role.getClass(),hashSet);
        SimpleSerializeFilter simpleSerializeFilter=new SimpleSerializeFilter(includes,null);
        String result= JSONObject.toJSONString(role,simpleSerializeFilter);
//        String result=JSONObject.toJSONString(role);
        System.out.println(result);
    }
}
