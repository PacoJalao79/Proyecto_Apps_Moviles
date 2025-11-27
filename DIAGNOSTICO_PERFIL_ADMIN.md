# 🔍 DIAGNÓSTICO: Problema de Perfil del Administrador

**Fecha:** 27 de noviembre de 2025  
**Estado:** 🔍 EN DIAGNÓSTICO

## 🎯 Problema Reportado

El administrador no puede ver sus datos en el perfil ni editarlos, mientras que el usuario normal sí puede.

---

## 🔧 Cambios Realizados para Diagnóstico

Se agregaron logs detallados en `ProfileFragment.kt` para identificar el problema:

### **Logs agregados en `updateUi()`:**
```kotlin
android.util.Log.d("ProfileFragment", "=== UPDATE UI ===")
android.util.Log.d("ProfileFragment", "¿Está logueado?: ${tokenManager.isLoggedIn()}")
android.util.Log.d("ProfileFragment", "User ID guardado: ${userManager.getUserId()}")
android.util.Log.d("ProfileFragment", "Rol guardado: ${userManager.getUserRole()}")
android.util.Log.d("ProfileFragment", "¿Es admin?: ${userManager.isAdmin()}")
```

### **Logs agregados en `loadUserProfile()`:**
```kotlin
android.util.Log.d("ProfileFragment", "=== LOAD USER PROFILE ===")
android.util.Log.d("ProfileFragment", "Usuario en caché: $cachedUser")
android.util.Log.d("ProfileFragment", "User ID para consulta: $userId")
android.util.Log.d("ProfileFragment", "📡 Llamando a API: GET /user/$userId")
android.util.Log.d("ProfileFragment", "✅ Usuario recibido de API: $updatedUser")
```

---

## 📋 PASOS PARA DIAGNOSTICAR

### **Paso 1: Compilar la app**
1. Ejecuta el archivo: `COMPILAR_PROYECTO_FINAL.bat`
2. O desde Android Studio: **Build → Rebuild Project**

### **Paso 2: Iniciar sesión como Administrador**
1. Abre la app
2. Inicia sesión con una cuenta de administrador (email terminado en `@gmail.com.admin`)
3. Ve a la pestaña **Perfil**

### **Paso 3: Recopilar Logs**

Abre **Logcat** en Android Studio y filtra por: **`ProfileFragment`**

Busca los siguientes logs:

#### **A. Logs de UPDATE UI:**
```
D/ProfileFragment: === UPDATE UI ===
D/ProfileFragment: ¿Está logueado?: true
D/ProfileFragment: User ID guardado: [NÚMERO]
D/ProfileFragment: Rol guardado: [admin/user]
D/ProfileFragment: ¿Es admin?: [true/false]
```

#### **B. Logs de LOAD USER PROFILE:**
```
D/ProfileFragment: === LOAD USER PROFILE ===
D/ProfileFragment: Usuario en caché: [User(...) o null]
D/ProfileFragment: User ID para consulta: [NÚMERO]
D/ProfileFragment: 📡 Llamando a API: GET /user/[ID]
```

#### **C. Logs de Resultado:**
```
D/ProfileFragment: ✅ Usuario recibido de API: User(...)
D/ProfileFragment: ✅ Perfil actualizado exitosamente
```

O si hay error:
```
E/ProfileFragment: ❌ Error al actualizar perfil desde API: [mensaje]
```

---

## 🔍 Posibles Causas del Problema

### **1. Usuario en Caché es NULL**
- **Síntoma:** `Usuario en caché: null`
- **Causa:** Los datos del admin no se guardaron al hacer login
- **Solución:** Verificar que `MainActivity` esté guardando el usuario al hacer login

### **2. User ID es -1**
- **Síntoma:** `User ID guardado: -1` o `User ID para consulta: -1`
- **Causa:** No se guardó el ID del admin en SharedPreferences
- **Solución:** Verificar que el login guarde todos los datos del usuario

