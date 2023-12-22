package com.tui.proof.model;

public class OrderNotEditableException extends Throwable {
    private String message;

    public OrderNotEditableException(Long orderId) {
        this.message = String.format("The order: %s can not be edited because is in preparation", orderId);
    }

    public String getMessage() {
        return message;
    }
}
