💳 Payment Service - E-commerce Microservices Project
This is the Payment Service component of the E-commerce Microservices Application. It handles processing of payments for orders, tracks payment status, and integrates with the Order Service for verification and updates.

---

📦 Features
✅ Process payment using multiple payment methods (Credit Card, Debit Card, UPI, etc.)
✅ Maintain and retrieve payment status (PENDING, SUCCESS, FAILED)
✅ Fetch payment details by orderId
✅ Integration-ready with Order Service for order validation
✅ Exception handling for invalid payment requests
✅ Built with clean separation of concerns (DTOs, Service, Repository layers)
✅ Unit tested with Mockito and JUnit 5

-----

🧱 Technologies Used
Java 17

Spring Boot

Spring Data JPA

H2 / MySQL (for persistence)

Mockito & JUnit 5

Maven

-----

💰 Payment Flow
Client sends a PaymentRequestDTO containing order ID, payment method, and amount.

PaymentService validates and processes the payment.

A Payment entity is created with a PENDING status.

Based on the simulated outcome or integration (e.g., with Razorpay or Stripe in future), status is updated to SUCCESS or FAILED.

A PaymentResponseDTO is returned to the client with payment ID, status, and transaction time.

Payment details can be retrieved later by orderId.

-----
🧪 Testing
All core service logic is unit tested using Mockito and JUnit 5.

Edge cases such as duplicate payments or missing order ID are handled with meaningful exceptions.