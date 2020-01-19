package com.programmer.rubicon;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderStore {

    private static OrderStore instance = new OrderStore();
    private Map<String, WaterOrder> orderMap = new HashMap<>();
    private Map<Integer, LinkedList<String>> activeOrdersByFarmMap = new HashMap<>();
    private Map<Integer, LinkedList<String>> cancelledOrdersByFarmMap = new HashMap<>();

    public static OrderStore getInstance() {
        return instance;
    }

    public LinkedList<String> getActiveOrderIdList(int farmId) {
        return activeOrdersByFarmMap.get(farmId);
    }

    public LinkedList<String> getCancelledOrderIdList(int farmId) {
        return cancelledOrdersByFarmMap.get(farmId);
    }

    public List<WaterOrder> getWaterOrders(int farmId) {
        List<String> orderList = activeOrdersByFarmMap.get(farmId);
        List<String> cancelledOrderList = cancelledOrdersByFarmMap.get(farmId);
        if (cancelledOrderList != null) {
            orderList.addAll(cancelledOrderList);
        }
        List<WaterOrder> list = new ArrayList<>();
        if (orderList != null) {
            return new ArrayList<>(orderList.stream()
                    .filter(orderMap::containsKey)
                    .collect(Collectors.toMap(Function.identity(), orderMap::get)).values());
        } else {
            return new ArrayList<>();
        }
    }

    public WaterOrder getWaterOrderById(String orderId) {
        return orderMap.get(orderId);
    }

    public void putActiveWaterOrderToFarmId(int farmId, LinkedList<String> orders) {
        if (activeOrdersByFarmMap.get(farmId) != null) {
            activeOrdersByFarmMap.remove(farmId);
        }
        activeOrdersByFarmMap.put(farmId, orders);
    }

    public void putWaterIdToList(WaterOrder order) {
        orderMap.put(order.getOrderId().toString(), order);
    }

    public void cancelWaterOrder(int farmId, String orderId) {
        LinkedList<String> currentOrders = activeOrdersByFarmMap.get(farmId);
        LinkedList<String> cancelledOrders = cancelledOrdersByFarmMap.get(farmId);
        if(cancelledOrders == null){
            cancelledOrders = new LinkedList<>();
        }
        currentOrders.remove(orderId);
        cancelledOrders.add(orderId);
        // update maps
        activeOrdersByFarmMap.put(farmId, currentOrders);
        cancelledOrdersByFarmMap.put(farmId, cancelledOrders);
    }


}
