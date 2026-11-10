package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.CanalVerificacion;
import co.edu.uco.kudos.dominio.CodigoVerificacionDominio;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.EstadoFacultad;
import co.edu.uco.kudos.dominio.EstadoSolicitud;
import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dominio.NotificacionDominio;
import co.edu.uco.kudos.dominio.ParametroGlobalDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.dominio.TipoNotificacion;
import co.edu.uco.kudos.dominio.TipoParametro;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.CodigoVerificacionDto;
import co.edu.uco.kudos.entidad.CodigoVerificacionEntidad;
import co.edu.uco.kudos.entidad.SolicitudRegistroEntidad;
import co.edu.uco.kudos.dto.CuentaDto;
import co.edu.uco.kudos.dto.OpcionesRegistroDto;
import co.edu.uco.kudos.dto.RegistroDto;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.CuentaDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.FacultadDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ProgramaAcademicoDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.RegistroDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.CodigoVerificacionEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.FacultadEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.NotificacionEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ParametroGlobalEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ProgramaAcademicoEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.SolicitudRegistroEntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Ensambladores y DTO de identidad, organización y notificaciones")
class AssemblersIdentidadTest {

    private final FacultadDominio facultad = new FacultadDominio.Builder()
            .id(UtilId.generarId()).nombre("Ingeniería").estado(EstadoFacultad.ACTIVA).fechaCreacion(UtilFecha.fechaActual()).build();
    private final ProgramaAcademicoDominio programa = new ProgramaAcademicoDominio.Builder()
            .id(UtilId.generarId()).nombre("Sistemas").facultad(facultad).activo(true).fechaCreacion(UtilFecha.fechaActual()).build();
    private final UsuarioDominio usuario = new UsuarioDominio.Builder()
            .id(UtilId.generarId()).nombres("Ana").apellidos("Pérez").correoInstitucional("ana@gmail.com")
            .correoVerificado(true).numeroCelular("3001234567").celularVerificado(false)
            .estadoCuenta(EstadoCuenta.PENDIENTE_APROBACION).fechaRegistro(UtilFecha.fechaActual()).build();

    @Test
    @DisplayName("Facultad y Programa van y vuelven entre dominio y entidad, y entre dominio y DTO")
    void organizacion() {
        var facultadDeVuelta = FacultadEntidadAssembler.getInstance().convertirADominio(FacultadEntidadAssembler.getInstance().convertirAEntidad(facultad));
        var programaDeVuelta = ProgramaAcademicoEntidadAssembler.getInstance().convertirADominio(ProgramaAcademicoEntidadAssembler.getInstance().convertirAEntidad(programa));
        var facultadDto = FacultadDtoAssembler.getInstance().convertirADTO(facultad);
        var programaDto = ProgramaAcademicoDtoAssembler.getInstance().convertirADTO(programa);

        assertEquals(EstadoFacultad.ACTIVA, facultadDeVuelta.getEstado());
        assertEquals(facultad.getId(), programaDeVuelta.getFacultad().getId());
        assertEquals("ACTIVA", facultadDto.getEstado());
        assertEquals("Ingeniería", programaDto.getNombreFacultad());
        assertEquals(facultad.getId(), programaDto.getIdFacultad());
        assertTrue(programaDto.isActivo());
        assertEquals(EstadoFacultad.ACTIVA, FacultadDtoAssembler.getInstance().convertirADominio(facultadDto).getEstado());
        assertEquals(facultad.getId(), ProgramaAcademicoDtoAssembler.getInstance().convertirADominio(programaDto).getFacultad().getId());
        assertEquals(1, FacultadEntidadAssembler.getInstance().convertirADominio(List.of(FacultadEntidadAssembler.getInstance().convertirAEntidad(facultad))).size());
        assertEquals(1, ProgramaAcademicoEntidadAssembler.getInstance().convertirADominio(List.of(ProgramaAcademicoEntidadAssembler.getInstance().convertirAEntidad(programa))).size());
        assertEquals(1, FacultadDtoAssembler.getInstance().convertirADTO(List.of(facultad)).size());
        assertEquals(1, ProgramaAcademicoDtoAssembler.getInstance().convertirADTO(List.of(programa)).size());
    }

