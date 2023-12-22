package com.tui.proof.service;

import com.tui.proof.model.ClientNotFoundException;
import com.tui.proof.model.dto.request.CreateClientDTO;
import com.tui.proof.model.dto.request.UpdateClientDTO;
import com.tui.proof.model.jpa.Client;
import java.util.List;

public interface ClientService {
    Client save(CreateClientDTO clientDto);

    Client udpate(UpdateClientDTO clientDTO) throws ClientNotFoundException;

    List<Client> search();
}
