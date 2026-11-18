-- ============================================================================
-- PUBLIC TRANSPORT MANAGEMENT AND PASSENGER TRAVEL ANALYSIS SYSTEM
-- Sample Data Insertion Script (DML)
-- ============================================================================

-- 1. Insert Passengers
INSERT INTO Passenger (PassengerID, Name, Email, Phone, Gender, Address) VALUES
(101, 'Aarav Sharma', 'aarav.sharma@example.com', '9876543210', 'Male', '12 Lake View, Bangalore'),
(102, 'Diya Patel', 'diya.patel@example.com', '9876543211', 'Female', '45 MG Road, Chennai'),
(103, 'Rohan Verma', 'rohan.verma@example.com', '9876543212', 'Male', '78 Ring Road, Hyderabad'),
(104, 'Sneha Reddy', 'sneha.reddy@example.com', '9876543213', 'Female', '23 Banjara Hills, Hyderabad'),
(105, 'Karthik Raman', 'karthik.raman@example.com', '9876543214', 'Male', '89 Anna Nagar, Chennai'),
(106, 'Pooja Nair', 'pooja.nair@example.com', '9876543215', 'Female', '14 Marine Drive, Kochi'),
(107, 'Vikram Singh', 'vikram.singh@example.com', '9876543216', 'Male', '56 Civil Lines, Coimbatore'),
(108, 'Ananya Iyer', 'ananya.iyer@example.com', '9876543217', 'Female', '67 Indira Nagar, Bangalore'),
(109, 'Rajesh Kumar', 'rajesh.kumar@example.com', '9876543218', 'Male', '34 RS Puram, Coimbatore'),
(110, 'Meera Joshi', 'meera.joshi@example.com', '9876543219', 'Female', '90 Race Course, Madurai'),
(111, 'Suresh Menon', 'suresh.menon@example.com', '9876543220', 'Male', '11 Palayam, Trivandrum'),
(112, 'Kavya Sundaram', 'kavya.sundaram@example.com', '9876543221', 'Female', '22 Town Hall, Salem');

-- 2. Insert Buses
INSERT INTO Bus (BusID, BusNumber, Capacity, BusType, Status) VALUES
('BUS-01', 'KA-01-F-1001', 30, 'AC Sleeper', 'Active'),
('BUS-02', 'TN-09-E-2002', 45, 'Non-AC Seater', 'Active'),
('BUS-03', 'TS-07-H-3003', 36, 'Volvo Multi-Axle', 'Active'),
('BUS-04', 'KL-01-C-4004', 32, 'Electric Express', 'Active'),
('BUS-05', 'KA-05-D-5005', 40, 'Volvo Multi-Axle', 'Active'),
('BUS-06', 'TN-38-A-6006', 45, 'Non-AC Seater', 'Active'),
('BUS-07', 'TS-09-B-7007', 30, 'AC Sleeper', 'Active'),
('BUS-08', 'KL-07-K-8008', 28, 'Electric Express', 'In Maintenance');

-- 3. Insert Routes
INSERT INTO Route (RouteID, Source, Destination, Distance) VALUES
(201, 'Bangalore', 'Chennai', 350.0),
(202, 'Chennai', 'Bangalore', 350.0),
(203, 'Bangalore', 'Hyderabad', 570.0),
(204, 'Hyderabad', 'Bangalore', 570.0),
(205, 'Chennai', 'Coimbatore', 510.0),
(206, 'Coimbatore', 'Chennai', 510.0),
(207, 'Bangalore', 'Kochi', 540.0),
(208, 'Chennai', 'Madurai', 460.0);

-- 4. Insert Schedules
INSERT INTO Schedule (ScheduleID, BusID, RouteID, TravelDate, DepartureTime, ArrivalTime, Fare) VALUES
(301, 'BUS-01', 201, '2026-11-20', '21:00:00', '05:30:00', 850.00),
(302, 'BUS-02', 202, '2026-11-20', '07:30:00', '14:30:00', 450.00),
(303, 'BUS-03', 203, '2026-11-21', '20:00:00', '06:00:00', 1100.00),
(304, 'BUS-04', 204, '2026-11-21', '21:30:00', '07:00:00', 950.00),
(305, 'BUS-05', 205, '2026-11-22', '22:00:00', '06:30:00', 1050.00),
(306, 'BUS-06', 206, '2026-11-22', '08:00:00', '17:00:00', 500.00),
(307, 'BUS-01', 207, '2026-11-23', '19:30:00', '06:00:00', 1200.00),
(308, 'BUS-07', 208, '2026-11-23', '22:30:00', '06:00:00', 800.00),
(309, 'BUS-03', 201, '2026-11-24', '14:00:00', '21:00:00', 900.00),
(310, 'BUS-04', 203, '2026-11-24', '20:30:00', '06:30:00', 1000.00);

