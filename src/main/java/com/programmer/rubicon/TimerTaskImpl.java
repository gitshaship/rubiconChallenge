package com.programmer.rubicon;

import java.util.TimerTask;

public class TimerTaskImpl extends TimerTask {

    private String message;

    public TimerTaskImpl(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println(message);
    }
}
