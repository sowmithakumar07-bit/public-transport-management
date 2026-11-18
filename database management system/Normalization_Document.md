# Database Normalization Document
## Public Transport Management and Passenger Travel Analysis System

### Course: 25ITT31 Database Management Systems
**Department of Information Technology**

---

## 1. Introduction to Normalization
Database normalization is a systematic technique of organizing data in a relational database to minimize data redundancy, avoid insertion, update, and deletion anomalies, and maintain data integrity.

In this project, the relational schema for the **Public Transport Management and Passenger Travel Analysis System** has been designed and normalized through **1NF, 2NF, 3NF, and Boyce-Codd Normal Form (BCNF)**.

---

## 2. Functional Dependencies and Armstrong's Axioms

### 2.1 Functional Dependencies (FDs)
A functional dependency $\alpha \rightarrow \beta$ specifies that the value of attribute set $\alpha$ uniquely determines the value of attribute set $\beta$.

The fundamental functional dependencies in our system are:

1. **Passenger Relation:**
   - $\text{PassengerID} \rightarrow \{\text{Name}, \text{Email}, \text{Phone}, \text{Gender}, \text{Address}\}$
   - $\text{Email} \rightarrow \{\text{PassengerID}, \text{Name}, \text{Phone}, \text{Gender}, \text{Address}\}$ (Candidate Key)

2. **Bus Relation:**
   - $\text{BusID} \rightarrow \{\text{BusNumber}, \text{Capacity}, \text{BusType}, \text{Status}\}$
   - $\text{BusNumber} \rightarrow \{\text{BusID}, \text{Capacity}, \text{BusType}, \text{Status}\}$ (Candidate Key)

3. **Route Relation:**
   - $\text{RouteID} \rightarrow \{\text{Source}, \text{Destination}, \text{Distance}\}$
   - $\{\text{Source}, \text{Destination}\} \rightarrow \{\text{RouteID}, \text{Distance}\}$ (Candidate Key)

4. **Schedule Relation:**
   - $\text{ScheduleID} \rightarrow \{\text{BusID}, \text{RouteID}, \text{TravelDate}, \text{DepartureTime}, \text{ArrivalTime}, \text{Fare}\}$

5. **Booking Relation:**
   - $\text{BookingID} \rightarrow \{\text{PassengerID}, \text{ScheduleID}, \text{BookingDate}, \text{TravelDate}, \text{SeatNo}, \text{Status}, \text{TotalFare}\}$
   - $\{\text{ScheduleID}, \text{SeatNo}\} \rightarrow \{\text{BookingID}, \text{PassengerID}, \text{BookingDate}, \text{TravelDate}, \text{Status}, \text{TotalFare}\}$ (Prevents double booking)

6. **Ticket Relation:**
   - $\text{TicketID} \rightarrow \{\text{BookingID}, \text{TicketNo}, \text{IssueDate}, \text{Fare}, \text{QRCode}\}$
   - $\text{TicketNo} \rightarrow \{\text{TicketID}, \text{BookingID}, \text{IssueDate}, \text{Fare}, \text{QRCode}\}$ (Candidate Key)
   - $\text{BookingID} \rightarrow \{\text{TicketID}, \text{TicketNo}, \text{IssueDate}, \text{Fare}, \text{QRCode}\}$ (1:1 Relationship with Booking)

### 2.2 Armstrong's Axioms
The derivation of functional dependencies follows Armstrong's inference rules:
1. **Reflexivity:** If $\beta \subseteq \alpha$, then $\alpha \rightarrow \beta$.
   - Example: $\{\text{PassengerID}, \text{Email}\} \rightarrow \text{PassengerID}$.
2. **Augmentation:** If $\alpha \rightarrow \beta$, then $\gamma\alpha \rightarrow \gamma\beta$.
   - Example: Since $\text{BusID} \rightarrow \text{Capacity}$, $\{\text{BusID}, \text{Date}\} \rightarrow \{\text{Capacity}, \text{Date}\}$.
3. **Transitivity:** If $\alpha \rightarrow \beta$ and $\beta \rightarrow \gamma$, then $\alpha \rightarrow \gamma$.
   - Example: $\text{TicketID} \rightarrow \text{BookingID}$ and $\text{BookingID} \rightarrow \text{PassengerID} \implies \text{TicketID} \rightarrow \text{PassengerID}$.

---

## 3. Step-by-Step Normalization Process

