package in.rabbitmq.orderService.controller;

import in.rabbitmq.orderService.dto.Order;
import in.rabbitmq.orderService.dto.OrderEvent;
import in.rabbitmq.orderService.publisher.OrderProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderProducer orderProducer;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping("/orders")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        order.setOrderId(UUID.randomUUID().toString());
        OrderEvent orderEvent = new OrderEvent("PENDING", "order is in peding status", order);
        orderProducer.send(orderEvent);
        return ResponseEntity.ok("order placed with orderId: " + order.getOrderId());
    }
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok("health check ok");
    }
}
