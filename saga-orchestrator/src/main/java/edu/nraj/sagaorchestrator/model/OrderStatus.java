package edu.nraj.sagaorchestrator.model;

public enum OrderStatus {
    VERIFICATION_PENDING,
    CONSUMER_VERIFIED,
    ORDER_REJECTED,
    ORDER_APPROVED,
    CARD_NOT_AUTHORIZED,
    CARD_AUTHORIZED,
    TICKET_NOT_CREATED,
    TICKET_CREATED,
    CONSUMER_VERIFICATION_FAILED,

    REJECTED_CONSUMER_NOT_VERIFIED,
    REJECTED_TICKET_NOT_CREATED
}
