package com.programmer.rubicon;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


public class WaterOrderDTO {

    private int farmId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Australia/Melbourne")
    private Date startTime;
    private double duration;

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
}
