# Software Testing and Quality Assurance Document
## Public Transport Management and Passenger Travel Analysis System

### Course: 25ITT33 Software Engineering (Unit 4: Software Testing)

---

## 1. Testing Fundamentals

### 1.1 Verification vs. Validation
- **Verification ("Are we building the product right?"):** Ensuring that design models, classes, and database schemas adhere strictly to the SRS specifications through code walkthroughs, static analysis, and review checklists.
- **Validation ("Are we building the right product?"):** Evaluating the executed software to ensure it fulfills actual end-user expectations, manages passenger travel accurately, and generates correct analytical insights.

---

## 2. Testing Levels and Strategies

### 2.1 Unit Testing
- Focuses on individual class methods in isolation:
  - `Bus.checkAvailability(int)`
  - `Schedule.bookSeat(String)` and `Schedule.releaseSeat(String)`
  - `DataProcessor.preprocessData()`
  - `AnalysisEngine.calculateDescriptiveStatistics()`

### 2.2 Integration Testing
- Verifies inter-component communication:
  - Java Business Logic invoking Database operations.
  - Ticket Booking updating both Schedule and Ticket tables synchronously.
  - Python Analytics querying the SQLite database and transforming records into Pandas DataFrames.

### 2.3 System Testing
- Tests complete end-to-end user workflows:
  - Register passenger -> Query schedule -> Reserve seat -> Validate double-booking protection -> Generate e-ticket -> Perform cancellation -> Run revenue report.

---

## 3. Black-Box Testing Techniques

### 3.1 Equivalence Class Partitioning (ECP)
Used for validating passenger seat numbers and bus capacities:
- **Variable: Bus Capacity ($C$)**
  - Invalid Class 1 ($C \le 0$): System rejects capacity.
  - Valid Class ($1 \le C \le 60$): System registers bus successfully.
  - Invalid Class 2 ($C > 60$): System rejects over-sized capacity.
- **Variable: Seat Booking Availability**
  - Valid Class: Unoccupied seat number on schedule ($S \notin \text{BookedSeats}$).
  - Invalid Class: Seat already occupied ($S \in \text{BookedSeats}$) $\rightarrow$ Throws `SeatUnavailableException`.

### 3.2 Boundary Value Analysis (BVA)
Used for ticket fares and booking lead days:
- **Test on Bus Capacity = 30:**
  - $N = 0$: Rejected.
  - $N = 1$: Accepted (First seat allocated).
  - $N = 29$: Accepted (Near capacity).
  - $N = 30$: Accepted (Last seat allocated, bus full).
  - $N = 31$: Rejected (Overbooking prevented).

---

## 4. White-Box Testing (Basis Path Testing)

### 4.1 Cyclomatic Complexity Calculation
Consider the core seat allocation method in `Schedule.java`:
```java
public synchronized boolean bookSeat(String seatNo) {
    if (!bookedSeats.contains(seatNo) && bookedSeats.size() < bus.getCapacity()) {
        bookedSeats.add(seatNo);
        return true;
    }
    return false;
}
```
- **Flow Graph Nodes ($N$):** 4
- **Edges ($E$):** 5
- **Predicate Nodes ($P$):** 2 (seat not booked, size < capacity)
- **Cyclomatic Complexity:**
  $$V(G) = E - N + 2P = 5 - 4 + 2(1) = 3$$
- **Independent Execution Paths:**
  1. *Path 1:* Seat already booked $\rightarrow$ returns `false`.
  2. *Path 2:* Seat not booked, but capacity reached $\rightarrow$ returns `false`.
  3. *Path 3:* Seat not booked and capacity available $\rightarrow$ adds seat, returns `true`.

All three basis paths are thoroughly exercised in the test suite.

---

## 5. Formal Test Cases and Execution Results

| Test ID | Test Case Description | Preconditions | Test Steps & Input Data | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :---: |
| **TC-01** | Register valid passenger | Database initialized | Enter ID: 105, Name: 'Karthik Raman', Phone: '9876543214' | Passenger registered with unique ID | Registered successfully | **PASS** |
| **TC-02** | Search bus schedules | Schedules exist for Bangalore -> Chennai | Query `Source='Bangalore'`, `Destination='Chennai'` | Displays Schedule 301 (Fare: ₹850, Departure: 21:00) | Schedule 301 displayed with available seats | **PASS** |
| **TC-03** | Confirm ticket booking | Schedule 301 has available seats | Book Schedule 301, Passenger 101, Seat 'A1' | Booking created, ticket generated with QR, seat count decremented | Ticket generated (TCK-0301-505) | **PASS** |
| **TC-04** | Prevent double booking on same seat | Seat 'A1' is already booked on Schedule 301 | Passenger 104 attempts booking Seat 'A1' on Schedule 301 | Throws `SeatUnavailableException`, booking rejected | Caught `SeatUnavailableException: Seat 'A1' is unavailable` | **PASS** |
| **TC-05** | Cancel existing booking | Booking #501 exists and is 'Confirmed' | Request cancellation for Booking ID #501 | Booking status becomes 'Cancelled', Seat 'A1' is re-released | Booking #501 cancelled, seat freed | **PASS** |
| **TC-06** | Multithreaded concurrent booking | Two threads compete for single remaining seat 'T-SEAT-99' | Thread 1 & Thread 2 attempt simultaneous booking | Exactly 1 thread succeeds; other thread is safely blocked | Thread 2 secured seat; Thread 1 safely denied | **PASS** |
| **TC-07** | SQL Route Revenue aggregation | Bookings present in database | Execute `SELECT * FROM vw_RouteWiseRevenue;` | Returns revenue grouped by route, matching manual calculations | All 8 routes displayed with accurate revenue totals | **PASS** |
| **TC-08** | Data Science missing value imputation | Raw dataset contains missing age fields | Run `DataProcessor.preprocessData()` | Missing ages imputed using median (42.0) | Imputed missing age records with median | **PASS** |
| **TC-09** | Outlier detection and treatment | Dataset contains extreme fare of ₹4500 | Run `DataProcessor.preprocessData()` | Outlier flagged ($|z| > 3.0$) and capped to $3\sigma$ threshold | Identified and capped 1 outlier record | **PASS** |
| **TC-10** | Visualization image generation | Cleaned dataset available | Run `Visualization.generateAllVisualizations()` | 7 PNG chart files created in `visualizations/` | All 7 high-resolution charts created | **PASS** |

**Summary:** 10 out of 10 test cases passed with 100% success rate.
