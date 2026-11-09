# Facultad

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Unidad académica de la Universidad que agrupa Programas Académicos y es acompañada por un Coordinador de Facultad.

**Línea de vida:** 2

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único de la Facultad (RN-039) | UUID | Sí |
| nombre | Nombre oficial de la Facultad (RN-040) | Texto | Sí |
| estado | Estado de la Facultad | [EstadoFacultad](../README.md#enumeraciones) | Sí |
| fechaCreacion | Fecha y hora de creación | FechaHora | Sí |

### Datos de muestra

_4 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | nombre | estado | fechaCreacion |
|---|---|---|---|
| f-01 | Ingeniería | ACTIVA | 2026-11-01 11:00 |
| f-02 | Ciencias Económicas, Administrativas y Contables | ACTIVA | 2026-11-01 11:10 |
| f-03 | Ciencias de la Salud | ACTIVA | 2026-11-01 11:20 |
| f-04 | Derecho | EN_CONFORMACION | 2026-11-06 08:00 |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Es coordinada mediante-> | [AsignacionRol](AsignacionRol.md) | Facultad 0..1 — 0..* AsignacionRol | Asociación |
| Agrupa-> | [ProgramaAcademico](ProgramaAcademico.md) | Facultad 1 — 0..* ProgramaAcademico | Composición |
| Es solicitada en-> | [SolicitudRegistro](SolicitudRegistro.md) | Facultad 0..1 — 0..* SolicitudRegistro | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | Una unidad académica de la Universidad que agrupa Programas Académicos. |
| **Tipo de objeto** | Entidad raíz de agregado |
| **Agregado** | Facultad (raíz). Contiene sus ProgramaAcademico. |
| **¿Qué conoce? (información que administra)** | Su nombre, su estado y sus programas. |
| **¿Qué hace? (responsabilidades)** | • Crear Facultad<br>• Renombrar Facultad<br>• Activar Facultad<br>• Inactivar Facultad<br>• Consultar Facultades |
| **¿Qué decisiones toma?** | • Si puede tener Coordinador de Facultad (solo ACTIVA).<br>• Cuándo pasa de EN_CONFORMACION a ACTIVA (al tener su primer programa).<br>• Si puede inactivarse (sin programas activos). |
| **¿Qué decisiones delega?** | • La unicidad del nombre a una regla que consulta la base de datos.<br>• La verificación de que el actor es Coordinador Académico a la política de autorización. |
| **¿Con quién colabora?** | ProgramaAcademico, AsignacionRol, SolicitudRegistro |
| **¿Qué eventos genera?** | • Facultad creada<br>• Facultad modificada<br>• Facultad activada<br>• Facultad inactivada<br>• Facultades consultadas |
| **¿Qué invariantes protege?** | • RN-039/RN-040: identificador único y nombre.<br>• RN-045: sin programas solo mientras se conforma.<br>• INV-003: un solo coordinador vigente. |

### Caracterización de atributos

| Nombre atributo | id | nombre | estado | fechaCreacion |
|---|---|---|---|---|
| Descripción atributo | Identificador único de la Facultad (RN-039) | Nombre oficial de la Facultad (RN-040) | Estado de la Facultad | Fecha y hora de creación |
| Identificador del objeto de dominio | Sí | No | No | No |
| Tipo de dato | UUID | Texto | EstadoFacultad | FechaHora |
| Obligatorio | Sí | Sí | Sí | Sí |
| Longitud mínima (si aplica) |  | 3 |  |  |
| Longitud máxima (si aplica) |  | 80 |  |  |
| Precisión (si aplica) |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  |
| Rango final (si aplica) |  |  |  |  |
| Formato (si aplica) |  |  |  |  |
| Reglas especiales (si aplica) | Autogenerado | 3 a 80 caracteres; único; sin espacios al inicio ni al final | Nace EN_CONFORMACION y pasa a ACTIVA al crear su primer Programa (RN-045, D-05) | Asignada por el sistema |
| Dato calculado | No | No | No | No |
| Quitar espacios en blanco al inicio y al fin | No | Sí | No | No |
| Dato sensible | No | No | No | No |
| Valor por defecto (si aplica) |  |  | EN_CONFORMACION | Fecha actual |
| Auto generado | Sí | No | No | Sí |
| Crear Facultad | No requerido | Requerido | No requerido | No requerido |
| Renombrar Facultad | Requerido | Requerido | No requerido | No requerido |
| Activar Facultad | Requerido | No requerido | No requerido | No requerido |
| Inactivar Facultad | Requerido | No requerido | No requerido | No requerido |
| Consultar Facultades | Mostrar | Mostrar | Mostrar | Mostrar |

### Combinaciones únicas

| Nombre combinación única | Descripción | Atributos involucrados |
|---|---|---|
| UQ-FAC-1 | No pueden existir dos Facultades con el mismo nombre | nombre |

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Crear Facultad** (`crear()`) | Crea una Facultad nueva en conformación. | nombre | String | Nombre oficial. | Facultad | Facultad EN_CONFORMACION. | P-FAC-001 | Asegurar que el nombre de la Facultad tenga de 3 a 80 caracteres. | El nombre no es válido. | Rechazar la operación e informar el error. |
|  |  | actor | Usuario | Coordinador Académico. |  |  | P-FAC-002 | Asegurar que no exista otra Facultad con el mismo nombre. | Ya existe una Facultad con ese nombre. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-FAC-003 | Asegurar que solo el Coordinador Académico cree, modifique o inactive Facultades. | El actor no es Coordinador Académico. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-FAC-004 | Toda Facultad nace en estado EN_CONFORMACION. |  |  |
| **Renombrar Facultad** (`renombrar()`) | Cambia el nombre de la Facultad. | nombre | String | Nuevo nombre. | Facultad | Facultad modificada. | P-FAC-001 | Asegurar que el nombre de la Facultad tenga de 3 a 80 caracteres. | El nombre no es válido. | Rechazar la operación e informar el error. |
|  |  | actor | Usuario | Coordinador Académico. |  |  | P-FAC-002 | Asegurar que no exista otra Facultad con el mismo nombre. | Ya existe una Facultad con ese nombre. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-FAC-003 | Asegurar que solo el Coordinador Académico cree, modifique o inactive Facultades. | El actor no es Coordinador Académico. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-FAC-005 | Asegurar que no se modifique una Facultad inactiva. | La Facultad está inactiva. | Rechazar la modificación. |
| **Activar Facultad** (`activar()`) | Pasa la Facultad a ACTIVA cuando recibe su primer programa. | Ninguno | - | No requiere datos de entrada. | Facultad | Facultad activa. | P-FAC-006 | La Facultad pasa a ACTIVA cuando se le crea su primer Programa; solo una Facultad ACTIVA puede tener coordinador. |  |  |
| **Inactivar Facultad** (`inactivar()`) | Saca de operación una Facultad que no tiene programas activos. | actor | Usuario | Coordinador Académico. | Facultad | Facultad inactiva. | P-FAC-003 | Asegurar que solo el Coordinador Académico cree, modifique o inactive Facultades. | El actor no es Coordinador Académico. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-FAC-007 | Asegurar que no tenga Programas activos al inactivarla. | La Facultad tiene programas activos. | Rechazar la inactivación e informar cuántos programas activos tiene. |
| **Consultar Facultades** (`consultar()`) | Lista las Facultades con su estado y su coordinador vigente. | actor | Usuario | Usuario que consulta. | Lista de Facultad | Facultades visibles para el actor. | P-FAC-008 | Al consultar Facultades, mostrar su coordinador vigente; el Coordinador de Facultad ve solo la suya. |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Coordinador Académico | Crear Facultad | Crea una Facultad nueva en conformación. | Facultad |  |  | P-FAC-001 |  | Facultad creada | EVT-008 / RN-218 |
|  |  |  |  |  |  | P-FAC-002 |  |  |  |
|  |  |  |  |  |  | P-FAC-003 |  |  |  |
|  |  |  |  |  |  | P-FAC-004 |  |  |  |
| Coordinador Académico | Renombrar Facultad | Cambia el nombre de la Facultad. | Facultad |  |  | P-FAC-001 |  | Facultad modificada | RF-017 |
|  |  |  |  |  |  | P-FAC-002 |  |  |  |
|  |  |  |  |  |  | P-FAC-003 |  |  |  |
|  |  |  |  |  |  | P-FAC-005 |  |  |  |
| Sistema (al crear un programa) | Activar Facultad | Pasa la Facultad a ACTIVA cuando recibe su primer programa. | Facultad |  |  | P-FAC-006 |  | Facultad activada |  |
| Coordinador Académico | Inactivar Facultad | Saca de operación una Facultad que no tiene programas activos. | Facultad | Programas activos de la Facultad | Para validar que no tenga. | P-FAC-003 |  | Facultad inactivada |  |
|  |  |  |  |  |  | P-FAC-007 |  |  |  |
| Usuario activo | Consultar Facultades | Lista las Facultades con su estado y su coordinador vigente. | Facultad |  |  | P-FAC-008 |  | Facultades consultadas | RF-016 |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-FAC-001 | Asegurar que el nombre de la Facultad tenga de 3 a 80 caracteres. | Facultad | RN-040 |
| P-FAC-002 | Asegurar que no exista otra Facultad con el mismo nombre. | Facultad | UQ-FAC-1 |
| P-FAC-003 | Asegurar que solo el Coordinador Académico cree, modifique o inactive Facultades. | PoliticaAutorizacion | D-04, RN-121, RN-122 |
| P-FAC-004 | Toda Facultad nace en estado EN_CONFORMACION. | Facultad | RN-045, D-05 |
| P-FAC-005 | Asegurar que no se modifique una Facultad inactiva. | Facultad | D-05 |
| P-FAC-006 | La Facultad pasa a ACTIVA cuando se le crea su primer Programa; solo una Facultad ACTIVA puede tener coordinador. | Facultad | RN-045, INV-010, D-05 |
| P-FAC-007 | Asegurar que no tenga Programas activos al inactivarla. | Facultad | D-05, D-28 |
| P-FAC-008 | Al consultar Facultades, mostrar su coordinador vigente; el Coordinador de Facultad ve solo la suya. | Facultad | RN-042, RN-122, RN-119 |
