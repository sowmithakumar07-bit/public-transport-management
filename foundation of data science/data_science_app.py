"""
Public Transport Management and Passenger Travel Analysis System
Foundation of Data Science (25ITC31) - Analytics & Visualization Engine
Implements DataProcessor, AnalysisEngine, Visualization, and Report Classes.
"""

import os
import sys
import sqlite3
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from datetime import datetime

# Set styles for academic presentation
sns.set_theme(style="whitegrid", palette="deep")
plt.rcParams['font.sans-serif'] = 'Arial'
plt.rcParams['axes.edgecolor'] = '#cccccc'
plt.rcParams['axes.linewidth'] = 0.8

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
CSV_FILE = os.path.join(BASE_DIR, "transport_booking_dataset.csv")
DB_FILE = os.path.join(BASE_DIR, "..", "database management system", "transport_system.db")
VIZ_DIR = os.path.join(BASE_DIR, "visualizations")
OUTPUT_FILE = os.path.join(BASE_DIR, "fds_output.txt")

os.makedirs(VIZ_DIR, exist_ok=True)


class DataProcessor:
    """
    Handles data ingestion, data cleaning, type conversion, and transformation.
    Aligned with Syllabus Units 2, 3, and 4.
    """
    def __init__(self, data_path: str):
        self.dataPath = data_path
        self.raw_data = None
        self.cleanedData = None
        self.cleaning_log = []

    def fetchData(self) -> pd.DataFrame:
        """Fetches data from CSV or SQLite database fallback."""
        if os.path.exists(self.dataPath):
            self.raw_data = pd.read_csv(self.dataPath)
            self.cleaning_log.append(f"Successfully loaded {len(self.raw_data)} records from CSV: {os.path.basename(self.dataPath)}")
        elif os.path.exists(DB_FILE):
            conn = sqlite3.connect(DB_FILE)
            query = '''
                SELECT b.BookingID, p.PassengerID, p.Name AS PassengerName, p.Gender,
                       r.Source, r.Destination, r.Distance AS Distance_KM, bs.BusType,
                       s.DepartureTime, b.BookingDate, b.TravelDate, b.SeatNo,
                       b.TotalFare AS Fare, b.Status
                FROM Booking b
                JOIN Passenger p ON b.PassengerID = p.PassengerID
                JOIN Schedule s ON b.ScheduleID = s.ScheduleID
                JOIN Route r ON s.RouteID = r.RouteID
                JOIN Bus bs ON s.BusID = bs.BusID
            '''
            self.raw_data = pd.read_sql_query(query, conn)
            conn.close()
            self.cleaning_log.append(f"Retrieved {len(self.raw_data)} records directly from SQLite database.")
        else:
            raise FileNotFoundError("Neither dataset CSV nor SQLite database could be located!")
        return self.raw_data

    def preprocessData(self) -> pd.DataFrame:
        """
        Cleans data: handles missing values, detects and removes duplicates,
        and converts data types (Unit 4).
        """
        df = self.raw_data.copy()
        initial_count = len(df)

        # 1. Duplicate Detection & Removal
        duplicates_count = df.duplicated().sum()
        df = df.drop_duplicates()
        self.cleaning_log.append(f"Duplicate Detection: Found and removed {duplicates_count} duplicate rows.")

        # 2. Missing Value Imputation
        if 'Age' in df.columns:
            missing_age = df['Age'].isnull().sum()
            df['Age'] = pd.to_numeric(df['Age'], errors='coerce')
            median_age = df['Age'].median()
            df['Age'] = df['Age'].fillna(median_age)
            self.cleaning_log.append(f"Missing Values: Imputed {missing_age} missing Age entries with median age ({median_age:.1f}).")

        if 'Rating' in df.columns:
            df['Rating'] = pd.to_numeric(df['Rating'], errors='coerce')
            mean_rating = df['Rating'].mean()
            df['Rating'] = df['Rating'].fillna(round(mean_rating, 1))

        # 3. Data Type Conversions
        df['BookingDate'] = pd.to_datetime(df['BookingDate'])
        df['TravelDate'] = pd.to_datetime(df['TravelDate'])
        df['Fare'] = pd.to_numeric(df['Fare'], errors='coerce')
        df['Distance_KM'] = pd.to_numeric(df['Distance_KM'], errors='coerce')

        # 4. Outlier Detection & Treatment via Z-Score & IQR (Unit 1 & 4)
        fare_mean = df['Fare'].mean()
        fare_std = df['Fare'].std()
        df['Fare_ZScore'] = (df['Fare'] - fare_mean) / fare_std

        # Cap extreme fare outliers at 3 standard deviations
        outliers = df[np.abs(df['Fare_ZScore']) > 3.0]
        self.cleaning_log.append(f"Outlier Treatment: Identified {len(outliers)} statistical fare outliers (|z| > 3.0). Capping to threshold.")
        upper_limit = fare_mean + 3.0 * fare_std
        lower_limit = max(100.0, fare_mean - 3.0 * fare_std)
        df['Fare'] = np.clip(df['Fare'], lower_limit, upper_limit)

        self.cleanedData = df
        self.cleaning_log.append(f"Preprocessing Complete: Cleaned dataset contains {len(df)} valid records.")
        return self.cleanedData

    def transformData(self) -> pd.DataFrame:
        """
        Feature Engineering & Transformation (Unit 4).
        Creates LeadDays, Route label, and Fare categories.
        """
        df = self.cleanedData.copy()
        df['LeadDays'] = (df['TravelDate'] - df['BookingDate']).dt.days
        df['Route'] = df['Source'] + " -> " + df['Destination']
        df['TravelDay'] = df['TravelDate'].dt.day_name()

        # Fare categorization (Binning)
        df['FareCategory'] = pd.cut(
            df['Fare'],
            bins=[0, 700, 1100, 2500, 10000],
            labels=['Budget (<₹700)', 'Standard (₹700-1100)', 'Premium (₹1100-2500)', 'Luxury (>₹2500)']
        )

        self.cleanedData = df
        self.cleaning_log.append("Data Transformation: Engineered 'LeadDays', 'Route', 'TravelDay', and 'FareCategory'.")
        return self.cleanedData


