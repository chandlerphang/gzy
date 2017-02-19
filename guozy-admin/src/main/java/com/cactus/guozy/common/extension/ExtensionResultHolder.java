package com.cactus.guozy.common.extension;

import java.util.HashMap;
import java.util.Map;

public class ExtensionResultHolder<T> {

    protected T result;
    protected Throwable throwable;
    protected Map<String, Object> contextMap = new HashMap<String, Object>();

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Map<String, Object> getContextMap() {
        return contextMap;
    }
    
}
