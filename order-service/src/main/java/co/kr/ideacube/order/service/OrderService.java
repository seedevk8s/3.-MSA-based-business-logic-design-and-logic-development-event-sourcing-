package co.kr.ideacube.order.service;

import co.kr.ideacube.dto.OrchestratorRequestDTO;
import co.kr.ideacube.dto.OrderRequestDTO;
import co.kr.ideacube.dto.OrderResponseDTO;
import co.kr.ideacube.enums.OrderStatus;
import co.kr.ideacube.order.entity.PurchaseOrder;
import co.kr.ideacube.order.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;

@Service
public class OrderService {

    // product price map
    private static final Map<Integer, Double> PRODUCT_PRICE =  Map.of(
            1, 100d,
            2, 200d,
            3, 300d
    );

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private Sinks.Many<OrchestratorRequestDTO> sink;

    public Mono<PurchaseOrder> createOrder(OrderRequestDTO orderRequestDTO){
        return this.purchaseOrderRepository.save(this.dtoToEntity(orderRequestDTO))
                .doOnNext(e -> orderRequestDTO.setOrderId(e.getId()))
                .doOnNext(e -> this.emitEvent(orderRequestDTO));
    }

    public Flux<OrderResponseDTO> getAll() {
        return this.purchaseOrderRepository.findAll()
                .map(this::entityToDto);
    }

    private void emitEvent(OrderRequestDTO orderRequestDTO){
        this.sink.tryEmitNext(this.getOrchestratorRequestDTO(orderRequestDTO));
    }

    private PurchaseOrder dtoToEntity(final OrderRequestDTO dto){
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(dto.getOrderId());
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(PRODUCT_PRICE.get(purchaseOrder.getProductId()));
        return purchaseOrder;
    }

    private OrderResponseDTO entityToDto(final PurchaseOrder purchaseOrder){
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(purchaseOrder.getId());
        dto.setProductId(purchaseOrder.getProductId());
        dto.setUserId(purchaseOrder.getUserId());
        dto.setStatus(purchaseOrder.getStatus());
        dto.setAmount(purchaseOrder.getPrice());
        return dto;
    }

    public OrchestratorRequestDTO getOrchestratorRequestDTO(OrderRequestDTO orderRequestDTO){
        OrchestratorRequestDTO requestDTO = new OrchestratorRequestDTO();
        requestDTO.setUserId(orderRequestDTO.getUserId());
        requestDTO.setAmount(PRODUCT_PRICE.get(orderRequestDTO.getProductId()));
        requestDTO.setOrderId(orderRequestDTO.getOrderId());
        requestDTO.setProductId(orderRequestDTO.getProductId());
        return requestDTO;
    }

}
