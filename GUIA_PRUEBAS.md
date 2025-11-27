# 🧪 GUÍA DE PRUEBAS - SISTEMA DE ROLES

## 📱 PRUEBAS PASO A PASO

### **PRUEBA 1: Verificar que el proyecto compila**

1. Abre el proyecto en Android Studio
2. Ve a: `Build` → `Clean Project`
3. Luego: `Build` → `Rebuild Project`
4. Si hay errores de caché con `userManager`, reinicia el IDE

**Resultado esperado**: El proyecto debe compilar sin errores.

---

### **PRUEBA 2: Registro de un usuario normal**

1. Ejecuta la app
2. Toca en "Crear Cuenta"
3. Registra un usuario con un email normal:
   - Nombre: `Usuario Normal`
   - Email: `usuario@gmail.com`
   - Password: `12345678`
4. El sistema debe:
   - ✅ Guardar el token
   - ✅ Detectar rol como `"user"` (por el email)
   - ✅ Navegar a HomeActivity

**Verificación en código**:
Abre Logcat y busca las peticiones HTTP (filtro: `OkHttp` o `Galletas`)

---

### **PRUEBA 3: Registro de un administrador**

1. Cierra sesión (si estás logueado)
2. Toca en "Crear Cuenta"
3. Registra un usuario con email de admin:
   - Nombre: `Admin Usuario`
   - Email: `admin@gmail.com.admin`
   - Password: `12345678`
4. El sistema debe:
   - ✅ Guardar el token
   - ✅ Detectar rol como `"admin"` (por el email)
   - ✅ Navegar a HomeActivity

---

### **PRUEBA 4: Verificar datos guardados**

Para verificar que se guardaron correctamente los datos del usuario, puedes agregar temporalmente este código en `HomeActivity.onCreate()`:

```kotlin
// Agregar después de RetrofitClient.init(applicationContext)
val userManager = UserManager(this)
val tokenManager = TokenManager(this)

Log.d("GalletasApp", "===== INFO DE SESIÓN =====")
Log.d("GalletasApp", "Token guardado: ${if (tokenManager.isLoggedIn()) "SÍ" else "NO"}")
Log.d("GalletasApp", "ID Usuario: ${userManager.getUserId()}")
Log.d("GalletasApp", "Nombre: ${userManager.getUserName()}")
Log.d("GalletasApp", "Email: ${userManager.getUserEmail()}")
Log.d("GalletasApp", "Rol: ${userManager.getUserRole()}")
Log.d("GalletasApp", "Es Admin: ${userManager.isAdmin()}")
Log.d("GalletasApp", "===========================")
```

**Resultado esperado en Logcat**:
```
GalletasApp: ===== INFO DE SESIÓN =====
GalletasApp: Token guardado: SÍ
GalletasApp: ID Usuario: -1 (o el ID si Xano lo devuelve)
GalletasApp: Nombre: null (o el nombre si Xano lo devuelve)
GalletasApp: Email: null (o el email si Xano lo devuelve)
GalletasApp: Rol: admin (o user)
GalletasApp: Es Admin: true (o false)
GalletasApp: ===========================
```

---

### **PRUEBA 5: Verificar respuesta de Xano**

**OBJETIVO**: Saber exactamente qué devuelve Xano en la respuesta de autenticación.

1. Abre Android Studio
2. Ve a la pestaña `Logcat` (abajo)
3. En el filtro, escribe: `OkHttp`
4. Haz login o registro
5. Busca la línea que dice: `<-- 200 OK` seguida del JSON de respuesta

**Ejemplo de lo que deberías ver**:

**Si Xano devuelve solo el token**:
```json
{
  "authToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Si Xano devuelve token + usuario**:
```json
{
  "authToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 123,
    "name": "Admin Usuario",
    "email": "admin@gmail.com.admin",
    "role": "admin",
    "created_at": 1234567890
  }
}
```

📋 **ANOTA QUÉ FORMATO DEVUELVE XANO** para confirmar el siguiente paso.

---

### **PRUEBA 6: Cerrar sesión**

1. Ve a la pestaña "Perfil"
2. Toca "Cerrar Sesión"
3. Verifica que:
   - ✅ Se borra el token
   - ✅ Se borran los datos del usuario
   - ✅ Vuelve a la pantalla de login

---

### **PRUEBA 7: Modo invitado**

1. En la pantalla de login, toca "Modo Invitado"
2. Verifica que:
   - ✅ No hay token guardado
   - ✅ No hay datos de usuario
   - ✅ Puedes navegar por la app
   - ✅ En el perfil muestra opciones para login/registro

---

## 🔍 PUNTOS DE VERIFICACIÓN

### ✅ **Lo que debe funcionar correctamente ahora**:

1. **Login/Registro**: Guardar token y detectar rol
2. **Cierre de sesión**: Limpiar token y datos de usuario
3. **Modo invitado**: Permitir navegación sin autenticación
4. **Detección de rol**: Por email (fallback temporal)
5. **Navegación básica**: Entre fragmentos funcionando

### ❌ **Lo que todavía NO está implementado**:

1. **Menús diferenciados**: Admin y usuario ven el mismo menú
2. **Protección de rutas**: Cualquiera puede acceder a todo
3. **Perfil real**: No muestra datos del usuario (solo login/logout)
4. **Panel de admin**: No existe la gestión de usuarios

---

## 📞 REPORTE DE RESULTADOS

**Por favor, ejecuta las pruebas y responde**:

1. ✅/❌ ¿El proyecto compila sin errores?
2. ✅/❌ ¿Puedes registrarte/loguearte correctamente?
3. ✅/❌ ¿Se detecta correctamente el rol (admin vs user)?
4. 📋 **MUY IMPORTANTE**: ¿Qué formato JSON devuelve Xano? (copia la respuesta del Logcat)
5. ✅/❌ ¿El cierre de sesión funciona correctamente?

Con esta información, procederé a implementar la **Fase 2: Navegación Diferenciada por Roles**.

---

## 🚀 PRÓXIMOS PASOS (FASE 2)

Una vez confirmes que todo funciona, implementaré:

1. **Crear dos menús XML**:
   - `bottom_nav_menu_admin.xml` (Productos, Añadir, Usuarios, Perfil)
   - `bottom_nav_menu_user.xml` (Productos, Carrito, Perfil)

2. **Modificar HomeActivity**:
   - Detectar rol al iniciar
   - Cargar el menú correcto según el rol
   - Configurar navegación apropiada

3. **Crear UsersFragment** (solo admin):
   - Listar todos los usuarios
   - Ver detalles de usuario
   - Editar/Eliminar usuarios

4. **Proteger AddProductFragment**:
   - Solo accesible para admin
   - Redirigir si un usuario normal intenta acceder

5. **Mejorar ProfileFragment**:
   - Mostrar datos reales del usuario
   - Agregar formulario de edición

**¿Listo para testear y continuar?** 🎯

