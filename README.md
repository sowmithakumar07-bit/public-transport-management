# Public Transport Management and Passenger Travel Analysis System

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&logoColor=white)](#3-java-programming)
[![Database](https://img.shields.io/badge/DBMS-MySQL%208%20%7C%20SQLite3-4479A1?logo=mysql&logoColor=white)](#2-database-management-system)
[![Python](https://img.shields.io/badge/Python-3.10%2B-3776AB?logo=python&logoColor=white)](#4-foundation-of-data-science)
[![Software Engineering](https://img.shields.io/badge/Standard-IEEE%20830-blue)](#1-software-engineering)

An integrated, enterprise-grade engineering project developed according to the B.Tech curriculum and syllabus specifications. The project is systematically categorized into four core academic disciplines:
1. **Software Engineering (25ITT33)**
2. **Database Management System (25ITT31)**
3. **Java Programming (25ITT32)**
4. **Foundation of Data Science (25ITC31)**

---

## Repository Structure Overview

```text
Public transport management/
│
├── software engineering/
│   ├── diagrams/
│   │   ├── activity_diagram.jpg          <- Activity Workflow Diagram
│   │   ├── class_diagram.jpg             <- Class Diagram (Java & Python)
│   │   ├── dfd_level_0.jpg               <- Context Analysis Diagram (DFD Level 0)
│   │   ├── dfd_level_1.jpg               <- Data Flow Diagram (DFD Level 1)
│   │   ├── sequence_diagram.jpg          <- Ticket Booking & Analysis Sequence Diagram
│   │   ├── system_architecture_diagram.jpg <- Multi-tier Architectural Blueprint
│   │   └── use_case_diagram.jpg          <- System Use Case Diagram
│   ├── Software_Requirements_Specification.md <- IEEE 830 Standard SRS Document
│   ├── System_Design_and_Architecture.md     <- Architectural styles, MVC, & diagrams
│   ├── Software_Testing_Document.md          <- Unit, Integration, BVA, ECP, and 10 Test Cases
│   └── Project_Management_and_Estimation.md  <- LOC, Function Points, COCOMO, RMMM, CMMI
│
├── database management system/
│   ├── ER_Diagram.jpg                   <- Entity-Relationship Diagram
│   ├── schema.sql                       <- DDL: Tables, Primary/Foreign Keys, Checks, Views, Indexes
│   ├── sample_data.sql                  <- DML: Realistic seed data across 8 entities
│   ├── queries.sql                      <- 14+ SQL queries (Joins, Aggregations, Subqueries, Views)
│   ├── Relational_Model.md              <- Relational schema, integrity rules, relational algebra
│   ├── Normalization_Document.md        <- Functional dependencies, Armstrong's axioms, 1NF to BCNF
│   ├── dbms_interactive.py              <- Interactive SQL console with manual giving
│   ├── run_dbms.bat                     <- Double-clickable batch runner for DBMS
│   ├── dbms_output.txt                  <- Output of executed SQL queries and reports
│   └── dbms_execution_screenshot.png    <- Screenshot of DBMS console execution
│
├── java programming/
│   ├── src/com/transport/
│   │   ├── model/                       <- Passenger, Bus, Route, Schedule, Booking, TicketBooking, Administrator
│   │   ├── exception/                   <- SeatUnavailableException, InvalidBookingException
│   │   ├── service/                     <- TransportService (Collections, Streams, Multithreading)
│   │   └── app/                         <- TransportManagementApp (Interactive Manual Giving Menu)
│   ├── bin/                             <- Compiled Java bytecode (.class files)
│   ├── run_java.bat                     <- Double-clickable batch runner for Java application
│   ├── java_output.txt                  <- Execution log of booking, exceptions, cancellation, reports
│   └── java_execution_screenshot.png   <- Screenshot of Java console execution
│
├── foundation of data science/
│   ├── data_science_app.py              <- DataProcessor, AnalysisEngine, Visualization, Report classes
│   ├── transport_booking_dataset.csv    <- 300+ records dataset with deliberately injected outliers & missing values
│   ├── visualizations/                  <- 7 generated high-resolution charts (Line, Bar, Pie, Boxplot, Heatmap, etc.)
│   │   ├── bustype_pie_chart.png
│   │   ├── distance_vs_fare_scatter.png
│   │   ├── fare_distribution_hist.png
│   │   ├── route_fare_boxplot.png
│   │   ├── route_revenue_bar.png
│   │   ├── time_series_bookings.png
│   │   └── travel_correlation_heatmap.png
│   ├── run_fds.bat                      <- Double-clickable batch runner for Data Science console
│   ├── fds_output.txt                   <- Output of EDA, descriptive statistics, and travel patterns
│   └── fds_execution_screenshot.png     <- Screenshot of Data Science console execution
│
└── README.md                            <- Project documentation and instructions
```

---

## 1. Software Engineering

- **Syllabus Reference:** `25ITT33 SOFTWARE ENGINEERING`
- **Deliverables:**
  - [Software_Requirements_Specification.md](software%20engineering/Software_Requirements_Specification.md): Prepared according to **IEEE Std 830-1998**. Includes problem analysis, functional/non-functional requirements, use cases, and operating constraints.
  - [System_Design_and_Architecture.md](software%20engineering/System_Design_and_Architecture.md): Architectural design (Layered + MVC), component-level design, and detailed descriptions of all 7 design diagrams:
    - [Use Case Diagram](software%20engineering/diagrams/use_case_diagram.jpg)
    - [Activity Diagram](software%20engineering/diagrams/activity_diagram.jpg)
    - [DFD Level 0](software%20engineering/diagrams/dfd_level_0.jpg)
    - [DFD Level 1](software%20engineering/diagrams/dfd_level_1.jpg)
    - [Class Diagram](software%20engineering/diagrams/class_diagram.jpg)
    - [System Architecture Diagram](software%20engineering/diagrams/system_architecture_diagram.jpg)
    - [Sequence Diagram](software%20engineering/diagrams/sequence_diagram.jpg)
  - [Software_Testing_Document.md](software%20engineering/Software_Testing_Document.md): Verification and Validation, Unit/Integration/System testing, Black-Box techniques (Equivalence Partitioning, Boundary Value Analysis), White-Box Basis Path testing ($V(G) = 3$), and 10 formal test cases with expected vs. actual outcomes (100% Pass rate).
  - [Project_Management_and_Estimation.md](software%20engineering/Project_Management_and_Estimation.md): LOC metrics (2,250 LOC), Function Point sizing (105 FPs), Basic COCOMO effort (5.64 Person-Months, 4.83 Months Duration), RMMM risk management, and CMMI Level 3 alignment.

---

## 2. Database Management System

- **Syllabus Reference:** `25ITT31 DATABASE MANAGEMENT SYSTEMS`
- **Deliverables:**
  - [ER_Diagram.jpg](database%20management%20system/ER_Diagram.jpg): Complete Entity-Relationship model depicting 8 entities (`Passenger`, `Bus`, `Route`, `Schedule`, `Booking`, `Ticket`, `TransportAdministrator`, `TravelAnalysis`) and cardinalities.
  - [Relational_Model.md](database%20management%20system/Relational_Model.md): Schema definitions, primary keys, candidate keys, foreign key constraints, domain constraints, and relational algebra operations.
  - [Normalization_Document.md](database%20management%20system/Normalization_Document.md): Step-by-step normalization from UNF through 1NF, 2NF, 3NF to **Boyce-Codd Normal Form (BCNF)** with functional dependencies and Armstrong's axioms.
  - [schema.sql](database%20management%20system/schema.sql): Production DDL script defining tables, check constraints, indexes, and reporting views (`vw_RouteWiseRevenue`, `vw_BusOccupancy`, `vw_PassengerBookingHistory`).
  - [sample_data.sql](database%20management%20system/sample_data.sql): Realistic seed data across all 8 entities.
  - [queries.sql](database%20management%20system/queries.sql): 14 SQL queries covering joins (INNER, LEFT, Multi-table), aggregations, `GROUP BY`, `HAVING`, correlated/non-correlated subqueries, and view querying.
  - [dbms_interactive.py](database%20management%20system/dbms_interactive.py): Interactive SQL management console supporting manual giving of SQL queries, table inspection, and passenger insertion.
  - [dbms_output.txt](database%20management%20system/dbms_output.txt): Log of executed queries and reports.
  - [dbms_execution_screenshot.png](database%20management%20system/dbms_execution_screenshot.png): Terminal screenshot of DBMS execution.

### How to Run DBMS (Manual Giving Mode)
Double-click the runnable batch file:
```cmd
database management system\run_dbms.bat
```
Or run via terminal:
```cmd
cd "database management system"
python dbms_interactive.py
```
**Manual Giving Menu Options:**
- `1`: Execute custom SQL queries manually (e.g. `SELECT * FROM Bus;`)
- `2`: List all tables and views
- `3`: View records of a specific table
- `4`: View route-wise revenue report
- `5`: View bus occupancy report
- `6`: Manually insert a new passenger record
- `7`: Run predefined analytical demo suite
- `8`: Exit

---

## 3. Java Programming

- **Syllabus Reference:** `25ITT32 JAVA PROGRAMMING`
- **Features Implemented:**
  - **Unit 1 & 2 (OOP Fundamentals):** Classes, constructors, method overloading, encapsulation, access control, packages, and console I/O.
  - **Unit 3 (Exceptions & Concurrency):** User-defined custom exceptions (`SeatUnavailableException`, `InvalidBookingException`), synchronized seat reservation critical sections, and multithreaded concurrent booking simulations.
  - **Unit 4 (Strings & Formatting):** String manipulation, `StringBuilder` formatted electronic tickets with digital QR markers.
  - **Unit 5 (Collections & Streams):** Generics, Collections Framework (`List`, `Map`, `Set`), Lambda expressions, and Stream API (`filter`, `map`, `sorted`, `collect`, `groupingBy`, `summingDouble`) for real-time revenue and occupancy calculations.
- **Deliverables:**
  - Java source code in `src/com/transport/`
  - Compiled bytecode in `bin/`
  - [run_java.bat](java%20programming/run_java.bat): Double-clickable batch runner
  - [java_output.txt](java%20programming/java_output.txt): Sample execution log
  - [java_execution_screenshot.png](java%20programming/java_execution_screenshot.png): Terminal screenshot

### How to Run Java (Manual Giving Mode)
Double-click the runnable batch file:
```cmd
java programming\run_java.bat
```
Or run via terminal:
```cmd
cd "java programming"
java -cp bin com.transport.app.TransportManagementApp
```
**Manual Giving Menu Options:**
- `1`: View all available bus schedules
- `2`: Search bus schedules by entering source and destination manually
- `3`: Book ticket by entering passenger ID, name, schedule ID, and seat preference manually
- `4`: Cancel ticket by entering booking ID manually (seat is immediately released)
- `5`: View ticket and print boarding pass
- `6`: Admin: Add a new bus to the fleet
- `7`: Admin: Add a new bus schedule
- `8`: Analytical reports (Stream API: Route Revenue & Bus Occupancy)
- `9`: Multithreaded concurrency simulation (Unit 3)
- `10`: Exit

---

## 4. Foundation of Data Science

- **Syllabus Reference:** `25ITC31 FOUNDATIONS OF DATA SCIENCE`
- **Architecture Implemented (From Class Diagram):**
  - `DataProcessor`: Loads data, handles missing values (median imputation), removes duplicates, converts types, and treats outliers using Z-score and IQR clipping.
  - `AnalysisEngine`: Measures central tendency (mean, median), dispersion (std dev, variance, IQR), quantiles (25%, 50%, 75%), route-wise revenue, and passenger travel patterns.
  - `Visualization`: Generates publication-ready charts using Matplotlib & Seaborn.
  - `Report`: Compiles and exports structured analytics summaries.
- **Deliverables:**
  - [data_science_app.py](foundation%20of%20data%20science/data_science_app.py): Complete analytical engine
  - [transport_booking_dataset.csv](foundation%20of%20data%20science/transport_booking_dataset.csv): 300+ record dataset
  - [run_fds.bat](foundation%20of%20data%20science/run_fds.bat): Double-clickable batch runner
  - [fds_output.txt](foundation%20of%20data%20science/fds_output.txt): Full textual analytics report
  - [fds_execution_screenshot.png](foundation%20of%20data%20science/fds_execution_screenshot.png): Terminal screenshot
  - **7 Publication Charts in [visualizations/](foundation%20of%20data%20science/visualizations/):**
    1. `time_series_bookings.png` - Daily booking volume over time
    2. `route_revenue_bar.png` - Total revenue per route
    3. `bustype_pie_chart.png` - Fleet booking preference distribution
    4. `fare_distribution_hist.png` - Ticket fare histogram & Kernel Density Estimate
    5. `distance_vs_fare_scatter.png` - Distance vs. Fare pricing correlation
    6. `route_fare_boxplot.png` - Route fare spread & statistical outliers
    7. `travel_correlation_heatmap.png` - Heatmap of quantitative transit metrics

### How to Run Data Science (Manual Giving Mode)
Double-click the runnable batch file:
```cmd
foundation of data science\run_fds.bat
```
Or run via terminal:
```cmd
cd "foundation of data science"
python data_science_app.py
```
**Manual Giving Menu Options:**
- `1`: Run full EDA pipeline & generate/save all 7 visualizations
- `2`: Manual route query & specific route analytics (user types Source and Destination)
- `3`: Manual outlier & Z-score analysis (user enters custom $|z|$ threshold)
- `4`: Inspect data cleaning & preprocessing log
- `5`: View descriptive statistics (Mean, Median, Std Dev, IQR)
- `6`: View cross-tabulation pivot table (Bus Type vs. Route)
- `7`: Exit

---

## Deliverables Checklist & Compliance

| Requirement | Category | File / Artifact Path | Status |
| :--- | :--- | :--- | :---: |
| SRS Document (IEEE 830) | Software Engineering | `software engineering/Software_Requirements_Specification.md` | **Complete** |
| 7 System Design Diagrams | Software Engineering | `software engineering/diagrams/*.jpg` | **Complete** |
| System Design & Architecture Doc | Software Engineering | `software engineering/System_Design_and_Architecture.md` | **Complete** |
| Software Testing Document | Software Engineering | `software engineering/Software_Testing_Document.md` | **Complete** |
| Project Management & Cost Estimation | Software Engineering | `software engineering/Project_Management_and_Estimation.md` | **Complete** |
| ER Diagram | DBMS | `database management system/ER_Diagram.jpg` | **Complete** |
| Relational Model & Schema | DBMS | `database management system/Relational_Model.md` | **Complete** |
| Normalization (1NF to BCNF) | DBMS | `database management system/Normalization_Document.md` | **Complete** |
| SQL Schema (DDL) & Sample Data (DML) | DBMS | `database management system/schema.sql`, `sample_data.sql` | **Complete** |
| Advanced SQL Queries | DBMS | `database management system/queries.sql` | **Complete** |
| Interactive DBMS Console (Manual Giving) | DBMS | `database management system/dbms_interactive.py` | **Complete** |
| DBMS Batch Runner | DBMS | `database management system/run_dbms.bat` | **Complete** |
| DBMS Output & Screenshot | DBMS | `database management system/dbms_output.txt`, `dbms_execution_screenshot.png` | **Complete** |
| Java Application Code | Java Programming | `java programming/src/com/transport/**/*.java` | **Complete** |
| Compiled Java Bytecode | Java Programming | `java programming/bin/com/transport/**/*.class` | **Complete** |
| Java Batch Runner | Java Programming | `java programming/run_java.bat` | **Complete** |
| Java Output & Screenshot | Java Programming | `java programming/java_output.txt`, `java_execution_screenshot.png` | **Complete** |
| Python Data Science Engine | Data Science | `foundation of data science/data_science_app.py` | **Complete** |
| Transport Booking Dataset | Data Science | `foundation of data science/transport_booking_dataset.csv` | **Complete** |
| 7 Academic Visualizations | Data Science | `foundation of data science/visualizations/*.png` | **Complete** |
| Data Science Batch Runner | Data Science | `foundation of data science/run_fds.bat` | **Complete** |
| Data Science Output & Screenshot | Data Science | `foundation of data science/fds_output.txt`, `fds_execution_screenshot.png` | **Complete** |
| GitHub Root Readme | Root | `README.md` | **Complete** |