class AnalysisEngine:
    """
    Performs Exploratory Data Analysis, Descriptive Statistics, and Pattern Extraction.
    Aligned with Syllabus Units 1, 3, and 5.
    """
    def __init__(self, data: pd.DataFrame):
        self.df = data
        self.patterns = []
        self.insights = ""

    def calculateDescriptiveStatistics(self) -> dict:
        """Calculates central tendency, variability, and quantiles (Unit 1)."""
        metrics = ['Fare', 'Distance_KM', 'LeadDays']
        stats = {}
        for col in metrics:
            if col in self.df.columns:
                series = self.df[col].dropna()
                stats[col] = {
                    'Mean': series.mean(),
                    'Median': series.median(),
                    'Mode': series.mode()[0] if not series.mode().empty else None,
                    'Variance': series.var(),
                    'Std_Dev': series.std(),
                    'Min': series.min(),
                    'Q1 (25%)': series.quantile(0.25),
                    'Q2 (50%)': series.quantile(0.50),
                    'Q3 (75%)': series.quantile(0.75),
                    'Max': series.max(),
                    'IQR': series.quantile(0.75) - series.quantile(0.25)
                }
        return stats

    def analyzeTravelPatterns(self) -> list:
        """Identifies key trends, popular routes, and peak travel patterns."""
        self.patterns.clear()

        # Route Popularity & Revenue
        route_perf = self.df.groupby('Route').agg(
            Bookings=('BookingID', 'count'),
            TotalRevenue=('Fare', 'sum'),
            AvgFare=('Fare', 'mean')
        ).sort_values(by='TotalRevenue', ascending=False)

        top_route = route_perf.index[0]
        top_rev = route_perf.iloc[0]['TotalRevenue']
        self.patterns.append(f"Highest Revenue Corridor: '{top_route}' generated ₹{top_rev:,.2f} across {route_perf.iloc[0]['Bookings']} bookings.")

        # Bus Type Distribution
        bustype_counts = self.df['BusType'].value_counts()
        self.patterns.append(f"Dominant Fleet Preference: '{bustype_counts.index[0]}' represents {bustype_counts.iloc[0]} ({(bustype_counts.iloc[0]/len(self.df))*100:.1f}%) of passenger bookings.")

        # Booking Lead Time
        avg_lead = self.df['LeadDays'].mean()
        self.patterns.append(f"Advance Booking Window: Passengers book their tickets an average of {avg_lead:.1f} days in advance.")

        # Cancellation Rate
        cancels = (self.df['Status'] == 'Cancelled').sum()
        cancel_rate = (cancels / len(self.df)) * 100
        self.patterns.append(f"Ticket Cancellation Ratio: {cancel_rate:.2f}% of bookings were cancelled before departure.")

        return self.patterns

    def generateReports(self) -> str:
        """Compiles a complete textual analytical report."""
        stats = self.calculateDescriptiveStatistics()
        lines = []
        lines.append("============================================================================")
        lines.append("  EXPLORATORY DATA ANALYSIS & PASSENGER TRAVEL PATTERNS REPORT")
        lines.append("============================================================================\n")

        lines.append("1. MEASURES OF CENTRAL TENDENCY & VARIABILITY:")
        for col, s in stats.items():
            lines.append(f"\n   Metric: {col}")
            lines.append(f"   - Mean      : {s['Mean']:10.2f} | Median : {s['Median']:10.2f}")
            lines.append(f"   - Std Dev   : {s['Std_Dev']:10.2f} | Variance: {s['Variance']:10.2f}")
            lines.append(f"   - Min - Max : {s['Min']:10.2f} to {s['Max']:10.2f} (IQR: {s['IQR']:.2f})")
            lines.append(f"   - Quantiles : 25% = {s['Q1 (25%)']:.2f}, 50% = {s['Q2 (50%)']:.2f}, 75% = {s['Q3 (75%)']:.2f}")

        lines.append("\n2. ROUTE-WISE REVENUE AND OCCUPANCY SUMMARY:")
        route_summary = self.df.groupby('Route').agg(
            Bookings=('BookingID', 'count'),
            Revenue=('Fare', 'sum'),
            AvgFare=('Fare', 'mean')
        ).sort_values(by='Revenue', ascending=False)
        lines.append(route_summary.to_string())

        lines.append("\n3. KEY TRAVEL INSIGHTS & PATTERNS:")
        for idx, p in enumerate(self.analyzeTravelPatterns(), 1):
            lines.append(f"   [{idx}] {p}")

        self.insights = "\n".join(lines)
        return self.insights


