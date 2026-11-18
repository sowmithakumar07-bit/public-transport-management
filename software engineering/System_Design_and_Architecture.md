# System Design and Architecture Document
## Public Transport Management and Passenger Travel Analysis System

### Course: 25ITT33 Software Engineering (Unit 3: Software Design)

---

## 1. Architectural Design Overview

The system adopts a **Multi-Tier Layered Architecture** coupled with the **Model-View-Controller (MVC)** architectural design style. This architecture decouples data persistence, domain business logic, presentation interfaces, and analytical modules.

```
+-------------------------------------------------------------------------+
|                        PRESENTATION LAYER                               |
|   Interactive CLI Consoles  |  Batch Run Scripts  | Visual Dashboards   |
+------------------------------------+------------------------------------+
                                     |
                                     v
+-------------------------------------------------------------------------+
|                        APPLICATION LAYER                                |
|   Passenger Services (Java)  | Transport Management | Analytics Bridge  |
|   - Concurrency Control      | - Capacity Auditing  | - Preprocessing   |
|   - Exception Handling       | - Stream API Reports | - Pattern Mining  |
+------------------------------------+------------------------------------+
                                     |
                                     v
+-------------------------------------------------------------------------+
|                        DATA & PERSISTENCE LAYER                         |
|   Relational Database (MySQL 8.0+ / SQLite 3)                           |
|   - Tables, Primary/Foreign Keys, Views, Indexes, Transactions          |
+-------------------------------------------------------------------------+
```

---

## 2. Design Diagrams Specification

All high-resolution diagram image files are stored in the [diagrams](diagrams/) directory.

### 2.1 Use Case Diagram
- **Artifact:** [use_case_diagram.jpg](diagrams/use_case_diagram.jpg)
- **Actors:**
  1. `Passenger`: Primary actor interacting with booking, search, cancellation, and ticket receipt.
  2. `Transport Administrator`: Administrative actor managing buses, routes, schedules, and operational records.
  3. `Transport Analyst`: Specialized actor running data extraction, preprocessing, pattern evaluation, and chart generation.
- **Key Use Cases:**
  - `View Bus Schedules`
  - `Check Seat Availability`
  - `Book Ticket` (Includes `Validate Booking Details`, `Generate Ticket`, and `Update Seat Availability`)
  - `Cancel Ticket` (Includes `Update Seat Availability`)
  - `Manage Bus Details`, `Manage Route Details`, `Manage Bus Schedules`
  - `Retrieve Booking Data` (Includes `Preprocess Travel Data` and `Generate Travel Visualizations`)
  - `Analyze Passenger Travel Patterns`

---

### 2.2 Activity Diagram
- **Artifact:** [activity_diagram.jpg](diagrams/activity_diagram.jpg)
- **Workflow Description:**
  - **Passenger Swimlane:** Initiates login/registration -> submits credentials -> views interactive dashboard -> selects operation (search, book, cancel, view reports, analyze).
  - **System Swimlane:** Validates credentials -> executes business rules -> checks seat capacity -> verifies if seats are available -> if seats full, displays error; if seats available, confirms booking, generates ticket, and updates records.
  - **Database / Analytics Swimlane:** Validates user credentials -> persists updated transactions -> executes analytical queries and visual rendering.

---

### 2.3 Data Flow Diagrams (DFD)

#### Level 0 DFD (Context Analysis Diagram)
- **Artifact:** [dfd_level_0.jpg](diagrams/dfd_level_0.jpg)
- **Central Process:** `0: Public Transport Management and Passenger Travel Analysis System`
- **Data Inflows:**
  - Passenger -> System: Search queries, booking parameters, cancellation IDs.
  - Transport Administrator -> System: Bus attributes, route paths, schedule timings.
  - Transport Analyst -> System: Analysis criteria, visualization configurations.
- **Data Outflows:**
  - System -> Passenger: Seat availability matrix, ticket confirmations, booking statuses.
  - System -> Administrator: Operational summaries, fleet audit logs.
  - System -> Analyst: Aggregated booking datasets, charts, travel trend insights.

