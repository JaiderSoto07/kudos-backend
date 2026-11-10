package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.EstadoFacultad;
import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.entidad.FacultadEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class FacultadEntidadAssembler implements EntidadAssembler<FacultadDominio, FacultadEntidad> {

    private static final EntidadAssembler<FacultadDominio, FacultadEntidad> INSTANCIA = new FacultadEntidadAssembler();

    private FacultadEntidadAssembler() {
    }

    public static EntidadAssembler<FacultadDominio, FacultadEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public FacultadEntidad convertirAEntidad(FacultadDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new FacultadDominio.Builder().build());
        return new FacultadEntidad.Builder()
                .id(dominioSeguro.getId())
                .nombre(dominioSeguro.getNombre())
                .estado(UtilEnum.obtenerTexto(dominioSeguro.getEstado()))
                .fechaCreacion(dominioSeguro.getFechaCreacion())
                .build();
    }

    @Override
    public FacultadDominio convertirADominio(FacultadEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new FacultadEntidad.Builder().build());
        return new FacultadDominio.Builder()
                .id(entidadSegura.getId())
                .nombre(entidadSegura.getNombre())
                .estado(UtilEnum.obtenerValor(EstadoFacultad.class, entidadSegura.getEstado(), EstadoFacultad.EN_CONFORMACION))
                .fechaCreacion(entidadSegura.getFechaCreacion())
                .build();
    }

    @Override
    public List<FacultadDominio> convertirADominio(List<FacultadEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<FacultadEntidad>());
        var listaDominio = new ArrayList<FacultadDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
