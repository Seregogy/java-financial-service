package com.financial.loan.domain.service;

import java.util.UUID;

public interface JwtService {
    String generateToken(UUID userId);

    UUID extractUserId(String token);

    boolean isValid(String token);
}
