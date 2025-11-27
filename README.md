# 🍪 Galletas Store - Aplicación Móvil de E-commerce

![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)
![API](https://img.shields.io/badge/API-24%2B-orange.svg)
![Version](https://img.shields.io/badge/Version-1.0-brightgreen.svg)

Aplicación móvil de e-commerce para la venta de galletas, desarrollada en **Android Studio** con **Kotlin**. La aplicación cuenta con dos tipos de usuarios: **Administrador** y **Usuario Común**, cada uno con funcionalidades específicas.

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración de la API](#-configuración-de-la-api)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Guía de Uso](#-guía-de-uso)
- [Funcionalidades por Rol](#-funcionalidades-por-rol)
- [Arquitectura](#-arquitectura)
- [Base de Datos (Xano)](#-base-de-datos-xano)
- [Capturas de Pantalla](#-capturas-de-pantalla)
- [Solución de Problemas](#-solución-de-problemas)
- [Contribuir](#-contribuir)
- [Licencia](#-licencia)

---

## ✨ Características

### 👤 Usuario Común
- ✅ Registro e inicio de sesión
- ✅ Explorar catálogo de productos con imágenes múltiples
- ✅ Carrusel de imágenes para cada producto
- ✅ Agregar productos al carrito (+/-)
- ✅ Gestión de cantidades en el carrito
- ✅ Ver total de compra en tiempo real
- ✅ Realizar pedidos
- ✅ Historial de pedidos personales
- ✅ Editar perfil (nombre, apellido, dirección, teléfono, email, contraseña)
- ✅ Navegación fluida con Bottom Navigation

### 👨‍💼 Administrador
- ✅ Inicio de sesión como admin
- ✅ Ver todos los productos
- ✅ Agregar nuevos productos con múltiples imágenes
- ✅ Editar productos existentes
- ✅ Eliminar productos
- ✅ Ver todos los pedidos de todos los usuarios
- ✅ Información detallada de cada pedido (cliente, dirección, teléfono)
- ✅ Gestión de usuarios (ver, editar, eliminar)
- ✅ Editar perfil de administrador
- ✅ Panel de administración completo

### 🎨 Diseño
- Material Design 3
- Interfaz intuitiva y moderna
- Indicadores circulares perfectos para carrusel de imágenes
- Iconos personalizados
- Animaciones fluidas
- Responsive design

---

## 🛠 Tecnologías Utilizadas

### Frontend (Android)
- **Lenguaje:** Kotlin 1.9.0
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 36
- **IDE:** Android Studio

### Librerías y Frameworks
| Librería | Propósito | Versión |
|----------|-----------|---------|
| **Retrofit** | Cliente HTTP para consumo de API REST | 2.9.0 |
| **OkHttp** | Interceptores y logging de peticiones HTTP | 4.11.0 |
| **Gson** | Serialización/Deserialización JSON | 2.10.1 |
| **Coil** | Carga de imágenes desde URLs | 2.4.0 |
| **Navigation Component** | Navegación entre fragmentos | 2.7.5 |
| **Material Components** | Componentes de Material Design | 1.10.0 |
| **Coroutines** | Programación asíncrona | 1.7.3 |
| **ViewBinding** | Binding de vistas | - |

### Backend
- **Plataforma:** Xano (No-Code Backend)
- **Base de Datos:** PostgreSQL (gestionada por Xano)
- **APIs:** RESTful APIs

---

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

1. **Android Studio** (versión Hedgehog 2023.1.1 o superior)
   - [Descargar Android Studio](https://developer.android.com/studio)

2. **JDK 11** o superior
   - Verificar con: `java -version`

3. **Git** (para clonar el repositorio)
   - [Descargar Git](https://git-scm.com/)

4. **Dispositivo Android** o **Emulador**
   - API 24 (Android 7.0) o superior

---

## 🚀 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/PacoJalao79/Proyecto-app-moviles.git
cd Proyecto-app-moviles
```

### 2. Abrir el Proyecto en Android Studio

1. Abre **Android Studio**
2. Selecciona **File > Open**
3. Navega hasta la carpeta del proyecto clonado
4. Espera a que Gradle sincronice las dependencias (puede tardar unos minutos)

### 3. Sincronizar Gradle

Android Studio sincronizará automáticamente las dependencias. Si no lo hace:
- Click en **File > Sync Project with Gradle Files**

### 4. Limpiar y Compilar (Opcional)

Si encuentras problemas, ejecuta:

**Opción A: Desde Android Studio**
- **Build > Clean Project**
- **Build > Rebuild Project**

**Opción B: Desde terminal**
```bash
# Limpiar proyecto
./gradlew clean

# Compilar
./gradlew assembleDebug
```

En Windows, usa el archivo `.bat` incluido:
```cmd
LIMPIAR_Y_COMPILAR.bat
```

### 5. Ejecutar la Aplicación

1. Conecta un dispositivo Android o inicia un emulador
2. Click en el botón **Run** (▶️) en Android Studio
3. Selecciona tu dispositivo
4. La app se instalará y ejecutará automáticamente

---

## 🔌 Configuración de la API

La aplicación utiliza dos APIs de **Xano**:

### APIs Configuradas

```kotlin
// En app/build.gradle.kts
buildConfigField("String", "AUTH_BASE_URL", "\"https://x8ki-letl-twmt.n7.xano.io/api:xoz1eOTl/\"")
buildConfigField("String", "STORE_BASE_URL", "\"https://x8ki-letl-twmt.n7.xano.io/api:zd4q1kxN/\"")
```

### Endpoints Principales

#### 🔐 Authentication API (`AUTH_BASE_URL`)
- `POST /auth/signup` - Registro de usuario
- `POST /auth/login` - Inicio de sesión
- `GET /auth/me` - Obtener información del usuario autenticado

#### 🛒 E-commerce API (`STORE_BASE_URL`)
- **Productos:**
  - `GET /product` - Listar productos
  - `POST /product` - Crear producto (admin)
  - `PUT /product/{id}` - Actualizar producto (admin)
  - `DELETE /product/{id}` - Eliminar producto (admin)

- **Pedidos:**
  - `GET /order` - Listar pedidos (filtrado por rol)
  - `POST /order` - Crear pedido

- **Usuarios:**
  - `GET /user` - Listar usuarios (admin)
  - `GET /user/{id}` - Obtener usuario por ID
  - `PUT /user/{id}` - Actualizar usuario
  - `DELETE /user/{id}` - Eliminar usuario (admin)

### 🔑 Autenticación

La app utiliza **Bearer Token Authentication**:
- Al iniciar sesión, se recibe un token JWT
- El token se guarda en `SharedPreferences`
- Se envía automáticamente en cada petición mediante `AuthInterceptor`

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/example/galletas/
│
├── api/                          # Configuración de API
│   ├── RetrofitClient.kt         # Cliente Retrofit singleton
│   ├── AuthInterceptor.kt        # Interceptor para autenticación
│   ├── AuthService.kt            # Endpoints de autenticación
│   ├── ProductService.kt         # Endpoints de productos
│   ├── OrderService.kt           # Endpoints de pedidos
│   └── UserService.kt            # Endpoints de usuarios
│
├── data/                         # Modelos de datos
│   └── model/
│       ├── User.kt               # Modelo de usuario
│       ├── Product.kt            # Modelo de producto
│       ├── Order.kt              # Modelo de pedido
│       ├── LoginRequest.kt       # Request de login
│       ├── SignUpRequest.kt      # Request de registro
│       └── OrderRequest.kt       # Request de pedido
│
├── ui/                           # Interfaz de usuario
│   ├── activities/               # Actividades
│   │   ├── MainActivity.kt       # Pantalla de login
│   │   ├── SignUpActivity.kt     # Pantalla de registro
│   │   └── HomeActivity.kt       # Pantalla principal con navigation
│   │
│   ├── fragments/                # Fragmentos
│   │   ├── ProductsFragment.kt   # Lista de productos
│   │   ├── CartFragment.kt       # Carrito de compras
│   │   ├── OrdersFragment.kt     # Historial de pedidos
│   │   ├── ProfileFragment.kt    # Perfil de usuario
│   │   ├── AddProductFragment.kt # Agregar/editar productos (admin)
│   │   └── UsersFragment.kt      # Gestión de usuarios (admin)
│   │
│   └── adapter/                  # Adaptadores de RecyclerView
│       ├── ProductAdapter.kt     # Adaptador de productos
│       ├── CartAdapter.kt        # Adaptador del carrito
│       ├── OrderAdapter.kt       # Adaptador de pedidos
│       ├── UserAdapter.kt        # Adaptador de usuarios
│       └── ImageSliderAdapter.kt # Adaptador de carrusel de imágenes
│
└── util/                         # Utilidades
    ├── UserManager.kt            # Gestión de sesión y datos del usuario
    └── CartManager.kt            # Gestión del carrito de compras

res/
├── layout/                       # Archivos XML de diseño
├── drawable/                     # Recursos gráficos
├── menu/                         # Menús de navegación
├── navigation/                   # Gráficos de navegación
└── values/                       # Colores, strings, temas
```

---

## 📖 Guía de Uso

### 🔐 Registro e Inicio de Sesión

#### Para Usuario Nuevo:
1. Abre la aplicación
2. Click en **"¿No tienes cuenta? Regístrate"**
3. Completa el formulario:
   - Nombre
   - Email
   - Contraseña
4. Click en **"Registrarse"**
5. Serás redirigido al login automáticamente

#### Credenciales de Prueba:

**Usuario Común:**
```
Email: usuario@test.com
Contraseña: 123456
```

**Administrador:**
```
Email: admin@test.com
Contraseña: 123456
```

### 🛍️ Como Usuario Común

#### Explorar Productos:
1. Ve al tab **"Productos"**
2. Desliza las imágenes del producto para ver más fotos
3. Click en el botón **"ℹ️"** para ver detalles completos

#### Agregar al Carrito:
1. Encuentra un producto que te guste
2. Click en el botón **"+"** para agregar al carrito
3. El contador se actualizará automáticamente

#### Gestionar Carrito:
1. Ve al tab **"Carrito"**
2. Usa **"+"** y **"-"** para ajustar cantidades
3. Verás el total actualizado en tiempo real
4. Click en **"Realizar Pedido"** para finalizar la compra

#### Ver Pedidos:
1. Ve al tab **"Pedidos"**
2. Verás solo tus pedidos personales
3. Click en un pedido para ver detalles completos

#### Editar Perfil:
1. Ve al tab **"Perfil"**
2. Click en **"Editar Perfil"**
3. Modifica:
   - Nombre
   - Apellido
   - Email
   - Teléfono
   - Dirección de envío
   - Contraseña (opcional)
4. Click en **"Guardar Cambios"**

### 👨‍💼 Como Administrador

#### Gestionar Productos:
1. Ve al tab **"Productos"**
2. Click en el botón **"+"** flotante para agregar producto
3. Completa el formulario:
   - Nombre del producto
   - Descripción
   - Precio
   - URLs de imágenes (separadas por comas)
4. Para editar: click largo en un producto
5. Para eliminar: confirma en el diálogo

#### Ver Todos los Pedidos:
1. Ve al tab **"Pedidos"**
2. Verás TODOS los pedidos de todos los usuarios
3. Click en un pedido para ver:
   - Datos del cliente
   - Dirección de envío
   - Teléfono de contacto
   - Total del pedido

#### Gestionar Usuarios:
1. Ve al tab **"Usuarios"**
2. Verás la lista completa de usuarios registrados
3. Click en **"Editar"** para modificar datos
4. Click en **"Eliminar"** para borrar usuario

#### Editar Perfil Admin:
1. Ve al tab **"Perfil"**
2. Click en **"Editar Perfil"**
3. Modifica:
   - Nombre
   - Apellido
   - Email
   - Teléfono
   - Contraseña (opcional)
4. Click en **"Guardar Cambios"**

---

## 🎭 Funcionalidades por Rol

### 👤 Usuario Común

| Funcionalidad | Descripción |
|---------------|-------------|
| **Registro/Login** | Crear cuenta y autenticarse |
| **Ver Productos** | Explorar catálogo completo con imágenes |
| **Carrusel de Imágenes** | Ver múltiples fotos de cada producto |
| **Carrito** | Agregar/quitar productos, ajustar cantidades |
| **Realizar Pedidos** | Finalizar compra con información de envío |
| **Historial Personal** | Ver solo sus propios pedidos |
| **Editar Perfil** | Actualizar datos personales y dirección |

### 👨‍💼 Administrador

| Funcionalidad | Descripción |
|---------------|-------------|
| **Login Admin** | Autenticación con privilegios de admin |
| **CRUD Productos** | Crear, editar y eliminar productos |
| **Múltiples Imágenes** | Agregar varias URLs de imágenes por producto |
| **Ver Todos los Pedidos** | Acceso completo al historial de todos los usuarios |
| **Detalles de Cliente** | Ver datos de contacto y envío en cada pedido |
| **Gestión de Usuarios** | Ver, editar y eliminar cuentas de usuario |
| **Panel Administrativo** | Interfaz dedicada con funciones especiales |
| **Editar Perfil Admin** | Actualizar datos del administrador |

---

## 🏗 Arquitectura

La aplicación sigue una arquitectura **modular y escalable**:

### Patrón de Arquitectura
- **MVC/MVVM Híbrido**: Separación de lógica de negocio y presentación
- **Repository Pattern**: Abstracción de fuentes de datos
- **Singleton Pattern**: Para clientes HTTP y managers

### Flujo de Datos

```
┌─────────────┐
│   UI Layer  │ (Activities, Fragments)
└──────┬──────┘
       │
       ↓
┌─────────────┐
│  Managers   │ (UserManager, CartManager)
└──────┬──────┘
       │
       ↓
┌─────────────┐
│  API Layer  │ (Retrofit Services)
└──────┬──────┘
       │
       ↓
┌─────────────┐
│  Backend    │ (Xano APIs)
└─────────────┘
```

### Gestión de Estado
- **SharedPreferences**: Para datos de sesión y configuración local
- **Memoria Volátil**: Carrito de compras (se pierde al cerrar la app)
- **API**: Fuente de verdad para productos, pedidos y usuarios

---

## 🗄 Base de Datos (Xano)

### Tablas Principales

#### 🧑 `user` (Usuarios)
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Integer | ID único del usuario |
| `name` | String | Nombre completo |
| `email` | String | Correo electrónico (único) |
| `first_name` | String | Primer nombre |
| `last_name` | String | Apellido |
| `phone` | String | Teléfono de contacto |
| `shippingAddress` | String | Dirección de envío |
| `role` | String | Rol: "user" o "admin" |
| `created_at` | Timestamp | Fecha de registro |

#### 🍪 `product` (Productos)
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Integer | ID único del producto |
| `name` | String | Nombre del producto |
| `description` | Text | Descripción detallada |
| `price` | Float | Precio en USD |
| `images` | JSON Array | URLs de imágenes del producto |
| `created_at` | Timestamp | Fecha de creación |

#### 📦 `order` (Pedidos)
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Integer | ID único del pedido |
| `user_id` | Integer | ID del usuario que realizó el pedido |
| `total` | Float | Total del pedido en USD |
| `status` | String | Estado del pedido (ej: "pending") |
| `created_at` | Timestamp | Fecha y hora del pedido |
| `_user` | Object | Relación con tabla user (JOIN) |

### Relaciones
- **order → user**: Un pedido pertenece a un usuario (relación 1:N)
- **order → product**: Un pedido puede contener múltiples productos (relación N:N, manejada por la app)

---

## 📸 Capturas de Pantalla

*(Aquí puedes agregar capturas de pantalla de tu aplicación)*

### Pantallas Principales

```
┌──────────────┬──────────────┬──────────────┐
│    Login     │   Registro   │   Productos  │
├──────────────┼──────────────┼──────────────┤
│   Carrito    │   Pedidos    │    Perfil    │
├──────────────┼──────────────┼──────────────┤
│ Admin Panel  │   Usuarios   │  Agregar Prod│
└──────────────┴──────────────┴──────────────┘
```

---

## 🔧 Solución de Problemas

### Error: "Unresolved reference"
**Solución:**
```bash
./gradlew clean
./gradlew build
```
O usa el archivo: `LIMPIAR_Y_COMPILAR.bat`

### Error: HTTP 401 (No Autorizado)
**Causa:** Token expirado o inválido
**Solución:**
1. Cierra sesión en la app
2. Vuelve a iniciar sesión
3. El token se renovará automáticamente

### Error: HTTP 404 en `/auth/me`
**Causa:** El usuario no existe o el token es inválido
**Solución:**
1. Verifica que el usuario esté registrado en Xano
2. Cierra sesión y vuelve a iniciar

### Productos no muestran imágenes
**Causa:** URLs de imágenes incorrectas o sin conexión a internet
**Solución:**
1. Verifica tu conexión a internet
2. Asegúrate de que las URLs de imágenes sean válidas
3. Las URLs deben empezar con `http://` o `https://`

### Carrito vacío después de reiniciar la app
**Comportamiento esperado:** El carrito se guarda solo en memoria volátil
**Solución (si quieres persistencia):**
- Implementa guardado en `SharedPreferences`
- O guarda el carrito en el backend

### Gradle Sync Failed
**Solución:**
1. **File > Invalidate Caches / Restart**
2. Espera a que reinicie Android Studio
3. **File > Sync Project with Gradle Files**

---

## 🚀 Despliegue

### Generar APK de Producción

1. **Build > Generate Signed Bundle / APK**
2. Selecciona **APK**
3. Crea o selecciona tu keystore
4. Completa la información de firma
5. Selecciona **release**
6. El APK se generará en: `app/release/app-release.apk`

### Configuración de Release (opcional)

En `app/build.gradle.kts`, la configuración de release:

```kotlin
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}
```

---

## 🤝 Contribuir

Las contribuciones son bienvenidas. Para contribuir:

1. **Fork** el repositorio
2. Crea una **rama** para tu feature:
   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```
3. **Commit** tus cambios:
   ```bash
   git commit -m "Añadir nueva funcionalidad"
   ```
4. **Push** a la rama:
   ```bash
   git push origin feature/nueva-funcionalidad
   ```
5. Abre un **Pull Request**

### Guía de Estilo
- Usa nombres descriptivos para variables y funciones
- Comenta código complejo
- Sigue las convenciones de Kotlin
- Usa el formateador de Android Studio (Ctrl+Alt+L)

---

## 📝 Notas Importantes

### Seguridad
- **No compartas** las URLs de las APIs en repositorios públicos
- Las contraseñas se envían en texto plano (considera implementar hash)
- El token se guarda en `SharedPreferences` (considera usar EncryptedSharedPreferences)

### Limitaciones Conocidas
- El carrito no persiste al cerrar la app
- Las imágenes deben ser URLs externas (no se suben archivos)
- No hay sistema de estados de pedido (pendiente, enviado, etc.)
- No hay notificaciones push

### Mejoras Futuras
- [ ] Implementar estados de pedido (pendiente, enviado, entregado)
- [ ] Agregar filtros y búsqueda de productos
- [ ] Implementar sistema de favoritos
- [ ] Agregar pasarela de pago real
- [ ] Notificaciones push para pedidos
- [ ] Chat de soporte
- [ ] Sistema de reseñas y calificaciones
- [ ] Modo oscuro

---

## 📄 Licencia

Este proyecto es parte de un proyecto académico. Todos los derechos reservados.

---

## 👨‍💻 Desarrolladores

Desarrollado como proyecto de **Desarrollo de Aplicaciones Móviles**.

**Contacto:**
- GitHub: [@PacoJalao79](https://github.com/PacoJalao79)
- Repositorio: [Proyecto-app-moviles](https://github.com/PacoJalao79/Proyecto-app-moviles)

---

## 🙏 Agradecimientos

- **Android Developers** por la documentación oficial
- **Xano** por la plataforma backend
- **Material Design** por los componentes de UI
- **Stack Overflow** por resolver dudas infinitas 😄

---

## 📚 Recursos Adicionales

### Documentación Oficial
- [Android Developers](https://developer.android.com/)
- [Kotlin Language](https://kotlinlang.org/)
- [Material Design](https://m3.material.io/)
- [Retrofit](https://square.github.io/retrofit/)
- [Xano Documentation](https://docs.xano.com/)

### Tutoriales Recomendados
- [Android Basics in Kotlin](https://developer.android.com/courses/android-basics-kotlin/course)
- [Retrofit Tutorial](https://square.github.io/retrofit/)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)

---

<div align="center">

**⭐ Si te gusta este proyecto, dale una estrella en GitHub ⭐**

Hecho con ❤️ y ☕

</div>

