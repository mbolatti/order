package com.tui.proof.model.dto.request;

import com.tui.proof.model.jpa.Order;
import com.tui.proof.model.jpa.OrderQuanity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderDTO extends CreateOrderDTO {
    private Long id;
    public UpdateOrderDTO(Long id, String deliveryAddress, OrderQuanity quantity, Long client, double unitPrice) {
        super(deliveryAddress, quantity, client, unitPrice);
        this.id = id;
    }
}
