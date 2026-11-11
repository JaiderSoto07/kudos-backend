package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.dominio.MotivoReporte;
import co.edu.uco.kudos.dominio.ReporteDominio;
import co.edu.uco.kudos.dto.ReporteDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class ReporteDtoAssembler implements DTOAssembler<ReporteDominio, ReporteDto> {

    public static final String TIPO_KUDO = "KUDO";
    public static final String TIPO_REPLICA = "REPLICA";

    private static final DTOAssembler<ReporteDominio, ReporteDto> INSTANCIA = new ReporteDtoAssembler();

    private ReporteDtoAssembler() {
    }

    public static DTOAssembler<ReporteDominio, ReporteDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public ReporteDto convertirADTO(ReporteDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new ReporteDominio.Builder().build());
        var esKudo = dominioSeguro.esSobreKudo();
        var estadoContenido = esKudo ? dominioSeguro.getKudo().getEstado() : dominioSeguro.getReplica().getEstado();
        var contenido = esKudo ? dominioSeguro.getKudo().getContenido() : dominioSeguro.getReplica().getContenido();
        var dto = new ReporteDto();
        dto.setId(dominioSeguro.getId());
        dto.setMotivo(UtilEnum.obtenerTexto(dominioSeguro.getMotivo()));
        dto.setDescripcion(dominioSeguro.getDescripcion());
        dto.setFechaReporte(dominioSeguro.getFechaReporte());
        dto.setEstado(UtilEnum.obtenerTexto(dominioSeguro.getEstado()));
        dto.setTipoContenido(esKudo ? TIPO_KUDO : TIPO_REPLICA);
        dto.setIdContenido(esKudo ? dominioSeguro.getKudo().getId() : dominioSeguro.getReplica().getId());
        dto.setIdKudo(esKudo ? dominioSeguro.getKudo().getId() : dominioSeguro.getReplica().getKudo().getId());
        dto.setContenido(estadoContenido == EstadoContenido.VETADO ? CatalogoMensajes.Replica.CONTENIDO_VETADO : contenido);
        dto.setNombreAutorContenido(esKudo ? dominioSeguro.getKudo().getAutor().nombreCompleto() : dominioSeguro.getReplica().getAutor().nombreCompleto());
        dto.setNombreReportante(dominioSeguro.getReportante().nombreCompleto());
        return dto;
    }

    @Override
    public ReporteDominio convertirADominio(ReporteDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new ReporteDto());
        return new ReporteDominio.Builder()
                .id(dtoSeguro.getId())
                .motivo(UtilEnum.obtenerValor(MotivoReporte.class, dtoSeguro.getMotivo(), MotivoReporte.OTRO))
                .descripcion(dtoSeguro.getDescripcion())
                .build();
    }

    @Override
    public List<ReporteDto> convertirADTO(List<ReporteDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<ReporteDominio>());
        var listaDto = new ArrayList<ReporteDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
