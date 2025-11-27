@echo off
echo ================================================
echo   COMPILAR PROYECTO - USERSERVICE CORREGIDO
echo ================================================
echo.
echo Este script:
echo 1. Elimina archivo temporal UserServiceTemp.kt
echo 2. Limpia cache de Kotlin
echo 3. Compila el proyecto
echo.
pause

echo.
echo [1/4] Eliminando archivo temporal...
del /F /Q "app\src\main\java\com\example\galletas\api\UserServiceTemp.kt" 2>nul
echo Eliminado.

echo.
echo [2/4] Limpiando cache de Kotlin...
rmdir /S /Q ".kotlin" 2>nul
echo Cache limpiado.

echo.
echo [3/4] Gradle Clean...
call gradlew.bat clean
if %errorlevel% neq 0 (
    echo ERROR en gradle clean!
    pause
    exit /b 1
)
echo Clean completado.

echo.
echo [4/4] Gradle Build...
call gradlew.bat build
if %errorlevel% neq 0 (
    echo.
    echo ================================================
    echo   ERROR EN LA COMPILACION
    echo ================================================
    echo.
    echo Por favor:
    echo 1. Abre Android Studio
    echo 2. File ^> Invalidate Caches / Restart
    echo 3. Build ^> Rebuild Project
    echo.
    pause
    exit /b 1
)

echo.
echo ================================================
echo   COMPILACION EXITOSA!
echo ================================================
echo.
echo El proyecto se compilo correctamente.
echo UserService.kt esta funcionando.
echo.
echo Ahora puedes:
echo 1. Abrir Android Studio
echo 2. Ejecutar la app (Run ^> Run 'app')
echo.
pause

