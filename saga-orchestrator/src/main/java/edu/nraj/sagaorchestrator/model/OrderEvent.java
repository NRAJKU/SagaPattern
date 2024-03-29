package edu.nraj.sagaorchestrator.model;

public enum OrderEvent {
    VERIFY_CONSUMER,

    CREATE_ORDER_TICKET,

    AUTHORIZE_CARD,

    APPROVE_ORDER,

    REJECT_ORDER_CONSUMER_NOT_VERIFIED,
    REJECT_ORDER_TICKET_NOT_CREATED,
    REJECT_ORDER_NOT_AUTHORIZED,
    REJECT_ORDER
}