package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.CanalVerificacion;
import co.edu.uco.kudos.dominio.CodigoVerificacionDominio;
import co.edu.uco.kudos.entidad.CodigoVerificacionEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class CodigoVerificacionEntidadAssembler implements EntidadAssembler<CodigoVerificacionDominio, CodigoVerificacionEntidad> {

    private static final EntidadAssembler<CodigoVerificacionDominio, CodigoVerificacionEntidad> INSTANCIA = new CodigoVerificacionEntidadAssembler();

    private CodigoVerificacionEntidadAssembler() {
    }

    public static EntidadAssembler<CodigoVerificacionDominio, CodigoVerificacionEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public CodigoVerificacionEntidad convertirAEntidad(CodigoVerificacionDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new CodigoVerificacionDominio.Builder().build());
        return new CodigoVerificacionEntidad.Builder()
                .id(dominioSeguro.getId())
                .usuario(UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getUsuario()))
                .canal(UtilEnum.obtenerTexto(dominioSeguro.getCanal()))
                .destino(dominioSeguro.getDestino())
                .codigo(dominioSeguro.getCodigo())
                .fechaGeneracion(dominioSeguro.getFechaGeneracion())
                .fechaExpiracion(dominioSeguro.getFechaExpiracion())
                .intentosFallidos(dominioSeguro.getIntentosFallidos())
                .usado(dominioSeguro.isUsado())
                .build();
    }

    @Override
    public CodigoVerificacionDominio convertirADominio(CodigoVerificacionEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new CodigoVerificacionEntidad.Builder().build());
        return new CodigoVerificacionDominio.Builder()
                .id(entidadSegura.getId())
                .usuario(UsuarioEntidadAssembler.getInstance().convertirADominio(entidadSegura.getUsuario()))
                .canal(UtilEnum.obtenerValor(CanalVerificacion.class, entidadSegura.getCanal(), CanalVerificacion.CORREO))
                .destino(entidadSegura.getDestino())
                .codigo(entidadSegura.getCodigo())
                .fechaGeneracion(entidadSegura.getFechaGeneracion())
                .fechaExpiracion(entidadSegura.getFechaExpiracion())
                .intentosFallidos(entidadSegura.getIntentosFallidos())
                .usado(entidadSegura.isUsado())
                .build();
    }

    @Override
    public List<CodigoVerificacionDominio> convertirADominio(List<CodigoVerificacionEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<CodigoVerificacionEntidad>());
        var listaDominio = new ArrayList<CodigoVerificacionDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
