@echo off
echo Cleaning old bin...
if exist bin ( rmdir /s /q bin )
mkdir bin

echo Compiling...
javac -d bin -sourcepath src src/app/SavingsTrackerNiAres.java src/model/*.java src/panels/*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Running...
java -cp bin app.SavingsTrackerNiAres
if %errorlevel% neq 0 (
    echo Application crashed!
    pause
    exit /b %errorlevel%
)
pause
