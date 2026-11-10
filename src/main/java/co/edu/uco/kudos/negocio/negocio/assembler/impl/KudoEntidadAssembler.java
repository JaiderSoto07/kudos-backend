package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class KudoEntidadAssembler implements EntidadAssembler<KudoDominio, KudoEntidad> {

    private static final EntidadAssembler<KudoDominio, KudoEntidad> INSTANCIA = new KudoEntidadAssembler();

    private KudoEntidadAssembler() {
    }

    public static EntidadAssembler<KudoDominio, KudoEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public KudoEntidad convertirAEntidad(KudoDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new KudoDominio.Builder().build());
        return new KudoEntidad.Builder()
                .id(dominioSeguro.getId())
                .autor(UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getAutor()))
                .destinatario(UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getDestinatario()))
                .categoria(CategoriaEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getCategoria()))
                .contenido(dominioSeguro.getContenido())
                .fechaCreacion(dominioSeguro.getFechaCreacion())
                .estado(UtilEnum.obtenerTexto(dominioSeguro.getEstado()))
                .cantidadVisualizaciones(dominioSeguro.getCantidadVisualizaciones())
                .promedioCalificacion(dominioSeguro.getPromedioCalificacion())
                .cantidadCalificaciones(dominioSeguro.getCantidadCalificaciones())
                .build();
    }

    @Override
    public KudoDominio convertirADominio(KudoEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new KudoEntidad.Builder().build());
        return new KudoDominio.Builder()
                .id(entidadSegura.getId())
                .autor(UsuarioEntidadAssembler.getInstance().convertirADominio(entidadSegura.getAutor()))
                .destinatario(UsuarioEntidadAssembler.getInstance().convertirADominio(entidadSegura.getDestinatario()))
                .categoria(CategoriaEntidadAssembler.getInstance().convertirADominio(entidadSegura.getCategoria()))
                .contenido(entidadSegura.getContenido())
                .fechaCreacion(entidadSegura.getFechaCreacion())
                .estado(UtilEnum.obtenerValor(EstadoContenido.class, entidadSegura.getEstado(), EstadoContenido.CREADO))
                .cantidadVisualizaciones(entidadSegura.getCantidadVisualizaciones())
                .promedioCalificacion(entidadSegura.getPromedioCalificacion())
                .cantidadCalificaciones(entidadSegura.getCantidadCalificaciones())
                .build();
    }

    @Override
    public List<KudoDominio> convertirADominio(List<KudoEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<KudoEntidad>());
        var listaDominio = new ArrayList<KudoDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