    @Test
    @DisplayName("Solicitud, código, notificación y parámetro conservan sus datos al pasar por la entidad")
    void identidad() {
        var solicitud = new SolicitudRegistroDominio.Builder()
                .id(UtilId.generarId()).solicitante(usuario).rolSolicitado(Rol.DOCENTE).programa(programa)
                .estado(EstadoSolicitud.PENDIENTE).fechaSolicitud(UtilFecha.fechaActual()).build();
        var codigo = new CodigoVerificacionDominio.Builder()
                .id(UtilId.generarId()).usuario(usuario).canal(CanalVerificacion.CELULAR).destino("3001234567").codigo("123456")
                .fechaGeneracion(UtilFecha.fechaActual()).fechaExpiracion(UtilFecha.fechaActual().plusMinutes(15)).intentosFallidos(2).build();
        var notificacion = new NotificacionDominio.Builder()
                .id(UtilId.generarId()).destinatario(usuario).tipo(TipoNotificacion.CODIGO_VERIFICACION).mensaje("Tu código es 123456")
                .fechaCreacion(UtilFecha.fechaActual()).build();
        var parametro = new ParametroGlobalDominio.Builder()
                .id(UtilId.generarId()).clave(ParametroGlobalDominio.MAX_INTENTOS_VERIFICACION).valor("5").tipoDato(TipoParametro.ENTERO).build();

        var solicitudDeVuelta = SolicitudRegistroEntidadAssembler.getInstance().convertirADominio(SolicitudRegistroEntidadAssembler.getInstance().convertirAEntidad(solicitud));
        var codigoDeVuelta = CodigoVerificacionEntidadAssembler.getInstance().convertirADominio(CodigoVerificacionEntidadAssembler.getInstance().convertirAEntidad(codigo));
        var notificacionDeVuelta = NotificacionEntidadAssembler.getInstance().convertirADominio(NotificacionEntidadAssembler.getInstance().convertirAEntidad(notificacion));
        var parametroDeVuelta = ParametroGlobalEntidadAssembler.getInstance().convertirADominio(ParametroGlobalEntidadAssembler.getInstance().convertirAEntidad(parametro));

        assertEquals(Rol.DOCENTE, solicitudDeVuelta.getRolSolicitado());
        assertEquals(programa.getId(), solicitudDeVuelta.getPrograma().getId());
        assertEquals(usuario.getId(), solicitudDeVuelta.getSolicitante().getId());
        assertEquals(CanalVerificacion.CELULAR, codigoDeVuelta.getCanal());
        assertEquals(2, codigoDeVuelta.getIntentosFallidos());
        assertEquals(TipoNotificacion.CODIGO_VERIFICACION, notificacionDeVuelta.getTipo());
        assertEquals(usuario.getId(), notificacionDeVuelta.getDestinatario().getId());
        assertEquals(5, parametroDeVuelta.valorComoEntero());
        assertEquals(1, SolicitudRegistroEntidadAssembler.getInstance().convertirADominio(List.of(SolicitudRegistroEntidadAssembler.getInstance().convertirAEntidad(solicitud))).size());
        assertEquals(1, CodigoVerificacionEntidadAssembler.getInstance().convertirADominio(List.of(CodigoVerificacionEntidadAssembler.getInstance().convertirAEntidad(codigo))).size());
        assertEquals(1, NotificacionEntidadAssembler.getInstance().convertirADominio(List.of(NotificacionEntidadAssembler.getInstance().convertirAEntidad(notificacion))).size());
        assertEquals(1, ParametroGlobalEntidadAssembler.getInstance().convertirADominio(List.of(ParametroGlobalEntidadAssembler.getInstance().convertirAEntidad(parametro))).size());
    }

