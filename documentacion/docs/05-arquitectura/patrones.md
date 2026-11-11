# Patrones utilizados

[Inicio](../../README.md) · [Arquitectura](README.md) · [API REST](api-rest.md) · [Diagrama de clases](../02-diagrama-clases/README.md)

Sección 13 del contenido mínimo. Aquí están **solo los patrones que existen en el código**, cada uno con el problema concreto que resuelve, dónde está y por qué se eligió (ARQ-017 a ARQ-021: "los patrones deben justificarse; no se aceptan por moda"). Al final están los patrones que se **evaluaron y descartaron**, con la razón.

| # | Patrón | Problema que resuelve | Dónde está |
|---|---|---|---|
| 1 | State | Lo que se puede hacer con un Kudo o una réplica depende de su estado | `EstadoContenido` |
| 2 | Builder | Objetos con muchos atributos, valores por defecto y limpieza de datos | `XxxDominio.Builder`, `XxxEntidad.Builder` |
| 3 | Singleton | Reglas y ensambladores sin estado: basta una instancia | `XxxRule.obtenerInstancia()`, `XxxAssembler.getInstance()`, `UtilTexto.getUtilTexto()` |
| 4 | Abstract Factory | Varios DAO deben compartir la misma conexión y transacción | `DAOFactory` → `SqlServerDAOFactory` |
| 5 | Facade | Un punto de entrada simple por caso de uso que maneja la transacción | `XxxFachada` + `XxxFachadaImpl` |
| 6 | DAO | Aislar el SQL del negocio | `XxxDAO` + `XxxSqlServerDAO` |
| 7 | DTO + Assembler | Separar los datos de la API, el dominio y la tabla | `XxxDto`, `XxxEntidad`, `DTOAssembler`, `EntidadAssembler` |
| 8 | Interfaz con implementación intercambiable | Enviar códigos sin depender de un proveedor real | `EnviadorNotificacion` → `EnviadorNotificacionSimulado` |
| 9 | Respuesta uniforme | Que toda respuesta de la API tenga la misma forma | `Respuesta`, `RespuestaOk`, `RespuestaError` |
| 10 | Experto en información (GRASP) | Que el objeto que tiene los datos responda la pregunta | `asignacion.puedeDecidir(...)`, `usuario.intentarActivar()`, `codigo.estaVencido(...)` |

---

## 1. State

**Problema.** Un Kudo o una réplica puede estar CREADO, VISIBLE, REPORTADO, EN_REVISION o VETADO, y cada estado permite operaciones distintas: un contenido VETADO no se puede reportar ni volver a vetar, uno VISIBLE no se puede liberar. Con `if` y `switch` repartidos por el negocio, cada regla nueva obligaría a tocar muchos lugares.

