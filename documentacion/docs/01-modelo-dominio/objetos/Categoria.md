# Categoria

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Clasificación institucional de los reconocimientos (RN-132). Puede estar activa o inactiva; solo las activas se usan para Kudos nuevos.

**Línea de vida:** 2

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único de la categoría | UUID | Sí |
| nombre | Nombre de la categoría | Texto | Sí |
| descripcion | Qué tipo de comportamiento o logro reconoce | Texto | Sí |
| activa | Indica si puede usarse para Kudos nuevos (RN-133, RN-144) | Lógico | Sí |
| fechaCreacion | Fecha y hora de creación | FechaHora | Sí |

### Datos de muestra

_6 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | nombre | descripcion | activa | fechaCreacion |
|---|---|---|---|---|
| c-01 | Excelencia académica | Reconoce logros académicos sobresalientes en clase, investigación o evaluaciones. | Sí | 2026-11-01 09:30 |
| c-02 | Trabajo en equipo | Reconoce la colaboración y el apoyo a compañeros para alcanzar metas comunes. | Sí | 2026-11-01 09:31 |
| c-03 | Servicio a la comunidad | Reconoce aportes a la comunidad universitaria o a la región. | Sí | 2026-11-01 09:32 |
| c-04 | Liderazgo | Reconoce a quien inspira y guía a otros con el ejemplo. | Sí | 2026-11-01 09:33 |
| c-05 | Valores institucionales | Reconoce comportamientos alineados con los valores católicos y el proyecto educativo de la UCO. | Sí | 2026-11-01 10:40 |
| c-06 | Semana de la innovación 2025 | Reconoce proyectos presentados en la Semana de la Innovación 2025. | No | 2025-09-01 08:00 |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Clasifica-> | [Kudo](Kudo.md) | Categoria 1 — 0..* Kudo | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | Una clasificación institucional de los reconocimientos. |
| **Tipo de objeto** | Entidad raíz de agregado |
| **Agregado** | Categoria (raíz). |
| **¿Qué conoce? (información que administra)** | Su nombre, su descripción y si está activa. |
| **¿Qué hace? (responsabilidades)** | • Crear categoría<br>• Modificar categoría<br>• Activar categoría<br>• Desactivar categoría<br>• Consultar categorías |
| **¿Qué decisiones toma?** | • Si puede usarse en un Kudo nuevo (solo activa).<br>• Si una activación o desactivación es válida. |
| **¿Qué decisiones delega?** | • Quién puede gestionarla a la política de gestión de categorías.<br>• La unicidad del nombre a una regla de negocio. |
| **¿Con quién colabora?** | Kudo |
| **¿Qué eventos genera?** | • Categoría creada<br>• Categoría modificada<br>• Categoría activada<br>• Categoría desactivada<br>• Categorías consultadas |
| **¿Qué invariantes protege?** | • INV-019/RN-144: una categoría inactiva no se usa en Kudos nuevos.<br>• INV-018/RN-145: los Kudos históricos conservan su categoría. |

### Caracterización de atributos

| Nombre atributo | id | nombre | descripcion | activa | fechaCreacion |
|---|---|---|---|---|---|
| Descripción atributo | Identificador único de la categoría | Nombre de la categoría | Qué tipo de comportamiento o logro reconoce | Indica si puede usarse para Kudos nuevos (RN-133, RN-144) | Fecha y hora de creación |
| Identificador del objeto de dominio | Sí | No | No | No | No |
| Tipo de dato | UUID | Texto | Texto | Lógico | FechaHora |
| Obligatorio | Sí | Sí | Sí | Sí | Sí |
| Longitud mínima (si aplica) |  | 3 | 10 |  |  |
| Longitud máxima (si aplica) |  | 50 | 250 |  |  |
| Precisión (si aplica) |  |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  |  |
| Rango final (si aplica) |  |  |  |  |  |
| Formato (si aplica) |  |  |  |  |  |
| Reglas especiales (si aplica) | Autogenerado | 3 a 50 caracteres; único | 10 a 250 caracteres |  | Asignada por el sistema |
| Dato calculado | No | No | No | No | No |
| Quitar espacios en blanco al inicio y al fin | No | Sí | Sí | No | No |
| Dato sensible | No | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  | Sí | Fecha actual |
| Auto generado | Sí | No | No | No | Sí |
| Crear categoría | No requerido | Requerido | Requerido | No requerido | No requerido |
| Modificar categoría | Requerido | Requerido | Requerido | No requerido | No requerido |
| Activar categoría | Requerido | No requerido | No requerido | No requerido | No requerido |
| Desactivar categoría | Requerido | No requerido | No requerido | No requerido | No requerido |
| Consultar categorías | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |

### Combinaciones únicas

