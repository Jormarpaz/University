@echo off

cd /d "%~dp0"
echo Directorio actual: %cd%
pause  // Para verificar

REM === Versión personalizada para pruebas del estudiante ===
REM === Basado en el original pero usando tus propios tests ===

REM === Mensaje bienvenida ===
echo Pruebas PERSONALES de la practica 2024/2025
echo.

REM === Inputs ===
set SRC_DIR=%cd%\src
set BIN_DIR=bin
set MAIN=es/uned/lsi/eped/pract2024_2025/Main
set JAVA_HOME_JDK="C:\Program Files\Java\jdk-23"
set TMP_FOLDER=%cd%\juego_de_pruebas_2025\tmp_personal

REM === Configuración de tus pruebas ===
set CARPETA_TUS_PRUEBAS=%cd%\mis_pruebas
set CARPETA_TUS_SALIDAS=%cd%\mis_resultados

REM Verificar Java
IF %JAVA_HOME_JDK%=="" (
    IF "%JAVA_HOME%" == "" (
        echo ERROR: Configure JAVA_HOME_JDK en el archivo bat
        pause
        exit
    ) ELSE (
        set JAVA_HOME_JDK="%JAVA_HOME%"
    )
)

REM Crear carpetas si no existen
if not exist "%CARPETA_TUS_PRUEBAS%" mkdir "%CARPETA_TUS_PRUEBAS%"
if not exist "%CARPETA_TUS_SALIDAS%" mkdir "%CARPETA_TUS_SALIDAS%"

REM === Limpieza y preparación ===
echo Limpiando compilacion anterior...
rd /q /s "%TMP_FOLDER%" 2>nul
if not exist "%TMP_FOLDER%" mkdir "%TMP_FOLDER%" 
mkdir "%TMP_FOLDER%%BIN_DIR%"
mkdir "%TMP_FOLDER%\src"
mkdir "%TMP_FOLDER%\src\es"
mkdir "%TMP_FOLDER%\src\es\uned"
mkdir "%TMP_FOLDER%\src\es\uned\lsi"
mkdir "%TMP_FOLDER%\src\es\uned\lsi\eped"
mkdir "%TMP_FOLDER%\src\es\uned\lsi\eped\pract2024_2025"
xcopy /s/q "%SRC_DIR%\es\uned\lsi\eped\pract2024_2025" "%TMP_FOLDER%\src\es\uned\lsi\eped\pract2024_2025"
xcopy /s/y/q "juego_de_pruebas_2025\lib\src" "%TMP_FOLDER%\src\es\uned\lsi\eped\pract2024_2025"

echo.
echo.
pause

REM === Compilación ===
echo Compilando en carpeta temporal...
%JAVA_HOME_JDK%"\bin\javac" -d "%TMP_FOLDER%%BIN_DIR%" -sourcepath "%TMP_FOLDER%\src" -cp "juego_de_pruebas_2025/lib/TAD_modified.jar" "%TMP_FOLDER%\src\%MAIN%.java"
if errorlevel 1 (
    echo Compilacion con errores
    pause
    exit /B 1
)

echo Compilacion sin errores
echo.
echo.
pause

REM === Menú de pruebas ===
:menu
cls
echo *******************************************************
echo *   PRUEBAS PERSONALES - PLANIFICADOR DE TAREAS       *
echo *******************************************************
echo.
echo 1. Ejecutar prueba con implementacion SEQUENCE
echo 2. Ejecutar prueba con implementacion TREE
echo 3. Comparar resultados SEQUENCE vs TREE
echo 4. Salir
echo.
set /p opcion="Seleccione una opcion (1-4): "

if "%opcion%"=="1" goto prueba_sequence
if "%opcion%"=="2" goto prueba_tree
if "%opcion%"=="3" goto comparar
if "%opcion%"=="4" exit

echo Opcion no valida. Intente nuevamente.
pause
goto menu

:prueba_sequence
echo.
set /p nombre_prueba="Nombre del archivo de prueba (sin .tsv): "
echo Ejecutando prueba %nombre_prueba% con SEQUENCE...

%JAVA_HOME_JDK%"\bin\java" -cp "%TMP_FOLDER%%BIN_DIR%;juego_de_pruebas_2025/lib/TAD_modified.jar" "%MAIN%" SEQUENCE "%CARPETA_TUS_PRUEBAS%\%nombre_prueba%.tsv" "%CARPETA_TUS_SALIDAS%\Salida_SEQ_%nombre_prueba%.tsv"

if errorlevel 1 (
    echo ERROR: Ejecucion fallida
) else (
    echo Prueba completada. Resultados en: %CARPETA_TUS_SALIDAS%\Salida_SEQ_%nombre_prueba%.tsv
)
pause
goto menu

:prueba_tree
echo.
set /p nombre_prueba="Nombre del archivo de prueba (sin .tsv): "
echo Ejecutando prueba %nombre_prueba% con TREE...

%JAVA_HOME_JDK%"\bin\java" -cp "%TMP_FOLDER%%BIN_DIR%;juego_de_pruebas_2025/lib/TAD_modified.jar" "%MAIN%" TREE "%CARPETA_TUS_PRUEBAS%\%nombre_prueba%.tsv" "%CARPETA_TUS_SALIDAS%\Salida_TREE_%nombre_prueba%.tsv"

if errorlevel 1 (
    echo ERROR: Ejecucion fallida
) else (
    echo Prueba completada. Resultados en: %CARPETA_TUS_SALIDAS%\Salida_TREE_%nombre_prueba%.tsv
)
pause
goto menu

:comparar
echo.
set /p nombre_prueba="Nombre del archivo de prueba (sin .tsv): "
echo Comparando resultados para %nombre_prueba%...
%JAVA_HOME_JDK%"\bin\java" -jar "juego_de_pruebas_2025/lib/Comparator.jar" "%CARPETA_TUS_SALIDAS%\Salida_SEQ_%nombre_prueba%.tsv" "%CARPETA_TUS_SALIDAS%\Salida_TREE_%nombre_prueba%.tsv" "%CARPETA_TUS_SALIDAS%\Diferencias_%nombre_prueba%.txt"
if errorlevel 1 (
    echo ERROR: Comparacion fallida
) else (
    echo Comparacion completada. Diferencias en: %CARPETA_TUS_SALIDAS%\Diferencias_%nombre_prueba%.txt
)
pause
goto menu