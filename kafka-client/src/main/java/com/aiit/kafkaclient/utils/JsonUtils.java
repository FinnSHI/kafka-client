package com.aiit.kafkaclient.utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 转换为格式化的json
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //修改日期格式
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    //生成非压缩的json字符串
    public static String toJson(Object obj) {
        String jsonStr = null;
        try {
            jsonStr = MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        return jsonStr;
    }

    //生成压缩过的json字符串
    public static String toJsonWithCompress(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T fromJson(String jsonStr, TypeReference<T> valueTypeRef) {
        T t = null;
        try {
            t = MAPPER.readValue(jsonStr, valueTypeRef);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return t;
    }

    public static <T> T fromJson(String jsonStr, Class<T> valueTypeRef) {
        T t = null;
        try {
            t = MAPPER.readValue(jsonStr, valueTypeRef);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return t;
    }

    public static <T, S> T convert(S s, TypeReference<T> valueTypeRef) {
        return fromJson(toJson(s), valueTypeRef);
    }

    public static <T, S> T convert(S s, Class<T> valueTypeRef) {
        return fromJson(toJson(s), valueTypeRef);
    }

    public static <T> MultiValueMap getMultiValueMap(T request) {
        // 将任意request对象转换成map
        HashMap<String, Object> map = convert(request, new TypeReference<HashMap<String, Object>>() {
        });

        MultiValueMap multiValueMap = new LinkedMultiValueMap();

        // 添加map键值对到MultiValueMap
        map.forEach(multiValueMap::add);

        return multiValueMap;
    }

    /*
     * json里面拿指定字段的值
     */
    public static String getJsonValue(String json, String jsonPath) {
        List<String> tableName = JsonPath.read(json, jsonPath);
        return tableName.toString();
    }
}
