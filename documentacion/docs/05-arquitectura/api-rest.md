# API REST

[Inicio](../../README.md) · [Arquitectura](README.md) · [Patrones](patrones.md) · [Casos de uso](../00-analisis/casos-de-uso.md)

Lista **completa** de los 64 endpoints del back-end, tomada de los controladores (`co.edu.uco.kudos.controlador.impl`). Todos empiezan por `/api/v1`, reciben y devuelven JSON y exigen **sesión**: sin ella responden **401**. Salvo los de autenticación, registro, verificación y cuenta (`/cuenta/**`), también exigen cuenta **ACTIVA** (el controlador obtiene al actor con `obtenerIdActor`).

## Convenciones

| Tema | Regla |
|---|---|
| Ingreso | `GET /oauth2/authorization/google` (lo maneja Spring Security). Al volver, la cookie `JSESSIONID` queda en el navegador. |
| Salida | `POST /api/v1/autenticacion/salir` (Spring Security) responde 200 y cierra la sesión. |
| Respuesta | Siempre `Respuesta`: `correlationId`, `timestamp`, `datos` (lista), `mensajes` y `estado` (`OK` o `ERROR`). |
| Errores | 400 regla de negocio incumplida (el mensaje dice qué hacer) · 401 sin sesión · 403 sin permiso por rol o ámbito · 404 recurso inexistente · 500 error inesperado (mensaje genérico; el detalle va al log). |
| Verbos | `GET` consulta · `POST` crea o ejecuta una acción (aprobación, veto, lectura…) · `PUT` modifica o reemplaza (calificar es `PUT` porque crea o reemplaza **mi** calificación). |
| Pruebas | Colección de Bruno en `bruno/Kudos` (16 carpetas, una por módulo), con la variable secreta `sesion` (cookie `JSESSIONID`). |

## Autenticación, registro y cuenta

| Método | Ruta | Qué hace | Cuerpo | Parámetros | Caso de uso | Quién | Controlador |
|---|---|---|---|---|---|---|---|
| `GET` | `/autenticacion` | Usuario de la sesión: si está registrado, estado de la cuenta y rol vigente | — | — | CU-018 | Cualquier persona con sesión de Google | `AutenticacionControlador` |
| `GET` | `/cuenta` | Mi cuenta y el estado de sus validaciones | — | — | CU-020 | Usuario registrado | `CuentaControlador` |
| `PUT` | `/cuenta/datos-personales` | Cambiar nombres y apellidos | `CuentaDto` | — | CU-020 | Usuario registrado | `CuentaControlador` |
| `PUT` | `/cuenta/celular` | Cambiar el celular (queda por verificar) | `CuentaDto` | — | CU-020 | Usuario registrado | `CuentaControlador` |
| `PUT` | `/cuenta/correo` | Cambiar el correo (se verifica al ingresar con Google) | `CuentaDto` | — | CU-020 | Usuario registrado | `CuentaControlador` |
| `GET` | `/registros/opciones` | Facultades y Programas disponibles para el registro | — | — | CU-016 | Persona con sesión de Google | `RegistroControlador` |
| `POST` | `/registros` | Registrarse: crea la cuenta y la solicitud | `RegistroDto` | — | CU-016 | Persona con sesión de Google no registrada | `RegistroControlador` |
| `POST` | `/registros/nueva-solicitud` | Enviar una nueva solicitud tras un rechazo | `RegistroDto` | — | CU-016 | Usuario con solicitud rechazada | `RegistroControlador` |
| `POST` | `/verificaciones/celular` | Verificar el celular con el código | `CodigoVerificacionDto` | — | CU-017 | Usuario registrado | `VerificacionControlador` |
| `POST` | `/verificaciones/celular/reenvio` | Pedir un nuevo código (invalida los anteriores) | — | — | CU-017 | Usuario registrado | `VerificacionControlador` |

## Aprobaciones y autoridades

| Método | Ruta | Qué hace | Cuerpo | Parámetros | Caso de uso | Quién | Controlador |
|---|---|---|---|---|---|---|---|
| `POST` | `/autoridades` | Registrar un Rector o un Administrador | `AutoridadDto` | — | CU-024, CU-025 | Administrador | `AutoridadControlador` |
| `GET` | `/solicitudes/pendientes` | Solicitudes pendientes de mi ámbito | — | — | CU-022 | Autoridad aprobadora | `SolicitudControlador` |
| `POST` | `/solicitudes/{id}/aprobacion` | Aprobar una solicitud | — | — | CU-022 | Autoridad competente | `SolicitudControlador` |
| `POST` | `/solicitudes/{id}/rechazo` | Rechazar una solicitud con motivo | `RechazoDto` | — | CU-023 | Autoridad competente | `SolicitudControlador` |

## Estructura organizacional

