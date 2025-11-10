package com.example.challenge.api;

import com.example.challenge.api.dto.NewOrderRequest;
import com.example.challenge.api.dto.UpdateOrderRequest;
import com.example.challenge.domain.Order;
import com.example.challenge.domain.OrderStatus;
import com.example.challenge.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.camel.ProducerTemplate;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@Tag(name="Orders")
public class OrderController {

  private final OrderService service;
  private final ProducerTemplate template;

  public OrderController(OrderService service, ProducerTemplate template) {
    this.service = service;
    this.template = template;
  }

  @Operation(summary = "Cria um novo pedido")
  @PostMapping
  public ResponseEntity<Order> create(@Valid @RequestBody NewOrderRequest req) {
    Order order = service.create(req);
    return ResponseEntity.status(HttpStatus.SC_CREATED).body(order);
  }

  @Operation(summary = "Busca um pedido por ID")
  @GetMapping("/{id}")
  public ResponseEntity<Order> get(@PathVariable String id) {
    service.get(id);
    return ResponseEntity.status(501).build();
  }

  @Operation(summary = "Lista pedidos (opcional filtrar por status)")
  @GetMapping
  public ResponseEntity<List<Order>> list(@RequestParam Optional<OrderStatus> status) {
    service.list(status);
    List<Order> orders = service.list(status);
    return ResponseEntity.ok(orders);
  }

  @Operation(summary = "Atualiza itens de um pedido (apenas se NEW)")
  @PutMapping("/{id}")
  public ResponseEntity<Order> update(@PathVariable String id, @Valid @RequestBody UpdateOrderRequest req) {
    service.updateItems(id, req);
    return ResponseEntity.status(501).build();
  }

  @Operation(summary = "Exclui um pedido (apenas se NEW)")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    service.delete(id);
    return ResponseEntity.status(501).build();
  }

  @Operation(summary = "Processa pagamento do pedido via Camel chamando DummyJSON")
  @PostMapping("/{id}/pay")
  public ResponseEntity<Order> pay(@PathVariable String id) {
    // TODO: validar status, enviar para rota Camel (ex.: direct:payOrder) com headers orderId/amount
    return ResponseEntity.status(501).build();
  }
}
