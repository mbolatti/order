package com.tui.proof.ws.controller;

import com.tui.proof.model.ClientNotFoundException;
import com.tui.proof.model.OrderNotEditableException;
import com.tui.proof.model.OrderNotFoundException;
import com.tui.proof.model.dto.request.*;
import com.tui.proof.model.dto.response.GetClientDTO;
import com.tui.proof.service.ClientService;
import com.tui.proof.service.MapperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/api/v1/clients")
@Validated
public class ClientsController {

    @Autowired
    private ClientService clientService;

    @Operation(summary = "Creates a new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created the client"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PostMapping(value = "/create", consumes = {"application/json"})
    public ResponseEntity<GetClientDTO> create(@Valid @RequestBody CreateClientDTO clientDto) {
        return ResponseEntity.ok(MapperService.toClientDTO(clientService.save(clientDto)));
    }


    @Operation(summary = "Update a Client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Update the client"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PutMapping(value = "/update", consumes = {"application/json"})
    public ResponseEntity<GetClientDTO> update(@Valid @RequestBody UpdateClientDTO updateClientDTO) throws ClientNotFoundException {
        return new ResponseEntity<>(MapperService.toClientDTO(clientService.udpate(updateClientDTO)), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get All clients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients list"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @GetMapping(value = "/search", produces = {"application/json"})
    public ResponseEntity<List<GetClientDTO>> search() {
        List<GetClientDTO> orderList = clientService.search().stream().map(MapperService::toClientDTO).collect(Collectors.toList());
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }
}
