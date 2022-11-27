package edu.nraj.sagaorchestrator.model;

public enum OrderState {
    INITIAL,
    VERIFYING_CONSUMER,

    CREATING_TICKET,

    AUTHORIZING_CARD,

    ORDER_APPROVED,
    ORDER_REJECTED,

    APPROVING_ORDER,

    REJECTED
}
