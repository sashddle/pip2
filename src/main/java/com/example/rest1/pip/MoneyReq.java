package com.example.rest1.pip;

import java.math.BigDecimal;
import com.example.rest1.model.*;

public record MoneyReq(String name, BigDecimal initialBalance, Long ownerId) {
}
