@echo off
cd /d "%~dp0"
title Java - Public Transport Management System
color 0A
echo ============================================================================
echo   PUBLIC TRANSPORT MANAGEMENT & PASSENGER TRAVEL SYSTEM
echo   Java Programming (25ITT32) - Manual Giving Console
echo ============================================================================
echo.

set JAVA_CMD=java
if exist "..\jdk\jdk-17.0.21+9\bin\java.exe" (
    set JAVA_CMD="..\jdk\jdk-17.0.21+9\bin\java.exe"
) else (
    where java >nul 2>&1
    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Java runtime not found!
        echo Please ensure Java 17+ is installed and configured on your PATH.
        pause
        exit /b 1
    )
)

echo Starting interactive Java application (Manual Giving)...
echo Refer to 'sample_inputs.txt' for recommended sample inputs and booking tests.
echo.
%JAVA_CMD% -cp bin com.transport.app.TransportManagementApp

echo.
echo ============================================================================
echo Java Application terminated.
pause
