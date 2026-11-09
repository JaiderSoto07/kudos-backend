# Notificacion

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Aviso dentro de la aplicación dirigido a un usuario sobre algo que le concierne.

**Línea de vida:** 2

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único de la notificación | UUID | Sí |
| destinatario | Usuario que recibe el aviso | [Usuario](Usuario.md) | Sí |
| tipo | Tipo de aviso | [TipoNotificacion](../README.md#enumeraciones) | Sí |
| mensaje | Texto del aviso | Texto | Sí |
| tipoReferencia | Tipo del objeto relacionado | Texto | No |
| idReferencia | Identificador del objeto relacionado | UUID | No |
| fechaCreacion | Fecha y hora de creación | FechaHora | Sí |
| leida | Indica si el destinatario ya la leyó | Lógico | Sí |
| fechaLectura | Fecha y hora de lectura | FechaHora | No |

### Datos de muestra

_4 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | destinatario | tipo | mensaje | tipoReferencia | idReferencia | fechaCreacion | leida | fechaLectura |
|---|---|---|---|---|---|---|---|---|
| n-01 | u-07 Santiago Henao | KUDO_RECIBIDO | Andrea Giraldo te otorgó un Kudo en Excelencia académica | Kudo | k-01 | 2026-11-05 10:00 | Sí | 2026-11-05 10:30 |
| n-02 | u-07 Santiago Henao | REPLICA_RECIBIDA | Andrea Giraldo respondió tu réplica | Replica | r-03 | 2026-11-05 12:00 | No |  |
| n-03 | u-08 Valentina Cardona | CODIGO_VERIFICACION | Tu código para verificar el celular es ****** | Usuario | u-08 | 2026-11-08 09:00 | No |  |
| n-04 | u-08 Valentina Cardona | CONTENIDO_VETADO | Tu Kudo fue vetado por moderación | Kudo | k-04 | 2026-11-07 08:20 | Sí | 2026-11-07 12:00 |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Avisa a-> | [Usuario](Usuario.md) | Notificacion 0..* — 1 Usuario | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | Un aviso dentro de la aplicación para un usuario sobre algo que le concierne. |
| **Tipo de objeto** | Entidad raíz de agregado |
| **Agregado** | Notificacion (raíz). |
| **¿Qué conoce? (información que administra)** | El destinatario, el tipo, el mensaje, el objeto relacionado, la fecha y si fue leída. |
| **¿Qué hace? (responsabilidades)** | • Crear notificación<br>• Marcar como leída<br>• Consultar notificaciones |
| **¿Qué decisiones toma?** | • Que solo su destinatario puede marcarla como leída. |
| **¿Qué decisiones delega?** | • Se crea desde los eventos de dominio (suscriptor de notificaciones). |
| **¿Con quién colabora?** | Usuario, EventoDominio |
| **¿Qué eventos genera?** | • Notificación creada<br>• Notificación leída<br>• Notificaciones consultadas |
| **¿Qué invariantes protege?** | • Una notificación pertenece a un único destinatario. |

### Caracterización de atributos

| Nombre atributo | id | destinatario | tipo | mensaje | tipoReferencia | idReferencia | fechaCreacion | leida | fechaLectura |
|---|---|---|---|---|---|---|---|---|---|
| Descripción atributo | Identificador único de la notificación | Usuario que recibe el aviso | Tipo de aviso | Texto del aviso | Tipo del objeto relacionado | Identificador del objeto relacionado | Fecha y hora de creación | Indica si el destinatario ya la leyó | Fecha y hora de lectura |
| Identificador del objeto de dominio | Sí | No | No | No | No | No | No | No | No |
| Tipo de dato | UUID | Usuario | TipoNotificacion | Texto | Texto | UUID | FechaHora | Lógico | FechaHora |
| Obligatorio | Sí | Sí | Sí | Sí | No | No | Sí | Sí | No |
| Longitud mínima (si aplica) |  |  |  | 1 |  |  |  |  |  |
| Longitud máxima (si aplica) |  |  |  | 500 |  |  |  |  |  |
| Precisión (si aplica) |  |  |  |  |  |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  |  |  |  |  |  |
| Rango final (si aplica) |  |  |  |  |  |  |  |  |  |
| Formato (si aplica) |  |  |  |  |  |  |  |  |  |
| Reglas especiales (si aplica) | Autogenerado |  |  | 1 a 500 caracteres | Ej. Kudo, Replica, SolicitudRegistro | Permite navegar al objeto | Asignada por el sistema |  | Obligatoria cuando leida = Sí |
| Dato calculado | No | No | No | No | No | No | No | No | No |
| Quitar espacios en blanco al inicio y al fin | No | No | No | Sí | Sí | No | No | No | No |
| Dato sensible | No | No | No | No | No | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  |  |  |  | Fecha actual | No |  |
| Auto generado | Sí | No | No | No | No | No | Sí | No | No |
| Crear notificación | No requerido | Requerido | Requerido | Requerido | Requerido | Requerido | No requerido | No requerido | No requerido |
| Marcar como leída | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Consultar notificaciones | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |

### Combinaciones únicas

No tiene combinaciones únicas además de su identificador.

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Crear notificación** (`crear()`) | Crea un aviso para el usuario afectado por un evento. | destinatario | Usuario | A quién se avisa. | Notificacion | Notificación no leída. | P-NOT-001 | Toda notificación tiene un destinatario existente y un mensaje de 1 a 500 caracteres, y referencia al objeto relacionado. |  |  |
|  |  | tipo | TipoNotificacion | Tipo de aviso. |  |  |  |  |  |  |
|  |  | mensaje | String | Texto. |  |  |  |  |  |  |
|  |  | referencia | UUID | Objeto relacionado. |  |  |  |  |  |  |
| **Marcar como leída** (`marcarLeida()`) | Registra que el destinatario leyó la notificación. | actor | Usuario | Usuario que la lee. | Notificacion | Notificación leída. | P-NOT-002 | Solo el destinatario marca su notificación como leída. | La notificación no es del usuario. | Rechazar la operación. |
| **Consultar notificaciones** (`consultar()`) | Lista las notificaciones del usuario. | usuario | Usuario | Usuario que consulta. | Lista de Notificacion | Notificaciones. | P-NOT-003 | Cada usuario consulta solo sus notificaciones, las no leídas primero. |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Sistema (suscriptor de notificaciones) | Crear notificación | Crea un aviso para el usuario afectado por un evento. | Notificacion |  |  | P-NOT-001 |  | Notificación creada |  |
| Usuario activo | Marcar como leída | Registra que el destinatario leyó la notificación. | Notificacion |  |  | P-NOT-002 |  | Notificación leída |  |
| Usuario activo | Consultar notificaciones | Lista las notificaciones del usuario. | Notificacion |  |  | P-NOT-003 |  | Notificaciones consultadas |  |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-NOT-001 | Toda notificación tiene un destinatario existente y un mensaje de 1 a 500 caracteres, y referencia al objeto relacionado. | Notificacion | D-27 |
| P-NOT-002 | Solo el destinatario marca su notificación como leída. | Notificacion | D-27 |
| P-NOT-003 | Cada usuario consulta solo sus notificaciones, las no leídas primero. | Notificacion | D-27 |
