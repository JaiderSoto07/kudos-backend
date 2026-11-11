# Moderacion

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: Moderacion](../../01-modelo-dominio/objetos/Moderacion.md) · [Entidad MER: Moderacion](../../03-modelo-entidad-relacion/entidades/Moderacion.md)

| Campo | Valor |
|---|---|
| Clase | Moderacion |
| Clase en el código | `ModeracionDominio` |
| Estereotipo | class (entidad de dominio) |
| Descripción | Revisión y decisión institucional sobre un Kudo o una réplica, tomada por el Rector o el Coordinador Académico. El histórico de vetos son las moderaciones con decisión VETAR. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Necesita de | Asociación | Kudo |
| Necesita de | Asociación | Replica |
| Necesita de | Asociación | Usuario |
| Es usada por | Asociación | Reporte |
| Necesita de | Dependencia (enumeración) | EstadoModeracion |
| Necesita de | Dependencia (enumeración) | DecisionModeracion |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único de la moderación |
| private | kudo | Otro | Kudo | final | no static | Instancia por defecto de Kudo | Kudo moderado |
| private | replica | Otro | Replica | final | no static | Instancia por defecto de Replica | Réplica moderada |
| private | moderador | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Autoridad que revisa y decide |
| private | estado | Otro | EstadoModeracion | no final | no static | EN_CURSO | Estado de la moderación |
| private | fechaInicio | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora en que inicia la revisión (RN-239) |
| private | fechaCierre | Otro | LocalDateTime | no final | no static | Fecha por defecto (UtilFecha) | Fecha y hora de la decisión (RN-240) |
| private | decision | Otro | DecisionModeracion | no final | no static | VETAR | Decisión tomada |
| private | motivoDecision | String |  | no final | no static | "" (cadena vacía) | Justificación de la decisión (RN-250) |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | Moderacion | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getKudo | getter | Obtiene el valor del atributo kudo. | Otro | Kudo |
| public | getReplica | getter | Obtiene el valor del atributo replica. | Otro | Replica |
| public | getModerador | getter | Obtiene el valor del atributo moderador. | Otro | Usuario |
| public | getEstado | getter | Obtiene el valor del atributo estado. | Otro | EstadoModeracion |
| public | getFechaInicio | getter | Obtiene el valor del atributo fechaInicio. | Otro | LocalDateTime |
| public | getFechaCierre | getter | Obtiene el valor del atributo fechaCierre. | Otro | LocalDateTime |
| public | getDecision | getter | Obtiene el valor del atributo decision. | Otro | DecisionModeracion |
| public | getMotivoDecision | getter | Obtiene el valor del atributo motivoDecision. | String |  |

## Métodos de negocio

Métodos **reales** de `ModeracionDominio` (comportamiento del dominio rico, D-29).

| Modificador de acceso | Nombre | Parámetros | Tipo dato de retorno | Descripción |
|---|---|---|---|---|
| public | estaEnCurso | — | boolean | Indica si la moderación sigue EN_CURSO. |
| public | terminoEnVeto | — | boolean | Indica si la decisión fue VETAR. |
| public | vetar | String motivo | void | El moderador veta el contenido y cierra la revisión. |
| public | liberar | String motivo | void | El moderador libera el contenido y cierra la revisión. |
| public | esSobreKudo | — | boolean | Indica si el objeto se refiere a un Kudo (si no, a una réplica). |

## Operaciones del caso de uso (capa de negocio)

Responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/Moderacion.md#responsabilidades) que coordinan varios objetos o consultan la base de datos. Las implementa: ModeracionNegocioImpl.

| Operación | Descripción | Políticas que protege |
|---|---|---|
| iniciar | Un moderador abre la revisión de un contenido reportado. | P-MOD-001, P-MOD-002, P-MOD-003 |
| consultarHistoricoVetos | Lista las moderaciones que terminaron en veto. | P-MOD-009 |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | kudo | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Kudo. | kudo | Kudo | Builder |
| public | replica | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Replica. | replica | Replica | Builder |
| public | moderador | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | moderador | Usuario | Builder |
| public | estado | no static | Asigna el valor recibido; si es nulo, asigna: EN_CURSO. | estado | EstadoModeracion | Builder |
| public | fechaInicio | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaInicio | LocalDateTime | Builder |
| public | fechaCierre | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaCierre | LocalDateTime | Builder |
| public | decision | no static | Asigna el valor recibido; si es nulo, asigna: VETAR. | decision | DecisionModeracion | Builder |
| public | motivoDecision | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | motivoDecision | String | Builder |
| public | build | no static | Crea la instancia de Moderacion con los valores reunidos, usando su constructor privado. | | | Moderacion |
