package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.DecisionModeracion;
import co.edu.uco.kudos.dominio.EstadoModeracion;
import co.edu.uco.kudos.dominio.ModeracionDominio;
import co.edu.uco.kudos.entidad.ModeracionEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class ModeracionEntidadAssembler implements EntidadAssembler<ModeracionDominio, ModeracionEntidad> {

    private static final EntidadAssembler<ModeracionDominio, ModeracionEntidad> INSTANCIA = new ModeracionEntidadAssembler();

    private ModeracionEntidadAssembler() {
    }

    public static EntidadAssembler<ModeracionDominio, ModeracionEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public ModeracionEntidad convertirAEntidad(ModeracionDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new ModeracionDominio.Builder().build());
        return new ModeracionEntidad.Builder()
                .id(dominioSeguro.getId())
                .kudo(KudoEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getKudo()))
                .replica(ReplicaEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getReplica()))
                .moderador(UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getModerador()))
                .estado(UtilEnum.obtenerTexto(dominioSeguro.getEstado()))
                .fechaInicio(dominioSeguro.getFechaInicio())
                .fechaCierre(dominioSeguro.getFechaCierre())
                .decision(UtilEnum.obtenerTexto(dominioSeguro.getDecision()))
                .motivoDecision(dominioSeguro.getMotivoDecision())
                .build();
    }

    @Override
    public ModeracionDominio convertirADominio(ModeracionEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new ModeracionEntidad.Builder().build());
        return new ModeracionDominio.Builder()
                .id(entidadSegura.getId())
                .kudo(KudoEntidadAssembler.getInstance().convertirADominio(entidadSegura.getKudo()))
                .replica(ReplicaEntidadAssembler.getInstance().convertirADominio(entidadSegura.getReplica()))
                .moderador(UsuarioEntidadAssembler.getInstance().convertirADominio(entidadSegura.getModerador()))
                .estado(UtilEnum.obtenerValor(EstadoModeracion.class, entidadSegura.getEstado(), EstadoModeracion.EN_CURSO))
                .fechaInicio(entidadSegura.getFechaInicio())
                .fechaCierre(entidadSegura.getFechaCierre())
                .decision(UtilEnum.obtenerValor(DecisionModeracion.class, entidadSegura.getDecision(), null))
                .motivoDecision(entidadSegura.getMotivoDecision())
                .build();
    }

    @Override
    public List<ModeracionDominio> convertirADominio(List<ModeracionEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<ModeracionEntidad>());
        var listaDominio = new ArrayList<ModeracionDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
