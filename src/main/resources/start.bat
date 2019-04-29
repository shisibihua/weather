@echo off
rem Add extra JVM options here

title WeatherService8020

cd /d %~dp0
set OPTS=-Xms128m -Xmx256m
"jre8_64\bin\java" -jar %OPTS% weather-service-2.0.jar