class Visualization:
    """
    Generates rich, publication-ready charts using Matplotlib and Seaborn.
    Aligned with Syllabus Unit 5.
    """
    def __init__(self, data: pd.DataFrame, output_dir: str):
        self.df = data
        self.outputDir = output_dir

    def generateAllVisualizations(self) -> list:
        """Generates all 7 academic visualization deliverables."""
        generated_files = []

        # 1. Line Chart: Daily Passenger Bookings Trend (Matplotlib)
        plt.figure(figsize=(10, 5))
        daily_bookings = self.df.groupby('BookingDate').size()
        plt.plot(daily_bookings.index, daily_bookings.values, marker='o', color='#1f77b4', linewidth=2.5, markersize=6)
        plt.title('Daily Passenger Booking Trend (Time-Series)', fontsize=14, fontweight='bold', pad=12)
        plt.xlabel('Booking Date', fontsize=11)
        plt.ylabel('Number of Bookings', fontsize=11)
        plt.xticks(rotation=45)
        plt.tight_layout()
        f1 = os.path.join(self.outputDir, 'time_series_bookings.png')
        plt.savefig(f1, dpi=300)
        plt.close()
        generated_files.append(f1)

        # 2. Bar Chart: Route-wise Total Revenue (Matplotlib)
        plt.figure(figsize=(11, 5.5))
        route_rev = self.df.groupby('Route')['Fare'].sum().sort_values(ascending=False)
        colors = plt.cm.viridis(np.linspace(0.2, 0.85, len(route_rev)))
        bars = plt.bar(route_rev.index, route_rev.values, color=colors, edgecolor='black', width=0.6)
        plt.title('Route-Wise Total Revenue Generation (₹)', fontsize=14, fontweight='bold', pad=12)
        plt.xlabel('Bus Route', fontsize=11)
        plt.ylabel('Total Revenue (₹)', fontsize=11)
        plt.xticks(rotation=30, ha='right')
        for bar in bars:
            height = bar.get_height()
            plt.text(bar.get_x() + bar.get_width() / 2., height + 1000, f'₹{int(height):,}', ha='center', va='bottom', fontsize=9)
        plt.tight_layout()
        f2 = os.path.join(self.outputDir, 'route_revenue_bar.png')
        plt.savefig(f2, dpi=300)
        plt.close()
        generated_files.append(f2)

        # 3. Pie Chart: Passenger Bookings by Bus Type (Matplotlib)
        plt.figure(figsize=(7, 7))
        bus_dist = self.df['BusType'].value_counts()
        explode = [0.05 if i == 0 else 0 for i in range(len(bus_dist))]
        palette = ['#4e79a7', '#f28e2b', '#e15759', '#76b7b2']
        plt.pie(bus_dist.values, labels=bus_dist.index, autopct='%1.1f%%', startangle=140,
                explode=explode, colors=palette, shadow=True, textprops={'fontsize': 11})
        plt.title('Passenger Booking Distribution by Bus Type', fontsize=14, fontweight='bold', pad=12)
        plt.tight_layout()
        f3 = os.path.join(self.outputDir, 'bustype_pie_chart.png')
        plt.savefig(f3, dpi=300)
        plt.close()
        generated_files.append(f3)

        # 4. Histogram & KDE: Ticket Fare Distribution (Matplotlib & Seaborn)
        plt.figure(figsize=(9, 5))
        sns.histplot(self.df['Fare'], kde=True, color='#2b5c8f', bins=20, edgecolor='white')
        plt.title('Distribution of Passenger Ticket Fares (Histogram & KDE)', fontsize=14, fontweight='bold', pad=12)
        plt.xlabel('Ticket Fare (₹)', fontsize=11)
        plt.ylabel('Passenger Frequency', fontsize=11)
        plt.tight_layout()
        f4 = os.path.join(self.outputDir, 'fare_distribution_hist.png')
        plt.savefig(f4, dpi=300)
        plt.close()
        generated_files.append(f4)

        # 5. Scatter Plot: Distance vs. Ticket Fare (Matplotlib)
        plt.figure(figsize=(9, 5.5))
        sns.scatterplot(data=self.df, x='Distance_KM', y='Fare', hue='BusType', style='BusType', s=70, alpha=0.85)
        plt.title('Relationship Between Travel Distance and Ticket Fare', fontsize=14, fontweight='bold', pad=12)
        plt.xlabel('Route Distance (Kilometers)', fontsize=11)
        plt.ylabel('Ticket Fare (₹)', fontsize=11)
        plt.legend(title='Bus Category', loc='upper left')
        plt.tight_layout()
        f5 = os.path.join(self.outputDir, 'distance_vs_fare_scatter.png')
        plt.savefig(f5, dpi=300)
        plt.close()
        generated_files.append(f5)

        # 6. Seaborn Box Plot: Fare Variance by Route (Outlier Detection)
        plt.figure(figsize=(11, 6))
        sns.boxplot(data=self.df, x='Route', y='Fare', palette='Set2')
        plt.title('Fare Distribution and Outlier Spread Across Routes (Box Plot)', fontsize=14, fontweight='bold', pad=12)
        plt.xlabel('Transit Route', fontsize=11)
        plt.ylabel('Fare (₹)', fontsize=11)
        plt.xticks(rotation=30, ha='right')
        plt.tight_layout()
        f6 = os.path.join(self.outputDir, 'route_fare_boxplot.png')
        plt.savefig(f6, dpi=300)
        plt.close()
        generated_files.append(f6)

        # 7. Seaborn Heatmap: Correlation Matrix of Quantitative Metrics
        plt.figure(figsize=(8, 6))
        num_cols = ['Distance_KM', 'Fare', 'LeadDays']
        if 'Age' in self.df.columns:
            num_cols.append('Age')
        if 'Rating' in self.df.columns:
            num_cols.append('Rating')
        corr = self.df[num_cols].corr()
        sns.heatmap(corr, annot=True, cmap='coolwarm', fmt=".2f", linewidths=1.0, square=True)
        plt.title('Correlation Matrix of Transit Variables (Heatmap)', fontsize=14, fontweight='bold', pad=12)
        plt.tight_layout()
        f7 = os.path.join(self.outputDir, 'travel_correlation_heatmap.png')
        plt.savefig(f7, dpi=300)
        plt.close()
        generated_files.append(f7)

        return generated_files


