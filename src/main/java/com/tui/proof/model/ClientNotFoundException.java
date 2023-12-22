package com.tui.proof.model;

public class ClientNotFoundException extends Throwable {
    private String message;

    public ClientNotFoundException(Long orderId) {
        this.message = String.format("The client: %s not found", orderId);
    }

    public String getMessage() {
        return message;
    }
}
