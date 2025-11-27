@echo off
echo ========================================
echo LIMPIANDO CACHE DE KOTLIN Y GRADLE
echo ========================================
echo.

cd /d "%~dp0"

echo [1/5] Eliminando directorio build...
if exist "app\build" (
    rmdir /s /q "app\build"
    echo    - app\build eliminado
)
if exist "build" (
    rmdir /s /q "build"
    echo    - build eliminado
)

echo.
echo [2/5] Eliminando cache de Gradle...
if exist ".gradle" (
    rmdir /s /q ".gradle"
    echo    - .gradle eliminado
)

echo.
echo [3/5] Eliminando archivos compilados de Kotlin...
if exist "app\build\kotlin" (
    rmdir /s /q "app\build\kotlin"
    echo    - app\build\kotlin eliminado
)

echo.
echo [4/5] Ejecutando gradlew clean...
call gradlew clean
echo    - gradlew clean completado

echo.
echo [5/5] Reconstruyendo proyecto...
call gradlew build
echo    - gradlew build completado

echo.
echo ========================================
echo LIMPIEZA COMPLETADA
echo ========================================
echo.
echo Ahora puedes abrir Android Studio y ejecutar la app.
echo.
pause

