# Kudo

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: Kudo](../../01-modelo-dominio/objetos/Kudo.md) · [Entidad MER: Kudo](../../03-modelo-entidad-relacion/entidades/Kudo.md)

| Campo | Valor |
|---|---|
| Clase | Kudo |
| Clase en el código | `KudoDominio` |
| Estereotipo | class (entidad de dominio) |
| Descripción | Reconocimiento formal que una persona otorga a otra persona de la comunidad universitaria. Es la raíz de su conversación. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Necesita de | Asociación | Usuario |
| Necesita de | Asociación | Categoria |
| Es usada por | Composición | Replica |
| Es usada por | Asociación | Calificacion |
| Es usada por | Asociación | Reporte |
| Es usada por | Asociación | Moderacion |
| Necesita de | Dependencia (enumeración) | EstadoContenido |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único del Kudo |
| private | autor | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Usuario que otorga el reconocimiento (RN-147) |
| private | destinatario | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Usuario que recibe el reconocimiento (RN-148) |
| private | categoria | Otro | Categoria | final | no static | Instancia por defecto de Categoria | Categoría del reconocimiento (RN-149) |
| private | contenido | String |  | final | no static | "" (cadena vacía) | Texto del reconocimiento |
| private | fechaCreacion | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora de publicación (RN-150) |
| private | estado | Otro | EstadoContenido | no final | no static | CREADO | Estado del ciclo de vida del contenido |
| private | cantidadVisualizaciones | int |  | no final | no static | 0 | Número de veces que ha sido consultado (RN-229, D-25) |
| private | promedioCalificacion | Otro | BigDecimal | no final | no static | 0 | Promedio de estrellas de sus calificaciones activas |
| private | cantidadCalificaciones | int |  | no final | no static | 0 | Número de calificaciones activas |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | Kudo | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getAutor | getter | Obtiene el valor del atributo autor. | Otro | Usuario |
| public | getDestinatario | getter | Obtiene el valor del atributo destinatario. | Otro | Usuario |
| public | getCategoria | getter | Obtiene el valor del atributo categoria. | Otro | Categoria |
| public | getContenido | getter | Obtiene el valor del atributo contenido. | String |  |
| public | getFechaCreacion | getter | Obtiene el valor del atributo fechaCreacion. | Otro | LocalDateTime |
| public | getEstado | getter | Obtiene el valor del atributo estado. | Otro | EstadoContenido |
| public | getCantidadVisualizaciones | getter | Obtiene el valor del atributo cantidadVisualizaciones. | int |  |
| public | getPromedioCalificacion | getter | Obtiene el valor del atributo promedioCalificacion. | Otro | BigDecimal |
| public | getCantidadCalificaciones | getter | Obtiene el valor del atributo cantidadCalificaciones. | int |  |

## Métodos de negocio

Métodos **reales** de `KudoDominio` (comportamiento del dominio rico, D-29).

| Modificador de acceso | Nombre | Parámetros | Tipo dato de retorno | Descripción |
|---|---|---|---|---|
| public | publicar | — | void | Transición del patrón State: CREADO → VISIBLE. |
| public | reportar | — | void | Registra que el Kudo recibió un reporte. |
| public | iniciarRevision | — | void | El Kudo entra en revisión de un moderador. |
| public | liberar | — | void | El Kudo vuelve a estar visible tras la revisión. |
| public | vetar | — | void | Inhabilita el Kudo de forma definitiva. |
| public | admiteInteraccion | — | boolean | Indica si el Kudo acepta réplicas, calificaciones y reportes. |
| public | esAutor | UUID idUsuario | boolean | Indica si el usuario dado es el autor. |
| public | actualizarResumenCalificaciones | List<Integer> estrellasActivas | void | Recalcula el promedio y la cantidad de calificaciones activas. |
| public | registrarVisualizacion | — | void | Muestra el Kudo completo y registra la visualización. |

## Operaciones del caso de uso (capa de negocio)

Responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/Kudo.md#responsabilidades) que coordinan varios objetos o consultan la base de datos. Las implementa: KudoNegocioImpl.

| Operación | Descripción | Políticas que protege |
|---|---|---|
| crear | Crea y publica un reconocimiento de un usuario activo a otro. | P-KUD-001, P-KUD-002, P-KUD-003, P-KUD-004, P-KUD-005, P-KUD-006, P-KUD-007 |
| consultarMuro | Lista los Kudos no vetados, del más reciente al más antiguo. | P-KUD-011 |
| consultarHistorial | Lista los Kudos recibidos y emitidos por una persona. | P-KUD-012 |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | autor | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | autor | Usuario | Builder |
| public | destinatario | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | destinatario | Usuario | Builder |
| public | categoria | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Categoria. | categoria | Categoria | Builder |
| public | contenido | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | contenido | String | Builder |
| public | fechaCreacion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaCreacion | LocalDateTime | Builder |
| public | estado | no static | Asigna el valor recibido; si es nulo, asigna: CREADO. | estado | EstadoContenido | Builder |
| public | cantidadVisualizaciones | no static | Asigna el valor recibido; si es nulo o negativo, asigna 0. | cantidadVisualizaciones | int | Builder |
| public | promedioCalificacion | no static | Asigna el valor recibido; si es nulo o negativo, asigna 0. | promedioCalificacion | BigDecimal | Builder |
| public | cantidadCalificaciones | no static | Asigna el valor recibido; si es nulo o negativo, asigna 0. | cantidadCalificaciones | int | Builder |
| public | build | no static | Crea la instancia de Kudo con los valores reunidos, usando su constructor privado. | | | Kudo |
