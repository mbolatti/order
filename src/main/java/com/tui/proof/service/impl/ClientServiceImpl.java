package com.tui.proof.service.impl;

import com.tui.proof.model.ClientNotFoundException;
import com.tui.proof.model.dto.request.CreateClientDTO;
import com.tui.proof.model.dto.request.UpdateClientDTO;
import com.tui.proof.model.jpa.Client;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.service.ClientService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    ClientRepository clientRepository;


    @Override
    public Client save(CreateClientDTO clientDto) {
        return clientRepository.saveAndFlush(mapToClient(clientDto, new Client()));
    }

    @Override
    public Client udpate(UpdateClientDTO clientDTO) throws ClientNotFoundException {
        Client client = mergeClient(clientDTO);
        return clientRepository.saveAndFlush(client);
    }

    private Client mergeClient(UpdateClientDTO clientDTO) throws ClientNotFoundException {
        Optional<Client> clientOpt = clientRepository.findById(clientDTO.getId());
        if (clientOpt.isEmpty()) {
            throw new ClientNotFoundException(clientDTO.getId());
        }
        return mapToClient(clientDTO, clientOpt.get());
    }

    private static Client mapToClient(CreateClientDTO clientDTO, Client client) {
        client.setLastName(clientDTO.getLastName());
        client.setEmailAddress(clientDTO.getEmailAddress());
        client.setPhoneNumber(clientDTO.getPhoneNumber());
        client.setFirstName(clientDTO.getFirstName());
        return client;
    }

    @Override
    public List<Client> search() {
        return clientRepository.findAll();
    }
}