| Método | Ruta | Qué hace | Cuerpo | Parámetros | Caso de uso | Quién | Controlador |
|---|---|---|---|---|---|---|---|
| `GET` | `/facultades` | Consultar Facultades según el ámbito | — | — | CU-034 | Autoridades | `FacultadControlador` |
| `POST` | `/facultades` | Crear una Facultad | `FacultadDto` | — | CU-028 | Coord. Académico | `FacultadControlador` |
| `PUT` | `/facultades/{id}` | Renombrar una Facultad | `FacultadDto` | — | CU-029 | Coord. Académico | `FacultadControlador` |
| `POST` | `/facultades/{id}/inactivacion` | Inactivar una Facultad sin Programas activos | — | — | CU-029 | Coord. Académico | `FacultadControlador` |
| `GET` | `/facultades/{id}/coordinadores` | Coordinador vigente e historial | — | — | CU-034 | Autoridades (según ámbito) | `FacultadControlador` |
| `GET` | `/programas` | Consultar Programas según el ámbito | — | — | CU-034 | Autoridades | `ProgramaControlador` |
| `GET` | `/programas/{id}` | Detalle de un Programa | — | — | CU-034 | Autoridades (según ámbito) | `ProgramaControlador` |
| `POST` | `/programas` | Crear un Programa (activa su Facultad si es el primero) | `ProgramaAcademicoDto` | — | CU-030 | Coord. Académico | `ProgramaControlador` |
| `PUT` | `/programas/{id}` | Renombrar un Programa | `ProgramaAcademicoDto` | — | CU-031 | Coord. Académico | `ProgramaControlador` |
| `POST` | `/programas/{id}/inactivacion` | Inactivar un Programa sin vínculos vigentes | — | — | CU-031 | Coord. Académico | `ProgramaControlador` |
| `GET` | `/programas/{id}/coordinadores` | Coordinador vigente e historial | — | — | CU-034 | Autoridades (según ámbito) | `ProgramaControlador` |
| `GET` | `/programas/{id}/vinculos` | Docentes y estudiantes del Programa | — | — | CU-034 | Coordinadores (según ámbito) | `ProgramaControlador` |
| `POST` | `/programas/{id}/vinculos` | Vincular a un docente o estudiante registrado | `VinculoDto` | — | CU-032 | Coord. de ese Programa | `ProgramaControlador` |
| `POST` | `/vinculos/{id}/desvinculacion` | Desvincular con motivo (suspende si queda sin vínculos) | `DesvinculacionDto` | — | CU-033 | Coord. de ese Programa | `VinculacionControlador` |
| `GET` | `/usuarios/{id}/vinculos` | Programas vigentes e históricos de una persona | — | — | CU-034 | Autoridades (según ámbito) | `VinculacionControlador` |
| `GET` | `/cuenta/vinculos` | Mis Programas | — | — | CU-034 | Usuario registrado | `VinculacionControlador` |
| `GET` | `/estructura` | Cargos vigentes: Rector, Coord. Académico y coordinadores | — | — | CU-034 | Usuario activo | `VinculacionControlador` |

## Categorías

| Método | Ruta | Qué hace | Cuerpo | Parámetros | Caso de uso | Quién | Controlador |
|---|---|---|---|---|---|---|---|
| `GET` | `/categorias` | Categorías activas | — | — | CU-035 | Usuario activo | `CategoriaControlador` |
| `GET` | `/categorias/todas` | Todas las categorías, activas e inactivas | — | — | CU-035 | Gestor de categorías | `CategoriaControlador` |
| `POST` | `/categorias` | Crear una categoría | `CategoriaDto` | — | CU-035 | Gestor de categorías | `CategoriaControlador` |
| `PUT` | `/categorias/{id}` | Modificar una categoría | `CategoriaDto` | — | CU-035 | Gestor de categorías | `CategoriaControlador` |
| `POST` | `/categorias/{id}/activacion` | Activar una categoría | — | — | CU-035 | Gestor de categorías | `CategoriaControlador` |
| `POST` | `/categorias/{id}/desactivacion` | Desactivar una categoría | — | — | CU-035 | Gestor de categorías | `CategoriaControlador` |

## Kudos

| Método | Ruta | Qué hace | Cuerpo | Parámetros | Caso de uso | Quién | Controlador |
|---|---|---|---|---|---|---|---|
| `POST` | `/kudos` | Crear y publicar un Kudo | `KudoDto` | — | CU-001 | Usuario activo | `KudoControlador` |
| `GET` | `/kudos` | Muro paginado (10 por página) | — | `categoria`, `persona`, `pagina` | CU-042 | Usuario activo | `KudoControlador` |
| `GET` | `/kudos/{id}` | Detalle del Kudo (suma una visualización si no es el autor) | — | — | CU-002 | Usuario activo | `KudoControlador` |
| `GET` | `/usuarios/{id}/kudos` | Historial de una persona | — | `tipo` = `recibidos` o `emitidos` | CU-003 | Usuario activo | `KudoControlador` |
| `GET` | `/personas` | Buscar personas activas (máximo 20) | — | `texto` | CU-001 | Usuario activo | `KudoControlador` |

