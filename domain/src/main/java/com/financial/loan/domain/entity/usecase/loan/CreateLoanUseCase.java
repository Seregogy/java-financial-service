package com.financial.loan.domain.entity.usecase.loan;


import com.financial.loan.domain.entity.ValueObject.Passport;
import com.financial.loan.domain.entity.domainexception.DuplicateLoanApplicationException;
import com.financial.loan.domain.entity.domainexception.TooManyActiveLoansException;
import com.financial.loan.domain.entity.domainexception.UserNotFoundException;
import com.financial.loan.domain.entity.entity.LoanApplication;
import com.financial.loan.domain.entity.entity.User;
import com.financial.loan.domain.entity.enums.Status;

import com.financial.loan.domain.entity.interfaces.LoanApplicationRepository;
import com.financial.loan.domain.entity.interfaces.UserAdditionalDataRepository;
import com.financial.loan.domain.entity.interfaces.UserRepository;
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

    public UUID execute(UUID carId, UUID userId, BigDecimal loanAmount,
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

        // 4. Проверяем на дубликаты по паспорту за последние 24 часа
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

        // 5. Создаем заявку (уже выбрасывает исключение при невалидных данных)
        LoanApplication loan = LoanApplication.create(carId, userId, loanAmount, firstPayment, term);

        // 6. Сохраняем
        loanRepository.create(loan);

        return loan.getId();
    }
}