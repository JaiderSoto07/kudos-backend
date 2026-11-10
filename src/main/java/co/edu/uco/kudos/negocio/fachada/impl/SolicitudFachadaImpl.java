package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dto.RechazoDto;
import co.edu.uco.kudos.dto.SolicitudDto;
import co.edu.uco.kudos.negocio.fachada.SolicitudFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.SolicitudDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.SolicitudNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.List;
import java.util.UUID;

public class SolicitudFachadaImpl implements SolicitudFachada {

    @Override
    public List<SolicitudDto> consultarPendientes(UUID idActor) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var solicitudNegocio = new SolicitudNegocioImpl(daoFactory);

        try {
            return SolicitudDtoAssembler.getInstance().convertirADTO(solicitudNegocio.consultarPendientes(idActor));
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.AprobacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_SOLICITUDES;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void aprobar(UUID idActor, UUID idSolicitud) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var solicitudNegocio = new SolicitudNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            solicitudNegocio.aprobar(idActor, idSolicitud);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.AprobacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_APROBANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void rechazar(UUID idActor, UUID idSolicitud, RechazoDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var solicitudNegocio = new SolicitudNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var datosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(datos, new RechazoDto());
            solicitudNegocio.rechazar(idActor, idSolicitud, datosSeguros.getMotivo());
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.AprobacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_RECHAZANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
