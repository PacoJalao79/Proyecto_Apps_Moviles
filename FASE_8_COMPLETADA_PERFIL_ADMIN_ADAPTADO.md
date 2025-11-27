# ✅ FASE 8: Perfil del Administrador Adaptado

**Fecha:** 26 de noviembre de 2025  
**Estado:** ✅ COMPLETADO

## 🎯 Objetivo

Adaptar la funcionalidad de edición y visualización de perfil para el **administrador**, ocultando el campo de **dirección** ya que el admin no realiza compras.

---

## 🔧 Cambios Implementados

### **1. ProfileFragment.kt - Método `displayUserProfile()`**

Se agregó detección del rol de usuario para ocultar la dirección si es administrador:

```kotlin
private fun displayUserProfile(user: User) {
    val isAdmin = userManager.isAdmin()  // ✅ Detectar si es admin
    
    with(binding) {
        // ...código existente para nombre, email, teléfono...
        
        // ✅ NUEVO: Ocultar dirección para administradores
        if (isAdmin) {
            userAddressTextView.visibility = View.GONE
            android.util.Log.d("ProfileFragment", "Dirección oculta para administrador")
        } else {
            // Mostrar dirección solo para usuarios normales
            if (!user.shippingAddress.isNullOrBlank()) {
                userAddressTextView.text = "Dirección: ${user.shippingAddress}"
                userAddressTextView.visibility = View.VISIBLE
            } else {
                userAddressTextView.visibility = View.GONE
            }
        }
        
        // ...resto del código...
    }
}
```

### **2. ProfileFragment.kt - Método `showEditProfileDialog()`**

Se modificó el diálogo de edición para ocultar el campo de dirección si el usuario es admin:

```kotlin
private fun showEditProfileDialog() {
    val currentUser = userManager.getUser() ?: return
    val isAdmin = userManager.isAdmin()  // ✅ Detectar si es admin

    val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)
    // ...obtener referencias a los campos...
    
    // ✅ NUEVO: Ocultar campo de dirección para administradores
    val addressInputLayout = dialogView.findViewById<TextInputLayout>(R.id.edit_address_layout)
    if (isAdmin) {
        addressInputLayout.visibility = View.GONE
        android.util.Log.d("ProfileFragment", "Campo de dirección oculto para administrador")
    }

    // Pre-llenar campos
    editName.setText(currentUser.name)
    editFirstName.setText(currentUser.firstName ?: "")
    editLastName.setText(currentUser.lastName ?: "")
    editEmail.setText(currentUser.email)
    editPhone.setText(currentUser.phone ?: "")
    if (!isAdmin) {  // ✅ Solo llenar dirección si NO es admin
        editAddress.setText(currentUser.shippingAddress ?: "")
    }

    MaterialAlertDialogBuilder(requireContext())
        .setTitle("Editar Perfil")
        .setView(dialogView)
        .setPositiveButton("Guardar") { _, _ ->
            // ...obtener valores de los campos...
            
            // ✅ NUEVO: Dirección null para admin, del campo para usuarios
            val newAddress = if (isAdmin) null else editAddress.text.toString().trim().ifBlank { null }

            // Crear usuario actualizado
            val updatedUser = currentUser.copy(
                name = newName,
                firstName = newFirstName.ifBlank { null },
                lastName = newLastName.ifBlank { null },
                email = newEmail,
                phone = newPhone.ifBlank { null },
                shippingAddress = newAddress,  // ✅ null para admin
                password = newPassword.ifBlank { null }
            )

            updateProfile(updatedUser)
        }
        .setNegativeButton("Cancelar", null)
        .show()
}
```

---

## 📊 Comparación de Perfiles

### **Perfil de Usuario Normal:**

```
┌─────────────────────────────────┐
│      ¡Hola, Juan Pérez!        │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│   📋 Información del Usuario   │
├─────────────────────────────────┤
│ Juan Pérez                      │
│ Primer Nombre: Juan             │
│ Apellido: Pérez                 │
│ juan@example.com                │
│ Teléfono: 555-1234              │
│ Dirección: Calle Principal 123  │ ✅ VISIBLE
│ Rol: user                       │
└─────────────────────────────────┘
```

### **Perfil de Administrador:**

