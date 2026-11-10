package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.EstadoFacultad;
import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dominio.ParametroGlobalDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.RegistroNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.RegistroNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de registro: solicitud de cuenta con el correo de Google (CU-016, RF-001 a RF-005, D-17)")
class RegistroNegocioTest {

    private static final String CORREO = "ana.perez@gmail.com";
    private static final String CELULAR = "3001234567";

    private DAOFactoryFalsa daoFactory;
    private RegistroNegocio negocio;
    private UUID idPrograma;
    private UUID idFacultad;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        daoFactory.registrarParametro(ParametroGlobalDominio.DOMINIO_CORREO, "gmail.com,uco.net.co");
        daoFactory.registrarParametro(ParametroGlobalDominio.VIGENCIA_CODIGO_MINUTOS, "15");
        daoFactory.registrarParametro(ParametroGlobalDominio.MAX_INTENTOS_VERIFICACION, "5");
        idPrograma = daoFactory.registrarPrograma(true);
        idFacultad = daoFactory.registrarFacultad(EstadoFacultad.ACTIVA);
        negocio = new RegistroNegocioImpl(daoFactory);
    }

    private SolicitudRegistroDominio datos(UUID facultad, UUID programa) {
        return new SolicitudRegistroDominio.Builder()
                .solicitante(new UsuarioDominio.Builder().nombres(" Ana ").apellidos("Pérez").numeroCelular(" " + CELULAR + " ").build())
                .facultad(new FacultadDominio.Builder().id(facultad).build())
                .programa(new ProgramaAcademicoDominio.Builder().id(programa).build())
                .build();
    }

    @Test
    @DisplayName("Un estudiante queda pendiente de aprobación, con el correo verificado por Google y un código para el celular")
    void registroDeEstudiante() {
        negocio.registrarSolicitud(" Ana.Perez@Gmail.com ", "ESTUDIANTE", datos(idFacultad, idPrograma));

        var usuario = daoFactory.getUsuarioDAOFalso().consultarTodos().getFirst();
        assertEquals(CORREO, usuario.getCorreoInstitucional());
        assertEquals(CELULAR, usuario.getNumeroCelular());
        assertEquals("PENDIENTE_APROBACION", usuario.getEstadoCuenta());
        assertTrue(usuario.isCorreoVerificado());
        assertFalse(usuario.isCelularVerificado());
        assertFalse(usuario.isAprobada());

        var solicitud = daoFactory.getSolicitudRegistroDAOFalso().getSolicitudes().getFirst();
        assertEquals("ESTUDIANTE", solicitud.getRolSolicitado());
        assertEquals("PENDIENTE", solicitud.getEstado());
        assertEquals(usuario.getId(), solicitud.getSolicitante().getId());
        assertEquals(idPrograma, solicitud.getPrograma().getId());
        assertTrue(UtilId.esValorDefecto(solicitud.getFacultad().getId()));

        var codigo = daoFactory.getCodigoVerificacionDAOFalso().getCodigos().getFirst();
        assertEquals("CELULAR", codigo.getCanal());
        assertEquals(CELULAR, codigo.getDestino());
        assertTrue(codigo.getCodigo().matches("\\d{6}"));
        assertEquals(15, Duration.between(codigo.getFechaGeneracion(), codigo.getFechaExpiracion()).toMinutes());

        var notificacion = daoFactory.getNotificacionDAOFalso().getNotificaciones().getFirst();
        assertEquals("CODIGO_VERIFICACION", notificacion.getTipo());
        assertTrue(notificacion.getMensaje().contains(codigo.getCodigo()));

        var auditoria = daoFactory.getAuditoriaDAOFalso().getRegistros().getLast();
        assertEquals("SolicitudRegistroCreada", auditoria.getAccion());
    }

    @Test
    @DisplayName("El Coordinador de Facultad guarda solo la Facultad y el Coordinador Académico ningún ámbito")
    void ambitoSegunRol() {
        negocio.registrarSolicitud(CORREO, "COORDINADOR_FACULTAD", datos(idFacultad, idPrograma));
        negocio.registrarSolicitud("luis@gmail.com", "COORDINADOR_ACADEMICO",
                new SolicitudRegistroDominio.Builder()
                        .solicitante(new UsuarioDominio.Builder().nombres("Luis").apellidos("Gómez").numeroCelular("3109876543").build())
                        .facultad(new FacultadDominio.Builder().id(idFacultad).build())
                        .build());

        var solicitudes = daoFactory.getSolicitudRegistroDAOFalso().getSolicitudes();
        var academico = solicitudes.get(0);
        var deFacultad = solicitudes.get(1);
        assertEquals(idFacultad, deFacultad.getFacultad().getId());
        assertTrue(UtilId.esValorDefecto(deFacultad.getPrograma().getId()));
        assertTrue(UtilId.esValorDefecto(academico.getFacultad().getId()));
        assertTrue(UtilId.esValorDefecto(academico.getPrograma().getId()));
    }

    @Test
    @DisplayName("Si una regla falla no se guarda nada: correo repetido, rol no autorregistrable o sin Programa")
    void rechazosSinGuardarNada() {
        daoFactory.registrarUsuarioConContacto(CORREO, "3110000000");
        var datosValidos = datos(idFacultad, idPrograma);
        var datosSinPrograma = datos(idFacultad, null);

        var repetido = assertThrows(KudosExcepcion.class, () -> negocio.registrarSolicitud(CORREO, "ESTUDIANTE", datosValidos));
        var rector = assertThrows(KudosExcepcion.class, () -> negocio.registrarSolicitud("otro@gmail.com", "RECTOR", datosValidos));
        var sinPrograma = assertThrows(KudosExcepcion.class, () -> negocio.registrarSolicitud("otro@gmail.com", "DOCENTE", datosSinPrograma));

        assertEquals(CatalogoMensajes.Identidad.CORREO_YA_REGISTRADO, repetido.getMensajeUsuario());
        assertEquals(CatalogoMensajes.Identidad.ROL_NO_AUTORREGISTRABLE, rector.getMensajeUsuario());
        assertEquals(CatalogoMensajes.Identidad.PROGRAMA_OBLIGATORIO, sinPrograma.getMensajeUsuario());
        assertEquals(1, daoFactory.getUsuarioDAOFalso().consultarTodos().size());
        assertTrue(daoFactory.getSolicitudRegistroDAOFalso().getSolicitudes().isEmpty());
        assertTrue(daoFactory.getCodigoVerificacionDAOFalso().getCodigos().isEmpty());
    }

    @Test
    @DisplayName("Las opciones del formulario muestran solo Facultades activas y Programas activos")
    void opcionesDisponibles() {
        daoFactory.registrarFacultad(EstadoFacultad.EN_CONFORMACION);
        daoFactory.registrarFacultad(EstadoFacultad.INACTIVA);
        daoFactory.registrarPrograma(false);

        var facultades = negocio.consultarFacultadesDisponibles();
        var programas = negocio.consultarProgramasDisponibles();

        assertEquals(1, facultades.size());
        assertEquals(idFacultad, facultades.getFirst().getId());
        assertEquals(1, programas.size());
        assertEquals(idPrograma, programas.getFirst().getId());
    }
}