| Nombre combinación única | Descripción | Atributos involucrados |
|---|---|---|
| UQ-CAT-1 | No pueden existir dos categorías con el mismo nombre | nombre |

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Crear categoría** (`crear()`) | Crea una categoría institucional de reconocimiento. | nombre | String | Nombre. | Categoria | Categoría activa. | P-CAT-001 | Asegurar que el nombre tenga de 3 a 50 caracteres y la descripción de 10 a 250. | El nombre o la descripción no son válidos. | Rechazar la operación. |
|  |  | descripcion | String | Qué reconoce. |  |  | P-CAT-002 | Asegurar que no exista otra categoría con el mismo nombre. | Ya existe una categoría con ese nombre. | Rechazar la operación. |
|  |  | actor | Usuario | Gestor de categorías. |  |  | P-CAT-003 | Asegurar que solo Administrador, Rector o Coordinador Académico gestionen categorías. | El actor no puede gestionar categorías. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-CAT-004 | Toda categoría nace activa. |  |  |
| **Modificar categoría** (`modificar()`) | Cambia el nombre o la descripción de una categoría. | nombre | String | Nuevo nombre. | Categoria | Categoría modificada. | P-CAT-001 | Asegurar que el nombre tenga de 3 a 50 caracteres y la descripción de 10 a 250. | El nombre o la descripción no son válidos. | Rechazar la operación. |
|  |  | descripcion | String | Nueva descripción. |  |  | P-CAT-002 | Asegurar que no exista otra categoría con el mismo nombre. | Ya existe una categoría con ese nombre. | Rechazar la operación. |
|  |  | actor | Usuario | Gestor de categorías. |  |  | P-CAT-003 | Asegurar que solo Administrador, Rector o Coordinador Académico gestionen categorías. | El actor no puede gestionar categorías. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-CAT-005 | Los Kudos existentes conservan la categoría con la que se crearon, aunque esta cambie o se desactive. |  |  |
| **Activar categoría** (`activar()`) | Permite de nuevo usar la categoría en Kudos nuevos. | actor | Usuario | Gestor de categorías. | Categoria | Categoría activa. | P-CAT-003 | Asegurar que solo Administrador, Rector o Coordinador Académico gestionen categorías. | El actor no puede gestionar categorías. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-CAT-006 | Asegurar que solo se active una categoría inactiva. | La categoría ya está activa. | Informar que ya está activa. |
| **Desactivar categoría** (`desactivar()`) | Impide usar la categoría en Kudos nuevos. | actor | Usuario | Gestor de categorías. | Categoria | Categoría inactiva. | P-CAT-003 | Asegurar que solo Administrador, Rector o Coordinador Académico gestionen categorías. | El actor no puede gestionar categorías. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-CAT-007 | Asegurar que solo se desactive una categoría activa; desde entonces no se usa en Kudos nuevos. | La categoría ya está inactiva. | Informar que ya está inactiva. |
| **Consultar categorías** (`consultar()`) | Lista las categorías; las inactivas solo para los gestores. | actor | Usuario | Usuario que consulta. | Lista de Categoria | Categorías. | P-CAT-008 | Todos los usuarios consultan las categorías activas; los gestores consultan también las inactivas. |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Gestor de categorías | Crear categoría | Crea una categoría institucional de reconocimiento. | Categoria |  |  | P-CAT-001 |  | Categoría creada | EVT-020 / RN-224 |
|  |  |  |  |  |  | P-CAT-002 |  |  |  |
|  |  |  |  |  |  | P-CAT-003 |  |  |  |
|  |  |  |  |  |  | P-CAT-004 |  |  |  |
| Gestor de categorías | Modificar categoría | Cambia el nombre o la descripción de una categoría. | Categoria |  |  | P-CAT-001 |  | Categoría modificada | EVT-021 / RN-225 |
|  |  |  |  |  |  | P-CAT-002 |  |  |  |
|  |  |  |  |  |  | P-CAT-003 |  |  |  |
|  |  |  |  |  |  | P-CAT-005 |  |  |  |
| Gestor de categorías | Activar categoría | Permite de nuevo usar la categoría en Kudos nuevos. | Categoria |  |  | P-CAT-003 |  | Categoría activada | EVT-022 / RN-226 |
|  |  |  |  |  |  | P-CAT-006 |  |  |  |
| Gestor de categorías | Desactivar categoría | Impide usar la categoría en Kudos nuevos. | Categoria |  |  | P-CAT-003 |  | Categoría desactivada | EVT-023 / RN-227 |
|  |  |  |  |  |  | P-CAT-007 |  |  |  |
| Usuario activo | Consultar categorías | Lista las categorías; las inactivas solo para los gestores. | Categoria |  |  | P-CAT-008 |  | Categorías consultadas | RF-045 |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-CAT-001 | Asegurar que el nombre tenga de 3 a 50 caracteres y la descripción de 10 a 250. | Categoria | RN-132 |
| P-CAT-002 | Asegurar que no exista otra categoría con el mismo nombre. | Categoria | UQ-CAT-1 |
| P-CAT-003 | Asegurar que solo Administrador, Rector o Coordinador Académico gestionen categorías. | PoliticaGestionCategorias | RN-134 a RN-143, D-01 |
| P-CAT-004 | Toda categoría nace activa. | Categoria | RN-133 |
| P-CAT-005 | Los Kudos existentes conservan la categoría con la que se crearon, aunque esta cambie o se desactive. | Kudo | RN-145, INV-018 |
| P-CAT-006 | Asegurar que solo se active una categoría inactiva. | Categoria | RN-142 |
| P-CAT-007 | Asegurar que solo se desactive una categoría activa; desde entonces no se usa en Kudos nuevos. | Categoria | RN-143, RN-144, INV-019 |
| P-CAT-008 | Todos los usuarios consultan las categorías activas; los gestores consultan también las inactivas. | Categoria | INV-018 |
