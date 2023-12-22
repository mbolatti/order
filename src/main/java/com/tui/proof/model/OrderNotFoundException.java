package com.tui.proof.model;

public class OrderNotFoundException extends Throwable {
    private String message;

    public OrderNotFoundException(Long orderId) {
        this.message = String.format("The order: %s not found", orderId);
    }

    public String getMessage() {
        return message;
    }
}
