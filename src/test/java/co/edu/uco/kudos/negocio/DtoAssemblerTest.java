package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.dto.CategoriaDto;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.CategoriaDtoAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Ensambladores DTO ↔ dominio")
class DtoAssemblerTest {

    @Test
    @DisplayName("Categoría: conserva todos los datos en ambos sentidos")
    void categoria() {
        var fecha = UtilFecha.fechaActual();
        var dominio = new CategoriaDominio.Builder().id(UtilId.generarId()).nombre("Liderazgo")
                .descripcion("Reconoce a quien inspira.").activa(false).fechaCreacion(fecha).build();

        var dto = CategoriaDtoAssembler.getInstance().convertirADTO(dominio);
        var deVuelta = CategoriaDtoAssembler.getInstance().convertirADominio(dto);

        assertEquals(dominio.getId(), dto.getId());
        assertEquals("Liderazgo", deVuelta.getNombre());
        assertEquals("Reconoce a quien inspira.", deVuelta.getDescripcion());
        assertFalse(deVuelta.isActiva());
        assertEquals(fecha, deVuelta.getFechaCreacion());
        assertEquals(1, CategoriaDtoAssembler.getInstance().convertirADTO(List.of(dominio)).size());
    }

    @Test
    @DisplayName("El DTO limpia espacios y los nulos se convierten en valores por defecto, como en Sigma")
    void valoresPorDefecto() {
        var dto = new CategoriaDto();
        dto.setNombre("  Liderazgo  ");
        dto.setId(null);
        dto.setFechaCreacion(null);

        assertEquals("Liderazgo", dto.getNombre());
        assertTrue(UtilId.esValorDefecto(dto.getId()));
        assertTrue(UtilFecha.esValorDefecto(dto.getFechaCreacion()));
        assertTrue(dto.isActiva());
        assertTrue(UtilId.esValorDefecto(CategoriaDtoAssembler.getInstance().convertirADTO((CategoriaDominio) null).getId()));
        assertTrue(UtilId.esValorDefecto(CategoriaDtoAssembler.getInstance().convertirADominio(null).getId()));
        assertTrue(CategoriaDtoAssembler.getInstance().convertirADTO((List<CategoriaDominio>) null).isEmpty());
    }
}
