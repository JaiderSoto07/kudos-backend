package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.entidad.NotificacionEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.NotificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.NotificacionNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de notificaciones: mis avisos, marcar como leída y marcar todas (CU-039, RF-095, D-27)")
class NotificacionNegocioTest {

    private DAOFactoryFalsa daoFactory;
    private NotificacionNegocio negocio;
    private UUID ana;
    private UUID luis;
    private UUID kudoRecibido;
    private UUID replicaRecibida;
    private UUID contenidoVetado;
    private UUID codigo;
    private UUID deLuis;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        ana = UtilId.generarId();
        luis = UtilId.generarId();
        kudoRecibido = registrar(ana, "KUDO_RECIBIDO", LocalDateTime.of(2026, 11, 8, 10, 0), true);
        replicaRecibida = registrar(ana, "REPLICA_RECIBIDA", LocalDateTime.of(2026, 11, 9, 10, 0), false);
        contenidoVetado = registrar(ana, "CONTENIDO_VETADO", LocalDateTime.of(2026, 11, 10, 10, 0), false);
        codigo = registrar(ana, "CODIGO_VERIFICACION", LocalDateTime.of(2026, 11, 10, 11, 0), false);
        deLuis = registrar(luis, "KUDO_RECIBIDO", LocalDateTime.of(2026, 11, 10, 12, 0), false);
        negocio = new NotificacionNegocioImpl(daoFactory);
    }

    private UUID registrar(UUID idDestinatario, String tipo, LocalDateTime fecha, boolean leida) {
        var id = UtilId.generarId();
        daoFactory.getNotificacionDAOFalso().crear(new NotificacionEntidad.Builder()
                .id(id)
                .destinatario(new UsuarioEntidad.Builder().id(idDestinatario).build())
                .tipo(tipo)
                .mensaje("Aviso " + tipo)
                .tipoReferencia("Kudo")
                .idReferencia(UtilId.generarId())
                .fechaCreacion(fecha)
                .leida(leida)
                .fechaLectura(leida ? fecha : null)
                .build());
        return id;
    }

    private NotificacionEntidad guardada(UUID id) {
        return daoFactory.getNotificacionDAOFalso().consultarPorId(id);
    }

    private void assertRechazo(String mensaje, Executable operacion) {
        var excepcion = assertThrows(KudosExcepcion.class, operacion);
        assertEquals(mensaje, excepcion.getMensajeUsuario());
    }

    @Test
    @DisplayName("Mis notificaciones: solo las mías, sin códigos de verificación, primero las no leídas y de la más reciente a la más antigua")
    void consultarMias() {
        var mias = negocio.consultarMias(ana);

        assertEquals(List.of(contenidoVetado, replicaRecibida, kudoRecibido), mias.stream().map(n -> n.getId()).toList());
        assertFalse(mias.get(0).isLeida());
        assertTrue(mias.get(2).isLeida());
    }

    @Test
    @DisplayName("Marcar una como leída guarda la fecha de lectura; nadie marca la de otra persona ni una que no existe")
    void marcarLeida() {
        negocio.marcarLeida(ana, replicaRecibida);

        assertTrue(guardada(replicaRecibida).isLeida());
        assertFalse(UtilFecha.esValorDefecto(guardada(replicaRecibida).getFechaLectura()));
        assertRechazo(CatalogoMensajes.Notificacion.NOTIFICACION_AJENA, () -> negocio.marcarLeida(ana, deLuis));
        assertRechazo(CatalogoMensajes.Notificacion.NOTIFICACION_NO_EXISTE, () -> negocio.marcarLeida(ana, UtilId.generarId()));
        assertFalse(guardada(deLuis).isLeida());
    }

    @Test
    @DisplayName("Marcar todas deja leídas solo mis avisos; el código de verificación y los avisos de otros no cambian")
    void marcarTodas() {
        negocio.marcarTodasLeidas(ana);

        assertTrue(guardada(replicaRecibida).isLeida());
        assertTrue(guardada(contenidoVetado).isLeida());
        assertTrue(guardada(kudoRecibido).isLeida());
        assertFalse(guardada(codigo).isLeida());
        assertFalse(guardada(deLuis).isLeida());
        assertTrue(negocio.consultarMias(ana).stream().allMatch(n -> n.isLeida()));
    }
}
