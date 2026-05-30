package com.financial.loan.domain.service;

public interface PasswordEncodeService {
    String encode(String rawPassword);
    Boolean checkPassword(String rawPassword, String encodedPassword);
}
