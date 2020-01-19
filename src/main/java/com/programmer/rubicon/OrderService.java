package com.programmer.rubicon;

import java.util.List;

public interface OrderService {

    Boolean createOrder(WaterOrderDTO request);

    List<OrderResponseDTO> getWaterOrdersByFarmId(int farmId);

    WaterOrder getWaterOrderById(String orderId);

    Boolean cancelWaterOrder(String orderId);

}
