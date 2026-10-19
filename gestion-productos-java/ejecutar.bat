@echo off
cd /d "%~dp0"
if not exist out mkdir out
javac -encoding UTF-8 -d out src\Producto.java src\Inventario.java src\Main.java test\Pruebas.java
if errorlevel 1 (
    echo No se pudo compilar. Verifica que el JDK este instalado y en PATH.
    pause
    exit /b 1
)
java -cp out Main --interactivo
pause
