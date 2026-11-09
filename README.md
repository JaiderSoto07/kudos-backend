# UCO Kudos — Back-End

Back-end de la **Plataforma Institucional de Reconocimientos de la Universidad Católica de Oriente (UCO Kudos)**. Proyecto Integrador Final del curso Diseño Orientado a Objetos.

| | |
|---|---|
| Lenguaje | Java 26 |
| Framework | Spring Boot (web y security) |
| Persistencia | JDBC + HikariCP sobre SQL Server, sin JPA |
| Arquitectura | Capas del curso: `controlador → fachada → negocio → dao`, con dominio enriquecido |
| Pruebas | JUnit 5 + JaCoCo; colección Bruno para la API |

> **Estado:** en construcción.

## Contenido del repositorio

| Carpeta | Qué contiene |
|---|---|
| `src/` | Código fuente y pruebas *(próximamente)* |
| [`documentacion/`](documentacion/README.md) | Documentación del proyecto: análisis, modelo de dominio, diagramas UML, modelo de datos, arquitectura, scripts SQL y [manual de puesta a punto](documentacion/manual-puesta-a-punto.md) |
| `bruno/` | Colección de pruebas de la API *(próximamente)* |
