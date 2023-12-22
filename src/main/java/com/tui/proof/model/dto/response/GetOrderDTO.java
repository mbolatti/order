package com.tui.proof.model.dto.response;

import com.tui.proof.model.jpa.OrderQuanity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GetOrderDTO {
    private Long id;
    private String deliveryAddress;
    private OrderQuanity quantityPilotes;
    private GetClientDTO client;
    private Date orderDate = new Date();
    private double unitPrice = 1.33;
    private double totalOrder;
}
