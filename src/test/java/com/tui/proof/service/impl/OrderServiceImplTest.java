package com.tui.proof.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tui.proof.BaseTest;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class OrderServiceImplTest extends BaseTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private OrderServiceImpl classToTest;

    @BeforeEach
    void setUp() {
      MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveOrder_Successfully() throws ClientNotFoundException {
      CreateOrderDTO createOrderDTO = generateCompleteCreateOrderDto(0L);
      Client client = generateFakeClient(0L);

      when(clientRepository.findById(0L)).thenReturn(Optional.of(client));

      Order savedOrder = generateFakeOrder(0L);
      when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(savedOrder);

      // Test
      Order result = classToTest.save(createOrderDTO);
      assertNotNull(result);
      assertEquals(createOrderDTO.getClient(), result.getClient().getId());
      assertEquals(createOrderDTO.getDeliveryAddress(), result.getDeliveryAddress());
      assertEquals(createOrderDTO.getQuantityPilotes().getValue(), result.getQuantityPilotes());
      assertEquals(createOrderDTO.getUnitPrice(), result.getUnitPrice());
      assertNotNull(result.getOrderDate());
    }

    @Test
    void saveOrderWithNotExistingClient_ReturnNotFoundException() {
      //when
      CreateOrderDTO createOrderDTO = generateCompleteCreateOrderDto(0L);
      when(clientRepository.findById(0L)).thenReturn(Optional.empty());

      // then
      assertThrows(ClientNotFoundException.class, () -> classToTest.save(createOrderDTO));

      // verify
      verify(clientRepository, times(1)).findById(anyLong());
      verify(orderRepository, times(0)).saveAndFlush(any(Order.class));

    }

    @Test
    void updateOrder_Successfully() throws OrderNotEditableException, OrderNotFoundException, ClientNotFoundException {
      // Mock data
      UpdateOrderDTO updateOrderDTO = generateUpdateOrderDto(2L);

      Order order = generateFakeOrder(2L);
      order.setOrderDate(new Date(System.currentTimeMillis() - (4 * 60 * 1000)));

      Client client = generateFakeClient(2L);

      when(orderRepository.findById(2L)).thenReturn(Optional.of(order));
      when(clientRepository.findById(2L)).thenReturn(Optional.of(client));
      when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(order);

      // Test
      Order result = classToTest.udpate(updateOrderDTO);
      assertNotNull(result);
      assertEquals(updateOrderDTO.getId(), order.getId());
      assertEquals(updateOrderDTO.getClient(), order.getClient().getId());
      assertEquals(updateOrderDTO.getUnitPrice(), order.getUnitPrice());
      assertEquals(updateOrderDTO.getQuantityPilotes().getValue(), order.getQuantityPilotes());
      assertEquals(updateOrderDTO.getDeliveryAddress(), order.getDeliveryAddress());
    }

  @Test
  void updateOrderOutOfTime_returnOrderNotEditableException() {
    // Mock data
    UpdateOrderDTO updateOrderDTO = generateUpdateOrderDto(2L);

    Order order = generateFakeOrder(2L);
    order.setOrderDate(new Date(System.currentTimeMillis() - (6 * 60 * 1000)));

    when(orderRepository.findById(2L)).thenReturn(Optional.of(order));

    // Test
    assertThrows(OrderNotEditableException.class, () -> classToTest.udpate(updateOrderDTO));

    // verify
    verify(orderRepository, times(1)).findById(anyLong());
    verify(clientRepository, times(0)).findById(anyLong());
    verify(orderRepository, times(0)).saveAndFlush(any(Order.class));
  }

  @Test
  void updateNotExistentOrder_returnOrderNotFoundException() {
    // Mock data
    UpdateOrderDTO updateOrderDTO = generateUpdateOrderDto(2L);

    Order order = generateFakeOrder(2L);
    order.setOrderDate(new Date(System.currentTimeMillis() - (6 * 60 * 1000)));

    when(orderRepository.findById(2L)).thenReturn(Optional.empty());

    // Test
    assertThrows(OrderNotFoundException.class, () -> classToTest.udpate(updateOrderDTO));

    // verify
    verify(orderRepository, times(1)).findById(anyLong());
    verify(clientRepository, times(0)).findById(anyLong());
    verify(orderRepository, times(0)).saveAndFlush(any(Order.class));
  }

  @Test
  void updateOrderWithNotExistentClient_returnClientNotFoundException() {
    // Mock data
    UpdateOrderDTO updateOrderDTO = generateUpdateOrderDto(2L);

    Order order = generateFakeOrder(2L);
    order.setOrderDate(new Date(System.currentTimeMillis() - (3 * 60 * 1000)));

    when(orderRepository.findById(2L)).thenReturn(Optional.of(order));
    when(clientRepository.findById(2L)).thenReturn(Optional.empty());

    // Test
    assertThrows(ClientNotFoundException.class, () -> classToTest.udpate(updateOrderDTO));

    // verify
    verify(orderRepository, times(1)).findById(anyLong());
    verify(clientRepository, times(1)).findById(anyLong());
    verify(orderRepository, times(0)).saveAndFlush(any(Order.class));
  }

  @Test
  void testSearchWithFilter() {
    // Mock data
    OrderFilerDTO orderFilterDTO = new OrderFilerDTO();
    orderFilterDTO.setFirstName("");
    orderFilterDTO.setLastName("Doe");
    orderFilterDTO.setEmail("");

    // Mocking repository behavior
    Client client1 = new Client(1L, "John", "Doe", "john.doe@example.com", "234567890");
    Client client2 = new Client(2L, "Alice", "Smith", "alice.smith@example.com", "326548789");
    Client client3 = new Client(3L, "Bob", "Johnson", "bob.johnson@example.com", "987441223");
    Order order1 = generateFakeOrder(1L, client1);
    Order order2 = generateFakeOrder(2L, client2);
    Order order3 = generateFakeOrder(3L, client3);


    List<Order> mockOrders = Arrays.asList(order1, order2, order3);
    when(orderRepository.findAll()).thenReturn(mockOrders);

    // Performing the search
    List<Order> result = classToTest.search(orderFilterDTO);

    // Asserting the result
    assertEquals(1, result.size());
    assertEquals("John", result.get(0).getClient().getFirstName());
    assertEquals("Doe", result.get(0).getClient().getLastName());
    assertEquals("john.doe@example.com", result.get(0).getClient().getEmailAddress());
  }

  @Test
  void testSearchWithEmptyFilter_returnsAllTheSavedClients() {
    // Mock data
    OrderFilerDTO orderFilterDTO = new OrderFilerDTO();
    orderFilterDTO.setFirstName("");
    orderFilterDTO.setLastName("");
    orderFilterDTO.setEmail("");

    // Mocking repository behavior
    Client client1 = new Client(1L, "John", "Doe", "john.doe@example.com", "234567890");
    Client client2 = new Client(2L, "Alice", "Smith", "alice.smith@example.com", "326548789");
    Client client3 = new Client(3L, "Bob", "Johnson", "bob.johnson@example.com", "987441223");
    Order order1 = generateFakeOrder(1L, client1);
    Order order2 = generateFakeOrder(2L, client2);
    Order order3 = generateFakeOrder(3L, client3);


    List<Order> mockOrders = Arrays.asList(order1, order2, order3);
    when(orderRepository.findAll()).thenReturn(mockOrders);

    // Performing the search
    List<Order> result = classToTest.search(orderFilterDTO);

    // Asserting the result
    assertEquals(3, result.size());
    assertEquals(1L, result.get(0).getId());
    assertEquals(2L, result.get(1).getId());
    assertEquals(3L, result.get(2).getId());
  }

  @Test
  void testSearchWithPartialMatchForNameFilter_returnsTheSelectedClients() {
    // Mock data
    OrderFilerDTO orderFilterDTO = new OrderFilerDTO();
    orderFilterDTO.setFirstName("Bob");
    orderFilterDTO.setLastName("");
    orderFilterDTO.setEmail("");

    // Mocking repository behavior
    Client client1 = new Client(1L, "John", "Doe", "john.doe@example.com", "234567890");
    Client client2 = new Client(2L, "Alice", "Smith", "alice.smith@example.com", "326548789");
    Client client3 = new Client(3L, "Bob", "Johnson", "bob.johnson@example.com", "987441223");
    Client client4 = new Client(4L, "Boby", "Johanssen", "boby.johnssen@example.com", "8412355687");
    Order order1 = generateFakeOrder(1L, client1);
    Order order2 = generateFakeOrder(2L, client2);
    Order order3 = generateFakeOrder(3L, client3);
    Order order4 = generateFakeOrder(4L, client4);


    List<Order> mockOrders = Arrays.asList(order1, order2, order3, order4);
    when(orderRepository.findAll()).thenReturn(mockOrders);

    // Performing the search
    List<Order> result = classToTest.search(orderFilterDTO);

    // Asserting the result
    assertEquals(2, result.size());
    assertEquals(3L, result.get(0).getId());
    assertEquals("Bob", result.get(0).getClient().getFirstName());
    assertEquals(4L, result.get(1).getId());
    assertEquals("Boby", result.get(1).getClient().getFirstName());
  }

  @Test
  void testSearchWithPartialMatchForEmailFilter_returnsTheSelectedClients() {
    // Mock data
    OrderFilerDTO orderFilterDTO = new OrderFilerDTO();
    orderFilterDTO.setFirstName("");
    orderFilterDTO.setLastName("");
    orderFilterDTO.setEmail("john");

    // Mocking repository behavior
    Client client1 = new Client(1L, "John", "Doe", "john.doe@example.com", "234567890");
    Client client2 = new Client(2L, "Alice", "Smith", "alice.smith@example.com", "326548789");
    Client client3 = new Client(3L, "Bob", "Johnson", "bob.johnson@example.com", "987441223");
    Client client4 = new Client(4L, "Boby", "Johanssen", "boby.johnssen@example.com", "8412355687");
    Order order1 = generateFakeOrder(1L, client1);
    Order order2 = generateFakeOrder(2L, client2);
    Order order3 = generateFakeOrder(3L, client3);
    Order order4 = generateFakeOrder(4L, client4);


    List<Order> mockOrders = Arrays.asList(order1, order2, order3, order4);
    when(orderRepository.findAll()).thenReturn(mockOrders);

    // Performing the search
    List<Order> result = classToTest.search(orderFilterDTO);

    // Asserting the result
    assertEquals(3, result.size());
    assertEquals(1L, result.get(0).getId());
    assertEquals("John", result.get(0).getClient().getFirstName());
    assertEquals(3L, result.get(1).getId());
    assertEquals("Bob", result.get(1).getClient().getFirstName());
    assertEquals(4L, result.get(2).getId());
    assertEquals("Boby", result.get(2).getClient().getFirstName());
  }

}
