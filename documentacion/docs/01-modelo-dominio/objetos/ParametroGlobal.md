# ParametroGlobal

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Valor de configuración de la plataforma que administran los Administradores (RN-130, D-11).

**Línea de vida:** 1

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único del parámetro | UUID | Sí |
| clave | Nombre técnico del parámetro | Texto | Sí |
| valor | Valor vigente del parámetro | Texto | Sí |
| tipoDato | Tipo de dato del valor | [TipoParametro](../README.md#enumeraciones) | Sí |
| descripcion | Para qué sirve el parámetro | Texto | Sí |
| fechaModificacion | Fecha y hora del último cambio | FechaHora | Sí |

### Datos de muestra

_3 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | clave | valor | tipoDato | descripcion | fechaModificacion |
|---|---|---|---|---|---|
| pg-01 | DOMINIO_CORREO | uco.edu.co | TEXTO | Dominio obligatorio del correo institucional | 2026-11-01 08:00 |
| pg-02 | VIGENCIA_CODIGO_MINUTOS | 15 | ENTERO | Minutos de vigencia de un código de verificación | 2026-11-01 08:00 |
| pg-03 | MAX_INTENTOS_VERIFICACION | 5 | ENTERO | Intentos fallidos permitidos por código | 2026-11-01 08:00 |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | Un valor de configuración de la plataforma que administran los Administradores. |
| **Tipo de objeto** | Entidad raíz de agregado |
| **Agregado** | ParametroGlobal (raíz). |
| **¿Qué conoce? (información que administra)** | Su clave, su valor, su tipo de dato, su descripción y su última modificación. |
| **¿Qué hace? (responsabilidades)** | • Cambiar valor<br>• Consultar parámetros<br>• Obtener valor entero |
| **¿Qué decisiones toma?** | • Si un valor nuevo es coherente con su tipo de dato. |
| **¿Qué decisiones delega?** | • Quién puede modificarlo a la política de autorización. |
| **¿Con quién colabora?** | CorreoInstitucional, CodigoVerificacion |
| **¿Qué eventos genera?** | • Parámetro global modificado<br>• Parámetros consultados |
| **¿Qué invariantes protege?** | • RN-130: solo los Administradores gestionan los parámetros.<br>• La clave no cambia. |

### Caracterización de atributos

| Nombre atributo | id | clave | valor | tipoDato | descripcion | fechaModificacion |
|---|---|---|---|---|---|---|
| Descripción atributo | Identificador único del parámetro | Nombre técnico del parámetro | Valor vigente del parámetro | Tipo de dato del valor | Para qué sirve el parámetro | Fecha y hora del último cambio |
| Identificador del objeto de dominio | Sí | No | No | No | No | No |
| Tipo de dato | UUID | Texto | Texto | TipoParametro | Texto | FechaHora |
| Obligatorio | Sí | Sí | Sí | Sí | Sí | Sí |
| Longitud mínima (si aplica) |  | 1 | 1 |  | 1 |  |
| Longitud máxima (si aplica) |  | 50 | 250 |  | 250 |  |
| Precisión (si aplica) |  |  |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  |  |  |
| Rango final (si aplica) |  |  |  |  |  |  |
| Formato (si aplica) |  | MAYÚSCULAS_CON_GUION_BAJO |  |  |  |  |
| Reglas especiales (si aplica) | Autogenerado | Máx. 50; mayúsculas y guion bajo; único; no se modifica | Máx. 250; debe ser coherente con tipoDato | TEXTO o ENTERO | Máx. 250 |  |
| Dato calculado | No | No | No | No | No | No |
| Quitar espacios en blanco al inicio y al fin | No | Sí | Sí | No | Sí | No |
| Dato sensible | No | No | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  |  |  | Fecha actual |
| Auto generado | Sí | No | No | No | No | No |
| Cambiar valor | Requerido | No requerido | Requerido | No requerido | No requerido | No requerido |
| Consultar parámetros | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |
| Obtener valor entero | No requerido | Requerido | No requerido | No requerido | No requerido | No requerido |

### Combinaciones únicas

| Nombre combinación única | Descripción | Atributos involucrados |
|---|---|---|
| UQ-PAR-1 | No pueden existir dos parámetros con la misma clave | clave |

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Cambiar valor** (`cambiarValor()`) | Modifica el valor de un parámetro de configuración. | valor | String | Nuevo valor. | ParametroGlobal | Parámetro modificado. | P-PAR-001 | Asegurar que solo un Administrador modifique los parámetros globales. | El actor no es Administrador. | Rechazar la modificación. |
|  |  | actor | Usuario | Administrador. |  |  | P-PAR-002 | Asegurar que el valor sea coherente con el tipo de dato del parámetro. | El valor no corresponde al tipo de dato. | Rechazar la modificación e informar el tipo esperado. |
|  |  |  |  |  |  |  | P-PAR-003 | Asegurar que el parámetro exista; su clave no cambia y la modificación se audita. | El parámetro no existe. | Rechazar la modificación. |
| **Consultar parámetros** (`consultar()`) | Lista los parámetros globales. | actor | Usuario | Administrador. | Lista de ParametroGlobal | Parámetros. | P-PAR-004 | Solo los Administradores consultan la lista de parámetros. |  |  |
| **Obtener valor entero** (`valorComoEntero()`) | Entrega el valor de un parámetro numérico. | Ninguno | - | No requiere datos de entrada. | int | Valor numérico. | P-PAR-002 | Asegurar que el valor sea coherente con el tipo de dato del parámetro. | El valor no corresponde al tipo de dato. | Rechazar la modificación e informar el tipo esperado. |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Administrador | Cambiar valor | Modifica el valor de un parámetro de configuración. | ParametroGlobal |  |  | P-PAR-001 |  | Parámetro global modificado | RN-130 |
|  |  |  |  |  |  | P-PAR-002 |  |  |  |
|  |  |  |  |  |  | P-PAR-003 |  |  |  |
| Administrador | Consultar parámetros | Lista los parámetros globales. | ParametroGlobal |  |  | P-PAR-004 |  | Parámetros consultados |  |
| Sistema | Obtener valor entero | Entrega el valor de un parámetro numérico. | ParametroGlobal |  |  | P-PAR-002 |  | (consulta interna, sin evento) |  |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-PAR-001 | Asegurar que solo un Administrador modifique los parámetros globales. | PoliticaAutorizacion | RN-130, RN-131 |
| P-PAR-002 | Asegurar que el valor sea coherente con el tipo de dato del parámetro. | ParametroGlobal | D-11 |
| P-PAR-003 | Asegurar que el parámetro exista; su clave no cambia y la modificación se audita. | ParametroGlobal | RN-131, P-AUD-001 |
| P-PAR-004 | Solo los Administradores consultan la lista de parámetros. | PoliticaAutorizacion | RN-130 |
