package com.programmer.rubicon;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private static final int CANCELLED_STATUS = -1;
    private static final String REQUESTED = "Order has been placed but not yet delivered";
    private static final String INPROGRESS = "Order is being delivered right now";
    private static final String DELIVERED = "Order has been delivered";
    private static final String CANCELLED = "Order was cancelled before delivery";
    private static final String CREATE_ALERT = "Order is placed";
    private static final String START_ALERT = "Order is started";
    private static final String DELIVER_ALERT = "Order is completed";
    private static final String CANCEL_ALERT = "Order is canceled";


    @Override
    public Boolean createOrder(WaterOrderDTO request) {
        WaterOrder newOrder = new WaterOrder(request.getFarmId(), request.getStartTime(), request.getDuration());
        Date startTime = request.getStartTime();
        Date endTime = calculateEndTime(startTime, request.getDuration());
        Date now = new Date();

        if (startTime.before(now)) {
            return false;
        }

        LinkedList orderIdList = OrderStore.getInstance().getActiveOrderIdList(request.getFarmId());
        if (orderIdList != null) {
            int indexOfNewOrder = findIndexInList(orderIdList, 0, orderIdList.size() - 1, startTime, endTime);
            if (indexOfNewOrder == -1) {
                return false;
            } else {
                orderIdList.add(indexOfNewOrder, newOrder.getOrderId().toString());
            }

        } else {
            orderIdList = new LinkedList();
            orderIdList.add(newOrder.getOrderId().toString());
        }

        OrderStore.getInstance().putActiveWaterOrderToFarmId(request.getFarmId(), orderIdList);
        OrderStore.getInstance().putWaterIdToList(newOrder);
        setAlertsWhenOrderIsPlaced(startTime, endTime, newOrder.getOrderId().toString());
        return true;
    }

    @Override
    public List<OrderResponseDTO> getWaterOrdersByFarmId(int farmId) {
        List<WaterOrder> orderList = OrderStore.getInstance().getWaterOrders(farmId);
        List<OrderResponseDTO> response = new ArrayList<>();
        if (orderList != null) {
            for (WaterOrder order : orderList) {
                OrderResponseDTO dto = new OrderResponseDTO(order);
                Date endTime = calculateEndTime(order.getStartTime(), order.getDuration());
                if (order.getOrderStatus() == -1) {
                    dto.setOrderStatus(CANCELLED);
                } else if (order.getStartTime().after(new Date())) {
                    dto.setOrderStatus(REQUESTED);
                } else if (endTime.after(new Date())) {
                    dto.setOrderStatus(INPROGRESS);
                } else {
                    dto.setOrderStatus(DELIVERED);
                }
                response.add(dto);
            }
        }
        return response;
    }

    private Date calculateEndTime(Date startTime, double hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        int durationInMin = (int) (hours * 60);
        calendar.add(Calendar.MINUTE, durationInMin);
        return calendar.getTime();

    }

    private int findIndexInList(LinkedList orderIdList, int start, int end, Date startTime, Date endTime) {
        int numOrders = orderIdList.size();
        while (end > start) {
            int mid = start + (end - start) / 2;
            WaterOrder midOrder = OrderStore.getInstance().getWaterOrderById(orderIdList.get(mid).toString());
            if (midOrder.getStartTime().after(startTime)) {
                if (mid - 1 >= start)
                    end = mid - 1;
                else
                    end = start;
            }
            if (midOrder.getStartTime().before(startTime)) {
                if (mid + 1 <= end)
                    start = mid + 1;
                else
                    start = end;
            } else {
                start = mid;
                end = mid;
            }
        }
        if (end == start) {
            int mid = start + (end - start) / 2;
            //insert before
            WaterOrder midOrder = OrderStore.getInstance().getWaterOrderById(orderIdList.get(mid).toString());
            if (endTime.before(midOrder.getStartTime()) || endTime.equals(midOrder.getStartTime())) {
                if (start == 0) {
                    return 0;
                } else {
                    WaterOrder prevOrder = OrderStore.getInstance().getWaterOrderById(orderIdList.get(mid - 1).toString());
                    Date endTimeOfPrev = calculateEndTime(prevOrder.getStartTime(), prevOrder.getDuration());
                    if (startTime.after(endTimeOfPrev) || startTime.equals(endTimeOfPrev)) {
                        return mid - 1;
                    }
                }
            }

            //insert after
            Date endTimeOfMid = calculateEndTime(midOrder.getStartTime(), midOrder.getDuration());
            if (startTime.after(endTimeOfMid) || startTime.equals(endTimeOfMid)) {
                if (end == numOrders - 1) {
                    return numOrders;
                } else {
                    WaterOrder nextOrder = OrderStore.getInstance().getWaterOrderById(orderIdList.get(mid + 1).toString());
                    if (endTime.before(nextOrder.getStartTime()) || endTime.equals(nextOrder.getStartTime())) {
                        return mid + 1;
                    }
                }
            }
        }
        return -1;
    }

    public WaterOrder getWaterOrderById(String orderId) {
        return OrderStore.getInstance().getWaterOrderById(orderId);
    }

    public Boolean cancelWaterOrder(String orderId) {
        WaterOrder order = OrderStore.getInstance().getWaterOrderById(orderId);
        if (order != null) {
            // already cancelled
            if(order.getOrderStatus() == CANCELLED_STATUS){
                return false;
            }
            Date endDateTime = calculateEndTime(order.getStartTime(), order.getDuration());
            if (endDateTime.after(new Date())) {
                order.setOrderStatus(CANCELLED_STATUS);
                OrderStore.getInstance().putWaterIdToList(order);
                OrderStore.getInstance().cancelWaterOrder(order.getFarmId(),orderId);
                TaskSimulation.getInstance().scheduleTask(new Date(), orderId + ' ' + CANCEL_ALERT, orderId, false);
                // clear pending tasks
                TaskSimulation.getInstance().cancelTaskList(orderId);
                return true;
            }
        }
        return false;
    }

    private void setAlertsWhenOrderIsPlaced(Date startTime, Date endTime, String orderId) {
        // create alert
        TaskSimulation.getInstance().scheduleTask(new Date(), orderId + ' ' + CREATE_ALERT, orderId, false);
        // start alert
        TaskSimulation.getInstance().scheduleTask(startTime, orderId + ' ' + START_ALERT, orderId, true);
        // deliver alert
        TaskSimulation.getInstance().scheduleTask(endTime, orderId + ' ' + DELIVER_ALERT, orderId, true);
    }

}
