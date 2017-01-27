package com.cactus.guozy.core.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.OrderItem;

public class UpdateCartResponse implements Serializable {
    protected static final long serialVersionUID = 1L;

    protected Order order;

    protected List<OrderItem> removedItems = new ArrayList<OrderItem>();;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getRemovedItems() {
        return removedItems;
    }

    public void setRemovedItems(List<OrderItem> removedItems) {
        this.removedItems = removedItems;
    }

}