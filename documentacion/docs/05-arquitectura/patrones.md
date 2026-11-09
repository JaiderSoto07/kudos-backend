# Patrones utilizados

[Inicio](../../README.md) · [Arquitectura](README.md) · [Diagrama de clases](../02-diagrama-clases/README.md) · [Diagramas de comportamiento](../06-diagramas-comportamiento/README.md)

El enunciado no evalúa la cantidad de patrones, sino que cada uno **resuelva un problema identificado** y esté justificado (ARQ-058 a ARQ-061). Por eso cada patrón se presenta con el problema concreto de UCO Kudos que lo motivó y con la alternativa que se descartó. Al final están los patrones evaluados que **no** se usaron, y por qué.

| # | Patrón | Problema que resuelve | Dónde |
|---|---|---|---|
| 1 | State | El ciclo de vida del contenido tiene reglas distintas en cada estado | `EstadoContenido` en `Kudo` y `Replica` |
| 2 | Composite | La conversación es un árbol de profundidad ilimitada | `Conversacion`, `NodoConversacion`, `ContenidoConversable` |
| 3 | Observer | Cada operación debe auditarse y notificarse sin acoplar el negocio | `PublicadorEventos` y sus suscriptores |
| 4 | Specification | Las reglas de autorización por rol y ámbito se repiten en muchos casos de uso | `PoliticaAmbito`, `PoliticaModeracion`, `PoliticaGestionCategorias` |
| 5 | Builder | Construir objetos con muchos atributos, valores por defecto y saneamiento | Clases de dominio, entidad y DTO |
| 6 | Abstract Factory | Obtener una familia de DAO que comparten conexión y transacción | `DAOFactory` → `SqlServerDAOFactory` |
| 7 | Facade | Ofrecer un punto de entrada simple por caso de uso y controlar la transacción | `XxxFachada` |
| 8 | DAO + Assembler | Separar el dominio de las tablas y de la API | `XxxDAO`, `DTOAssembler`, `EntidadAssembler` |
| 9 | Objeto de valor | Reglas de formato que se repetirían en muchos lugares | `CorreoInstitucional`, `NumeroCelular`, `ContenidoTexto`, `Estrellas` |
| 10 | Servicio de dominio | Operaciones que coordinan varios agregados | `ServicioAprobacion`, `ServicioModeracion` |
| 11 | Puerto y adaptador (Strategy) | Enviar códigos sin depender de un proveedor de correo o SMS | `EnviadorNotificacion` → `EnviadorNotificacionSimulado` |
| 12 | Singleton (reglas y ensambladores) | Las reglas no tienen estado y se usan en todas partes | `XxxRule.obtenerInstancia()`, `XxxAssembler.getInstance()` |

---

## 1. State

**Problema.** Un Kudo o una réplica pasa por CREADO → VISIBLE → REPORTADO → EN_REVISION → VETADO (RN-201 a RN-209, D-20). En cada estado cambia lo que se puede hacer: solo un contenido REPORTADO entra en revisión, solo uno EN_REVISION se libera, uno VETADO no admite réplicas, calificaciones ni reportes. Escrito con `if` y `switch`, esas reglas quedarían repartidas por el negocio y se repetirían para el Kudo y para la réplica.

**Solución.** Cada estado es un objeto que sabe qué transiciones permite. En Java se implementa con un `enum` cuyas constantes sobrescriben el comportamiento:

```java
public enum EstadoContenido {
    CREADO      { public EstadoContenido publicar() { return VISIBLE; } },
    VISIBLE     { public EstadoContenido reportar() { return REPORTADO; }
                  public EstadoContenido vetar()    { return VETADO; } },
    REPORTADO   { public EstadoContenido reportar() { return REPORTADO; }
                  public EstadoContenido iniciarRevision() { return EN_REVISION; }
                  public EstadoContenido vetar()    { return VETADO; } },
    EN_REVISION { public EstadoContenido liberar()  { return VISIBLE; }
                  public EstadoContenido vetar()    { return VETADO; } },
    VETADO      { public boolean admiteInteraccion() { return false; } };

    // Por defecto la transición no está permitida
    public EstadoContenido publicar()        { throw transicionInvalida("publicar"); }
    public EstadoContenido reportar()        { throw transicionInvalida("reportar"); }
    public EstadoContenido iniciarRevision() { throw transicionInvalida("iniciar revisión"); }
    public EstadoContenido liberar()         { throw transicionInvalida("liberar"); }
    public EstadoContenido vetar()           { throw transicionInvalida("vetar"); }
    public boolean admiteInteraccion()       { return true; }
}

// En Kudo (y en Replica), Tell, Don't Ask:
public void vetar() { this.estado = estado.vetar(); }
```

