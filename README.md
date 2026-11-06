# Sistema de Gestión Meteorológica — API REST con Spring Boot y JPA

Proyecto desarrollado como solución al **Taller Guiado** y **Taller Complementario** de desarrollo backend con Spring Boot, Spring Data JPA, PostgreSQL y Maven.

---

## 🛠️ Tecnologías Utilizadas

- **Java 17** (LTS)
- **Spring Boot 4.x** (Web MVC, Data JPA)
- **Hibernate / JPA** (Mapeo objeto-relacional)
- **PostgreSQL** (Base de datos relacional en producción/laboratorio)
- **H2 Database** (Base de datos en memoria para pruebas automáticas y ejecución sin dependencias)
- **Maven** (Gestor de dependencias mediante Maven Wrapper `mvnw`)

---

## 🏛️ Modelo de Datos y Relaciones JPA

El modelo implementa las siguientes relaciones relacionales:

1. **Estación ↔ Configuración (1:1):** `@OneToOne` y `@JoinColumn(name = "configuracion_id", unique = true)` con `orphanRemoval = true`.
2. **Ciudad ↔ Estación (1:N):** `@OneToMany(mappedBy = "ciudad")` y `@ManyToOne(name = "ciudad_id")`.
3. **Estación ↔ RegistroClima (1:N):** `@OneToMany(mappedBy = "estacion")` y `@ManyToOne(name = "estacion_id")`.
4. **Usuario ↔ Rol (N:M):** `@ManyToMany` con tabla intermedia `usuario_rol`.
5. **RegistroClima ↔ TipoMedicion (N:M):** `@ManyToMany` con tabla intermedia `registro_clima_tipo_medicion`.
6. **RegistroClima ↔ Alerta (1:N):** `@OneToMany(mappedBy = "registroClima")` y `@ManyToOne(name = "registro_clima_id")`.

---

## 🚀 Cómo Ejecutar el Proyecto

### 1. Clonar el repositorio
```bash
git clone https://github.com/miguelvictoria59385-crypto/Taller_Api.git
cd Taller_Api/clima
```

### 2. Configurar Base de Datos
En `src/main/resources/application.properties` puedes elegir:
- **PostgreSQL (por defecto):** Requiere una base de datos creada llamada `clima`. Ajusta la contraseña en `spring.datasource.password`.
- **H2 (en memoria, cero configuración):** Comenta las líneas de PostgreSQL y descomenta las líneas de H2 indicadas en el archivo.

### 3. Iniciar la aplicación
- **En Windows:**
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```
- **En Linux / macOS:**
  ```bash
  chmod +x mvnw
  ./mvnw spring-boot:run
  ```

El servidor iniciará en: `http://localhost:8081`

### 4. Ejecutar Pruebas Automatizadas
Para verificar todo el flujo y consultas JPQL:
```bash
.\mvnw.cmd test    # En Windows
./mvnw test        # En Linux/Mac
```

---

## 📡 Endpoints de la API REST

| Recurso | Método | Endpoint | Descripción |
| :--- | :--- | :--- | :--- |
| **Ciudades** | `GET` / `POST` | `/api/ciudades` | Listar y crear ciudades |
| | `GET` / `PUT` / `DELETE` | `/api/ciudades/{id}` | Consultar, actualizar y eliminar ciudad |
| | `GET` | `/api/ciudades/{id}/temperatura-promedio` | **Reto:** Promedio de temperatura por ciudad (`@Query`) |
| **Estaciones** | `GET` / `POST` | `/api/estaciones` | Listar y registrar estación (con configuración 1:1) |
| | `GET` / `PUT` / `DELETE` | `/api/estaciones/{id}` | Consultar, actualizar y eliminar estación |
| | `GET` | `/api/estaciones/ciudad/{id}` | Estaciones de una ciudad específica |
| | `PUT` | `/api/estaciones/{id}/configuracion` | Actualizar configuración de la estación |
| **Mediciones** | `GET` / `POST` | `/api/mediciones` | Listar y registrar mediciones climáticas |
| | `GET` / `PUT` / `DELETE` | `/api/mediciones/{id}` | Consultar, actualizar y eliminar medición |
| | `GET` | `/api/mediciones/estacion/{id}` | Mediciones capturadas por una estación |
| | `GET` | `/api/mediciones/ciudad/{id}` | Mediciones de una ciudad |
| | `GET` | `/api/mediciones/rango-fechas` | Filtrar mediciones por rango de fechas |
| | `GET` | `/api/mediciones/temperatura-alta?min=30.0` | **Reto:** Mediciones con temp > 30°C (`@Query`) |
| | `GET` | `/api/mediciones/promedio/ciudad/{ciudadId}` | **Reto:** Promedio de temperatura JPQL |
| **Tipos Medición** | `GET` / `POST` / `PUT` / `DELETE` | `/api/tipos-medicion` y `/{id}` | Catálogo de tipos de medición |
| **Roles** | `GET` / `POST` / `PUT` / `DELETE` | `/api/roles` y `/{id}` | Catálogo de roles del sistema |
| **Usuarios** | `GET` / `POST` / `PUT` / `DELETE` | `/api/usuarios` y `/{id}` | Gestión de usuarios |
| | `POST` / `DELETE` | `/api/usuarios/{id}/roles/{rolId}` | Asignar / remover rol a usuario (`N:M`) |
| | `GET` | `/api/usuarios/rol/{nombre}` | Consultar usuarios por rol (`@Query`) |
| **Alertas** | `GET` / `POST` / `PUT` / `DELETE` | `/api/alertas` y `/{id}` | Gestión de alertas climáticas |
| | `GET` | `/api/alertas/nivel/{nivel}` | Consultar alertas por nivel |
| | `GET` | `/api/alertas/activas` | Listar alertas activas |
| | `GET` | `/api/alertas/activas/ciudad/{ciudadId}` | **Reto:** Alertas activas por ciudad (`@Query`) |

---

## 📂 Recursos de Entrega
- **Script SQL:** [`clima/sql/script_modelo_meteorologico.sql`](clima/sql/script_modelo_meteorologico.sql)
- **Colección Postman:** [`clima/postman/Clima_API_Collection.postman_collection.json`](clima/postman/Clima_API_Collection.postman_collection.json)
