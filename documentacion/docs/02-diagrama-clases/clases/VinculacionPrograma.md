# VinculacionPrograma

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: VinculacionPrograma](../../01-modelo-dominio/objetos/VinculacionPrograma.md) · [Entidad MER: VinculacionPrograma](../../03-modelo-entidad-relacion/entidades/VinculacionPrograma.md)

| Campo | Valor |
|---|---|
| Clase | VinculacionPrograma |
| Clase en el código | `VinculacionProgramaDominio` |
| Estereotipo | class (entidad de dominio) |
| Descripción | Vínculo, con historial, entre un Docente o Estudiante y un Programa Académico. Resuelve la relación muchos a muchos y conserva la trazabilidad de vinculaciones y desvinculaciones. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

> El dato calculado `vigente` no es un atributo almacenado: se obtiene con su método de negocio (por ejemplo `estaVigente()`).

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Necesita de | Asociación | Usuario |
| Necesita de | Asociación | ProgramaAcademico |
| Necesita de | Dependencia (enumeración) | TipoVinculo |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único del vínculo |
| private | usuario | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Docente o Estudiante vinculado |
| private | programa | Otro | ProgramaAcademico | final | no static | Instancia por defecto de ProgramaAcademico | Programa al que se vincula |
| private | tipoVinculo | Otro | TipoVinculo | final | no static | DOCENTE | Calidad en la que participa en el programa |
| private | fechaInicio | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora de vinculación |
| private | fechaFin | Otro | LocalDateTime | no final | no static | Fecha por defecto (UtilFecha) | Fecha y hora de desvinculación |
| private | motivoDesvinculacion | String |  | no final | no static | "" (cadena vacía) | Razón de la desvinculación |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | VinculacionPrograma | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getUsuario | getter | Obtiene el valor del atributo usuario. | Otro | Usuario |
| public | getPrograma | getter | Obtiene el valor del atributo programa. | Otro | ProgramaAcademico |
| public | getTipoVinculo | getter | Obtiene el valor del atributo tipoVinculo. | Otro | TipoVinculo |
| public | getFechaInicio | getter | Obtiene el valor del atributo fechaInicio. | Otro | LocalDateTime |
| public | getFechaFin | getter | Obtiene el valor del atributo fechaFin. | Otro | LocalDateTime |
| public | getMotivoDesvinculacion | getter | Obtiene el valor del atributo motivoDesvinculacion. | String |  |

## Métodos de negocio

Métodos **reales** de `VinculacionProgramaDominio` (comportamiento del dominio rico, D-29).

| Modificador de acceso | Nombre | Parámetros | Tipo dato de retorno | Descripción |
|---|---|---|---|---|
| public | estaVigente | — | boolean | Indica si no tiene fecha de fin. |
| public | desvincular | String motivo | void | Cierra el vínculo de una persona con el programa sin borrarlo. |

## Operaciones del caso de uso (capa de negocio)

Responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/VinculacionPrograma.md#responsabilidades) que coordinan varios objetos o consultan la base de datos. Las implementa: VinculacionNegocioImpl.

| Operación | Descripción | Políticas que protege |
|---|---|---|
| vincular | Vincula a un docente o estudiante al programa del coordinador. | P-VPR-001, P-VPR-002, P-VPR-003, P-VPR-004, P-VPR-005 |
| consultarPorUsuario | Lista los programas vigentes e históricos de un docente o estudiante. | P-VPR-008 |
| consultarPorPrograma | Lista los docentes y estudiantes vigentes de un programa. | P-VPR-009 |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | usuario | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | usuario | Usuario | Builder |
| public | programa | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de ProgramaAcademico. | programa | ProgramaAcademico | Builder |
| public | tipoVinculo | no static | Asigna el valor recibido; si es nulo, asigna: DOCENTE. | tipoVinculo | TipoVinculo | Builder |
| public | fechaInicio | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaInicio | LocalDateTime | Builder |
| public | fechaFin | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaFin | LocalDateTime | Builder |
| public | motivoDesvinculacion | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | motivoDesvinculacion | String | Builder |
| public | build | no static | Crea la instancia de VinculacionPrograma con los valores reunidos, usando su constructor privado. | | | VinculacionPrograma |
