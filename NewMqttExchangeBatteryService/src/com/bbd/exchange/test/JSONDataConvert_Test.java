package com.bbd.exchange.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bbd.exchange.mobile.ClientRequestMessage;

import net.sf.json.JSONObject;  

public class JSONDataConvert_Test {
    /** 
     * ����JSON�ַ��� 
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
        // һ���û�����ת��  
        Person person = new Person(001,"����Ů");  
        System.out.println(createJsonString("person", person));  
  
        // �û����󼯺�ת��  
        List<Person> personList = new ArrayList<Person>();  
        Person person1 = new Person(001, "���ʿ�");  
        Person person2 = new Person(002, "ţ����");  
        personList.add(person1);  
        personList.add(person2);  
        System.out.println(createJsonString("personList", personList));  
  
        // �ַ�������ת��  
        List<String> stringList = new ArrayList<String>();  
        stringList.add("X-rapido");  
        stringList.add("NiuYue");  
  
        // list��map����ת��  
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();  
        for (int i = 0; i < 3; ++i) {  
            Map<String, String> map = new HashMap<String, String>();  
            map.put("���", "id_" + i);  
            map.put("��ַ", "Name_" + i);  
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
