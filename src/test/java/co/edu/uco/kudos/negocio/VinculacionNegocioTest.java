package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.EstadoFacultad;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.VinculacionNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.VinculacionNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
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

@DisplayName("Negocio de vínculos con Programas: vincular, desvincular, suspender y reactivar (CU-032, CU-033, RF-025 a RF-034, RF-085, D-16)")
class VinculacionNegocioTest {

    private static final String MOTIVO = "Terminó sus estudios en el Programa.";

    private DAOFactoryFalsa daoFactory;
    private VinculacionNegocio negocio;
    private UUID idPrograma;
    private UUID idOtroPrograma;
    private UUID coordinador;
    private UUID estudiante;
    private UUID docente;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        var idFacultad = daoFactory.registrarFacultad(EstadoFacultad.ACTIVA);
        idPrograma = daoFactory.registrarPrograma(idFacultad);
        idOtroPrograma = daoFactory.registrarPrograma(idFacultad);
        coordinador = daoFactory.registrarAutoridad(Rol.COORDINADOR_PROGRAMA, null, idPrograma);
        estudiante = daoFactory.registrarPersona("sofia@gmail.com", Rol.ESTUDIANTE, EstadoCuenta.ACTIVA);
        docente = daoFactory.registrarPersona("mateo@gmail.com", Rol.DOCENTE, EstadoCuenta.ACTIVA);
        daoFactory.registrarVinculo(estudiante, idOtroPrograma, "ESTUDIANTE");
        negocio = new VinculacionNegocioImpl(daoFactory);
    }

    private void assertRechazo(Class<? extends KudosExcepcion> tipo, String mensaje, Executable operacion) {
        var excepcion = assertThrows(tipo, operacion);
        assertEquals(mensaje, excepcion.getMensajeUsuario());
    }

    private String estadoDe(UUID idUsuario) {
        return daoFactory.getUsuarioDAOFalso().consultarPorId(idUsuario).getEstadoCuenta();
    }

    @Test
    @DisplayName("El coordinador vincula por correo a un estudiante ya registrado; puede estar en varios Programas (RF-026)")
    void vincularEstudiante() {
        negocio.vincular(coordinador, idPrograma, " Sofia@Gmail.com ", "ESTUDIANTE");

        var vinculos = negocio.consultarPropios(estudiante);
        assertEquals(2, vinculos.size());
        assertTrue(vinculos.stream().allMatch(v -> v.estaVigente()));
        assertEquals(1, negocio.consultarDePrograma(coordinador, idPrograma).size());
        assertEquals("PersonaVinculada", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());
    }

    @Test
    @DisplayName("Rechaza correo inexistente, tipo inválido, rol distinto, vínculo repetido y Programa ajeno")
    void rechazosAlVincular() {
        var otroCoordinador = daoFactory.registrarAutoridad(Rol.COORDINADOR_PROGRAMA, null, idOtroPrograma);

        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.PERSONA_NO_REGISTRADA,
                () -> negocio.vincular(coordinador, idPrograma, "nadie@gmail.com", "ESTUDIANTE"));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.TIPO_VINCULO_INVALIDO,
                () -> negocio.vincular(coordinador, idPrograma, "sofia@gmail.com", "EGRESADO"));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.ROL_NO_COINCIDE_CON_VINCULO,
                () -> negocio.vincular(coordinador, idPrograma, "mateo@gmail.com", "ESTUDIANTE"));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.VINCULO_YA_EXISTE,
                () -> negocio.vincular(otroCoordinador, idOtroPrograma, "sofia@gmail.com", "ESTUDIANTE"));
        assertRechazo(KudosAutorizacionExcepcion.class, CatalogoMensajes.Organizacion.ACTOR_NO_COORDINA_PROGRAMA,
                () -> negocio.vincular(otroCoordinador, idPrograma, "mateo@gmail.com", "DOCENTE"));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.PROGRAMA_NO_EXISTE,
                () -> negocio.vincular(coordinador, UtilId.generarId(), "mateo@gmail.com", "DOCENTE"));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.PERSONA_NO_REGISTRADA,
                () -> negocio.vincular(coordinador, idPrograma, " ", "DOCENTE"));
    }

    @Test
    @DisplayName("Desvincular exige motivo, cierra el vínculo sin borrarlo y suspende a quien queda sin Programas (D-16)")
    void desvincularYSuspender() {
        negocio.vincular(coordinador, idPrograma, "mateo@gmail.com", "DOCENTE");
        var idVinculo = negocio.consultarDePrograma(coordinador, idPrograma).getFirst().getId();

        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.MOTIVO_DESVINCULACION_OBLIGATORIO,
                () -> negocio.desvincular(coordinador, idVinculo, ""));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.MOTIVO_DESVINCULACION_LONGITUD_INVALIDA,
                () -> negocio.desvincular(coordinador, idVinculo, "Corto"));

        negocio.desvincular(coordinador, idVinculo, MOTIVO);

        var vinculo = negocio.consultarPropios(docente).getFirst();
        assertFalse(vinculo.estaVigente());
        assertFalse(UtilFecha.esValorDefecto(vinculo.getFechaFin()));
        assertEquals(MOTIVO, vinculo.getMotivoDesvinculacion());
        assertEquals("SUSPENDIDA", estadoDe(docente));
        assertEquals("CUENTA_SUSPENDIDA", daoFactory.getNotificacionDAOFalso().getNotificaciones().getFirst().getTipo());
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.VINCULO_YA_CERRADO,
                () -> negocio.desvincular(coordinador, idVinculo, MOTIVO));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.VINCULO_NO_EXISTE,
                () -> negocio.desvincular(coordinador, UtilId.generarId(), MOTIVO));
    }

    @Test
    @DisplayName("Quien conserva otro Programa no se suspende; si vuelven a vincular a un suspendido, se reactiva (decisión a de M3)")
    void reactivarAlVolverAVincular() {
        negocio.vincular(coordinador, idPrograma, "sofia@gmail.com", "ESTUDIANTE");
        var idVinculo = negocio.consultarDePrograma(coordinador, idPrograma).getFirst().getId();
        negocio.desvincular(coordinador, idVinculo, MOTIVO);
        assertEquals("ACTIVA", estadoDe(estudiante));

        negocio.vincular(coordinador, idPrograma, "mateo@gmail.com", "DOCENTE");
        negocio.desvincular(coordinador, negocio.consultarPropios(docente).getFirst().getId(), MOTIVO);
        assertEquals("SUSPENDIDA", estadoDe(docente));

        negocio.vincular(coordinador, idPrograma, "mateo@gmail.com", "DOCENTE");

        assertEquals("ACTIVA", estadoDe(docente));
        assertEquals("CUENTA_REACTIVADA", daoFactory.getNotificacionDAOFalso().getNotificaciones().getFirst().getTipo());
        assertEquals(2, negocio.consultarPropios(docente).size());
    }

    @Test
    @DisplayName("Las autoridades consultan los Programas de una persona; un estudiante no (RF-027, RF-072)")
    void consultarProgramasDeUnaPersona() {
        assertEquals(1, negocio.consultarDeUsuario(coordinador, estudiante).size());
        assertRechazo(KudosAutorizacionExcepcion.class, CatalogoMensajes.Organizacion.ACTOR_NO_CONSULTA_ORGANIZACION,
                () -> negocio.consultarDeUsuario(estudiante, docente));
    }
}
