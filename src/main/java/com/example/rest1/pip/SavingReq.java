package com.example.rest1.pip;

import java.math.BigDecimal;
import com.example.rest1.model.*;

public record SavingReq(String name, BigDecimal targetAmount, String status, Long ownerId, Long accountId) {
}
