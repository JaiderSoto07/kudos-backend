# Calidad del software, análisis estático y pruebas

[Inicio](../../README.md) · [Arquitectura](../05-arquitectura/README.md) · [Matriz de trazabilidad](../00-analisis/matriz-trazabilidad.md)

Cubre las secciones 14 (Calidad del software), 15 (Análisis estático) y 16 (Pruebas) del contenido mínimo, y los requisitos RNF-018 a RNF-027, ARQ-068 a ARQ-073 y CAL-001 a CAL-007.

> **Estado.** La **estrategia** (qué se prueba, con qué herramientas y con qué criterios) está definida. Los **resultados** del análisis estático y de las pruebas del back-end y del front-end se registran en las secciones marcadas como ⏳ a medida que se implementa. La validación de la base de datos ya se ejecutó y es la primera evidencia (§5).

---

## 1. Atributos de calidad y cómo se aseguran

| Atributo (CAL) | Cómo se asegura en el diseño | Cómo se verifica |
|---|---|---|
| Legibilidad (CAL-001) | Lenguaje del dominio en español, métodos cortos, Builder, mensajes en `CatalogoMensajes` | Análisis estático (*code smells*), revisión de código |
| Mantenibilidad (CAL-002) | Capas con dependencias en un sentido; una regla por clase; State y Observer | Duplicación y complejidad ciclomática en el análisis estático |
| Reutilización (CAL-003) | Objetos de valor, políticas (Specification), DAO genéricos y componentes `Base*` en Vue | Revisión de código |
| Acoplamiento (CAL-004) | Interfaces entre capas, eventos de dominio, `DAOFactory` | Revisión de dependencias entre paquetes |
| Cohesión (CAL-005) | Cada objeto protege sus reglas (modelo enriquecido) | Revisión del modelo contra el código |
| Testabilidad (CAL-006) | Dominio en Java puro; negocio con `DAOFactory` inyectada | Cobertura con JaCoCo |
| Evolución (CAL-007) | OCP en estados, suscriptores y fábrica de DAO | Ejercicios de cambio en la sustentación |

---

## 2. Estrategia de pruebas (ARQ-069)

La estrategia sigue una **pirámide**: muchas pruebas rápidas del dominio, menos pruebas de negocio y unas pocas de punta a punta. El motivo es que **las reglas viven en el dominio y en las reglas de negocio** (modelo enriquecido), así que ahí está el mayor valor y el menor costo de prueba (ARQ-070).

| Nivel | Qué prueba | Herramienta | Base de datos | Ejemplos |
|---|---|---|---|---|
| **Unitarias de dominio** (PU) | Objetos de valor, transiciones de estado (State), invariantes y decisiones de cada entidad | JUnit 5 | No | `CorreoInstitucionalTest` (dominio inválido, mayúsculas, espacios). `EstadoContenidoTest` (no se libera un VISIBLE, VETADO es final). `UsuarioTest.intentarActivar` (las tres condiciones). `ContenidoTextoTest` (texto de 100.000 caracteres intacto) |
| **Reglas y negocio** (PF) | Reglas que consultan datos, servicios de dominio y políticas | JUnit 5 + `DAOFactory` falsa en memoria | No | Segundo Rector rechazado. Coord. de Facultad fuera de su Facultad rechazado. Aprobar con sustitución cierra la asignación anterior. Vetar acepta todos los reportes abiertos |
| **Base de datos** | Restricciones de integridad | `sql/validar_sql.py` + SQL Server | Sí (temporal) | 16 violaciones de reglas rechazadas (§5) |
| **API** (PA) | Endpoints de punta a punta | Colección Bruno, igual que en Sigma | Sí | Registro → verificación → aprobación → ingreso. Crear Kudo y responder. Reportar y vetar. Respuestas 401 y 403 |
| **Front-end** (PV) | Stores, servicios y validaciones | Vitest + Vue Test Utils | No (servidor falso) | `sesionStore` (menú por rol). Validaciones del formulario de registro. Recorte visual de Kudos largos |

**Criterio de suficiencia** (RNF-024): **≥ 80 % de cobertura de líneas en el paquete `dominio`**, medida con JaCoCo, y al menos una prueba por cada política marcada con excepción en el [modelo enriquecido](../01-modelo-dominio/README.md). La [matriz de trazabilidad](../00-analisis/matriz-trazabilidad.md) relaciona cada requisito con su prueba.

---

## 3. Análisis estático (RNF-025 a RNF-027, ARQ-071 a ARQ-073)

