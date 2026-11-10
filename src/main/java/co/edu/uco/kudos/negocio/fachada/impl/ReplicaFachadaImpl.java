package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dto.ReplicaDto;
import co.edu.uco.kudos.negocio.fachada.ReplicaFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ConversacionDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ReplicaDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.ReplicaNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;

import java.util.List;
import java.util.UUID;

public class ReplicaFachadaImpl implements ReplicaFachada {

    @Override
    public void responderKudo(UUID idActor, UUID idKudo, ReplicaDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var replicaNegocio = new ReplicaNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            replicaNegocio.responderKudo(idActor, idKudo, ReplicaDtoAssembler.getInstance().convertirADominio(datos).getContenido());
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.ReplicaFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_RESPONDIENDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void responderReplica(UUID idActor, UUID idReplica, ReplicaDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var replicaNegocio = new ReplicaNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            replicaNegocio.responderReplica(idActor, idReplica, ReplicaDtoAssembler.getInstance().convertirADominio(datos).getContenido());
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.ReplicaFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_RESPONDIENDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<ReplicaDto> consultarConversacion(UUID idKudo) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var replicaNegocio = new ReplicaNegocioImpl(daoFactory);

        try {
            return ConversacionDtoAssembler.getInstance().armarArbol(replicaNegocio.consultarConversacion(idKudo));
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.ReplicaFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
