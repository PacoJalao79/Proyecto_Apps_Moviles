# 🚫 ERROR 403 FORBIDDEN - Credenciales Inválidas

## ❌ EL PROBLEMA

**Error HTTP 403**: "Invalid Credentials"

```
POST /auth/login
{"email":"javi@gmail.com","password":"dani1234"}

<-- 403 Forbidden
{"message":"Invalid Credentials."}
```

**Esto NO es un error de la app, es una respuesta del servidor Xano.**

---

## 🔍 CAUSAS POSIBLES

### **1. El usuario NO existe en la base de datos** ⭐ MÁS PROBABLE
- Nunca te has registrado con ese email
- El usuario fue eliminado de la base de datos

### **2. La contraseña es incorrecta**
- Contraseña mal escrita
- Cambio de mayúsculas/minúsculas

### **3. El email es incorrecto**
- Email mal escrito
- Espacios en blanco al inicio o final

---

## ✅ SOLUCIONES

### **Solución 1: Registrar un nuevo usuario** (RECOMENDADO)

1. **En la app**:
   - Toca "Crear Cuenta"
   - Completa el formulario:
     - **Nombre**: Javi
     - **Email**: javi@gmail.com
     - **Contraseña**: dani1234
   - Toca "Registrarse"

2. **Verifica**:
   - Deberías iniciar sesión automáticamente
   - Si funciona, el registro fue exitoso

---

### **Solución 2: Verificar en Xano**

1. **Ve a tu panel de Xano**:
   - Abre https://x8ki-letl-twmt.n7.xano.io

2. **Navega a la tabla `user`**:
   - Busca el email `javi@gmail.com`

3. **Si NO existe**:
   - Regístralo desde la app (Solución 1)

4. **Si existe**:
   - Verifica que el campo `password` esté hasheado
   - Intenta hacer login desde el panel de Xano para verificar

---

### **Solución 3: Usar un usuario diferente**

**Prueba con las credenciales que usaste antes**:
- Usuario: `usuario@gmail.com`
- Contraseña: `12345678`

O si eres admin:
- Usuario: `admin@gmail.com.admin`
- Contraseña: `12345678`

---

## 🔧 MEJORA IMPLEMENTADA

He mejorado el manejo de errores en `MainActivity.kt` para que ahora muestre mensajes más claros:

### **Error 403**:
```
❌ Credenciales incorrectas

Verifica tu email y contraseña.
Si no tienes cuenta, créala primero.
```

### **Error 404**:
```
❌ Usuario no encontrado

¿No tienes cuenta? Crea una nueva.
```

### **Error de conexión**:
```
Error de conexión: [detalle]

Verifica tu conexión a internet.
```

---

## 🧪 CÓMO PROBAR LA SOLUCIÓN

### **Opción A: Crear nueva cuenta**

1. Compila el proyecto:
   ```
   Build → Rebuild Project
   ```

2. Ejecuta la app

3. En la pantalla de login, toca **"Crear Cuenta"**

4. Llena el formulario:
   - Nombre: Javi
   - Email: javi@gmail.com
   - Contraseña: dani1234

5. Toca **"Registrarse"**

6. **Resultado esperado**:
   - ✅ Registro exitoso
   - ✅ Login automático
   - ✅ Navegación a HomeActivity

---

### **Opción B: Usar usuario existente**

1. En la pantalla de login, usa:
   - Email: `usuario@gmail.com`
   - Contraseña: `12345678`

2. Toca **"Iniciar Sesión"**

3. **Resultado esperado**:
   - ✅ Login exitoso
   - ✅ Navegación a HomeActivity

---

## 📊 CÓDIGOS DE ERROR Y SUS SIGNIFICADOS

| Código | Significado | Qué hacer |
|--------|-------------|-----------|
| **403** | Credenciales inválidas | Verifica email y contraseña, o regístrate |
| **401** | No autorizado | Contraseña incorrecta |
| **404** | Usuario no encontrado | Crea una cuenta nueva |
| **500** | Error del servidor | Espera un momento y reintenta |

---

## 🐛 DEBUGGING EN XANO

Si quieres verificar qué está pasando en Xano:

### **1. Revisar tabla de usuarios**:
```
Panel Xano → Database → Tabla "user"
Buscar: javi@gmail.com
```

### **2. Probar endpoint de login**:
```
Panel Xano → API → Endpoint "auth/login"
Test con:
{
  "email": "javi@gmail.com",
  "password": "dani1234"
}
```

### **3. Ver logs de Xano**:
```
Panel Xano → API → Logs
Buscar la petición fallida (403)
```

---

## 🎯 RESUMEN

**El error 403 significa**:
- ❌ Las credenciales son incorrectas
- ❌ O el usuario no existe

**Solución más simple**:
1. ✅ Usa "Crear Cuenta" en la app
2. ✅ Registra: javi@gmail.com / dani1234
3. ✅ Intenta login nuevamente

---

## 📞 PRÓXIMOS PASOS

1. **Compila el proyecto** (ya tiene mejor manejo de errores)
2. **Registra un nuevo usuario** desde la app
3. **Intenta hacer login** con ese usuario
4. **Reporta** si el error persiste

---

**¡El error 403 es normal si el usuario no existe!**

Solo necesitas registrarte primero. 🎯

