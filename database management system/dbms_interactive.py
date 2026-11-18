"""
Public Transport Management and Passenger Travel Analysis System
DBMS Interactive Query & Management Console (Manual Giving)
Compatible with SQLite 3 (Built-in) and MySQL.
"""

import sqlite3
import os
import sys

DB_FILE = os.path.join(os.path.dirname(os.path.abspath(__file__)), "transport_system.db")
SCHEMA_FILE = os.path.join(os.path.dirname(os.path.abspath(__file__)), "schema.sql")
DATA_FILE = os.path.join(os.path.dirname(os.path.abspath(__file__)), "sample_data.sql")
OUTPUT_FILE = os.path.join(os.path.dirname(os.path.abspath(__file__)), "dbms_output.txt")


def initialize_database():
    """Initializes the database schema and sample data."""
    conn = sqlite3.connect(DB_FILE)
    cursor = conn.cursor()

    if os.path.exists(SCHEMA_FILE):
        with open(SCHEMA_FILE, "r", encoding="utf-8") as f:
            schema_sql = f.read()
        cursor.executescript(schema_sql)

    cursor.execute("SELECT COUNT(*) FROM Passenger")
    count = cursor.fetchone()[0]
    if count == 0 and os.path.exists(DATA_FILE):
        with open(DATA_FILE, "r", encoding="utf-8") as f:
            data_sql = f.read()
        cursor.executescript(data_sql)

    conn.commit()
    conn.close()


def print_table(cursor, rows, out_f=None):
    """Nicely formats and prints tabular SQL results."""
    if not cursor.description:
        print("[No data returned / Operation executed successfully]")
        if out_f:
            out_f.write("[No data returned / Operation executed successfully]\n")
        return

    headers = [col[0] for col in cursor.description]
    col_widths = [len(h) for h in headers]

    for row in rows:
        for idx, val in enumerate(row):
            col_widths[idx] = max(col_widths[idx], len(str(val) if val is not None else "NULL"))

    header_str = " | ".join(h.ljust(col_widths[i]) for i, h in enumerate(headers))
    sep_str = "-+-".join("-" * col_widths[i] for i in range(len(headers)))

    print(header_str)
    print(sep_str)
    if out_f:
        out_f.write(header_str + "\n" + sep_str + "\n")

    for row in rows:
        row_str = " | ".join(str(val if val is not None else "NULL").ljust(col_widths[i]) for i, val in enumerate(row))
        print(row_str)
        if out_f:
            out_f.write(row_str + "\n")

    print(f"({len(rows)} row(s) returned)\n")
    if out_f:
        out_f.write(f"({len(rows)} row(s) returned)\n\n")


def run_custom_query(conn, query, out_f=None):
    """Executes a custom user-typed query."""
    cursor = conn.cursor()
    try:
        cursor.execute(query)
        if query.strip().upper().startswith("SELECT") or query.strip().upper().startswith("PRAGMA"):
            rows = cursor.fetchall()
            print_table(cursor, rows, out_f)
        else:
            conn.commit()
            msg = f"Query executed successfully. Rows affected: {cursor.rowcount}\n"
            print(msg)
            if out_f:
                out_f.write(msg)
    except Exception as e:
        err = f"SQL Execution Error: {e}\n"
        print(err)
        if out_f:
            out_f.write(err)


def run_demo_suite(conn, out_f=None):
    """Runs syllabus-aligned sample queries and prints results."""
    queries = [
        ("Query 1: List all Active Buses with Capacity >= 35",
         "SELECT BusID, BusNumber, Capacity, BusType, Status FROM Bus WHERE Status = 'Active' AND Capacity >= 35;"),
        ("Query 2: Route-wise Revenue and Booking Count (Aggregation & Grouping)",
         "SELECT r.RouteID, r.Source, r.Destination, COUNT(b.BookingID) AS TotalBookings, SUM(b.TotalFare) AS Revenue "
         "FROM Route r JOIN Schedule s ON r.RouteID = s.RouteID JOIN Booking b ON s.ScheduleID = b.ScheduleID "
         "WHERE b.Status = 'Confirmed' GROUP BY r.RouteID, r.Source, r.Destination ORDER BY Revenue DESC;"),
        ("Query 3: View Route-Wise Revenue View",
         "SELECT * FROM vw_RouteWiseRevenue;"),
        ("Query 4: Bus Occupancy and Remaining Seats View",
         "SELECT * FROM vw_BusOccupancy;"),
        ("Query 5: Multi-table Passenger Booking Details (5-table JOIN)",
         "SELECT p.Name AS PassengerName, p.Phone, t.TicketNo, b.SeatNo, r.Source, r.Destination, s.TravelDate, b.TotalFare "
         "FROM Passenger p "
         "JOIN Booking b ON p.PassengerID = b.PassengerID "
         "JOIN Ticket t ON b.BookingID = t.BookingID "
         "JOIN Schedule s ON b.ScheduleID = s.ScheduleID "
         "JOIN Route r ON s.RouteID = r.RouteID LIMIT 8;"),
        ("Query 6: Subquery - Schedules with Fare greater than Average Fare",
         "SELECT ScheduleID, BusID, RouteID, TravelDate, Fare FROM Schedule WHERE Fare > (SELECT AVG(Fare) FROM Schedule);")
    ]

    for title, q in queries:
        header = f"=== {title} ===\nSQL: {q}\n"
        print(header)
        if out_f:
            out_f.write(header)
        run_custom_query(conn, q, out_f)


