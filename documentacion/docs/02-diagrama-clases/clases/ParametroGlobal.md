# ParametroGlobal

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: ParametroGlobal](../../01-modelo-dominio/objetos/ParametroGlobal.md) · [Entidad MER: ParametroGlobal](../../03-modelo-entidad-relacion/entidades/ParametroGlobal.md)

| Campo | Valor |
|---|---|
| Clase | ParametroGlobal |
| Estereotipo | class (entidad de dominio) |
| Descripción | Valor de configuración de la plataforma que administran los Administradores (RN-130, D-11). |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Necesita de | Dependencia (enumeración) | TipoParametro |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único del parámetro |
| private | clave | String |  | final | no static | "" (cadena vacía) | Nombre técnico del parámetro |
| private | valor | String |  | no final | no static | "" (cadena vacía) | Valor vigente del parámetro |
| private | tipoDato | Otro | TipoParametro | final | no static | TEXTO | Tipo de dato del valor |
| private | descripcion | String |  | final | no static | "" (cadena vacía) | Para qué sirve el parámetro |
| private | fechaModificacion | Otro | LocalDateTime | no final | no static | Fecha actual | Fecha y hora del último cambio |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | ParametroGlobal | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getClave | getter | Obtiene el valor del atributo clave. | String |  |
| public | getValor | getter | Obtiene el valor del atributo valor. | String |  |
| public | getTipoDato | getter | Obtiene el valor del atributo tipoDato. | Otro | TipoParametro |
| public | getDescripcion | getter | Obtiene el valor del atributo descripcion. | String |  |
| public | getFechaModificacion | getter | Obtiene el valor del atributo fechaModificacion. | Otro | LocalDateTime |

## Métodos

Métodos de negocio: salen de las responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/ParametroGlobal.md#responsabilidades). Las consultas se marcan `static`, como en Sigma: en la implementación las resuelve la capa de negocio con su DAO.

| Modificador de acceso | Nombre | Tipo acceso método | Tipo modificación método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · Descripción | Tipo dato de retorno | Excepciones (políticas que puede incumplir) |
|---|---|---|---|---|---|---|---|---|---|
| public | cambiarValor | no static | final | Modifica el valor de un parámetro de configuración. | valor | String | Nuevo valor. | ParametroGlobal | P-PAR-001: El actor no es Administrador.<br>P-PAR-002: El valor no corresponde al tipo de dato.<br>P-PAR-003: El parámetro no existe. |
|  |  |  |  |  | actor | Usuario | Administrador. |  |  |
| public | consultar | static | final | Lista los parámetros globales. | actor | Usuario | Administrador. | Lista de ParametroGlobal |  |
| public | valorComoEntero | no static | final | Entrega el valor de un parámetro numérico. |  |  |  | int | P-PAR-002: El valor no corresponde al tipo de dato. |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | clave | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | clave | String | Builder |
| public | valor | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | valor | String | Builder |
| public | tipoDato | no static | Asigna el valor recibido; si es nulo, asigna: TEXTO. | tipoDato | TipoParametro | Builder |
| public | descripcion | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | descripcion | String | Builder |
| public | fechaModificacion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaModificacion | LocalDateTime | Builder |
| public | build | no static | Crea la instancia de ParametroGlobal con los valores reunidos, usando su constructor privado. | | | ParametroGlobal |
