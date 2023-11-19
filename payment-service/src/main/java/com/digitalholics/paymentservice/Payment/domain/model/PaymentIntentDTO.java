package com.digitalholics.paymentservice.Payment.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PaymentIntentDTO {
    public enum Currency {
        PEN
    }
    private int amount;
    private Currency currency;
}
