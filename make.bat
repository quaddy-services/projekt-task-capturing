echo on

git pull
if %errorlevel% == 1 pause

set t=%temp%\build-dir-ptc

set actualDir=%CD%

rmdir %t% /s /q
mkdir %t%\src
mkdir %t%\target

if x%java_home%x == xx set java_home=c:\programs\currentJDK
%java_home%\bin\java -version 2>src\main\resources\make-info.txt
if %errorlevel% == 1 goto noJDK

type src\main\resources\make-info.txt

echo %date% >src\main\resources\version.txt
git add src/main/resources/version.txt
git add src/main/resources/make-info.txt
git commit -m %date%
git push
if %errorlevel% == 1 pause

xcopy src\*.* %t%\src /s
copy pom.xml %t% /y

cd /D %t%

set CURRENT_DATE=%date:~6,4%.%date:~3,2%.%date:~0,2%

call mvn -U clean install source:jar -Drevision=%CURRENT_DATE%
if %errorlevel% == 1 goto ende
echo on

del %actualDir%\target\*.jar
copy target\*.jar %actualDir%\target\ /y
copy target\*.jar c:\temp\ /y

rmdir %t% /s /q

goto ende

:noJDK
echo off
echo ERROR:
echo there is no link to the newest JDK in c:\programs\currentJDK
echo you can create it via c:\Programs\SysinternalsSuite\junction.exe c:\programs\currentjdk "%currentjdk%"
echo see https://technet.microsoft.com/de-de/sysinternals
goto ende

:ende

cd /D %actualDir%

