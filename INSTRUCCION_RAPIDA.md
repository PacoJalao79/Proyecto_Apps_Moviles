# ⚡ INSTRUCCIÓN RÁPIDA - ARREGLAR ERROR USERSERVICE

## 🎯 LO QUE TIENES QUE HACER AHORA MISMO:

### 1️⃣ CIERRA ANDROID STUDIO
❌ Cierra todas las ventanas de Android Studio

### 2️⃣ EJECUTA EL SCRIPT
📁 Ve a la carpeta del proyecto:
```
C:\Users\pc\Desktop\Proyecto-app-moviles-main
```

🖱️ **DOBLE CLIC** en el archivo:
```
ARREGLAR_USERSERVICE.bat
```

⏳ Espera 5-10 minutos a que termine

### 3️⃣ ABRE ANDROID STUDIO
✅ Abre Android Studio
✅ Abre el proyecto
✅ Espera a que termine de indexar (barra de progreso abajo)

### 4️⃣ INVALIDA CACHÉ
✅ **File** → **Invalidate Caches / Restart...**
✅ Selecciona: **"Invalidate and Restart"**
✅ Espera a que se reinicie y termine de indexar

### 5️⃣ REBUILD
✅ **Build** → **Rebuild Project**
✅ Espera a que termine

---

## ✅ VERIFICACIÓN

Después de hacer todo esto, intenta compilar:
- **Build** → **Make Project** (o presiona **Ctrl + F9**)

Si ves **"Build successful"** abajo, ¡LISTO! ✅

Si aún hay errores, avísame con una captura de pantalla.

---

## 🤔 ¿QUÉ HACE EL SCRIPT?

El script `ARREGLAR_USERSERVICE.bat`:
1. Elimina el archivo `UserService.kt` corrupto
2. Renombra `UserServiceNew.kt` → `UserService.kt`
3. Limpia el caché de Kotlin
4. Ejecuta `gradlew clean`
5. Ejecuta `gradlew build`

Todo automáticamente. ✨

---

## 📞 ¿NECESITAS AYUDA?

Si el script no funciona o tienes errores, dime:
- ¿Qué mensaje de error apareció?
- ¿En qué paso te quedaste?
- Captura de pantalla del error

---

**Creado**: 2025-11-26
**Para**: Arreglar error "Unresolved reference 'UserService'"

