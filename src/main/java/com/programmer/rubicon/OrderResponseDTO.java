package com.programmer.rubicon;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.UUID;

public class OrderResponseDTO {

    private UUID orderId;
    private int farmId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Australia/Melbourne")
    private Date startTime;
    private double duration;
    private String orderStatus;

    public OrderResponseDTO(WaterOrder order) {
        this.orderId = order.getOrderId();
        this.farmId = order.getFarmId();
        this.startTime = order.getStartTime();
        this.duration = order.getDuration();
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public int getFarmId() {
        return farmId;
    }

    public void setFarmId(int farmId) {
        this.farmId = farmId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
