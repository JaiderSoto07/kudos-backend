# VinculacionPrograma

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Vínculo, con historial, entre un Docente o Estudiante y un Programa Académico. Resuelve la relación muchos a muchos y conserva la trazabilidad de vinculaciones y desvinculaciones.

**Línea de vida:** 4

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único del vínculo | UUID | Sí |
| usuario | Docente o Estudiante vinculado | [Usuario](Usuario.md) | Sí |
| programa | Programa al que se vincula | [ProgramaAcademico](ProgramaAcademico.md) | Sí |
| tipoVinculo | Calidad en la que participa en el programa | [TipoVinculo](../README.md#enumeraciones) | Sí |
| fechaInicio | Fecha y hora de vinculación | FechaHora | Sí |
| fechaFin | Fecha y hora de desvinculación | FechaHora | No |
| vigente | Indica si el vínculo está vigente | Lógico | Sí |
| motivoDesvinculacion | Razón de la desvinculación | Texto | No |

### Datos de muestra

_4 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | usuario | programa | tipoVinculo | fechaInicio | fechaFin | vigente | motivoDesvinculacion |
|---|---|---|---|---|---|---|---|
| vp-01 | u-06 Andrea Giraldo | Ingeniería de Sistemas | DOCENTE | 2026-11-04 11:00 |  | Sí |  |
| vp-02 | u-06 Andrea Giraldo | Ingeniería Industrial | DOCENTE | 2026-11-05 08:00 |  | Sí |  |
| vp-03 | u-07 Santiago Henao | Ingeniería de Sistemas | ESTUDIANTE | 2026-11-04 15:00 |  | Sí |  |
| vp-04 | u-07 Santiago Henao | Administración de Empresas | ESTUDIANTE | 2026-11-04 16:00 | 2026-11-06 09:00 | No | Cancelación del programa por el estudiante |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Vincula a-> | [Usuario](Usuario.md) | VinculacionPrograma 0..* — 1 Usuario | Asociación |
| Pertenece a-> | [ProgramaAcademico](ProgramaAcademico.md) | VinculacionPrograma 0..* — 1 ProgramaAcademico | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | La pertenencia, con historial, de un Docente o Estudiante a un Programa Académico. |
| **Tipo de objeto** | Entidad raíz de agregado |
| **Agregado** | VinculacionPrograma (raíz). |
| **¿Qué conoce? (información que administra)** | La persona, el programa, el tipo de vínculo, las fechas de inicio y fin y el motivo de desvinculación. |
| **¿Qué hace? (responsabilidades)** | • Vincular a programa<br>• Desvincular de programa<br>• Consultar programas de una persona<br>• Consultar personas de un programa |
| **¿Qué decisiones toma?** | • Si está vigente.<br>• Que desvincular cierra el vínculo sin borrarlo. |
| **¿Qué decisiones delega?** | • La verificación de que quien vincula es el coordinador vigente del programa a la política de ámbito.<br>• La suspensión de la cuenta cuando la persona queda sin vínculos a Usuario. |
| **¿Con quién colabora?** | Usuario, ProgramaAcademico, TipoVinculo |
| **¿Qué eventos genera?** | • Estudiante o docente vinculado a programa<br>• Estudiante o docente desvinculado de programa<br>• Programas de la persona consultados<br>• Personas del programa consultadas |
| **¿Qué invariantes protege?** | • INV-011/INV-014: una persona puede pertenecer a varios programas.<br>• INV-012/INV-013/INV-015/INV-016: los vínculos son trazables y desvincular no elimina historia. |

### Caracterización de atributos

| Nombre atributo | id | usuario | programa | tipoVinculo | fechaInicio | fechaFin | vigente | motivoDesvinculacion |
|---|---|---|---|---|---|---|---|---|
| Descripción atributo | Identificador único del vínculo | Docente o Estudiante vinculado | Programa al que se vincula | Calidad en la que participa en el programa | Fecha y hora de vinculación | Fecha y hora de desvinculación | Indica si el vínculo está vigente | Razón de la desvinculación |
| Identificador del objeto de dominio | Sí | No | No | No | No | No | No | No |
| Tipo de dato | UUID | Usuario | ProgramaAcademico | TipoVinculo | FechaHora | FechaHora | Lógico | Texto |
| Obligatorio | Sí | Sí | Sí | Sí | Sí | No | Sí | No |
| Longitud mínima (si aplica) |  |  |  |  |  |  |  | 0 |
| Longitud máxima (si aplica) |  |  |  |  |  |  |  | 250 |
| Precisión (si aplica) |  |  |  |  |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  |  |  |  |  |
| Rango final (si aplica) |  |  |  |  |  |  |  |  |
| Formato (si aplica) |  |  |  |  |  |  |  |  |
| Reglas especiales (si aplica) | Autogenerado | Su rol vigente debe ser DOCENTE o ESTUDIANTE | Debe estar activo | Coincide con el rol vigente del usuario (DOCENTE o ESTUDIANTE) | Asignada por el sistema | Vacía mientras está vigente | Sí cuando fechaFin está vacía | Máx. 250; obligatorio cuando se diligencia fechaFin |
| Dato calculado | No | No | No | No | No | No | Sí | No |
| Quitar espacios en blanco al inicio y al fin | No | No | No | No | No | No | No | Sí |
| Dato sensible | No | No | No | No | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  |  | Fecha actual |  | Sí |  |
| Auto generado | Sí | No | No | No | Sí | No | Sí | No |
| Vincular a programa | No requerido | Requerido | Requerido | Requerido | No requerido | No requerido | No requerido | No requerido |
| Desvincular de programa | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | Requerido |
| Consultar programas de una persona | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |
| Consultar personas de un programa | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | No mostrar |

### Combinaciones únicas

| Nombre combinación única | Descripción | Atributos involucrados |
|---|---|---|
| UQ-VIN-1 | Un usuario no puede tener dos vínculos vigentes con el mismo programa | usuario, programa, vigente = Sí |

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Vincular a programa** (`vincular()`) | Vincula a un docente o estudiante al programa del coordinador. | usuario | Usuario | Docente o estudiante. | VinculacionPrograma | Vínculo vigente. | P-VPR-001 | Asegurar que la persona tenga rol vigente DOCENTE o ESTUDIANTE y que su cuenta no esté desactivada. | La persona no es docente ni estudiante o su cuenta está desactivada. | Rechazar la vinculación. |
|  |  | programa | ProgramaAcademico | Programa del coordinador. |  |  | P-VPR-002 | Asegurar que el Programa exista y esté activo. | El programa no existe o está inactivo. | Rechazar la vinculación. |
|  |  | actor | Usuario | Coordinador de Programa. |  |  | P-VPR-003 | Asegurar que el actor sea el Coordinador de Programa vigente del programa. | El actor no coordina ese programa. | Rechazar la operación e informar que solo actúa sobre su programa. |
|  |  |  |  |  |  |  | P-VPR-004 | Asegurar que la persona no tenga ya un vínculo vigente con el mismo programa. | Ya existe un vínculo vigente. | Rechazar la vinculación e informar que ya pertenece al programa. |
|  |  |  |  |  |  |  | P-VPR-005 | Si la cuenta estaba suspendida por no tener vínculos, reactivarla al vincular. |  |  |
| **Desvincular de programa** (`desvincular()`) | Cierra el vínculo de una persona con el programa sin borrarlo. | actor | Usuario | Coordinador de Programa. | VinculacionPrograma | Vínculo cerrado. | P-VPR-003 | Asegurar que el actor sea el Coordinador de Programa vigente del programa. | El actor no coordina ese programa. | Rechazar la operación e informar que solo actúa sobre su programa. |
|  |  | motivo | String | Razón de la desvinculación. |  |  | P-VPR-006 | Asegurar que solo se desvincule un vínculo vigente, con motivo, cerrándolo sin borrarlo. | El vínculo no está vigente o falta el motivo. | Rechazar la desvinculación. |
|  |  |  |  |  |  |  | P-VPR-007 | Si la persona queda sin vínculos vigentes, suspender su cuenta. |  |  |
| **Consultar programas de una persona** (`consultarPorUsuario()`) | Lista los programas vigentes e históricos de un docente o estudiante. | usuario | UUID | Persona consultada. | Lista de VinculacionPrograma | Vínculos de la persona. | P-VPR-008 | Los programas de una persona incluyen los vigentes y los históricos con sus fechas. |  |  |
| **Consultar personas de un programa** (`consultarPorPrograma()`) | Lista los docentes y estudiantes vigentes de un programa. | programa | UUID | Programa consultado. | Lista de VinculacionPrograma | Personas del programa. | P-VPR-009 | Las personas de un programa las consultan su coordinador, el coordinador de su Facultad y el Coordinador Académico. | El actor no tiene permiso sobre el programa. | Rechazar la consulta. |
|  |  | tipo | TipoVinculo | Docentes o estudiantes. |  |  |  |  |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Coordinador de Programa | Vincular a programa | Vincula a un docente o estudiante al programa del coordinador. | VinculacionPrograma | Coordinación vigente | Para verificar que el actor coordina el programa. | P-VPR-001 |  | Estudiante o docente vinculado a programa | EVT-016, EVT-018 |
|  |  |  |  |  |  | P-VPR-002 |  |  |  |
|  |  |  |  |  |  | P-VPR-003 |  |  |  |
|  |  |  |  |  |  | P-VPR-004 |  |  |  |
|  |  |  |  |  |  | P-VPR-005 |  |  |  |
| Coordinador de Programa | Desvincular de programa | Cierra el vínculo de una persona con el programa sin borrarlo. | VinculacionPrograma |  |  | P-VPR-003 |  | Estudiante o docente desvinculado de programa | EVT-017, EVT-019 |
|  |  |  |  |  |  | P-VPR-006 |  |  |  |
|  |  |  |  |  |  | P-VPR-007 |  |  |  |
| Usuario activo | Consultar programas de una persona | Lista los programas vigentes e históricos de un docente o estudiante. | VinculacionPrograma |  |  | P-VPR-008 |  | Programas de la persona consultados | RF-027, RF-032, RF-072, RF-073 |
| Coordinadores | Consultar personas de un programa | Lista los docentes y estudiantes vigentes de un programa. | VinculacionPrograma |  |  | P-VPR-009 |  | Personas del programa consultadas | RF-028, RF-033 |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-VPR-001 | Asegurar que la persona tenga rol vigente DOCENTE o ESTUDIANTE y que su cuenta no esté desactivada. | VinculacionPrograma | RN-053, RN-061 |
| P-VPR-002 | Asegurar que el Programa exista y esté activo. | ProgramaAcademico | P-PAC-006 |
| P-VPR-003 | Asegurar que el actor sea el Coordinador de Programa vigente del programa. | PoliticaAmbito | RN-112 a RN-115, POL-004 |
| P-VPR-004 | Asegurar que la persona no tenga ya un vínculo vigente con el mismo programa. | VinculacionPrograma | UQ-VIN-1 |
| P-VPR-005 | Si la cuenta estaba suspendida por no tener vínculos, reactivarla al vincular. | Usuario | D-16, P-USU-025 |
| P-VPR-006 | Asegurar que solo se desvincule un vínculo vigente, con motivo, cerrándolo sin borrarlo. | VinculacionPrograma | RN-056, RN-057, RN-064, RN-065, INV-013, INV-016 |
| P-VPR-007 | Si la persona queda sin vínculos vigentes, suspender su cuenta. | Usuario | D-16, P-USU-023 |
| P-VPR-008 | Los programas de una persona incluyen los vigentes y los históricos con sus fechas. | VinculacionPrograma | RN-054, RN-062, RF-072, RF-073 |
| P-VPR-009 | Las personas de un programa las consultan su coordinador, el coordinador de su Facultad y el Coordinador Académico. | PoliticaAmbito | RN-055, RN-063, RN-118 |
