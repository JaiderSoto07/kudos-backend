package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.ModeracionDominio;
import co.edu.uco.kudos.dominio.ReporteDominio;
import co.edu.uco.kudos.dto.VetoDto;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class VetoDtoAssembler {

    private static final VetoDtoAssembler INSTANCIA = new VetoDtoAssembler();

    private VetoDtoAssembler() {
    }

    public static VetoDtoAssembler getInstance() {
        return INSTANCIA;
    }

    public VetoDto armar(ModeracionDominio moderacion, List<ReporteDominio> reportesResueltos) {
        var moderacionSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(moderacion, new ModeracionDominio.Builder().build());
        var reportesSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(reportesResueltos, new ArrayList<ReporteDominio>());
        var esKudo = moderacionSegura.esSobreKudo();
        var kudo = moderacionSegura.getKudo();
        var replica = moderacionSegura.getReplica();
        var dto = new VetoDto();
        dto.setIdModeracion(moderacionSegura.getId());
        dto.setTipoContenido(esKudo ? ReporteDtoAssembler.TIPO_KUDO : ReporteDtoAssembler.TIPO_REPLICA);
        dto.setIdContenido(esKudo ? kudo.getId() : replica.getId());
        dto.setIdKudo(esKudo ? kudo.getId() : replica.getKudo().getId());
        dto.setContenido(esKudo ? kudo.getContenido() : replica.getContenido());
        dto.setNombreAutor(esKudo ? kudo.getAutor().nombreCompleto() : replica.getAutor().nombreCompleto());
        dto.setNombreModerador(moderacionSegura.getModerador().nombreCompleto());
        dto.setFechaVeto(moderacionSegura.getFechaCierre());
        dto.setMotivo(moderacionSegura.getMotivoDecision());
        dto.setCantidadReportes(reportesSeguros.size());
        return dto;
    }
}