**Participantes.** `EstadoContenido` (los estados), `Kudo` y `Replica` (el contexto que delega).

**Beneficio.** Agregar un estado o cambiar una transición se hace en un solo lugar (OCP). `Kudo` y `Replica` comparten las mismas reglas sin duplicarlas (DRY). El [diagrama de estado](../06-diagramas-comportamiento/README.md#estado-de-kudo-y-réplica) es exactamente este `enum`.

**Alternativa descartada.** Una interfaz `EstadoContenido` con cinco clases. Es equivalente, pero tiene más archivos sin ganar nada, porque los estados no guardan datos propios (KISS).

---

## 2. Composite

**Problema.** Una réplica responde a un Kudo o a otra réplica, sin límite de profundidad (RN-162 a RN-164, INV-026 a INV-029). Mostrar la conversación (CU-006) exige recorrer un árbol en el que cada nodo se trata igual sin importar su nivel.

**Solución.** Un `NodoConversacion` contiene un contenido y una lista de nodos hijos. La `Conversacion` arma el árbol a partir de las réplicas de un Kudo, ya leídas con una sola consulta, y lo recorre recursivamente. `Kudo` y `Replica` implementan la interfaz común `ContenidoConversable`: `admiteInteraccion()`, `getAutor()` y `actualizarResumenCalificaciones()`.

```java
public final class NodoConversacion {
    private final Replica replica;
    private final List<NodoConversacion> respuestas = new ArrayList<>();

    public void agregarRespuesta(NodoConversacion hijo) { respuestas.add(hijo); }
    public int contarRespuestas() {               // recursivo: el mismo trato en todos los niveles
        return respuestas.stream().mapToInt(h -> 1 + h.contarRespuestas()).sum();
    }
}
```

**Participantes.** `Conversacion` (raíz: el Kudo), `NodoConversacion` (compuesto y hoja), `ContenidoConversable` (interfaz común de Kudo y Réplica).

**Beneficio.** La profundidad ilimitada no complica el código. Calificar, reportar o moderar funciona igual para un Kudo o una réplica (LSP). En el front-end, `ConversacionArbol` es el mismo patrón como componente Vue recursivo.

**Alternativa descartada.** Guardar el árbol dentro del agregado Kudo. La conversación crece sin límite (RN-163) y habría que cargarla completa para cualquier operación.

---

## 3. Observer

**Problema.** Casi todas las operaciones deben quedar auditadas (RN-096 a RN-100, RN-249, RN-250), muchas deben notificar a alguien (D-27) y las calificaciones deben recalcular indicadores (RN-238). Si cada caso de uso llamara directamente a la auditoría, a las notificaciones y a los indicadores, el negocio quedaría acoplado a todo y cada efecto nuevo obligaría a modificar muchos casos de uso.

**Solución.** El negocio publica un **evento de dominio** y los suscriptores interesados reaccionan.

```java
public interface SuscriptorEvento {
    boolean leInteresa(EventoDominio evento);
    void manejar(EventoDominio evento, DAOFactory daoFactory);
}

public final class PublicadorEventos {
    private final List<SuscriptorEvento> suscriptores;   // auditoría, notificaciones, indicadores
    public void publicar(EventoDominio evento, DAOFactory daoFactory) {
        suscriptores.stream().filter(s -> s.leInteresa(evento)).forEach(s -> s.manejar(evento, daoFactory));
    }
}

// En KudoNegocioImpl:
publicador.publicar(new KudoPublicado(kudo, actor), daoFactory);
```

**Participantes.** `EventoDominio` y sus subclases (`KudoPublicado`, `UsuarioAprobado`, `ContenidoVetado`…), `PublicadorEventos` (sujeto), `AuditoriaSuscriptor`, `NotificacionSuscriptor` e `IndicadorSuscriptor` (observadores).

**Beneficio.** El negocio no conoce la auditoría (SRP, bajo acoplamiento). Un nuevo efecto, como un correo real o una métrica, es un suscriptor nuevo (OCP). Los suscriptores corren en la misma transacción, así que una operación nunca queda sin auditar.

**Alternativa descartada.** *Triggers* de base de datos para la auditoría. Esconden lógica de negocio en la base, no conocen al actor ni el motivo y son difíciles de probar.

---

## 4. Specification (políticas)

**Problema.** "¿Puede este actor actuar sobre este usuario, programa o facultad?" se pregunta en aprobar, rechazar, suspender, vincular, consultar personas y consultar auditoría (RN-112 a RN-131, POL-004). "¿Es moderador?" y "¿puede gestionar categorías?" se repiten igual. Copiar esas condiciones en cada caso de uso es duplicación y una fuente segura de errores.

**Solución.** Cada regla de autorización es un objeto con un método que responde sí o no, y se reutiliza donde haga falta.

```java
public final class PoliticaModeracion {
    public boolean esSatisfechaPor(Usuario actor) {
        return actor.tieneRolVigente(Rol.RECTOR) || actor.tieneRolVigente(Rol.COORDINADOR_ACADEMICO);
    }
}
```

`PoliticaAmbito` concentra la matriz de gestión: el Coordinador de Programa sobre los vinculados a su programa, el Coordinador de Facultad sobre los Coordinadores de Programa de su Facultad, y así hasta el Administrador sobre Administradores y Rectores.

**Beneficio.** Las reglas de autorización están en un solo lugar, tienen nombre del dominio y se prueban por separado. Si cambia la matriz de gestión, cambia una clase.

---

## 5. Builder

**Problema.** Las clases tienen muchos atributos (Usuario tiene 11, Kudo 10). Hacen falta valores por defecto seguros (UUID vacío, cadena vacía, instancia por defecto) y saneamiento (quitar espacios, evitar nulos). Un constructor con 11 parámetros es ilegible y propenso a errores.

**Solución.** Igual que en Sigma, cada clase de dominio, entidad y DTO tiene un `Builder` interno estático. El constructor es privado y solo lo invoca `build()`. Cada página del [diagrama de clases](../02-diagrama-clases/README.md) documenta su Builder.

**Beneficio.** No existen objetos a medio construir ni con `null`. La construcción es legible: `new Kudo.Builder().autor(a).destinatario(d)...build()`.

**Nota de diseño.** En el dominio, el Builder se usa para **reconstruir** objetos leídos de la base. Los objetos nuevos se crean con métodos de fábrica que validan las reglas (`Kudo.crear(...)`), para que no se salten las invariantes.

---

## 6. Abstract Factory (DAOFactory)

**Problema.** Un caso de uso necesita varios DAO (aprobar una solicitud usa los DAO de solicitud, usuario, asignación y vínculo) y todos deben compartir **la misma conexión**, para que la transacción los cubra a todos. Además, la persistencia debe poder cambiar de motor sin tocar el negocio.

**Solución.** Igual que en Sigma: `DAOFactory` es abstracta, abre la conexión, controla la transacción y fabrica cada DAO (`obtenerKudoDAO()`, `obtenerUsuarioDAO()`…). `SqlServerDAOFactory` es la implementación concreta, y `FactoriaEnum` elige la fábrica.

**Beneficio.** Una sola transacción por caso de uso. Cambiar de motor de base de datos es otra fábrica. En las pruebas, una fábrica falsa devuelve DAO en memoria (DIP, testabilidad).

---

## 7. Facade

**Problema.** El controlador no debería conocer los detalles de un caso de uso: transacción, conversión DTO ↔ dominio, reglas, varios DAO y eventos.

**Solución.** Una `XxxFachada` por módulo ofrece una operación por caso de uso (`KudoFachada.crear`, `ModeracionFachada.vetar`), abre y cierra la transacción y traduce los errores inesperados. Es el mismo papel que tiene en Sigma.

**Beneficio.** El controlador queda delgado, el límite transaccional queda claro y la capa web se podría reemplazar sin tocar el negocio.

---

## 8. DAO + Assembler

**Problema.** El dominio no debe depender de cómo se guardan los datos (tablas y columnas) ni de cómo viajan por la API (JSON). Cada lado tiene su propia forma.

**Solución.** Hay tres representaciones con su ensamblador, igual que en Sigma. `XxxDto` ↔ `XxxDominio` con `DTOAssembler` en la fachada, y `XxxDominio` ↔ `XxxEntidad` con `EntidadAssembler` en el negocio. Los `XxxDAO` traducen la entidad a SQL.

**Beneficio.** Cambiar una columna o un campo de la API no toca el dominio (SoC, ARQ-041, ARQ-042).

**Alternativa descartada.** *Repository* al estilo DDD. Es equivalente al DAO del curso; se mantiene el DAO para seguir el estilo enseñado.

---

## 9. Objeto de valor (Value Object)

**Problema.** El formato del correo institucional, del celular, del contenido no vacío y de las estrellas de 1 a 5 se validaría en el registro, en el cambio de datos, en las calificaciones y en las réplicas.

**Solución.** `CorreoInstitucional`, `NumeroCelular`, `ContenidoTexto` y `Estrellas` son inmutables y se validan en su método de fábrica `crear(...)`. Si existen, son válidos.

**Beneficio.** La regla vive en un solo lugar (DRY) y es imposible tener un correo o unas estrellas inválidas dentro del dominio.

---

## 10. Servicio de dominio

**Problema.** Aprobar una solicitud toca SolicitudRegistro, Usuario, AsignacionRol (incluida la sustitución del coordinador anterior) y VinculacionPrograma. Vetar toca Moderacion, el Kudo o la réplica y todos sus reportes. Ninguna de esas entidades debería conocer a las demás.

**Solución.** `ServicioAprobacion` y `ServicioModeracion` coordinan las entidades dentro de la transacción de la fachada. Cada entidad sigue protegiendo su propio estado. Ver los diagramas de secuencia de [aprobar solicitud](../06-diagramas-comportamiento/README.md#aprobar-solicitud-de-registro) y [vetar contenido](../06-diagramas-comportamiento/README.md#vetar-contenido).

**Beneficio.** Cada entidad conserva alta cohesión y la coordinación queda explícita y con nombre del dominio (doc. 06 §29).

---

## 11. Puerto y adaptador (Strategy) para el envío de códigos

**Problema.** Hay que enviar códigos por correo y SMS (RN-077, RN-078), pero no hay un proveedor real, y el dominio no debe depender de uno.

**Solución.** La interfaz `EnviadorNotificacion` es el puerto, con el método `enviar(canal, destino, mensaje)`. La implementación de este proyecto, `EnviadorNotificacionSimulado`, deja el código en la bandeja de notificaciones y en el log (D-10). Un proveedor real sería otra implementación intercambiable: es el patrón Strategy.

**Beneficio.** DIP y OCP: integrar SMTP o un servicio de SMS no cambia el dominio ni el negocio.

---

## 12. Singleton (reglas y ensambladores)

**Problema.** Las reglas (`Rule`) y los ensambladores no guardan estado y se usan en muchos casos de uso; crear una instancia por llamada no aporta nada.

**Solución.** Igual que en Sigma, cada regla expone `obtenerInstancia()` y cada ensamblador `getInstance()`, con un constructor privado.

**Justificación y límite.** Es seguro porque son objetos **sin estado**: los datos y la `DAOFactory` llegan como parámetros, así que no hay problemas de concurrencia. No se usa Singleton para nada que guarde estado.

---

## Patrones evaluados y descartados

| Patrón | Por qué no se usó |
|---|---|
| Command | No hay operaciones para deshacer, encolar ni programar. Los casos de uso ya son métodos de la fachada. |
| Chain of Responsibility | Las reglas de un caso de uso se ejecutan en orden fijo y todas deben cumplirse; una lista de llamadas en el negocio es más simple (KISS). |
| Template Method | Las fachadas comparten el esqueleto de la transacción, pero se mantiene explícito como en Sigma para que se lea y se modifique fácilmente en la sustentación. Es un candidato de refactorización si la duplicación crece. |
| Strategy para los roles | Las diferencias entre roles son de **autorización**, no de algoritmo; se resuelven con la enumeración `Rol` y las políticas (Specification). |
| Herencia para los roles | Un objeto no puede cambiar de clase y los roles cambian con el tiempo. Se usa composición (`AsignacionRol`, D-13). |
| Repository (DDD) | Equivale al DAO del curso; se conserva el DAO por coherencia con lo enseñado. |
