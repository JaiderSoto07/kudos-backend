# UCO Kudos — Documentación del proyecto

Plataforma Institucional de Reconocimientos de la Universidad Católica de Oriente.
Proyecto Integrador Final del curso **Diseño Orientado a Objetos**.

| Campo | Valor |
|---|---|
| Modalidad | Individual |
| Fecha límite | Viernes 13 de noviembre de 2026, 12:00 m. |
| Back-End | Java 26 + Spring Boot 4.1.1, JDBC + HikariCP, SQL Server (repositorio `kudos-backend`) |
| Front-End | Vue 3 + Vite + Pinia + Vue Router (repositorio `kudos-frontend`) |
| Autenticación | Google (OAuth2 / OpenID Connect), aprobada por el docente; cuenta, aprobación y rol en nuestra base de datos |
| Pruebas | JUnit 5 + JaCoCo (240), Vitest (112), Bruno (84 peticiones), SonarQube Cloud, ESLint + oxlint |
| Formato de documentación | Markdown (RNF-028) |

---

## Contenido

Misma organización de la documentación entregada en Sigma (modelo de dominio, diagrama de clases y MER), más el análisis, la arquitectura y los diagramas que exige este enunciado.

| # | Documento | Qué contiene | Estado |
|---|---|---|---|
| 0 | [Análisis](docs/00-analisis/requisitos.md) | [Requisitos](docs/00-analisis/requisitos.md), [catálogo de reglas de negocio](docs/00-analisis/reglas-de-negocio.md), [decisiones de diseño](docs/00-analisis/decisiones-de-diseno.md), [especificación de casos de uso](docs/00-analisis/casos-de-uso.md) y [matriz de trazabilidad](docs/00-analisis/matriz-trazabilidad.md) | ✅ (la matriz usa las clases y pruebas reales) |
| 1 | [Modelo de dominio](docs/01-modelo-dominio/README.md) | Diagrama de línea de vida, objetos, reglas de negocio, agregados, servicios, catálogo de eventos y, por objeto: muestreo, caracterización, responsabilidades, eventos y políticas | ✅ |
| 2 | [Diagrama de clases](docs/02-diagrama-clases/README.md) | Diagrama UML con los atributos y métodos reales y una página por clase: relaciones, atributos, constructores, accesores, métodos de negocio, operaciones del caso de uso y `Builder` | ✅ |
| 3 | [Modelo entidad-relación](docs/03-modelo-entidad-relacion/README.md) | Diagrama, diccionario de datos por entidad, scripts SQL y validación de restricciones | ✅ |
| 4 | [Diagrama de casos de uso](docs/04-diagrama-casos-uso/README.md) | Actores, generalizaciones y casos de uso por módulo | ✅ |
| 5 | [Arquitectura](docs/05-arquitectura/README.md), [patrones](docs/05-arquitectura/patrones.md) y [API REST](docs/05-arquitectura/api-rest.md) | Capas y paquetes reales, transacciones, errores, seguridad con Google, auditoría, front-end, SOLID, patrones justificados (y descartados) y los 64 endpoints | ✅ |
| 6 | [Diagramas de comportamiento](docs/06-diagramas-comportamiento/README.md) | 6 diagramas de estado, 3 de actividad y 5 de secuencia con las clases reales | ✅ |
| 7 | [Calidad y pruebas](docs/07-calidad-y-pruebas/README.md) | Estrategia, resultados de las pruebas, cobertura, análisis estático, 17 hallazgos con su corrección y evidencias | ✅ |
| — | [Manual de puesta a punto](manual-puesta-a-punto.md) | Requisitos previos, instalación, configuración (base de datos y Google), variables de entorno, ejecución, pruebas y despliegue | ✅ Probado |
| — | [Scripts SQL](sql) | Crear usuario, crear tablas, datos iniciales, datos de demostración y validación | ✅ Validados en SQL Server 2025 |

### Alcance de la versión entregada

