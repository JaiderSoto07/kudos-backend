package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.dominio.ReplicaDominio;
import co.edu.uco.kudos.entidad.ReplicaEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class ReplicaEntidadAssembler implements EntidadAssembler<ReplicaDominio, ReplicaEntidad> {

    private static final EntidadAssembler<ReplicaDominio, ReplicaEntidad> INSTANCIA = new ReplicaEntidadAssembler();

    private ReplicaEntidadAssembler() {
    }

    public static EntidadAssembler<ReplicaDominio, ReplicaEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public ReplicaEntidad convertirAEntidad(ReplicaDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new ReplicaDominio.Builder().build());
        return new ReplicaEntidad.Builder()
                .id(dominioSeguro.getId())
                .autor(UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getAutor()))
                .kudo(KudoEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getKudo()))
                .idReplicaPadre(dominioSeguro.getIdReplicaPadre())
                .contenido(dominioSeguro.getContenido())
                .fechaCreacion(dominioSeguro.getFechaCreacion())
                .estado(UtilEnum.obtenerTexto(dominioSeguro.getEstado()))
                .promedioCalificacion(dominioSeguro.getPromedioCalificacion())
                .cantidadCalificaciones(dominioSeguro.getCantidadCalificaciones())
                .build();
    }

    @Override
    public ReplicaDominio convertirADominio(ReplicaEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new ReplicaEntidad.Builder().build());
        return new ReplicaDominio.Builder()
                .id(entidadSegura.getId())
                .autor(UsuarioEntidadAssembler.getInstance().convertirADominio(entidadSegura.getAutor()))
                .kudo(KudoEntidadAssembler.getInstance().convertirADominio(entidadSegura.getKudo()))
                .idReplicaPadre(entidadSegura.getIdReplicaPadre())
                .contenido(entidadSegura.getContenido())
                .fechaCreacion(entidadSegura.getFechaCreacion())
                .estado(UtilEnum.obtenerValor(EstadoContenido.class, entidadSegura.getEstado(), EstadoContenido.CREADO))
                .promedioCalificacion(entidadSegura.getPromedioCalificacion())
                .cantidadCalificaciones(entidadSegura.getCantidadCalificaciones())
                .build();
    }

    @Override
    public List<ReplicaDominio> convertirADominio(List<ReplicaEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<ReplicaEntidad>());
        var listaDominio = new ArrayList<ReplicaDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
