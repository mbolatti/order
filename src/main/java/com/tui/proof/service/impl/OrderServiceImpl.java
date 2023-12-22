package com.tui.proof.service.impl;

import com.tui.proof.model.ClientNotFoundException;
import com.tui.proof.model.OrderNotEditableException;
import com.tui.proof.model.OrderNotFoundException;
import com.tui.proof.model.dto.request.CreateOrderDTO;
import com.tui.proof.model.dto.request.OrderFilerDTO;
import com.tui.proof.model.dto.request.UpdateOrderDTO;
import com.tui.proof.model.jpa.Client;
import com.tui.proof.model.jpa.Order;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.repository.OrderRepository;
import com.tui.proof.service.OrderService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ClientRepository clientRepository;

    public Order save(CreateOrderDTO orderdto) throws ClientNotFoundException {
        return orderRepository.saveAndFlush(mergeOrder(new Order(), orderdto));
    }

    public Order udpate(UpdateOrderDTO orderDTO) throws OrderNotEditableException, OrderNotFoundException, ClientNotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(orderDTO.getId());
        Date now = new Date();
        long fiveMinutesInMillis = 5 * 60 * 1000; // 5 minutes in milliseconds
        Date fiveMinutesBefore = new Date(now.getTime() - fiveMinutesInMillis);
        if (orderOptional.isPresent()) {
            if (orderOptional.get().getOrderDate().after(fiveMinutesBefore) && orderOptional.get().getOrderDate().before(now)){
                return orderRepository.saveAndFlush(mergeOrder(orderOptional.get(), orderDTO));
            } else {
                throw new OrderNotEditableException(orderDTO.getId());
            }
        } else {
            throw new OrderNotFoundException(orderDTO.getId());
        }
    }

    private Order mergeOrder(Order order, CreateOrderDTO orderDTO) throws ClientNotFoundException {
        Optional<Client> client = clientRepository.findById(orderDTO.getClient());
        if (client.isPresent()) {
            order.setClient(client.get());
        } else {
            throw new ClientNotFoundException(orderDTO.getClient());
        }

        order.setDeliveryAddress(orderDTO.getDeliveryAddress());

        order.setQuantityPilotes(orderDTO.getQuantityPilotes().getValue());
        order.setUnitPrice(orderDTO.getUnitPrice());
        if (order.getOrderDate() == null) {
            order.setOrderDate(new Date());
        }
        return order;
    }

    public List<Order> search(OrderFilerDTO orderSpec) {
        List<Order> orderList = orderRepository.findAll();
        if (!orderSpec.isEmpty()) {
            orderList = orderList.stream().filter(getOrderPredicate(orderSpec)).collect(Collectors.toList());
        }
        return orderList;
    }

    private static Predicate<Order> getOrderPredicate(OrderFilerDTO orderSpec) {
        return order ->
            (Strings.isNotBlank(orderSpec.getFirstName()) && order.getClient().getFirstName()
                .contains(orderSpec.getFirstName()))
                || (Strings.isNotBlank(orderSpec.getLastName()) && order.getClient().getLastName()
                .contains(orderSpec.getLastName()))
                || (Strings.isNotBlank(orderSpec.getEmail()) && order.getClient().getEmailAddress()
                .contains(orderSpec.getEmail()));
    }
}
