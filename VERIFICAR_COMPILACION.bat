@echo off
echo ================================================
echo   VERIFICAR Y COMPILAR PROYECTO
echo ================================================
echo.
echo Este script limpia y compila el proyecto
echo para verificar que UserService funcione correctamente.
echo.
pause

echo.
echo [1/4] Limpiando cache de Kotlin...
rmdir /S /Q ".kotlin" 2>nul
rmdir /S /Q "build\kotlin" 2>nul
rmdir /S /Q "app\build\kotlin" 2>nul
echo Cache de Kotlin limpiado.

echo.
echo [2/4] Ejecutando Gradle clean...
call gradlew.bat clean
if %errorlevel% neq 0 (
    echo ERROR en gradle clean!
    pause
    exit /b 1
)
echo Clean completado.

echo.
echo [3/4] Sincronizando dependencias...
call gradlew.bat --refresh-dependencies
echo Dependencias sincronizadas.

echo.
echo [4/4] Compilando proyecto (Build)...
call gradlew.bat build
if %errorlevel% neq 0 (
    echo.
    echo ================================================
    echo   ERROR EN LA COMPILACION
    echo ================================================
    echo.
    echo Revisa los errores arriba.
    echo Si ves errores relacionados con UserService,
    echo abre Android Studio y:
    echo   1. File ^> Invalidate Caches / Restart
    echo   2. Build ^> Rebuild Project
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
echo.
echo Ahora puedes:
echo   1. Abrir Android Studio
echo   2. File ^> Sync Project with Gradle Files
echo   3. Build ^> Make Project (Ctrl+F9)
echo.
echo Si todo sale bien, podras ejecutar la app!
echo.
pause