## Conversación y calificaciones

| Método | Ruta | Qué hace | Cuerpo | Parámetros | Caso de uso | Quién | Controlador |
|---|---|---|---|---|---|---|---|
| `PUT` | `/kudos/{id}/calificacion` | Calificar o modificar mi calificación de un Kudo (1 a 5) | `CalificacionDto` | — | CU-007 | Usuario activo | `CalificacionControlador` |
| `PUT` | `/replicas/{id}/calificacion` | Calificar o modificar mi calificación de una réplica | `CalificacionDto` | — | CU-008 | Usuario activo | `CalificacionControlador` |
| `GET` | `/kudos/{id}/valoraciones` | Promedio, cantidad, distribución y mi calificación | — | — | CU-009 | Usuario activo | `CalificacionControlador` |
| `GET` | `/replicas/{id}/valoraciones` | Ídem para una réplica | — | — | CU-009 | Usuario activo | `CalificacionControlador` |
| `POST` | `/kudos/{id}/replicas` | Responder un Kudo | `ReplicaDto` | — | CU-004 | Usuario activo | `ReplicaControlador` |
| `POST` | `/replicas/{id}/respuestas` | Responder una réplica | `ReplicaDto` | — | CU-005 | Usuario activo | `ReplicaControlador` |
| `GET` | `/kudos/{id}/conversacion` | Conversación completa como árbol | — | — | CU-006 | Usuario activo | `ReplicaControlador` |

## Reportes y moderación

| Método | Ruta | Qué hace | Cuerpo | Parámetros | Caso de uso | Quién | Controlador |
|---|---|---|---|---|---|---|---|
| `GET` | `/moderacion/reportes` | Bandeja: reportes abiertos agrupados por contenido | — | — | CU-012 | Moderador | `ModeracionControlador` |
| `GET` | `/moderacion/vetos` | Histórico de vetos con el texto original | — | — | CU-015 | Moderador | `ModeracionControlador` |
| `POST` | `/moderacion/kudos/{id}/revision` | Iniciar la revisión de un Kudo reportado | — | — | CU-036 | Moderador | `ModeracionControlador` |
| `POST` | `/moderacion/replicas/{id}/revision` | Iniciar la revisión de una réplica reportada | — | — | CU-036 | Moderador | `ModeracionControlador` |
| `POST` | `/moderacion/kudos/{id}/veto` | Vetar un Kudo (con o sin revisión previa) | `DecisionDto` | — | CU-013 | Moderador | `ModeracionControlador` |
| `POST` | `/moderacion/replicas/{id}/veto` | Vetar una réplica | `DecisionDto` | — | CU-014 | Moderador | `ModeracionControlador` |
| `POST` | `/moderacion/kudos/{id}/liberacion` | Liberar un Kudo en revisión | `DecisionDto` | — | CU-037 | Moderador | `ModeracionControlador` |
| `POST` | `/moderacion/replicas/{id}/liberacion` | Liberar una réplica en revisión | `DecisionDto` | — | CU-037 | Moderador | `ModeracionControlador` |
| `POST` | `/kudos/{id}/reportes` | Reportar un Kudo con motivo | `ReporteDto` | — | CU-010 | Usuario activo | `ReporteControlador` |
| `POST` | `/replicas/{id}/reportes` | Reportar una réplica con motivo | `ReporteDto` | — | CU-011 | Usuario activo | `ReporteControlador` |
| `GET` | `/reportes/mios` | Mis reportes y su estado | — | — | CU-012 | Usuario activo | `ReporteControlador` |

## Auditoría y notificaciones

| Método | Ruta | Qué hace | Cuerpo | Parámetros | Caso de uso | Quién | Controlador |
|---|---|---|---|---|---|---|---|
| `GET` | `/auditoria` | Auditoría filtrada y paginada (20 por página) según el ámbito | — | `desde`, `hasta` (aaaa-mm-dd), `actor`, `accion`, `tipoObjeto`, `pagina` | CU-038 | Autoridades | `AuditoriaControlador` |
| `GET` | `/notificaciones` | Mis notificaciones, primero las no leídas | — | — | CU-039 | Usuario activo | `NotificacionControlador` |
| `POST` | `/notificaciones/{id}/lectura` | Marcar una notificación como leída | — | — | CU-039 | Destinatario | `NotificacionControlador` |
| `POST` | `/notificaciones/lectura` | Marcar todas como leídas | — | — | CU-039 | Usuario activo | `NotificacionControlador` |

## Fuera del alcance de esta versión

No hay endpoints para el historial de actividad propia (RF-082), suspender, reactivar o desactivar cuentas a mano (RF-089), indicadores (RF-096) ni parámetros globales (RF-097). Ver los [requisitos](../00-analisis/requisitos.md).
