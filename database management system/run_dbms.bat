@echo off
cd /d "%~dp0"
title DBMS - Public Transport Management System
color 0B
echo ============================================================================
echo   PUBLIC TRANSPORT MANAGEMENT & PASSENGER TRAVEL ANALYSIS SYSTEM
echo   Database Management Systems (25ITT31) - Manual Giving Console
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

echo Starting interactive DBMS console (Manual Giving)...
echo Refer to 'sample_inputs.txt' for recommended sample queries and inputs.
echo.
%PYTHON_CMD% dbms_interactive.py

echo.
echo ============================================================================
echo DBMS Console closed.
pause
