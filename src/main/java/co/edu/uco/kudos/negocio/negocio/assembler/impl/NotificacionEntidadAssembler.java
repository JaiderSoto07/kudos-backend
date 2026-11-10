package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.NotificacionDominio;
import co.edu.uco.kudos.dominio.TipoNotificacion;
import co.edu.uco.kudos.entidad.NotificacionEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class NotificacionEntidadAssembler implements EntidadAssembler<NotificacionDominio, NotificacionEntidad> {

    private static final EntidadAssembler<NotificacionDominio, NotificacionEntidad> INSTANCIA = new NotificacionEntidadAssembler();

    private NotificacionEntidadAssembler() {
    }

    public static EntidadAssembler<NotificacionDominio, NotificacionEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public NotificacionEntidad convertirAEntidad(NotificacionDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new NotificacionDominio.Builder().build());
        return new NotificacionEntidad.Builder()
                .id(dominioSeguro.getId())
                .destinatario(UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getDestinatario()))
                .tipo(UtilEnum.obtenerTexto(dominioSeguro.getTipo()))
                .mensaje(dominioSeguro.getMensaje())
                .tipoReferencia(dominioSeguro.getTipoReferencia())
                .idReferencia(dominioSeguro.getIdReferencia())
                .fechaCreacion(dominioSeguro.getFechaCreacion())
                .leida(dominioSeguro.isLeida())
                .fechaLectura(dominioSeguro.getFechaLectura())
                .build();
    }

    @Override
    public NotificacionDominio convertirADominio(NotificacionEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new NotificacionEntidad.Builder().build());
        return new NotificacionDominio.Builder()
                .id(entidadSegura.getId())
                .destinatario(UsuarioEntidadAssembler.getInstance().convertirADominio(entidadSegura.getDestinatario()))
                .tipo(UtilEnum.obtenerValor(TipoNotificacion.class, entidadSegura.getTipo(), null))
                .mensaje(entidadSegura.getMensaje())
                .tipoReferencia(entidadSegura.getTipoReferencia())
                .idReferencia(entidadSegura.getIdReferencia())
                .fechaCreacion(entidadSegura.getFechaCreacion())
                .leida(entidadSegura.isLeida())
                .fechaLectura(entidadSegura.getFechaLectura())
                .build();
    }

    @Override
    public List<NotificacionDominio> convertirADominio(List<NotificacionEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<NotificacionEntidad>());
        var listaDominio = new ArrayList<NotificacionDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
