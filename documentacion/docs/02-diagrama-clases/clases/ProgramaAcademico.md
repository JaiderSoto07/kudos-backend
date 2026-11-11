# ProgramaAcademico

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: ProgramaAcademico](../../01-modelo-dominio/objetos/ProgramaAcademico.md) · [Entidad MER: ProgramaAcademico](../../03-modelo-entidad-relacion/entidades/ProgramaAcademico.md)

| Campo | Valor |
|---|---|
| Clase | ProgramaAcademico |
| Clase en el código | `ProgramaAcademicoDominio` |
| Estereotipo | class (entidad de dominio) |
| Descripción | Programa de formación que pertenece a exactamente una Facultad y es acompañado por un Coordinador de Programa. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Es usada por | Asociación | AsignacionRol |
| Necesita de | Composición | Facultad |
| Es usada por | Asociación | VinculacionPrograma |
| Es usada por | Asociación | SolicitudRegistro |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único del Programa (RN-047) |
| private | nombre | String |  | no final | no static | "" (cadena vacía) | Nombre oficial del Programa (RN-048) |
| private | facultad | Otro | Facultad | final | no static | Instancia por defecto de Facultad | Facultad a la que pertenece (RN-046, RN-049) |
| private | activo | boolean/Boolean |  | no final | no static | true | Indica si el Programa está activo |
| private | fechaCreacion | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora de creación |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | ProgramaAcademico | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getNombre | getter | Obtiene el valor del atributo nombre. | String |  |
| public | getFacultad | getter | Obtiene el valor del atributo facultad. | Otro | Facultad |
| public | isActivo | getter | Obtiene el valor del atributo activo. | boolean/Boolean |  |
| public | getFechaCreacion | getter | Obtiene el valor del atributo fechaCreacion. | Otro | LocalDateTime |

## Métodos de negocio

Métodos **reales** de `ProgramaAcademicoDominio` (comportamiento del dominio rico, D-29).

| Modificador de acceso | Nombre | Parámetros | Tipo dato de retorno | Descripción |
|---|---|---|---|---|
| public | renombrar | String nuevoNombre | void | Cambia el nombre del programa. |
| public | inactivar | — | void | Saca de operación un programa sin vínculos ni coordinador vigentes. |

## Operaciones del caso de uso (capa de negocio)

Responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/ProgramaAcademico.md#responsabilidades) que coordinan varios objetos o consultan la base de datos. Las implementa: ProgramaNegocioImpl.

| Operación | Descripción | Políticas que protege |
|---|---|---|
| crear | Crea un programa en una Facultad no inactiva. | P-PAC-001, P-PAC-002, P-PAC-003, P-PAC-004, P-PAC-005 |
| consultar | Lista los programas visibles para el actor con su coordinador vigente. | P-PAC-008 |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | nombre | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | nombre | String | Builder |
| public | facultad | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Facultad. | facultad | Facultad | Builder |
| public | activo | no static | Asigna el valor recibido. | activo | boolean | Builder |
| public | fechaCreacion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaCreacion | LocalDateTime | Builder |
| public | build | no static | Crea la instancia de ProgramaAcademico con los valores reunidos, usando su constructor privado. | | | ProgramaAcademico |
