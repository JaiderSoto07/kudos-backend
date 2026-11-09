# Calificacion

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: Calificacion](../../01-modelo-dominio/objetos/Calificacion.md) · [Entidad MER: Calificacion](../../03-modelo-entidad-relacion/entidades/Calificacion.md)

| Campo | Valor |
|---|---|
| Clase | Calificacion |
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
| Necesita de | Composición (objeto de valor) | Estrellas |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único de la calificación |
| private | autor | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Usuario que califica (RN-183) |
| private | kudo | Otro | Kudo | final | no static | Instancia por defecto de Kudo | Kudo calificado |
| private | replica | Otro | Replica | final | no static | Instancia por defecto de Replica | Réplica calificada |
| private | estrellas | Otro | Estrellas | no final | no static | Instancia vacía de Estrellas | Valoración asignada (RN-177) |
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
| public | getEstrellas | getter | Obtiene el valor del atributo estrellas. | Otro | Estrellas |
| public | getFechaCalificacion | getter | Obtiene el valor del atributo fechaCalificacion. | Otro | LocalDateTime |
| public | getFechaModificacion | getter | Obtiene el valor del atributo fechaModificacion. | Otro | LocalDateTime |
| public | isActiva | getter | Obtiene el valor del atributo activa. | boolean/Boolean |  |

## Métodos

Métodos de negocio: salen de las responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/Calificacion.md#responsabilidades). Las consultas se marcan `static`, como en Sigma: en la implementación las resuelve la capa de negocio con su DAO.

| Modificador de acceso | Nombre | Tipo acceso método | Tipo modificación método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · Descripción | Tipo dato de retorno | Excepciones (políticas que puede incumplir) |
|---|---|---|---|---|---|---|---|---|---|
| public | calificar | no static | final | Registra la valoración de un usuario sobre un Kudo o una réplica. | autor | Usuario | Quien califica. | Calificacion | P-CAL-001: El usuario no está activo.<br>P-CAL-002: No indica contenido o indica ambos.<br>P-CAL-003: El contenido está vetado.<br>P-CAL-004: El usuario es el autor del contenido.<br>P-CAL-005: El valor está fuera de la escala. |
|  |  |  |  |  | contenido | Kudo o Replica | Contenido calificado. |  |  |
|  |  |  |  |  | estrellas | Estrellas | De 1 a 5. |  |  |
| public | modificarEstrellas | no static | final | Cambia las estrellas de la calificación activa del usuario. | estrellas | Estrellas | Nuevo valor. | Calificacion | P-CAL-005: El valor está fuera de la escala.<br>P-CAL-008: La calificación no es del usuario o no está activa. |
| public | consultarValoraciones | static | final | Muestra el resumen de calificaciones de un contenido. | contenido | UUID | Kudo o réplica. | ResumenValoraciones |  |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | autor | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | autor | Usuario | Builder |
| public | kudo | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Kudo. | kudo | Kudo | Builder |
| public | replica | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Replica. | replica | Replica | Builder |
| public | estrellas | no static | Asigna el valor recibido; si es nulo, asigna: Instancia vacía de Estrellas. | estrellas | Estrellas | Builder |
| public | fechaCalificacion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaCalificacion | LocalDateTime | Builder |
| public | fechaModificacion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaModificacion | LocalDateTime | Builder |
| public | activa | no static | Asigna el valor recibido. | activa | boolean | Builder |
| public | build | no static | Crea la instancia de Calificacion con los valores reunidos, usando su constructor privado. | | | Calificacion |
