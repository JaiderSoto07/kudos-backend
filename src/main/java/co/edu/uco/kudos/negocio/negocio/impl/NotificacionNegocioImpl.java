package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.NotificacionDominio;
import co.edu.uco.kudos.dominio.TipoNotificacion;
import co.edu.uco.kudos.entidad.NotificacionEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.NotificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.NotificacionEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.notificacion.AsegurarNotificacionExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.notificacion.AsegurarNotificacionPropiaRule;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificacionNegocioImpl implements NotificacionNegocio {

    private static final int LONGITUD_MAXIMA_MENSAJE = 500;
    private static final String PUNTOS_SUSPENSIVOS = "...";

    private final DAOFactory daoFactory;

    public NotificacionNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void notificar(NotificacionDominio datos) {
        var notificacionNueva = new NotificacionDominio.Builder()
                .id(UtilId.generarId())
                .destinatario(datos.getDestinatario())
                .tipo(datos.getTipo())
                .mensaje(recortar(datos.getMensaje()))
                .tipoReferencia(datos.getTipoReferencia())
                .idReferencia(datos.getIdReferencia())
                .fechaCreacion(UtilFecha.fechaActual())
                .leida(false)
                .build();

        var notificacionEntidad = NotificacionEntidadAssembler.getInstance().convertirAEntidad(notificacionNueva);
        daoFactory.obtenerNotificacionDAO().crear(notificacionEntidad);
    }

    @Override
    public List<NotificacionDominio> consultarPorTipo(UUID idDestinatario, TipoNotificacion tipo) {
        var filtro = new NotificacionEntidad.Builder()
                .destinatario(new UsuarioEntidad.Builder().id(idDestinatario).build())
                .tipo(UtilEnum.obtenerTexto(tipo))
                .build();
        var notificaciones = daoFactory.obtenerNotificacionDAO().consultarPorFiltro(filtro);
        return NotificacionEntidadAssembler.getInstance().convertirADominio(notificaciones);
    }

    @Override
    public List<NotificacionDominio> consultarMias(UUID idActor) {
        var filtro = new NotificacionEntidad.Builder()
                .destinatario(new UsuarioEntidad.Builder().id(idActor).build())
                .build();
        var mias = new ArrayList<NotificacionDominio>();
        for (var notificacion : NotificacionEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerNotificacionDAO().consultarPorFiltro(filtro))) {
            if (notificacion.getTipo() != TipoNotificacion.CODIGO_VERIFICACION) {
                mias.add(notificacion);
            }
        }
        mias.sort((primera, segunda) -> primera.isLeida() == segunda.isLeida()
                ? segunda.getFechaCreacion().compareTo(primera.getFechaCreacion())
                : Boolean.compare(primera.isLeida(), segunda.isLeida()));
        return mias;
    }

    @Override
    public void marcarLeida(UUID idActor, UUID idNotificacion) {
        var notificacion = NotificacionEntidadAssembler.getInstance().convertirADominio(
                daoFactory.obtenerNotificacionDAO().consultarPorId(UtilId.valorDefecto(idNotificacion)));
        AsegurarNotificacionExisteRule.obtenerInstancia().ejecutar(notificacion);
        AsegurarNotificacionPropiaRule.obtenerInstancia().ejecutar(idActor, notificacion.getDestinatario().getId());
        guardarLeida(notificacion);
    }

    @Override
    public void marcarTodasLeidas(UUID idActor) {
        for (var notificacion : consultarMias(idActor)) {
            if (!notificacion.isLeida()) {
                guardarLeida(notificacion);
            }
        }
    }

    private void guardarLeida(NotificacionDominio notificacion) {
        notificacion.marcarLeida();
        daoFactory.obtenerNotificacionDAO().actualizar(notificacion.getId(), NotificacionEntidadAssembler.getInstance().convertirAEntidad(notificacion));
    }

    private String recortar(String mensaje) {
        if (mensaje.length() <= LONGITUD_MAXIMA_MENSAJE) {
            return mensaje;
        }
        return mensaje.substring(0, LONGITUD_MAXIMA_MENSAJE - PUNTOS_SUSPENSIVOS.length()) + PUNTOS_SUSPENSIVOS;
    }
}
