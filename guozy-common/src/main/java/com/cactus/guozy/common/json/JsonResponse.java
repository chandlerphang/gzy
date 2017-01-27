package com.cactus.guozy.common.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Chaven Peng
 */
public class JsonResponse {
    protected Map<String, Object> map = new HashMap<String, Object>();
    protected HttpServletResponse response;
    
    public JsonResponse(HttpServletResponse response) {
        this.response = response;
    }
    
    public JsonResponse with(String key, Object value) {
        map.put(key, value);
        return this;
    }
    
    public String done() {
        response.setHeader("Content-Type", "application/json");
        try {
            new ObjectMapper().writeValue(response.getWriter(), map);
        } catch (Exception e) {
            throw new JsonException("Could not serialize JSON", e);
        }
        return null;
    }
}
