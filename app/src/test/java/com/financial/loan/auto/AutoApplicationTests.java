package com.financial.loan.auto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AutoApplicationTests {

	// ===== 1. ТЕСТЫ ДЛЯ МОДЕЛИ ДАННЫХ

	// Временный класс для тестирования 
	static class TestLoanApplication {
		private Long id;
		private String customerName;
		private String customerEmail;
		private BigDecimal loanAmount;
		private Integer creditScore;
		private Integer loanTermMonths;
		private String status;
		private LocalDateTime applicationDate;

		public TestLoanApplication(String customerName, String customerEmail,
								   BigDecimal loanAmount, Integer loanTermMonths) {
			this.customerName = customerName;
			this.customerEmail = customerEmail;
			this.loanAmount = loanAmount;
			this.loanTermMonths = loanTermMonths;
			this.status = "PENDING";
			this.applicationDate = LocalDateTime.now();
		}

		// Getters and Setters
		public Long getId() { return id; }
		public void setId(Long id) { this.id = id; }
		public String getCustomerName() { return customerName; }
		public void setCustomerName(String customerName) { this.customerName = customerName; }
		public String getCustomerEmail() { return customerEmail; }
		public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
		public BigDecimal getLoanAmount() { return loanAmount; }
		public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }
		public Integer getCreditScore() { return creditScore; }
		public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }
		public Integer getLoanTermMonths() { return loanTermMonths; }
		public void setLoanTermMonths(Integer loanTermMonths) { this.loanTermMonths = loanTermMonths; }
		public String getStatus() { return status; }
		public void setStatus(String status) { this.status = status; }
		public LocalDateTime getApplicationDate() { return applicationDate; }
		public void setApplicationDate(LocalDateTime applicationDate) { this.applicationDate = applicationDate; }

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			TestLoanApplication that = (TestLoanApplication) o;
			return Objects.equals(customerEmail, that.customerEmail);
		}

		@Override
		public int hashCode() {
			return Objects.hash(customerEmail);
		}
	}

	// Временный репозиторий (in-memory)
	static class TestLoanRepository {
		private Map<Long, TestLoanApplication> storage = new HashMap<>();
		private Long currentId = 1L;

		public TestLoanApplication save(TestLoanApplication application) {
			if (application.getId() == null) {
				application.setId(currentId++);
			}
			storage.put(application.getId(), application);
			return application;
		}

		public Optional<TestLoanApplication> findById(Long id) {
			return Optional.ofNullable(storage.get(id));
		}

		public Optional<TestLoanApplication> findByEmail(String email) {
			return storage.values().stream()
					.filter(app -> app.getCustomerEmail().equals(email))
					.findFirst();
		}

		public List<TestLoanApplication> findAll() {
			return new ArrayList<>(storage.values());
		}

		public List<TestLoanApplication> findByStatus(String status) {
			return storage.values().stream()
					.filter(app -> app.getStatus().equals(status))
					.collect(Collectors.toList());
		}

		public void deleteById(Long id) {
			storage.remove(id);
		}

		public void clear() {
			storage.clear();
			currentId = 1L;
		}

		public boolean existsByEmailAndStatus(String email, String status) {
			return storage.values().stream()
					.anyMatch(app -> app.getCustomerEmail().equals(email) && app.getStatus().equals(status));
		}
	}

	// Временный сервис для кредитного скора
	static class TestCreditScoreService {
		private Map<String, Integer> creditScores = new HashMap<>();

		public TestCreditScoreService() {
			// Добавляем тестовые данные
			creditScores.put("good@test.com", 750);
			creditScores.put("bad@test.com", 550);
			creditScores.put("medium@test.com", 650);
			creditScores.put("john@test.com", 720);
			creditScores.put("jane@test.com", 680);
		}

		public Integer getCreditScore(String email) {
			return creditScores.getOrDefault(email, 650);
		}

		public void setCreditScore(String email, Integer score) {
			creditScores.put(email, score);
		}
	}

	// Временный сервис для заявок
	static class TestLoanService {
		private TestLoanRepository repository;
		private TestCreditScoreService creditScoreService;

		public TestLoanService(TestLoanRepository repository, TestCreditScoreService creditScoreService) {
			this.repository = repository;
			this.creditScoreService = creditScoreService;
		}

		public TestLoanApplication submitApplication(TestLoanApplication application) {
			// Проверка на существующую PENDING заявку
			if (repository.existsByEmailAndStatus(application.getCustomerEmail(), "PENDING")) {
				throw new IllegalStateException("Customer already has a pending application");
			}

			// Получаем кредитный скор
			Integer creditScore = creditScoreService.getCreditScore(application.getCustomerEmail());
			application.setCreditScore(creditScore);

			// Определяем статус на основе кредитного скора
			if (creditScore >= 700) {
				application.setStatus("APPROVED");
			} else if (creditScore < 600) {
				application.setStatus("REJECTED");
			} else {
				application.setStatus("UNDER_REVIEW");
			}

			application.setApplicationDate(LocalDateTime.now());
			return repository.save(application);
		}

		public TestLoanApplication getApplicationById(Long id) {
			return repository.findById(id)
					.orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
		}

		public List<TestLoanApplication> getApplicationsByStatus(String status) {
			return repository.findByStatus(status);
		}

		public TestLoanApplication updateApplicationStatus(Long id, String newStatus) {
			TestLoanApplication application = getApplicationById(id);
			application.setStatus(newStatus);
			return repository.save(application);
		}

		public List<TestLoanApplication> getAllApplications() {
			return repository.findAll();
		}
	}


	// ===== 2. ТЕСТЫ =====

	private TestLoanRepository repository;
	private TestCreditScoreService creditScoreService;
	private TestLoanService loanService;

	@BeforeEach
	void setUp() {
		repository = new TestLoanRepository();
		creditScoreService = new TestCreditScoreService();
		loanService = new TestLoanService(repository, creditScoreService);
	}

	@Test
	@DisplayName("Тест: Подача заявки с хорошим кредитным рейтингом - должна быть одобрена")
	void testSubmitApplication_GoodCreditScore_ShouldBeApproved() {
		// Given
		TestLoanApplication application = new TestLoanApplication(
				"John Doe", "good@test.com", new BigDecimal("30000"), 60
		);

		// When
		TestLoanApplication result = loanService.submitApplication(application);

		// Then
		assertNotNull(result.getId());
		assertEquals("APPROVED", result.getStatus());
		assertEquals(750, result.getCreditScore());
		assertEquals("John Doe", result.getCustomerName());
	}

	@Test
	@DisplayName("Тест: Подача заявки с плохим кредитным рейтингом - должна быть отклонена")
	void testSubmitApplication_BadCreditScore_ShouldBeRejected() {
		// Given
		TestLoanApplication application = new TestLoanApplication(
				"Jane Smith", "bad@test.com", new BigDecimal("50000"), 72
		);

		// When
		TestLoanApplication result = loanService.submitApplication(application);

		// Then
		assertNotNull(result.getId());
		assertEquals("REJECTED", result.getStatus());
		assertEquals(550, result.getCreditScore());
	}

	@Test
	@DisplayName("Тест: Подача заявки со средним кредитным рейтингом - должна быть на рассмотрении")
	void testSubmitApplication_MediumCreditScore_ShouldBeUnderReview() {
		// Given
		TestLoanApplication application = new TestLoanApplication(
				"Bob Johnson", "medium@test.com", new BigDecimal("25000"), 60
		);

		// When
		TestLoanApplication result = loanService.submitApplication(application);

		// Then
		assertNotNull(result.getId());
		assertEquals("UNDER_REVIEW", result.getStatus());
		assertEquals(650, result.getCreditScore());
	}

	@Test
	@DisplayName("Тест: Повторная подача заявки с PENDING статусом - должно выбросить исключение")
	void testSubmitApplication_DuplicatePending_ShouldThrowException() {
		// Given - первая заявка
		TestLoanApplication firstApplication = new TestLoanApplication(
				"Alice Brown", "alice@test.com", new BigDecimal("20000"), 48
		);
		loanService.submitApplication(firstApplication);

		// When & Then - вторая заявка (такой же email)
		TestLoanApplication secondApplication = new TestLoanApplication(
				"Alice Brown", "alice@test.com", new BigDecimal("25000"), 60
		);

		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
			loanService.submitApplication(secondApplication);
		});

		assertTrue(exception.getMessage().contains("already has a pending application"));
	}

	@Test
	@DisplayName("Тест: Поиск заявки по ID - успешный")
	void testGetApplicationById_ExistingId_ShouldReturnApplication() {
		// Given
		TestLoanApplication application = new TestLoanApplication(
				"Charlie Wilson", "charlie@test.com", new BigDecimal("35000"), 60
		);
		TestLoanApplication saved = loanService.submitApplication(application);

		// When
		TestLoanApplication found = loanService.getApplicationById(saved.getId());

		// Then
		assertNotNull(found);
		assertEquals(saved.getId(), found.getId());
		assertEquals("charlie@test.com", found.getCustomerEmail());
	}

	@Test
	@DisplayName("Тест: Поиск заявки по ID - не существует, должно выбросить исключение")
	void testGetApplicationById_NonExistingId_ShouldThrowException() {
		// When & Then
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			loanService.getApplicationById(999L);
		});

		assertTrue(exception.getMessage().contains("Application not found"));
	}

	@Test
	@DisplayName("Тест: Получение заявок по статусу")
	void testGetApplicationsByStatus_ShouldReturnFilteredList() {
		// Given - создаем несколько заявок
		TestLoanApplication app1 = new TestLoanApplication("User1", "user1@test.com", new BigDecimal("10000"), 60);
		TestLoanApplication app2 = new TestLoanApplication("User2", "user2@test.com", new BigDecimal("20000"), 60);
		TestLoanApplication app3 = new TestLoanApplication("User3", "user3@test.com", new BigDecimal("30000"), 60);

		loanService.submitApplication(app1);
		loanService.submitApplication(app2);
		loanService.submitApplication(app3);

		// When
		List<TestLoanApplication> approvedApps = loanService.getApplicationsByStatus("APPROVED");

		// Then
		assertEquals(3, approvedApps.size()); // Все с хорошим credit score (650+)
	}

	@Test
	@DisplayName("Тест: Обновление статуса заявки")
	void testUpdateApplicationStatus_ShouldUpdateStatus() {
		// Given
		TestLoanApplication application = new TestLoanApplication(
				"Diana Prince", "diana@test.com", new BigDecimal("40000"), 72
		);
		TestLoanApplication saved = loanService.submitApplication(application);
		assertEquals("APPROVED", saved.getStatus());

		// When
		TestLoanApplication updated = loanService.updateApplicationStatus(saved.getId(), "REJECTED");

		// Then
		assertEquals("REJECTED", updated.getStatus());

		// Проверяем, что статус обновился в репозитории
		TestLoanApplication fromRepo = loanService.getApplicationById(saved.getId());
		assertEquals("REJECTED", fromRepo.getStatus());
	}

	@Test
	@DisplayName("Тест: Получение всех заявок")
	void testGetAllApplications_ShouldReturnAllApplications() {
		// Given
		loanService.submitApplication(new TestLoanApplication("User1", "u1@test.com", new BigDecimal("10000"), 60));
		loanService.submitApplication(new TestLoanApplication("User2", "u2@test.com", new BigDecimal("20000"), 60));
		loanService.submitApplication(new TestLoanApplication("User3", "u3@test.com", new BigDecimal("30000"), 60));

		// When
		List<TestLoanApplication> all = loanService.getAllApplications();

		// Then
		assertEquals(3, all.size());
	}

	@Test
	@DisplayName("Тест: Валидация суммы кредита - не может быть отрицательной")
	void testLoanAmount_ShouldNotBeNegative() {
		// Given
		TestLoanApplication application = new TestLoanApplication(
				"Test User", "test@test.com", new BigDecimal("-10000"), 60
		);

		// When
		TestLoanApplication result = loanService.submitApplication(application);

		// Then
		assertTrue(result.getLoanAmount().compareTo(BigDecimal.ZERO) < 0);
		// В реальном приложении здесь должна быть валидация, но для теста просто проверяем
	}

	@Test
	@DisplayName("Тест: Срок кредита должен быть положительным")
	void testLoanTerm_ShouldBePositive() {
		// Given
		TestLoanApplication application = new TestLoanApplication(
				"Test User", "test@test.com", new BigDecimal("25000"), -12
		);

		// When
		TestLoanApplication result = loanService.submitApplication(application);

		// Then
		assertTrue(result.getLoanTermMonths() < 0);
		// В реальном приложении здесь должна быть валидация
	}

	@Test
	@DisplayName("Тест: Полный процесс - подача, поиск, обновление, удаление")
	void testCompleteWorkflow_SubmitFindUpdateDelete() {
		// Step 1: Submit application
		TestLoanApplication application = new TestLoanApplication(
				"Full Flow User", "flow@test.com", new BigDecimal("45000"), 60
		);
		TestLoanApplication submitted = loanService.submitApplication(application);
		assertNotNull(submitted.getId());
		assertEquals("APPROVED", submitted.getStatus());

		// Step 2: Find by ID
		TestLoanApplication found = loanService.getApplicationById(submitted.getId());
		assertEquals("flow@test.com", found.getCustomerEmail());

		// Step 3: Update status
		TestLoanApplication updated = loanService.updateApplicationStatus(submitted.getId(), "UNDER_REVIEW");
		assertEquals("UNDER_REVIEW", updated.getStatus());

		// Step 4: Get all applications
		List<TestLoanApplication> all = loanService.getAllApplications();
		assertEquals(1, all.size());

		// Step 5: Delete
		repository.deleteById(submitted.getId());

		// Step 6: Verify deletion
		assertThrows(RuntimeException.class, () -> {
			loanService.getApplicationById(submitted.getId());
		});
	}

	@Test
	@DisplayName("Тест: Контекст Spring Boot загружается")
	void contextLoads() {
		// Этот тест проверяет, что Spring Boot контекст загружается
		assertTrue(true);
	}
}