# UCO Kudos — Back-End

Back-end de la **Plataforma Institucional de Reconocimientos de la Universidad Católica de Oriente (UCO Kudos)**. Proyecto Integrador Final del curso Diseño Orientado a Objetos.

| | |
|---|---|
| Lenguaje | Java 26 |
| Framework | Spring Boot 4.1.1 (web MVC y security con OAuth2 / Google) |
| Persistencia | JDBC + HikariCP sobre SQL Server, sin JPA |
| Arquitectura | Capas del curso: `controlador → fachada → negocio → dao`, con dominio enriquecido |
| API | REST en `/api/v1` (64 endpoints) |
| Pruebas | JUnit 5 + JaCoCo (240 pruebas), colección Bruno (84 peticiones), SonarQube Cloud |

## Contenido del repositorio

| Carpeta | Qué contiene |
|---|---|
| `src/main/java/co/edu/uco/kudos` | Código fuente: `configuracion`, `controlador`, `negocio` (fachada, negocio, reglas, ensambladores), `dominio`, `dto`, `entidad`, `dao`, `transversal` |
| `src/test/java` | Pruebas JUnit del dominio, las reglas, el negocio (con DAO falsos en memoria) y los ensambladores |
| `bruno/Kudos` | Colección de Bruno para probar la API, una carpeta por módulo |
| [`documentacion/`](documentacion/README.md) | Documentación completa: análisis, modelo de dominio, diagramas UML, modelo de datos, arquitectura, [API REST](documentacion/docs/05-arquitectura/api-rest.md), calidad y pruebas, scripts SQL y [manual de puesta a punto](documentacion/manual-puesta-a-punto.md) |
| `.github/workflows` | Integración continua: compila, prueba con JaCoCo y envía el análisis a SonarQube Cloud |

## Dependencias

Están definidas en `pom.xml` (Maven, con el *wrapper* `mvnw` incluido, así que no hace falta instalar Maven): `spring-boot-starter`, `spring-boot-starter-webmvc`, `spring-boot-starter-security-oauth2-client`, `mssql-jdbc`, `HikariCP`, `spring-boot-starter-test` y los plugins `spring-boot-maven-plugin` y `jacoco-maven-plugin`.

## Ejecución rápida

Requisitos: JDK 26, SQL Server con la base `Kudos` creada (scripts en `documentacion/sql`) y un cliente OAuth de Google. El paso a paso desde cero está en el [manual de puesta a punto](documentacion/manual-puesta-a-punto.md).

1. Cree `src/main/resources/basedatos.properties` (conexión a SQL Server) y `src/main/resources/oauth.properties` (cliente de Google). No se suben al repositorio; el contenido está en el manual (§3).
2. Ejecute:

```powershell
.\mvnw.cmd spring-boot:run
```

El back-end queda en `http://localhost:8080`. Sin sesión, `http://localhost:8080/api/v1/kudos` responde 401.

## Pruebas

```powershell
.\mvnw.cmd test
```

Resultado esperado: `Tests run: 240, Failures: 0, Errors: 0`. El informe de cobertura queda en `target/site/jacoco/index.html`.
