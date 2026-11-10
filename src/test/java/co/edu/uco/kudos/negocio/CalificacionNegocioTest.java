package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ValoracionesDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.CalificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.KudoEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ReplicaEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.CalificacionNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.KudoNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.ReplicaNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de calificaciones: calificar, modificar y consultar valoraciones (CU-007 a CU-009, RF-057 a RF-060, RF-091, RF-092, D-23)")
class CalificacionNegocioTest {

    private DAOFactoryFalsa daoFactory;
    private CalificacionNegocio negocio;
    private UUID ana;
    private UUID luis;
    private UUID marta;
    private UUID idKudo;
    private UUID idReplica;

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
        new ReplicaNegocioImpl(daoFactory).responderKudo(marta, idKudo, "¡Muy merecido!");
        idReplica = daoFactory.getReplicaDAOFalso().getReplicas().getLast().getId();
        negocio = new CalificacionNegocioImpl(daoFactory);
    }

    private void assertRechazo(String mensaje, Executable operacion) {
        var excepcion = assertThrows(KudosExcepcion.class, operacion);
        assertEquals(mensaje, excepcion.getMensajeUsuario());
    }

    private KudoDominio kudo() {
        return KudoEntidadAssembler.getInstance().convertirADominio(daoFactory.getKudoDAOFalso().consultarPorId(idKudo));
    }

    @Test
    @DisplayName("Calificar un Kudo crea la calificación activa, se audita y recalcula el promedio y la cantidad (RF-092)")
    void calificarKudo() {
        negocio.calificarKudo(luis, idKudo, 5);
        negocio.calificarKudo(marta, idKudo, 4);

        var calificaciones = daoFactory.getCalificacionDAOFalso().getCalificaciones();
        assertEquals(2, calificaciones.size());
        assertTrue(calificaciones.getFirst().isActiva());
        assertEquals(luis, calificaciones.getFirst().getAutor().getId());
        assertEquals(idKudo, calificaciones.getFirst().getKudo().getId());
        assertTrue(UtilId.esValorDefecto(calificaciones.getFirst().getReplica().getId()));
        assertEquals(new BigDecimal("4.50"), kudo().getPromedioCalificacion());
        assertEquals(2, kudo().getCantidadCalificaciones());
        var auditoria = daoFactory.getAuditoriaDAOFalso().getRegistros().getLast();
        assertEquals("CalificacionRegistrada", auditoria.getAccion());
        assertEquals("4", auditoria.getEstadoNuevo());
    }

    @Test
    @DisplayName("Volver a calificar modifica la misma calificación y audita el valor anterior y el nuevo (RF-060, RF-091)")
    void modificarCalificacion() {
        negocio.calificarKudo(luis, idKudo, 2);
        negocio.calificarKudo(luis, idKudo, 5);

        var calificaciones = daoFactory.getCalificacionDAOFalso().getCalificaciones();
        assertEquals(1, calificaciones.size());
        assertEquals(5, calificaciones.getFirst().getEstrellas());
        assertFalse(UtilFecha.esValorDefecto(calificaciones.getFirst().getFechaModificacion()));
        assertEquals(new BigDecimal("5.00"), kudo().getPromedioCalificacion());
        assertEquals(1, kudo().getCantidadCalificaciones());
        var auditoria = daoFactory.getAuditoriaDAOFalso().getRegistros().getLast();
        assertEquals("CalificacionActualizada", auditoria.getAccion());
        assertEquals("2", auditoria.getEstadoAnterior());
        assertEquals("5", auditoria.getEstadoNuevo());
    }

    @Test
    @DisplayName("Calificar una réplica recalcula su promedio; el destinatario del Kudo también puede calificar")
    void calificarReplica() {
        negocio.calificarReplica(luis, idReplica, 3);
        negocio.calificarReplica(ana, idReplica, 4);

        var replica = ReplicaEntidadAssembler.getInstance().convertirADominio(daoFactory.getReplicaDAOFalso().consultarPorId(idReplica));
        assertEquals(new BigDecimal("3.50"), replica.getPromedioCalificacion());
        assertEquals(2, replica.getCantidadCalificaciones());
        assertEquals(0, kudo().getCantidadCalificaciones());
    }

    @Test
    @DisplayName("Las valoraciones muestran promedio, cantidad, distribución por estrellas y la calificación propia, sin nombres (CU-009)")
    void valoraciones() {
        negocio.calificarKudo(luis, idKudo, 5);
        negocio.calificarKudo(marta, idKudo, 3);

        var paraMarta = ValoracionesDtoAssembler.getInstance().armar(negocio.consultarValoracionesKudo(idKudo), marta);
        assertEquals(2, paraMarta.getCantidad());
        assertEquals(new BigDecimal("4.00"), paraMarta.getPromedio());
        assertEquals(List.of(0, 0, 1, 0, 1), paraMarta.getDistribucion());
        assertEquals(3, paraMarta.getMiCalificacion());

        var paraAna = ValoracionesDtoAssembler.getInstance().armar(negocio.consultarValoracionesKudo(idKudo), ana);
        assertEquals(0, paraAna.getMiCalificacion());

        var deLaReplica = ValoracionesDtoAssembler.getInstance().armar(negocio.consultarValoracionesReplica(idReplica), ana);
        assertEquals(0, deLaReplica.getCantidad());
        assertEquals(BigDecimal.ZERO, deLaReplica.getPromedio());
        assertEquals(List.of(0, 0, 0, 0, 0), deLaReplica.getDistribucion());
    }

    @Test
    @DisplayName("Se rechaza calificar lo propio, fuera de 1 a 5, contenido que no existe o contenido vetado, y no se guarda nada")
    void rechazos() {
        var desconocido = UtilId.generarId();

        assertRechazo(CatalogoMensajes.Calificacion.CONTENIDO_PROPIO, () -> negocio.calificarKudo(ana, idKudo, 5));
        assertRechazo(CatalogoMensajes.Calificacion.CONTENIDO_PROPIO, () -> negocio.calificarReplica(marta, idReplica, 5));
        assertRechazo(CatalogoMensajes.Calificacion.ESTRELLAS_FUERA_DE_RANGO, () -> negocio.calificarKudo(luis, idKudo, 0));
        assertRechazo(CatalogoMensajes.Calificacion.ESTRELLAS_FUERA_DE_RANGO, () -> negocio.calificarKudo(luis, idKudo, 6));
        assertRechazo(CatalogoMensajes.Calificacion.ESTRELLAS_FUERA_DE_RANGO, () -> negocio.calificarReplica(luis, idReplica, null));
        assertRechazo(CatalogoMensajes.Kudo.KUDO_NO_EXISTE, () -> negocio.calificarKudo(luis, desconocido, 5));
        assertRechazo(CatalogoMensajes.Kudo.KUDO_NO_EXISTE, () -> negocio.consultarValoracionesKudo(desconocido));
        assertRechazo(CatalogoMensajes.Replica.REPLICA_NO_EXISTE, () -> negocio.calificarReplica(luis, desconocido, 5));
        assertRechazo(CatalogoMensajes.Replica.REPLICA_NO_EXISTE, () -> negocio.consultarValoracionesReplica(desconocido));

        var replica = ReplicaEntidadAssembler.getInstance().convertirADominio(daoFactory.getReplicaDAOFalso().consultarPorId(idReplica));
        replica.vetar();
        daoFactory.getReplicaDAOFalso().actualizar(idReplica, ReplicaEntidadAssembler.getInstance().convertirAEntidad(replica));
        assertRechazo(CatalogoMensajes.Replica.REPLICA_VETADA, () -> negocio.calificarReplica(luis, idReplica, 5));
        var kudo = kudo();
        kudo.vetar();
        daoFactory.getKudoDAOFalso().actualizar(idKudo, KudoEntidadAssembler.getInstance().convertirAEntidad(kudo));
        assertRechazo(CatalogoMensajes.Replica.KUDO_VETADO, () -> negocio.calificarKudo(luis, idKudo, 5));

        assertTrue(daoFactory.getCalificacionDAOFalso().getCalificaciones().isEmpty());
    }
}
