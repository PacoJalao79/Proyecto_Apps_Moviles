# 🧪 GUÍA DE PRUEBAS - SISTEMA DE ROLES

## 📱 PREPARACIÓN

Antes de empezar:
1. ✅ Asegúrate de que la app esté instalada en tu dispositivo/emulador
2. ✅ Abre la app desde cero (cierra y vuelve a abrir si ya estaba abierta)

---

## 🎯 PRUEBA 1: REGISTRO Y LOGIN COMO USUARIO NORMAL

### **Paso 1: Registro**
1. En la pantalla de login, toca **"Crear Cuenta"**
2. Completa el formulario:
   - **Nombre**: `Usuario Prueba`
   - **Email**: `usuario@gmail.com`
   - **Contraseña**: `12345678`
3. Toca **"Registrarse"**

### **Resultado Esperado**:
- ✅ Debe iniciar sesión automáticamente
- ✅ Debe navegar a la pantalla principal

### **Paso 2: Verificar Menú de Usuario Normal**
1. Observa la barra de navegación inferior

### **Resultado Esperado**:
```
┌────────────────────────────────────┐
│  🛍️ Productos │ 🛒 Carrito │ 👤 Perfil │
└────────────────────────────────────┘
```
- ✅ Debe mostrar **3 opciones**: Productos, Carrito, Perfil
- ❌ NO debe mostrar "Añadir" ni "Usuarios"

### **Paso 3: Verificar Vista de Productos**
1. Toca en **"Productos"** (debería estar activo por defecto)
2. Observa cada tarjeta de producto

### **Resultado Esperado**:
- ✅ Cada producto debe tener el botón **"Añadir al carrito"**
- ❌ NO debe aparecer el botón de **eliminar** (❌)

### **Paso 4: Probar Añadir al Carrito**
1. Toca el botón **"Añadir al carrito"** en cualquier producto
2. Debe aparecer un mensaje: `"[Nombre del producto] añadido al carrito"`
3. Toca en **"Carrito"** en el menú inferior

### **Resultado Esperado**:
- ✅ Debe mostrar el producto que añadiste
- ✅ Debe mostrar el precio total
- ✅ Debe mostrar los botones "Realizar Compra" y "Limpiar Carrito"

### **Paso 5: Verificar Perfil**
1. Toca en **"Perfil"** en el menú inferior

### **Resultado Esperado**:
- ✅ Debe mostrar un mensaje de bienvenida
- ✅ Debe mostrar el botón **"Cerrar Sesión"**
- ❌ NO debe mostrar mensajes de invitado

---

## 🎯 PRUEBA 2: CIERRE DE SESIÓN Y MODO INVITADO

### **Paso 1: Cerrar Sesión**
1. Desde el perfil, toca **"Cerrar Sesión"**

### **Resultado Esperado**:
- ✅ Debe volver a la pantalla de login
- ✅ Los campos deben estar vacíos

### **Paso 2: Modo Invitado**
1. En la pantalla de login, toca **"Modo Invitado"**

### **Resultado Esperado**:
- ✅ Debe navegar a la pantalla principal
- ✅ Debe mostrar el menú de usuario normal (sin privilegios admin)

### **Paso 3: Verificar Perfil como Invitado**
1. Toca en **"Perfil"**

### **Resultado Esperado**:
- ✅ Debe mostrar un mensaje indicando que eres invitado
- ✅ Debe mostrar botones para **"Iniciar Sesión"** y **"Crear Cuenta"**
- ❌ NO debe mostrar botón de cerrar sesión

---

## 🎯 PRUEBA 3: REGISTRO Y LOGIN COMO ADMINISTRADOR

### **Paso 1: Volver al Login**
1. Si estás como invitado, toca cualquier botón que te lleve al login
2. O cierra y vuelve a abrir la app

### **Paso 2: Registrar Admin**
1. Toca **"Crear Cuenta"**
2. Completa el formulario:
   - **Nombre**: `Admin Prueba`
   - **Email**: `admin@gmail.com.admin` ⚠️ **MUY IMPORTANTE EL SUFIJO**
   - **Contraseña**: `12345678`
3. Toca **"Registrarse"**

### **Resultado Esperado**:
- ✅ Debe iniciar sesión automáticamente
- ✅ Debe navegar a la pantalla principal

### **Paso 3: Verificar Menú de Administrador**
1. Observa la barra de navegación inferior

### **Resultado Esperado**:
```
┌──────────────────────────────────────────────────┐
│  🛍️ Productos │ ➕ Añadir │ 👥 Usuarios │ 👤 Perfil │
└──────────────────────────────────────────────────┘
```
- ✅ Debe mostrar **4 opciones**: Productos, Añadir, Usuarios, Perfil
- ❌ NO debe mostrar "Carrito"

### **Paso 4: Verificar Vista de Productos como Admin**
1. Toca en **"Productos"**
2. Observa cada tarjeta de producto

### **Resultado Esperado**:
- ✅ Cada producto debe tener el botón de **eliminar** (❌) en la esquina superior derecha
- ❌ NO debe aparecer el botón **"Añadir al carrito"**

### **Paso 5: Probar Eliminar Producto**
1. Toca el botón **eliminar** (❌) en cualquier producto
2. Debe aparecer un diálogo de confirmación

### **Resultado Esperado**:
- ✅ Debe mostrar: `"¿Estás seguro de que quieres eliminar el producto '[nombre]'?"`
- ✅ Debe tener botones "Eliminar" y "Cancelar"

3. Toca **"Cancelar"** (no elimines aún)

### **Paso 6: Verificar Opción Añadir Producto**
1. Toca en **"Añadir"** en el menú inferior

