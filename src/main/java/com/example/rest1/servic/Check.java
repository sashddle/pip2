package com.example.rest1.servic;

import com.example.rest1.model.*;
import com.example.rest1.pip.*;
import com.example.rest1.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

final class Check {
    private Check() {
    }
    static void require(boolean ok, String error) {
        if (!ok) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
    }
    static void text(String s) {
        require(s != null && !s.isBlank(), "Text");
    }
    static void positive(BigDecimal n) {
        require(n != null && n.signum() > 0, "positive");
    }
    static void nonnegative(BigDecimal n) {
        require(n != null && n.signum() >= 0, "negative");
    }
    static ResponseStatusException missing(String name) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, name + "not found");
    }
}
