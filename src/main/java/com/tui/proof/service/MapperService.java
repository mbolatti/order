package com.tui.proof.service;

import com.tui.proof.model.dto.response.GetClientDTO;
import com.tui.proof.model.dto.response.GetOrderDTO;
import com.tui.proof.model.jpa.Client;
import com.tui.proof.model.jpa.Order;
import com.tui.proof.model.jpa.OrderQuanity;

public class MapperService {
    public static GetOrderDTO toGetOrderDTO(Order order){
        return GetOrderDTO.builder()
                .orderDate(order.getOrderDate())
                .id(order.getId())
                .client(toClientDTO(order.getClient()))
                .quantityPilotes(OrderQuanity.valueOf(order.getQuantityPilotes()))
                .unitPrice(order.getUnitPrice())
                .totalOrder(order.getQuantityPilotes() * order.getUnitPrice())
                .deliveryAddress(order.getDeliveryAddress())
                .build();
    }

    public static GetClientDTO toClientDTO(Client client) {
        return GetClientDTO.builder()
                .emailAddress(client.getEmailAddress())
                .lastName(client.getLastName())
                .firstName(client.getFirstName())
                .phoneNumber(client.getPhoneNumber())
                .id(client.getId())
                .build();
    }
}
