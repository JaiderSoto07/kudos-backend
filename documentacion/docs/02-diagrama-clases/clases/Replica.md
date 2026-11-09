# Replica

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: Replica](../../01-modelo-dominio/objetos/Replica.md) · [Entidad MER: Replica](../../03-modelo-entidad-relacion/entidades/Replica.md)

| Campo | Valor |
|---|---|
| Clase | Replica |
| Estereotipo | class (entidad de dominio) |
| Descripción | Intervención en la conversación de un Kudo. Su padre es el Kudo (réplica de primer nivel) u otra réplica (respuesta). |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Necesita de | Composición | Kudo |
| Necesita de / Es usada por | Asociación reflexiva | Replica |
| Necesita de | Asociación | Usuario |
| Es usada por | Asociación | Calificacion |
| Es usada por | Asociación | Reporte |
| Es usada por | Asociación | Moderacion |
| Necesita de | Composición (objeto de valor) | ContenidoTexto |
| Necesita de | Dependencia (enumeración) | EstadoContenido |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único de la réplica |
| private | autor | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Usuario que escribe la réplica (RN-168) |
| private | kudo | Otro | Kudo | final | no static | Instancia por defecto de Kudo | Kudo raíz de la conversación |
| private | replicaPadre | Otro | Replica | final | no static | Instancia por defecto de Replica | Réplica a la que responde (RN-162) |
| private | contenido | Otro | ContenidoTexto | final | no static | Instancia vacía de ContenidoTexto | Texto de la réplica |
| private | fechaCreacion | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora de publicación (RN-169) |
| private | estado | Otro | EstadoContenido | no final | no static | CREADO | Estado del ciclo de vida del contenido |
| private | promedioCalificacion | Otro | BigDecimal | no final | no static | 0 | Promedio de estrellas de sus calificaciones activas |
| private | cantidadCalificaciones | int |  | no final | no static | 0 | Número de calificaciones activas |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | Replica | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getAutor | getter | Obtiene el valor del atributo autor. | Otro | Usuario |
| public | getKudo | getter | Obtiene el valor del atributo kudo. | Otro | Kudo |
| public | getReplicaPadre | getter | Obtiene el valor del atributo replicaPadre. | Otro | Replica |
| public | getContenido | getter | Obtiene el valor del atributo contenido. | Otro | ContenidoTexto |
| public | getFechaCreacion | getter | Obtiene el valor del atributo fechaCreacion. | Otro | LocalDateTime |
| public | getEstado | getter | Obtiene el valor del atributo estado. | Otro | EstadoContenido |
| public | getPromedioCalificacion | getter | Obtiene el valor del atributo promedioCalificacion. | Otro | BigDecimal |
| public | getCantidadCalificaciones | getter | Obtiene el valor del atributo cantidadCalificaciones. | int |  |

## Métodos

Métodos de negocio: salen de las responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/Replica.md#responsabilidades). Las consultas se marcan `static`, como en Sigma: en la implementación las resuelve la capa de negocio con su DAO.

| Modificador de acceso | Nombre | Tipo acceso método | Tipo modificación método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · Descripción | Tipo dato de retorno | Excepciones (políticas que puede incumplir) |
|---|---|---|---|---|---|---|---|---|---|
| public | responderKudo | no static | final | Agrega una réplica de primer nivel a la conversación de un Kudo. | autor | Usuario | Quien responde. | Replica | P-RPL-001: El autor no está activo.<br>P-RPL-002: La conversación está cerrada.<br>P-RPL-003: El contenido está vacío. |
|  |  |  |  |  | kudo | Kudo | Kudo raíz. |  |  |
|  |  |  |  |  | contenido | ContenidoTexto | Texto. |  |  |
| public | responderReplica | no static | final | Agrega una respuesta a una réplica existente. | autor | Usuario | Quien responde. | Replica | P-RPL-001: El autor no está activo.<br>P-RPL-002: La conversación está cerrada.<br>P-RPL-003: El contenido está vacío.<br>P-RPL-006: La réplica padre no existe, está vetada o es de otro Kudo. |
|  |  |  |  |  | padre | Replica | Réplica respondida. |  |  |
|  |  |  |  |  | contenido | ContenidoTexto | Texto. |  |  |
| public | consultarConversacion | static | final | Arma el árbol completo de réplicas de un Kudo. | kudo | UUID | Kudo raíz. | Conversacion |  |
| public | marcarReportada | no static | final | Registra que la réplica recibió un reporte. |  |  |  | Replica | P-RPL-009: La réplica está vetada. |
| public | iniciarRevision | no static | final | La réplica entra en revisión. |  |  |  | Replica | P-RPL-010: La réplica no está reportada. |
| public | liberar | no static | final | La réplica vuelve a estar visible. |  |  |  | Replica | P-RPL-011: La réplica no está en revisión. |
| public | vetar | no static | final | Inhabilita la réplica de forma definitiva. |  |  |  | Replica | P-RPL-012: La réplica ya está vetada. |
| public | admiteInteraccion | no static | final | Indica si la réplica acepta respuestas, calificaciones y reportes. |  |  |  | boolean | P-RPL-014: La réplica está vetada. |
| public | actualizarResumenCalificaciones | no static | final | Recalcula el promedio y la cantidad de calificaciones activas. | calificaciones | Lista de Estrellas | Calificaciones activas. | Replica |  |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | autor | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | autor | Usuario | Builder |
| public | kudo | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Kudo. | kudo | Kudo | Builder |
| public | replicaPadre | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Replica. | replicaPadre | Replica | Builder |
| public | contenido | no static | Asigna el valor recibido; si es nulo, asigna: Instancia vacía de ContenidoTexto. | contenido | ContenidoTexto | Builder |
| public | fechaCreacion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaCreacion | LocalDateTime | Builder |
| public | estado | no static | Asigna el valor recibido; si es nulo, asigna: CREADO. | estado | EstadoContenido | Builder |
| public | promedioCalificacion | no static | Asigna el valor recibido; si es nulo o negativo, asigna 0. | promedioCalificacion | BigDecimal | Builder |
| public | cantidadCalificaciones | no static | Asigna el valor recibido; si es nulo o negativo, asigna 0. | cantidadCalificaciones | int | Builder |
| public | build | no static | Crea la instancia de Replica con los valores reunidos, usando su constructor privado. | | | Replica |
