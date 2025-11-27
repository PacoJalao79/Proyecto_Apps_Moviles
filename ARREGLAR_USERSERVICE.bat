@echo off
echo ================================================
echo    SOLUCION PARA ERROR USERSERVICE
echo ================================================
echo.
echo Este script:
echo 1. Elimina el archivo UserService.kt antiguo
echo 2. Renombra UserServiceNew.kt a UserService.kt
echo 3. Limpia el cache de Kotlin
echo 4. Ejecuta Gradle clean y build
echo.
pause

echo.
echo [1/5] Eliminando UserService.kt antiguo...
del /F /Q "app\src\main\java\com\example\galletas\api\UserService.kt" 2>nul
echo Eliminado.

echo.
echo [2/5] Renombrando UserServiceNew.kt a UserService.kt...
ren "app\src\main\java\com\example\galletas\api\UserServiceNew.kt" "UserService.kt"
echo Renombrado.

echo.
echo [3/5] Limpiando cache de Kotlin...
rmdir /S /Q ".kotlin" 2>nul
rmdir /S /Q "build" 2>nul
rmdir /S /Q "app\build" 2>nul
echo Cache limpiado.

echo.
echo [4/5] Ejecutando Gradle clean...
call gradlew.bat clean
echo Clean completado.

echo.
echo [5/5] Ejecutando Gradle build...
call gradlew.bat build
echo Build completado.

echo.
echo ================================================
echo    PROCESO COMPLETADO
echo ================================================
echo.
echo Ahora abre Android Studio y:
echo 1. File ^> Invalidate Caches / Restart
echo 2. Espera a que indexe
echo 3. Build ^> Rebuild Project
echo.
pause

