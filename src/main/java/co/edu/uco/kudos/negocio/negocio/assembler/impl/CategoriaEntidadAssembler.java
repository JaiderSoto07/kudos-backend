package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.entidad.CategoriaEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class CategoriaEntidadAssembler implements EntidadAssembler<CategoriaDominio, CategoriaEntidad> {

    private static final EntidadAssembler<CategoriaDominio, CategoriaEntidad> INSTANCIA = new CategoriaEntidadAssembler();

    private CategoriaEntidadAssembler() {
    }

    public static EntidadAssembler<CategoriaDominio, CategoriaEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public CategoriaEntidad convertirAEntidad(CategoriaDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new CategoriaDominio.Builder().build());
        return new CategoriaEntidad.Builder()
                .id(dominioSeguro.getId())
                .nombre(dominioSeguro.getNombre())
                .descripcion(dominioSeguro.getDescripcion())
                .activa(dominioSeguro.isActiva())
                .fechaCreacion(dominioSeguro.getFechaCreacion())
                .build();
    }

    @Override
    public CategoriaDominio convertirADominio(CategoriaEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new CategoriaEntidad.Builder().build());
        return new CategoriaDominio.Builder()
                .id(entidadSegura.getId())
                .nombre(entidadSegura.getNombre())
                .descripcion(entidadSegura.getDescripcion())
                .activa(entidadSegura.isActiva())
                .fechaCreacion(entidadSegura.getFechaCreacion())
                .build();
    }

    @Override
    public List<CategoriaDominio> convertirADominio(List<CategoriaEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<CategoriaEntidad>());
        var listaDominio = new ArrayList<CategoriaDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
