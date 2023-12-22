package com.tui.proof.ws.controller;

import com.tui.proof.model.ClientNotFoundException;
import com.tui.proof.model.OrderNotEditableException;
import com.tui.proof.model.OrderNotFoundException;
import com.tui.proof.model.dto.request.OrderFilerDTO;
import com.tui.proof.model.dto.request.UpdateOrderDTO;
import com.tui.proof.model.dto.response.GetOrderDTO;
import com.tui.proof.model.jpa.Order;
import com.tui.proof.service.MapperService;
import com.tui.proof.service.OrderService;
import com.tui.proof.model.dto.request.CreateOrderDTO;
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
@RequestMapping("/api/v1/orders")
@Validated
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Creates a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created the order"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PostMapping(value = "/create", consumes = {"application/json"})
    public ResponseEntity<GetOrderDTO> create(@Valid @RequestBody CreateOrderDTO orderDto) throws ClientNotFoundException {
        return ResponseEntity.ok(MapperService.toGetOrderDTO(orderService.save(orderDto)));
    }


    @Operation(summary = "Update a order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Update the order"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order/Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PutMapping(value = "/update", consumes = {"application/json"})
    public ResponseEntity<GetOrderDTO> update(@Valid @RequestBody UpdateOrderDTO orderDto) throws OrderNotFoundException, OrderNotEditableException, ClientNotFoundException {
        return new ResponseEntity<>(MapperService.toGetOrderDTO(orderService.udpate(orderDto)), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Search orders by criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @GetMapping(value = "/search", produces = {"application/json"})
    public ResponseEntity<List<GetOrderDTO>> search(@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName, @RequestParam(required = false) String email) {
        List<GetOrderDTO> orderList = orderService.search(OrderFilerDTO.of(firstName, lastName, email)).stream().map(MapperService::toGetOrderDTO).collect(Collectors.toList());
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }
}