### **3. Error de API**
- **Síntoma:** `❌ Error al actualizar perfil desde API`
- **Causa:** La API no puede encontrar al usuario con ese ID
- **Solución:** Verificar que el endpoint `/user/{user_id}` funcione para admins

### **4. Rol Incorrecto**
- **Síntoma:** `Rol guardado: user` cuando debería ser `admin`
- **Causa:** El rol no se detectó correctamente
- **Solución:** Verificar detección de rol por email

---

## 📊 Información a Recopilar

Por favor, copia y pega los siguientes logs:

### **1. Logs al hacer Login (MainActivity):**
```
Busca en Logcat: "MainActivity"
```

### **2. Logs al abrir Perfil (ProfileFragment):**
```
Busca en Logcat: "ProfileFragment"
```

### **3. Logs de UserManager:**
```
Busca en Logcat: "UserManager"
```

---

## 🎯 Qué Esperamos Ver (Funcionamiento Correcto)

### **Para Usuario Normal:**
```
D/ProfileFragment: === UPDATE UI ===
D/ProfileFragment: ¿Está logueado?: true
D/ProfileFragment: User ID guardado: 123
D/ProfileFragment: Rol guardado: user
D/ProfileFragment: ¿Es admin?: false
D/ProfileFragment: === LOAD USER PROFILE ===
D/ProfileFragment: Usuario en caché: User(id=123, name=Juan Pérez, ...)
D/ProfileFragment: ✅ Mostrando usuario en caché
D/ProfileFragment: 📡 Llamando a API: GET /user/123
D/ProfileFragment: ✅ Usuario recibido de API: User(...)
D/ProfileFragment: ✅ Perfil actualizado exitosamente
```

### **Para Administrador (Lo que DEBERÍA verse):**
```
D/ProfileFragment: === UPDATE UI ===
D/ProfileFragment: ¿Está logueado?: true
D/ProfileFragment: User ID guardado: 456
D/ProfileFragment: Rol guardado: admin
D/ProfileFragment: ¿Es admin?: true
D/ProfileFragment: === LOAD USER PROFILE ===
D/ProfileFragment: Usuario en caché: User(id=456, name=Admin García, role=admin, ...)
D/ProfileFragment: ✅ Mostrando usuario en caché
D/ProfileFragment: 📡 Llamando a API: GET /user/456
D/ProfileFragment: ✅ Usuario recibido de API: User(...)
D/ProfileFragment: ✅ Perfil actualizado exitosamente
```

---

## 🚨 Si el Problema Persiste

### **Escenario A: Usuario en caché es NULL**

El problema está en el login. Necesitamos verificar `MainActivity.kt`:

```kotlin
// MainActivity.kt - loginUser()
// Después de authService.login(...)
userManager.saveUser(authResponse.user)  // ¿Se está ejecutando esto?
```

### **Escenario B: User ID es -1**

El problema es que `authResponse.user` no tiene ID o no se guardó:

```kotlin
// Verificar que authResponse.user tenga un ID válido
android.util.Log.d("MainActivity", "User ID recibido: ${authResponse.user.id}")
```

### **Escenario C: API devuelve error para admin**

La API E-commerce puede tener restricciones para usuarios admin:

```kotlin
// Verificar el error exacto en Logcat
E/ProfileFragment: ❌ Error al actualizar perfil desde API: HTTP 404 
```

---

## 📝 Próximos Pasos

Una vez que tengas los logs:

1. **Cópialos y pégalos en el chat**
2. **Analizaremos qué está fallando exactamente**
3. **Implementaremos la solución correcta**

---

## 🔧 Código Actual

El código del `ProfileFragment` **es el mismo para admin y usuario normal**. No hay condiciones que lo bloqueen. Por lo tanto, si el perfil del usuario normal funciona, el del admin también debería funcionar.

La diferencia está en:
- ✅ Los **datos guardados** en SharedPreferences
- ✅ El **proceso de login** que guarda esos datos
- ✅ La **respuesta de la API** al consultar el perfil

---

**¡Compila la app, prueba con el admin y copia los logs para que podamos identificar el problema exacto!** 🔍

