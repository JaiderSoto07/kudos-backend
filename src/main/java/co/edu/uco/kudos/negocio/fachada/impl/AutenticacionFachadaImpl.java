package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.UsuarioDto;
import co.edu.uco.kudos.negocio.fachada.AutenticacionFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.UsuarioDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.AutenticacionNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.AutenticacionNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.UUID;

public class AutenticacionFachadaImpl implements AutenticacionFachada {

    @Override
    public UsuarioDto consultarUsuarioActual(String correo) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var autenticacionNegocio = new AutenticacionNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var usuario = autenticacionNegocio.consultarUsuarioPorCorreo(correo);
            autenticacionNegocio.confirmarCorreoVerificadoPorGoogle(usuario);
            daoFactory.confirmarTransaccion();
            return armarUsuarioActual(usuario, correo, autenticacionNegocio);
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.AutenticacionFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_USUARIO_ACTUAL;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public UUID obtenerIdUsuarioRegistrado(String correo) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var autenticacionNegocio = new AutenticacionNegocioImpl(daoFactory);

        try {
            return autenticacionNegocio.obtenerIdUsuarioRegistrado(correo);
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.AutenticacionFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_USUARIO_ACTUAL;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public UUID obtenerIdActorActivo(String correo) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var autenticacionNegocio = new AutenticacionNegocioImpl(daoFactory);

        try {
            return autenticacionNegocio.obtenerIdActorActivo(correo);
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.AutenticacionFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_USUARIO_ACTUAL;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    private UsuarioDto armarUsuarioActual(UsuarioDominio usuario, String correo, AutenticacionNegocio autenticacionNegocio) {
        var usuarioActual = UsuarioDtoAssembler.getInstance().convertirADTO(usuario);

        if (UtilId.esValorDefecto(usuario.getId())) {
            usuarioActual.setCorreoInstitucional(correo);
            usuarioActual.setEstadoCuenta(UtilTexto.VACIO);
            usuarioActual.setMensajeEstado(CatalogoMensajes.AutenticacionNegocioImpl.USUARIO_NO_REGISTRADO);
            return usuarioActual;
        }

        usuarioActual.setRegistrado(true);
        usuarioActual.setRol(UtilEnum.obtenerTexto(autenticacionNegocio.consultarRolVigente(usuario.getId())));
        usuarioActual.setMensajeEstado(usuario.motivoParaNoIngresar());
        return usuarioActual;
    }
}