def main():
    initialize_database()
    conn = sqlite3.connect(DB_FILE)

    # If run with --demo argument, run demo suite and output to file
    if len(sys.argv) > 1 and sys.argv[1] == "--demo":
        with open(OUTPUT_FILE, "w", encoding="utf-8") as out_f:
            out_f.write("="*75 + "\n")
            out_f.write("PUBLIC TRANSPORT MANAGEMENT & PASSENGER TRAVEL ANALYSIS SYSTEM\n")
            out_f.write("DBMS PROGRAM EXECUTION & QUERY RESULTS OUTPUT\n")
            out_f.write("Database: SQLite 3 / Relational DBMS Engine\n")
            out_f.write("="*75 + "\n\n")
            run_demo_suite(conn, out_f)
        print(f"Demo completed. Output saved to: {OUTPUT_FILE}")
        conn.close()
        return

    # Interactive Manual Giving Mode
    print("=" * 75)
    print("  PUBLIC TRANSPORT MANAGEMENT & PASSENGER TRAVEL ANALYSIS SYSTEM")
    print("  DBMS INTERACTIVE MANAGEMENT CONSOLE (MANUAL GIVING)")
    print("=" * 75)
    print("Database connection established: transport_system.db\n")

    while True:
        print("\n--- DBMS MAIN MENU ---")
        print("1. Execute Custom SQL Query (Manual Input)")
        print("2. List All Database Tables")
        print("3. View Records of a Specific Table")
        print("4. View Route-Wise Revenue Report")
        print("5. View Bus Occupancy Report")
        print("6. Insert New Passenger Record (Manual Input)")
        print("7. Run Predefined Analytical Demo Suite")
        print("8. Exit Console")

        choice = input("\nEnter your choice (1-8): ").strip()

        if choice == "1":
            print("\nEnter your SQL query (end with semicolon, or press Enter to cancel):")
            query = input("SQL> ").strip()
            if query:
                with open(OUTPUT_FILE, "a", encoding="utf-8") as out_f:
                    out_f.write(f"\nUser Query: {query}\n")
                    run_custom_query(conn, query, out_f)

        elif choice == "2":
            query = "SELECT name, type FROM sqlite_master WHERE type IN ('table', 'view') AND name NOT LIKE 'sqlite_%';"
            print("\nDatabase Tables and Views:")
            run_custom_query(conn, query)

        elif choice == "3":
            table_name = input("Enter Table Name (e.g., Passenger, Bus, Route, Schedule, Booking, Ticket): ").strip()
            if table_name:
                query = f"SELECT * FROM {table_name} LIMIT 20;"
                print(f"\nRecords from {table_name}:")
                run_custom_query(conn, query)

        elif choice == "4":
            print("\nRoute-Wise Revenue Report:")
            run_custom_query(conn, "SELECT * FROM vw_RouteWiseRevenue;")

        elif choice == "5":
            print("\nBus Occupancy & Remaining Seats Report:")
            run_custom_query(conn, "SELECT * FROM vw_BusOccupancy;")

        elif choice == "6":
            print("\n--- Manual Passenger Registration ---")
            try:
                pid = int(input("Enter Passenger ID (e.g. 115): ").strip())
                name = input("Enter Full Name: ").strip()
                email = input("Enter Email Address: ").strip()
                phone = input("Enter Phone Number: ").strip()
                gender = input("Enter Gender (Male/Female/Other): ").strip()
                address = input("Enter Address/City: ").strip()

                insert_sql = "INSERT INTO Passenger (PassengerID, Name, Email, Phone, Gender, Address) VALUES (?, ?, ?, ?, ?, ?);"
                cursor = conn.cursor()
                cursor.execute(insert_sql, (pid, name, email, phone, gender, address))
                conn.commit()
                print(f"[SUCCESS] Passenger '{name}' with ID {pid} registered successfully!")
            except Exception as e:
                print(f"[ERROR] Failed to insert passenger: {e}")

        elif choice == "7":
            print("\nRunning Predefined Demo Suite...")
            with open(OUTPUT_FILE, "a", encoding="utf-8") as out_f:
                run_demo_suite(conn, out_f)

        elif choice == "8":
            print("Exiting DBMS Console. Goodbye!")
            break
        else:
            print("Invalid choice! Please choose an option from 1 to 8.")

    conn.close()


if __name__ == "__main__":
    main()
