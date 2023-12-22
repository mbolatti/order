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
import com.tui.proof.model.dto.request.CreateClientDTO;
import com.tui.proof.model.dto.request.CreateOrderDTO;
import com.tui.proof.model.dto.request.OrderFilerDTO;
import com.tui.proof.model.dto.request.UpdateClientDTO;
import com.tui.proof.model.dto.request.UpdateOrderDTO;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.repository.OrderRepository;
import com.tui.proof.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql(scripts = {"/data.sql"})
public class ClientControllerITTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

   @Autowired
    private ObjectMapper objectMapper;

//----------------- CREATE CLIENT ---------------------------
    @Test
    public void givenValidInput_whenCreateClient_ReturnsOK() throws Exception {
        mockMvc.perform(post("/api/v1/clients/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateCompleteCreateClientDto())))
            .andExpect(status().isOk());
    }

    @Test
    public void givenInvalidOrderAddressInput_whenCreateOrder_returnsBadRequest() throws Exception {
        CreateClientDTO createClientDTO = generateCompleteCreateClientDto();
        createClientDTO.setFirstName("");
        createClientDTO.setLastName("");
        createClientDTO.setEmailAddress("");
        createClientDTO.setPhoneNumber("");
        mockMvc.perform(post("/api/v1/clients/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createClientDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors", hasItem("The first name is mandatory")))
            .andExpect(jsonPath("$.errors", hasItem("The last name is mandatory")))
            .andExpect(jsonPath("$.errors", hasItem("Email must be valid")))
            .andExpect(jsonPath("$.errors", hasItem("Phone number must contain 9 digits")));
    }

    //----------------- UPDATE CLIENT ---------------------------
    @Test
    public void givenValidInput_whenUpdateClient_returnsOK() throws Exception {
        mockMvc.perform(put("/api/v1/clients/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(generateCompleteUpdateClientDto(1L))))
            .andExpect(status().isAccepted());
    }

    @Test
    public void givenInvalidInput_whenUpdateClient_returnsBadRequestException() throws Exception {
        UpdateClientDTO updateClientDTO = generateCompleteUpdateClientDto(1L);
        updateClientDTO.setId(-1L);
        updateClientDTO.setEmailAddress("");
        updateClientDTO.setFirstName("");
        updateClientDTO.setLastName("");
        updateClientDTO.setPhoneNumber("");
        mockMvc.perform(put("/api/v1/clients/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateClientDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors", hasItem("The client id is mandatory")))
            .andExpect(jsonPath("$.errors", hasItem("The first name is mandatory")))
            .andExpect(jsonPath("$.errors", hasItem("The last name is mandatory")))
            .andExpect(jsonPath("$.errors", hasItem("Email must be valid")))
            .andExpect(jsonPath("$.errors", hasItem("Phone number must contain 9 digits")));
    }

    @Test
    public void giveInvalidClientInput_whenUpdateClient_returnsNotFound() throws Exception {
        UpdateClientDTO updateOrderDTO = generateCompleteUpdateClientDto(300L);
        mockMvc.perform(put("/api/v1/clients/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateOrderDTO)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("The client: 300 not found")));
    }

    //----------------- SEARCH ORDER ---------------------------

    @Test
    public void givenValidInput_whenSearchClient_ReturnsOK() throws Exception {
        mockMvc.perform(get("/api/v1/clients/search")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(6));
    }
}
