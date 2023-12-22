package com.tui.proof.model.dto.request;

import com.tui.proof.model.jpa.OrderQuanity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CreateOrderDTO {
    @NotEmpty(message = "The delivery Address is mandatory")
    protected String deliveryAddress;
    protected OrderQuanity quantityPilotes;
    protected Long client;
    protected double unitPrice = 1.33;
}
