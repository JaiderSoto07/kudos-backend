# Categoria

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: Categoria](../../01-modelo-dominio/objetos/Categoria.md) · [Entidad MER: Categoria](../../03-modelo-entidad-relacion/entidades/Categoria.md)

| Campo | Valor |
|---|---|
| Clase | Categoria |
| Clase en el código | `CategoriaDominio` |
| Estereotipo | class (entidad de dominio) |
| Descripción | Clasificación institucional de los reconocimientos (RN-132). Puede estar activa o inactiva; solo las activas se usan para Kudos nuevos. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Es usada por | Asociación | Kudo |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único de la categoría |
| private | nombre | String |  | no final | no static | "" (cadena vacía) | Nombre de la categoría |
| private | descripcion | String |  | no final | no static | "" (cadena vacía) | Qué tipo de comportamiento o logro reconoce |
| private | activa | boolean/Boolean |  | no final | no static | true | Indica si puede usarse para Kudos nuevos (RN-133, RN-144) |
| private | fechaCreacion | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora de creación |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | Categoria | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getNombre | getter | Obtiene el valor del atributo nombre. | String |  |
| public | getDescripcion | getter | Obtiene el valor del atributo descripcion. | String |  |
| public | isActiva | getter | Obtiene el valor del atributo activa. | boolean/Boolean |  |
| public | getFechaCreacion | getter | Obtiene el valor del atributo fechaCreacion. | Otro | LocalDateTime |

## Métodos de negocio

Métodos **reales** de `CategoriaDominio` (comportamiento del dominio rico, D-29).

| Modificador de acceso | Nombre | Parámetros | Tipo dato de retorno | Descripción |
|---|---|---|---|---|
| public | modificar | String nuevoNombre, String nuevaDescripcion | void | Cambia el nombre o la descripción de una categoría. |
| public | activar | — | void | Permite de nuevo usar la categoría en Kudos nuevos. |
| public | desactivar | — | void | Impide usar la categoría en Kudos nuevos. |

## Operaciones del caso de uso (capa de negocio)

Responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/Categoria.md#responsabilidades) que coordinan varios objetos o consultan la base de datos. Las implementa: CategoriaNegocioImpl.

| Operación | Descripción | Políticas que protege |
|---|---|---|
| crear | Crea una categoría institucional de reconocimiento. | P-CAT-001, P-CAT-002, P-CAT-003, P-CAT-004 |
| consultar | Lista las categorías; las inactivas solo para los gestores. | P-CAT-008 |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | nombre | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | nombre | String | Builder |
| public | descripcion | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | descripcion | String | Builder |
| public | activa | no static | Asigna el valor recibido. | activa | boolean | Builder |
| public | fechaCreacion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaCreacion | LocalDateTime | Builder |
| public | build | no static | Crea la instancia de Categoria con los valores reunidos, usando su constructor privado. | | | Categoria |
