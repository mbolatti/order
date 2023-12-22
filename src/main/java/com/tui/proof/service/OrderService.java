package com.tui.proof.service;

import com.tui.proof.model.ClientNotFoundException;
import com.tui.proof.model.OrderNotEditableException;
import com.tui.proof.model.OrderNotFoundException;
import com.tui.proof.model.dto.request.CreateOrderDTO;
import com.tui.proof.model.dto.request.OrderFilerDTO;
import com.tui.proof.model.dto.request.UpdateOrderDTO;
import com.tui.proof.model.jpa.Order;
import java.util.List;

public interface OrderService {
    Order save(CreateOrderDTO order) throws ClientNotFoundException;

    Order udpate(UpdateOrderDTO orderDTO) throws OrderNotEditableException, OrderNotFoundException, ClientNotFoundException;

    List<Order> search(OrderFilerDTO orderSpec);
}
