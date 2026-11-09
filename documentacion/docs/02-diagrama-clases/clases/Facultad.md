# Facultad

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: Facultad](../../01-modelo-dominio/objetos/Facultad.md) · [Entidad MER: Facultad](../../03-modelo-entidad-relacion/entidades/Facultad.md)

| Campo | Valor |
|---|---|
| Clase | Facultad |
| Estereotipo | class (entidad de dominio) |
| Descripción | Unidad académica de la Universidad que agrupa Programas Académicos y es acompañada por un Coordinador de Facultad. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Es usada por | Asociación | AsignacionRol |
| Es usada por | Composición | ProgramaAcademico |
| Es usada por | Asociación | SolicitudRegistro |
| Necesita de | Dependencia (enumeración) | EstadoFacultad |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único de la Facultad (RN-039) |
| private | nombre | String |  | no final | no static | "" (cadena vacía) | Nombre oficial de la Facultad (RN-040) |
| private | estado | Otro | EstadoFacultad | no final | no static | EN_CONFORMACION | Estado de la Facultad |
| private | fechaCreacion | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora de creación |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | Facultad | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getNombre | getter | Obtiene el valor del atributo nombre. | String |  |
| public | getEstado | getter | Obtiene el valor del atributo estado. | Otro | EstadoFacultad |
| public | getFechaCreacion | getter | Obtiene el valor del atributo fechaCreacion. | Otro | LocalDateTime |

## Métodos

Métodos de negocio: salen de las responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/Facultad.md#responsabilidades). Las consultas se marcan `static`, como en Sigma: en la implementación las resuelve la capa de negocio con su DAO.

| Modificador de acceso | Nombre | Tipo acceso método | Tipo modificación método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · Descripción | Tipo dato de retorno | Excepciones (políticas que puede incumplir) |
|---|---|---|---|---|---|---|---|---|---|
| public | crear | no static | final | Crea una Facultad nueva en conformación. | nombre | String | Nombre oficial. | Facultad | P-FAC-001: El nombre no es válido.<br>P-FAC-002: Ya existe una Facultad con ese nombre.<br>P-FAC-003: El actor no es Coordinador Académico. |
|  |  |  |  |  | actor | Usuario | Coordinador Académico. |  |  |
| public | renombrar | no static | final | Cambia el nombre de la Facultad. | nombre | String | Nuevo nombre. | Facultad | P-FAC-001: El nombre no es válido.<br>P-FAC-002: Ya existe una Facultad con ese nombre.<br>P-FAC-003: El actor no es Coordinador Académico.<br>P-FAC-005: La Facultad está inactiva. |
|  |  |  |  |  | actor | Usuario | Coordinador Académico. |  |  |
| public | activar | no static | final | Pasa la Facultad a ACTIVA cuando recibe su primer programa. |  |  |  | Facultad |  |
| public | inactivar | no static | final | Saca de operación una Facultad que no tiene programas activos. | actor | Usuario | Coordinador Académico. | Facultad | P-FAC-003: El actor no es Coordinador Académico.<br>P-FAC-007: La Facultad tiene programas activos. |
| public | consultar | static | final | Lista las Facultades con su estado y su coordinador vigente. | actor | Usuario | Usuario que consulta. | Lista de Facultad |  |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | nombre | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | nombre | String | Builder |
| public | estado | no static | Asigna el valor recibido; si es nulo, asigna: EN_CONFORMACION. | estado | EstadoFacultad | Builder |
| public | fechaCreacion | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaCreacion | LocalDateTime | Builder |
| public | build | no static | Crea la instancia de Facultad con los valores reunidos, usando su constructor privado. | | | Facultad |
