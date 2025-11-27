@echo off
echo ========================================
echo LIMPIEZA COMPLETA DEL PROYECTO
echo ========================================
echo.

echo [1/6] Cerrando Android Studio si esta abierto...
taskkill /F /IM studio64.exe 2>nul
timeout /t 2 >nul

echo.
echo [2/6] Eliminando carpetas de build del proyecto...
if exist ".gradle" (
    rmdir /s /q ".gradle"
    echo   - .gradle eliminado
)
if exist "build" (
    rmdir /s /q "build"
    echo   - build eliminado
)
if exist "app\build" (
    rmdir /s /q "app\build"
    echo   - app\build eliminado
)
if exist ".idea" (
    rmdir /s /q ".idea"
    echo   - .idea eliminado
)
if exist ".kotlin" (
    rmdir /s /q ".kotlin"
    echo   - .kotlin eliminado
)

echo.
echo [3/6] Eliminando caches globales de Gradle...
if exist "%USERPROFILE%\.gradle\caches" (
    rmdir /s /q "%USERPROFILE%\.gradle\caches"
    echo   - Caches de Gradle eliminados
)

echo.
echo [4/6] Eliminando caches de Android Studio...
if exist "%USERPROFILE%\.AndroidStudio*" (
    for /d %%i in ("%USERPROFILE%\.AndroidStudio*") do (
        if exist "%%i\system\caches" (
            rmdir /s /q "%%i\system\caches"
            echo   - Cache de Android Studio eliminado
        )
    )
)

echo.
echo [5/6] Ejecutando gradlew clean...
call gradlew.bat clean --no-daemon

echo.
echo [6/6] Refrescando dependencias...
call gradlew.bat build --refresh-dependencies --no-daemon --stacktrace

echo.
echo ========================================
echo LIMPIEZA COMPLETADA
echo ========================================
echo Ahora puedes abrir Android Studio y hacer Sync Project
pause

