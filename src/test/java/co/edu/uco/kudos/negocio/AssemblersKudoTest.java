package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.KudoDto;
import co.edu.uco.kudos.dto.PaginaKudosDto;
import co.edu.uco.kudos.dto.PersonaDto;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.KudoDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.PersonaDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.KudoEntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Ensambladores y DTO de Kudos y personas")
class AssemblersKudoTest {

    private final UsuarioDominio ana = new UsuarioDominio.Builder().id(UtilId.generarId()).nombres("Ana").apellidos("Pérez").correoInstitucional("ana@gmail.com").build();
    private final UsuarioDominio luis = new UsuarioDominio.Builder().id(UtilId.generarId()).nombres("Luis").apellidos("Gómez").build();
    private final CategoriaDominio liderazgo = new CategoriaDominio.Builder().id(UtilId.generarId()).nombre("Liderazgo").activa(true).build();

    private KudoDominio kudoPublicado() {
        var kudo = new KudoDominio.Builder()
                .id(UtilId.generarId()).autor(ana).destinatario(luis).categoria(liderazgo).contenido("Gracias por todo.")
                .fechaCreacion(UtilFecha.fechaActual()).estado(EstadoContenido.CREADO).cantidadVisualizaciones(4).build();
        kudo.publicar();
        kudo.actualizarResumenCalificaciones(List.of(5, 4));
        return kudo;
    }

    @Test
    @DisplayName("El Kudo conserva autor, destinatario, categoría, estado y contadores al pasar por la entidad y el DTO")
    void kudoCompleto() {
        var original = kudoPublicado();
        var deVuelta = KudoEntidadAssembler.getInstance().convertirADominio(KudoEntidadAssembler.getInstance().convertirAEntidad(original));
        var dto = KudoDtoAssembler.getInstance().convertirADTO(deVuelta);

        assertEquals(EstadoContenido.VISIBLE, deVuelta.getEstado());
        assertEquals("Ana Pérez", dto.getNombreAutor());
        assertEquals(ana.getId(), dto.getIdAutor());
        assertEquals("Luis Gómez", dto.getNombreDestinatario());
        assertEquals(luis.getId(), dto.getIdDestinatario());
        assertEquals("Liderazgo", dto.getNombreCategoria());
        assertEquals(liderazgo.getId(), dto.getIdCategoria());
        assertEquals("Gracias por todo.", dto.getContenido());
        assertEquals("VISIBLE", dto.getEstado());
        assertEquals(4, dto.getCantidadVisualizaciones());
        assertEquals(2, dto.getCantidadCalificaciones());
        assertEquals(new BigDecimal("4.50"), dto.getPromedioCalificacion());
        assertEquals(original.getId(), dto.getId());
        assertTrue(dto.getFechaCreacion().isAfter(UtilFecha.FECHA_HORA_POR_DEFECTO));

        var pedido = KudoDtoAssembler.getInstance().convertirADominio(dto);
        assertEquals(luis.getId(), pedido.getDestinatario().getId());
        assertEquals(liderazgo.getId(), pedido.getCategoria().getId());
        assertEquals(1, KudoDtoAssembler.getInstance().convertirADTO(List.of(original)).size());
        assertEquals(1, KudoEntidadAssembler.getInstance().convertirADominio(List.of(new KudoEntidad.Builder().build())).size());
        assertTrue(KudoDtoAssembler.getInstance().convertirADTO((List<KudoDominio>) null).isEmpty());
        assertTrue(UtilId.esValorDefecto(KudoDtoAssembler.getInstance().convertirADominio((KudoDto) null).getId()));
        assertTrue(UtilId.esValorDefecto(KudoEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
    }

    @Test
    @DisplayName("La persona y la página de Kudos se arman con valores seguros")
    void personaYPagina() {
        var persona = PersonaDtoAssembler.getInstance().convertirADTO(ana);
        assertEquals("Ana Pérez", persona.getNombre());
        assertEquals("ana@gmail.com", persona.getCorreo());
        assertEquals(ana.getId(), persona.getId());
        assertEquals(ana.getId(), PersonaDtoAssembler.getInstance().convertirADominio(persona).getId());
        assertEquals(1, PersonaDtoAssembler.getInstance().convertirADTO(List.of(ana)).size());
        assertTrue(PersonaDtoAssembler.getInstance().convertirADTO((List<UsuarioDominio>) null).isEmpty());
        assertTrue(UtilId.esValorDefecto(PersonaDtoAssembler.getInstance().convertirADominio((PersonaDto) null).getId()));

        var pagina = new PaginaKudosDto();
        pagina.setPagina(0);
        pagina.setTotalPaginas(-1);
        pagina.setTotalKudos(-5);
        pagina.setKudos(null);
        assertEquals(1, pagina.getPagina());
        assertEquals(0, pagina.getTotalPaginas());
        assertEquals(0, pagina.getTotalKudos());
        assertTrue(pagina.getKudos().isEmpty());

        var dto = new KudoDto();
        dto.setPromedioCalificacion(null);
        dto.setCantidadVisualizaciones(-1);
        dto.setCantidadCalificaciones(-1);
        assertEquals(BigDecimal.ZERO, dto.getPromedioCalificacion());
        assertEquals(0, dto.getCantidadVisualizaciones());
        assertEquals(0, dto.getCantidadCalificaciones());
    }
}
