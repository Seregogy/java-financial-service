package com.financial.loan.domain.usecase.loan;


import com.financial.loan.domain.ValueObject.Passport;
import com.financial.loan.domain.domainexception.DuplicateLoanApplicationException;
import com.financial.loan.domain.domainexception.TooManyActiveLoansException;
import com.financial.loan.domain.domainexception.UserNotFoundException;
import com.financial.loan.domain.entity.Car;
import com.financial.loan.domain.entity.LoanApplication;
import com.financial.loan.domain.entity.User;
import com.financial.loan.domain.enums.Status;

import com.financial.loan.domain.interfaces.CarRepository;
import com.financial.loan.domain.interfaces.LoanApplicationRepository;
import com.financial.loan.domain.interfaces.UserAdditionalDataRepository;
import com.financial.loan.domain.interfaces.UserRepository;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class CreateLoanUseCase {

    private static final int MAX_ACTIVE_LOANS = 10;
    private static final long DUPLICATE_CHECK_HOURS = 24;

    private final LoanApplicationRepository loanRepository;
    private final UserRepository userRepository;
    private final UserAdditionalDataRepository userAdditionalDataRepository;
    private final CarRepository carRepository;

    public LoanApplication execute(UUID carId, UUID userId, BigDecimal loanAmount,
                        BigDecimal firstPayment, LocalDateTime term) {


        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }


        List<LoanApplication> userLoans = loanRepository.getByIdUser(user.getId());

        
        long activeLoans = userLoans.stream()
                .filter(loan -> loan.getStatus() != Status.REJECTED &&
                        loan.getStatus() != Status.EXPIRED)
                .count();

        if (activeLoans >= MAX_ACTIVE_LOANS) {
            throw new TooManyActiveLoansException(user.getId(), MAX_ACTIVE_LOANS);
        }

        Passport passportNumber = userAdditionalDataRepository.getPassportByUserId(user.getId());
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(DUPLICATE_CHECK_HOURS);

        boolean hasDuplicateInLast24Hours = userLoans.stream()
                .anyMatch(loan ->
                        loan.getCreated().isAfter(twentyFourHoursAgo) &&
                                userAdditionalDataRepository.getPassportByUserId(loan.getUserId()).equals(passportNumber) &&
                                loan.getStatus() != Status.REJECTED &&
                                loan.getStatus() != Status.EXPIRED
                );

        if (hasDuplicateInLast24Hours) {
            throw new DuplicateLoanApplicationException(passportNumber);
        }

        LoanApplication loan = LoanApplication.create(carId, userId, loanAmount, firstPayment, term);
        loanRepository.create(loan);

        return loan;
    }
}