package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.EstadoSolicitud;
import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.dominio.TipoVinculo;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dominio.VinculacionProgramaDominio;
import co.edu.uco.kudos.dto.AutoridadDto;
import co.edu.uco.kudos.dto.RechazoDto;
import co.edu.uco.kudos.dto.SolicitudDto;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.entidad.VinculacionProgramaEntidad;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.AutoridadDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.SolicitudDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.AsignacionRolEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.VinculacionProgramaEntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Ensambladores y DTO de aprobaciones, asignaciones de rol y vínculos")
class AssemblersAprobacionTest {

    private final UsuarioDominio usuario = new UsuarioDominio.Builder()
            .id(UtilId.generarId()).nombres("Sofía").apellidos("Henao").correoInstitucional("sofia@gmail.com").numeroCelular("3001234567").build();
    private final FacultadDominio facultad = new FacultadDominio.Builder().id(UtilId.generarId()).nombre("Ingeniería").build();
    private final ProgramaAcademicoDominio programa = new ProgramaAcademicoDominio.Builder()
            .id(UtilId.generarId()).nombre("Ingeniería de Sistemas").facultad(facultad).build();

    @Test
    @DisplayName("Asignación de rol y vínculo conservan sus datos entre dominio y entidad")
    void asignacionYVinculo() {
        var asignacion = new AsignacionRolDominio.Builder()
                .id(UtilId.generarId()).usuario(usuario).rol(Rol.COORDINADOR_PROGRAMA).programa(programa)
                .fechaInicio(UtilFecha.fechaActual()).build();
        var vinculo = new VinculacionProgramaDominio.Builder()
                .id(UtilId.generarId()).usuario(usuario).programa(programa).tipoVinculo(TipoVinculo.DOCENTE)
                .fechaInicio(UtilFecha.fechaActual()).build();

        var asignacionDeVuelta = AsignacionRolEntidadAssembler.getInstance().convertirADominio(AsignacionRolEntidadAssembler.getInstance().convertirAEntidad(asignacion));
        var vinculoDeVuelta = VinculacionProgramaEntidadAssembler.getInstance().convertirADominio(VinculacionProgramaEntidadAssembler.getInstance().convertirAEntidad(vinculo));

        assertEquals(Rol.COORDINADOR_PROGRAMA, asignacionDeVuelta.getRol());
        assertEquals(programa.getId(), asignacionDeVuelta.getPrograma().getId());
        assertTrue(asignacionDeVuelta.estaVigente());
        assertEquals(TipoVinculo.DOCENTE, vinculoDeVuelta.getTipoVinculo());
        assertEquals(usuario.getId(), vinculoDeVuelta.getUsuario().getId());
        assertEquals(1, AsignacionRolEntidadAssembler.getInstance().convertirADominio(List.of(new AsignacionRolEntidad.Builder().build())).size());
        assertEquals(1, VinculacionProgramaEntidadAssembler.getInstance().convertirADominio(List.of(new VinculacionProgramaEntidad.Builder().build())).size());
        assertTrue(UtilId.esValorDefecto(AsignacionRolEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
        assertTrue(UtilId.esValorDefecto(VinculacionProgramaEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
        assertTrue(AsignacionRolEntidadAssembler.getInstance().convertirADominio((List<AsignacionRolEntidad>) null).isEmpty());
    }

    @Test
    @DisplayName("La solicitud llega a la pantalla con el nombre del solicitante, el rol y el Programa")
    void solicitudDto() {
        var solicitud = new SolicitudRegistroDominio.Builder()
                .id(UtilId.generarId()).solicitante(usuario).rolSolicitado(Rol.ESTUDIANTE).programa(programa)
                .estado(EstadoSolicitud.RECHAZADA).fechaSolicitud(UtilFecha.fechaActual()).motivoRechazo("No pertenece al Programa.").build();

        var dto = SolicitudDtoAssembler.getInstance().convertirADTO(solicitud);
        var deVuelta = SolicitudDtoAssembler.getInstance().convertirADominio(dto);

        assertEquals("Sofía Henao", dto.getNombreSolicitante());
        assertEquals("sofia@gmail.com", dto.getCorreoSolicitante());
        assertEquals(usuario.getId(), dto.getIdSolicitante());
        assertEquals("ESTUDIANTE", dto.getRolSolicitado());
        assertEquals("Ingeniería de Sistemas", dto.getNombrePrograma());
        assertTrue(dto.getNombreFacultad().isEmpty());
        assertEquals("RECHAZADA", dto.getEstado());
        assertEquals("No pertenece al Programa.", dto.getMotivoRechazo());
        assertFalse(UtilFecha.esValorDefecto(dto.getFechaSolicitud()));
        assertEquals(solicitud.getId(), dto.getId());
        assertEquals(EstadoSolicitud.RECHAZADA, deVuelta.getEstado());
        assertEquals(Rol.ESTUDIANTE, deVuelta.getRolSolicitado());
        assertEquals(1, SolicitudDtoAssembler.getInstance().convertirADTO(List.of(solicitud)).size());
        assertTrue(SolicitudDtoAssembler.getInstance().convertirADTO((List<SolicitudRegistroDominio>) null).isEmpty());
        assertTrue(UtilId.esValorDefecto(SolicitudDtoAssembler.getInstance().convertirADominio((SolicitudDto) null).getId()));
    }

    @Test
    @DisplayName("Los datos de una autoridad y el motivo de rechazo se limpian de espacios")
    void autoridadYRechazo() {
        var dto = new AutoridadDto();
        dto.setRol(" RECTOR ");
        dto.setNombres(" María ");
        dto.setApellidos(" Gómez ");
        dto.setCorreoInstitucional(" rectora@gmail.com ");
        dto.setNumeroCelular(" 3001112233 ");

        var dominio = AutoridadDtoAssembler.getInstance().convertirADominio(dto);
        var deVuelta = AutoridadDtoAssembler.getInstance().convertirADTO(dominio);

        assertEquals("RECTOR", dto.getRol());
        assertEquals("María", dominio.getNombres());
        assertEquals("rectora@gmail.com", deVuelta.getCorreoInstitucional());
        assertEquals("3001112233", deVuelta.getNumeroCelular());
        assertEquals("Gómez", deVuelta.getApellidos());
        assertEquals(1, AutoridadDtoAssembler.getInstance().convertirADTO(List.of(dominio)).size());
        assertTrue(AutoridadDtoAssembler.getInstance().convertirADTO((List<UsuarioDominio>) null).isEmpty());

        var rechazo = new RechazoDto();
        rechazo.setMotivo("  No pertenece al Programa.  ");
        assertEquals("No pertenece al Programa.", rechazo.getMotivo());
    }
}
