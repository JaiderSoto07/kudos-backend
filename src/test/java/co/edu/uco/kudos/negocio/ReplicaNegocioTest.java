package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.ReplicaEntidad;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ConversacionDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.ReplicaNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.KudoEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ReplicaEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.KudoNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.ReplicaNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de réplicas: responder y consultar la conversación (CU-004 a CU-006, RF-053 a RF-056, D-21, D-22)")
class ReplicaNegocioTest {

    private DAOFactoryFalsa daoFactory;
    private ReplicaNegocio negocio;
    private UUID ana;
    private UUID luis;
    private UUID marta;
    private UUID idKudo;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        ana = daoFactory.registrarPersona("ana@gmail.com", Rol.ESTUDIANTE, EstadoCuenta.ACTIVA);
        luis = daoFactory.registrarPersona("luis@gmail.com", Rol.DOCENTE, EstadoCuenta.ACTIVA);
        marta = daoFactory.registrarPersona("marta@gmail.com", Rol.ESTUDIANTE, EstadoCuenta.ACTIVA);
        var liderazgo = daoFactory.registrarCategoria("Liderazgo", true);
        new KudoNegocioImpl(daoFactory).crear(ana, new KudoDominio.Builder()
                .destinatario(new UsuarioDominio.Builder().id(luis).build())
                .categoria(new CategoriaDominio.Builder().id(liderazgo).build())
                .contenido("Gracias por tu apoyo en el semillero.")
                .build());
        idKudo = daoFactory.getKudoDAOFalso().getKudos().getFirst().getId();
        daoFactory.getNotificacionDAOFalso().getNotificaciones().clear();
        negocio = new ReplicaNegocioImpl(daoFactory);
    }

    private void assertRechazo(String mensaje, Executable operacion) {
        var excepcion = assertThrows(KudosExcepcion.class, operacion);
        assertEquals(mensaje, excepcion.getMensajeUsuario());
    }

    private ReplicaEntidad ultimaReplica() {
        return daoFactory.getReplicaDAOFalso().getReplicas().getLast();
    }

    private void vetarKudo() {
        var kudo = KudoEntidadAssembler.getInstance().convertirADominio(daoFactory.getKudoDAOFalso().consultarPorId(idKudo));
        kudo.vetar();
        daoFactory.getKudoDAOFalso().actualizar(idKudo, KudoEntidadAssembler.getInstance().convertirAEntidad(kudo));
    }

    private void vetarReplica(UUID idReplica) {
        var replica = ReplicaEntidadAssembler.getInstance().convertirADominio(daoFactory.getReplicaDAOFalso().consultarPorId(idReplica));
        replica.vetar();
        daoFactory.getReplicaDAOFalso().actualizar(idReplica, ReplicaEntidadAssembler.getInstance().convertirAEntidad(replica));
    }

    @Test
    @DisplayName("Responder un Kudo crea la réplica VISIBLE, la audita y avisa al autor y al destinatario")
    void responderKudo() {
        negocio.responderKudo(marta, idKudo, "¡Muy merecido!");

        var replica = ultimaReplica();
        assertEquals("VISIBLE", replica.getEstado());
        assertEquals(idKudo, replica.getKudo().getId());
        assertEquals(marta, replica.getAutor().getId());
        assertTrue(UtilId.esValorDefecto(replica.getIdReplicaPadre()));
        var notificaciones = daoFactory.getNotificacionDAOFalso().getNotificaciones();
        assertEquals(2, notificaciones.size());
        assertTrue(notificaciones.stream().allMatch(notificacion -> "REPLICA_RECIBIDA".equals(notificacion.getTipo())));
        assertTrue(notificaciones.stream().anyMatch(notificacion -> ana.equals(notificacion.getDestinatario().getId())));
        assertTrue(notificaciones.stream().anyMatch(notificacion -> luis.equals(notificacion.getDestinatario().getId())));
        assertEquals("ReplicaPublicada", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());
    }

    @Test
    @DisplayName("Nadie recibe aviso de su propia respuesta")
    void sinAvisoPropio() {
        negocio.responderKudo(luis, idKudo, "Gracias, Ana.");

        var notificaciones = daoFactory.getNotificacionDAOFalso().getNotificaciones();
        assertEquals(1, notificaciones.size());
        assertEquals(ana, notificaciones.getFirst().getDestinatario().getId());
    }

    @Test
    @DisplayName("Responder una réplica fija su padre, avisa solo al autor de esa réplica y admite profundidad ilimitada (RF-056)")
    void responderReplica() {
        negocio.responderKudo(marta, idKudo, "¡Muy merecido!");
        var idPadre = ultimaReplica().getId();
        daoFactory.getNotificacionDAOFalso().getNotificaciones().clear();

        negocio.responderReplica(luis, idPadre, "Gracias, Marta.");

        var hija = ultimaReplica();
        assertEquals(idPadre, hija.getIdReplicaPadre());
        assertEquals(idKudo, hija.getKudo().getId());
        var notificaciones = daoFactory.getNotificacionDAOFalso().getNotificaciones();
        assertEquals(1, notificaciones.size());
        assertEquals(marta, notificaciones.getFirst().getDestinatario().getId());

        for (var nivel = 0; nivel < 15; nivel++) {
            negocio.responderReplica(nivel % 2 == 0 ? ana : marta, ultimaReplica().getId(), "Nivel " + nivel);
        }
        var arbol = ConversacionDtoAssembler.getInstance().armarArbol(negocio.consultarConversacion(idKudo));
        var profundidad = 0;
        var nodo = arbol.getFirst();
        while (!nodo.getRespuestas().isEmpty()) {
            nodo = nodo.getRespuestas().getFirst();
            profundidad++;
        }
        assertEquals(16, profundidad);
    }

    @Test
    @DisplayName("La conversación se arma como árbol; una réplica vetada se muestra como contenido vetado y conserva sus respuestas (D-21, D-22)")
    void conversacionConVetada() {
        negocio.responderKudo(marta, idKudo, "Primera réplica");
        var idPrimera = ultimaReplica().getId();
        negocio.responderKudo(luis, idKudo, "Segunda réplica");
        negocio.responderReplica(ana, idPrimera, "Respuesta a la primera");
        vetarReplica(idPrimera);

        var arbol = ConversacionDtoAssembler.getInstance().armarArbol(negocio.consultarConversacion(idKudo));

        assertEquals(2, arbol.size());
        var primera = arbol.getFirst();
        assertTrue(primera.isVetada());
        assertEquals(CatalogoMensajes.Replica.CONTENIDO_VETADO, primera.getContenido());
        assertEquals(1, primera.getRespuestas().size());
        assertEquals("Respuesta a la primera", primera.getRespuestas().getFirst().getContenido());
        assertFalse(arbol.getLast().isVetada());
        assertTrue(arbol.getLast().getRespuestas().isEmpty());
    }

    @Test
    @DisplayName("Se rechaza responder sin contenido, un Kudo que no existe, un Kudo vetado o una réplica vetada, y no se guarda nada")
    void rechazos() {
        negocio.responderKudo(marta, idKudo, "Réplica válida");
        var idReplica = ultimaReplica().getId();
        var desconocido = UtilId.generarId();

        assertRechazo(CatalogoMensajes.Kudo.CONTENIDO_OBLIGATORIO, () -> negocio.responderKudo(marta, idKudo, "   "));
        assertRechazo(CatalogoMensajes.Kudo.CONTENIDO_OBLIGATORIO, () -> negocio.responderReplica(ana, idReplica, null));
        assertRechazo(CatalogoMensajes.Kudo.KUDO_NO_EXISTE, () -> negocio.responderKudo(marta, desconocido, "Hola"));
        assertRechazo(CatalogoMensajes.Kudo.KUDO_NO_EXISTE, () -> negocio.consultarConversacion(desconocido));
        assertRechazo(CatalogoMensajes.Replica.REPLICA_NO_EXISTE, () -> negocio.responderReplica(ana, desconocido, "Hola"));
        vetarReplica(idReplica);
        assertRechazo(CatalogoMensajes.Replica.REPLICA_VETADA, () -> negocio.responderReplica(ana, idReplica, "Hola"));
        vetarKudo();
        assertRechazo(CatalogoMensajes.Replica.KUDO_VETADO, () -> negocio.responderKudo(marta, idKudo, "Hola"));
        assertRechazo(CatalogoMensajes.Replica.KUDO_VETADO, () -> negocio.responderReplica(ana, idReplica, "Hola"));

        assertEquals(1, daoFactory.getReplicaDAOFalso().getReplicas().size());
        assertEquals(1, negocio.consultarConversacion(idKudo).size());
    }
}