Están implementados **todos** los requisitos funcionales del enunciado (RF-001 a RF-077) y sus casos de uso (CU-001 a CU-015), más los complementarios del análisis (registro, verificación, aprobaciones por ámbito, muro, revisión y liberación de contenido, notificaciones y auditoría). Cuatro requisitos complementarios que propusimos en el análisis quedan como **trabajo futuro**, marcados ⛔ en los [requisitos](docs/00-analisis/requisitos.md): historial de actividad propia (RF-082), suspender, reactivar o desactivar cuentas a mano (RF-089; la suspensión y reactivación automáticas sí existen), indicadores (RF-096) y pantalla de parámetros globales (RF-097).

Los diagramas usan los nombres **conceptuales** del dominio (`Kudo`, `Usuario`…); en el código cada clase de dominio lleva el sufijo `Dominio` (`KudoDominio`). La [tabla de correspondencia](docs/01-modelo-dominio/README.md#correspondencia-con-el-código) dice dónde vive cada objeto en cada capa.

### Correspondencia con el contenido mínimo del enunciado (doc. 09 §10 y §11)

| Sección exigida | Dónde está |
|---|---|
| 1–4. Introducción, contexto, objetivos y actores | Este archivo |
| 5. Modelo anémico | [Modelo de dominio](docs/01-modelo-dominio/README.md): diagrama de línea de vida + sección **Muestreo** de cada objeto |
| 6. Modelo enriquecido | Sección **Caracterización** de cada objeto: caracterización del objeto, de sus atributos y responsabilidades |
| 7. Eventos | Sección **Eventos** de cada objeto + [catálogo de eventos](docs/01-modelo-dominio/README.md#catálogo-de-eventos-de-dominio) |
| 8. Políticas | Sección **Políticas** de cada objeto + [catálogo de reglas de negocio](docs/00-analisis/reglas-de-negocio.md) (POL, INV y RN con la política que las garantiza) + [decisiones de diseño](docs/00-analisis/decisiones-de-diseno.md) |
| 9. Modelo de dominio | [Modelo de dominio](docs/01-modelo-dominio/README.md): agregados, entidades, servicios, validaciones y correspondencia con el código |
| 10. Diagramas UML | UML-001 [casos de uso](docs/04-diagrama-casos-uso/README.md), UML-002 [clases](docs/02-diagrama-clases/README.md), UML-003 a UML-005 [secuencia, actividad y estado](docs/06-diagramas-comportamiento/README.md) |
| 11. Modelo de datos | [MER](docs/03-modelo-entidad-relacion/README.md) |
| 12. Arquitectura | [Arquitectura](docs/05-arquitectura/README.md) |
| 13. Patrones utilizados | [Patrones](docs/05-arquitectura/patrones.md) |
| 14–16. Calidad, análisis estático y pruebas | [Calidad y pruebas](docs/07-calidad-y-pruebas/README.md) |
| 17. Conclusiones | Este archivo (§17) |

### Correspondencia con los requisitos de la documentación (doc. 09 §6)

| Requisito | Dónde está |
|---|---|
| DOC-001 Análisis completo del problema | §1–4 de este archivo + [requisitos](docs/00-analisis/requisitos.md), [casos de uso](docs/00-analisis/casos-de-uso.md) y [decisiones](docs/00-analisis/decisiones-de-diseno.md) |
| DOC-002 Identificación de requisitos | [Requisitos](docs/00-analisis/requisitos.md): RF-001 a RF-097 y RNF-001 a RNF-040 |
| DOC-003 Todas las reglas de negocio | [Catálogo de reglas de negocio](docs/00-analisis/reglas-de-negocio.md): RN-001 a RN-250, INV-001 a INV-035 y POL-001 a POL-007 |
| DOC-004 Modelo anémico | [Modelo de dominio](docs/01-modelo-dominio/README.md): diagrama + **Muestreo** de cada objeto |
| DOC-005 Modelo enriquecido | **Caracterización** y responsabilidades de cada objeto |
| DOC-006 Eventos del dominio | **Eventos** de cada objeto + [catálogo de eventos](docs/01-modelo-dominio/README.md#catálogo-de-eventos-de-dominio) + [EVT-001 a EVT-038](docs/00-analisis/reglas-de-negocio.md#4-eventos-de-referencia) |
| DOC-007 Políticas | **Políticas** de cada objeto (162) + [catálogo de reglas](docs/00-analisis/reglas-de-negocio.md) |
| DOC-008 Justificación de decisiones | [Decisiones de diseño](docs/00-analisis/decisiones-de-diseno.md) (D-01 a D-31) + [arquitectura](docs/05-arquitectura/README.md#12-resumen-de-decisiones-arquitectónicas) + [patrones](docs/05-arquitectura/patrones.md) |
| DOC-009 Diagramas UML | Casos de uso, clases, secuencia, actividad y estado (§10 de la tabla anterior) |
| DOC-010 Modelo de datos | [MER](docs/03-modelo-entidad-relacion/README.md) + [scripts SQL](sql) |

## Diagramas

| Diagrama | Imagen | Fuente editable (draw.io) |
|---|---|---|
| Modelo de dominio (línea de vida) | [SVG](assets/diagramas/modelo-dominio.svg) · [PNG](assets/diagramas/modelo-dominio.png) | [modelo-dominio.drawio](fuentes/modelo-dominio.drawio) |
| Diagrama de clases | [SVG](assets/diagramas/diagrama-clases.svg) · [PNG](assets/diagramas/diagrama-clases.png) | [diagrama-clases.drawio](fuentes/diagrama-clases.drawio) |
| Modelo entidad-relación | [SVG](assets/diagramas/mer.svg) · [PNG](assets/diagramas/mer.png) | [mer.drawio](fuentes/mer.drawio) |
| Casos de uso (5 páginas) | [Actores](assets/diagramas/casos-uso-1.svg) y 4 módulos (`casos-uso-2` a `casos-uso-5`) | [casos-uso.drawio](fuentes/casos-uso.drawio) |
| Arquitectura por capas | [SVG](assets/diagramas/arquitectura-capas.svg) · [PNG](assets/diagramas/arquitectura-capas.png) | [arquitectura-capas.drawio](fuentes/arquitectura-capas.drawio) |
| Estados (6) | `diagramas-estado-1` a `-6` | [diagramas-estado.drawio](fuentes/diagramas-estado.drawio) |
| Actividades (3) | `diagramas-actividad-1` a `-3` | [diagramas-actividad.drawio](fuentes/diagramas-actividad.drawio) |
| Secuencias (5) | `diagramas-secuencia-1` a `-5` | [diagramas-secuencia.drawio](fuentes/diagramas-secuencia.drawio) |

> Los SVG llevan el diagrama incrustado: también se abren y se editan en draw.io. Cada documento muestra además una versión en texto (Mermaid) plegable.

## Estructura de la entrega

```
documentacion/
├── README.md                          ← este archivo (secciones 1–4 y 17)
├── manual-puesta-a-punto.md           ← entregable 4
├── docs/
│   ├── 00-analisis/                   ← requisitos, reglas de negocio, decisiones, casos de uso, trazabilidad
│   ├── 01-modelo-dominio/             ← README + objetos/*.md (16 objetos)
│   ├── 02-diagrama-clases/            ← README + clases/*.md (16 clases)
│   ├── 03-modelo-entidad-relacion/    ← README + entidades/*.md (16 tablas)
│   ├── 04-diagrama-casos-uso/         ← actores y casos de uso por módulo
│   ├── 05-arquitectura/               ← arquitectura, patrones y API REST
│   ├── 06-diagramas-comportamiento/   ← estados, actividades y secuencias
│   └── 07-calidad-y-pruebas/          ← resultados, hallazgos y evidencias/ (capturas y salidas de las pruebas)
├── sql/                               ← scripts de base de datos y validación
├── assets/diagramas/                  ← imágenes de los diagramas (SVG y PNG)
└── fuentes/                           ← diagramas editables (.drawio)
```

---

## 1. Introducción

La Universidad Católica de Oriente (UCO) necesita una plataforma institucional donde los integrantes de su comunidad se reconozcan entre sí. A cada reconocimiento se le llama **Kudo**.

La plataforma permite:

- Publicar Kudos.
- Conversar sobre ellos mediante réplicas.
- Calificarlos.
- Reportarlos cuando son inapropiados.
- Que las autoridades los moderen.

Todo esto respeta la estructura organizacional de la Universidad: rector, coordinadores, facultades y programas. Además, queda trazabilidad completa de lo que ocurre.

Este documento reúne el análisis, el diseño y las decisiones que soportan la implementación.

## 2. Contexto del problema

Hoy los reconocimientos se hacen por correo, redes sociales, conversaciones informales o eventos. Esto produce cuatro problemas:

| Problema | Consecuencia |
|---|---|
| No hay trazabilidad | No se sabe quién reconoció a quién ni por qué |
| No hay historial | Se pierde la memoria institucional de los reconocimientos |
| No hay indicadores | No se puede medir la participación ni la cultura de reconocimiento |
| No hay estrategia unificada | Cada dependencia reconoce a su manera, sin control ni gobierno |

La situación deseada es una plataforma única que cumpla cinco condiciones:

- Reconoce personas, comparte reconocimientos y permite participar en conversaciones.
- Permite calificar y moderar el contenido.
- Mide la participación.
- Administra la estructura académica: facultades, programas y vínculos de docentes y estudiantes.
- Conserva todo con auditoría.

Hay además una condición de acceso. Cada persona entra con una **identidad institucional verificada**: el correo lo confirma Google al ingresar y el celular se valida con un código. Su cuenta debe estar **aprobada** por la autoridad que le corresponde según la jerarquía.

## 3. Objetivos

### 3.1 Objetivo general

Diseñar e implementar con orientación a objetos una plataforma completamente funcional (front-end, back-end, persistencia, seguridad y pruebas) para gestionar el proceso institucional de reconocimientos de la UCO.

### 3.2 Objetivos de la plataforma

| Objetivo | Qué significa en el sistema |
|---|---|
| Reconocimiento | Cualquier usuario activo puede otorgar un Kudo a otra persona de la comunidad |
| Visibilidad | Todo Kudo visible puede ser consultado por la comunidad autenticada |
| Participación | Réplicas en árbol ilimitado y calificaciones de 1 a 5 estrellas |
| Trazabilidad | Cada operación relevante genera un evento de dominio que queda auditado |
| Gobernanza | Aprobaciones jerárquicas, ámbitos de autoridad, reportes y vetos |

### 3.3 Objetivos académicos

| Área | Evidencia |
|---|---|
| Análisis | Requisitos, reglas, eventos, políticas e invariantes |
| Diseño | Modelo anémico → modelo enriquecido, entidades con comportamiento, patrón State, agregados y servicios |
| Arquitectura | Estilo por capas del curso, SOLID, bajo acoplamiento y alta cohesión |
| Calidad | Clean Code, refactorización, análisis estático y pruebas |

## 4. Actores

| Actor | Responsabilidad principal | Lo aprueba o registra | Gestiona | ¿Puede vetar? | ¿Gestiona categorías? |
|---|---|---|---|:---:|:---:|
| Administrador del Sistema | Administración técnica y parámetros globales | Otro Administrador | Administradores y Rectores | No | Sí |
| Rector | Gobierno institucional | Administrador | Coordinadores Académicos | **Sí** | Sí |
| Coordinador Académico | Gobierno académico de toda la Universidad | Rector | Coordinadores de Facultad (y estructura académica, ver D-04) | **Sí** | Sí |
| Coordinador de Facultad | Administración de **una** Facultad | Coordinador Académico | Coordinadores de Programa de su Facultad | No | No |
| Coordinador de Programa | Administración de **un** Programa | Coordinador de Facultad | Estudiantes y Docentes de su Programa | No | No |
| Docente | Participación académica en uno o más Programas | Coordinador de Programa (D-03) | Su información propia | No | No |
| Estudiante | Participación estudiantil en uno o más Programas | Coordinador de Programa | Su información propia | No | No |

Todos los actores con cuenta activa pueden:

- Publicar Kudos.
- Responder.
- Calificar.
- Reportar contenido.
- Consultar y editar su información personal.

Reglas transversales sobre los roles (RN-001 a RN-038):

- Una persona tiene **un único rol activo**.
- Los roles son **mutuamente excluyentes**.
- Solo existe **un** Rector activo y **un** Coordinador Académico activo.
- Cada Facultad y cada Programa tiene **un** coordinador activo.

---

## 17. Conclusiones

### 17.1 Sobre el análisis y el diseño

1. **El problema es más organizacional que técnico.** La mayor parte de las 250 reglas trata de quién puede hacer qué: roles excluyentes, cadena de aprobación, ámbitos de autoridad y moderación. Por eso el análisis empezó por la estructura de la Universidad y no por los Kudos.
2. **El enunciado tiene vacíos y contradicciones que hubo que resolver con criterio.** Por ejemplo: quién crea categorías (D-01), quién veta (D-02), quién aprueba a los Docentes (D-03) y quién crea Facultades (D-04). Cada caso quedó registrado con su justificación, de modo que el diseño se puede defender regla por regla.
3. **La mejor regla es la que la estructura hace imposible de violar.** Las 38 reglas de exclusividad de roles se cumplen con un solo rol vigente por persona (D-13). Los ciclos en la conversación son imposibles porque el padre de una réplica nunca cambia (D-22). Y un contenido vetado no puede volver a interactuar porque su estado (patrón State) rechaza toda transición (D-20).
4. **Pasar del modelo anémico al enriquecido cambió dónde viven las reglas.** En el anémico, el Kudo era una fila con un estado. En el enriquecido, el Kudo sabe si admite réplicas, cuándo puede ser vetado y qué evento produce. Las 84 responsabilidades y 162 políticas quedaron repartidas en los objetos que tienen la información para cumplirlas (Tell, Don't Ask).
5. **Se usaron patrones solo donde resolvían un problema concreto.** State para el ciclo de vida del contenido, Builder y Singleton como en Sigma, Abstract Factory para compartir la transacción, Facade, DAO y DTO + Assembler entre capas. Otros que aparecían en el diseño inicial (Observer, Composite, objetos de valor, Specification) se **descartaron al implementar** porque una solución más simple resolvía lo mismo; quedó registrado el motivo.
6. **La trazabilidad se diseñó desde el principio.** Cada operación registra su evento de dominio en la auditoría dentro de la misma transacción, y nada se borra físicamente (D-26, D-28). Así se cumplen RN-096 a RN-100 y POL-007: no existe una operación sin su registro ni un registro de algo que no pasó.
7. **La base de datos es una segunda línea de defensa.** Los índices únicos filtrados y los `CHECK` garantizan las reglas críticas aunque falle el código o haya concurrencia. Se comprobó con 16 de 16 violaciones rechazadas en SQL Server.
8. **Mantener la arquitectura del curso facilitó el diseño sin limitarlo.** Las capas de Sigma se conservaron y solo se cambió lo que el enunciado exigía: un dominio con comportamiento. Así la solución se puede explicar y modificar en vivo.

### 17.2 Sobre la implementación y las pruebas

1. **Construir por módulos completos funcionó.** Cada módulo (identidad, aprobaciones, organización, Kudos, conversación, moderación, auditoría y notificaciones) se terminó de punta a punta: reglas, negocio, fachada, controlador, colección de Bruno, pantallas y pruebas, y se verificó contra la base real antes de seguir. Así nunca quedó una capa a medias.
2. **El diseño se simplificó al implementar, y eso quedó documentado.** Observer, Composite, objetos de valor y Specification resolvían problemas que, en el código, se resolvían más simple con la arquitectura del curso. La documentación se ajustó para decir lo que el código hace; los patrones descartados quedan con su razón.
3. **Las pruebas sin base de datos dieron velocidad y confianza.** Con la `DAOFactory` inyectada, las 240 pruebas del back-end corren en segundos y cubren el 99,7 % de las líneas medidas. Las pruebas del front (112) recorren las pantallas completas contra un servidor falso.
4. **El análisis estático encontró problemas reales.** SonarQube marcó posibles nulos, duplicación y accesos fuera de rango; cada hallazgo se corrigió con una refactorización concreta (17 hallazgos registrados), y ESLint y oxlint mantuvieron el front sin errores.
5. **La base de datos y las pruebas contra la base real atraparon lo que las pruebas unitarias no ven**, como un aviso que no cabía en su columna; se corrigió en un solo lugar para todos los módulos.
6. **Delegar la autenticación en Google fue la decisión correcta.** El sistema no guarda contraseñas y las reglas de la cuenta (aprobación, verificación del celular, rol y ámbito) siguen en nuestro dominio, como exige el enunciado.
7. **Trabajo futuro:** historial de actividad propia, gestión manual de cuentas, tablero de indicadores y pantalla de parámetros (requisitos complementarios marcados ⛔), y un proveedor real de SMS detrás de la interfaz `EnviadorNotificacion`.
