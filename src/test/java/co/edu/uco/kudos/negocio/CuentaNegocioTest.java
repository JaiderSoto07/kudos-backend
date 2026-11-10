package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.EstadoSolicitud;
import co.edu.uco.kudos.dominio.ParametroGlobalDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.entidad.SolicitudRegistroEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.CuentaNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.CuentaNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.VerificacionNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de mi cuenta: consultar y actualizar datos personales, celular y correo (CU-020, RF-008 a RF-011, RF-081)")
class CuentaNegocioTest {

    private static final String CORREO = "ana.perez@gmail.com";
    private static final String CELULAR = "3001234567";

    private DAOFactoryFalsa daoFactory;
    private CuentaNegocio negocio;
    private UUID idUsuario;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        daoFactory.registrarParametro(ParametroGlobalDominio.DOMINIO_CORREO, "gmail.com,uco.net.co");
        daoFactory.registrarParametro(ParametroGlobalDominio.VIGENCIA_CODIGO_MINUTOS, "15");
        daoFactory.registrarParametro(ParametroGlobalDominio.MAX_INTENTOS_VERIFICACION, "5");
        idUsuario = UtilId.generarId();
        daoFactory.getUsuarioDAOFalso().crear(new UsuarioEntidad.Builder()
                .id(idUsuario)
                .nombres("Ana")
                .apellidos("Pérez")
                .correoInstitucional(CORREO)
                .correoVerificado(true)
                .numeroCelular(CELULAR)
                .celularVerificado(true)
                .aprobada(true)
                .estadoCuenta(EstadoCuenta.ACTIVA.name())
                .build());
        negocio = new CuentaNegocioImpl(daoFactory);
    }

    private UsuarioEntidad usuario() {
        return daoFactory.getUsuarioDAOFalso().consultarPorId(idUsuario);
    }

    private void assertRechazo(String mensaje, Executable operacion) {
        var excepcion = assertThrows(KudosExcepcion.class, operacion);
        assertEquals(mensaje, excepcion.getMensajeUsuario());
    }

    @Test
    @DisplayName("Consulta la cuenta, la última solicitud y no muestra código si el celular ya está verificado")
    void consultarCuenta() {
        var idPrograma = daoFactory.registrarPrograma(true);
        daoFactory.getSolicitudRegistroDAOFalso().crear(new SolicitudRegistroEntidad.Builder()
                .id(UtilId.generarId())
                .solicitante(new UsuarioEntidad.Builder().id(idUsuario).build())
                .rolSolicitado(Rol.DOCENTE.name())
                .programa(new ProgramaAcademicoEntidad.Builder().id(idPrograma).build())
                .estado(EstadoSolicitud.APROBADA.name())
                .fechaSolicitud(UtilFecha.fechaActual())
                .build());

        assertEquals(CORREO, negocio.consultarUsuario(idUsuario).getCorreoInstitucional());
        assertEquals(Rol.DOCENTE, negocio.consultarUltimaSolicitud(idUsuario).getRolSolicitado());
        assertEquals(EstadoSolicitud.APROBADA, negocio.consultarUltimaSolicitud(idUsuario).getEstado());
        assertTrue(negocio.consultarUltimoCodigoEnviado(idUsuario).isEmpty());
        assertTrue(UtilId.esValorDefecto(negocio.consultarUltimaSolicitud(UtilId.generarId()).getId()));
    }

    @Test
    @DisplayName("Un usuario que no existe no puede consultar una cuenta")
    void usuarioInexistente() {
        var idInexistente = UtilId.generarId();

        assertThrows(KudosAutorizacionExcepcion.class, () -> negocio.consultarUsuario(idInexistente));
    }

    @Test
    @DisplayName("Actualiza nombres y apellidos sin tocar el estado de la cuenta (RN-102)")
    void actualizarDatosPersonales() {
        negocio.actualizarDatosPersonales(idUsuario, new UsuarioDominio.Builder().nombres(" Ana María ").apellidos("Pérez Ríos").build());

        assertEquals("Ana María", usuario().getNombres());
        assertEquals("Pérez Ríos", usuario().getApellidos());
        assertEquals("ACTIVA", usuario().getEstadoCuenta());
        assertEquals("DatosPersonalesActualizados", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());

        var sinNombres = new UsuarioDominio.Builder().apellidos("Pérez").build();
        assertRechazo(CatalogoMensajes.Identidad.NOMBRES_OBLIGATORIOS, () -> negocio.actualizarDatosPersonales(idUsuario, sinNombres));
    }

    @Test
    @DisplayName("Cambiar el celular lo deja sin verificar, saca la cuenta de ACTIVA y envía un código (RF-011)")
    void cambiarCelular() {
        negocio.cambiarCelular(idUsuario, new UsuarioDominio.Builder().numeroCelular("3119998877").build());

        assertEquals("3119998877", usuario().getNumeroCelular());
        assertFalse(usuario().isCelularVerificado());
        assertEquals("PENDIENTE_VERIFICACION", usuario().getEstadoCuenta());
        assertEquals("3119998877", daoFactory.getCodigoVerificacionDAOFalso().getCodigos().getFirst().getDestino());
        assertTrue(negocio.consultarUltimoCodigoEnviado(idUsuario).contains(daoFactory.getCodigoVerificacionDAOFalso().getCodigos().getFirst().getCodigo()));
    }

    @Test
    @DisplayName("El aviso de demostración muestra el código pendiente aunque las notificaciones tengan la misma hora")
    void avisoMuestraElCodigoPendiente() {
        negocio.cambiarCelular(idUsuario, new UsuarioDominio.Builder().numeroCelular("3119998877").build());
        new VerificacionNegocioImpl(daoFactory).reenviarCodigoCelular(idUsuario);
        Collections.reverse(daoFactory.getNotificacionDAOFalso().getNotificaciones());

        var codigoPendiente = daoFactory.getCodigoVerificacionDAOFalso().getCodigos().getFirst();

        assertFalse(codigoPendiente.isUsado());
        assertTrue(negocio.consultarUltimoCodigoEnviado(idUsuario).contains(codigoPendiente.getCodigo()));
    }

    @Test
    @DisplayName("No se cambia el celular por el mismo, por uno inválido ni por uno de otra persona (RN-074)")
    void cambiarCelularRechazado() {
        daoFactory.registrarUsuarioConContacto("luis@gmail.com", "3205556677");
        var mismo = new UsuarioDominio.Builder().numeroCelular(CELULAR).build();
        var invalido = new UsuarioDominio.Builder().numeroCelular("123").build();
        var ajeno = new UsuarioDominio.Builder().numeroCelular("3205556677").build();

        assertRechazo(CatalogoMensajes.Identidad.CELULAR_IGUAL_AL_ACTUAL, () -> negocio.cambiarCelular(idUsuario, mismo));
        assertRechazo(CatalogoMensajes.Identidad.CELULAR_FORMATO_INVALIDO, () -> negocio.cambiarCelular(idUsuario, invalido));
        assertRechazo(CatalogoMensajes.Identidad.CELULAR_YA_REGISTRADO, () -> negocio.cambiarCelular(idUsuario, ajeno));
        assertTrue(usuario().isCelularVerificado());
    }

    @Test
    @DisplayName("Cambiar el correo lo deja sin verificar hasta entrar con Google con el correo nuevo (RF-010)")
    void cambiarCorreo() {
        negocio.cambiarCorreo(idUsuario, new UsuarioDominio.Builder().correoInstitucional(" Ana.Nueva@UCO.NET.CO ").build());

        assertEquals("ana.nueva@uco.net.co", usuario().getCorreoInstitucional());
        assertFalse(usuario().isCorreoVerificado());
        assertEquals("PENDIENTE_VERIFICACION", usuario().getEstadoCuenta());
        assertEquals("CorreoCambiado", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());
    }

    @Test
    @DisplayName("No se cambia el correo por el mismo ni por uno de un dominio no permitido (RN-071, RN-073)")
    void cambiarCorreoRechazado() {
        var mismo = new UsuarioDominio.Builder().correoInstitucional("ANA.PEREZ@gmail.com").build();
        var otroDominio = new UsuarioDominio.Builder().correoInstitucional("ana@hotmail.com").build();

        assertRechazo(CatalogoMensajes.Identidad.CORREO_IGUAL_AL_ACTUAL, () -> negocio.cambiarCorreo(idUsuario, mismo));
        assertThrows(KudosExcepcion.class, () -> negocio.cambiarCorreo(idUsuario, otroDominio));
        assertTrue(usuario().isCorreoVerificado());
    }
}