```
┌─────────────────────────────────┐
│      ¡Hola, Admin García!      │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│   📋 Información del Admin     │
├─────────────────────────────────┤
│ Admin García                    │
│ Primer Nombre: Admin            │
│ Apellido: García                │
│ admin@gmail.com.admin           │
│ Teléfono: 555-9999              │
│ Rol: admin                      │
└─────────────────────────────────┘
                                    ❌ Dirección OCULTA
```

---

## 🎨 Diálogo de Edición

### **Usuario Normal:**

```
┌──────────────────────────────────┐
│       Editar Perfil             │
├──────────────────────────────────┤
│ Nombre:          [Juan Pérez   ] │
│ Primer Nombre:   [Juan         ] │
│ Apellido:        [Pérez        ] │
│ Email:           [juan@...     ] │
│ Teléfono:        [555-1234     ] │
│ Dirección:       [Calle...     ] │ ✅
│ Contraseña:      [••••••••     ] │
│                                   │
│      [Cancelar]    [Guardar]     │
└──────────────────────────────────┘
```

### **Administrador:**

```
┌──────────────────────────────────┐
│       Editar Perfil             │
├──────────────────────────────────┤
│ Nombre:          [Admin García ] │
│ Primer Nombre:   [Admin        ] │
│ Apellido:        [García       ] │
│ Email:           [admin@...    ] │
│ Teléfono:        [555-9999     ] │
│ Contraseña:      [••••••••     ] │
│                                   │
│      [Cancelar]    [Guardar]     │
└──────────────────────────────────┘
                                    ❌ Sin campo de dirección
```

---

## 🔄 Flujo de Datos del Administrador

### **Login del Admin:**

```
1. Admin ingresa email (termina en @gmail.com.admin)
   ↓
2. POST /auth/login → Obtiene token + datos básicos
   ↓
3. Sistema detecta rol = "admin"
   ↓
4. GET /user/{user_id} → Carga perfil completo
   ↓
5. Guarda en SharedPreferences (con shippingAddress = null)
   ↓
6. Navega a HomeActivity con menú de admin
   ↓
7. Al ver perfil → Campo de dirección oculto ✅
```

### **Admin edita su perfil:**

```
1. Admin hace clic en "Editar Perfil"
   ↓
2. Diálogo se muestra SIN campo de dirección
   ↓
3. Admin modifica: nombre, apellido, teléfono, etc.
   ↓
4. Al guardar: shippingAddress se envía como null
   ↓
5. PUT /user/{user_id} con dirección = null
   ↓
6. Perfil actualizado sin dirección
   ↓
7. ✅ Todo correcto
```

---

## 💾 Estructura de Datos

### **Usuario Normal en SharedPreferences:**

```json
{
    "id": 123,
    "name": "Juan Pérez",
    "email": "juan@example.com",
    "firstName": "Juan",
    "lastName": "Pérez",
    "phone": "555-1234",
    "shippingAddress": "Calle Principal 123",  ✅ Tiene dirección
    "role": "user",
    "createdAt": 1732667413000
}
```

### **Administrador en SharedPreferences:**

```json
{
    "id": 456,
    "name": "Admin García",
    "email": "admin@gmail.com.admin",
    "firstName": "Admin",
    "lastName": "García",
    "phone": "555-9999",
    "shippingAddress": null,  ❌ Sin dirección
    "role": "admin",
    "createdAt": 1732667413000
}
```

---

## 🧪 Cómo Probar

### **Prueba 1: Verificar perfil de admin**

1. **Inicia sesión** con una cuenta de administrador
   - Email terminado en `@gmail.com.admin`
2. **Ve a la pestaña Perfil**
3. **✅ VERIFICAR:** 
   - El campo de dirección NO aparece
   - Se muestran: nombre, primer nombre, apellido, email, teléfono, rol

### **Prueba 2: Editar perfil de admin**

1. **Como admin, haz clic en "Editar Perfil"**
2. **✅ VERIFICAR:**
   - El diálogo NO muestra el campo de dirección
   - Solo aparecen: nombre, primer nombre, apellido, email, teléfono, contraseña
3. **Modifica algunos datos**
4. **Guarda los cambios**
5. **✅ VERIFICAR:** Los cambios se guardan correctamente

### **Prueba 3: Persistencia de datos del admin**

