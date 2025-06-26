# 🧾 Microservicio de Pagos y Cupones

Este microservicio forma parte de un sistema distribuido basado en microservicios. Su función principal es gestionar pagos de cursos, permitiendo la aplicación de cupones de descuento válidos.

## 📦 Funcionalidades

- Registrar pagos de cursos.
- Asociar cupones a pagos.
- Consultar pagos individuales o en listado.
- Gestionar cupones (crear, listar, buscar por ID).
- Respuestas enriquecidas con HATEOAS.
- Documentación OpenAPI vía Swagger.

## 🔧 Tecnologías Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring HATEOAS
- MySQL
- Docker / Docker Compose
- Faker (datos de prueba)
- JUnit 5 y Mockito (pruebas unitarias)

## 🚀 Endpoints Principales

- `/api/v2/pagos`: listado y creación de pagos
- `/api/v2/pagos/{id}`: detalle de pago
- `/api/v2/cupones`: listado de cupones
- `/api/v2/cupones/{id}`: detalle de cupon

## 📄 Documentación Swagger
http://localhost:8080/swagger-ui/index.html

## 📄 Documentación completa
https://edutech-1.gitbook.io/edutech-documentacion
