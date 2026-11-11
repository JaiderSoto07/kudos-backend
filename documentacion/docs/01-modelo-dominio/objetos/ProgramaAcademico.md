# ProgramaAcademico

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Programa de formación que pertenece a exactamente una Facultad y es acompañado por un Coordinador de Programa.

**Línea de vida:** 3

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único del Programa (RN-047) | UUID | Sí |
| nombre | Nombre oficial del Programa (RN-048) | Texto | Sí |
| facultad | Facultad a la que pertenece (RN-046, RN-049) | [Facultad](Facultad.md) | Sí |
| activo | Indica si el Programa está activo | Lógico | Sí |
| fechaCreacion | Fecha y hora de creación | FechaHora | Sí |

### Datos de muestra

_6 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | nombre | facultad | activo | fechaCreacion |
|---|---|---|---|---|
| p-01 | Ingeniería de Sistemas | Ingeniería | Sí | 2026-11-01 11:05 |
| p-02 | Ingeniería Industrial | Ingeniería | Sí | 2026-11-01 11:06 |
| p-03 | Ingeniería Electrónica | Ingeniería | Sí | 2026-11-01 11:07 |
| p-04 | Administración de Empresas | Ciencias Económicas, Administrativas y Contables | Sí | 2026-11-01 11:12 |
| p-05 | Contaduría Pública | Ciencias Económicas, Administrativas y Contables | Sí | 2026-11-01 11:13 |
| p-06 | Enfermería | Ciencias de la Salud | Sí | 2026-11-01 11:22 |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Es coordinado mediante-> | [AsignacionRol](AsignacionRol.md) | ProgramaAcademico 0..1 — 0..* AsignacionRol | Asociación |
| Pertenece a-> | [Facultad](Facultad.md) | ProgramaAcademico 0..* — 1 Facultad | Composición |
| Tiene-> | [VinculacionPrograma](VinculacionPrograma.md) | ProgramaAcademico 1 — 0..* VinculacionPrograma | Asociación |
| Es solicitado en-> | [SolicitudRegistro](SolicitudRegistro.md) | ProgramaAcademico 0..1 — 0..* SolicitudRegistro | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | Un programa de formación que pertenece a exactamente una Facultad. |
| **Tipo de objeto** | Entidad interna de agregado |
| **Agregado** | Facultad (entidad interna). |
| **¿Qué conoce? (información que administra)** | Su nombre, su Facultad y si está activo. |
| **¿Qué hace? (responsabilidades)** | • Crear Programa<br>• Renombrar Programa<br>• Inactivar Programa<br>• Consultar Programas |
| **¿Qué decisiones toma?** | • Si está activo para recibir vínculos y coordinador.<br>• Si puede inactivarse (sin vínculos ni coordinador vigentes). |
| **¿Qué decisiones delega?** | • La activación de la Facultad a Facultad.<br>• La unicidad del nombre a una regla de negocio. |
| **¿Con quién colabora?** | Facultad, VinculacionPrograma, AsignacionRol, SolicitudRegistro |
| **¿Qué eventos genera?** | • Programa creado<br>• Programa modificado<br>• Programa inactivado<br>• Programas consultados |
| **¿Qué invariantes protege?** | • INV-008/RN-046/RN-049: pertenece a exactamente una Facultad.<br>• INV-004: un solo coordinador vigente. |

### Caracterización de atributos

| Nombre atributo | id | nombre | facultad | activo | fechaCreacion |
|---|---|---|---|---|---|
| Descripción atributo | Identificador único del Programa (RN-047) | Nombre oficial del Programa (RN-048) | Facultad a la que pertenece (RN-046, RN-049) | Indica si el Programa está activo | Fecha y hora de creación |
| Identificador del objeto de dominio | Sí | No | No | No | No |
| Tipo de dato | UUID | Texto | Facultad | Lógico | FechaHora |
| Obligatorio | Sí | Sí | Sí | Sí | Sí |
| Longitud mínima (si aplica) |  | 3 |  |  |  |
| Longitud máxima (si aplica) |  | 100 |  |  |  |
| Precisión (si aplica) |  |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  |  |
| Rango final (si aplica) |  |  |  |  |  |
| Formato (si aplica) |  |  |  |  |  |
| Reglas especiales (si aplica) | Autogenerado | 3 a 100 caracteres; único; sin espacios al inicio ni al final | Exactamente una; no puede estar INACTIVA | Un Programa inactivo no recibe vínculos ni coordinadores nuevos | Asignada por el sistema |
| Dato calculado | No | No | No | No | No |
| Quitar espacios en blanco al inicio y al fin | No | Sí | No | No | No |
| Dato sensible | No | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  | Sí | Fecha actual |
| Auto generado | Sí | No | No | No | Sí |
| Crear Programa | No requerido | Requerido | Requerido | No requerido | No requerido |
| Renombrar Programa | Requerido | Requerido | No requerido | No requerido | No requerido |
| Inactivar Programa | Requerido | No requerido | No requerido | No requerido | No requerido |
| Consultar Programas | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |

### Combinaciones únicas

