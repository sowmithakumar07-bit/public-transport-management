# Project Management and Software Estimation Document
## Public Transport Management and Passenger Travel Analysis System

### Course: 25ITT33 Software Engineering (Unit 5: Project Management)

---

## 1. The 4 P's Framework
1. **People:**
   - Development Team: Software Engineers, Database Administrators, Data Scientists, Quality Assurance Testers.
   - Stakeholders: Transport Operators, Passengers, Administrative Analysts.
2. **Product:**
   - Public Transport Management & Passenger Travel Analysis System.
   - Comprehensive modules covering booking management, relational persistence, and exploratory data analytics.
3. **Process:**
   - Agile Evolutionary Lifecycle combined with Unified Process (Inception, Elaboration, Construction, Transition).
4. **Project:**
   - Strict tracking of delivery milestones, unit test coverage, and documentation baselines.

---

## 2. Software Sizing & Metrics

### 2.1 Lines of Code (LOC) Analysis
- **Java Programming Module:** ~1,150 LOC (Model, Exception, Service, and Interactive Application classes)
- **Database Management (SQL):** ~450 LOC (DDL, DML, Views, Indexes, Queries)
- **Foundations of Data Science (Python):** ~650 LOC (DataProcessor, AnalysisEngine, Visualization, CLI)
- **Total Estimated Codebase:** **2,250 LOC** (2.25 KLOC)

### 2.2 Function Point (FP) Sizing
Using Albrecht's Function Point Metric:
- **External Inputs (EI):** Passenger registration, bus creation, route definition, schedule creation, booking request, cancellation request $\rightarrow 6 \times 4 = 24$
- **External Outputs (EO):** E-Ticket generation, revenue reports, occupancy tables $\rightarrow 3 \times 5 = 15$
- **External Inquiries (EQ):** Search schedules, query table records, check seat availability $\rightarrow 3 \times 3 = 9$
- **Internal Logical Files (ILF):** Passenger, Bus, Route, Schedule, Booking, Ticket $\rightarrow 6 \times 7 = 42$
- **External Interface Files (EIF):** Dataset CSV, Analytics Image files $\rightarrow 2 \times 5 = 10$
- **Unadjusted Function Points (UFP):**
  $$\text{UFP} = 24 + 15 + 9 + 42 + 10 = 100$$
- Assuming Value Adjustment Factor ($\text{VAF}$) of $1.05$:
  $$\text{FP} = \text{UFP} \times [0.65 + 0.01 \times \sum F_i] = 100 \times 1.05 = \mathbf{105\text{ FPs}}$$

---

## 3. COCOMO Cost & Effort Estimation

Applying the **Basic COCOMO Model** (Organic Mode: projects with small teams and stable requirements):
Constants for Organic Mode: $a_b = 2.4, b_b = 1.05, c_b = 2.5, d_b = 0.38$.

### 3.1 Effort Calculation
$$\text{Effort} = a_b \times (\text{KLOC})^{b_b}$$
$$\text{Effort} = 2.4 \times (2.25)^{1.05} \approx 2.4 \times 2.348 \approx \mathbf{5.64\text{ Person-Months}}$$

### 3.2 Development Time (Duration)
$$\text{Duration} = c_b \times (\text{Effort})^{d_b}$$
$$\text{Duration} = 2.5 \times (5.64)^{0.38} \approx 2.5 \times 1.932 \approx \mathbf{4.83\text{ Months}}$$

### 3.3 Average Staff Size
$$\text{Staff Size} = \frac{\text{Effort}}{\text{Duration}} = \frac{5.64}{4.83} \approx \mathbf{1.17 \approx 2\text{ Engineers}}$$

---

## 4. Risk Identification and Management (RMMM Plan)

| Risk Category | Risk Description | Probability | Impact | Mitigation Strategy |
| :--- | :--- | :---: | :---: | :--- |
| **Concurrency Risk** | Simultaneous bookings causing duplicate seat allocation | High | Severe | Thread synchronization on `Schedule.bookSeat()` using critical sections. |
| **Data Quality Risk** | Missing data or extreme outliers skewing analytics | Medium | Moderate | Impute missing values with medians and cap statistical outliers via Z-scores ($|z| > 3.0$). |
| **Referential Integrity** | Deletion of bus leaving orphaned schedule records | Low | High | Foreign key constraints with `ON DELETE CASCADE` and `ON UPDATE CASCADE`. |
| **System Usability** | Non-technical users finding CLI difficult | Medium | Low | Self-explanatory numeric menus with clear prompts and sample inputs. |

---

## 5. Software Quality Standards & CMMI Alignment
The project follows **CMMI Level 3 (Defined)** principles:
- Standardized documentation across SRS, Design, and Testing.
- Rigorous peer-review checkpoints for code and database schemas.
- Comprehensive traceability matrix linking Functional Requirements to Test Cases.
