package com.cactus.guozy.common.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.cactus.guozy.common.utils.Strings;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Chaven Peng
 */
public class Jsons {

    /**
     * 忽略对象中值为NULL或""的属性
     */
    public static final Jsons EXCLUDE_EMPTY = new Jsons(JsonInclude.Include.NON_EMPTY);

    /**
     * 忽略对象中值为默认值的属性
     */
    public static final Jsons EXCLUDE_DEFAULT = new Jsons(JsonInclude.Include.NON_DEFAULT);

    /**
     * 默认不排除任何属性
     */
    public static final Jsons DEFAULT = new Jsons();

    private ObjectMapper mapper;
    
    protected Map<String, Object> map;

    private Jsons() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private Jsons(JsonInclude.Include include) {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(include);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
    
    public Jsons with(String key, Object value) {
    	if(map == null) {
    		map = new HashMap<String, Object>();
    	}
        map.put(key, value);
        return this;
    }
    
    public String done() {
        try {
        	String ret = mapper.writeValueAsString(map);
        	map.clear();
        	return ret;
        } catch (IOException e) {
            throw new JsonException("Could not serialize JSON", e);
        }
    }

    /**
     * convert an object(POJO, Collection, ...) to json string
     * @param target target object
     * @return json string
     * @throws me.hao0.common.exception.JsonException the exception for json
     */
    public String toJson(Object target) {
        try {
            return mapper.writeValueAsString(target);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * deserialize a json to target class object
     * @param json json string
     * @param target target class
     * @param <T> the generic type
     * @return target object
     * @throws JsonException the exception for json
     */
    public <T> T fromJson(String json, Class<T> target) {
        if (Strings.isNullOrEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, target);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * 反序列化
     * @param javaType JavaType
     * @param jsonString json string
     * @param <T> the generic type
     * @see #createCollectionType(Class, Class...)
     * @return the javaType's object
     * @throws JsonException the exception for json
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (Strings.isNullOrEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * construct collection type
     * @param collectionClass collection class, such as ArrayList, HashMap, ...
     * @param elementClasses element class
     * @return JavaType
     */
    public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
