# CodigoVerificacion

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: CodigoVerificacion](../../01-modelo-dominio/objetos/CodigoVerificacion.md) · [Entidad MER: CodigoVerificacion](../../03-modelo-entidad-relacion/entidades/CodigoVerificacion.md)

| Campo | Valor |
|---|---|
| Clase | CodigoVerificacion |
| Clase en el código | `CodigoVerificacionDominio` |
| Estereotipo | class (entidad de dominio) |
| Descripción | Código temporal que se envía al correo o al celular de un usuario para validar que le pertenece. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Necesita de | Composición | Usuario |
| Necesita de | Dependencia (enumeración) | CanalVerificacion |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único del código |
| private | usuario | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Usuario que debe verificar el dato |
| private | canal | Otro | CanalVerificacion | final | no static | CORREO | Dato que se verifica |
| private | destino | String |  | final | no static | "" (cadena vacía) | Valor exacto al que se envió el código |
| private | codigo | String |  | final | no static | "" (cadena vacía) | Código de 6 dígitos generado con SecureRandom |
| private | fechaGeneracion | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora de generación |
| private | fechaExpiracion | Otro | LocalDateTime | final | no static | Fecha por defecto (UtilFecha) | Fecha y hora en que vence |
| private | intentosFallidos | int |  | no final | no static | 0 | Número de intentos con código incorrecto |
| private | usado | boolean/Boolean |  | no final | no static | false | Indica si el código ya se usó con éxito |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | CodigoVerificacion | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getUsuario | getter | Obtiene el valor del atributo usuario. | Otro | Usuario |
| public | getCanal | getter | Obtiene el valor del atributo canal. | Otro | CanalVerificacion |
| public | getDestino | getter | Obtiene el valor del atributo destino. | String |  |
| public | getCodigo | getter | Obtiene el valor del atributo codigo. | String |  |
| public | getFechaGeneracion | getter | Obtiene el valor del atributo fechaGeneracion. | Otro | LocalDateTime |
| public | getFechaExpiracion | getter | Obtiene el valor del atributo fechaExpiracion. | Otro | LocalDateTime |
| public | getIntentosFallidos | getter | Obtiene el valor del atributo intentosFallidos. | int |  |
| public | isUsado | getter | Obtiene el valor del atributo usado. | boolean/Boolean |  |

## Métodos de negocio

Métodos **reales** de `CodigoVerificacionDominio` (comportamiento del dominio rico, D-29).

| Modificador de acceso | Nombre | Parámetros | Tipo dato de retorno | Descripción |
|---|---|---|---|---|
| public | estaVencido | LocalDateTime ahora | boolean | Indica si el código pasó su fecha de expiración. |
| public | intentosAgotados | int maximoIntentos | boolean | Indica si se alcanzó el máximo de intentos fallidos. |
| public | registrarIntentoFallido | — | void | Suma un intento fallido. |
| public | marcarUsado | — | void | Marca el código como usado. |

## Operaciones del caso de uso (capa de negocio)

Responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/CodigoVerificacion.md#responsabilidades) que coordinan varios objetos o consultan la base de datos. Las implementa: VerificacionNegocioImpl.

| Operación | Descripción | Políticas que protege |
|---|---|---|
| generar | Crea un código temporal para verificar el correo o el celular y lo envía. | P-CVE-001, P-CVE-002, P-CVE-003, P-CVE-004 |
| verificar | Compara el código digitado con el código vigente. | P-CVE-005, P-CVE-006, P-CVE-007 |
| invalidar | Deja el código sin posibilidad de uso. | P-CVE-008 |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | usuario | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | usuario | Usuario | Builder |
| public | canal | no static | Asigna el valor recibido; si es nulo, asigna: CORREO. | canal | CanalVerificacion | Builder |
| public | destino | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | destino | String | Builder |
| public | codigo | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | codigo | String | Builder |
| public | fechaGeneracion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaGeneracion | LocalDateTime | Builder |
| public | fechaExpiracion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaExpiracion | LocalDateTime | Builder |
| public | intentosFallidos | no static | Asigna el valor recibido; si es nulo o negativo, asigna 0. | intentosFallidos | int | Builder |
| public | usado | no static | Asigna el valor recibido. | usado | boolean | Builder |
| public | build | no static | Crea la instancia de CodigoVerificacion con los valores reunidos, usando su constructor privado. | | | CodigoVerificacion |