class Report:
    """Manages report compilation and persistence."""
    def __init__(self, report_id: int, report_type: str):
        self.reportId = report_id
        self.type = report_type
        self.generatedOn = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    def exportReport(self, content: str, file_path: str):
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(f"Report ID: {self.reportId} | Type: {self.type} | Date: {self.generatedOn}\n")
            f.write(content)


def run_pipeline(demo_mode=False):
    """Executes the data processing, analysis, and visualization pipeline."""
    processor = DataProcessor(CSV_FILE)
    processor.fetchData()
    processor.preprocessData()
    cleaned_df = processor.transformData()

    engine = AnalysisEngine(cleaned_df)
    report_text = engine.generateReports()

    viz = Visualization(cleaned_df, VIZ_DIR)
    chart_files = viz.generateAllVisualizations()

    rep = Report(101, "Comprehensive Travel Analytics & EDA")
    rep.exportReport(report_text, OUTPUT_FILE)

    if demo_mode:
        print(report_text)
        print("\nGenerated Visualizations:")
        for cf in chart_files:
            print(f"   -> {os.path.basename(cf)}")
        print(f"\nExecution summary logged to: {OUTPUT_FILE}")

    return processor, engine, viz, cleaned_df


def main():
    if len(sys.argv) > 1 and sys.argv[1] == "--demo":
        run_pipeline(demo_mode=True)
        return

    # Interactive Manual Giving Mode
    print("=" * 75)
    print("   PUBLIC TRANSPORT MANAGEMENT & PASSENGER TRAVEL ANALYSIS SYSTEM")
    print("   Foundations of Data Science (25ITC31) - Manual Giving Console")
    print("=" * 75)

    processor = DataProcessor(CSV_FILE)
    processor.fetchData()
    processor.preprocessData()
    df = processor.transformData()
    engine = AnalysisEngine(df)
    viz = Visualization(df, VIZ_DIR)

    while True:
        print("\n--- DATA SCIENCE CONSOLE MENU ---")
        print("1. Run Full EDA Pipeline & Save All 7 Visualizations")
        print("2. Manual Route Query & Specific Route Analytics (Manual Input)")
        print("3. Manual Outlier & Z-Score Analysis (Manual Input)")
        print("4. Inspect Data Cleaning & Preprocessing Log")
        print("5. View Descriptive Statistics (Mean, Median, Std Dev, IQR)")
        print("6. View Cross-Tabulation Pivot Table (Bus Type vs. Route)")
        print("7. Exit Console")

        choice = input("\nEnter choice (1-7): ").strip()

        if choice == "1":
            print("\nExecuting Data Science Pipeline and Generating Visualizations...")
            run_pipeline(demo_mode=True)

        elif choice == "2":
            routes = df['Route'].unique().tolist()
            print("\nAvailable Transit Routes:")
            for idx, r in enumerate(routes, 1):
                print(f"   {idx}. {r}")
            r_input = input("\nEnter Route Name (or copy from above, e.g. 'Bangalore -> Chennai'): ").strip()
            route_df = df[df['Route'].str.lower() == r_input.lower()]
            if route_df.empty:
                print(f"[ERROR] No records found for route '{r_input}'.")
            else:
                print(f"\n--- Analytics for Route: {r_input} ---")
                print(f"Total Bookings    : {len(route_df)}")
                print(f"Total Revenue     : ₹{route_df['Fare'].sum():,.2f}")
                print(f"Average Fare      : ₹{route_df['Fare'].mean():,.2f}")
                print(f"Bus Types Serving : {', '.join(route_df['BusType'].unique())}")
                print(f"Average Lead Time : {route_df['LeadDays'].mean():.1f} days")

        elif choice == "3":
            try:
                z_thresh = float(input("\nEnter Z-Score Threshold (e.g. 2.0 or 2.5): ").strip())
                outliers = df[np.abs(df['Fare_ZScore']) >= z_thresh]
                print(f"\nFound {len(outliers)} bookings exceeding Z-Score threshold of {z_thresh}:")
                if not outliers.empty:
                    print(outliers[['BookingID', 'PassengerName', 'Route', 'BusType', 'Fare', 'Fare_ZScore']].head(10).to_string())
                else:
                    print("No records outside this threshold.")
            except ValueError:
                print("[ERROR] Please enter a valid decimal number.")

        elif choice == "4":
            print("\n--- Data Cleaning & Quality Log ---")
            for log_entry in processor.cleaning_log:
                print(f"   * {log_entry}")

        elif choice == "5":
            print("\n--- Descriptive Statistics (Units 1 & 5) ---")
            stats = engine.calculateDescriptiveStatistics()
            for col, s in stats.items():
                print(f"\nMetric: {col}")
                for k, v in s.items():
                    print(f"   {k:12} : {v:.2f}")

        elif choice == "6":
            print("\n--- Pivot Table: Average Fare by Route & Bus Type ---")
            pivot = df.pivot_table(index='Route', columns='BusType', values='Fare', aggfunc='mean')
            print(pivot.round(2).to_string())

        elif choice == "7":
            print("Exiting Data Science Console. Goodbye!")
            break
        else:
            print("Invalid choice! Please choose 1 through 7.")


if __name__ == "__main__":
    main()
