package com.example.challenge.service;

import com.example.challenge.api.dto.NewOrderRequest;
import com.example.challenge.api.dto.UpdateOrderRequest;
import com.example.challenge.domain.Order;
import com.example.challenge.domain.OrderItem;
import com.example.challenge.domain.OrderStatus;
import com.example.challenge.repo.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

  private final OrderRepository repo;

  public OrderService(OrderRepository repo) {
    this.repo = repo;
  }

  public static double calculateTotal(List<OrderItem> items) {
    return items.stream().mapToDouble(i -> i.getQty() * i.getUnitPrice()).sum();
  }

  @Transactional
  public Order create(NewOrderRequest req) {
    Order order = new Order();
    order.setCustomerId(req.getCustomerId());
    order.setStatus(OrderStatus.NEW);

    List<OrderItem> orderItems = req.getItems().stream()
        .map(itemReq -> {
          OrderItem orderItem = new OrderItem();
          orderItem.setSku(itemReq.getSku());
          orderItem.setQty(itemReq.getQty());
          orderItem.setUnitPrice(itemReq.getUnitPrice());
          orderItem.setOrder(order);
          return orderItem;
        })
        .toList();
        
    order.setItems(orderItems);
    order.setTotal(calculateTotal(orderItems));
    
    var save = this.repo.save(order);
    return save;
  }

  public Optional<Order> get(String id) {
    return repo.findById(id);
  }

  public List<Order> list(Optional<OrderStatus> status) {
    return status.map(repo::findByStatus).orElseGet(repo::findAll);
  }

  @Transactional
  public Order updateItems(String id, UpdateOrderRequest req) {
    // TODO: permitir atualizar itens apenas se status=NEW, recalc total
    throw new UnsupportedOperationException("TODO: implementar atualização de itens");
  }

  @Transactional
  public void delete(String id) {
    // TODO: excluir apenas se status=NEW
    throw new UnsupportedOperationException("TODO: implementar exclusão de pedido");
  }

  @Transactional
  public void markPaid(String id) {
    // TODO: alterar status para PAID
    throw new UnsupportedOperationException("TODO: implementar markPaid");
  }

  @Transactional
  public void markFailed(String id) {
    // TODO: alterar status para FAILED_PAYMENT
    throw new UnsupportedOperationException("TODO: implementar markFailed");
  }
}
