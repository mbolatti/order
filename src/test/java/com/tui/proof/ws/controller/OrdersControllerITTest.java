package com.tui.proof.ws.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.BaseTest;
import com.tui.proof.model.dto.request.CreateOrderDTO;
import com.tui.proof.model.dto.request.OrderFilerDTO;
import com.tui.proof.model.dto.request.UpdateOrderDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = {"/data.sql"})
public class OrdersControllerITTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//----------------- CREATE ORDER ---------------------------
    @Test
    public void givenValidInput_whenCreateOrder_ReturnsOK() throws Exception {
        mockMvc.perform(post("/api/v1/orders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateCompleteCreateOrderDto(1L))))
            .andExpect(status().isOk());
    }

    @Test
    public void givenInvalidOrderAddressInput_whenCreateOrder_returnsBadRequest() throws Exception {
        CreateOrderDTO createOrderDTO = generateCompleteCreateOrderDto(1L);
        createOrderDTO.setDeliveryAddress("");
        mockMvc.perform(post("/api/v1/orders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors", hasItem("The delivery Address is mandatory")));
    }

    @Test
    public void givenInvalidCliendInput_whenCreateOrder_returnsClientNotFound() throws Exception {
        CreateOrderDTO createOrderDTO = generateCompleteCreateOrderDto(1L);
        createOrderDTO.setClient(200L);
        mockMvc.perform(post("/api/v1/orders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderDTO)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("The client: 200 not found")));
    }


    //----------------- UPDATE ORDER ---------------------------
    @Test
    public void givenValidInput_whenUpdateOrder_returnsOK() throws Exception {
        mockMvc.perform(put("/api/v1/orders/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateUpdateOrderDto(1L))))
            .andExpect(status().isAccepted());
    }

    @Test
    public void givenInvalidOrderAddressInput_whenUpdateOrder_returnsBadRequest() throws Exception {
        UpdateOrderDTO updateOrderDTO = generateUpdateOrderDto(1L);
        updateOrderDTO.setDeliveryAddress("");
        mockMvc.perform(put("/api/v1/orders/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateOrderDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors", hasItem("The delivery Address is mandatory")));
    }

    @Test
    public void givenValidOrderButOutOfTimeInput_whenUpdateOrder_returnsOrderNotEditableException() throws Exception {
        UpdateOrderDTO updateOrderDTO = generateUpdateOrderDto(3L);
        mockMvc.perform(put("/api/v1/orders/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateOrderDTO)))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.message", is("The order: 3 can not be edited because is in preparation")));
    }

    @Test
    public void giveInvalidCliendInput_whenUpdateOrder_returnsNotFound() throws Exception {
        UpdateOrderDTO updateOrderDTO = generateUpdateOrderDto(1L);
        updateOrderDTO.setClient(200L);
        mockMvc.perform(put("/api/v1/orders/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateOrderDTO)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("The client: 200 not found")));
    }


    //----------------- SEARCH ORDER ---------------------------


    @Test
    public void givenValidInput_whenSearchOrder_ReturnsOK() throws Exception {
        OrderFilerDTO orderFilterDTO = new OrderFilerDTO();
        orderFilterDTO.setFirstName("");
        orderFilterDTO.setLastName("");
        orderFilterDTO.setEmail("");
        mockMvc.perform(get("/api/v1/orders/search")
                .param("firstName", orderFilterDTO.getFirstName())
                .param("lastName", orderFilterDTO.getLastName())
                .param("email", orderFilterDTO.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderFilterDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").isNotEmpty());
    }

    @Test
    public void givenValidInput_whenSearchOrder_ReturnsOKButNotMatch() throws Exception {
        OrderFilerDTO orderFilterDTO = new OrderFilerDTO();
        orderFilterDTO.setFirstName("martin");
        orderFilterDTO.setLastName("");
        orderFilterDTO.setEmail("");
        mockMvc.perform(get("/api/v1/orders/search")
                .param("firstName", orderFilterDTO.getFirstName())
                .param("lastName", orderFilterDTO.getLastName())
                .param("email", orderFilterDTO.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderFilterDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void givenValidInput_whenSearchOrder_ReturnsOKWithMatch() throws Exception {
        OrderFilerDTO orderFilterDTO = new OrderFilerDTO();
        orderFilterDTO.setFirstName("Three");
        orderFilterDTO.setLastName("");
        orderFilterDTO.setEmail("");
        mockMvc.perform(get("/api/v1/orders/search")
                .param("firstName", orderFilterDTO.getFirstName())
                .param("lastName", orderFilterDTO.getLastName())
                .param("email", orderFilterDTO.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderFilterDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").isNotEmpty());
    }
}