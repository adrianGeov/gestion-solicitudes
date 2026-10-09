# API Gestión de Solicitudes y Centros de Costo

API REST desarrollada con **Java 17 y Spring Boot** para administrar **centros de costo** y sus **solicitudes**. Incluye paginación, filtros dinámicos, seguridad con JWT y roles, auditoría y consumo de un servicio externo.

---

## Tabla de contenido

1. [Tecnologías](#tecnologías)
2. [Requisitos previos](#requisitos-previos)
3. [Cómo levantar el proyecto](#cómo-levantar-el-proyecto)
4. [Variables de entorno](#variables-de-entorno)
5. [Documentación Swagger](#documentación-swagger)
6. [Autenticación](#autenticación)
7. [Endpoints](#endpoints)
8. [Paginación, ordenamiento y filtros](#paginación-ordenamiento-y-filtros)
9. [Manejo de errores](#manejo-de-errores)
10. [Arquitectura](#arquitectura)
11. [Decisiones técnicas](#decisiones-técnicas)
12. [Observabilidad](#observabilidad)
13. [Pruebas de la API](#pruebas-de-la-api)

---

## Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17+ | Lenguaje |
| Spring Boot | 4.1.1 | Framework base |
| Spring Web MVC | — | API REST y `RestClient` |
| Spring Data JPA / Hibernate | 7.x | Persistencia |
| PostgreSQL | 14+ | Base de datos |
| Spring Security + jjwt | 0.12.6 | Autenticación JWT y roles |
| Bean Validation | — | Validación de entradas |
| springdoc-openapi | 3.1.1 | Documentación OpenAPI / Swagger UI |
| Spring Boot Actuator | — | Health check e info |
| Lombok | — | Reducción de código repetitivo |
| Maven (Wrapper incluido) | 3.9+ | Construcción |

---

## Requisitos previos

- **JDK 17 o superior**. Verificar con `java -version`.
- **PostgreSQL 14 o superior** en ejecución.
- **Git**.
- No es necesario instalar Maven: el proyecto incluye **Maven Wrapper** (`mvnw` / `mvnw.cmd`).

---

## Cómo levantar el proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/adrianGeov/gestion-solicitudes.git
cd gestion-solicitudes
```

### 2. Crear la base de datos

En PostgreSQL (pgAdmin o psql):

```sql
CREATE DATABASE solicitudes_db;
```

Las tablas se crean automáticamente al iniciar la aplicación (`spring.jpa.hibernate.ddl-auto=update`).

### 3. Configurar la conexión

Por defecto la aplicación se conecta a:

| Parámetro | Valor por defecto |
|---|---|
| URL | `jdbc:postgresql://localhost:5433/solicitudes_db` |
| Usuario | `postgres` |
| Contraseña | `postgres` |

Si tu instalación es distinta (por ejemplo, puerto `5432`), define las [variables de entorno](#variables-de-entorno) o ajusta `src/main/resources/application.properties`.

### 4. Ejecutar

**Windows (PowerShell):**
```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
./mvnw spring-boot:run
```

La API queda disponible en **http://localhost:8080**.

### 5. Verificar

```
GET http://localhost:8080/actuator/health   →   {"status":"UP"}
```

### Generar el JAR (opcional)

```bash
./mvnw clean package
java -jar target/gestion-solicitudes-0.0.1-SNAPSHOT.jar
```

---

## Variables de entorno

Todas las propiedades sensibles se pueden sobrescribir con variables de entorno, sin modificar el código:

| Variable | Descripción | Valor por defecto (solo desarrollo) |
|---|---|---|
| `DB_URL` | URL JDBC de PostgreSQL | `jdbc:postgresql://localhost:5433/solicitudes_db` |
| `DB_USERNAME` | Usuario de la BD | `postgres` |
| `DB_PASSWORD` | Contraseña de la BD | `postgres` |
| `JWT_SECRET` | Clave de firma JWT en Base64 (mínimo 32 bytes) | Clave de desarrollo |
| `ADMIN_PASSWORD` | Contraseña del usuario `admin` | `Admin123*` |
| `USER_PASSWORD` | Contraseña del usuario `usuario` | `Usuario123*` |

Ejemplo en PowerShell:
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/solicitudes_db"
$env:DB_PASSWORD="mi_password"
.\mvnw.cmd spring-boot:run
```


---

## Documentación Swagger

| Recurso | URL |
|---|---|
| **Swagger UI** | **http://localhost:8080/swagger-ui.html** |
| Especificación OpenAPI (JSON) | http://localhost:8080/v3/api-docs |

**Para probar endpoints protegidos desde Swagger:**
1. Ejecutar `POST /api/v1/auth/login` con un usuario de prueba.
2. Copiar el valor de `token` de la respuesta.
3. Dar clic en el botón **Authorize** (arriba a la derecha), pegar el token y aceptar.

---

## Autenticación

La API usa **JWT (Bearer Token)**. Todas las rutas requieren token, excepto login, Swagger y Actuator.

### Usuarios de prueba

| Usuario | Contraseña | Roles | Permisos |
|---|---|---|---|
| `admin` | `Admin123*` | ADMIN, USER | Consultar, crear, actualizar y **eliminar** |
| `usuario` | `Usuario123*` | USER | Consultar, crear y actualizar |

### Obtener token

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "Admin123*"
}
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "expiraEnSegundos": 3600
}
```

### Usar el token

```http
GET /api/v1/centros-costo
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## Endpoints

### Autenticación

| Método | Ruta | Descripción | Acceso |
|---|---|---|---|
| POST | `/api/v1/auth/login` | Obtener token JWT | Público |

### Centros de Costo

| Método | Ruta | Descripción | Respuesta | Acceso |
|---|---|---|---|---|
| POST | `/api/v1/centros-costo` | Crear | 201 | Autenticado |
| GET | `/api/v1/centros-costo/{id}` | Consultar por id | 200 | Autenticado |
| GET | `/api/v1/centros-costo` | Listar paginado y filtrado | 200 | Autenticado |
| PUT | `/api/v1/centros-costo/{id}` | Actualizar | 200 | Autenticado |
| DELETE | `/api/v1/centros-costo/{id}` | Eliminación lógica | 204 | **Solo ADMIN** |

### Solicitudes

| Método | Ruta | Descripción | Respuesta | Acceso |
|---|---|---|---|---|
| POST | `/api/v1/solicitudes` | Crear | 201 | Autenticado |
| GET | `/api/v1/solicitudes/{id}` | Consultar por id | 200 | Autenticado |
| GET | `/api/v1/solicitudes` | Listar paginado y filtrado | 200 | Autenticado |
| PUT | `/api/v1/solicitudes/{id}` | Actualizar (solo PENDIENTE) | 200 | Autenticado |
| DELETE | `/api/v1/solicitudes/{id}` | Eliminación lógica | 204 | **Solo ADMIN** |

### Usuarios Externos (JSONPlaceholder)

| Método | Ruta | Descripción | Acceso |
|---|---|---|---|
| GET | `/api/v1/usuarios-externos` | Listar usuarios del servicio externo | Autenticado |
| GET | `/api/v1/usuarios-externos/{id}` | Consultar usuario externo | Autenticado |

### Actuator

| Método | Ruta | Descripción | Acceso |
|---|---|---|---|
| GET | `/actuator/health` | Estado de la aplicación y la BD | Público |
| GET | `/actuator/info` | Información de la aplicación | Público |

---

## Paginación, ordenamiento y filtros

Los listados aceptan parámetros estándar de paginación:

| Parámetro | Descripción | Ejemplo |
|---|---|---|
| `page` | Número de página (inicia en 0) | `page=0` |
| `size` | Registros por página (máximo 100) | `size=10` |
| `sort` | Campo y dirección | `sort=monto,desc` |

### Filtros de Solicitudes (opcionales y combinables)

| Parámetro | Tipo | Descripción |
|---|---|---|
| `titulo` | texto | Búsqueda parcial, sin distinguir mayúsculas |
| `estatus` | `PENDIENTE` \| `APROBADA` \| `RECHAZADA` | Estatus exacto |
| `centroCostoId` | número | Solicitudes de un centro de costo |
| `fechaDesde` | `yyyy-MM-dd` | Fecha de solicitud mayor o igual |
| `fechaHasta` | `yyyy-MM-dd` | Fecha de solicitud menor o igual |

Ejemplo:
```
GET /api/v1/solicitudes?estatus=PENDIENTE&centroCostoId=1&fechaDesde=2026-01-01&fechaHasta=2026-12-31&page=0&size=10&sort=monto,desc
```

### Filtro de Centros de Costo

| Parámetro | Descripción |
|---|---|
| `nombre` | Búsqueda parcial por nombre |

### Formato de respuesta paginada

```json
{
  "content": [ ... ],
  "page": 0,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3,
  "first": true,
  "last": false
}
```

---

## Manejo de errores

Todos los errores responden con el mismo formato JSON:

```json
{
  "timestamp": "2026-10-09T01:30:15.123",
  "status": 400,
  "error": "Bad Request",
  "message": "Error de validación en los datos enviados",
  "path": "/api/v1/centros-costo",
  "details": [
    "codigo: El código es obligatorio",
    "nombre: El nombre es obligatorio"
  ]
}
```

| Código | Cuándo ocurre |
|---|---|
| **200** OK | Consulta o actualización exitosa |
| **201** Created | Recurso creado (incluye header `Location`) |
| **204** No Content | Eliminación exitosa |
| **400** Bad Request | Validación fallida, JSON mal formado o parámetro con tipo inválido |
| **401** Unauthorized | Sin token, token inválido o expirado, o credenciales incorrectas |
| **403** Forbidden | Usuario sin el rol requerido (ej. USER intentando eliminar) |
| **404** Not Found | Recurso inexistente o ruta no válida |
| **405** Method Not Allowed | Verbo HTTP no soportado en la ruta |
| **409** Conflict | Regla de negocio violada (código duplicado, solicitud no PENDIENTE, centro con solicitudes activas, rango de fechas inválido) |
| **500** Internal Server Error | Error inesperado (el detalle solo se registra en el log, nunca se expone) |
| **503** Service Unavailable | El servicio externo no responde o falla |

### Reglas de negocio

- El **código** del centro de costo es único y se guarda en mayúsculas.
- No se puede eliminar un centro de costo con **solicitudes activas**.
- Solo se pueden modificar solicitudes en estatus **PENDIENTE**.
- Una solicitud solo puede asociarse a un centro de costo **activo**.
- Las eliminaciones son **lógicas** (`activo = false`): los registros se conservan para auditoría.

---

## Arquitectura

Arquitectura en capas con separación estricta de responsabilidades:

```
Cliente ──► Controller ──► Service ──► Repository ──► PostgreSQL
               │              │
              DTO          Mapper ◄──► Entity
```

### Estructura de paquetes

```
src/main/java/com/evaluacion/gestion_solicitudes/
├── client/          Cliente HTTP para el servicio externo (RestClient)
├── config/          Configuración: Security, OpenAPI, RestClient, filtro de logging
├── controller/      Endpoints REST (sin lógica de negocio)
├── dto/             Records de request/response (las entidades nunca se exponen)
├── entity/          Entidades JPA y enums
├── exception/       Excepciones personalizadas y @RestControllerAdvice
├── mapper/          Conversión Entidad ↔ DTO
├── repository/      Repositorios JPA y Specifications
├── security/        JWT, filtro de autenticación, usuario autenticado
└── service/         Interfaces de negocio
    └── impl/        Implementaciones transaccionales
```

### Modelo de datos

```
┌─────────────────────┐          ┌──────────────────────────┐
│    centro_costo     │          │        solicitud         │
├─────────────────────┤          ├──────────────────────────┤
│ id (PK)             │ 1      * │ id (PK)                  │
│ codigo (UNIQUE)     │──────────│ centro_costo_id (FK)     │
│ nombre              │          │ titulo                   │
│ descripcion         │          │ descripcion              │
│ activo              │          │ monto                    │
│ fecha_creacion      │          │ estatus                  │
│ fecha_actualizacion │          │ fecha_solicitud          │
│ creado_por          │          │ activo                   │
│ modificado_por      │          │ fecha_creacion           │
└─────────────────────┘          │ fecha_actualizacion      │
                                 │ creado_por               │
                                 │ modificado_por           │
                                 └──────────────────────────┘
```

---

## Decisiones técnicas

### API REST
- Verbos HTTP semánticos y códigos de estado adecuados; `POST` devuelve `201` con header `Location`.
- **DTOs con `record`** (inmutables) separados en Request y Response: el cliente no puede enviar `id`, `activo`, fechas ni campos de auditoría.
- Validación con **Bean Validation** (`@Valid`, `@NotBlank`, `@Size`, `@DecimalMin`, `@Digits`, etc.).
- Respuesta paginada propia (`PageResponse`) en lugar de exponer la estructura interna de Spring Data.

### Arquitectura y Clean Code
- **Inyección por constructor** (`@RequiredArgsConstructor` + campos `final`), sin `@Autowired` en atributos.
- Services definidos por **interfaz + implementación** (inversión de dependencias).
- **Mappers dedicados** como `@Component`: una sola responsabilidad y fácil de probar.
- Uso correcto de **`Optional`** con `orElseThrow`, nunca `.get()` sin validar.
- `BigDecimal` para montos y `enum` con `EnumType.STRING` para estatus.

### Persistencia y rendimiento
- Relación **uno a muchos / muchos a uno** con `FetchType.LAZY`.
- **`@Transactional(readOnly = true)`** a nivel de clase y `@Transactional` en las operaciones de escritura.
- **Filtros dinámicos** con JPA `Specification` (Criteria API).
- **Prevención de SQL Injection**: todas las consultas son parametrizadas (métodos derivados, `@Query` con `@Param` y Criteria API); no hay concatenación de SQL.
- **Prevención de N+1**: `JOIN FETCH` en la consulta por id y `@EntityGraph` en el listado paginado.
- **HikariCP** configurado (`maximum-pool-size`, `minimum-idle`, `connection-timeout`).
- Tamaño máximo de página limitado a 100.

### Consumo de servicio externo
- `RestClient` con **timeout de conexión (3 s)** y **timeout de lectura (5 s)** configurables.
- Errores HTTP, timeouts y fallas de conexión se traducen a respuestas controladas (404 / 503); la API nunca se queda colgada.

### Seguridad
- **JWT** firmado con HMAC-SHA256 y expiración configurable (1 hora por defecto).
- Sesión **stateless**; CSRF deshabilitado por ser una API sin cookies.
- **Restricción por rol**: solo `ADMIN` puede eliminar.
- Contraseñas cifradas con **BCrypt**.
- **Auditoría**: `creadoPor` y `modificadoPor` se obtienen del usuario autenticado en la capa de servicio.
- Respuestas 401 y 403 con el mismo formato JSON de errores.
- `/actuator/health` solo muestra detalles a usuarios autenticados.

---

## Observabilidad

### Actuator
- `GET /actuator/health`: estado de la aplicación y de la conexión a PostgreSQL.
- `GET /actuator/info`: nombre, descripción, versión y versión de Java.

### Logging
- Niveles adecuados: **INFO** para operaciones exitosas, **WARN** para errores del cliente y reglas violadas, **ERROR** para fallas del servidor o servicios externos.
- Cada petición recibe un **`requestId`** (header `X-Request-Id`) que aparece en todos sus logs, para rastrearla de principio a fin.
- Log de acceso por petición: método, ruta, status y duración.
- **Consola** en formato legible; **archivo** `logs/gestion-solicitudes.log` en **JSON estructurado (ECS)**.
- **Nunca se registran** tokens, contraseñas, headers ni cuerpos de petición.

---

## Pruebas de la API

El archivo **`requests.http`** en la raíz del proyecto contiene todas las peticiones listas para ejecutar, incluyendo casos de error y de seguridad.

**Uso en VS Code:**
1. Instalar la extensión **REST Client** (Huachao Mao).
2. Levantar la aplicación.
3. Abrir `requests.http` y ejecutar primero los dos **login** (clic en *Send Request*).
4. Ejecutar el resto de las peticiones **en orden**. Los tokens y los ids creados se reutilizan automáticamente.

**Uso en IntelliJ IDEA:** el formato `.http` es compatible con su cliente HTTP integrado.

---

## Autor

**Adrián Tolentino** — adriant.masariego96@gmail.com
