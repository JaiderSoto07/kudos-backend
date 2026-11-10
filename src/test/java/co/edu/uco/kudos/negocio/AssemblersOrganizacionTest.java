package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.TipoVinculo;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dominio.VinculacionProgramaDominio;
import co.edu.uco.kudos.dto.CargoDto;
import co.edu.uco.kudos.dto.DesvinculacionDto;
import co.edu.uco.kudos.dto.FacultadDto;
import co.edu.uco.kudos.dto.ProgramaAcademicoDto;
import co.edu.uco.kudos.dto.VinculoDto;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.CargoDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.VinculoDtoAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Ensambladores y DTO de organización: cargos y vínculos")
class AssemblersOrganizacionTest {

    private final UsuarioDominio persona = new UsuarioDominio.Builder()
            .id(UtilId.generarId()).nombres("Juan Pablo").apellidos("Zuluaga").correoInstitucional("jp@gmail.com").build();
    private final FacultadDominio facultad = new FacultadDominio.Builder().id(UtilId.generarId()).nombre("Ingeniería").build();
    private final ProgramaAcademicoDominio programa = new ProgramaAcademicoDominio.Builder()
            .id(UtilId.generarId()).nombre("Sistemas").facultad(facultad).build();

    @Test
    @DisplayName("Un cargo cerrado llega con persona, ámbito, fechas, motivo y vigencia")
    void cargo() {
        var asignacion = new AsignacionRolDominio.Builder()
                .id(UtilId.generarId()).usuario(persona).rol(Rol.COORDINADOR_PROGRAMA).programa(programa)
                .fechaInicio(UtilFecha.fechaActual().minusDays(30)).build();
        asignacion.cerrar("Terminó su periodo.");

        var dto = CargoDtoAssembler.getInstance().convertirADTO(asignacion);
        var deVuelta = CargoDtoAssembler.getInstance().convertirADominio(dto);

        assertEquals("Juan Pablo Zuluaga", dto.getNombrePersona());
        assertEquals("jp@gmail.com", dto.getCorreo());
        assertEquals(persona.getId(), dto.getIdUsuario());
        assertEquals("COORDINADOR_PROGRAMA", dto.getRol());
        assertEquals(programa.getId(), dto.getIdPrograma());
        assertEquals("Sistemas", dto.getNombrePrograma());
        assertTrue(UtilId.esValorDefecto(dto.getIdFacultad()));
        assertTrue(dto.getNombreFacultad().isEmpty());
        assertFalse(dto.isVigente());
        assertEquals("Terminó su periodo.", dto.getMotivoFin());
        assertFalse(UtilFecha.esValorDefecto(dto.getFechaInicio()));
        assertFalse(UtilFecha.esValorDefecto(dto.getFechaFin()));
        assertEquals(Rol.COORDINADOR_PROGRAMA, deVuelta.getRol());
        assertFalse(deVuelta.estaVigente());
        assertEquals(1, CargoDtoAssembler.getInstance().convertirADTO(List.of(asignacion)).size());
        assertTrue(CargoDtoAssembler.getInstance().convertirADTO((List<AsignacionRolDominio>) null).isEmpty());
        assertTrue(UtilId.esValorDefecto(CargoDtoAssembler.getInstance().convertirADominio((CargoDto) null).getUsuario().getId()));
    }

    @Test
    @DisplayName("Un vínculo llega con la persona, el Programa y su vigencia; el pedido de vínculo trae correo y tipo")
    void vinculo() {
        var vinculo = new VinculacionProgramaDominio.Builder()
                .id(UtilId.generarId()).usuario(persona).programa(programa).tipoVinculo(TipoVinculo.DOCENTE)
                .fechaInicio(UtilFecha.fechaActual()).build();

        var dto = VinculoDtoAssembler.getInstance().convertirADTO(vinculo);

        assertEquals(vinculo.getId(), dto.getId());
        assertEquals(persona.getId(), dto.getIdUsuario());
        assertEquals("Juan Pablo Zuluaga", dto.getNombrePersona());
        assertEquals("DOCENTE", dto.getTipoVinculo());
        assertEquals("Sistemas", dto.getNombrePrograma());
        assertEquals(programa.getId(), dto.getIdPrograma());
        assertTrue(dto.isVigente());
        assertTrue(dto.getMotivoDesvinculacion().isEmpty());
        assertTrue(UtilFecha.esValorDefecto(dto.getFechaFin()));
        assertFalse(UtilFecha.esValorDefecto(dto.getFechaInicio()));

        var pedido = new VinculoDto();
        pedido.setCorreo(" mateo@gmail.com ");
        pedido.setTipoVinculo(" ESTUDIANTE ");
        var dominio = VinculoDtoAssembler.getInstance().convertirADominio(pedido);
        assertEquals("mateo@gmail.com", dominio.getUsuario().getCorreoInstitucional());
        assertEquals(TipoVinculo.ESTUDIANTE, dominio.getTipoVinculo());
        assertEquals(1, VinculoDtoAssembler.getInstance().convertirADTO(List.of(vinculo)).size());
        assertTrue(VinculoDtoAssembler.getInstance().convertirADTO((List<VinculacionProgramaDominio>) null).isEmpty());
        assertTrue(UtilId.esValorDefecto(VinculoDtoAssembler.getInstance().convertirADominio((VinculoDto) null).getId()));
    }

    @Test
    @DisplayName("Facultad y Programa llevan su coordinador y la cantidad de Programas; el motivo se limpia")
    void camposNuevos() {
        var facultadDto = new FacultadDto();
        facultadDto.setCantidadProgramas(-3);
        facultadDto.setNombreCoordinador(" Luisa Ospina ");
        var programaDto = new ProgramaAcademicoDto();
        programaDto.setNombreCoordinador(" Juan Pablo ");
        var desvinculacion = new DesvinculacionDto();
        desvinculacion.setMotivo("  Terminó sus estudios.  ");

        assertEquals(0, facultadDto.getCantidadProgramas());
        assertEquals("Luisa Ospina", facultadDto.getNombreCoordinador());
        assertEquals("Juan Pablo", programaDto.getNombreCoordinador());
        assertEquals("Terminó sus estudios.", desvinculacion.getMotivo());
    }
}
