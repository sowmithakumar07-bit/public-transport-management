-- ============================================================================
-- PUBLIC TRANSPORT MANAGEMENT AND PASSENGER TRAVEL ANALYSIS SYSTEM
-- Database Management Systems - Schema Definition (DDL)
-- Compatible with MySQL 8.0+ and SQLite 3
-- ============================================================================

-- 1. Create Passenger Table
CREATE TABLE IF NOT EXISTS Passenger (
    PassengerID INT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Phone VARCHAR(20) NOT NULL,
    Gender VARCHAR(10) CHECK (Gender IN ('Male', 'Female', 'Other')),
    Address VARCHAR(255) NOT NULL
);

-- 2. Create Bus Table
CREATE TABLE IF NOT EXISTS Bus (
    BusID VARCHAR(20) PRIMARY KEY,
    BusNumber VARCHAR(50) UNIQUE NOT NULL,
    Capacity INT NOT NULL CHECK (Capacity > 0),
    BusType VARCHAR(50) NOT NULL CHECK (BusType IN ('AC Sleeper', 'Non-AC Seater', 'Volvo Multi-Axle', 'Electric Express')),
    Status VARCHAR(20) DEFAULT 'Active' CHECK (Status IN ('Active', 'In Maintenance', 'Retired'))
);

-- 3. Create Route Table
CREATE TABLE IF NOT EXISTS Route (
    RouteID INT PRIMARY KEY,
    Source VARCHAR(100) NOT NULL,
    Destination VARCHAR(100) NOT NULL,
    Distance DOUBLE NOT NULL CHECK (Distance > 0),
    UNIQUE (Source, Destination)
);

-- 4. Create Schedule Table
CREATE TABLE IF NOT EXISTS Schedule (
    ScheduleID INT PRIMARY KEY,
    BusID VARCHAR(20) NOT NULL,
    RouteID INT NOT NULL,
    TravelDate DATE NOT NULL,
    DepartureTime TIME NOT NULL,
    ArrivalTime TIME NOT NULL,
    Fare DOUBLE NOT NULL CHECK (Fare >= 0),
    FOREIGN KEY (BusID) REFERENCES Bus(BusID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (RouteID) REFERENCES Route(RouteID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 5. Create Booking Table
CREATE TABLE IF NOT EXISTS Booking (
    BookingID INT PRIMARY KEY,
    PassengerID INT NOT NULL,
    ScheduleID INT NOT NULL,
    BookingDate DATE NOT NULL,
    TravelDate DATE NOT NULL,
    SeatNo VARCHAR(10) NOT NULL,
    Status VARCHAR(20) DEFAULT 'Confirmed' CHECK (Status IN ('Confirmed', 'Cancelled', 'Completed')),
    TotalFare DOUBLE NOT NULL CHECK (TotalFare >= 0),
    FOREIGN KEY (PassengerID) REFERENCES Passenger(PassengerID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ScheduleID) REFERENCES Schedule(ScheduleID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 6. Create Ticket Table
CREATE TABLE IF NOT EXISTS Ticket (
    TicketID INT PRIMARY KEY,
    BookingID INT UNIQUE NOT NULL,
    TicketNo VARCHAR(50) UNIQUE NOT NULL,
    IssueDate DATE NOT NULL,
    Fare DOUBLE NOT NULL CHECK (Fare >= 0),
    QRCode VARCHAR(255) NOT NULL,
    FOREIGN KEY (BookingID) REFERENCES Booking(BookingID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 7. Create Transport Administrator Table
CREATE TABLE IF NOT EXISTS TransportAdministrator (
    AdminID INT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Role VARCHAR(50) NOT NULL
);

-- 8. Create Travel Analysis Records Table
CREATE TABLE IF NOT EXISTS TravelAnalysis (
    AnalysisID INT PRIMARY KEY,
    AnalysisDate DATE NOT NULL,
    TravelPattern VARCHAR(255) NOT NULL,
    Insights TEXT NOT NULL,
    GraphURL VARCHAR(255) NOT NULL
);

-- ============================================================================
-- INDEXES FOR PERFORMANCE OPTIMIZATION
-- ============================================================================
CREATE INDEX IF NOT EXISTS idx_schedule_bus ON Schedule(BusID);
CREATE INDEX IF NOT EXISTS idx_schedule_route ON Schedule(RouteID);
CREATE INDEX IF NOT EXISTS idx_schedule_traveldate ON Schedule(TravelDate);
CREATE INDEX IF NOT EXISTS idx_booking_passenger ON Booking(PassengerID);
CREATE INDEX IF NOT EXISTS idx_booking_schedule ON Booking(ScheduleID);
CREATE INDEX IF NOT EXISTS idx_ticket_booking ON Ticket(BookingID);

-- ============================================================================
-- DATABASE VIEWS FOR REPORTING & ANALYTICS
-- ============================================================================

-- View 1: Route-wise Revenue and Booking Volume
CREATE VIEW IF NOT EXISTS vw_RouteWiseRevenue AS
SELECT 
    r.RouteID,
    r.Source,
    r.Destination,
    r.Distance,
    COUNT(b.BookingID) AS TotalBookings,
    COALESCE(SUM(CASE WHEN b.Status = 'Confirmed' THEN b.TotalFare ELSE 0 END), 0) AS TotalRevenue,
    ROUND(AVG(s.Fare), 2) AS AverageTicketFare
FROM Route r
LEFT JOIN Schedule s ON r.RouteID = s.RouteID
LEFT JOIN Booking b ON s.ScheduleID = b.ScheduleID
GROUP BY r.RouteID, r.Source, r.Destination, r.Distance;

-- View 2: Bus Occupancy and Utilization Summary
CREATE VIEW IF NOT EXISTS vw_BusOccupancy AS
SELECT 
    b.BusID,
    b.BusNumber,
    b.BusType,
    b.Capacity,
    COUNT(bk.BookingID) AS SeatsBooked,
    ROUND((COUNT(bk.BookingID) * 100.0) / b.Capacity, 2) AS OccupancyPercentage,
    (b.Capacity - COUNT(bk.BookingID)) AS RemainingSeats
FROM Bus b
JOIN Schedule s ON b.BusID = s.BusID
LEFT JOIN Booking bk ON s.ScheduleID = bk.ScheduleID AND bk.Status = 'Confirmed'
GROUP BY b.BusID, b.BusNumber, b.BusType, b.Capacity;

-- View 3: Comprehensive Passenger Booking History
CREATE VIEW IF NOT EXISTS vw_PassengerBookingHistory AS
SELECT 
    p.PassengerID,
    p.Name AS PassengerName,
    p.Phone,
    t.TicketNo,
    b.SeatNo,
    b.Status AS BookingStatus,
    r.Source,
    r.Destination,
    s.TravelDate,
    s.DepartureTime,
    b.TotalFare
FROM Passenger p
JOIN Booking b ON p.PassengerID = b.PassengerID
JOIN Ticket t ON b.BookingID = t.BookingID
JOIN Schedule s ON b.ScheduleID = s.ScheduleID
JOIN Route r ON s.RouteID = r.RouteID;
