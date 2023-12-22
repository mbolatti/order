package com.tui.proof.ws.controller;

import com.tui.proof.BaseTest;
import com.tui.proof.model.ClientNotFoundException;
import com.tui.proof.model.dto.request.CreateClientDTO;
import com.tui.proof.model.dto.request.UpdateClientDTO;
import com.tui.proof.model.dto.response.GetClientDTO;
import com.tui.proof.model.jpa.Client;
import com.tui.proof.service.ClientService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class ClientsControllerTest extends BaseTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientsController classToTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOK() {
        // Mock input
        CreateClientDTO createClientDTO = generateCompleteCreateClientDto();
        // Mock service behavior
        when(clientService.save(createClientDTO)).thenReturn(new Client());

        // Perform the controller action
        ResponseEntity<GetClientDTO> response = classToTest.create(createClientDTO);

        // Verify service method invocation
        verify(clientService, times(1)).save(createClientDTO);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testWhenGotUnexpectedException_returnsGenericException() {
        // Mock input
        CreateClientDTO createClientDTO = generateCompleteCreateClientDto();
        // Mock service behavior
        when(clientService.save(createClientDTO)).thenThrow(new RuntimeException("some error happens"));

        // Perform the controller action
        assertThrows(Exception.class, () -> classToTest.create(createClientDTO));

        // Verify service method invocation
        verify(clientService, times(1)).save(createClientDTO);
    }

    @Test
    void testUpdateOK() throws ClientNotFoundException {
        // Mock input
        UpdateClientDTO updateClientDTO = generateCompleteUpdateClientDto(1L);
        // Mock service behavior
        when(clientService.udpate(updateClientDTO)).thenReturn(generateFakeClient(1l));

        // Perform the controller action
        ResponseEntity<GetClientDTO> response = classToTest.update(updateClientDTO);

        // Verify service method invocation
        verify(clientService, times(1)).udpate(updateClientDTO);

        // Assert the response
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    void testWhenClientNotExist_returnsClientNotFoundException() throws ClientNotFoundException {
        // Mock input
        UpdateClientDTO updateClientDTO = generateCompleteUpdateClientDto(1L);
        // Mock service behavior
        when(clientService.udpate(updateClientDTO)).thenThrow(new ClientNotFoundException(1L));

        // Perform the controller action
        assertThrows(ClientNotFoundException.class, () -> classToTest.update(updateClientDTO));

        // Verify service method invocation
        verify(clientService, times(1)).udpate(updateClientDTO);
    }

    @Test
    void testSearch() {
        // Mock service behavior
        when(clientService.search()).thenReturn(Collections.singletonList(generateFakeClient(2L)));

        // Perform the controller action
        ResponseEntity<List<GetClientDTO>> response = classToTest.search();

        // Verify service method invocation
        verify(clientService, times(1)).search();

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}