package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.NotificacionDominio;
import co.edu.uco.kudos.dominio.TipoNotificacion;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.AuditoriaDto;
import co.edu.uco.kudos.dto.NotificacionDto;
import co.edu.uco.kudos.dto.PaginaAuditoriaDto;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.AuditoriaDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.NotificacionDtoAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Ensambladores y DTO de auditoría y notificaciones (M7)")
class AssemblersAuditoriaTest {

    @Test
    @DisplayName("La auditoría pasa a DTO con el nombre del actor y vuelve a dominio; los nulos dan valores por defecto")
    void auditoria() {
        var registro = new AuditoriaDominio.Builder()
                .id(UtilId.generarId())
                .fechaHora(UtilFecha.fechaActual())
                .actor(new UsuarioDominio.Builder().id(UtilId.generarId()).nombres("María").apellidos("Gómez").build())
                .accion("KudoVetado")
                .tipoObjeto("Kudo")
                .idObjeto(UtilId.generarId())
                .estadoAnterior("EN_REVISION")
                .estadoNuevo("VETADO")
                .motivo("Contiene burlas")
                .detalle("Detalle")
                .build();

        var dto = AuditoriaDtoAssembler.getInstance().convertirADTO(registro);
        assertEquals(registro.getId(), dto.getId());
        assertEquals(registro.getFechaHora(), dto.getFechaHora());
        assertEquals(registro.getActor().getId(), dto.getIdActor());
        assertEquals("María Gómez", dto.getNombreActor());
        assertEquals("KudoVetado", dto.getAccion());
        assertEquals("Kudo", dto.getTipoObjeto());
        assertEquals(registro.getIdObjeto(), dto.getIdObjeto());
        assertEquals("EN_REVISION", dto.getEstadoAnterior());
        assertEquals("VETADO", dto.getEstadoNuevo());
        assertEquals("Contiene burlas", dto.getMotivo());
        assertEquals("Detalle", dto.getDetalle());

        var deVuelta = AuditoriaDtoAssembler.getInstance().convertirADominio(dto);
        assertEquals(registro.getId(), deVuelta.getId());
        assertEquals(registro.getActor().getId(), deVuelta.getActor().getId());
        assertEquals("VETADO", deVuelta.getEstadoNuevo());
        assertTrue(UtilId.esValorDefecto(AuditoriaDtoAssembler.getInstance().convertirADTO((AuditoriaDominio) null).getId()));
        assertTrue(UtilId.esValorDefecto(AuditoriaDtoAssembler.getInstance().convertirADominio(null).getId()));
        assertTrue(AuditoriaDtoAssembler.getInstance().convertirADTO((List<AuditoriaDominio>) null).isEmpty());
        assertEquals(1, AuditoriaDtoAssembler.getInstance().convertirADTO(List.of(registro)).size());

        var pagina = new PaginaAuditoriaDto();
        pagina.setRegistros(List.of(dto));
        pagina.setPagina(2);
        pagina.setTotalPaginas(3);
        pagina.setTotalRegistros(41);
        assertEquals(1, pagina.getRegistros().size());
        assertEquals(2, pagina.getPagina());
        assertEquals(3, pagina.getTotalPaginas());
        assertEquals(41, pagina.getTotalRegistros());
        pagina.setRegistros(null);
        assertTrue(pagina.getRegistros().isEmpty());
        assertTrue(new AuditoriaDto().getNombreActor().isEmpty());
    }

    @Test
    @DisplayName("La notificación pasa a DTO con su tipo y si está leída, y vuelve a dominio")
    void notificacion() {
        var notificacion = new NotificacionDominio.Builder()
                .id(UtilId.generarId())
                .destinatario(new UsuarioDominio.Builder().id(UtilId.generarId()).build())
                .tipo(TipoNotificacion.REPORTE_RECHAZADO)
                .mensaje("Revisamos el contenido")
                .tipoReferencia("Kudo")
                .idReferencia(UtilId.generarId())
                .fechaCreacion(UtilFecha.fechaActual())
                .leida(true)
                .build();

        var dto = NotificacionDtoAssembler.getInstance().convertirADTO(notificacion);
        assertEquals(notificacion.getId(), dto.getId());
        assertEquals("REPORTE_RECHAZADO", dto.getTipo());
        assertEquals("Revisamos el contenido", dto.getMensaje());
        assertEquals("Kudo", dto.getTipoReferencia());
        assertEquals(notificacion.getIdReferencia(), dto.getIdReferencia());
        assertEquals(notificacion.getFechaCreacion(), dto.getFechaCreacion());
        assertTrue(dto.isLeida());

        var deVuelta = NotificacionDtoAssembler.getInstance().convertirADominio(dto);
        assertEquals(TipoNotificacion.REPORTE_RECHAZADO, deVuelta.getTipo());
        assertTrue(deVuelta.isLeida());
        assertEquals(TipoNotificacion.KUDO_RECIBIDO, NotificacionDtoAssembler.getInstance().convertirADominio(null).getTipo());
        assertFalse(NotificacionDtoAssembler.getInstance().convertirADTO((NotificacionDominio) null).isLeida());
        assertTrue(NotificacionDtoAssembler.getInstance().convertirADTO((List<NotificacionDominio>) null).isEmpty());
        assertEquals(1, NotificacionDtoAssembler.getInstance().convertirADTO(List.of(notificacion)).size());
    }
}
