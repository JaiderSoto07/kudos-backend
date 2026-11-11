# Notificacion

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: Notificacion](../../01-modelo-dominio/objetos/Notificacion.md) · [Entidad MER: Notificacion](../../03-modelo-entidad-relacion/entidades/Notificacion.md)

| Campo | Valor |
|---|---|
| Clase | Notificacion |
| Clase en el código | `NotificacionDominio` |
| Estereotipo | class (entidad de dominio) |
| Descripción | Aviso dentro de la aplicación dirigido a un usuario sobre algo que le concierne. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Necesita de | Asociación | Usuario |
| Necesita de | Dependencia (enumeración) | TipoNotificacion |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único de la notificación |
| private | destinatario | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Usuario que recibe el aviso |
| private | tipo | Otro | TipoNotificacion | final | no static | KUDO_RECIBIDO | Tipo de aviso |
| private | mensaje | String |  | final | no static | "" (cadena vacía) | Texto del aviso |
| private | tipoReferencia | String |  | final | no static | "" (cadena vacía) | Tipo del objeto relacionado |
| private | idReferencia | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador del objeto relacionado |
| private | fechaCreacion | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora de creación |
| private | leida | boolean/Boolean |  | no final | no static | false | Indica si el destinatario ya la leyó |
| private | fechaLectura | Otro | LocalDateTime | no final | no static | Fecha por defecto (UtilFecha) | Fecha y hora de lectura |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | Notificacion | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getDestinatario | getter | Obtiene el valor del atributo destinatario. | Otro | Usuario |
| public | getTipo | getter | Obtiene el valor del atributo tipo. | Otro | TipoNotificacion |
| public | getMensaje | getter | Obtiene el valor del atributo mensaje. | String |  |
| public | getTipoReferencia | getter | Obtiene el valor del atributo tipoReferencia. | String |  |
| public | getIdReferencia | getter | Obtiene el valor del atributo idReferencia. | Otro | UUID |
| public | getFechaCreacion | getter | Obtiene el valor del atributo fechaCreacion. | Otro | LocalDateTime |
| public | isLeida | getter | Obtiene el valor del atributo leida. | boolean/Boolean |  |
| public | getFechaLectura | getter | Obtiene el valor del atributo fechaLectura. | Otro | LocalDateTime |

## Métodos de negocio

Métodos **reales** de `NotificacionDominio` (comportamiento del dominio rico, D-29).

| Modificador de acceso | Nombre | Parámetros | Tipo dato de retorno | Descripción |
|---|---|---|---|---|
| public | marcarLeida | — | void | Registra que el destinatario leyó la notificación. |

## Operaciones del caso de uso (capa de negocio)

Responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/Notificacion.md#responsabilidades) que coordinan varios objetos o consultan la base de datos. Las implementa: NotificacionNegocioImpl (lo llaman todos los negocios).

| Operación | Descripción | Políticas que protege |
|---|---|---|
| crear | Crea un aviso para el usuario afectado por un evento. | P-NOT-001 |
| consultar | Lista las notificaciones del usuario. | P-NOT-003 |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | destinatario | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | destinatario | Usuario | Builder |
| public | tipo | no static | Asigna el valor recibido; si es nulo, asigna: KUDO_RECIBIDO. | tipo | TipoNotificacion | Builder |
| public | mensaje | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | mensaje | String | Builder |
| public | tipoReferencia | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | tipoReferencia | String | Builder |
| public | idReferencia | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | idReferencia | UUID | Builder |
| public | fechaCreacion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaCreacion | LocalDateTime | Builder |
| public | leida | no static | Asigna el valor recibido. | leida | boolean | Builder |
| public | fechaLectura | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaLectura | LocalDateTime | Builder |
| public | build | no static | Crea la instancia de Notificacion con los valores reunidos, usando su constructor privado. | | | Notificacion |
