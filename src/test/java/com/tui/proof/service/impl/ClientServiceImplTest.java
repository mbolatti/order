package com.tui.proof.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tui.proof.BaseTest;
import com.tui.proof.model.ClientNotFoundException;
import com.tui.proof.model.dto.request.CreateClientDTO;
import com.tui.proof.model.dto.request.UpdateClientDTO;
import com.tui.proof.model.jpa.Client;
import com.tui.proof.repository.ClientRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyLong;

@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class ClientServiceImplTest extends BaseTest {

    @Mock
    private ClientRepository clientRepository;
    @InjectMocks
    private ClientServiceImpl classToTest;

    @Test
    void saveClient_ReturnsSavedClient() {
        // Given
        CreateClientDTO createClientDTO = generateCompleteCreateClientDto();
        Client expectedClient = generateFakeClient(1L);

        when(clientRepository.saveAndFlush(any(Client.class)))
            .thenReturn(expectedClient);

        // When
        Client savedClient = classToTest.save(createClientDTO);

        // Then
        assertNotNull(savedClient);
        assertEquals(expectedClient, savedClient);
        verify(clientRepository, times(1)).saveAndFlush(any(Client.class));
    }

    @Test
    void updateClient_ReturnsUpdatedClient() throws ClientNotFoundException {
        // Given
        UpdateClientDTO updateClientDTO = generateCompleteUpdateClientDto(1L);
        Client existingClient = generateFakeClient(1L);

        when(clientRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(existingClient));
        when(clientRepository.saveAndFlush(any(Client.class))).thenReturn(existingClient);

        // When
        Client updatedClient = classToTest.udpate(updateClientDTO);

        // Then
        assertNotNull(updatedClient);
        assertEquals(existingClient, updatedClient);
        verify(clientRepository, times(1)).findById(anyLong());
        verify(clientRepository, times(1)).saveAndFlush(any(Client.class));
    }

    @Test
    void updateNotExistingClient_ReturnsException() throws ClientNotFoundException {
        // Given
        UpdateClientDTO updateClientDTO = generateCompleteUpdateClientDto(1L);
        when(clientRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // When and Then
        assertThrows(ClientNotFoundException.class, () -> classToTest.udpate(updateClientDTO));

        // verify
        verify(clientRepository, times(1)).findById(anyLong());
        verify(clientRepository, times(0)).saveAndFlush(any(Client.class));
    }

    @Test
    void search_ReturnsListOfClients() {
        // Given
        List<Client> expectedClients = Arrays.asList(generateFakeClient(1L), generateFakeClient(2L));

        when(clientRepository.findAll()).thenReturn(expectedClients);

        // When
        List<Client> foundClients = classToTest.search();

        // Then
        assertNotNull(foundClients);
        assertEquals(expectedClients.size(), foundClients.size());
        assertEquals(expectedClients, foundClients);
        verify(clientRepository, times(1)).findAll();
    }

}