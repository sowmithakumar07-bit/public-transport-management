@echo off
cd /d "%~dp0"
title Foundations of Data Science - Public Transport Analytics
color 0E
echo ============================================================================
echo   PUBLIC TRANSPORT MANAGEMENT & PASSENGER TRAVEL ANALYSIS SYSTEM
echo   Foundations of Data Science (25ITC31) - Manual Giving Console
echo ============================================================================
echo.

set PYTHON_CMD=python
where python >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    if exist "C:\Users\%USERNAME%\AppData\Local\Python\pythoncore-3.15-64\python.exe" (
        set PYTHON_CMD="C:\Users\%USERNAME%\AppData\Local\Python\pythoncore-3.15-64\python.exe"
    ) else if exist "C:\Users\sowmi\AppData\Local\Python\pythoncore-3.15-64\python.exe" (
        set PYTHON_CMD="C:\Users\sowmi\AppData\Local\Python\pythoncore-3.15-64\python.exe"
    )
)

echo Starting interactive Data Science & Visualization console (Manual Giving)...
echo Refer to 'sample_inputs.txt' for recommended sample inputs and analytical queries.
echo.
%PYTHON_CMD% data_science_app.py

echo.
echo ============================================================================
echo Data Science Console closed.
pause
