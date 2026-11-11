# Catálogo de reglas de negocio

[Inicio](../../README.md) · [Requisitos](requisitos.md) · [Decisiones de diseño](decisiones-de-diseno.md) · [Matriz de trazabilidad](matriz-trazabilidad.md) · [Modelo de dominio](../01-modelo-dominio/README.md)

Este documento reúne **todas** las reglas de negocio del enunciado (DOC-003) y dice, para cada una, **dónde se garantiza** en el diseño:

- **Reglas RN-001 a RN-250** (documentos 03, 04 y 05).
- **Invariantes INV-001 a INV-035** y **políticas institucionales POL-001 a POL-007** (documento 06).
- **Eventos EVT-001 a EVT-038** (documento 06) con el evento de dominio que los produce.

La columna **Dónde se garantiza** enlaza la política del modelo enriquecido (`P-XXX-nnn`) que protege la regla, en la página del objeto responsable. Cuando ninguna política cita la regla de forma directa, se indica *en cursiva* el objeto y el mecanismo que la cubren. La columna **Decisión / requisito** muestra la decisión de diseño (D-xx) y los requisitos (RF) relacionados.

## Resumen

| Tipo | Cantidad | Fuente |
|---|:---:|---|
| Reglas de negocio (RN) | 250 | Documentos 03, 04 y 05 |
| Invariantes (INV) | 35 | Documento 06 |
| Políticas institucionales (POL) | 7 | Documento 06 |
| Eventos de referencia (EVT) | 38 | Documento 06 |

De las 292 reglas, invariantes y políticas, **194** las cita directamente una política del modelo enriquecido. Las demás las cubre el objeto o el mecanismo indicado en cursiva. **Ninguna queda sin responsable.**

## Contenido