#### Level 1 DFD
- **Artifact:** [dfd_level_1.jpg](diagrams/dfd_level_1.jpg)
- **Decomposed Sub-processes:**
  - `1.0 Manage Passenger Services`: Interacts with Passenger and data store `D1: Booking & Ticket Database`.
  - `2.0 Manage Transport Details`: Interacts with Administrator and data store `D2: Bus & Route Database`.
  - `3.0 Process Bookings and Ticketing`: Processes reservations, validates capacity, stores tickets in `D3: Ticket Records`.
  - `4.0 Analyze Travel Patterns`: Fetches dataset into Python analytics module.
  - `5.0 Generate Reports and Visualizations`: Renders charts and insights for Transport Analyst.

---

### 2.4 Class Diagram
- **Artifact:** [class_diagram.jpg](diagrams/class_diagram.jpg)
- **Core Java Domain Classes:**
  - `Passenger`: Attributes (`passengerId`, `name`, `email`, `phone`); Methods (`searchSchedules()`, `bookTicket()`, `cancelTicket()`, `viewBookings()`).
  - `Bus`: Attributes (`busId`, `busNumber`, `capacity`, `type`, `status`); Methods (`updateDetails()`, `checkAvailability()`).
  - `Route`: Attributes (`routeId`, `source`, `destination`, `distance`); Methods (`updateRoute()`).
  - `Schedule`: Attributes (`scheduleId`, `travelDate`, `departureTime`, `arrivalTime`, `fare`); Methods (`updateSchedule()`, `checkSeats()`, `bookSeat()`, `releaseSeat()`).
  - `Booking`: Attributes (`bookingId`, `passengerId`, `seatNo`, `amount`, `status`, `bookingDate`); Methods (`confirmBooking()`, `cancelBooking()`).
  - `TicketBooking`: Attributes (`ticketId`, `ticketNo`, `issueDate`, `totalFare`, `qrCode`); Methods (`generateTicket()`, `viewTicket()`).
  - `TransportAdministrator`: Attributes (`adminId`, `name`, `role`); Methods (`manageBus()`, `manageRoute()`, `manageSchedule()`, `viewReports()`).
- **Python Analytics Classes:**
  - `DataProcessor`: Attributes (`dataPath`, `cleanedData`); Methods (`fetchData()`, `preprocessData()`, `transformData()`).
  - `AnalysisEngine`: Attributes (`patterns`, `insights`); Methods (`calculateDescriptiveStatistics()`, `analyzeTravelPatterns()`, `generateReports()`).
  - `Visualization`: Attributes (`df`, `outputDir`); Methods (`generateAllVisualizations()`).
  - `Report`: Attributes (`reportId`, `type`, `generatedOn`); Methods (`exportReport()`).

---

### 2.5 Sequence Diagram
- **Artifact:** [sequence_diagram.jpg](diagrams/sequence_diagram.jpg)
- **Chronological Sequence:**
  1. Passenger sends `Login / Search Buses` to Web/CLI Interface.
  2. Interface invokes `Fetch Schedules & Availability` on Java Business Logic.
  3. Business Logic executes `Query Bus, Route, Schedule Data` against Database.
  4. Database returns schedule tuples; Interface displays available buses to Passenger.
  5. Passenger sends `Select Bus & Book Ticket`.
  6. Java layer checks seat availability in synchronized critical section.
  7. *Alternative [Seats Available]:* Java layer confirms booking, inserts booking record into DB, generates Ticket with QR code, and displays confirmation to Passenger.
  8. *Alternative [Seats Not Available]:* Java layer raises `SeatUnavailableException` and displays error to Passenger.
  9. Transport Analyst submits `Request Reports`; Analytics Engine extracts data, calculates trends, generates charts, and presents visual dashboard.

---

### 2.6 System Architecture Diagram
- **Artifact:** [system_architecture_diagram.jpg](diagrams/system_architecture_diagram.jpg)
- Depicts the complete technical blueprint spanning Presentation Layer, Java Application Layer, Python Analytics Module, Stored Data Layer, and Final Business Outputs (E-Tickets, Revenue Reports, Interactive Visualizations, and Management Summaries).
