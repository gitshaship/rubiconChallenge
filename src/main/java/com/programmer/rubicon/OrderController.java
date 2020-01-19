package com.programmer.rubicon;

import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // create a new water order
    @RequestMapping(value = "/order", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createOrder(@RequestBody WaterOrderDTO request) {
        if (orderService.createOrder(request)) {
            return ResponseEntity.ok().body(JSONParser.quote("Successfully created"));
        } else {
            return ResponseEntity.badRequest().body(JSONParser.quote("Bad request"));
        }
    }

    // get all water orders created by a farm
    @RequestMapping(value = "/order", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getOrdersForAFarm(@RequestParam(value = "farmId") int farmId) {
        List<OrderResponseDTO> orderList = orderService.getWaterOrdersByFarmId(farmId);
        if (orderList.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(orderList);
        }
    }

    // cancel an existing order
    @RequestMapping(value = "/order/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> cancelOrder(@PathVariable(value = "id") String orderId) {
        if (orderService.getWaterOrderById(orderId) == null) {
            return ResponseEntity.notFound().build();
        } else if (orderService.cancelWaterOrder(orderId)) {
            return ResponseEntity.ok().body(JSONParser.quote("Successfully cancelled"));
        } else {
            return ResponseEntity.ok().body(JSONParser.quote("Order has already been delivered"));
        }
    }


}


