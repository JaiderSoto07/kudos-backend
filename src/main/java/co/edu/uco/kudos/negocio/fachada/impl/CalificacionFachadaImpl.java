package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dto.CalificacionDto;
import co.edu.uco.kudos.dto.ValoracionesDto;
import co.edu.uco.kudos.negocio.fachada.CalificacionFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ValoracionesDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.CalificacionNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.UUID;

public class CalificacionFachadaImpl implements CalificacionFachada {

    @Override
    public void calificarKudo(UUID idActor, UUID idKudo, CalificacionDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var calificacionNegocio = new CalificacionNegocioImpl(daoFactory);
        var estrellas = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(datos, new CalificacionDto()).getEstrellas();

        daoFactory.iniciarTransaccion();
        try {
            calificacionNegocio.calificarKudo(idActor, idKudo, estrellas);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.CalificacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CALIFICANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void calificarReplica(UUID idActor, UUID idReplica, CalificacionDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var calificacionNegocio = new CalificacionNegocioImpl(daoFactory);
        var estrellas = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(datos, new CalificacionDto()).getEstrellas();

        daoFactory.iniciarTransaccion();
        try {
            calificacionNegocio.calificarReplica(idActor, idReplica, estrellas);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.CalificacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CALIFICANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public ValoracionesDto consultarValoracionesKudo(UUID idActor, UUID idKudo) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var calificacionNegocio = new CalificacionNegocioImpl(daoFactory);

        try {
            return ValoracionesDtoAssembler.getInstance().armar(calificacionNegocio.consultarValoracionesKudo(idKudo), idActor);
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.CalificacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public ValoracionesDto consultarValoracionesReplica(UUID idActor, UUID idReplica) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var calificacionNegocio = new CalificacionNegocioImpl(daoFactory);

        try {
            return ValoracionesDtoAssembler.getInstance().armar(calificacionNegocio.consultarValoracionesReplica(idReplica), idActor);
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.CalificacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
