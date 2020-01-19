package com.programmer.rubicon;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.UUID;

public class WaterOrder {

    private UUID orderId;
    private int farmId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Australia/Melbourne")
    private Date startTime;
    private double duration;
    private int orderStatus;

    private static final int CREATED = 0;

    public WaterOrder(int farmId, Date startTime, double duration) {
        this.orderId = UUID.randomUUID();
        this.farmId = farmId;
        this.startTime = startTime;
        this.duration = duration;
        this.orderStatus = CREATED;
    }

    public WaterOrder() {
        this.orderId = UUID.randomUUID();
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

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }


}
