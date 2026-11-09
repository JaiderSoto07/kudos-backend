package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.AuditoriaEntidad;
import co.edu.uco.kudos.entidad.CategoriaEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.AuditoriaEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.CategoriaEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Ensambladores entidad ↔ dominio")
class EntidadAssemblerTest {

    @Test
    @DisplayName("Categoría: conserva todos los datos en ambos sentidos")
    void categoria() {
        var fecha = UtilFecha.fechaActual();
        var dominio = new CategoriaDominio.Builder().id(UtilId.generarId()).nombre("Liderazgo")
                .descripcion("Reconoce a quien inspira.").activa(false).fechaCreacion(fecha).build();

        var entidad = CategoriaEntidadAssembler.getInstance().convertirAEntidad(dominio);
        var deVuelta = CategoriaEntidadAssembler.getInstance().convertirADominio(entidad);

        assertEquals(dominio.getId(), deVuelta.getId());
        assertEquals("Liderazgo", deVuelta.getNombre());
        assertEquals("Reconoce a quien inspira.", deVuelta.getDescripcion());
        assertFalse(deVuelta.isActiva());
        assertEquals(fecha, deVuelta.getFechaCreacion());
        assertEquals(1, CategoriaEntidadAssembler.getInstance().convertirADominio(List.of(entidad)).size());
    }

    @Test
    @DisplayName("Usuario: convierte el estado de la cuenta entre texto y enum")
    void usuario() {
        var dominio = new UsuarioDominio.Builder().id(UtilId.generarId()).nombres("Ana").apellidos("Giraldo")
                .correoInstitucional("agiraldo@uco.edu.co").correoVerificado(true).numeroCelular("3001234567")
                .celularVerificado(true).contrasena("hash").aprobada(true).estadoCuenta(EstadoCuenta.ACTIVA)
                .fechaRegistro(UtilFecha.fechaActual()).build();

        var entidad = UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominio);
        assertEquals("ACTIVA", entidad.getEstadoCuenta());

        var deVuelta = UsuarioEntidadAssembler.getInstance().convertirADominio(entidad);
        assertEquals(EstadoCuenta.ACTIVA, deVuelta.getEstadoCuenta());
        assertEquals("agiraldo@uco.edu.co", deVuelta.getCorreoInstitucional());
        assertTrue(deVuelta.isCorreoVerificado());
        assertEquals(1, UsuarioEntidadAssembler.getInstance().convertirADominio(List.of(entidad)).size());
    }

    @Test
    @DisplayName("Auditoría: convierte también al actor")
    void auditoria() {
        var idActor = UtilId.generarId();
        var dominio = new AuditoriaDominio.Builder().id(UtilId.generarId()).fechaHora(UtilFecha.fechaActual())
                .actor(new UsuarioDominio.Builder().id(idActor).build()).accion("CategoriaCreada")
                .tipoObjeto("Categoria").idObjeto(UtilId.generarId()).estadoAnterior("").estadoNuevo("Liderazgo")
                .motivo("").detalle("").build();

        var entidad = AuditoriaEntidadAssembler.getInstance().convertirAEntidad(dominio);
        var deVuelta = AuditoriaEntidadAssembler.getInstance().convertirADominio(entidad);

        assertEquals(idActor, entidad.getActor().getId());
        assertEquals(idActor, deVuelta.getActor().getId());
        assertEquals("CategoriaCreada", deVuelta.getAccion());
        assertEquals("Liderazgo", deVuelta.getEstadoNuevo());
        assertEquals(1, AuditoriaEntidadAssembler.getInstance().convertirADominio(List.of(entidad)).size());
    }

    @Test
    @DisplayName("Los valores nulos se convierten en objetos por defecto, como en Sigma")
    void nulos() {
        assertTrue(UtilId.esValorDefecto(CategoriaEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
        assertTrue(UtilId.esValorDefecto(CategoriaEntidadAssembler.getInstance().convertirADominio((CategoriaEntidad) null).getId()));
        assertTrue(CategoriaEntidadAssembler.getInstance().convertirADominio((List<CategoriaEntidad>) null).isEmpty());
        assertTrue(UtilId.esValorDefecto(UsuarioEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
        assertEquals(EstadoCuenta.PENDIENTE_APROBACION, UsuarioEntidadAssembler.getInstance().convertirADominio((UsuarioEntidad) null).getEstadoCuenta());
        assertTrue(UsuarioEntidadAssembler.getInstance().convertirADominio((List<UsuarioEntidad>) null).isEmpty());
        assertTrue(UtilId.esValorDefecto(AuditoriaEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
        assertTrue(UtilId.esValorDefecto(AuditoriaEntidadAssembler.getInstance().convertirADominio((AuditoriaEntidad) null).getId()));
        assertTrue(AuditoriaEntidadAssembler.getInstance().convertirADominio((List<AuditoriaEntidad>) null).isEmpty());
    }
}