**Solución.** Cada constante de la enumeración `EstadoContenido` sabe qué transiciones permite. Por defecto toda transición lanza una excepción; cada estado sobrescribe solo las que permite. `KudoDominio` y `ReplicaDominio` delegan sin preguntar el estado (Tell, Don't Ask).

```java
public enum EstadoContenido {
    VISIBLE {
        @Override public EstadoContenido reportar() { return REPORTADO; }
        @Override public EstadoContenido vetar() { return VETADO; }
    },
    VETADO {
        @Override public boolean admiteInteraccion() { return false; }
    };
    public EstadoContenido vetar() { throw cambioNoPermitido("vetar"); }
}

public void vetar() {
    estado = estado.vetar();
}
```

**Dónde.** `dominio/EstadoContenido.java`, `KudoDominio`, `ReplicaDominio`. Diagrama: [estado de Kudo y Réplica](../06-diagramas-comportamiento/README.md#estado-de-kudo-y-réplica). Pruebas: `KudoDominioTest`.

**Por qué.** Agregar un estado o cambiar una transición se hace en un solo lugar (OCP), y una transición inválida es imposible porque el estado la rechaza.

## 2. Builder

**Problema.** Los objetos de dominio y las entidades tienen muchos atributos, varios opcionales, y necesitan valores por defecto seguros (nunca `null`) y textos limpios.

**Solución.** Cada `XxxDominio` y `XxxEntidad` tiene una clase interna `Builder`, igual que en Sigma: el constructor del Builder asigna valores por defecto (`UtilId.VALOR_DEFECTO`, `UtilTexto.VACIO`, `UtilFecha.FECHA_HORA_POR_DEFECTO`), cada método limpia el valor recibido y `build()` usa el constructor privado.

```java
var kudo = new KudoDominio.Builder()
        .id(UtilId.generarId())
        .autor(autor)
        .contenido(contenido)
        .estado(EstadoContenido.CREADO)
        .build();
```

**Por qué.** No existen objetos a medio construir y los nulos no llegan al negocio. Es el estilo del curso.

## 3. Singleton

**Problema.** Las reglas (`XxxRule`), los ensambladores (`XxxAssembler`) y `UtilTexto` no guardan estado: crear una instancia nueva en cada uso no aporta nada.

**Solución.** Constructor privado, una constante `INSTANCIA` y un método de acceso, como en Sigma.

```java
public final class ValidarEstrellasRule implements Rule<Integer> {
    private static final ValidarEstrellasRule INSTANCIA = new ValidarEstrellasRule();
    private ValidarEstrellasRule() { }
    public static ValidarEstrellasRule obtenerInstancia() { return INSTANCIA; }
    @Override public void ejecutar(Integer... datos) { ... }
}
```

**Dónde.** 64 reglas en `negocio/negocio/reglas/impl/<módulo>/`, los ensambladores en `negocio/fachada/assembler/impl` y `negocio/negocio/assembler/impl`, y `UtilTexto`.

**Por qué.** `obtenerInstancia()` devuelve el **tipo concreto** de la regla (no `Rule<Object>`): así queda claro qué regla se ejecuta y con qué datos. Esto se corrigió por un hallazgo de Sonar ([calidad](../07-calidad-y-pruebas/README.md)).

## 4. Abstract Factory (`DAOFactory`)

**Problema.** Un caso de uso usa varios DAO (por ejemplo, vetar cambia el Kudo, la moderación, los reportes, la notificación y la auditoría) y todos deben trabajar sobre **la misma conexión** para que la transacción sea una sola.

**Solución.** `DAOFactory` es una clase abstracta que abre la conexión, maneja la transacción y entrega cada DAO. `SqlServerDAOFactory` es la implementación real; en las pruebas, `DAOFactoryFalsa` entrega DAO en memoria.

```java
var daoFactory = DAOFactory.obtenerFactoria();
daoFactory.iniciarTransaccion();
daoFactory.obtenerKudoDAO().actualizar(id, entidad);
daoFactory.obtenerReporteDAO().actualizar(idReporte, reporte);
daoFactory.confirmarTransaccion();
```

**Por qué.** Cambiar de motor de base de datos es otra fábrica (ARQ-077), y el negocio se prueba sin base de datos.

## 5. Facade

**Problema.** El controlador no debería conocer transacciones, ensambladores ni la creación del negocio.

**Solución.** Cada módulo tiene `XxxFachada` (interfaz) y `XxxFachadaImpl`: obtiene la `DAOFactory`, abre la transacción, convierte el DTO, llama al negocio y confirma o cancela. En `ModeracionFachadaImpl` y `NotificacionFachadaImpl` ese bloque está una sola vez en `ejecutarEnTransaccion(Consumer<XxxNegocio>)`.

**Por qué.** El controlador queda en una línea por operación y la transacción tiene un solo lugar (un caso de uso = una unidad de trabajo).

## 6. DAO

**Problema.** El SQL no debe mezclarse con las reglas.

**Solución.** Interfaces pequeñas (`CrearDAO`, `ConsultarDAO`, `ActualizarDAO`, `ConsultarActivosDAO`) que cada `XxxDAO` combina según lo que necesita, e implementaciones `XxxSqlServerDAO` con `PreparedStatement`. `AuditoriaDAO` no tiene `actualizar` y ningún DAO tiene borrar (D-28).

**Por qué.** El negocio habla en objetos; el SQL vive en un solo lugar y no hay inyección SQL.

## 7. DTO + Assembler

**Problema.** La API, el dominio y la tabla necesitan formas distintas de los mismos datos: la API no debe exponer el dominio ni la tabla, y la tabla guarda ids donde el dominio tiene objetos.

**Solución.** `XxxDto` (API), `XxxDominio` (comportamiento) y `XxxEntidad` (tabla), con dos familias de ensambladores: `DTOAssembler` (fachada) y `EntidadAssembler` (negocio). Ejemplos con lógica propia: `KudoDtoAssembler` oculta el texto de un Kudo vetado; `ConversacionDtoAssembler.armarArbol` arma el árbol de réplicas; `ContenidoReportadoDtoAssembler.agrupar` agrupa los reportes por contenido.

**Por qué.** Cada capa cambia sin romper a las otras, y lo que sale por la API se decide en un solo lugar (por ejemplo, que el texto vetado no salga del servidor).

## 8. Interfaz con implementación intercambiable (inversión de dependencias)

**Problema.** No hay un proveedor real de SMS, pero el diseño no debe depender de eso (D-10).

**Solución.** `VerificacionNegocioImpl` depende de la interfaz `EnviadorNotificacion`; la implementación `EnviadorNotificacionSimulado` deja el código como notificación y en el log, y la pantalla lo muestra en un aviso de demostración.

**Por qué.** Pasar a un proveedor real es escribir otra implementación, sin tocar el negocio ni el dominio (DIP).

## 9. Respuesta uniforme

**Problema.** El front-end necesita leer igual cualquier respuesta, buena o mala.

**Solución.** `Respuesta` con `correlationId`, `timestamp`, `datos`, `mensajes` y `estado`; `RespuestaOk` y `RespuestaError` la especializan. `AbstractRestController` convierte toda excepción en una `RespuestaError` con el código HTTP correcto, y `src/api/http.js` en el front la interpreta en un solo lugar.

## 10. Experto en información (GRASP)

**Problema.** Decidir si una autoridad puede aprobar una solicitud requiere su rol y su ámbito (Facultad o Programa). Si lo decide el negocio con `if`, la lógica se repite en cada módulo.

**Solución.** El objeto que tiene los datos responde: `AsignacionRolDominio.puedeDecidir(solicitud)`, `puedeVerFacultad(...)`, `puedeVerPrograma(...)`, `coordinaPrograma(...)`; `UsuarioDominio.intentarActivar()` decide si la cuenta pasa a ACTIVA; `CodigoVerificacionDominio.estaVencido(ahora)` e `intentosAgotados(...)`.

---

## Técnica en el front-end: componente recursivo

La conversación es un árbol de profundidad ilimitada. `ReplicaNodo.vue` se dibuja a sí mismo para cada respuesta (`<ReplicaNodo v-for="hija in replica.respuestas" …/>`) y la sangría crece hasta el nivel 5. No es un patrón de diseño de clases, sino la forma natural de recorrer un árbol en Vue.

---

## Patrones evaluados y descartados

| Patrón | Para qué se pensó | Por qué no se usó |
|---|---|---|
| **Observer** | Que la auditoría y las notificaciones reaccionaran a eventos publicados por el negocio | Registrar desde el negocio (`auditoriaNegocio.registrar`) es más simple, explícito y queda en la misma transacción. Observer agregaba publicador, suscriptores y clases de eventos sin aportar en este alcance (D-26). |
| **Composite** | Una interfaz común `ContenidoConversable` para Kudo y Réplica | El árbol se arma con `idReplicaPadre` en `ConversacionDtoAssembler` y se dibuja con un componente recursivo; no hacía falta una jerarquía de clases (D-22). |
| **Objeto de valor** (`CorreoInstitucional`, `NumeroCelular`, `Estrellas`) | Que el correo, el celular y las estrellas se validaran al construirse | Las validaciones de formato quedaron en clases `Rule`, como en Sigma, para mantener un solo estilo de validación (D-12, D-23). |
| **Specification** (`PoliticaAmbito`…) | Expresar los permisos por ámbito como objetos combinables | Bastó con `AsegurarActorTieneRolRule` y los métodos de `AsignacionRol` (experto en información). |
| **Servicio de dominio** como capa aparte | Coordinar operaciones que tocan varios objetos | Ese papel lo cumplen los `XxxNegocioImpl` (por ejemplo `SolicitudNegocioImpl` y `ModeracionNegocioImpl`), que ya existen en la arquitectura del curso. |
| **Repository** | Colecciones de objetos de dominio persistentes | El curso usa DAO; agregar otra capa duplicaría responsabilidades. |
| **Command** | Encolar o deshacer operaciones | No hay operaciones que deshacer ni encolar. |
| **Chain of Responsibility** | Encadenar las reglas | Las reglas se ejecutan en un orden fijo y todas deben cumplirse; el negocio las llama una tras otra. |
| **Herencia para los roles** (`Rector extends Usuario`…) | Modelar cada rol como subclase | Los roles cambian en el tiempo y un objeto no puede cambiar de clase: se usa composición con `AsignacionRol` (ver el [modelo de dominio](../01-modelo-dominio/README.md#por-qué-rector-docente-estudiante-no-son-clases)). |
