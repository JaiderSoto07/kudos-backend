package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.ReporteDominio;
import co.edu.uco.kudos.dto.ContenidoReportadoDto;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public final class ContenidoReportadoDtoAssembler {

    private static final ContenidoReportadoDtoAssembler INSTANCIA = new ContenidoReportadoDtoAssembler();

    private ContenidoReportadoDtoAssembler() {
    }

    public static ContenidoReportadoDtoAssembler getInstance() {
        return INSTANCIA;
    }

    public List<ContenidoReportadoDto> agrupar(List<ReporteDominio> reportes) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(reportes, new ArrayList<ReporteDominio>());
        var contenidos = new LinkedHashMap<UUID, ContenidoReportadoDto>();
        for (var reporte : listaSegura) {
            var dto = ReporteDtoAssembler.getInstance().convertirADTO(reporte);
            var contenido = contenidos.get(dto.getIdContenido());
            if (contenido == null) {
                contenido = new ContenidoReportadoDto();
                contenido.setTipoContenido(dto.getTipoContenido());
                contenido.setIdContenido(dto.getIdContenido());
                contenido.setIdKudo(dto.getIdKudo());
                contenido.setContenido(dto.getContenido());
                contenido.setNombreAutor(dto.getNombreAutorContenido());
                contenido.setEstadoContenido(UtilEnum.obtenerTexto(reporte.esSobreKudo() ? reporte.getKudo().getEstado() : reporte.getReplica().getEstado()));
                contenido.setFechaUltimoReporte(dto.getFechaReporte());
                contenidos.put(dto.getIdContenido(), contenido);
            }
            contenido.getReportes().add(dto);
            contenido.setCantidadReportes(contenido.getReportes().size());
        }
        return new ArrayList<>(contenidos.values());
    }
}
