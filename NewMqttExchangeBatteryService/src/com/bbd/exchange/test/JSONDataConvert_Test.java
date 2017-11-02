package com.bbd.exchange.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bbd.exchange.mobile.ClientRequestMessage;

import net.sf.json.JSONObject;  

public class JSONDataConvert_Test {
    /** 
     * 创建JSON字符串 
     * @param key 
     * @param value 
     * @return 
     */  
    public static String createJsonString(String key, Object value) {  
        JSONObject jsonObject = new JSONObject();  
        jsonObject.put(key, value);  
        return jsonObject.toString();  
    }  
  
    public static void init() {  
        // 一个用户对象转换  
        Person person = new Person(001,"大美女");  
        System.out.println(createJsonString("person", person));  
  
        // 用户对象集合转换  
        List<Person> personList = new ArrayList<Person>();  
        Person person1 = new Person(001, "刘仁奎");  
        Person person2 = new Person(002, "牛月月");  
        personList.add(person1);  
        personList.add(person2);  
        System.out.println(createJsonString("personList", personList));  
  
        // 字符串集合转换  
        List<String> stringList = new ArrayList<String>();  
        stringList.add("X-rapido");  
        stringList.add("NiuYue");  
  
        // list中map集合转换  
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();  
        for (int i = 0; i < 3; ++i) {  
            Map<String, String> map = new HashMap<String, String>();  
            map.put("编号", "id_" + i);  
            map.put("地址", "Name_" + i);  
            mapList.add(map);  
        }  
        System.out.println(createJsonString("mapList", mapList));  
    }  
  
    public static void main(String[] args) {  
        init();  
       
		ClientRequestMessage message = new ClientRequestMessage("exchange", "12345678", "HDG-000011", "6020");
        JSONObject jsonObject = new JSONObject(); 
        jsonObject.put("request", message);
        System.out.println(jsonObject.toString());
    }  
}