### **Resultado Esperado**:
- ✅ Debe mostrar un formulario con campos:
  - Nombre del producto
  - Descripción
  - Precio
  - Botón para seleccionar imágenes
  - Botón "Guardar"

### **Paso 7: Verificar Opción Usuarios**
1. Toca en **"Usuarios"** en el menú inferior

### **Resultado Esperado**:
- ✅ Debe mostrar una pantalla con título "Gestión de Usuarios"
- ⚠️ Por ahora mostrará un mensaje temporal: "Panel de Usuarios - Próximamente"
- *(Esta funcionalidad se implementará en la Fase 3)*

---

## 🎯 PRUEBA 4: CAMBIO ENTRE ROLES

### **Paso 1: Cerrar Sesión como Admin**
1. Toca en **"Perfil"**
2. Toca **"Cerrar Sesión"**

### **Paso 2: Login como Usuario Normal**
1. En la pantalla de login, ingresa:
   - **Email**: `usuario@gmail.com`
   - **Contraseña**: `12345678`
2. Toca **"Iniciar Sesión"**

### **Resultado Esperado**:
- ✅ El menú debe cambiar automáticamente a: **[Productos | Carrito | Perfil]**
- ✅ Los productos deben mostrar el botón **"Añadir al carrito"**
- ❌ NO debe mostrar el botón de eliminar

### **Paso 3: Volver a Login como Admin**
1. Cierra sesión nuevamente
2. Login con:
   - **Email**: `admin@gmail.com.admin`
   - **Contraseña**: `12345678`

### **Resultado Esperado**:
- ✅ El menú debe cambiar automáticamente a: **[Productos | Añadir | Usuarios | Perfil]**
- ✅ Los productos deben mostrar el botón de **eliminar**
- ❌ NO debe mostrar el botón "Añadir al carrito"

---

## ✅ CHECKLIST DE VERIFICACIÓN

### **Usuario Normal**:
- [ ] Puede registrarse y loguearse
- [ ] Ve el menú: [Productos | Carrito | Perfil]
- [ ] Ve el botón "Añadir al carrito" en productos
- [ ] NO ve el botón eliminar en productos
- [ ] NO ve las opciones "Añadir" ni "Usuarios" en el menú
- [ ] Puede añadir productos al carrito
- [ ] Puede ver su carrito
- [ ] Puede cerrar sesión

### **Administrador**:
- [ ] Puede registrarse con email `@gmail.com.admin`
- [ ] Ve el menú: [Productos | Añadir | Usuarios | Perfil]
- [ ] Ve el botón eliminar (❌) en productos
- [ ] NO ve el botón "Añadir al carrito" en productos
- [ ] NO ve la opción "Carrito" en el menú
- [ ] Puede acceder a "Añadir Producto"
- [ ] Puede acceder a "Usuarios"
- [ ] Puede eliminar productos
- [ ] Puede cerrar sesión

### **Modo Invitado**:
- [ ] Puede entrar como invitado
- [ ] Ve el menú de usuario normal (sin admin)
- [ ] En perfil ve opciones para login/registro
- [ ] NO puede hacer checkout (o sí, según diseñes)

### **Cambio de Roles**:
- [ ] Al cambiar de usuario normal a admin, el menú cambia
- [ ] Al cambiar de admin a usuario normal, el menú cambia
- [ ] Los botones de productos cambian según el rol

---

## 🐛 PROBLEMAS COMUNES Y SOLUCIONES

### **Problema 1: El menú no cambia al cambiar de usuario**
**Solución**: 
- Asegúrate de cerrar sesión completamente
- Reinicia la app
- Verifica que el email del admin termine en `@gmail.com.admin`

### **Problema 2: Ambos botones (añadir y eliminar) aparecen**
**Solución**:
- Limpia y recompila el proyecto: `Build → Rebuild Project`
- Verifica que `ProductAdapter` esté recibiendo correctamente el parámetro `isAdmin`

### **Problema 3: No aparece ningún botón en productos**
**Solución**:
- Verifica que tengas productos en la base de datos de Xano
- Revisa el Logcat para ver si hay errores de red

### **Problema 4: El fragmento de Usuarios da error**
**Solución**:
- Esto es normal, el binding se genera después de compilar
- Si persiste: `File → Invalidate Caches → Invalidate and Restart`

---

## 📸 EVIDENCIA SUGERIDA

Para cada prueba, puedes tomar screenshots de:
1. Menú de usuario normal
2. Vista de productos con botón "Añadir al carrito"
3. Menú de administrador
4. Vista de productos con botón eliminar
5. Pantalla de añadir producto
6. Pantalla de usuarios

---

## 📊 REGISTRO DE RESULTADOS

### Usuario Normal:
```
✅ Registro: _________
✅ Login: _________
✅ Menú correcto: _________
✅ Botones correctos: _________
✅ Carrito funciona: _________
```

### Administrador:
```
✅ Registro con @gmail.com.admin: _________
✅ Login: _________
✅ Menú correcto: _________
✅ Botones correctos: _________
✅ Acceso a Añadir: _________
✅ Acceso a Usuarios: _________
```

### Cambio de Roles:
```
✅ Menú cambia dinámicamente: _________
✅ Botones se actualizan: _________
```

---

## 🚀 PRÓXIMOS PASOS

**Después de completar todas las pruebas**:

1. ✅ Si todo funciona correctamente → Continuar con **Fase 3**
2. ❌ Si hay errores → Reportar qué prueba falló y qué mensaje de error aparece
3. 📋 Compartir resultados y decidir qué implementar en Fase 3

---

**¡Hora de probar la app!** 🎉

Sigue estas pruebas en orden y reporta los resultados.