### 3.1 Unnormalized Form (UNF)
Consider an initial unnormalized relation representing all booking activities:
```text
TRANSPORT_RECORD(
    PassengerID, Name, Email, Phone, BusID, BusNumber, Capacity, 
    BusType, RouteID, Source, Destination, Distance, ScheduleID, 
    TravelDate, DepartureTime, SeatNo, TotalFare, TicketNo
)
```
- **Anomalies in UNF:**
  - *Insertion Anomaly:* Cannot add a new bus or route without a passenger booking.
  - *Deletion Anomaly:* Deleting a passenger's booking deletes bus and schedule information.
  - *Update Anomaly:* Modifying bus capacity requires updating hundreds of booking rows.

---

### 3.2 First Normal Form (1NF)
> **Definition:** A relation $R$ is in 1NF if and only if all domain values are atomic (indivisible) and there are no repeating groups or multivalued attributes.

- Multi-valued attributes like phone numbers, seat lists, or multiple schedules are decomposed into separate rows with atomic values.
- Primary Key selected for Booking composite context: $\{\text{PassengerID}, \text{ScheduleID}, \text{SeatNo}\}$.

---

### 3.3 Second Normal Form (2NF)
> **Definition:** A relation $R$ is in 2NF if it is in 1NF and no non-prime attribute is partially dependent on any candidate key (no Partial Functional Dependency: $X \subset \text{CK} \rightarrow Y$).

- In the composite relation $\{\text{PassengerID}, \text{ScheduleID}, \text{SeatNo}\}$:
  - $\text{PassengerID} \rightarrow \{\text{Name}, \text{Email}, \text{Phone}\}$ (Partial dependency on part of key $\text{PassengerID}$)
  - $\text{ScheduleID} \rightarrow \{\text{BusID}, \text{RouteID}, \text{Fare}\}$ (Partial dependency on $\text{ScheduleID}$)
- **Decomposition into 2NF:**
  - `Passenger(PassengerID, Name, Email, Phone, Gender, Address)`
  - `Schedule(ScheduleID, BusID, RouteID, TravelDate, DepartureTime, ArrivalTime, Fare)`
  - `Booking(BookingID, PassengerID, ScheduleID, BookingDate, TravelDate, SeatNo, Status, TotalFare)`

All partial dependencies are completely eliminated.

---

### 3.4 Third Normal Form (3NF)
> **Definition:** A relation $R$ is in 3NF if it is in 2NF and for every non-trivial functional dependency $X \rightarrow Y$:
> 1. $X$ is a superkey, OR
> 2. $Y$ is a prime attribute (member of a candidate key).
> (i.e., no transitive dependencies $X \rightarrow Y \rightarrow Z$).

- In an unseparated Schedule relation:
  - $\text{ScheduleID} \rightarrow \text{BusID} \rightarrow \{\text{BusNumber}, \text{Capacity}, \text{BusType}\}$ (Transitive dependency)
  - $\text{ScheduleID} \rightarrow \text{RouteID} \rightarrow \{\text{Source}, \text{Destination}, \text{Distance}\}$ (Transitive dependency)
- **Decomposition into 3NF:**
  - `Bus(BusID, BusNumber, Capacity, BusType, Status)` - Key: `BusID`
  - `Route(RouteID, Source, Destination, Distance)` - Key: `RouteID`
  - `Schedule(ScheduleID, BusID, RouteID, TravelDate, DepartureTime, ArrivalTime, Fare)` - Foreign keys `BusID`, `RouteID`
  - `Booking(BookingID, PassengerID, ScheduleID, BookingDate, TravelDate, SeatNo, Status, TotalFare)`
  - `Ticket(TicketID, BookingID, TicketNo, IssueDate, Fare, QRCode)`

Every non-key attribute is determined directly and solely by the candidate key.

---

### 3.5 Boyce-Codd Normal Form (BCNF)
> **Definition:** A relation $R$ is in BCNF if for every non-trivial functional dependency $X \rightarrow Y$, $X$ is a superkey.

Let us evaluate every decomposed table:
1. `Passenger`: $\text{PassengerID}$ and $\text{Email}$ are candidate keys (superkeys). Satisfies BCNF.
2. `Bus`: $\text{BusID}$ and $\text{BusNumber}$ are superkeys. Satisfies BCNF.
3. `Route`: $\text{RouteID}$ and $\{\text{Source}, \text{Destination}\}$ are superkeys. Satisfies BCNF.
4. `Schedule`: $\text{ScheduleID}$ is the superkey. Satisfies BCNF.
5. `Booking`: $\text{BookingID}$ and $\{\text{ScheduleID}, \text{SeatNo}\}$ are superkeys. Satisfies BCNF.
6. `Ticket`: $\text{TicketID}$, $\text{BookingID}$, and $\text{TicketNo}$ are superkeys. Satisfies BCNF.

**Conclusion:** All relations in the system achieve **Lossless Join Decomposition** and **Dependency Preservation** in **BCNF**.
