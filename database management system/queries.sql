-- ============================================================================
-- PUBLIC TRANSPORT MANAGEMENT AND PASSENGER TRAVEL ANALYSIS SYSTEM
-- Comprehensive SQL Queries Demonstration
-- Aligned with DBMS Syllabus (Units 1, 2, 3)
-- ============================================================================

-- ============================================================================
-- SECTION 1: BASIC DML & FILTERING OPERATIONS
-- ============================================================================

-- Q1. Retrieve all active buses with capacity greater than or equal to 35
SELECT BusID, BusNumber, Capacity, BusType, Status
FROM Bus
WHERE Status = 'Active' AND Capacity >= 35
ORDER BY Capacity DESC;

-- Q2. List all routes originating from 'Bangalore' sorted by distance
SELECT RouteID, Source, Destination, Distance
FROM Route
WHERE Source = 'Bangalore'
ORDER BY Distance ASC;

-- Q3. Find distinct bus types available in the fleet
SELECT DISTINCT BusType
FROM Bus;

-- ============================================================================
-- SECTION 2: AGGREGATE FUNCTIONS & GROUP BY / HAVING CLAUSES
-- ============================================================================

-- Q4. Calculate total fleet capacity and average bus capacity
SELECT 
    COUNT(BusID) AS TotalBuses,
    SUM(Capacity) AS TotalFleetCapacity,
    ROUND(AVG(Capacity), 2) AS AverageCapacity,
    MIN(Capacity) AS MinCapacity,
    MAX(Capacity) AS MaxCapacity
FROM Bus
WHERE Status = 'Active';

-- Q5. Route-wise total revenue and booking count with HAVING filter (> 1000 revenue)
SELECT 
    r.RouteID,
    r.Source,
    r.Destination,
    COUNT(b.BookingID) AS TotalBookings,
    SUM(b.TotalFare) AS RouteRevenue
FROM Route r
JOIN Schedule s ON r.RouteID = s.RouteID
JOIN Booking b ON s.ScheduleID = b.ScheduleID
WHERE b.Status = 'Confirmed'
GROUP BY r.RouteID, r.Source, r.Destination
HAVING SUM(b.TotalFare) >= 1500
ORDER BY RouteRevenue DESC;

-- Q6. Booking status distribution (Confirmed vs Cancelled)
SELECT 
    Status,
    COUNT(BookingID) AS StatusCount,
    SUM(TotalFare) AS StatusTotalAmount
FROM Booking
GROUP BY Status;

-- ============================================================================
-- SECTION 3: COMPLEX JOINS (INNER, LEFT, MULTI-TABLE JOINS)
-- ============================================================================

-- Q7. Multi-table Join: Full passenger ticket details
SELECT 
    p.Name AS PassengerName,
    p.Phone,
    t.TicketNo,
    b.SeatNo,
    r.Source,
    r.Destination,
    s.TravelDate,
    s.DepartureTime,
    b.TotalFare,
    bs.BusType
FROM Passenger p
INNER JOIN Booking b ON p.PassengerID = b.PassengerID
INNER JOIN Ticket t ON b.BookingID = t.BookingID
INNER JOIN Schedule s ON b.ScheduleID = s.ScheduleID
INNER JOIN Route r ON s.RouteID = r.RouteID
INNER JOIN Bus bs ON s.BusID = bs.BusID
ORDER BY s.TravelDate, s.DepartureTime;

-- Q8. LEFT JOIN to identify schedules with zero bookings
SELECT 
    s.ScheduleID,
    s.TravelDate,
    s.DepartureTime,
    r.Source,
    r.Destination,
    COUNT(b.BookingID) AS ConfirmedBookings
FROM Schedule s
JOIN Route r ON s.RouteID = r.RouteID
LEFT JOIN Booking b ON s.ScheduleID = b.ScheduleID AND b.Status = 'Confirmed'
GROUP BY s.ScheduleID, s.TravelDate, s.DepartureTime, r.Source, r.Destination
HAVING COUNT(b.BookingID) = 0;

-- ============================================================================
-- SECTION 4: SUBQUERIES (CORRELATED & NON-CORRELATED)
-- ============================================================================

-- Q9. Non-correlated Subquery: Find schedules where fare is above the system-wide average fare
SELECT ScheduleID, BusID, RouteID, TravelDate, Fare
FROM Schedule
WHERE Fare > (SELECT AVG(Fare) FROM Schedule)
ORDER BY Fare DESC;

-- Q10. Correlated Subquery: Find passengers who have spent more than the average expenditure of all passengers
SELECT p.PassengerID, p.Name, SUM(b.TotalFare) AS TotalSpent
FROM Passenger p
JOIN Booking b ON p.PassengerID = b.PassengerID
WHERE b.Status = 'Confirmed'
GROUP BY p.PassengerID, p.Name
HAVING SUM(b.TotalFare) > (
    SELECT AVG(PassengerTotal) 
    FROM (
        SELECT SUM(TotalFare) AS PassengerTotal 
        FROM Booking 
        WHERE Status = 'Confirmed' 
        GROUP BY PassengerID
    ) AS SubTotals
);

-- Q11. Subquery with EXISTS: Identify routes that have at least one active schedule
SELECT RouteID, Source, Destination, Distance
FROM Route r
WHERE EXISTS (
    SELECT 1 FROM Schedule s WHERE s.RouteID = r.RouteID
);

-- ============================================================================
-- SECTION 5: VIEW QUERIES & ANALYTICAL CALCULATIONS
-- ============================================================================

-- Q12. Query the Route-wise Revenue View
SELECT * FROM vw_RouteWiseRevenue
ORDER BY TotalRevenue DESC;

-- Q13. Query the Bus Occupancy View
SELECT * FROM vw_BusOccupancy
ORDER BY OccupancyPercentage DESC;

-- Q14. Top 3 highest revenue routes
SELECT Source, Destination, TotalRevenue
FROM vw_RouteWiseRevenue
ORDER BY TotalRevenue DESC
LIMIT 3;
