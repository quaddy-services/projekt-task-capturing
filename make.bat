echo on
echo %date% >src/version.txt
call mvn clean install
if %errorlevel% == 1 pause
:ende