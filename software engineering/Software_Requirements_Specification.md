# Software Requirements Specification (SRS)
## Public Transport Management and Passenger Travel Analysis System

### Standard: IEEE Std 830-1998 Format
**Course:** 25ITT33 Software Engineering  
**Department:** Information Technology  

---

## 1. Introduction

### 1.1 Purpose
The purpose of this document is to provide a complete and formal Software Requirements Specification (SRS) for the **Public Transport Management and Passenger Travel Analysis System**. It details the functional and non-functional requirements, constraints, system interfaces, and operational models required to design, develop, test, and deploy the system.

### 1.2 Scope
The system addresses the operational bottlenecks of manual transport record keeping. It provides:
1. Automated fleet and route management (bus registration, route distance, scheduling, and fare calculation).
2. Passenger booking, dynamic seat allocation, double-booking prevention, and automated ticket generation with digital QR markers.
3. Fast ticket cancellation with real-time seat de-allocation.
4. Administrative oversight, capacity auditing, and revenue tracking.
5. End-to-end travel pattern analysis using Data Science and Machine Learning techniques in Python to discover high-demand corridors, seasonal passenger loads, and route profitability.

### 1.3 Definitions, Acronyms, and Abbreviations
- **SRS:** Software Requirements Specification
- **DFD:** Data Flow Diagram
- **ERD:** Entity-Relationship Diagram
- **DBMS:** Database Management System
- **ACID:** Atomicity, Consistency, Isolation, Durability
- **EDA:** Exploratory Data Analysis
- **KDE:** Kernel Density Estimation
- **IQR:** Interquartile Range

### 1.4 References
- IEEE Recommended Practice for Software Requirements Specifications (IEEE Std 830-1998)
- Pressman, R. S., *Software Engineering: A Practitioner's Approach*, 9th Edition.
- Silberschatz, A., Korth, H. F., *Database System Concepts*, 7th Edition.

---

## 2. Overall Description

### 2.1 Product Perspective
The system is a multi-tier client-server enterprise application consisting of:
- **Presentation Layer:** Command-line interfaces, batch automation consoles, and data visualization dashboards.
- **Application Layer (Java):** Core business logic, domain objects, multithreaded transaction concurrency, and exception management.
- **Analytics Layer (Python):** Data preprocessing, statistical computation (central tendency, variance, z-scores), and visualization engine (Matplotlib & Seaborn).
- **Data Layer (DBMS):** Relational database managing relational integrity, foreign keys, views, and indexes.

### 2.2 User Classes and Characteristics
1. **Passenger:**
   - Capabilities: Search schedules, view seat availability, book tickets, cancel reservations, print e-tickets.
2. **Transport Administrator:**
   - Capabilities: Manage bus fleet, create routes, establish trip schedules, audit booking ledgers, monitor occupancy.
3. **Transport Analyst:**
   - Capabilities: Extract booking histories, perform data cleaning and feature engineering, evaluate travel patterns, and export analytical visualizations.

### 2.3 Operating Environment
- **Operating System:** Windows 10 / 11, Linux (Ubuntu 20.04+), macOS.
- **Java Runtime:** OpenJDK / Oracle JDK 17+.
- **Python Environment:** Python 3.10+ with Pandas, NumPy, Matplotlib, Seaborn.
- **Database Engine:** MySQL 8.0+ / SQLite 3.

### 2.4 Design and Implementation Constraints
- Strong referential integrity enforced via relational foreign keys.
- Thread-safe seat allocation preventing race conditions during concurrent booking requests.
- Strict data typing and validation on financial and capacity inputs.

---

## 3. Specific Requirements

### 3.1 Functional Requirements

#### FR-01: Bus Fleet Management
- The system shall maintain complete bus details, including `BusID`, `BusNumber`, `Capacity`, `BusType` (AC Sleeper, Non-AC Seater, Volvo Multi-Axle, Electric Express), and `Status`.
- The system shall ensure bus registration numbers are strictly unique across the fleet.

#### FR-02: Route Management
- The system shall maintain transit routes with unique source and destination pairs and track physical travel distance in kilometers.
- The system shall prevent duplicate route definitions between identical terminals.

#### FR-03: Bus Scheduling and Dynamic Fare Management
- The system shall record schedules specifying departure time, arrival time, travel date, assigned bus, and ticket fare.
- The system shall allow administrators to update schedules dynamically.

#### FR-04: Passenger Seat Availability & Booking Management
- The system shall allow passengers to check real-time seat availability.
- The system shall strictly prevent overbooking beyond the physical capacity of the assigned bus.
- The system shall reject duplicate seat selection on the same schedule and throw user-defined exceptions (`SeatUnavailableException`).

#### FR-05: Ticket Generation & Electronic Boarding Pass
- Upon successful booking confirmation, the system shall generate an electronic ticket containing a unique `TicketNo`, passenger details, route, seat number, fare, issue timestamp, and a digital QR verification payload.

#### FR-06: Ticket Cancellation and Immediate Seat Re-allocation
- The system shall permit passengers to cancel confirmed tickets by specifying their `BookingID`.
- Upon cancellation, the system shall immediately mark the booking as `Cancelled`, release the held seat, and restore bus seat availability for subsequent passengers.

#### FR-07: Management & Financial Reporting
- The system shall compute route-wise total revenue and booking counts.
- The system shall compute bus schedule occupancy percentages using stream aggregations and database views.

#### FR-08: Python Travel Analytics & Pattern Discovery
- The system shall retrieve booking datasets from the database or CSV files.
- The system shall preprocess raw data by handling missing values, eliminating duplicates, and capping statistical outliers using Z-scores ($|z| > 3.0$).
- The system shall generate 7 visualizations:
  1. Daily passenger booking time series.
  2. Route-wise total revenue bar chart.
  3. Passenger distribution by bus type pie chart.
  4. Ticket fare distribution histogram and KDE.
  5. Distance vs. ticket fare scatter plot.
  6. Route fare variance and outlier box plot.
  7. Correlation heatmap of quantitative variables.

---

### 3.2 Non-Functional Requirements

#### NFR-01: Performance & Response Time
- Search queries for schedules shall execute within 200 milliseconds.
- Analytical reporting on datasets up to 10,000 records shall generate within 3 seconds.

#### NFR-02: Concurrency & Data Integrity
- The system shall maintain ACID transactional properties.
- Simultaneous concurrent booking requests for the identical seat shall grant the ticket to exactly one requester and fail gracefully for others.

#### NFR-03: Reliability & Fault Tolerance
- All invalid inputs (e.g. non-numeric inputs, negative fares, non-existent identifiers) shall be intercepted by custom exception handlers without crashing the application.

#### NFR-04: Usability & Portability
- The system shall provide intuitive, self-explanatory menu-driven consoles supporting manual user input.
- Double-clickable executable batch files (`.bat`) shall enable zero-configuration execution.
