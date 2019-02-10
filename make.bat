echo on
git pull
if %errorlevel% == 1 pause
echo %date% >src/version.txt
git add src/version.txt
if %errorlevel% == 1 pause
git commit -m %date%
if %errorlevel% == 1 pause
git push
if %errorlevel% == 1 pause
call mvn clean install
if %errorlevel% == 1 pause
:ende