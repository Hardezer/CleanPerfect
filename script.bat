@echo off
setlocal

set IMAGE_NAME=app1
set CONTAINER_NAME=cleanPerfect
set BROWSER=brave.exe
set URL=http://localhost:8091/

rem Check if script is running in PowerShell hidden mode
if "%1"=="h" (
    goto :hidden
)

rem Relaunch script in PowerShell hidden mode
powershell -WindowStyle Hidden -Command "Start-Process -FilePath '%~f0' -ArgumentList 'h' -NoNewWindow"
goto :eof

:hidden
docker-compose pull
docker-compose up -d
timeout /t 5 /nobreak > nul
powershell -Command "Start-Process %BROWSER% '%URL%' -Wait"
:WAIT
tasklist /FI "IMAGENAME eq brave.exe" 2>NUL | find /I /N "brave.exe">NUL
if "%ERRORLEVEL%"=="0" (
    timeout /t 1 /nobreak >nul
    goto WAIT
)
docker-compose stop
exit