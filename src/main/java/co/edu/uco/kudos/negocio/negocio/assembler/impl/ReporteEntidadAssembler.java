package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.EstadoReporte;
import co.edu.uco.kudos.dominio.MotivoReporte;
import co.edu.uco.kudos.dominio.ReporteDominio;
import co.edu.uco.kudos.entidad.ModeracionEntidad;
import co.edu.uco.kudos.entidad.ReporteEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class ReporteEntidadAssembler implements EntidadAssembler<ReporteDominio, ReporteEntidad> {

    private static final EntidadAssembler<ReporteDominio, ReporteEntidad> INSTANCIA = new ReporteEntidadAssembler();

    private ReporteEntidadAssembler() {
    }

    public static EntidadAssembler<ReporteDominio, ReporteEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public ReporteEntidad convertirAEntidad(ReporteDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new ReporteDominio.Builder().build());
        return new ReporteEntidad.Builder()
                .id(dominioSeguro.getId())
                .reportante(UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getReportante()))
                .kudo(KudoEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getKudo()))
                .replica(ReplicaEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getReplica()))
                .motivo(UtilEnum.obtenerTexto(dominioSeguro.getMotivo()))
                .descripcion(dominioSeguro.getDescripcion())
                .fechaReporte(dominioSeguro.getFechaReporte())
                .estado(UtilEnum.obtenerTexto(dominioSeguro.getEstado()))
                .moderacion(new ModeracionEntidad.Builder().id(dominioSeguro.getIdModeracion()).build())
                .build();
    }

    @Override
    public ReporteDominio convertirADominio(ReporteEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new ReporteEntidad.Builder().build());
        return new ReporteDominio.Builder()
                .id(entidadSegura.getId())
                .reportante(UsuarioEntidadAssembler.getInstance().convertirADominio(entidadSegura.getReportante()))
                .kudo(KudoEntidadAssembler.getInstance().convertirADominio(entidadSegura.getKudo()))
                .replica(ReplicaEntidadAssembler.getInstance().convertirADominio(entidadSegura.getReplica()))
                .motivo(UtilEnum.obtenerValor(MotivoReporte.class, entidadSegura.getMotivo(), MotivoReporte.OTRO))
                .descripcion(entidadSegura.getDescripcion())
                .fechaReporte(entidadSegura.getFechaReporte())
                .estado(UtilEnum.obtenerValor(EstadoReporte.class, entidadSegura.getEstado(), EstadoReporte.PENDIENTE))
                .idModeracion(entidadSegura.getModeracion().getId())
                .build();
    }

    @Override
    public List<ReporteDominio> convertirADominio(List<ReporteEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<ReporteEntidad>());
        var listaDominio = new ArrayList<ReporteDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
