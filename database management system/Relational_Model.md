# Relational Model Specification
## Public Transport Management and Passenger Travel Analysis System

### Course: 25ITT31 Database Management Systems

---

## 1. Relational Schema Representation

The relational model represents the database as a collection of relations (tables). Each relation consists of a set of attributes with defined domains.

```text
Passenger (
    PassengerID [PK], Name, Email, Phone, Gender, Address
)

Bus (
    BusID [PK], BusNumber, Capacity, BusType, Status
)

Route (
    RouteID [PK], Source, Destination, Distance
)

Schedule (
    ScheduleID [PK], BusID [FK -> Bus.BusID], RouteID [FK -> Route.RouteID], 
    TravelDate, DepartureTime, ArrivalTime, Fare
)

Booking (
    BookingID [PK], PassengerID [FK -> Passenger.PassengerID], 
    ScheduleID [FK -> Schedule.ScheduleID], BookingDate, TravelDate, 
    SeatNo, Status, TotalFare
)

Ticket (
    TicketID [PK], BookingID [FK -> Booking.BookingID, UNIQUE], 
    TicketNo, IssueDate, Fare, QRCode
)

TransportAdministrator (
    AdminID [PK], Name, Email, Role
)

TravelAnalysis (
    AnalysisID [PK], AnalysisDate, TravelPattern, Insights, GraphURL
)
```

---

## 2. Integrity Constraints

1. **Key Constraints:**
   - Primary Keys uniquely identify each tuple (`PassengerID`, `BusID`, `RouteID`, `ScheduleID`, `BookingID`, `TicketID`, `AdminID`, `AnalysisID`).
   - Alternate / Candidate Keys enforce uniqueness: `Passenger(Email)`, `Bus(BusNumber)`, `Route(Source, Destination)`, `Ticket(TicketNo)`.

2. **Entity Integrity:**
   - Primary key attributes cannot contain NULL values ($\forall t \in R, t[\text{PK}] \neq \text{NULL}$).

3. **Referential Integrity:**
   - `Schedule.BusID` $\subseteq$ `Bus.BusID`
   - `Schedule.RouteID` $\subseteq$ `Route.RouteID`
   - `Booking.PassengerID` $\subseteq$ `Passenger.PassengerID`
   - `Booking.ScheduleID` $\subseteq$ `Schedule.ScheduleID`
   - `Ticket.BookingID` $\subseteq$ `Booking.BookingID`

4. **Domain Constraints:**
   - `Capacity` $> 0$
   - `Distance` $> 0$
   - `Fare` $\ge 0$
   - `Gender` $\in \{\text{'Male'}, \text{'Female'}, \text{'Other'}\}$
   - `BusType` $\in \{\text{'AC Sleeper'}, \text{'Non-AC Seater'}, \text{'Volvo Multi-Axle'}, \text{'Electric Express'}\}$
   - `Status` $\in \{\text{'Confirmed'}, \text{'Cancelled'}, \text{'Completed'}\}$

---

## 3. Relational Algebra Queries

### Query 1: Selection ($\sigma$) and Projection ($\pi$)
Find names and phone numbers of passengers who booked on or after '2026-11-18':
$$\pi_{\text{Name}, \text{Phone}} \left( \sigma_{\text{BookingDate} \ge \text{'2026-11-18'}} (\text{Passenger} \bowtie_{\text{Passenger.PassengerID} = \text{Booking.PassengerID}} \text{Booking}) \right)$$

### Query 2: Natural Join ($\bowtie$)
List routes and schedules for Volvo Multi-Axle buses:
$$\pi_{\text{BusNumber}, \text{Source}, \text{Destination}, \text{TravelDate}, \text{Fare}} \left( \sigma_{\text{BusType} = \text{'Volvo Multi-Axle'}} (\text{Bus} \bowtie \text{Schedule} \bowtie \text{Route}) \right)$$

### Query 3: Set Difference ($-$)
Find buses that currently have no scheduled trips:
$$\pi_{\text{BusID}}(\text{Bus}) - \pi_{\text{BusID}}(\text{Schedule})$$

### Query 4: Aggregate and Grouping ($\mathcal{G}$)
Compute total revenue per route:
$${}_{\text{RouteID}} \mathcal{G}_{\text{SUM}(\text{TotalFare}) \rightarrow \text{TotalRevenue}} (\text{Schedule} \bowtie \text{Booking})$$