    @Test
    @DisplayName("Los ensambladores aceptan nulos y devuelven objetos con valores por defecto")
    void nulos() {
        assertTrue(UtilId.esValorDefecto(SolicitudRegistroEntidadAssembler.getInstance().convertirADominio((SolicitudRegistroEntidad) null).getId()));
        assertTrue(UtilId.esValorDefecto(CodigoVerificacionEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
        assertTrue(UtilId.esValorDefecto(NotificacionEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
        assertTrue(UtilId.esValorDefecto(ParametroGlobalEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
        assertTrue(UtilId.esValorDefecto(FacultadEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
        assertTrue(UtilId.esValorDefecto(ProgramaAcademicoEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
        assertTrue(CodigoVerificacionEntidadAssembler.getInstance().convertirADominio((List<CodigoVerificacionEntidad>) null).isEmpty());
        assertTrue(RegistroDtoAssembler.getInstance().convertirADTO((List<SolicitudRegistroDominio>) null).isEmpty());
        assertTrue(CuentaDtoAssembler.getInstance().convertirADTO((List<UsuarioDominio>) null).isEmpty());
        assertTrue(UtilId.esValorDefecto(FacultadDtoAssembler.getInstance().convertirADominio(null).getId()));
        assertTrue(UtilId.esValorDefecto(ProgramaAcademicoDtoAssembler.getInstance().convertirADominio(null).getId()));
    }

    @Test
    @DisplayName("Registro y cuenta: el DTO limpia espacios y el ensamblador lleva los datos al dominio")
    void registroYCuenta() {
        var registro = new RegistroDto();
        registro.setNombres(" Ana ");
        registro.setApellidos(" Pérez ");
        registro.setNumeroCelular(" 3001234567 ");
        registro.setRolSolicitado(" DOCENTE ");
        registro.setIdPrograma(programa.getId());
        registro.setIdFacultad(null);

        var solicitud = RegistroDtoAssembler.getInstance().convertirADominio(registro);
        var registroDeVuelta = RegistroDtoAssembler.getInstance().convertirADTO(solicitud);

        assertEquals("Ana", solicitud.getSolicitante().getNombres());
        assertEquals("3001234567", solicitud.getSolicitante().getNumeroCelular());
        assertEquals(programa.getId(), solicitud.getPrograma().getId());
        assertTrue(UtilId.esValorDefecto(solicitud.getFacultad().getId()));
        assertEquals("DOCENTE", registro.getRolSolicitado());
        assertEquals("Pérez", registroDeVuelta.getApellidos());
        assertEquals(programa.getId(), registroDeVuelta.getIdPrograma());
        assertTrue(UtilId.esValorDefecto(registroDeVuelta.getIdFacultad()));
        assertEquals(1, RegistroDtoAssembler.getInstance().convertirADTO(List.of(solicitud)).size());

        var cuenta = CuentaDtoAssembler.getInstance().convertirADTO(usuario);
        cuenta.setRol(" DOCENTE ");
        cuenta.setRolSolicitado("DOCENTE");
        cuenta.setEstadoSolicitud("PENDIENTE");
        cuenta.setMotivoRechazo("");
        cuenta.setUltimoCodigoEnviado(" Tu código es 123456 ");
        cuenta.setAprobada(false);
        cuenta.setCorreoVerificado(true);
        cuenta.setCelularVerificado(false);

        assertEquals("ana@gmail.com", cuenta.getCorreoInstitucional());
        assertEquals("PENDIENTE_APROBACION", cuenta.getEstadoCuenta());
        assertFalse(cuenta.getMensajeEstado().isEmpty());
        assertEquals("DOCENTE", cuenta.getRol());
        assertEquals("DOCENTE", cuenta.getRolSolicitado());
        assertEquals("PENDIENTE", cuenta.getEstadoSolicitud());
        assertTrue(cuenta.getMotivoRechazo().isEmpty());
        assertEquals("Tu código es 123456", cuenta.getUltimoCodigoEnviado());
        assertFalse(cuenta.isAprobada());
        assertTrue(cuenta.isCorreoVerificado());
        assertFalse(cuenta.isCelularVerificado());
        assertEquals(usuario.getId(), cuenta.getId());
        assertEquals("Ana", cuenta.getNombres());
        assertEquals("Pérez", cuenta.getApellidos());
        assertEquals("3001234567", cuenta.getNumeroCelular());

        var usuarioDeVuelta = CuentaDtoAssembler.getInstance().convertirADominio(cuenta);
        assertEquals("3001234567", usuarioDeVuelta.getNumeroCelular());
        assertEquals(1, CuentaDtoAssembler.getInstance().convertirADTO(List.of(usuario)).size());

        var codigo = new CodigoVerificacionDto();
        codigo.setCodigo(" 123456 ");
        assertEquals("123456", codigo.getCodigo());

        var opciones = new OpcionesRegistroDto();
        opciones.setRoles(null);
        opciones.setFacultades(FacultadDtoAssembler.getInstance().convertirADTO(List.of(facultad)));
        opciones.setProgramas(ProgramaAcademicoDtoAssembler.getInstance().convertirADTO(List.of(programa)));
        assertTrue(opciones.getRoles().isEmpty());
        assertEquals(1, opciones.getFacultades().size());
        assertEquals(1, opciones.getProgramas().size());
    }
}
