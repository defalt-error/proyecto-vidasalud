# Plataforma VidaSalud

**Asignatura:** Desarrollo Cloud Native I (DSY1107)  
**Institución:** Duoc UC  
**Caso:** Plataforma para la Gestión Unificada de Atenciones Médicas en Centros de Salud  

---

## 🏗️ Arquitectura del Sistema

Plataforma Cloud Native basada en arquitectura de microservicios desacoplados:

* **Frontend Angular (`frontend-vidasalud`)**: Interfaz web SPA con integración MSAL (Azure AD / Entra ID), guards de protección de rutas e interceptor para tokens Bearer.
* **Backend For Frontend (`ms-vidasalud-bff`)**: Spring Boot 3 + Spring Security configurado como OAuth2 Resource Server para la validación de tokens JWT de Azure AD y autorización RBAC.
* **Microservicio Atenciones (`ms-vidasalud-appointments`)**: Spring Boot 3 + Spring Data JPA para la gestión de citas médicas y reglas de negocio de cambio de estado.
* **Microservicio Catálogo (`ms-vidasalud-catalog`)**: Spring Boot 3 + Spring Data JPA para la gestión de aranceles, prestaciones médicas, cupos y boxes clínicos.

---

## 👥 Roles del Sistema (RBAC)

* **Admin**: Gestión global de la red y catálogo de prestaciones.
* **Recepcionista (Operador)**: Confirmación de atenciones, sala de espera y asignación de box.
* **Paciente (Cliente)**: Solicitud y seguimiento de atenciones médicas.
* **Auditor**: Trazabilidad y consulta de registros en modo solo lectura.