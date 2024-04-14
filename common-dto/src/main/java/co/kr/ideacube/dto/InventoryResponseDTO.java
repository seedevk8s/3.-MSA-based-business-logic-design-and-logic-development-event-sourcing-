package co.kr.ideacube.dto;

import co.kr.ideacube.enums.InventoryStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class InventoryResponseDTO {

    private UUID orderId;
    private Integer userId;
    private Integer productId;
    private InventoryStatus status;

}
