package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dto.NotificacionDto;
import co.edu.uco.kudos.negocio.fachada.NotificacionFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.NotificacionDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.NotificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.NotificacionNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class NotificacionFachadaImpl implements NotificacionFachada {

    @Override
    public List<NotificacionDto> consultarMias(UUID idActor) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var notificacionNegocio = new NotificacionNegocioImpl(daoFactory);

        try {
            return NotificacionDtoAssembler.getInstance().convertirADTO(notificacionNegocio.consultarMias(idActor));
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.NotificacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void marcarLeida(UUID idActor, UUID idNotificacion) {
        ejecutarEnTransaccion(negocio -> negocio.marcarLeida(idActor, idNotificacion));
    }

    @Override
    public void marcarTodasLeidas(UUID idActor) {
        ejecutarEnTransaccion(negocio -> negocio.marcarTodasLeidas(idActor));
    }

    private void ejecutarEnTransaccion(Consumer<NotificacionNegocio> operacion) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var notificacionNegocio = new NotificacionNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            operacion.accept(notificacionNegocio);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.NotificacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_MARCANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
