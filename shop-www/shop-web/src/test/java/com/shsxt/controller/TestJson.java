package com.shsxt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shop.core.model.SpecificationValue;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TW on 2017/6/13.
 */
public class TestJson {


    @Test
    public void test1() {
        String sepcialValues = "[{\"id\":2,\"value\":\"银色\"},{\"id\":6,\"value\":\"16GB\"}]";
        List<Map> result = JSON.parseArray(sepcialValues, Map.class);
        for (Map map: result) {
            System.out.println("id: =" + map.get("id") + "；value=" + map.get("value") );
        }

        List<SpecificationValue> specificationValues = JSON.parseArray(sepcialValues, SpecificationValue.class);
        System.out.print(specificationValues.size());
    }

    @Test
    public void test2() {
        Map<String, Object> result = new HashMap<>();
        result.put("resultCode", 1);
        result.put("resultMessage", "hello");
        Map<String, Object> map = new HashMap<>();
        map.put("abc", "sdfsdf");
        map.put("bcd", "sdfsdf");
        result.put("result", map);
        String resultJson = JSON.toJSONString(result);
        System.out.println(resultJson);

        // 不new对象获取abc的值
        JSONObject jsonObject = JSON.parseObject(resultJson);

        Integer resultCode = jsonObject.getInteger("resultCode");
        System.out.println("resultCode-->" + resultCode);

        JSONObject resultObj = jsonObject.getJSONObject("result");

        String bcd = resultObj.getString("bcd");
        System.out.println("bcd-->" + bcd);
    }

    @Test
    public void test3() {
        Map<String, Object> result = new HashMap<>();
        result.put("resultCode", 1);
        result.put("resultMessage", "hello");
        Map<String, Object> map = new HashMap<>();
        List<SpecificationValue> specificationValues = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SpecificationValue specificationValue = new SpecificationValue();
            specificationValue.setId(i);
            specificationValue.setValue("jello" + i);
            specificationValues.add(specificationValue);
        }
        map.put("specificationValues", specificationValues);
        map.put("abc", "sdfsdf");
        map.put("bcd", "sdfsdf");
        result.put("result", map);
        String resultJson = JSON.toJSONString(result);
        System.out.println(resultJson);

        // 不new对象获取abc的值,获取specificationValues里面的第一个value值
//		{"result":{"abc":"sdfsdf","specificationValues":[{"id":0,"value":"jello0"},{"id":1,"value":"jello1"},{"id":2,"value":"jello2"},{"id":3,"value":"jello3"},{"id":4,"value":"jello4"},{"id":5,"value":"jello5"},{"id":6,"value":"jello6"},{"id":7,"value":"jello7"},{"id":8,"value":"jello8"},{"id":9,"value":"jello9"}],"bcd":"sdfsdf"},"resultCode":1,"resultMessage":"hello"}
        String abcValue = JSON.parseObject(resultJson).getJSONObject("result").getString("abc");
        System.out.println(abcValue);

       String value = JSON.parseObject(resultJson).getJSONObject("result")
               .getJSONArray("specificationValues").getJSONObject(0).getString("value");
       System.out.println(value);
    }

    @Test
    public void test4() {
        String url = "http://api.map.baidu.com/telematics/v3/weather?location=上海&output=json&ak=A4c071813db2bfabad3593e97f46c6a4";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        System.out.println(result);
        System.out.println("----------------------------");
        JSONObject results = JSON.parseObject(result).getJSONArray("results").getJSONObject(0);
        System.out.println(results);
    }
}
