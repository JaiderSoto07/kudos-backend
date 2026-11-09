# Kudo

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: Kudo](../../01-modelo-dominio/objetos/Kudo.md) · [Entidad MER: Kudo](../../03-modelo-entidad-relacion/entidades/Kudo.md)

| Campo | Valor |
|---|---|
| Clase | Kudo |
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
| Necesita de | Composición (objeto de valor) | ContenidoTexto |
| Necesita de | Dependencia (enumeración) | EstadoContenido |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único del Kudo |
| private | autor | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Usuario que otorga el reconocimiento (RN-147) |
| private | destinatario | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Usuario que recibe el reconocimiento (RN-148) |
| private | categoria | Otro | Categoria | final | no static | Instancia por defecto de Categoria | Categoría del reconocimiento (RN-149) |
| private | contenido | Otro | ContenidoTexto | final | no static | Instancia vacía de ContenidoTexto | Texto del reconocimiento |
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
| public | getContenido | getter | Obtiene el valor del atributo contenido. | Otro | ContenidoTexto |
| public | getFechaCreacion | getter | Obtiene el valor del atributo fechaCreacion. | Otro | LocalDateTime |
| public | getEstado | getter | Obtiene el valor del atributo estado. | Otro | EstadoContenido |
| public | getCantidadVisualizaciones | getter | Obtiene el valor del atributo cantidadVisualizaciones. | int |  |
| public | getPromedioCalificacion | getter | Obtiene el valor del atributo promedioCalificacion. | Otro | BigDecimal |
| public | getCantidadCalificaciones | getter | Obtiene el valor del atributo cantidadCalificaciones. | int |  |

## Métodos

Métodos de negocio: salen de las responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/Kudo.md#responsabilidades). Las consultas se marcan `static`, como en Sigma: en la implementación las resuelve la capa de negocio con su DAO.

| Modificador de acceso | Nombre | Tipo acceso método | Tipo modificación método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · Descripción | Tipo dato de retorno | Excepciones (políticas que puede incumplir) |
|---|---|---|---|---|---|---|---|---|---|
| public | crear | no static | final | Crea y publica un reconocimiento de un usuario activo a otro. | autor | Usuario | Quien reconoce. | Kudo | P-KUD-001: El autor no está activo.<br>P-KUD-002: El destinatario no está activo o es el mismo autor.<br>P-KUD-003: La categoría no existe o está inactiva.<br>P-KUD-004: El contenido está vacío. |
|  |  |  |  |  | destinatario | Usuario | Quien es reconocido. |  |  |
|  |  |  |  |  | categoria | Categoria | Categoría activa. |  |  |
|  |  |  |  |  | contenido | ContenidoTexto | Texto del reconocimiento. |  |  |
| public | registrarVisualizacion | static | final | Muestra el Kudo completo y registra la visualización. | id | UUID | Kudo consultado. | Kudo |  |
|  |  |  |  |  | actor | Usuario | Usuario que consulta. |  |  |
| public | consultarMuro | static | final | Lista los Kudos no vetados, del más reciente al más antiguo. | pagina | int | Página. | Página de Kudo | P-KUD-011: No hay Kudos para mostrar. |
|  |  |  |  |  | filtro | FiltroKudo | Categoría o persona (opcional). |  |  |
| public | consultarHistorial | static | final | Lista los Kudos recibidos y emitidos por una persona. | persona | UUID | Persona consultada. | Lista de Kudo |  |
| public | marcarReportado | no static | final | Registra que el Kudo recibió un reporte. |  |  |  | Kudo | P-KUD-013: El Kudo está vetado. |
| public | iniciarRevision | no static | final | El Kudo entra en revisión de un moderador. |  |  |  | Kudo | P-KUD-014: El Kudo no está reportado. |
| public | liberar | no static | final | El Kudo vuelve a estar visible tras la revisión. |  |  |  | Kudo | P-KUD-015: El Kudo no está en revisión. |
| public | vetar | no static | final | Inhabilita el Kudo de forma definitiva. |  |  |  | Kudo | P-KUD-016: El Kudo ya está vetado. |
| public | admiteInteraccion | no static | final | Indica si el Kudo acepta réplicas, calificaciones y reportes. |  |  |  | boolean | P-KUD-018: El Kudo está vetado. |
| public | actualizarResumenCalificaciones | no static | final | Recalcula el promedio y la cantidad de calificaciones activas. | calificaciones | Lista de Estrellas | Calificaciones activas. | Kudo |  |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | autor | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | autor | Usuario | Builder |
| public | destinatario | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | destinatario | Usuario | Builder |
| public | categoria | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Categoria. | categoria | Categoria | Builder |
| public | contenido | no static | Asigna el valor recibido; si es nulo, asigna: Instancia vacía de ContenidoTexto. | contenido | ContenidoTexto | Builder |
| public | fechaCreacion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaCreacion | LocalDateTime | Builder |
| public | estado | no static | Asigna el valor recibido; si es nulo, asigna: CREADO. | estado | EstadoContenido | Builder |
| public | cantidadVisualizaciones | no static | Asigna el valor recibido; si es nulo o negativo, asigna 0. | cantidadVisualizaciones | int | Builder |
| public | promedioCalificacion | no static | Asigna el valor recibido; si es nulo o negativo, asigna 0. | promedioCalificacion | BigDecimal | Builder |
| public | cantidadCalificaciones | no static | Asigna el valor recibido; si es nulo o negativo, asigna 0. | cantidadCalificaciones | int | Builder |
| public | build | no static | Crea la instancia de Kudo con los valores reunidos, usando su constructor privado. | | | Kudo |
