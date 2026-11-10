package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.ParametroGlobalDominio;
import co.edu.uco.kudos.dominio.TipoParametro;
import co.edu.uco.kudos.entidad.ParametroGlobalEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class ParametroGlobalEntidadAssembler implements EntidadAssembler<ParametroGlobalDominio, ParametroGlobalEntidad> {

    private static final EntidadAssembler<ParametroGlobalDominio, ParametroGlobalEntidad> INSTANCIA = new ParametroGlobalEntidadAssembler();

    private ParametroGlobalEntidadAssembler() {
    }

    public static EntidadAssembler<ParametroGlobalDominio, ParametroGlobalEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public ParametroGlobalEntidad convertirAEntidad(ParametroGlobalDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new ParametroGlobalDominio.Builder().build());
        return new ParametroGlobalEntidad.Builder()
                .id(dominioSeguro.getId())
                .clave(dominioSeguro.getClave())
                .valor(dominioSeguro.getValor())
                .tipoDato(UtilEnum.obtenerTexto(dominioSeguro.getTipoDato()))
                .descripcion(dominioSeguro.getDescripcion())
                .fechaModificacion(dominioSeguro.getFechaModificacion())
                .build();
    }

    @Override
    public ParametroGlobalDominio convertirADominio(ParametroGlobalEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new ParametroGlobalEntidad.Builder().build());
        return new ParametroGlobalDominio.Builder()
                .id(entidadSegura.getId())
                .clave(entidadSegura.getClave())
                .valor(entidadSegura.getValor())
                .tipoDato(UtilEnum.obtenerValor(TipoParametro.class, entidadSegura.getTipoDato(), TipoParametro.TEXTO))
                .descripcion(entidadSegura.getDescripcion())
                .fechaModificacion(entidadSegura.getFechaModificacion())
                .build();
    }

    @Override
    public List<ParametroGlobalDominio> convertirADominio(List<ParametroGlobalEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<ParametroGlobalEntidad>());
        var listaDominio = new ArrayList<ParametroGlobalDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
