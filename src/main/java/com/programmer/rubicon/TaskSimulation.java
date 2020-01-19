package com.programmer.rubicon;

import java.util.*;

public class TaskSimulation {

    private static TaskSimulation instance = new TaskSimulation();
    private Timer timer = new Timer();
    private Map<String, List<TimerTaskImpl>> taskMap = new HashMap<>();

    public static TaskSimulation getInstance() {
        return instance;
    }

    public void scheduleTask(Date time, String message, String orderId, Boolean sentToMap) {
        TimerTaskImpl task = new TimerTaskImpl(message);
        timer.schedule(task, time);
        if (sentToMap) {
            // update task map
            List<TimerTaskImpl> taskList = taskMap.get(orderId);
            if (taskList == null) {
                taskList = new ArrayList<>();
            }
            taskList.add(task);
            taskMap.put(orderId, taskList);
        }
    }

    public void cancelTaskList(String orderId) {
        List<TimerTaskImpl> taskList = taskMap.get(orderId);
        for (TimerTaskImpl task : taskList) {
            task.cancel();
        }
        taskMap.remove(orderId);
    }


}