1. [Reglas de negocio RN-001 a RN-250](#1-reglas-de-negocio)
2. [Invariantes INV-001 a INV-035](#2-invariantes)
3. [Políticas institucionales POL-001 a POL-007](#3-políticas-institucionales)
4. [Eventos EVT-001 a EVT-038](#4-eventos-de-referencia)

## 1. Reglas de negocio


### Exclusividad de Roles (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-001 | Una persona solamente podrá tener un rol institucional activo. | [P-ASR-001](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-USU-029](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-13, RF-013 |
| RN-002 | Los roles institucionales son mutuamente excluyentes. | [P-ASR-001](../01-modelo-dominio/objetos/AsignacionRol.md#políticas) | D-13, RF-013 |
| RN-003 | No se permite la acumulación de cargos institucionales. | [P-ASR-001](../01-modelo-dominio/objetos/AsignacionRol.md#políticas) | D-13, RF-013 |
| RN-004 | Toda asignación de rol deberá respetar las políticas institucionales vigentes. | [P-ASR-001](../01-modelo-dominio/objetos/AsignacionRol.md#políticas) | D-13, RF-013 |

### Rector (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-005 | La Universidad solamente podrá tener un Rector activo. | [P-ASR-002](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-USU-010](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-13, RF-014 |
| RN-006 | Un Rector no podrá ser Coordinador Académico. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-007 | Un Rector no podrá ser Coordinador de Facultad. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-008 | Un Rector no podrá ser Coordinador de Programa. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-009 | Un Rector no podrá ser Docente. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-010 | Un Rector no podrá ser Estudiante. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |

### Coordinador Académico (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-011 | La Universidad solamente podrá tener un Coordinador Académico activo. | [P-ASR-002](../01-modelo-dominio/objetos/AsignacionRol.md#políticas) | D-13, RF-014 |
| RN-012 | Un Coordinador Académico no podrá ser Rector. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-013 | Un Coordinador Académico no podrá ser Coordinador de Facultad. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-014 | Un Coordinador Académico no podrá ser Coordinador de Programa. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-015 | Un Coordinador Académico no podrá ser Docente. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-016 | Un Coordinador Académico no podrá ser Estudiante. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |

### Coordinador de Facultad (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-017 | Cada Facultad solamente podrá tener un Coordinador de Facultad activo. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-018 | Un Coordinador de Facultad no podrá ser Rector. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-019 | Un Coordinador de Facultad no podrá ser Coordinador Académico. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-020 | Un Coordinador de Facultad no podrá ser Coordinador de Programa. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-021 | Un Coordinador de Facultad no podrá ser Docente. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-022 | Un Coordinador de Facultad no podrá ser Estudiante. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |

### Coordinador de Programa (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-023 | Cada Programa Académico solamente podrá tener un Coordinador de Programa activo. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-024 | Un Coordinador de Programa no podrá ser Rector. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-025 | Un Coordinador de Programa no podrá ser Coordinador Académico. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-026 | Un Coordinador de Programa no podrá ser Coordinador de Facultad. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-027 | Un Coordinador de Programa no podrá ser Docente. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-028 | Un Coordinador de Programa no podrá ser Estudiante. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |

### Docente (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-029 | Un Docente no podrá ser Rector. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-030 | Un Docente no podrá ser Coordinador Académico. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-031 | Un Docente no podrá ser Coordinador de Facultad. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-032 | Un Docente no podrá ser Coordinador de Programa. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-033 | Un Docente no podrá ser Estudiante. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |

### Estudiante (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-034 | Un Estudiante no podrá ser Rector. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-035 | Un Estudiante no podrá ser Coordinador Académico. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-036 | Un Estudiante no podrá ser Coordinador de Facultad. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-037 | Un Estudiante no podrá ser Coordinador de Programa. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |
| RN-038 | Un Estudiante no podrá ser Docente. | *Usuario + AsignacionRol: un solo rol vigente por estructura e índice único filtrado UQ-ASR-1 (D-13, D-14)* | D-13, RF-014 |

### Reglas de Facultades (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-039 | Una Facultad deberá poseer un identificador único. | *Facultad, ProgramaAcademico y AsignacionRol: estados de Facultad e historial de coordinadores (D-05, D-15)* | RF-015 |
| RN-040 | Una Facultad deberá tener un nombre. | [P-FAC-001](../01-modelo-dominio/objetos/Facultad.md#políticas) | RF-015 |
| RN-041 | Una Facultad podrá contener múltiples Programas Académicos. | *Facultad, ProgramaAcademico y AsignacionRol: estados de Facultad e historial de coordinadores (D-05, D-15)* |  |
| RN-042 | Debe ser posible identificar el Coordinador responsable de una Facultad. | [P-FAC-008](../01-modelo-dominio/objetos/Facultad.md#políticas) | RF-018 |
| RN-043 | Debe existir trazabilidad histórica de los Coordinadores de Facultad. | [P-ASR-004](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-ASR-006](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-ASR-008](../01-modelo-dominio/objetos/AsignacionRol.md#políticas) | D-15, RF-019 |
| RN-044 | La sustitución de un Coordinador de Facultad deberá quedar auditada. | [P-ASR-004](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-ASR-005](../01-modelo-dominio/objetos/AsignacionRol.md#políticas) | D-15, RF-019 |
| RN-045 | Una Facultad puede existir sin Programas únicamente durante procesos de creación institucional. | [P-FAC-004](../01-modelo-dominio/objetos/Facultad.md#políticas), [P-FAC-006](../01-modelo-dominio/objetos/Facultad.md#políticas) | D-05, RF-015 |

### Reglas de Programas Académicos (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-046 | Todo Programa Académico deberá pertenecer a una Facultad. | [P-PAC-003](../01-modelo-dominio/objetos/ProgramaAcademico.md#políticas) | RF-020 |
| RN-047 | Todo Programa Académico deberá poseer un identificador único. | *Facultad, ProgramaAcademico y AsignacionRol: estados de Facultad e historial de coordinadores (D-05, D-15)* | RF-020 |
| RN-048 | Todo Programa Académico deberá tener nombre. | [P-PAC-001](../01-modelo-dominio/objetos/ProgramaAcademico.md#políticas) | RF-020 |
| RN-049 | Todo Programa Académico deberá estar asociado a exactamente una Facultad. | [P-PAC-003](../01-modelo-dominio/objetos/ProgramaAcademico.md#políticas) | RF-020 |
| RN-050 | Debe ser posible identificar el Coordinador responsable de un Programa. | *Facultad, ProgramaAcademico y AsignacionRol: estados de Facultad e historial de coordinadores (D-05, D-15)* | RF-023 |
| RN-051 | Debe existir trazabilidad histórica de Coordinadores de Programa. | [P-ASR-004](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-ASR-006](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-ASR-008](../01-modelo-dominio/objetos/AsignacionRol.md#políticas) | RF-024 |
| RN-052 | Los cambios de Coordinador deberán quedar auditados. | [P-ASR-004](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-ASR-005](../01-modelo-dominio/objetos/AsignacionRol.md#políticas) | RF-024 |

### Reglas de Estudiantes (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-053 | Un Estudiante podrá pertenecer simultáneamente a múltiples Programas Académicos. | [P-VPR-001](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | D-16, RF-026 |
| RN-054 | Debe ser posible identificar todos los Programas Académicos asociados a un Estudiante. | [P-VPR-008](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | D-16, RF-027, RF-072 |
| RN-055 | Debe ser posible identificar todos los Estudiantes pertenecientes a un Programa. | [P-VPR-009](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | D-16, RF-028 |
| RN-056 | La vinculación entre Estudiante y Programa deberá ser trazable. | [P-VPR-006](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | D-16, RF-029 |
| RN-057 | Toda desvinculación deberá conservar historial. | [P-VPR-006](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | D-16, RF-029, RF-085 |
| RN-058 | Un Estudiante podrá otorgar Kudos. | [P-KUD-001](../01-modelo-dominio/objetos/Kudo.md#políticas) | D-16, RF-025 |
| RN-059 | Un Estudiante podrá recibir Kudos. | *VinculacionPrograma con vigencias e historial; cualquier usuario activo otorga, recibe y reporta (D-16)* | D-16 |
| RN-060 | Un Estudiante podrá reportar contenido. | *VinculacionPrograma con vigencias e historial; cualquier usuario activo otorga, recibe y reporta (D-16)* | D-16 |

### Reglas de Docentes (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-061 | Un Docente podrá pertenecer simultáneamente a múltiples Programas Académicos. | [P-VPR-001](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | D-16, RF-031 |
| RN-062 | Debe ser posible identificar todos los Programas asociados a un Docente. | [P-VPR-008](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | D-16, RF-032, RF-073 |
| RN-063 | Debe ser posible identificar todos los Docentes de un Programa Académico. | [P-VPR-009](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | D-16, RF-033 |
| RN-064 | La vinculación entre Docente y Programa deberá ser trazable. | [P-VPR-006](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | D-16, RF-034 |
| RN-065 | Toda desvinculación deberá conservar historial. | [P-VPR-006](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | D-16, RF-034, RF-085 |
| RN-066 | Un Docente podrá otorgar Kudos. | [P-KUD-001](../01-modelo-dominio/objetos/Kudo.md#políticas) | RF-030 |
| RN-067 | Un Docente podrá recibir Kudos. | *VinculacionPrograma con vigencias e historial; cualquier usuario activo otorga, recibe y reporta (D-16)* |  |
| RN-068 | Un Docente podrá reportar contenido. | *VinculacionPrograma con vigencias e historial; cualquier usuario activo otorga, recibe y reporta (D-16)* |  |

### Registro de Usuarios (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-069 | Todo usuario deberá registrarse utilizando correo institucional. | [P-USU-002](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-12, RF-001, RF-002 |
| RN-070 | Todo usuario deberá registrar un número celular. | [P-USU-004](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-12, RF-001, RF-003 |
| RN-071 | El correo institucional será obligatorio. | [P-USU-002](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-12, RF-001, RF-002 |
| RN-072 | El número celular será obligatorio. | [P-USU-004](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-12, RF-001, RF-003 |
| RN-073 | El correo institucional deberá ser único. | [P-USU-003](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-12, RF-001, RF-004 |
| RN-074 | El número celular deberá ser único. | [P-USU-005](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-12, RF-001, RF-005 |
| RN-075 | No podrá existir una cuenta activa sin correo institucional. | *Google verifica el correo; ValidarCorreoInstitucionalRule, ValidarCelularRule, CodigoVerificacion y Usuario.intentarActivar() (D-09, D-10, D-12, D-17)* | D-12, RF-001 |
| RN-076 | No podrá existir una cuenta activa sin número celular. | *Google verifica el correo; ValidarCorreoInstitucionalRule, ValidarCelularRule, CodigoVerificacion y Usuario.intentarActivar() (D-09, D-10, D-12, D-17)* | D-12, RF-001 |

### Verificación de Identidad (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-077 | Todo correo institucional deberá ser validado. | [P-CVE-001](../01-modelo-dominio/objetos/CodigoVerificacion.md#políticas), [P-USU-012](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-10, RF-006 |
| RN-078 | Todo número celular deberá ser validado. | [P-CVE-001](../01-modelo-dominio/objetos/CodigoVerificacion.md#políticas), [P-USU-012](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-10, RF-007 |
| RN-079 | La verificación de correo es obligatoria. | [P-USU-012](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-10, RF-006 |
| RN-080 | La verificación de celular es obligatoria. | [P-USU-012](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-10, RF-007 |
| RN-081 | Si el correo cambia deberá volver a verificarse. | [P-CVE-006](../01-modelo-dominio/objetos/CodigoVerificacion.md#políticas), [P-USU-020](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-10, RF-010 |
| RN-082 | Si el celular cambia deberá volver a verificarse. | [P-CVE-006](../01-modelo-dominio/objetos/CodigoVerificacion.md#políticas), [P-USU-021](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-10, RF-011 |
| RN-083 | La modificación de datos de contacto deberá quedar auditada. | [P-USU-019](../01-modelo-dominio/objetos/Usuario.md#políticas) |  |

### Activación de Cuenta (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-084 | Una cuenta aprobada no implica una cuenta activa. | [P-USU-007](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-USU-014](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-17 |
| RN-085 | Una cuenta activa requiere aprobación institucional. | [P-USU-013](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-USU-015](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-17, RF-078 |
| RN-086 | Una cuenta activa requiere correo validado. | [P-USU-013](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-USU-015](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-17, RF-078 |
| RN-087 | Una cuenta activa requiere celular validado. | [P-USU-013](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-USU-015](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-17, RF-078 |
| RN-088 | Las tres condiciones son obligatorias. | [P-USU-013](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-USU-015](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-17, RF-078 |
| RN-089 | La ausencia de cualquiera de ellas impedirá el ingreso. | [P-USU-017](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-17, RF-078 |

### Flujos de Aprobación (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-090 | Los Estudiantes serán aprobados por Coordinadores de Programa. | [P-SRE-006](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas), [P-SRE-008](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas) | D-03, D-17, RF-012, RF-025, RF-035 |
| RN-091 | Los Coordinadores de Programa serán aprobados por Coordinadores de Facultad. | [P-SRE-006](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas), [P-SRE-008](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas) | D-17, RF-012, RF-036 |
| RN-092 | Los Coordinadores de Facultad serán aprobados por Coordinadores Académicos. | [P-SRE-006](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas), [P-SRE-008](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas) | D-17, RF-012, RF-037 |
| RN-093 | Los Coordinadores Académicos serán aprobados por el Rector. | [P-SRE-006](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas), [P-SRE-008](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas) | D-17, RF-012, RF-038 |
| RN-094 | Los Rectores serán registrados por Administradores. | [P-SRE-001](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas), [P-USU-009](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-17, RF-012, RF-039 |
| RN-095 | Los Administradores serán registrados por otros Administradores. | [P-SRE-001](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas), [P-USU-009](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-17, D-18, RF-012, RF-040 |

### Auditoría (doc. 03)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-096 | Toda aprobación deberá quedar auditada. | [P-ASR-005](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-AUD-001](../01-modelo-dominio/objetos/Auditoria.md#políticas), [P-SRE-009](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas) | D-26, RF-069 |
| RN-097 | Toda modificación de datos deberá quedar auditada. | [P-AUD-001](../01-modelo-dominio/objetos/Auditoria.md#políticas), [P-USU-019](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-26, RF-069 |
| RN-098 | Toda activación deberá quedar auditada. | [P-AUD-001](../01-modelo-dominio/objetos/Auditoria.md#políticas) | D-26, RF-069 |
| RN-099 | Toda desactivación deberá quedar auditada. | [P-AUD-001](../01-modelo-dominio/objetos/Auditoria.md#políticas), [P-USU-026](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-26, RF-069 |
| RN-100 | Toda acción administrativa relevante deberá conservar trazabilidad histórica. | [P-AUD-001](../01-modelo-dominio/objetos/Auditoria.md#políticas) | D-26, D-28, D-31, RF-069 |

### Gestión de Información Personal (doc. 04)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-101 | Todo usuario podrá consultar su propia información personal. | [P-USU-027](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-008 |
| RN-102 | Todo usuario podrá actualizar su propia información personal. | [P-USU-001](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-009 |
| RN-103 | Todo usuario podrá consultar el estado de sus validaciones. | *Usuario: métodos de actualización que publican su evento* | RF-081 |
| RN-104 | Todo usuario podrá actualizar su correo institucional. | *Usuario: métodos de actualización que publican su evento* | RF-009 |
| RN-105 | Todo usuario podrá actualizar su número celular. | *Usuario: métodos de actualización que publican su evento* | RF-009 |
| RN-106 | Todo usuario podrá consultar su historial de actividad. | [P-AUD-006](../01-modelo-dominio/objetos/Auditoria.md#políticas) | RF-082 ⛔ |
| RN-107 | Ningún usuario podrá modificar directamente su rol institucional. | [P-USU-018](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-009 |
| RN-108 | Ningún usuario podrá aprobarse a sí mismo. | [P-SRE-007](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas), [P-USU-024](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-17, RF-088 |
| RN-109 | Toda modificación realizada por un usuario sobre su información personal deberá registrarse en auditoría. | [P-USU-019](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-009 |
| RN-110 | La actualización del correo institucional generará una nueva validación pendiente. | [P-USU-020](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-010 |
| RN-111 | La actualización del número celular generará una nueva validación pendiente. | [P-USU-021](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-011 |

### Coordinador de Programa (doc. 04)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-112 | Un Coordinador de Programa podrá gestionar información únicamente de estudiantes pertenecientes a su Programa Académico. | [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-VPR-003](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | RF-008, RF-026, RF-087 |
| RN-113 | Un Coordinador de Programa podrá gestionar información únicamente de docentes asociados a su Programa Académico. | [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-VPR-003](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | RF-008, RF-031, RF-087 |
| RN-114 | Un Coordinador de Programa no podrá gestionar estudiantes pertenecientes exclusivamente a otros Programas Académicos. | [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-VPR-003](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | RF-008, RF-085, RF-087 |
| RN-115 | Un Coordinador de Programa no podrá gestionar docentes asociados exclusivamente a otros Programas Académicos. | [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-VPR-003](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | RF-008, RF-085, RF-087 |
| RN-116 | Debe existir trazabilidad de todas las acciones realizadas por un Coordinador de Programa. | [P-AUD-004](../01-modelo-dominio/objetos/Auditoria.md#políticas), [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-008, RF-087, RF-070 |

### Coordinador de Facultad (doc. 04)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-117 | Un Coordinador de Facultad podrá gestionar únicamente Coordinadores de Programa pertenecientes a los Programas Académicos de su Facultad. | [P-SRE-006](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas), [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-008, RF-036, RF-087 |
| RN-118 | Un Coordinador de Facultad podrá consultar todos los Programas Académicos de su Facultad. | [P-PAC-008](../01-modelo-dominio/objetos/ProgramaAcademico.md#políticas), [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-VPR-009](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | RF-008, RF-021, RF-087 |
| RN-119 | Un Coordinador de Facultad podrá consultar los Coordinadores de Programa de su Facultad. | [P-FAC-008](../01-modelo-dominio/objetos/Facultad.md#políticas), [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-008, RF-087 |
| RN-120 | Un Coordinador de Facultad no podrá gestionar Coordinadores pertenecientes a otras Facultades. | [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-008, RF-087 |

### Coordinador Académico (doc. 04)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-121 | Un Coordinador Académico podrá gestionar Coordinadores de Facultad de toda la Universidad. | [P-FAC-003](../01-modelo-dominio/objetos/Facultad.md#políticas), [P-SRE-006](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas), [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-008, RF-037, RF-087 |
| RN-122 | Un Coordinador Académico podrá consultar todas las Facultades. | [P-FAC-003](../01-modelo-dominio/objetos/Facultad.md#políticas), [P-FAC-008](../01-modelo-dominio/objetos/Facultad.md#políticas), [P-PAC-008](../01-modelo-dominio/objetos/ProgramaAcademico.md#políticas), [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-008, RF-016, RF-087 |
| RN-123 | Un Coordinador Académico podrá consultar todos los Coordinadores de Facultad. | [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-008, RF-087 |
| RN-124 | Toda modificación realizada sobre Coordinadores de Facultad deberá quedar auditada. | [P-ASR-005](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-008, RF-087 |

### Rector (doc. 04)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-125 | El Rector podrá gestionar exclusivamente Coordinadores Académicos. | [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-008, RF-087 |
| RN-126 | El Rector podrá aprobar registros de Coordinadores Académicos. | [P-SRE-006](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas), [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-008, RF-038, RF-087 |
| RN-127 | El Rector podrá consultar información de los Coordinadores Académicos. | [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-008, RF-087 |
| RN-128 | Toda gestión realizada por el Rector deberá quedar registrada en auditoría. | [P-AUD-004](../01-modelo-dominio/objetos/Auditoria.md#políticas), [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-070 |

### Administrador (doc. 04)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-129 | Los Administradores gestionarán Administradores y Rectores. | [P-USU-009](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-039, RF-040 |
| RN-130 | Los Administradores gestionarán parámetros globales de la plataforma. | [P-PAR-001](../01-modelo-dominio/objetos/ParametroGlobal.md#políticas), [P-PAR-004](../01-modelo-dominio/objetos/ParametroGlobal.md#políticas) | D-11, RF-097 ⛔ |
| RN-131 | Los Administradores gestionarán configuraciones técnicas del sistema. | [P-PAR-001](../01-modelo-dominio/objetos/ParametroGlobal.md#políticas), [P-PAR-003](../01-modelo-dominio/objetos/ParametroGlobal.md#políticas) | RF-097 ⛔ |

### Gestión de Categorías de Kudos (doc. 04)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-132 | Las categorías de Kudos representan clasificaciones institucionales de reconocimiento. | [P-CAT-001](../01-modelo-dominio/objetos/Categoria.md#políticas) |  |
| RN-133 | Una categoría podrá encontrarse activa o inactiva. | [P-CAT-004](../01-modelo-dominio/objetos/Categoria.md#políticas) |  |
| RN-134 | Solo los Administradores podrán crear categorías. | [P-CAT-003](../01-modelo-dominio/objetos/Categoria.md#políticas) | D-01, RF-041 |
| RN-135 | El Rector podrá crear categorías. | [P-CAT-003](../01-modelo-dominio/objetos/Categoria.md#políticas) | D-01, RF-041 |
| RN-136 | El Coordinador Académico podrá crear categorías. | [P-CAT-003](../01-modelo-dominio/objetos/Categoria.md#políticas) | D-01, RF-041 |
| RN-137 | Los Coordinadores de Facultad no podrán crear categorías. | [P-CAT-003](../01-modelo-dominio/objetos/Categoria.md#políticas) | D-01, RF-041 |
| RN-138 | Los Coordinadores de Programa no podrán crear categorías. | [P-CAT-003](../01-modelo-dominio/objetos/Categoria.md#políticas) | D-01, RF-041 |
| RN-139 | Los Docentes no podrán crear categorías. | [P-CAT-003](../01-modelo-dominio/objetos/Categoria.md#políticas) | D-01, RF-041 |
| RN-140 | Los Estudiantes no podrán crear categorías. | [P-CAT-003](../01-modelo-dominio/objetos/Categoria.md#políticas) | D-01, RF-041 |
| RN-141 | Solo Administradores, Rector y Coordinador Académico podrán modificar categorías. | [P-CAT-003](../01-modelo-dominio/objetos/Categoria.md#políticas) | D-01, RF-042 |
| RN-142 | Solo Administradores, Rector y Coordinador Académico podrán activar categorías. | [P-CAT-003](../01-modelo-dominio/objetos/Categoria.md#políticas), [P-CAT-006](../01-modelo-dominio/objetos/Categoria.md#políticas) | D-01, RF-043 |
| RN-143 | Solo Administradores, Rector y Coordinador Académico podrán desactivar categorías. | [P-CAT-003](../01-modelo-dominio/objetos/Categoria.md#políticas), [P-CAT-007](../01-modelo-dominio/objetos/Categoria.md#políticas) | D-01, RF-044 |
| RN-144 | Las categorías desactivadas no podrán utilizarse para nuevos Kudos. | [P-CAT-007](../01-modelo-dominio/objetos/Categoria.md#políticas), [P-KUD-003](../01-modelo-dominio/objetos/Kudo.md#políticas) | RF-044 |
| RN-145 | Los Kudos históricos conservarán la categoría utilizada en el momento de su creación. | [P-CAT-005](../01-modelo-dominio/objetos/Categoria.md#políticas) | RF-042, RF-051 |

### Gestión de Kudos (doc. 04)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-146 | Un Kudo representa un reconocimiento institucional. | *Kudo + EstadoContenido (State) + ValidarContenidoRule (D-19, D-20)* | D-19, RF-046 |
| RN-147 | Todo Kudo deberá tener un autor. | [P-KUD-001](../01-modelo-dominio/objetos/Kudo.md#políticas) | D-19, RF-046 |
| RN-148 | Todo Kudo deberá tener un destinatario. | [P-KUD-002](../01-modelo-dominio/objetos/Kudo.md#políticas) | D-19, RF-046 |
| RN-149 | Todo Kudo deberá estar asociado a una categoría. | [P-KUD-003](../01-modelo-dominio/objetos/Kudo.md#políticas) | D-19, RF-046, RF-051 |
| RN-150 | Todo Kudo deberá registrar fecha de creación. | [P-KUD-005](../01-modelo-dominio/objetos/Kudo.md#políticas) | D-19, RF-046 |
| RN-151 | Todo Kudo deberá conservar trazabilidad histórica. | *Kudo + EstadoContenido (State) + ValidarContenidoRule (D-19, D-20)* | D-19 |
| RN-152 | El contenido textual de un Kudo no tendrá límite de longitud. | [P-KUD-004](../01-modelo-dominio/objetos/Kudo.md#políticas) | D-19, RF-052 |
| RN-153 | El sistema no podrá truncar automáticamente el contenido de un Kudo. | [P-KUD-004](../01-modelo-dominio/objetos/Kudo.md#políticas) | D-19, RF-052 |
| RN-154 | El contenido completo deberá permanecer disponible para consulta. | [P-KUD-008](../01-modelo-dominio/objetos/Kudo.md#políticas) | D-19, RF-047 |
| RN-155 | Todo Kudo podrá ser visualizado por la comunidad autenticada. | [P-KUD-008](../01-modelo-dominio/objetos/Kudo.md#políticas), [P-KUD-011](../01-modelo-dominio/objetos/Kudo.md#políticas) | RF-047, RF-090 |
| RN-156 | Todo Kudo podrá recibir réplicas. | *Kudo + EstadoContenido (State) + ValidarContenidoRule (D-19, D-20)* | RF-053 |
| RN-157 | Todo Kudo podrá recibir calificaciones. | *Kudo + EstadoContenido (State) + ValidarContenidoRule (D-19, D-20)* | RF-057 |
| RN-158 | Todo Kudo podrá ser reportado. | [P-RPT-002](../01-modelo-dominio/objetos/Reporte.md#políticas) | RF-061 |
| RN-159 | Todo Kudo podrá ser vetado conforme a las políticas institucionales. | *Kudo + EstadoContenido (State) + ValidarContenidoRule (D-19, D-20)* | RF-065 |
| RN-160 | Las operaciones asociadas al ciclo de vida de un Kudo deberán generar eventos auditables. | [P-AUD-001](../01-modelo-dominio/objetos/Auditoria.md#políticas) | RF-069 |

### Gestión de Réplicas (doc. 04)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-161 | Los usuarios podrán crear réplicas sobre un Kudo. | *Replica (árbol por idReplicaPadre) + EstadoContenido (D-20, D-22)* | RF-053 |
| RN-162 | Los usuarios podrán crear réplicas sobre otras réplicas. | *Replica (árbol por idReplicaPadre) + EstadoContenido (D-20, D-22)* | RF-054 |
| RN-163 | Las conversaciones podrán crecer indefinidamente. | [P-RPL-008](../01-modelo-dominio/objetos/Replica.md#políticas) | RF-056 |
| RN-164 | No existirá límite en la profundidad de una conversación. | [P-RPL-008](../01-modelo-dominio/objetos/Replica.md#políticas) | RF-056 |
| RN-165 | No existirá límite en la cantidad de réplicas que un usuario pueda realizar. | *Replica (árbol por idReplicaPadre) + EstadoContenido (D-20, D-22)* | RF-056 |
| RN-166 | Una misma persona podrá realizar múltiples réplicas sobre un mismo Kudo. | *Replica (árbol por idReplicaPadre) + EstadoContenido (D-20, D-22)* | RF-056 |
| RN-167 | Una misma persona podrá realizar múltiples réplicas sobre la misma réplica. | *Replica (árbol por idReplicaPadre) + EstadoContenido (D-20, D-22)* | RF-056 |
| RN-168 | Toda réplica deberá tener un autor. | [P-RPL-001](../01-modelo-dominio/objetos/Replica.md#políticas) |  |
| RN-169 | Toda réplica deberá tener una fecha de creación. | *Replica (árbol por idReplicaPadre) + EstadoContenido (D-20, D-22)* |  |
| RN-170 | El contenido textual de una réplica no tendrá límite de longitud. | [P-RPL-003](../01-modelo-dominio/objetos/Replica.md#políticas) | RF-052 |
| RN-171 | Toda réplica podrá ser calificada. | *Replica (árbol por idReplicaPadre) + EstadoContenido (D-20, D-22)* | RF-058 |
| RN-172 | Toda réplica podrá ser reportada. | [P-RPT-002](../01-modelo-dominio/objetos/Reporte.md#políticas) | RF-062 |
| RN-173 | Toda réplica podrá ser vetada conforme a las políticas institucionales. | *Replica (árbol por idReplicaPadre) + EstadoContenido (D-20, D-22)* | RF-066 |
| RN-174 | Toda réplica deberá conservar historial y trazabilidad. | *Replica (árbol por idReplicaPadre) + EstadoContenido (D-20, D-22)* |  |

### Gestión de Calificaciones (doc. 04)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-175 | Los Kudos podrán recibir calificaciones. | *Calificacion + ValidarEstrellasRule + índices UQ-CAL-1 y UQ-CAL-2 (D-23)* | D-23, RF-057 |
| RN-176 | Las réplicas podrán recibir calificaciones. | *Calificacion + ValidarEstrellasRule + índices UQ-CAL-1 y UQ-CAL-2 (D-23)* | D-23, RF-058 |
| RN-177 | La escala de calificación será de 1 a 5 estrellas. | [P-CAL-005](../01-modelo-dominio/objetos/Calificacion.md#políticas) | D-23, RF-059 |
| RN-178 | Una persona podrá calificar un mismo Kudo solamente una vez. | [P-CAL-006](../01-modelo-dominio/objetos/Calificacion.md#políticas) | D-23, RF-060 |
| RN-179 | Una persona podrá calificar una misma réplica solamente una vez. | [P-CAL-006](../01-modelo-dominio/objetos/Calificacion.md#políticas) | D-23, RF-060 |
| RN-180 | No podrán existir dos calificaciones activas de una misma persona para un mismo Kudo. | [P-CAL-006](../01-modelo-dominio/objetos/Calificacion.md#políticas) | D-23, RF-060 |
| RN-181 | No podrán existir dos calificaciones activas de una misma persona para una misma réplica. | [P-CAL-006](../01-modelo-dominio/objetos/Calificacion.md#políticas) | D-23, RF-060 |
| RN-182 | Toda calificación deberá registrar la fecha en que fue realizada. | *Calificacion + ValidarEstrellasRule + índices UQ-CAL-1 y UQ-CAL-2 (D-23)* | D-23 |
| RN-183 | Toda calificación deberá registrar quién la realizó. | [P-CAL-001](../01-modelo-dominio/objetos/Calificacion.md#políticas) | D-23 |
| RN-184 | Toda calificación deberá asociarse al contenido calificado. | [P-CAL-002](../01-modelo-dominio/objetos/Calificacion.md#políticas) | D-23 |
| RN-185 | Las calificaciones deberán participar en el cálculo de indicadores definidos por el equipo. | [P-CAL-007](../01-modelo-dominio/objetos/Calificacion.md#políticas), [P-KUD-017](../01-modelo-dominio/objetos/Kudo.md#políticas), [P-RPL-013](../01-modelo-dominio/objetos/Replica.md#políticas) | D-23, RF-092, RF-096 ⛔ |

### Reportes de Contenido (doc. 04)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-186 | Cualquier usuario autenticado podrá reportar un Kudo. | [P-RPT-001](../01-modelo-dominio/objetos/Reporte.md#políticas) | D-24, RF-061 |
| RN-187 | Cualquier usuario autenticado podrá reportar una réplica. | [P-RPT-001](../01-modelo-dominio/objetos/Reporte.md#políticas) | D-24, RF-062 |
| RN-188 | Todo reporte deberá registrar la persona que reporta. | *Reporte con MotivoReporte (D-24)* | D-24, RF-063 |
| RN-189 | Todo reporte deberá registrar la fecha. | *Reporte con MotivoReporte (D-24)* | D-24, RF-063 |
| RN-190 | Todo reporte deberá registrar un motivo. | [P-RPT-004](../01-modelo-dominio/objetos/Reporte.md#políticas) | D-24, RF-063 |

### Moderación (doc. 04)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-191 | Los Kudos podrán ser vetados por razones institucionales. | *Moderacion + AsegurarActorTieneRolRule: solo Rector y Coord. Académico (D-02)* | RF-065 |
| RN-192 | Las réplicas podrán ser vetadas por razones institucionales. | *Moderacion + AsegurarActorTieneRolRule: solo Rector y Coord. Académico (D-02)* | RF-066 |
| RN-193 | Solo el Rector podrá vetar cualquier Kudo de la plataforma. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-02, RF-065 |
| RN-194 | Solo el Coordinador Académico podrá vetar cualquier Kudo de la plataforma. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-02, RF-065 |
| RN-195 | Solo el Rector podrá vetar cualquier réplica de la plataforma. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-02, RF-066 |
| RN-196 | Solo el Coordinador Académico podrá vetar cualquier réplica de la plataforma. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-02, RF-066 |
| RN-197 | Los Administradores no podrán vetar contenido. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-02, RF-067 |
| RN-198 | Los Coordinadores de Facultad no podrán vetar contenido. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-02, RF-067 |
| RN-199 | Los Coordinadores de Programa no podrán vetar contenido. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-02, RF-067 |
| RN-200 | Docentes y Estudiantes no podrán vetar contenido. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-02, RF-067 |

### Estados del Kudo (doc. 05)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-201 | Todo Kudo inicia su ciclo de vida en estado creado. | [P-KUD-006](../01-modelo-dominio/objetos/Kudo.md#políticas) | D-20 |
| RN-202 | Un Kudo visible puede ser reportado. | [P-KUD-013](../01-modelo-dominio/objetos/Kudo.md#políticas) | D-20, RF-061 |
| RN-203 | Un Kudo reportado puede ingresar a revisión. | [P-KUD-014](../01-modelo-dominio/objetos/Kudo.md#políticas), [P-MOD-002](../01-modelo-dominio/objetos/Moderacion.md#políticas), [P-MOD-003](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-20, RF-093 |
| RN-204 | Un Kudo puede ser vetado. | [P-KUD-016](../01-modelo-dominio/objetos/Kudo.md#políticas), [P-MOD-004](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-20 |
| RN-205 | Un Kudo vetado deja de estar disponible para interacción normal. | [P-KUD-009](../01-modelo-dominio/objetos/Kudo.md#políticas), [P-KUD-016](../01-modelo-dominio/objetos/Kudo.md#políticas), [P-KUD-018](../01-modelo-dominio/objetos/Kudo.md#políticas), [P-MOD-006](../01-modelo-dominio/objetos/Moderacion.md#políticas), [P-RPL-002](../01-modelo-dominio/objetos/Replica.md#políticas) | D-20, D-21 |

### Estados de una Réplica (doc. 05)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-206 | Toda réplica inicia como creada. | [P-RPL-004](../01-modelo-dominio/objetos/Replica.md#políticas) | D-20 |
| RN-207 | Una réplica visible puede ser reportada. | [P-RPL-009](../01-modelo-dominio/objetos/Replica.md#políticas) | D-20, RF-062 |
| RN-208 | Una réplica reportada puede pasar a revisión. | [P-MOD-002](../01-modelo-dominio/objetos/Moderacion.md#políticas), [P-MOD-003](../01-modelo-dominio/objetos/Moderacion.md#políticas), [P-RPL-010](../01-modelo-dominio/objetos/Replica.md#políticas) | D-20, RF-093 |
| RN-209 | Una réplica puede ser vetada. | [P-MOD-004](../01-modelo-dominio/objetos/Moderacion.md#políticas), [P-RPL-012](../01-modelo-dominio/objetos/Replica.md#políticas) | D-20 |

### Estados de un Reporte (doc. 05)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-210 | Todo reporte inicia en estado pendiente. | [P-RPT-006](../01-modelo-dominio/objetos/Reporte.md#políticas) | D-24 |
| RN-211 | Un reporte pendiente podrá ser analizado. | [P-MOD-003](../01-modelo-dominio/objetos/Moderacion.md#políticas), [P-RPT-007](../01-modelo-dominio/objetos/Reporte.md#políticas) | D-24, RF-093 |
| RN-212 | Una decisión deberá producir aceptación o rechazo. | [P-MOD-006](../01-modelo-dominio/objetos/Moderacion.md#políticas), [P-MOD-008](../01-modelo-dominio/objetos/Moderacion.md#políticas), [P-RPT-008](../01-modelo-dominio/objetos/Reporte.md#políticas), [P-RPT-009](../01-modelo-dominio/objetos/Reporte.md#políticas) | D-24, RF-086, RF-094 |

### Eventos de Usuarios (doc. 05)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-213 | Debe generarse un evento cuando un usuario se registra. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-214 | Debe generarse un evento cuando un usuario es aprobado. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-215 | Debe generarse un evento cuando una cuenta es activada. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-216 | Debe generarse un evento cuando una cuenta es suspendida. | [P-USU-023](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-07, RF-089 ⛔ |
| RN-217 | Debe generarse un evento cuando una cuenta es desactivada. | [P-USU-026](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-07, RF-089 ⛔ |

### Eventos de Facultades (doc. 05)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-218 | Debe generarse un evento cuando una Facultad es creada. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-219 | Debe generarse un evento cuando se asigna un Coordinador de Facultad. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-220 | Debe generarse un evento cuando se reemplaza un Coordinador de Facultad. | [P-ASR-004](../01-modelo-dominio/objetos/AsignacionRol.md#políticas) | D-15 |

### Eventos de Programas (doc. 05)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-221 | Debe generarse un evento cuando se crea un Programa Académico. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-222 | Debe generarse un evento cuando se asigna un Coordinador de Programa. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-223 | Debe generarse un evento cuando se reemplaza un Coordinador de Programa. | [P-ASR-004](../01-modelo-dominio/objetos/AsignacionRol.md#políticas) | D-15 |

### Eventos de Categorías (doc. 05)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-224 | Debe generarse un evento cuando una categoría es creada. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-225 | Debe generarse un evento cuando una categoría es modificada. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-226 | Debe generarse un evento cuando una categoría es activada. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-227 | Debe generarse un evento cuando una categoría es desactivada. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |

### Eventos de Kudos (doc. 05)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-228 | Debe generarse un evento cuando se publica un Kudo. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-229 | Debe generarse un evento cuando un Kudo es consultado. | [P-KUD-010](../01-modelo-dominio/objetos/Kudo.md#políticas) | D-25, RF-047 |
| RN-230 | Debe generarse un evento cuando un Kudo es reportado. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-231 | Debe generarse un evento cuando un Kudo es vetado. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |

### Eventos de Réplicas (doc. 05)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-232 | Debe generarse un evento cuando una réplica es publicada. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-233 | Debe generarse un evento cuando una réplica recibe una respuesta. | [P-RPL-007](../01-modelo-dominio/objetos/Replica.md#políticas) |  |
| RN-234 | Debe generarse un evento cuando una réplica es reportada. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-235 | Debe generarse un evento cuando una réplica es vetada. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |

### Eventos de Calificaciones (doc. 05)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-236 | Debe generarse un evento cuando una calificación es registrada. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-237 | Debe generarse un evento cuando se modifica una calificación. | [P-CAL-008](../01-modelo-dominio/objetos/Calificacion.md#políticas) | RF-091 |
| RN-238 | Debe generarse un evento cuando se recalculan indicadores derivados de calificaciones. | [P-CAL-007](../01-modelo-dominio/objetos/Calificacion.md#políticas), [P-KUD-017](../01-modelo-dominio/objetos/Kudo.md#políticas), [P-RPL-013](../01-modelo-dominio/objetos/Replica.md#políticas) | RF-092 |

### Eventos de Moderación (doc. 05)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-239 | Debe generarse un evento cuando se inicia una revisión. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* | RF-093 |
| RN-240 | Debe generarse un evento cuando una revisión es cerrada. | [P-MOD-007](../01-modelo-dominio/objetos/Moderacion.md#políticas) | RF-094 |
| RN-241 | Debe generarse un evento cuando un contenido es vetado. | *Evento de dominio publicado por el objeto responsable (ver catálogo de eventos)* |  |
| RN-242 | Debe generarse un evento cuando un contenido es liberado tras revisión. | [P-KUD-015](../01-modelo-dominio/objetos/Kudo.md#políticas), [P-MOD-008](../01-modelo-dominio/objetos/Moderacion.md#políticas), [P-RPL-011](../01-modelo-dominio/objetos/Replica.md#políticas) | RF-094 |

### Restricciones Generales de Moderación (doc. 05)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-243 | Solamente Rector y Coordinador Académico pueden vetar contenido. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-02, RF-067 |
| RN-244 | Los Administradores no participan en decisiones de moderación. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | RF-067 |
| RN-245 | Los Coordinadores de Facultad no participan en decisiones de moderación. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | RF-067 |
| RN-246 | Los Coordinadores de Programa no participan en decisiones de moderación. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | RF-067 |
| RN-247 | Docentes no participan en decisiones de moderación. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | RF-067 |
| RN-248 | Estudiantes no participan en decisiones de moderación. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | RF-067 |

### Trazabilidad (doc. 05)

| Código | Regla | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| RN-249 | Toda decisión de moderación deberá quedar auditada. | [P-MOD-006](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-26, RF-068 |
| RN-250 | Toda auditoría deberá conservar la evidencia necesaria para reconstruir el proceso de decisión aplicado al contenido. | [P-AUD-003](../01-modelo-dominio/objetos/Auditoria.md#políticas), [P-MOD-005](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-26, RF-068 |

## 2. Invariantes


### Invariantes Organizacionales

| Código | Invariante | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| INV-001 | Solo puede existir un Rector activo. | [P-ASR-002](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-USU-010](../01-modelo-dominio/objetos/Usuario.md#políticas) | D-14, RF-014, RF-039, RF-076 |
| INV-002 | Solo puede existir un Coordinador Académico activo. | [P-ASR-002](../01-modelo-dominio/objetos/AsignacionRol.md#políticas) | D-14, RF-014, RF-077 |
| INV-003 | Cada Facultad posee un único Coordinador de Facultad activo. | *AsignacionRol con índices únicos filtrados UQ-ASR-1 a UQ-ASR-5 (D-13, D-14)* | D-14, RF-014 |
| INV-004 | Cada Programa Académico posee un único Coordinador de Programa activo. | *AsignacionRol con índices únicos filtrados UQ-ASR-1 a UQ-ASR-5 (D-13, D-14)* | D-14, RF-014 |
| INV-005 | Una persona solamente podrá tener un rol activo. | [P-ASR-001](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-USU-029](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-013, RF-014 |
| INV-006 | Los roles son mutuamente excluyentes. | [P-ASR-001](../01-modelo-dominio/objetos/AsignacionRol.md#políticas) | RF-014 |
| INV-007 | La estructura organizacional debe permanecer consistente en todo momento. | *Facultad (estados), AsignacionRol coherente con una Facultad o Programa activo, índices únicos filtrados y una transacción por caso de uso en la fachada (D-05, D-13, D-14, D-30)* |  |

### Invariantes Relacionados con Programas

| Código | Invariante | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| INV-008 | Todo Programa Académico debe pertenecer a una Facultad. | [P-PAC-003](../01-modelo-dominio/objetos/ProgramaAcademico.md#políticas) |  |
| INV-009 | Todo Coordinador de Programa debe estar asociado a exactamente un Programa Académico activo. | [P-ASR-003](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-PAC-006](../01-modelo-dominio/objetos/ProgramaAcademico.md#políticas), [P-SRE-002](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas) | D-06, RF-074 |
| INV-010 | Todo Coordinador de Facultad debe estar asociado a exactamente una Facultad activa. | [P-ASR-003](../01-modelo-dominio/objetos/AsignacionRol.md#políticas), [P-FAC-006](../01-modelo-dominio/objetos/Facultad.md#políticas), [P-SRE-002](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas) | D-05, RF-075 |

### Invariantes Relacionados con Estudiantes

| Código | Invariante | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| INV-011 | Un estudiante puede pertenecer a múltiples Programas Académicos. | *VinculacionPrograma con vigencias (D-16, D-28)* |  |
| INV-012 | La vinculación entre un estudiante y un programa debe ser trazable. | *VinculacionPrograma con vigencias (D-16, D-28)* | RF-029 |
| INV-013 | La desvinculación no elimina la historia académica. | [P-VPR-006](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | RF-029 |

### Invariantes Relacionados con Docentes

| Código | Invariante | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| INV-014 | Un docente puede pertenecer a múltiples Programas Académicos. | *VinculacionPrograma con vigencias (D-16, D-28)* |  |
| INV-015 | Todo vínculo docente-programa debe conservar historial. | *VinculacionPrograma con vigencias (D-16, D-28)* | RF-034 |
| INV-016 | La eliminación de un vínculo no implica eliminación de la trazabilidad. | [P-VPR-006](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | RF-034 |

### Invariantes de Categorías

| Código | Invariante | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| INV-017 | Todo Kudo debe pertenecer a una categoría. | *Kudo, Categoria y ValidarContenidoRule (D-19)* | RF-051 |
| INV-018 | Las categorías históricas deben mantenerse disponibles para consulta. | [P-CAT-005](../01-modelo-dominio/objetos/Categoria.md#políticas), [P-CAT-008](../01-modelo-dominio/objetos/Categoria.md#políticas) | RF-045 |
| INV-019 | Una categoría inactiva no puede utilizarse para nuevos Kudos. | [P-CAT-007](../01-modelo-dominio/objetos/Categoria.md#políticas), [P-KUD-003](../01-modelo-dominio/objetos/Kudo.md#políticas) |  |

### Invariantes de Kudos

| Código | Invariante | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| INV-020 | Todo Kudo tiene exactamente un autor. | *Kudo, Categoria y ValidarContenidoRule (D-19)* |  |
| INV-021 | Todo Kudo tiene exactamente un destinatario. | *Kudo, Categoria y ValidarContenidoRule (D-19)* |  |
| INV-022 | Todo Kudo posee fecha de creación. | [P-KUD-005](../01-modelo-dominio/objetos/Kudo.md#políticas) |  |
| INV-023 | Todo Kudo pertenece a una categoría válida. | *Kudo, Categoria y ValidarContenidoRule (D-19)* |  |
| INV-024 | Todo Kudo debe conservar su contenido completo. | [P-KUD-004](../01-modelo-dominio/objetos/Kudo.md#políticas) | RF-052 |

### Invariantes de Réplicas

| Código | Invariante | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| INV-025 | Toda réplica tiene exactamente un autor. | *Replica: padre fijo, sin ciclos por construcción (D-22)* | D-22 |
| INV-026 | Toda réplica tiene exactamente un padre. | [P-RPL-006](../01-modelo-dominio/objetos/Replica.md#políticas) | D-22 |
| INV-027 | El padre de una réplica puede ser: - Un Kudo. - Otra réplica. | [P-RPL-006](../01-modelo-dominio/objetos/Replica.md#políticas) | D-22 |
| INV-028 | Una réplica no puede referenciarse a sí misma. | [P-RPL-006](../01-modelo-dominio/objetos/Replica.md#políticas) | D-22 |
| INV-029 | No deben existir ciclos dentro del árbol de conversaciones. | [P-RPL-006](../01-modelo-dominio/objetos/Replica.md#políticas) | D-22 |

### Invariantes de Calificaciones

| Código | Invariante | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| INV-030 | La escala de valoración es de 1 a 5 estrellas. | [P-CAL-005](../01-modelo-dominio/objetos/Calificacion.md#políticas) | RF-059 |
| INV-031 | Un usuario puede tener una única calificación activa por Kudo. | [P-CAL-006](../01-modelo-dominio/objetos/Calificacion.md#políticas) | RF-060 |
| INV-032 | Un usuario puede tener una única calificación activa por réplica. | [P-CAL-006](../01-modelo-dominio/objetos/Calificacion.md#políticas) | RF-060 |

### Invariantes de Moderación

| Código | Invariante | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| INV-033 | Solo Rector y Coordinador Académico pueden vetar contenido. | [P-MOD-001](../01-modelo-dominio/objetos/Moderacion.md#políticas) | RF-067 |
| INV-034 | Todo veto debe quedar auditado. | [P-MOD-006](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-21 |
| INV-035 | Todo contenido vetado debe conservar trazabilidad. | [P-MOD-006](../01-modelo-dominio/objetos/Moderacion.md#políticas) | D-21, RF-068 |

## 3. Políticas institucionales

| Código | Política | Dónde se garantiza | Decisión / requisito |
|---|---|---|---|
| POL-001 | Todas las cuentas deberán estar asociadas a un correo institucional válido. | [P-USU-002](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-002 |
| POL-002 | Todas las cuentas deberán estar asociadas a un número celular válido. | [P-USU-004](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-003 |
| POL-003 | Toda cuenta deberá validar su identidad antes de ingresar al sistema. | [P-USU-015](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-USU-017](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-078 |
| POL-004 | Cada actor actúa únicamente dentro de su ámbito de responsabilidad. | [P-SRE-006](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas), [P-SRE-011](../01-modelo-dominio/objetos/SolicitudRegistro.md#políticas), [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-USU-027](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-VPR-003](../01-modelo-dominio/objetos/VinculacionPrograma.md#políticas) | RF-087, RF-088 |
| POL-005 | Ninguna autoridad podrá ejercer funciones superiores a las definidas en su rol. | [P-USU-022](../01-modelo-dominio/objetos/Usuario.md#políticas), [P-USU-024](../01-modelo-dominio/objetos/Usuario.md#políticas) | RF-088 |
| POL-006 | Las operaciones críticas deberán ser rastreables. | [P-AUD-001](../01-modelo-dominio/objetos/Auditoria.md#políticas) | RF-069 |
| POL-007 | Las decisiones institucionales deben poder reconstruirse históricamente. | [P-AUD-002](../01-modelo-dominio/objetos/Auditoria.md#políticas), [P-AUD-003](../01-modelo-dominio/objetos/Auditoria.md#políticas) | D-28, RF-071 |

## 4. Eventos de referencia

Cada evento del enunciado lo produce una responsabilidad de un objeto del dominio. El detalle (actor, información y políticas) está en la sección **Eventos** de cada objeto y en el [catálogo de eventos](../01-modelo-dominio/README.md#catálogo-de-eventos-de-dominio).


### Eventos de Registro

| Código | Evento del enunciado | Evento de dominio (objeto que lo produce) |
|---|---|---|
| EVT-001 | Usuario registrado. | Usuario registrado (Usuario); Usuario registrado y Rector asignado (Usuario) |
| EVT-002 | Correo verificado. | Correo o celular verificado (CodigoVerificacion); Correo verificado (Usuario) |
| EVT-003 | Celular verificado. | Correo o celular verificado (CodigoVerificacion); Celular verificado (Usuario) |
| EVT-004 | Usuario aprobado. | Usuario registrado y Rector asignado (Usuario); Usuario aprobado (Usuario); Usuario aprobado (SolicitudRegistro) |
| EVT-005 | Cuenta activada. | Cuenta activada (Usuario) |
| EVT-006 | Cuenta suspendida. | Cuenta suspendida (Usuario) |
| EVT-007 | Cuenta desactivada. | Cuenta desactivada (Usuario) |

### Eventos Organizacionales

| Código | Evento del enunciado | Evento de dominio (objeto que lo produce) |
|---|---|---|
| EVT-008 | Facultad creada. | Facultad creada (Facultad) |
| EVT-009 | Programa creado. | Programa creado (ProgramaAcademico) |
| EVT-010 | Coordinador de Facultad asignado. | Rol asignado / coordinador asignado (AsignacionRol) |
| EVT-011 | Coordinador de Facultad reemplazado. | Asignación cerrada / coordinador reemplazado (AsignacionRol) |
| EVT-012 | Coordinador de Programa asignado. | Rol asignado / coordinador asignado (AsignacionRol) |
| EVT-013 | Coordinador de Programa reemplazado. | Asignación cerrada / coordinador reemplazado (AsignacionRol) |
| EVT-014 | Coordinador Académico asignado. | Rol asignado / coordinador asignado (AsignacionRol) |
| EVT-015 | Rector asignado. | Rol asignado / coordinador asignado (AsignacionRol); Usuario registrado y Rector asignado (Usuario) |

### Eventos Académicos

| Código | Evento del enunciado | Evento de dominio (objeto que lo produce) |
|---|---|---|
| EVT-016 | Estudiante vinculado a Programa. | Estudiante o docente vinculado a programa (VinculacionPrograma) |
| EVT-017 | Estudiante desvinculado de Programa. | Estudiante o docente desvinculado de programa (VinculacionPrograma) |
| EVT-018 | Docente vinculado a Programa. | Estudiante o docente vinculado a programa (VinculacionPrograma) |
| EVT-019 | Docente desvinculado de Programa. | Estudiante o docente desvinculado de programa (VinculacionPrograma) |

### Eventos de Categorías

| Código | Evento del enunciado | Evento de dominio (objeto que lo produce) |
|---|---|---|
| EVT-020 | Categoría creada. | Categoría creada (Categoria) |
| EVT-021 | Categoría modificada. | Categoría modificada (Categoria) |
| EVT-022 | Categoría activada. | Categoría activada (Categoria) |
| EVT-023 | Categoría desactivada. | Categoría desactivada (Categoria) |

### Eventos de Kudos

| Código | Evento del enunciado | Evento de dominio (objeto que lo produce) |
|---|---|---|
| EVT-024 | Kudo creado. | Kudo creado (publicado) (Kudo) |
| EVT-025 | Kudo consultado. | Kudo consultado (Kudo) |
| EVT-026 | Kudo reportado. | Kudo reportado (Kudo); Kudo reportado / réplica reportada (Reporte) |
| EVT-027 | Kudo vetado. | Kudo vetado (Kudo); Contenido vetado y revisión cerrada (Moderacion) |

### Eventos de Réplicas

| Código | Evento del enunciado | Evento de dominio (objeto que lo produce) |
|---|---|---|
| EVT-028 | Réplica creada. | Réplica creada (Replica); Réplica creada y réplica respondida (Replica) |
| EVT-029 | Réplica respondida. | Réplica creada y réplica respondida (Replica) |
| EVT-030 | Réplica reportada. | Réplica reportada (Replica); Kudo reportado / réplica reportada (Reporte) |
| EVT-031 | Réplica vetada. | Réplica vetada (Replica); Contenido vetado y revisión cerrada (Moderacion) |

### Eventos de Calificaciones

| Código | Evento del enunciado | Evento de dominio (objeto que lo produce) |
|---|---|---|
| EVT-032 | Calificación registrada. | Calificación registrada (Calificacion) |
| EVT-033 | Calificación actualizada. | Calificación actualizada (Calificacion) |
| EVT-034 | Indicador recalculado. | Indicador recalculado (Kudo); Indicador recalculado (Replica) |

### Eventos de Moderación

| Código | Evento del enunciado | Evento de dominio (objeto que lo produce) |
|---|---|---|
| EVT-035 | Revisión iniciada. | Revisión iniciada (Kudo); Revisión iniciada (Replica); Revisión iniciada (Moderacion); Revisión iniciada (Reporte) |
| EVT-036 | Revisión cerrada. | Contenido vetado y revisión cerrada (Moderacion); Contenido liberado y revisión cerrada (Moderacion); Revisión cerrada (Reporte) |
| EVT-037 | Contenido aprobado. | Contenido liberado (Kudo); Contenido liberado (Replica); Contenido liberado y revisión cerrada (Moderacion) |
| EVT-038 | Contenido rechazado. | Contenido vetado y revisión cerrada (Moderacion) |
