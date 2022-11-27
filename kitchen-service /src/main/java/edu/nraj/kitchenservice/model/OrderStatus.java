package edu.nraj.kitchenservice.model;

public enum OrderStatus {
    CONSUMER_VERIFIED,

    TICKET_CREATED,
    TICKET_NOT_CREATED,
    TICKET_APPROVED,
    TICKET_REJECTED,

    CARD_NOT_AUTHORIZED,
    CARD_AUTHORIZED,
}
