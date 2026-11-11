# Usuario

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Persona de la comunidad universitaria que tiene una cuenta en la plataforma. Su rol institucional vigente se conoce por su AsignacionRol vigente.

**Línea de vida:** 1

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único del usuario | UUID | Sí |
| nombres | Nombres de la persona | Texto | Sí |
| apellidos | Apellidos de la persona | Texto | Sí |
| correoInstitucional | Correo de la cuenta de Google con la que se registra e ingresa | Texto | Sí |
| correoVerificado | Indica si el correo fue validado al ingresar con Google | Lógico | Sí |
| numeroCelular | Número celular de contacto | Texto | Sí |
| celularVerificado | Indica si el celular fue validado con un código | Lógico | Sí |
| aprobada | Indica si la autoridad correspondiente aprobó la cuenta | Lógico | Sí |
| estadoCuenta | Estado del ciclo de vida de la cuenta | [EstadoCuenta](../README.md#enumeraciones) | Sí |
| fechaRegistro | Fecha y hora en que se creó la cuenta | FechaHora | Sí |

### Datos de muestra

_9 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | nombres | apellidos | correoInstitucional | correoVerificado | numeroCelular | celularVerificado | aprobada | estadoCuenta | fechaRegistro |
|---|---|---|---|---|---|---|---|---|---|
| u-01 | Sistema | Administrador | admin@uco.edu.co | Sí | 3001112233 | Sí | Sí | ACTIVA | 2026-11-01 08:00 |
| u-02 | María Fernanda | Gómez Arango | mgomez@uco.edu.co | Sí | 3104567890 | Sí | Sí | ACTIVA | 2026-11-01 09:00 |
| u-03 | Carlos Andrés | Restrepo Mejía | crestrepo@uco.edu.co | Sí | 3127788990 | Sí | Sí | ACTIVA | 2026-11-01 10:00 |
| u-04 | Luisa Fernanda | Ospina Duque | lospina@uco.edu.co | Sí | 3154433221 | Sí | Sí | ACTIVA | 2026-11-02 08:30 |
| u-05 | Juan Pablo | Zuluaga Henao | jzuluaga@uco.edu.co | Sí | 3186655443 | Sí | Sí | ACTIVA | 2026-11-02 11:00 |
| u-06 | Andrea Marcela | Giraldo Ríos | agiraldo@uco.edu.co | Sí | 3017766554 | Sí | Sí | ACTIVA | 2026-11-03 07:45 |
| u-07 | Santiago | Henao Cardona | shenao@uco.edu.co | Sí | 3209988776 | Sí | Sí | ACTIVA | 2026-11-03 14:20 |
| u-08 | Valentina | Cardona López | vcardona@uco.edu.co | Sí | 3051239876 | No | Sí | PENDIENTE_VERIFICACION | 2026-11-05 16:10 |
| u-09 | Mateo | Arango Valencia | marango@uco.edu.co | No | 3112345678 | No | No | PENDIENTE_APROBACION | 2026-11-07 10:05 |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Tiene-> | [AsignacionRol](AsignacionRol.md) | Usuario 1 — 0..* AsignacionRol | Composición |
| Participa mediante-> | [VinculacionPrograma](VinculacionPrograma.md) | Usuario 1 — 0..* VinculacionPrograma | Asociación |
| Presenta-> | [SolicitudRegistro](SolicitudRegistro.md) | Usuario 1 — 0..* SolicitudRegistro | Asociación |
| Decide-> | [SolicitudRegistro](SolicitudRegistro.md) | Usuario 0..1 — 0..* SolicitudRegistro | Asociación |
| Verifica con-> | [CodigoVerificacion](CodigoVerificacion.md) | Usuario 1 — 0..* CodigoVerificacion | Composición |
| Otorga-> | [Kudo](Kudo.md) | Usuario 1 — 0..* Kudo | Asociación |
| Recibe-> | [Kudo](Kudo.md) | Usuario 1 — 0..* Kudo | Asociación |
| Escribe-> | [Replica](Replica.md) | Usuario 1 — 0..* Replica | Asociación |
| Asigna-> | [Calificacion](Calificacion.md) | Usuario 1 — 0..* Calificacion | Asociación |
| Hace-> | [Reporte](Reporte.md) | Usuario 1 — 0..* Reporte | Asociación |
| Modera-> | [Moderacion](Moderacion.md) | Usuario 1 — 0..* Moderacion | Asociación |
| Genera-> | [Auditoria](Auditoria.md) | Usuario 0..1 — 0..* Auditoria | Asociación |
| Recibe-> | [Notificacion](Notificacion.md) | Usuario 1 — 0..* Notificacion | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | La identidad institucional de una persona de la comunidad UCO dentro de la plataforma y el ciclo de vida de su cuenta. |
| **Tipo de objeto** | Entidad raíz de agregado |
| **Agregado** | Usuario (raíz). Contiene sus AsignacionRol y sus CodigoVerificacion. |
| **¿Qué conoce? (información que administra)** | Sus datos personales, su correo y celular (y si están verificados), si fue aprobada, el estado de su cuenta, su rol vigente y su historial de roles. |
| **¿Qué hace? (responsabilidades)** | • Registrar usuario<br>• Registrar Rector o Administrador<br>• Verificar correo<br>• Verificar celular<br>• Aprobar cuenta<br>• Activar cuenta<br>• Iniciar sesión<br>• Actualizar datos personales<br>• Cambiar correo<br>• Cambiar celular<br>• Suspender cuenta<br>• Reactivar cuenta<br>• Desactivar cuenta<br>• Decidir si tiene un rol vigente<br>• Consultar información personal |
| **¿Qué decisiones toma?** | • Si su cuenta puede pasar a ACTIVA (aprobada + correo verificado + celular verificado).<br>• Si puede iniciar sesión y, si no, qué le falta.<br>• Si una transición de estado de cuenta es válida (suspender, reactivar, desactivar).<br>• Que un cambio de correo o celular deja el dato sin verificar. |
| **¿Qué decisiones delega?** | • La validación del formato del correo y del celular a las reglas ValidarCorreoInstitucionalRule y ValidarCelularRule.<br>• La unicidad de correo y celular a reglas de negocio que consultan la base de datos.<br>• La verificación del código a CodigoVerificacion.<br>• La decisión de quién puede aprobarlo o gestionarlo a la AsignacionRol de la autoridad (puedeDecidir) y a AsegurarActorTieneRolRule. |
| **¿Con quién colabora?** | AsignacionRol, CodigoVerificacion, SolicitudRegistro, VinculacionPrograma, ParametroGlobal |
| **¿Qué eventos genera?** | • Usuario registrado<br>• Usuario registrado y Rector asignado<br>• Correo verificado<br>• Celular verificado<br>• Usuario aprobado<br>• Cuenta activada<br>• Sesión iniciada<br>• Datos personales modificados<br>• Correo modificado<br>• Celular modificado<br>• Cuenta suspendida<br>• Cuenta desactivada<br>• Información personal consultada |
| **¿Qué invariantes protege?** | • INV-005/RN-001: máximo un rol vigente.<br>• RN-075/RN-076: no existe cuenta activa sin correo o sin celular.<br>• RN-084 a RN-089: ACTIVA solo con las tres condiciones.<br>• RN-107: el usuario no modifica su propio rol. |

### Caracterización de atributos

| Nombre atributo | id | nombres | apellidos | correoInstitucional | correoVerificado | numeroCelular | celularVerificado | aprobada | estadoCuenta | fechaRegistro |
|---|---|---|---|---|---|---|---|---|---|---|
| Descripción atributo | Identificador único del usuario | Nombres de la persona | Apellidos de la persona | Correo de la cuenta de Google con la que se registra e ingresa | Indica si el correo fue validado al ingresar con Google | Número celular de contacto | Indica si el celular fue validado con un código | Indica si la autoridad correspondiente aprobó la cuenta | Estado del ciclo de vida de la cuenta | Fecha y hora en que se creó la cuenta |
| Identificador del objeto de dominio | Sí | No | No | No | No | No | No | No | No | No |
| Tipo de dato | UUID | Texto | Texto | Texto | Lógico | Texto | Lógico | Lógico | EstadoCuenta | FechaHora |
| Obligatorio | Sí | Sí | Sí | Sí | Sí | Sí | Sí | Sí | Sí | Sí |
| Longitud mínima (si aplica) |  | 1 | 1 | 6 |  | 10 |  |  |  |  |
| Longitud máxima (si aplica) |  | 60 | 60 | 100 |  | 10 |  |  |  |  |
| Precisión (si aplica) |  |  |  |  |  |  |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  |  |  |  |  |  |  |
| Rango final (si aplica) |  |  |  |  |  |  |  |  |  |  |
| Formato (si aplica) |  | Letras y espacios | Letras y espacios | usuario@uco.edu.co (minúsculas) |  | 3XXXXXXXXX |  |  |  | dd/MM/aaaa HH:mm |
| Reglas especiales (si aplica) | Autogenerado | 1 a 60 caracteres; sin espacios al inicio ni al final | 1 a 60 caracteres; sin espacios al inicio ni al final | Máx. 100; minúsculas; su dominio debe estar en el parámetro DOMINIO_CORREO; único | Vuelve a No si el correo cambia, hasta ingresar con Google con el correo nuevo (RN-081) | 10 dígitos; inicia en 3; único | Vuelve a No si el celular cambia (RN-082) | Rector y Administrador quedan aprobados al ser registrados por un Administrador | ACTIVA solo si aprobada + correoVerificado + celularVerificado (RN-088) | Asignada por el sistema |
| Dato calculado | No | No | No | No | No | No | No | No | Sí | No |
| Quitar espacios en blanco al inicio y al fin | No | Sí | Sí | Sí | No | Sí | No | No | No | No |
| Dato sensible | No | No | No | No | No | No | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  |  | No |  | No | No | PENDIENTE_APROBACION | Fecha actual |
| Auto generado | Sí | No | No | No | No | No | No | No | Sí | Sí |
| Registrar usuario | No requerido | Requerido | Requerido | Requerido | No requerido | Requerido | No requerido | No requerido | No requerido | No requerido |
| Registrar Rector o Administrador | No requerido | Requerido | Requerido | Requerido | No requerido | Requerido | No requerido | No requerido | No requerido | No requerido |
| Verificar correo | Requerido | No requerido | No requerido | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Verificar celular | Requerido | No requerido | No requerido | No requerido | No requerido | Requerido | No requerido | No requerido | No requerido | No requerido |
| Aprobar cuenta | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Activar cuenta | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Iniciar sesión | No requerido | No requerido | No requerido | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Actualizar datos personales | Requerido | Requerido | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Cambiar correo | Requerido | No requerido | No requerido | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Cambiar celular | Requerido | No requerido | No requerido | No requerido | No requerido | Requerido | No requerido | No requerido | No requerido | No requerido |
| Suspender cuenta | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Reactivar cuenta | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Desactivar cuenta | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Decidir si tiene un rol vigente | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Consultar información personal | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |

### Combinaciones únicas

| Nombre combinación única | Descripción | Atributos involucrados |
|---|---|---|
| UQ-USU-1 | No pueden existir dos usuarios con el mismo correo institucional (RN-073) | correoInstitucional |
| UQ-USU-2 | No pueden existir dos usuarios con el mismo número celular (RN-074) | numeroCelular |

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Registrar usuario** (`registrar()`) | Crea la cuenta de una persona que se autorregistra con su identidad institucional. | datos | Usuario | Nombres, apellidos y celular; el correo lo entrega Google. | Usuario | Usuario registrado en PENDIENTE_APROBACION. | P-USU-001 | Asegurar que los datos personales sean válidos: nombres y apellidos de 1 a 60 caracteres, sin espacios al inicio ni al final. | Los nombres o apellidos están vacíos o superan 60 caracteres. | Rechazar la operación e informar qué dato no es válido. |
|  |  |  |  |  |  |  | P-USU-002 | Asegurar que el correo tenga formato válido y termine en el dominio institucional configurado. | El correo no tiene formato válido o no es institucional. | Rechazar la operación e informar que debe usar su correo institucional. |
|  |  |  |  |  |  |  | P-USU-003 | Asegurar que no exista otro usuario con el mismo correo institucional. | Ya existe un usuario con ese correo. | Rechazar la operación e informar que el correo ya está registrado. |
|  |  |  |  |  |  |  | P-USU-004 | Asegurar que el celular sea un número colombiano de 10 dígitos que inicia en 3. | El celular no tiene el formato esperado. | Rechazar la operación e informar el formato correcto. |
|  |  |  |  |  |  |  | P-USU-005 | Asegurar que no exista otro usuario con el mismo número celular. | Ya existe un usuario con ese celular. | Rechazar la operación e informar que el celular ya está registrado. |
|  |  |  |  |  |  |  | P-USU-006 | No guardar contraseñas: la identidad la confirma Google al ingresar. |  |  |
|  |  |  |  |  |  |  | P-USU-007 | Al registrarse, la cuenta queda en PENDIENTE_APROBACION con correo y celular sin verificar, y se envían los códigos de verificación. | No fue posible generar o enviar los códigos. | Rechazar el registro completo para no dejar una cuenta sin forma de verificarse. |
|  |  |  |  |  |  |  | P-USU-008 | Asegurar que el identificador generado no esté siendo usado por otro usuario. | El identificador generado ya existe. | Generar un nuevo identificador hasta garantizar que no exista. |
| **Registrar Rector o Administrador** (`registrarPorAdministrador()`) | Un Administrador registra directamente a un Rector o a otro Administrador, que queda aprobado. | datos | Usuario | Datos personales del nuevo usuario. | Usuario | Usuario aprobado en PENDIENTE_VERIFICACION. | P-USU-001 | Asegurar que los datos personales sean válidos: nombres y apellidos de 1 a 60 caracteres, sin espacios al inicio ni al final. | Los nombres o apellidos están vacíos o superan 60 caracteres. | Rechazar la operación e informar qué dato no es válido. |
|  |  | rol | Rol | RECTOR o ADMINISTRADOR. |  |  | P-USU-002 | Asegurar que el correo tenga formato válido y termine en el dominio institucional configurado. | El correo no tiene formato válido o no es institucional. | Rechazar la operación e informar que debe usar su correo institucional. |
|  |  | actor | Usuario | Administrador que registra. |  |  | P-USU-003 | Asegurar que no exista otro usuario con el mismo correo institucional. | Ya existe un usuario con ese correo. | Rechazar la operación e informar que el correo ya está registrado. |
|  |  |  |  |  |  |  | P-USU-004 | Asegurar que el celular sea un número colombiano de 10 dígitos que inicia en 3. | El celular no tiene el formato esperado. | Rechazar la operación e informar el formato correcto. |
|  |  |  |  |  |  |  | P-USU-005 | Asegurar que no exista otro usuario con el mismo número celular. | Ya existe un usuario con ese celular. | Rechazar la operación e informar que el celular ya está registrado. |
|  |  |  |  |  |  |  | P-USU-006 | No guardar contraseñas: la identidad la confirma Google al ingresar. |  |  |
|  |  |  |  |  |  |  | P-USU-009 | Asegurar que solo un Administrador registre Rectores y Administradores. | El actor no es Administrador. | Rechazar la operación e informar que no tiene permiso. |
|  |  |  |  |  |  |  | P-USU-010 | Asegurar que no exista otro Rector vigente al registrar un Rector. | Ya existe un Rector vigente. | Rechazar el registro e informar que primero debe desactivarse el Rector actual. |
|  |  |  |  |  |  |  | P-USU-011 | Al ser registrado por un Administrador, el usuario queda aprobado y en PENDIENTE_VERIFICACION. |  |  |
| **Verificar correo** (`verificarCorreo()`) | Marca el correo como verificado con el código recibido. | codigo | String | Código de 6 dígitos recibido en el correo. | Usuario | Usuario con correo verificado (y activado si cumple las condiciones). | P-USU-012 | Asegurar que el código digitado corresponda al código vigente del canal y del dato actual. | El código es incorrecto, está vencido o fue invalidado. | Rechazar la verificación e informar el motivo; ofrecer reenviar el código. |
|  |  |  |  |  |  |  | P-USU-013 | Después de verificar un dato o de aprobar la cuenta, intentar activarla. |  |  |
| **Verificar celular** (`verificarCelular()`) | Marca el celular como verificado con el código recibido. | codigo | String | Código de 6 dígitos recibido por SMS. | Usuario | Usuario con celular verificado (y activado si cumple las condiciones). | P-USU-012 | Asegurar que el código digitado corresponda al código vigente del canal y del dato actual. | El código es incorrecto, está vencido o fue invalidado. | Rechazar la verificación e informar el motivo; ofrecer reenviar el código. |
|  |  |  |  |  |  |  | P-USU-013 | Después de verificar un dato o de aprobar la cuenta, intentar activarla. |  |  |
| **Aprobar cuenta** (`aprobar()`) | Registra que la autoridad competente aprobó la cuenta. | Ninguno | - | No requiere datos de entrada. | Usuario | Usuario aprobado. | P-USU-014 | Asegurar que solo se apruebe una cuenta pendiente de aprobación. | La cuenta ya fue aprobada o está desactivada. | Rechazar la aprobación e informar el estado de la cuenta. |
|  |  |  |  |  |  |  | P-USU-013 | Después de verificar un dato o de aprobar la cuenta, intentar activarla. |  |  |
| **Activar cuenta** (`intentarActivar()`) | Pasa la cuenta a ACTIVA si está aprobada y tiene correo y celular verificados. | Ninguno | - | No requiere datos de entrada. | boolean | Verdadero si la cuenta quedó activa. | P-USU-015 | La cuenta pasa a ACTIVA solo si está aprobada y tiene correo y celular verificados. | Falta alguna de las tres condiciones. | Mantener la cuenta en su estado e indicar qué falta. |
| **Iniciar sesión** (`puedeIniciarSesion()`) | Decide si la persona autenticada con Google puede usar la plataforma. | correo | String | Correo verificado por Google. | ResultadoIngreso | Permite el ingreso o indica qué le falta. | P-USU-016 | Asegurar que solo usen la aplicación correos registrados: si el correo de Google no existe, se muestra el registro. | El correo no está registrado. | Mostrar el formulario de registro. |
|  |  |  |  |  |  |  | P-USU-017 | Asegurar que solo ingresen cuentas ACTIVA e indicar qué le falta a las demás. | La cuenta no está activa. | Rechazar el ingreso e informar si falta aprobación, verificación o si está suspendida o desactivada. |
| **Actualizar datos personales** (`actualizarDatosPersonales()`) | Modifica los nombres y apellidos del propio usuario. | datos | Usuario | Nuevos nombres y apellidos. | Usuario | Usuario actualizado. | P-USU-001 | Asegurar que los datos personales sean válidos: nombres y apellidos de 1 a 60 caracteres, sin espacios al inicio ni al final. | Los nombres o apellidos están vacíos o superan 60 caracteres. | Rechazar la operación e informar qué dato no es válido. |
|  |  |  |  |  |  |  | P-USU-018 | Asegurar que el usuario no pueda modificar su propio rol. | Se intenta cambiar el rol desde la información personal. | Ignorar el cambio de rol; no existe operación para hacerlo. |
|  |  |  |  |  |  |  | P-USU-019 | Registrar en auditoría toda modificación de información personal con valor anterior y nuevo. |  |  |
| **Cambiar correo** (`cambiarCorreo()`) | Cambia el correo institucional y exige verificarlo de nuevo. | correo | String | Nuevo correo institucional. | Usuario | Usuario con correo sin verificar. | P-USU-002 | Asegurar que el correo tenga formato válido y termine en el dominio institucional configurado. | El correo no tiene formato válido o no es institucional. | Rechazar la operación e informar que debe usar su correo institucional. |
|  |  |  |  |  |  |  | P-USU-003 | Asegurar que no exista otro usuario con el mismo correo institucional. | Ya existe un usuario con ese correo. | Rechazar la operación e informar que el correo ya está registrado. |
|  |  |  |  |  |  |  | P-USU-020 | Al cambiar el correo, marcarlo como no verificado, enviar un nuevo código y dejar la cuenta en PENDIENTE_VERIFICACION. |  |  |
|  |  |  |  |  |  |  | P-USU-019 | Registrar en auditoría toda modificación de información personal con valor anterior y nuevo. |  |  |
| **Cambiar celular** (`cambiarCelular()`) | Cambia el número celular y exige verificarlo de nuevo. | celular | String | Nuevo número celular. | Usuario | Usuario con celular sin verificar. | P-USU-004 | Asegurar que el celular sea un número colombiano de 10 dígitos que inicia en 3. | El celular no tiene el formato esperado. | Rechazar la operación e informar el formato correcto. |
|  |  |  |  |  |  |  | P-USU-005 | Asegurar que no exista otro usuario con el mismo número celular. | Ya existe un usuario con ese celular. | Rechazar la operación e informar que el celular ya está registrado. |
|  |  |  |  |  |  |  | P-USU-021 | Al cambiar el celular, marcarlo como no verificado, enviar un nuevo código y dejar la cuenta en PENDIENTE_VERIFICACION. |  |  |
|  |  |  |  |  |  |  | P-USU-019 | Registrar en auditoría toda modificación de información personal con valor anterior y nuevo. |  |  |
| **Suspender cuenta** (`suspender()`) | Bloquea temporalmente la cuenta de un usuario del ámbito del actor. | actor | Usuario | Autoridad que suspende. | Usuario | Usuario suspendido. | P-USU-022 | Asegurar que el actor gestione al usuario según la matriz de gestión (su ámbito). | El usuario no pertenece al ámbito del actor. | Rechazar la operación e informar que no tiene permiso sobre ese usuario. |
|  |  | motivo | String | Razón de la suspensión. |  |  | P-USU-023 | Asegurar que solo se suspendan cuentas activas o pendientes, con motivo obligatorio. | La cuenta ya está suspendida o desactivada, o falta el motivo. | Rechazar la suspensión e informar el motivo. |
|  |  |  |  |  |  |  | P-USU-024 | Asegurar que nadie suspenda ni desactive su propia cuenta. | El actor es el mismo usuario. | Rechazar la operación. |
| **Reactivar cuenta** (`reactivar()`) | Levanta la suspensión de una cuenta. | actor | Usuario | Autoridad que reactiva. | Usuario | Usuario reactivado. | P-USU-022 | Asegurar que el actor gestione al usuario según la matriz de gestión (su ámbito). | El usuario no pertenece al ámbito del actor. | Rechazar la operación e informar que no tiene permiso sobre ese usuario. |
|  |  | motivo | String | Razón de la reactivación. |  |  | P-USU-025 | Asegurar que solo se reactiven cuentas SUSPENDIDA; vuelven a ACTIVA si cumplen las tres condiciones. | La cuenta no está suspendida. | Rechazar la reactivación e informar el estado. |
| **Desactivar cuenta** (`desactivar()`) | Cierra definitivamente la cuenta para su rol, conservando su historial. | actor | Usuario | Autoridad que desactiva. | Usuario | Usuario desactivado. | P-USU-022 | Asegurar que el actor gestione al usuario según la matriz de gestión (su ámbito). | El usuario no pertenece al ámbito del actor. | Rechazar la operación e informar que no tiene permiso sobre ese usuario. |
|  |  | motivo | String | Razón de la desactivación. |  |  | P-USU-024 | Asegurar que nadie suspenda ni desactive su propia cuenta. | El actor es el mismo usuario. | Rechazar la operación. |
|  |  |  |  |  |  |  | P-USU-026 | Al desactivar, cerrar la asignación de rol y los vínculos vigentes y conservar todo el historial. | La cuenta ya está desactivada o falta el motivo. | Rechazar la desactivación e informar el motivo. |
| **Decidir si tiene un rol vigente** (`tieneRolVigente()`) | Indica si el usuario ejerce hoy un rol dado; lo usan las políticas de autorización. | rol | Rol | Rol que se verifica. | boolean | Verdadero si su asignación vigente es de ese rol. | P-USU-029 | El rol vigente de un usuario es el de su única asignación sin fecha de fin; si no tiene, no tiene rol vigente. |  |  |
| **Consultar información personal** (`consultar()`) | Muestra los datos del usuario, su rol, sus vínculos y el estado de sus validaciones. | id | UUID | Identificador del usuario. | Usuario | Información del usuario. | P-USU-027 | Asegurar que la información personal la consulte el propio usuario o una autoridad de su ámbito. | El actor no tiene permiso sobre ese usuario. | Rechazar la consulta. |
|  |  |  |  |  |  |  | P-USU-028 | No exponer datos de otras personas; el código de verificación solo lo ve su dueño, en el aviso de demostración (D-10). |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Persona no registrada | Registrar usuario | Crea la cuenta de una persona que se autorregistra con su identidad institucional. | Usuario | Parámetro DOMINIO_CORREO | Dominio institucional que debe tener el correo. | P-USU-001 |  | Usuario registrado | EVT-001 / RN-213 |
|  |  |  |  |  |  | P-USU-002 |  |  |  |
|  |  |  |  |  |  | P-USU-003 |  |  |  |
|  |  |  |  |  |  | P-USU-004 |  |  |  |
|  |  |  |  |  |  | P-USU-005 |  |  |  |
|  |  |  |  |  |  | P-USU-006 |  |  |  |
|  |  |  |  |  |  | P-USU-007 |  |  |  |
|  |  |  |  |  |  | P-USU-008 |  |  |  |
| Administrador | Registrar Rector o Administrador | Un Administrador registra directamente a un Rector o a otro Administrador, que queda aprobado. | Usuario | Asignación de Rector vigente | Para garantizar un único Rector. | P-USU-001 |  | Usuario registrado y Rector asignado | EVT-001, EVT-004, EVT-015 |
|  |  |  |  |  |  | P-USU-002 |  |  |  |
|  |  |  |  |  |  | P-USU-003 |  |  |  |
|  |  |  |  |  |  | P-USU-004 |  |  |  |
|  |  |  |  |  |  | P-USU-005 |  |  |  |
|  |  |  |  |  |  | P-USU-006 |  |  |  |
|  |  |  |  |  |  | P-USU-009 |  |  |  |
|  |  |  |  |  |  | P-USU-010 |  |  |  |
|  |  |  |  |  |  | P-USU-011 |  |  |  |
| Usuario registrado | Verificar correo | Marca el correo como verificado con el código recibido. | Usuario | Código de verificación vigente | Código del canal CORREO. | P-USU-012 |  | Correo verificado | EVT-002 |
|  |  |  |  |  |  | P-USU-013 |  |  |  |
| Usuario registrado | Verificar celular | Marca el celular como verificado con el código recibido. | Usuario | Código de verificación vigente | Código del canal CELULAR. | P-USU-012 |  | Celular verificado | EVT-003 |
|  |  |  |  |  |  | P-USU-013 |  |  |  |
| Sistema (desde ServicioAprobacion) | Aprobar cuenta | Registra que la autoridad competente aprobó la cuenta. | Usuario |  |  | P-USU-014 |  | Usuario aprobado | EVT-004 / RN-214 |
|  |  |  |  |  |  | P-USU-013 |  |  |  |
| Sistema | Activar cuenta | Pasa la cuenta a ACTIVA si está aprobada y tiene correo y celular verificados. | Usuario |  |  | P-USU-015 |  | Cuenta activada | EVT-005 / RN-215 |
| Usuario registrado | Iniciar sesión | Decide si la persona autenticada con Google puede usar la plataforma. | Usuario |  |  | P-USU-016 | Google (autenticación) | Sesión iniciada |  |
|  |  |  |  |  |  | P-USU-017 |  |  |  |
| Usuario activo | Actualizar datos personales | Modifica los nombres y apellidos del propio usuario. | Usuario |  |  | P-USU-001 |  | Datos personales modificados | RN-109 |
|  |  |  |  |  |  | P-USU-018 |  |  |  |
|  |  |  |  |  |  | P-USU-019 |  |  |  |
| Usuario activo | Cambiar correo | Cambia el correo institucional y exige verificarlo de nuevo. | Usuario | Parámetro DOMINIO_CORREO | Dominio institucional. | P-USU-002 | Envío de correo (simulado) | Correo modificado | RN-083, RN-110 |
|  |  |  |  |  |  | P-USU-003 |  |  |  |
|  |  |  |  |  |  | P-USU-020 |  |  |  |
|  |  |  |  |  |  | P-USU-019 |  |  |  |
| Usuario activo | Cambiar celular | Cambia el número celular y exige verificarlo de nuevo. | Usuario |  |  | P-USU-004 | Envío de SMS (simulado) | Celular modificado | RN-083, RN-111 |
|  |  |  |  |  |  | P-USU-005 |  |  |  |
|  |  |  |  |  |  | P-USU-021 |  |  |  |
|  |  |  |  |  |  | P-USU-019 |  |  |  |
| Autoridad | Suspender cuenta | Bloquea temporalmente la cuenta de un usuario del ámbito del actor. | Usuario | Matriz de gestión | Qué autoridad gestiona a qué usuarios. | P-USU-022 |  | Cuenta suspendida | EVT-006 / RN-216 |
|  |  |  |  |  |  | P-USU-023 |  |  |  |
|  |  |  |  |  |  | P-USU-024 |  |  |  |
| Autoridad | Reactivar cuenta | Levanta la suspensión de una cuenta. | Usuario | Matriz de gestión | Qué autoridad gestiona a qué usuarios. | P-USU-022 |  | Cuenta activada | EVT-005 |
|  |  |  |  |  |  | P-USU-025 |  |  |  |
| Autoridad | Desactivar cuenta | Cierra definitivamente la cuenta para su rol, conservando su historial. | Usuario | Matriz de gestión | Qué autoridad gestiona a qué usuarios. | P-USU-022 |  | Cuenta desactivada | EVT-007 / RN-217 |
|  |  |  |  |  |  | P-USU-024 |  |  |  |
|  |  |  |  |  |  | P-USU-026 |  |  |  |
| Sistema (políticas de autorización) | Decidir si tiene un rol vigente | Indica si el usuario ejerce hoy un rol dado; lo usan las políticas de autorización. | Usuario |  |  | P-USU-029 |  | (decisión, sin evento) |  |
| Usuario activo | Consultar información personal | Muestra los datos del usuario, su rol, sus vínculos y el estado de sus validaciones. | Usuario |  |  | P-USU-027 |  | Información personal consultada | RN-101, RN-103 |
|  |  |  |  |  |  | P-USU-028 |  |  |  |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-USU-001 | Asegurar que los datos personales sean válidos: nombres y apellidos de 1 a 60 caracteres, sin espacios al inicio ni al final. | Usuario | RN-102 |
| P-USU-002 | Asegurar que el correo tenga formato válido y termine en el dominio institucional configurado. | ValidarCorreoInstitucionalRule | RN-069, RN-071, POL-001, D-12, P-PAR-002 |
| P-USU-003 | Asegurar que no exista otro usuario con el mismo correo institucional. | Usuario | RN-073, UQ-USU-1 |
| P-USU-004 | Asegurar que el celular sea un número colombiano de 10 dígitos que inicia en 3. | ValidarCelularRule | RN-070, RN-072, POL-002, D-12 |
| P-USU-005 | Asegurar que no exista otro usuario con el mismo número celular. | Usuario | RN-074, UQ-USU-2 |
| P-USU-006 | No guardar contraseñas: la identidad la confirma Google al ingresar. | Google + SeguridadConfiguracion | RNF-001, RNF-003, D-09 |
| P-USU-007 | Al registrarse, la cuenta queda en PENDIENTE_APROBACION con correo y celular sin verificar, y se envían los códigos de verificación. | Usuario | RN-084, D-17, P-CVE-001 |
| P-USU-008 | Asegurar que el identificador generado no esté siendo usado por otro usuario. | Usuario |  |
| P-USU-009 | Asegurar que solo un Administrador registre Rectores y Administradores. | AsegurarActorTieneRolRule | RN-094, RN-095, RN-129 |
| P-USU-010 | Asegurar que no exista otro Rector vigente al registrar un Rector. | AsignacionRol | INV-001, RN-005, P-ASR-002 |
| P-USU-011 | Al ser registrado por un Administrador, el usuario queda aprobado y en PENDIENTE_VERIFICACION. | Usuario | D-17 |
| P-USU-012 | Asegurar que el código digitado corresponda al código vigente del canal y del dato actual. | CodigoVerificacion | RN-077 a RN-080, P-CVE-005 a P-CVE-007 |
| P-USU-013 | Después de verificar un dato o de aprobar la cuenta, intentar activarla. | Usuario | RN-085 a RN-088, P-USU-015 |
| P-USU-014 | Asegurar que solo se apruebe una cuenta pendiente de aprobación. | Usuario | RN-084 |
| P-USU-015 | La cuenta pasa a ACTIVA solo si está aprobada y tiene correo y celular verificados. | Usuario | RN-085 a RN-088, POL-003 |
| P-USU-016 | Asegurar que solo usen la aplicación correos registrados: si el correo de Google no existe, se muestra el registro. | AsegurarUsuarioRegistradoRule | RNF-004, D-09 |
| P-USU-017 | Asegurar que solo ingresen cuentas ACTIVA e indicar qué le falta a las demás. | Usuario | RN-089, POL-003 |
| P-USU-018 | Asegurar que el usuario no pueda modificar su propio rol. | Usuario | RN-107 |
| P-USU-019 | Registrar en auditoría toda modificación de información personal con valor anterior y nuevo. | Auditoria | RN-083, RN-097, RN-109, P-AUD-001 |
| P-USU-020 | Al cambiar el correo, marcarlo como no verificado, enviar un nuevo código y dejar la cuenta en PENDIENTE_VERIFICACION. | Usuario | RN-081, RN-110, D-17 |
| P-USU-021 | Al cambiar el celular, marcarlo como no verificado, enviar un nuevo código y dejar la cuenta en PENDIENTE_VERIFICACION. | Usuario | RN-082, RN-111, D-17 |
| P-USU-022 | Asegurar que el actor gestione al usuario según la matriz de gestión (su ámbito). | AsignacionRol (ámbito) | POL-004, POL-005, RN-112 a RN-129 |
| P-USU-023 | Asegurar que solo se suspendan cuentas activas o pendientes, con motivo obligatorio. | Usuario | RN-216, D-07 |
| P-USU-024 | Asegurar que nadie suspenda ni desactive su propia cuenta. | Usuario | RN-108 (por analogía), POL-005 |
| P-USU-025 | Asegurar que solo se reactiven cuentas SUSPENDIDA; vuelven a ACTIVA si cumplen las tres condiciones. | Usuario | D-07, P-USU-015 |
| P-USU-026 | Al desactivar, cerrar la asignación de rol y los vínculos vigentes y conservar todo el historial. | Usuario | RN-099, RN-217, D-07, D-28 |
| P-USU-027 | Asegurar que la información personal la consulte el propio usuario o una autoridad de su ámbito. | AsignacionRol (ámbito) | RN-101, POL-004 |
| P-USU-028 | No exponer datos de otras personas; el código de verificación solo lo ve su dueño, en el aviso de demostración (D-10). | Usuario | RNF-003 |
| P-USU-029 | El rol vigente de un usuario es el de su única asignación sin fecha de fin; si no tiene, no tiene rol vigente. | AsignacionRol | RN-001, INV-005, D-13 |

## En el código

| Capa | Clase |
|---|---|
| Dominio (atributos y comportamiento) | `UsuarioDominio` (`co.edu.uco.kudos.dominio`) |
| Entidad (tabla) | `UsuarioEntidad` |
| DAO | `UsuarioDAO` + implementación SQL Server |
| DTO (API) | UsuarioDto, CuentaDto, RegistroDto, AutoridadDto, PersonaDto |
| Negocio | RegistroNegocioImpl, VerificacionNegocioImpl, CuentaNegocioImpl, AutenticacionNegocioImpl, AutoridadNegocioImpl, PersonaNegocioImpl |