-- 5. Insert Bookings
INSERT INTO Booking (BookingID, PassengerID, ScheduleID, BookingDate, TravelDate, SeatNo, Status, TotalFare) VALUES
(401, 101, 301, '2026-11-15', '2026-11-20', 'S1', 'Confirmed', 850.00),
(402, 102, 301, '2026-11-15', '2026-11-20', 'S2', 'Confirmed', 850.00),
(403, 103, 301, '2026-11-16', '2026-11-20', 'S3', 'Confirmed', 850.00),
(404, 104, 301, '2026-11-16', '2026-11-20', 'S4', 'Confirmed', 850.00),
(405, 105, 302, '2026-11-16', '2026-11-20', '12', 'Confirmed', 450.00),
(406, 106, 302, '2026-11-17', '2026-11-20', '14', 'Cancelled', 450.00),
(407, 107, 303, '2026-11-17', '2026-11-21', 'V01', 'Confirmed', 1100.00),
(408, 108, 303, '2026-11-17', '2026-11-21', 'V02', 'Confirmed', 1100.00),
(409, 109, 303, '2026-11-18', '2026-11-21', 'V05', 'Confirmed', 1100.00),
(410, 110, 304, '2026-11-18', '2026-11-21', 'E01', 'Confirmed', 950.00),
(411, 111, 304, '2026-11-18', '2026-11-21', 'E02', 'Confirmed', 950.00),
(412, 112, 305, '2026-11-19', '2026-11-22', 'V10', 'Confirmed', 1050.00),
(413, 101, 305, '2026-11-19', '2026-11-22', 'V11', 'Confirmed', 1050.00),
(414, 102, 306, '2026-11-19', '2026-11-22', '21', 'Confirmed', 500.00),
(415, 103, 307, '2026-11-19', '2026-11-23', 'S08', 'Confirmed', 1200.00),
(416, 104, 307, '2026-11-20', '2026-11-23', 'S09', 'Confirmed', 1200.00),
(417, 105, 308, '2026-11-20', '2026-11-23', 'S15', 'Confirmed', 800.00),
(418, 106, 308, '2026-11-20', '2026-11-23', 'S16', 'Confirmed', 800.00),
(419, 107, 309, '2026-11-20', '2026-11-24', 'V08', 'Confirmed', 900.00),
(420, 108, 310, '2026-11-20', '2026-11-24', 'E08', 'Confirmed', 1000.00);

-- 6. Insert Tickets
INSERT INTO Ticket (TicketID, BookingID, TicketNo, IssueDate, Fare, QRCode) VALUES
(501, 401, 'TCK-202611-401', '2026-11-15', 850.00, 'QR-DATA-401-BLR-CHN'),
(502, 402, 'TCK-202611-402', '2026-11-15', 850.00, 'QR-DATA-402-BLR-CHN'),
(503, 403, 'TCK-202611-403', '2026-11-16', 850.00, 'QR-DATA-403-BLR-CHN'),
(504, 404, 'TCK-202611-404', '2026-11-16', 850.00, 'QR-DATA-404-BLR-CHN'),
(505, 405, 'TCK-202611-405', '2026-11-16', 450.00, 'QR-DATA-405-CHN-BLR'),
(506, 407, 'TCK-202611-407', '2026-11-17', 1100.00, 'QR-DATA-407-BLR-HYD'),
(507, 408, 'TCK-202611-408', '2026-11-17', 1100.00, 'QR-DATA-408-BLR-HYD'),
(508, 409, 'TCK-202611-409', '2026-11-18', 1100.00, 'QR-DATA-409-BLR-HYD'),
(509, 410, 'TCK-202611-410', '2026-11-18', 950.00, 'QR-DATA-410-HYD-BLR'),
(510, 411, 'TCK-202611-411', '2026-11-18', 950.00, 'QR-DATA-411-HYD-BLR'),
(511, 412, 'TCK-202611-412', '2026-11-19', 1050.00, 'QR-DATA-412-CHN-CBE'),
(512, 413, 'TCK-202611-413', '2026-11-19', 1050.00, 'QR-DATA-413-CHN-CBE'),
(513, 414, 'TCK-202611-414', '2026-11-19', 500.00, 'QR-DATA-414-CBE-CHN'),
(514, 415, 'TCK-202611-415', '2026-11-19', 1200.00, 'QR-DATA-415-BLR-COK'),
(515, 416, 'TCK-202611-416', '2026-11-20', 1200.00, 'QR-DATA-416-BLR-COK'),
(516, 417, 'TCK-202611-417', '2026-11-20', 800.00, 'QR-DATA-417-CHN-MDU'),
(517, 418, 'TCK-202611-418', '2026-11-20', 800.00, 'QR-DATA-418-CHN-MDU'),
(518, 419, 'TCK-202611-419', '2026-11-20', 900.00, 'QR-DATA-419-BLR-CHN'),
(519, 420, 'TCK-202611-420', '2026-11-20', 1000.00, 'QR-DATA-420-BLR-HYD');

-- 7. Insert Administrators
INSERT INTO TransportAdministrator (AdminID, Name, Email, Role) VALUES
(1, 'Girish Sundaram', 'admin.girish@transport.gov.in', 'Fleet General Manager'),
(2, 'Praveen Narayanan', 'admin.praveen@transport.gov.in', 'Operations Controller');

-- 8. Insert Travel Analysis Metadata
INSERT INTO TravelAnalysis (AnalysisID, AnalysisDate, TravelPattern, Insights, GraphURL) VALUES
(1, '2026-11-20', 'High Weekend Demand on Tech Corridor (Bangalore-Hyderabad)', 'Demand spikes by 85% on Friday night schedules; recommend adding 2 additional Volvo coaches.', 'visualizations/revenue_by_route.png'),
(2, '2026-11-21', 'Peak Early Morning Passenger Travel on Chennai-Bangalore', 'Day express schedules have consistent 92% occupancy across non-AC and electric bus variants.', 'visualizations/occupancy_by_bus.png');