| Repositorio | Herramientas | Qué se revisa |
|---|---|---|
| Back-end | **SonarQube Community** (o SonarLint en IntelliJ) + **JaCoCo** | *Bugs*, vulnerabilidades, *code smells*, duplicación, complejidad y cobertura |
| Front-end | **ESLint** + **oxlint**, la configuración de Sigma | Errores, malas prácticas y estilo en Vue y JavaScript |

**Procedimiento**: (1) ejecutar el análisis, (2) guardar la evidencia en `evidencias/`, (3) registrar cada hallazgo con su causa y su corrección en la tabla de §4, (4) volver a ejecutar para confirmar.

### Resultados ⏳

| Fecha | Repositorio | Herramienta | Bugs | Vulnerabilidades | Code smells | Duplicación | Cobertura dominio | Evidencia |
|---|---|---|---|---|---|---|---|---|
| | Back-end | SonarQube + JaCoCo | | | | | | |
| | Front-end | ESLint + oxlint | | | | | — | |

---

## 4. Hallazgos, correcciones y refactorizaciones (RNF-027)

| # | Origen | Hallazgo | Causa | Corrección | Antes / después |
|---|---|---|---|---|---|
| 1 | Validación SQL | Los índices únicos filtrados no se creaban | `sqlcmd` trae `QUOTED_IDENTIFIER` apagado | Los scripts activan `set quoted_identifier on` | Antes: el script se detenía en `asignacion_rol`. Después: crea las 16 tablas |
| 2 | Validación SQL | Se aceptaba un reporte con motivo OTRO sin descripción | Un `check` con resultado *desconocido* (comparación con `NULL`) se deja pasar | `ck_reporte_descripcion` exige `descripcion is not null` | Antes: aceptado. Después: rechazado |
| 3 | Validación SQL | Se aceptaba una moderación cerrada sin motivo | La misma causa del hallazgo 2 | `ck_moderacion_cierre` exige `motivo_decision is not null` | Antes: aceptada. Después: rechazada |
| 4 | Revisión del modelo | `Usuario` quedaba conectado con unas 20 relaciones de autoría (`creadaPor`, `modificadoPor`…) | La autoría se repetía en cada entidad | La autoría vive en la auditoría (D-31) | Antes: 36 relaciones. Después: 29 |
| 5 | Revisión de los diagramas | Más de 10 cruces de líneas en los diagramas de dominio, clases y MER | Distribución automática | Distribución optimizada: 1 cruce, el mínimo posible (K3,3) | Ver el [modelo de dominio](../01-modelo-dominio/README.md#diagrama-línea-de-vida) |
| ⏳ | Análisis estático y revisión del código | | | | |

---

## 5. Evidencia ejecutada: validación de la base de datos

Los scripts `crear-tablas`, `datos-iniciales` y `datos-demo` se ejecutaron en **SQL Server 2025** sobre una base temporal (`KudosValidacion`, eliminada al terminar). Resultado: **16 tablas, 94 restricciones, 12 índices únicos filtrados** y los datos de demostración cargados sin errores. Luego se intentaron 16 operaciones que violan reglas de negocio, y **todas fueron rechazadas** por la restricción esperada. El detalle está en el [MER](../03-modelo-entidad-relacion/README.md#validación-de-las-restricciones).

Para repetirla: `python sql/validar_sql.py` (necesita SQL Server local con autenticación de Windows).

## 6. Resultados de las pruebas ⏳

| Nivel | Pruebas | Exitosas | Fallidas | Cobertura | Evidencia |
|---|---|---|---|---|---|
| Unitarias de dominio | | | | | |
| Reglas y negocio | | | | | |
| API (Bruno) | | | | — | |
| Front-end (Vitest) | | | | | |
| Base de datos | 16 | 16 | 0 | — | §5 |

## 7. Lista de revisión de código

Se aplica a cada módulo antes de darlo por terminado:

- [ ] Las reglas de negocio están en el dominio o en una `Rule`, nunca en el controlador ni en el DAO (ARQ-048).
- [ ] No hay setters públicos en las clases de dominio; el estado cambia solo con métodos de negocio (ARQ-008).
- [ ] Cada excepción de negocio usa un mensaje de `CatalogoMensajes` (RNF-006).
- [ ] Toda operación que cambia el estado publica su evento de dominio (RN-160, D-26).
- [ ] Toda consulta de listado es paginada (RNF-010).
- [ ] El SQL usa `PreparedStatement` y no concatena parámetros (RNF-003).
- [ ] Los nombres reflejan el dominio y no hay comentarios que compensen código confuso (ARQ-031 a ARQ-035).
- [ ] Hay pruebas para las políticas con excepción del módulo.