1. **Con la sesión de admin abierta**
2. **Edita tu perfil** (primer nombre, apellido, teléfono)
3. **Guarda los cambios**
4. **Cierra completamente la app**
5. **Vuelve a abrir la app**
6. **Ve a Perfil**
7. **✅ VERIFICAR:** 
   - Todos los datos aparecen inmediatamente
   - La dirección sigue sin aparecer

### **Prueba 4: Cambio entre usuarios**

1. **Inicia sesión como usuario normal**
2. **Ve a Perfil** → ✅ Dirección visible
3. **Cierra sesión**
4. **Inicia sesión como admin**
5. **Ve a Perfil** → ✅ Dirección oculta
6. **✅ VERIFICAR:** La interfaz se adapta correctamente al rol

---

## 🎯 Ventajas de esta Implementación

### ✅ **1. Adaptación Automática**
- La interfaz se adapta automáticamente según el rol del usuario
- No se requieren pantallas separadas

### ✅ **2. Coherencia de Datos**
- Los admins nunca tienen dirección de envío
- Se mantiene la integridad de los datos en la base de datos

### ✅ **3. Experiencia de Usuario**
- El admin no ve campos innecesarios
- La interfaz es limpia y relevante para cada tipo de usuario

### ✅ **4. Seguridad**
- Aunque el campo esté oculto, se fuerza `shippingAddress = null` para admins
- Previene que se guarden datos incorrectos

---

## 📝 Lógica de Detección de Admin

El sistema detecta si un usuario es administrador de dos formas:

### **1. Por el campo `role` en la base de datos:**
```kotlin
fun isAdmin(): Boolean {
    return getUserRole() == ROLE_ADMIN  // "admin"
}
```

### **2. Por el email (fallback):**
```kotlin
fun detectRoleFromEmail(email: String): String {
    return if (email.endsWith("@gmail.com.admin")) {
        ROLE_ADMIN
    } else {
        ROLE_USER
    }
}
```

---

## 📊 Logs de Depuración

Puedes verificar el comportamiento en Logcat:

### **Para Administrador:**

```
D/ProfileFragment: Rol guardado: admin
D/ProfileFragment: ¿Es admin?: true
D/ProfileFragment: Dirección oculta para administrador
D/ProfileFragment: Campo de dirección oculto para administrador
D/ProfileFragment: ✅ Perfil actualizado exitosamente
```

### **Para Usuario Normal:**

```
D/ProfileFragment: Rol guardado: user
D/ProfileFragment: ¿Es admin?: false
D/ProfileFragment: Mostrando dirección: Calle Principal 123
```

---

## ✨ Resultado Final

### **Antes de esta implementación:** ❌
- El admin veía el campo de dirección (innecesario)
- Podía editar la dirección sin necesidad
- Interfaz no adaptada al rol

### **Después de esta implementación:** ✅
- El admin **NO ve** el campo de dirección
- La interfaz se **adapta automáticamente** al rol
- El campo de dirección es **null** para admins
- **Persistencia de datos** funciona correctamente para ambos roles
- **Edición de perfil** adaptada a cada tipo de usuario

---

## 🚀 Estado del Proyecto

### Funcionalidades Completadas:

1. ✅ Autenticación (Login/Signup)
2. ✅ Gestión de productos
3. ✅ Carrito de compras (solo usuarios)
4. ✅ Realizar pedidos (solo usuarios)
5. ✅ Ver historial de pedidos
6. ✅ Editar perfil completo (usuarios y admin)
7. ✅ Mostrar todos los datos del perfil
8. ✅ Persistencia de datos del usuario
9. ✅ **Perfil del administrador adaptado** (sin dirección) 🎉

---

## 📌 Notas Importantes

### **Para Usuarios Normales:**
- ✅ Pueden ver y editar: nombre, primer nombre, apellido, email, teléfono, **dirección**, contraseña
- ✅ La dirección es necesaria para los envíos de pedidos

### **Para Administradores:**
- ✅ Pueden ver y editar: nombre, primer nombre, apellido, email, teléfono, contraseña
- ❌ NO ven ni editan la dirección (no realizan compras)
- ✅ Tienen acceso a funciones administrativas: añadir productos, ver usuarios, gestionar pedidos

---

**¡Implementación completada! El perfil del administrador ahora está correctamente adaptado. ✅**

