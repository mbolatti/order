package com.tui.proof.ws.controller;

import com.tui.proof.BaseTest;
import com.tui.proof.model.ClientNotFoundException;
import com.tui.proof.model.OrderNotEditableException;
import com.tui.proof.model.OrderNotFoundException;
import com.tui.proof.model.dto.request.CreateClientDTO;
import com.tui.proof.model.dto.request.CreateOrderDTO;
import com.tui.proof.model.dto.request.OrderFilerDTO;
import com.tui.proof.model.dto.request.UpdateClientDTO;
import com.tui.proof.model.dto.request.UpdateOrderDTO;
import com.tui.proof.model.dto.response.GetOrderDTO;
import com.tui.proof.service.OrderService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class OrdersControllerTest extends BaseTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrdersController classToTest;

    @Test
    void createOrderTest() throws ClientNotFoundException {
        // Mocking the necessary behavior of the orderService
        when(orderService.save(any(CreateOrderDTO.class))).thenReturn(generateFakeOrder(1L));

        // Creating a mock request body
        CreateOrderDTO mockOrderDTO = generateCompleteCreateOrderDto(1L);

        // Testing the create method in the controller
        ResponseEntity<GetOrderDTO> response = classToTest.create(mockOrderDTO);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GetOrderDTO getOrderDTOResponde = response.getBody();
        assertNotNull(getOrderDTOResponde.getId());
        assertEquals(mockOrderDTO.getClient(), getOrderDTOResponde.getClient().getId());
        assertEquals(mockOrderDTO.getUnitPrice(), getOrderDTOResponde.getUnitPrice());
        assertEquals(mockOrderDTO.getQuantityPilotes(), getOrderDTOResponde.getQuantityPilotes());
        assertEquals(mockOrderDTO.getDeliveryAddress(), getOrderDTOResponde.getDeliveryAddress());
    }

    @Test
    void testUnexpectedException_whenCreate_returnsGenericException() throws ClientNotFoundException {
        // Mock input
        CreateOrderDTO createOrderDTO = generateCompleteCreateOrderDto(1L);
        // Mock service behavior
        when(orderService.save(createOrderDTO)).thenThrow(new RuntimeException("some error happens"));

        // Perform the controller action
        assertThrows(Exception.class, () -> classToTest.create(createOrderDTO));

        // Verify service method invocation
        verify(orderService, times(1)).save(createOrderDTO);
    }

    @Test
    void updateOrderTest() throws OrderNotFoundException, OrderNotEditableException, ClientNotFoundException {
        // Mocking the necessary behavior of the orderService
        when(orderService.udpate(any(UpdateOrderDTO.class))).thenReturn(generateFakeOrder(1L));

        // Creating a mock request body
        UpdateOrderDTO mockOrderDTO = generateUpdateOrderDto(1L);

        // Testing the update method in the controller
        ResponseEntity<GetOrderDTO> response = classToTest.update(mockOrderDTO);

        // Verify the response
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        GetOrderDTO getOrderDTOResponse = response.getBody();
        assertNotNull(getOrderDTOResponse);
        assertNotNull(getOrderDTOResponse.getId());
        assertEquals(mockOrderDTO.getId(), getOrderDTOResponse.getId());
        assertEquals(mockOrderDTO.getClient(), getOrderDTOResponse.getClient().getId());
        assertEquals(mockOrderDTO.getUnitPrice(), getOrderDTOResponse.getUnitPrice());
        assertEquals(mockOrderDTO.getQuantityPilotes(), getOrderDTOResponse.getQuantityPilotes());
        assertEquals(mockOrderDTO.getDeliveryAddress(), getOrderDTOResponse.getDeliveryAddress());
    }

    @Test
    void testWhenClientNotExist_returnsClientNotFoundException()
        throws ClientNotFoundException, OrderNotFoundException, OrderNotEditableException {
        // Mock input
        UpdateOrderDTO updateOrderDTO = generateUpdateOrderDto(1L);
        // Mock service behavior
        when(orderService.udpate(updateOrderDTO)).thenThrow(new ClientNotFoundException(1L));

        // Perform the controller action
        assertThrows(ClientNotFoundException.class, () -> classToTest.update(updateOrderDTO));

        // Verify service method invocation
        verify(orderService, times(1)).udpate(updateOrderDTO);
    }

    @Test
    void testWhenOrderNotExist_returnOrderNotFoundException()
        throws ClientNotFoundException, OrderNotFoundException, OrderNotEditableException {
        // Mock input
        UpdateOrderDTO updateOrderDTO = generateUpdateOrderDto(1L);
        // Mock service behavior
        when(orderService.udpate(updateOrderDTO)).thenThrow(new OrderNotFoundException(1L));

        // Perform the controller action
        assertThrows(OrderNotFoundException.class, () -> classToTest.update(updateOrderDTO));

        // Verify service method invocation
        verify(orderService, times(1)).udpate(updateOrderDTO);
    }

    @Test
    void testWhenOrderIsOutOfTime_returnOrderNotEditableException()
        throws ClientNotFoundException, OrderNotFoundException, OrderNotEditableException {
        // Mock input
        UpdateOrderDTO updateOrderDTO = generateUpdateOrderDto(1L);
        // Mock service behavior
        when(orderService.udpate(updateOrderDTO)).thenThrow(new OrderNotEditableException(1L));

        // Perform the controller action
        assertThrows(OrderNotEditableException.class, () -> classToTest.update(updateOrderDTO));

        // Verify service method invocation
        verify(orderService, times(1)).udpate(updateOrderDTO);
    }

    @Test
    void searchOrdersTest() {
        // Mocking the necessary behavior of the orderService
        when(orderService.search(any(OrderFilerDTO.class))).thenReturn( Collections.singletonList(generateFakeOrder(1L)));

        // Testing the search method in the controller
        ResponseEntity<List<GetOrderDTO>> response = classToTest.search("John", "Doe","john@example.com");

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}