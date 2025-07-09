# BioReadSpring

**BioReadSpring** es un sistema de gestión de asistencia desarrollado con Spring Boot y Thymeleaf. Permite registrar entradas y salidas de usuarios, gestionar registros desde un panel administrativo, generar reportes y controlar el acceso según roles.

---

##  Tecnologías utilizadas

- Java 17+
- Spring Boot
- Thymeleaf
- Spring Security
- Bootstrap 5
- MySQL 
- JPA (Hibernate)

---

##  Estructura del proyecto

```plaintext
src/
├── main/
│   ├── java/com/example/BioRead/
│   │   ├── config/        # Seguridad y configuración
│   │   ├── controller/    # Controladores MVC
│   │   ├── model/         # Entidades JPA
│   │   ├── repository/    # Interfaces de persistencia
│   │   └── service/       # Servicios y lógica de negocio
│   └── resources/
│       ├── templates/     # Vistas Thymeleaf
│       └── application.properties
```
---

##  Roles soportados

- **Administrador**: Accede a todas las funcionalidades del sistema (ver, crear, editar, eliminar registros).
- **Usuario**: Puede ver su historial y marcar su asistencia diaria.

---

##  Funcionalidades principales

- Registro de entrada y salida de usuarios
- Registro manual desde el panel de administrador
- Filtro de registros por nombre o ID
- Exportación de datos a Excel
- Vista de dashboard con resumen y gráficos (opcional)
- CRUD completo de registros de asistencia
- Control de acceso por rol (admin / usuario)
- Diseño responsivo con Bootstrap

---

##  Requisitos previos

- Java 17 o superior
- Maven
- MySQL o MariaDB

---

##  Cómo ejecutar el proyecto

1. Clona el repositorio:

   ```bash
   git clone https://github.com/Andres55207/BioReadSpring.git
   cd BioReadSpring

