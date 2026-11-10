package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.CalificacionDominio;
import co.edu.uco.kudos.entidad.CalificacionEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class CalificacionEntidadAssembler implements EntidadAssembler<CalificacionDominio, CalificacionEntidad> {

    private static final EntidadAssembler<CalificacionDominio, CalificacionEntidad> INSTANCIA = new CalificacionEntidadAssembler();

    private CalificacionEntidadAssembler() {
    }

    public static EntidadAssembler<CalificacionDominio, CalificacionEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public CalificacionEntidad convertirAEntidad(CalificacionDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new CalificacionDominio.Builder().build());
        return new CalificacionEntidad.Builder()
                .id(dominioSeguro.getId())
                .autor(UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getAutor()))
                .kudo(KudoEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getKudo()))
                .replica(ReplicaEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getReplica()))
                .estrellas(dominioSeguro.getEstrellas())
                .fechaCalificacion(dominioSeguro.getFechaCalificacion())
                .fechaModificacion(dominioSeguro.getFechaModificacion())
                .activa(dominioSeguro.isActiva())
                .build();
    }

    @Override
    public CalificacionDominio convertirADominio(CalificacionEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new CalificacionEntidad.Builder().build());
        return new CalificacionDominio.Builder()
                .id(entidadSegura.getId())
                .autor(UsuarioEntidadAssembler.getInstance().convertirADominio(entidadSegura.getAutor()))
                .kudo(KudoEntidadAssembler.getInstance().convertirADominio(entidadSegura.getKudo()))
                .replica(ReplicaEntidadAssembler.getInstance().convertirADominio(entidadSegura.getReplica()))
                .estrellas(entidadSegura.getEstrellas())
                .fechaCalificacion(entidadSegura.getFechaCalificacion())
                .fechaModificacion(entidadSegura.getFechaModificacion())
                .activa(entidadSegura.isActiva())
                .build();
    }

    @Override
    public List<CalificacionDominio> convertirADominio(List<CalificacionEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<CalificacionEntidad>());
        var listaDominio = new ArrayList<CalificacionDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