| Nombre combinación única | Descripción | Atributos involucrados |
|---|---|---|
| UQ-PRO-1 | No pueden existir dos Programas con el mismo nombre | nombre |

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Crear Programa** (`crear()`) | Crea un programa en una Facultad no inactiva. | nombre | String | Nombre oficial. | ProgramaAcademico | Programa activo. | P-PAC-001 | Asegurar que el nombre del Programa tenga de 3 a 100 caracteres. | El nombre no es válido. | Rechazar la operación. |
|  |  | facultad | Facultad | Facultad a la que pertenece. |  |  | P-PAC-002 | Asegurar que no exista otro Programa con el mismo nombre. | Ya existe un Programa con ese nombre. | Rechazar la operación. |
|  |  | actor | Usuario | Coordinador Académico. |  |  | P-PAC-003 | Asegurar que el Programa pertenezca a exactamente una Facultad existente y no inactiva. | La Facultad no existe o está inactiva. | Rechazar la creación. |
|  |  |  |  |  |  |  | P-PAC-004 | Asegurar que solo el Coordinador Académico cree, modifique o inactive Programas. | El actor no es Coordinador Académico. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-PAC-005 | Al crear el primer Programa de una Facultad EN_CONFORMACION, activar la Facultad. |  |  |
| **Renombrar Programa** (`renombrar()`) | Cambia el nombre del programa. | nombre | String | Nuevo nombre. | ProgramaAcademico | Programa modificado. | P-PAC-001 | Asegurar que el nombre del Programa tenga de 3 a 100 caracteres. | El nombre no es válido. | Rechazar la operación. |
|  |  | actor | Usuario | Coordinador Académico. |  |  | P-PAC-002 | Asegurar que no exista otro Programa con el mismo nombre. | Ya existe un Programa con ese nombre. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-PAC-004 | Asegurar que solo el Coordinador Académico cree, modifique o inactive Programas. | El actor no es Coordinador Académico. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-PAC-006 | Asegurar que el Programa esté activo para modificarlo, vincular personas o asignarle coordinador. | El Programa está inactivo. | Rechazar la operación. |
| **Inactivar Programa** (`inactivar()`) | Saca de operación un programa sin vínculos ni coordinador vigentes. | actor | Usuario | Coordinador Académico. | ProgramaAcademico | Programa inactivo. | P-PAC-004 | Asegurar que solo el Coordinador Académico cree, modifique o inactive Programas. | El actor no es Coordinador Académico. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-PAC-007 | Asegurar que no tenga vínculos ni coordinador vigentes al inactivarlo. | El Programa tiene personas o coordinador vigentes. | Rechazar la inactivación. |
| **Consultar Programas** (`consultar()`) | Lista los programas visibles para el actor con su coordinador vigente. | actor | Usuario | Usuario que consulta. | Lista de ProgramaAcademico | Programas. | P-PAC-008 | El Coordinador de Facultad consulta los Programas de su Facultad; el Coordinador Académico, todos. |  |  |
|  |  | facultad | UUID | Filtro opcional. |  |  |  |  |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Coordinador Académico | Crear Programa | Crea un programa en una Facultad no inactiva. | ProgramaAcademico |  |  | P-PAC-001 |  | Programa creado | EVT-009 / RN-221 |
|  |  |  |  |  |  | P-PAC-002 |  |  |  |
|  |  |  |  |  |  | P-PAC-003 |  |  |  |
|  |  |  |  |  |  | P-PAC-004 |  |  |  |
|  |  |  |  |  |  | P-PAC-005 |  |  |  |
| Coordinador Académico | Renombrar Programa | Cambia el nombre del programa. | ProgramaAcademico |  |  | P-PAC-001 |  | Programa modificado | RF-022 |
|  |  |  |  |  |  | P-PAC-002 |  |  |  |
|  |  |  |  |  |  | P-PAC-004 |  |  |  |
|  |  |  |  |  |  | P-PAC-006 |  |  |  |
| Coordinador Académico | Inactivar Programa | Saca de operación un programa sin vínculos ni coordinador vigentes. | ProgramaAcademico | Vínculos y coordinación vigentes | Para validar que no tenga. | P-PAC-004 |  | Programa inactivado |  |
|  |  |  |  |  |  | P-PAC-007 |  |  |  |
| Usuario activo | Consultar Programas | Lista los programas visibles para el actor con su coordinador vigente. | ProgramaAcademico |  |  | P-PAC-008 |  | Programas consultados | RF-021 |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-PAC-001 | Asegurar que el nombre del Programa tenga de 3 a 100 caracteres. | ProgramaAcademico | RN-048 |
| P-PAC-002 | Asegurar que no exista otro Programa con el mismo nombre. | ProgramaAcademico | UQ-PRO-1 |
| P-PAC-003 | Asegurar que el Programa pertenezca a exactamente una Facultad existente y no inactiva. | Facultad | RN-046, RN-049, INV-008 |
| P-PAC-004 | Asegurar que solo el Coordinador Académico cree, modifique o inactive Programas. | AsegurarActorTieneRolRule | D-04 |
| P-PAC-005 | Al crear el primer Programa de una Facultad EN_CONFORMACION, activar la Facultad. | Facultad | P-FAC-006 |
| P-PAC-006 | Asegurar que el Programa esté activo para modificarlo, vincular personas o asignarle coordinador. | ProgramaAcademico | INV-009 |
| P-PAC-007 | Asegurar que no tenga vínculos ni coordinador vigentes al inactivarlo. | ProgramaAcademico | D-28 |
| P-PAC-008 | El Coordinador de Facultad consulta los Programas de su Facultad; el Coordinador Académico, todos. | AsignacionRol (ámbito) | RN-118, RN-122 |

## En el código

| Capa | Clase |
|---|---|
| Dominio (atributos y comportamiento) | `ProgramaAcademicoDominio` (`co.edu.uco.kudos.dominio`) |
| Entidad (tabla) | `ProgramaAcademicoEntidad` |
| DAO | `ProgramaAcademicoDAO` + implementación SQL Server |
| DTO (API) | ProgramaAcademicoDto |
| Negocio | ProgramaNegocioImpl |
