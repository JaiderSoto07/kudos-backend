# Reporte

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: Reporte](../../01-modelo-dominio/objetos/Reporte.md) · [Entidad MER: Reporte](../../03-modelo-entidad-relacion/entidades/Reporte.md)

| Campo | Valor |
|---|---|
| Clase | Reporte |
| Clase en el código | `ReporteDominio` |
| Estereotipo | class (entidad de dominio) |
| Descripción | Solicitud de revisión que un usuario hace sobre un Kudo o una réplica que considera inapropiado. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Necesita de | Asociación | Kudo |
| Necesita de | Asociación | Replica |
| Necesita de | Asociación | Usuario |
| Necesita de | Asociación | Moderacion |
| Necesita de | Dependencia (enumeración) | MotivoReporte |
| Necesita de | Dependencia (enumeración) | EstadoReporte |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único del reporte |
| private | reportante | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Usuario que reporta (RN-188) |
| private | kudo | Otro | Kudo | final | no static | Instancia por defecto de Kudo | Kudo reportado |
| private | replica | Otro | Replica | final | no static | Instancia por defecto de Replica | Réplica reportada |
| private | motivo | Otro | MotivoReporte | final | no static | LENGUAJE_OFENSIVO | Motivo del reporte (RN-190) |
| private | descripcion | String |  | final | no static | "" (cadena vacía) | Explicación del reportante |
| private | fechaReporte | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora del reporte (RN-189) |
| private | estado | Otro | EstadoReporte | no final | no static | PENDIENTE | Estado del reporte |
| private | moderacion | Otro | Moderacion | no final | no static | Instancia por defecto de Moderacion | Moderación que resolvió el reporte |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | Reporte | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getReportante | getter | Obtiene el valor del atributo reportante. | Otro | Usuario |
| public | getKudo | getter | Obtiene el valor del atributo kudo. | Otro | Kudo |
| public | getReplica | getter | Obtiene el valor del atributo replica. | Otro | Replica |
| public | getMotivo | getter | Obtiene el valor del atributo motivo. | Otro | MotivoReporte |
| public | getDescripcion | getter | Obtiene el valor del atributo descripcion. | String |  |
| public | getFechaReporte | getter | Obtiene el valor del atributo fechaReporte. | Otro | LocalDateTime |
| public | getEstado | getter | Obtiene el valor del atributo estado. | Otro | EstadoReporte |
| public | getModeracion | getter | Obtiene el valor del atributo moderacion. | Otro | Moderacion |

## Métodos de negocio

Métodos **reales** de `ReporteDominio` (comportamiento del dominio rico, D-29).

| Modificador de acceso | Nombre | Parámetros | Tipo dato de retorno | Descripción |
|---|---|---|---|---|
| public | estaAbierto | — | boolean | Indica si el reporte está PENDIENTE o EN_REVISION. |
| public | pasarARevision | UUID idModeracionEnCurso | void | El reporte entra en la moderación en curso. |
| public | aceptar | UUID idModeracionQueResuelve | void | El reporte queda aceptado porque el contenido fue vetado. |
| public | rechazar | UUID idModeracionQueResuelve | void | El reporte queda rechazado porque el contenido fue liberado. |
| public | esSobreKudo | — | boolean | Indica si el objeto se refiere a un Kudo (si no, a una réplica). |

## Operaciones del caso de uso (capa de negocio)

Responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/Reporte.md#responsabilidades) que coordinan varios objetos o consultan la base de datos. Las implementa: ReporteNegocioImpl.

| Operación | Descripción | Políticas que protege |
|---|---|---|
| crear | Registra el reporte de un usuario sobre un Kudo o una réplica. | P-RPT-001, P-RPT-002, P-RPT-003, P-RPT-004, P-RPT-005, P-RPT-006 |
| consultar | Lista los reportes según el permiso del actor. | P-RPT-010 |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | reportante | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | reportante | Usuario | Builder |
| public | kudo | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Kudo. | kudo | Kudo | Builder |
| public | replica | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Replica. | replica | Replica | Builder |
| public | motivo | no static | Asigna el valor recibido; si es nulo, asigna: LENGUAJE_OFENSIVO. | motivo | MotivoReporte | Builder |
| public | descripcion | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | descripcion | String | Builder |
| public | fechaReporte | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaReporte | LocalDateTime | Builder |
| public | estado | no static | Asigna el valor recibido; si es nulo, asigna: PENDIENTE. | estado | EstadoReporte | Builder |
| public | moderacion | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Moderacion. | moderacion | Moderacion | Builder |
| public | build | no static | Crea la instancia de Reporte con los valores reunidos, usando su constructor privado. | | | Reporte |
