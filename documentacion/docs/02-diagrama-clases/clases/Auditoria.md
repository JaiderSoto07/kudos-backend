# Auditoria

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: Auditoria](../../01-modelo-dominio/objetos/Auditoria.md) · [Entidad MER: Auditoria](../../03-modelo-entidad-relacion/entidades/Auditoria.md)

| Campo | Valor |
|---|---|
| Clase | Auditoria |
| Clase en el código | `AuditoriaDominio` |
| Estereotipo | class (entidad de dominio) |
| Descripción | Registro histórico e inmutable de una operación relevante realizada en la plataforma. Permite reconstruir quién hizo qué, cuándo, sobre qué objeto y por qué. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Necesita de | Asociación | Usuario |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único del registro |
| private | fechaHora | Otro | LocalDateTime | final | no static | Fecha actual | Momento en que ocurrió la operación |
| private | actor | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Usuario que realizó la operación |
| private | accion | String |  | final | no static | "" (cadena vacía) | Evento de dominio registrado |
| private | tipoObjeto | String |  | final | no static | "" (cadena vacía) | Tipo del objeto afectado |
| private | idObjeto | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador del objeto afectado |
| private | estadoAnterior | String |  | final | no static | "" (cadena vacía) | Estado o valor antes de la operación |
| private | estadoNuevo | String |  | final | no static | "" (cadena vacía) | Estado o valor después de la operación |
| private | motivo | String |  | final | no static | "" (cadena vacía) | Motivo declarado por el actor |
| private | detalle | String |  | final | no static | "" (cadena vacía) | Información adicional del evento |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | Auditoria | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getFechaHora | getter | Obtiene el valor del atributo fechaHora. | Otro | LocalDateTime |
| public | getActor | getter | Obtiene el valor del atributo actor. | Otro | Usuario |
| public | getAccion | getter | Obtiene el valor del atributo accion. | String |  |
| public | getTipoObjeto | getter | Obtiene el valor del atributo tipoObjeto. | String |  |
| public | getIdObjeto | getter | Obtiene el valor del atributo idObjeto. | Otro | UUID |
| public | getEstadoAnterior | getter | Obtiene el valor del atributo estadoAnterior. | String |  |
| public | getEstadoNuevo | getter | Obtiene el valor del atributo estadoNuevo. | String |  |
| public | getMotivo | getter | Obtiene el valor del atributo motivo. | String |  |
| public | getDetalle | getter | Obtiene el valor del atributo detalle. | String |  |

## Métodos de negocio

Métodos **reales** de `AuditoriaDominio` (comportamiento del dominio rico, D-29).

| Modificador de acceso | Nombre | Parámetros | Tipo dato de retorno | Descripción |
|---|---|---|---|---|
| — | — | — | — | Solo tiene Builder y getters: es inmutable una vez registrado. |

## Operaciones del caso de uso (capa de negocio)

Responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/Auditoria.md#responsabilidades) que coordinan varios objetos o consultan la base de datos. Las implementa: AuditoriaNegocioImpl (lo llaman todos los negocios).

| Operación | Descripción | Políticas que protege |
|---|---|---|
| registrar | Crea el registro histórico de un evento de dominio (lo llama cada negocio). | P-AUD-001, P-AUD-002, P-AUD-003 |
| consultar | Lista los registros de auditoría del ámbito del actor. | P-AUD-004, P-AUD-005 |
| consultarActividadPropia | Lista la actividad del propio usuario. | P-AUD-006 |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | fechaHora | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaHora | LocalDateTime | Builder |
| public | actor | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | actor | Usuario | Builder |
| public | accion | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | accion | String | Builder |
| public | tipoObjeto | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | tipoObjeto | String | Builder |
| public | idObjeto | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | idObjeto | UUID | Builder |
| public | estadoAnterior | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | estadoAnterior | String | Builder |
| public | estadoNuevo | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | estadoNuevo | String | Builder |
| public | motivo | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | motivo | String | Builder |
| public | detalle | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | detalle | String | Builder |
| public | build | no static | Crea la instancia de Auditoria con los valores reunidos, usando su constructor privado. | | | Auditoria |
