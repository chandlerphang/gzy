package com.cactus.guozy.common.extension;

import org.apache.commons.beanutils.BeanComparator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class ExtensionManager<T extends ExtensionHandler> implements InvocationHandler {

    protected boolean handlersSorted = false;
    protected static String LOCK_OBJECT = new String("EM_LOCK");
    
    protected T extensionHandler;
    protected List<T> handlers = new ArrayList<T>();

    @SuppressWarnings("unchecked")
    public ExtensionManager(Class<T> _clazz) {
        extensionHandler = (T) Proxy.newProxyInstance(_clazz.getClassLoader(),
                new Class[] { _clazz },
                this);
    }
    
    public T getProxy() {
        return extensionHandler;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<T> getHandlers() {
        synchronized (LOCK_OBJECT) {
            if (!handlersSorted) {
                Comparator fieldCompare = new BeanComparator("priority");
                Collections.sort(handlers, fieldCompare);
                handlersSorted = true;
            }
            return handlers;
        }
    }

    public boolean registerHandler(T handler) {
        synchronized (LOCK_OBJECT) {
            boolean add = true;
            for (T item : this.handlers) {
                if (item.getClass().equals(handler.getClass())) {
                    add = false;
                }
            }
            if (add) {
                this.handlers.add(handler);
                handlersSorted = false;
            }
            
            return add;
        }
    }

    public void setHandlers(List<T> handlers) {
        this.handlers = handlers;
    }

    public boolean shouldContinue(ExtensionResultStatus result, ExtensionHandler handler,
            Method method, Object[] args) {
        if (result != null) {
            if (ExtensionResultStatus.HANDLED_STOP.equals(result)) {
                return false;
            }
            
            if (ExtensionResultStatus.HANDLED.equals(result) && ! continueOnHandled()) {
                return false;
            }
        }
        return true;
    }

    public boolean continueOnHandled() {
        return false;
    }

    public int getPriority() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        boolean notHandled = true;
        for (ExtensionHandler handler : getHandlers()) {
            try {
                if (handler.isEnabled()) {
                    ExtensionResultStatus result = (ExtensionResultStatus) method.invoke(handler, args);
                    if (!ExtensionResultStatus.NOT_HANDLED.equals(result)) {
                        notHandled = false;
                    }
                    if (!shouldContinue(result, handler, method, args)) {
                        break;
                    }
                }
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
        if (notHandled) {
            return ExtensionResultStatus.NOT_HANDLED;
        } else {
            return ExtensionResultStatus.HANDLED;
        }
    }
       
}
