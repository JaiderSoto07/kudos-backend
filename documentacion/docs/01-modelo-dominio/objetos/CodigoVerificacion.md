# CodigoVerificacion

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Código temporal que se envía al correo o al celular de un usuario para validar que le pertenece.

**Línea de vida:** 2

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único del código | UUID | Sí |
| usuario | Usuario que debe verificar el dato | [Usuario](Usuario.md) | Sí |
| canal | Dato que se verifica | [CanalVerificacion](../README.md#enumeraciones) | Sí |
| destino | Valor exacto al que se envió el código | Texto | Sí |
| codigo | Código de 6 dígitos generado con SecureRandom | Texto | Sí |
| fechaGeneracion | Fecha y hora de generación | FechaHora | Sí |
| fechaExpiracion | Fecha y hora en que vence | FechaHora | Sí |
| intentosFallidos | Número de intentos con código incorrecto | Entero | Sí |
| usado | Indica si el código ya se usó con éxito | Lógico | Sí |

### Datos de muestra

_3 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | usuario | canal | destino | codigo | fechaGeneracion | fechaExpiracion | intentosFallidos | usado |
|---|---|---|---|---|---|---|---|---|
| cv-01 | u-08 Valentina Cardona | CORREO | vcardona@uco.edu.co | 482913 | 2026-11-05 16:10 | 2026-11-05 16:25 | 0 | Sí |
| cv-02 | u-08 Valentina Cardona | CELULAR | 3051239876 | 730155 | 2026-11-05 16:10 | 2026-11-05 16:25 | 5 | No |
| cv-03 | u-08 Valentina Cardona | CELULAR | 3051239876 | 118604 | 2026-11-08 09:00 | 2026-11-08 09:15 | 1 | No |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Valida datos de-> | [Usuario](Usuario.md) | CodigoVerificacion 0..* — 1 Usuario | Composición |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | Un código temporal enviado al correo o al celular para comprobar que el dato pertenece a la persona. |
| **Tipo de objeto** | Entidad interna de agregado |
| **Agregado** | Usuario (entidad interna). |
| **¿Qué conoce? (información que administra)** | El canal, el destino al que se envió, el código de 6 dígitos, su vigencia, los intentos fallidos y si ya se usó. |
| **¿Qué hace? (responsabilidades)** | • Generar código<br>• Verificar código<br>• Invalidar código |
| **¿Qué decisiones toma?** | • Si está vencido o ya no puede usarse.<br>• Si un código digitado coincide.<br>• Que al llegar al máximo de intentos se invalida. |
| **¿Qué decisiones delega?** | • El envío al EnviadorNotificacion (correo y SMS simulados).<br>• Marcar el dato como verificado a Usuario. |
| **¿Con quién colabora?** | Usuario, ParametroGlobal, EnviadorNotificacion |
| **¿Qué eventos genera?** | • Código de verificación generado<br>• Correo o celular verificado<br>• Código invalidado |
| **¿Qué invariantes protege?** | • RN-077 a RN-082: correo y celular se verifican, también después de cambiarlos.<br>• Un código se usa una sola vez. |

### Caracterización de atributos

| Nombre atributo | id | usuario | canal | destino | codigo | fechaGeneracion | fechaExpiracion | intentosFallidos | usado |
|---|---|---|---|---|---|---|---|---|---|
| Descripción atributo | Identificador único del código | Usuario que debe verificar el dato | Dato que se verifica | Valor exacto al que se envió el código | Código de 6 dígitos generado con SecureRandom | Fecha y hora de generación | Fecha y hora en que vence | Número de intentos con código incorrecto | Indica si el código ya se usó con éxito |
| Identificador del objeto de dominio | Sí | No | No | No | No | No | No | No | No |
| Tipo de dato | UUID | Usuario | CanalVerificacion | Texto | Texto | FechaHora | FechaHora | Entero | Lógico |
| Obligatorio | Sí | Sí | Sí | Sí | Sí | Sí | Sí | Sí | Sí |
| Longitud mínima (si aplica) |  |  |  | 1 | 6 |  |  |  |  |
| Longitud máxima (si aplica) |  |  |  | 100 | 60 |  |  |  |  |
| Precisión (si aplica) |  |  |  |  |  |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  |  |  |  | 0 |  |
| Rango final (si aplica) |  |  |  |  |  |  |  | 5 |  |
| Formato (si aplica) |  |  |  |  | 6 dígitos; generado con SecureRandom |  |  |  |  |
| Reglas especiales (si aplica) | Autogenerado |  | CORREO o CELULAR | Máx. 100. Si el usuario cambia el dato, el código deja de servir | Vence a los 15 minutos; máximo 5 intentos fallidos | Asignada por el sistema | fechaGeneracion + parámetro VIGENCIA_CODIGO_MINUTOS (15) | 0 a parámetro MAX_INTENTOS_VERIFICACION (5); al llegar al máximo el código se invalida | Un código se usa una sola vez |
| Dato calculado | No | No | No | No | No | No | Sí | No | No |
| Quitar espacios en blanco al inicio y al fin | No | No | No | Sí | No | No | No | No | No |
| Dato sensible | No | No | No | No | Sí | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  |  |  | Fecha actual |  | 0 | No |
| Auto generado | Sí | No | No | No | No | Sí | Sí | No | No |
| Generar código | No requerido | Requerido | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Verificar código | Requerido | No requerido | No requerido | No requerido | Requerido | No requerido | No requerido | No requerido | No requerido |
| Invalidar código | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |

### Combinaciones únicas

No tiene combinaciones únicas además de su identificador.

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Generar código** (`generar()`) | Crea un código temporal para verificar el correo o el celular y lo envía. | usuario | Usuario | Usuario a verificar. | CodigoVerificacion | Código vigente enviado. | P-CVE-001 | Generar un código aleatorio de 6 dígitos con SecureRandom. |  |  |
|  |  | canal | CanalVerificacion | CORREO o CELULAR. |  |  | P-CVE-002 | Fijar la vigencia del código según el parámetro VIGENCIA_CODIGO_MINUTOS. |  |  |
|  |  |  |  |  |  |  | P-CVE-003 | Al generar un código, invalidar los códigos anteriores del mismo usuario y canal. |  |  |
|  |  |  |  |  |  |  | P-CVE-004 | Enviar el código por el canal correspondiente mediante el EnviadorNotificacion. | No fue posible enviar el código. | Informar el error y permitir reenviar. |
| **Verificar código** (`verificar()`) | Compara el código digitado con el código vigente. | codigo | String | Código digitado. | boolean | Verdadero si el código es correcto. | P-CVE-005 | Asegurar que el código no esté vencido, usado ni invalidado. | El código ya no es válido. | Rechazar la verificación e indicar que solicite un nuevo código. |
|  |  | destinoActual | String | Dato actual del usuario. |  |  | P-CVE-006 | Asegurar que el destino del código coincida con el dato actual del usuario. | El usuario cambió el dato después de recibir el código. | Rechazar la verificación e indicar que solicite un nuevo código. |
|  |  |  |  |  |  |  | P-CVE-007 | Si el código no coincide, sumar un intento fallido; al llegar a MAX_INTENTOS_VERIFICACION, invalidarlo. | El código digitado es incorrecto. | Rechazar e informar los intentos restantes. |
| **Invalidar código** (`invalidar()`) | Deja el código sin posibilidad de uso. | Ninguno | - | No requiere datos de entrada. | CodigoVerificacion | Código invalidado. | P-CVE-008 | Un código invalidado o usado no puede volver a usarse. |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Sistema | Generar código | Crea un código temporal para verificar el correo o el celular y lo envía. | CodigoVerificacion | Parámetro VIGENCIA_CODIGO_MINUTOS | Minutos de vigencia. | P-CVE-001 | EnviadorNotificacion (correo / SMS simulados) | Código de verificación generado | Evento de integración |
|  |  |  |  |  |  | P-CVE-002 |  |  |  |
|  |  |  |  |  |  | P-CVE-003 |  |  |  |
|  |  |  |  |  |  | P-CVE-004 |  |  |  |
| Usuario registrado | Verificar código | Compara el código digitado con el código vigente. | CodigoVerificacion | Parámetro MAX_INTENTOS_VERIFICACION | Intentos permitidos. | P-CVE-005 |  | Correo o celular verificado | EVT-002, EVT-003 |
|  |  |  |  |  |  | P-CVE-006 |  |  |  |
|  |  |  |  |  |  | P-CVE-007 |  |  |  |
| Sistema | Invalidar código | Deja el código sin posibilidad de uso. | CodigoVerificacion |  |  | P-CVE-008 |  | Código invalidado |  |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-CVE-001 | Generar un código aleatorio de 6 dígitos con SecureRandom. | CodigoVerificacion | RN-077, RN-078, RNF-003 |
| P-CVE-002 | Fijar la vigencia del código según el parámetro VIGENCIA_CODIGO_MINUTOS. | ParametroGlobal | D-10, D-11 |
| P-CVE-003 | Al generar un código, invalidar los códigos anteriores del mismo usuario y canal. | CodigoVerificacion | D-10 |
| P-CVE-004 | Enviar el código por el canal correspondiente mediante el EnviadorNotificacion. | EnviadorNotificacion | D-10 |
| P-CVE-005 | Asegurar que el código no esté vencido, usado ni invalidado. | CodigoVerificacion | D-10 |
| P-CVE-006 | Asegurar que el destino del código coincida con el dato actual del usuario. | CodigoVerificacion | RN-081, RN-082 |
| P-CVE-007 | Si el código no coincide, sumar un intento fallido; al llegar a MAX_INTENTOS_VERIFICACION, invalidarlo. | CodigoVerificacion | D-10, D-11 |
| P-CVE-008 | Un código invalidado o usado no puede volver a usarse. | CodigoVerificacion | D-10 |

## En el código

| Capa | Clase |
|---|---|
| Dominio (atributos y comportamiento) | `CodigoVerificacionDominio` (`co.edu.uco.kudos.dominio`) |
| Entidad (tabla) | `CodigoVerificacionEntidad` |
| DAO | `CodigoVerificacionDAO` + implementación SQL Server |
| DTO (API) | CodigoVerificacionDto |
| Negocio | VerificacionNegocioImpl |
