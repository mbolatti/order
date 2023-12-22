package com.tui.proof;

import com.tui.proof.model.dto.request.CreateClientDTO;
import com.tui.proof.model.dto.request.CreateOrderDTO;
import com.tui.proof.model.dto.request.UpdateClientDTO;
import com.tui.proof.model.dto.request.UpdateOrderDTO;
import com.tui.proof.model.jpa.Client;
import com.tui.proof.model.jpa.Order;
import com.tui.proof.model.jpa.OrderQuanity;
import java.util.Date;

public class BaseTest {

    protected UpdateOrderDTO generateUpdateOrderDto(Long id){
        return new UpdateOrderDTO(id, "some address"+id, OrderQuanity.FIVE, id, 1.99);
    }

    protected CreateClientDTO generateCompleteCreateClientDto(){
        return CreateClientDTO.builder()
                .firstName("firstMame")
                .emailAddress("email@email.com")
                .lastName("lastName")
                .build();

    }

    protected UpdateClientDTO generateCompleteUpdateClientDto(Long id){
        UpdateClientDTO updateClientDTO = new UpdateClientDTO(id);
        updateClientDTO.setId(id);
        updateClientDTO.setEmailAddress("email@some.com");
        updateClientDTO.setFirstName("firstN");
        updateClientDTO.setLastName("lastN");
        updateClientDTO.setPhoneNumber("123456598");
        return updateClientDTO;

    }
    protected CreateOrderDTO generateCompleteCreateOrderDto(Long id){
        return CreateOrderDTO.builder()
                .client(id)
                .deliveryAddress("some address"+id)
                .unitPrice(1.99)
                .quantityPilotes(OrderQuanity.FIVE)
                .build();
    }

    protected Order generateFakeOrder(Long id){
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setClient(generateFakeClient(id));
        order.setUnitPrice(1.99);
        order.setId(id);
        order.setQuantityPilotes(5);
        order.setDeliveryAddress("some address"+id);
        return order;
    }

    protected Order generateFakeOrder(Long id, Client client){
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setClient(client);
        order.setUnitPrice(1.99);
        order.setId(id);
        order.setQuantityPilotes(5);
        order.setDeliveryAddress("some address"+id);
        return order;
    }
    protected Client generateFakeClient(Long id){
        Client client = new Client();
        client.setId(id);
        client.setFirstName("fn"+id);
        client.setPhoneNumber("1234"+id);
        client.setEmailAddress("someEmail"+id);
        client.setLastName("ln"+id);
        return client;
    }
}

