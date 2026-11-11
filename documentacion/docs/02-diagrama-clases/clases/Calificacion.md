# Calificacion

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: Calificacion](../../01-modelo-dominio/objetos/Calificacion.md) · [Entidad MER: Calificacion](../../03-modelo-entidad-relacion/entidades/Calificacion.md)

| Campo | Valor |
|---|---|
| Clase | Calificacion |
| Clase en el código | `CalificacionDominio` |
| Estereotipo | class (entidad de dominio) |
| Descripción | Valoración de 1 a 5 estrellas que un usuario asigna a un Kudo o a una réplica. Cada usuario tiene como máximo una calificación activa por contenido. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Necesita de | Asociación | Kudo |
| Necesita de | Asociación | Replica |
| Necesita de | Asociación | Usuario |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único de la calificación |
| private | autor | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Usuario que califica (RN-183) |
| private | kudo | Otro | Kudo | final | no static | Instancia por defecto de Kudo | Kudo calificado |
| private | replica | Otro | Replica | final | no static | Instancia por defecto de Replica | Réplica calificada |
| private | estrellas | int |  | no final | no static | 0 | Valoración asignada (RN-177) |
| private | fechaCalificacion | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora en que se registró (RN-182) |
| private | fechaModificacion | Otro | LocalDateTime | no final | no static | Fecha por defecto (UtilFecha) | Fecha y hora del último cambio de estrellas (RN-237) |
| private | activa | boolean/Boolean |  | no final | no static | true | Indica si es la calificación activa del usuario para ese contenido |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | Calificacion | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getAutor | getter | Obtiene el valor del atributo autor. | Otro | Usuario |
| public | getKudo | getter | Obtiene el valor del atributo kudo. | Otro | Kudo |
| public | getReplica | getter | Obtiene el valor del atributo replica. | Otro | Replica |
| public | getEstrellas | getter | Obtiene el valor del atributo estrellas. | int |  |
| public | getFechaCalificacion | getter | Obtiene el valor del atributo fechaCalificacion. | Otro | LocalDateTime |
| public | getFechaModificacion | getter | Obtiene el valor del atributo fechaModificacion. | Otro | LocalDateTime |
| public | isActiva | getter | Obtiene el valor del atributo activa. | boolean/Boolean |  |

## Métodos de negocio

Métodos **reales** de `CalificacionDominio` (comportamiento del dominio rico, D-29).

| Modificador de acceso | Nombre | Parámetros | Tipo dato de retorno | Descripción |
|---|---|---|---|---|
| public | modificarEstrellas | int nuevasEstrellas | void | Cambia las estrellas de la calificación activa del usuario. |
| public | esSobreKudo | — | boolean | Indica si el objeto se refiere a un Kudo (si no, a una réplica). |

## Operaciones del caso de uso (capa de negocio)

Responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/Calificacion.md#responsabilidades) que coordinan varios objetos o consultan la base de datos. Las implementa: CalificacionNegocioImpl.

| Operación | Descripción | Políticas que protege |
|---|---|---|
| calificar | Registra la valoración de un usuario sobre un Kudo o una réplica. | P-CAL-001, P-CAL-002, P-CAL-003, P-CAL-004, P-CAL-005, P-CAL-006, P-CAL-007 |
| consultarValoraciones | Muestra el resumen de calificaciones de un contenido. | P-CAL-009 |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | autor | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | autor | Usuario | Builder |
| public | kudo | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Kudo. | kudo | Kudo | Builder |
| public | replica | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Replica. | replica | Replica | Builder |
| public | estrellas | no static | Asigna el valor recibido; si es nulo o negativo, asigna 0. | estrellas | int | Builder |
| public | fechaCalificacion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaCalificacion | LocalDateTime | Builder |
| public | fechaModificacion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaModificacion | LocalDateTime | Builder |
| public | activa | no static | Asigna el valor recibido. | activa | boolean | Builder |
| public | build | no static | Crea la instancia de Calificacion con los valores reunidos, usando su constructor privado. | | | Calificacion |
