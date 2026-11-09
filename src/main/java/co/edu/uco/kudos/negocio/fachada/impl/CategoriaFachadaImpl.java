package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dto.CategoriaDto;
import co.edu.uco.kudos.negocio.fachada.CategoriaFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.CategoriaDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.CategoriaNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;

import java.util.List;
import java.util.UUID;

public class CategoriaFachadaImpl implements CategoriaFachada {

    @Override
    public void registrarNuevaCategoria(UUID idActor, CategoriaDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var categoriaNegocio = new CategoriaNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var categoriaDominio = CategoriaDtoAssembler.getInstance().convertirADominio(datos);
            categoriaNegocio.registrarNuevaCategoria(idActor, categoriaDominio);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.CategoriaFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_REGISTRANDO_CATEGORIA;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void modificarCategoria(UUID idActor, UUID id, CategoriaDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var categoriaNegocio = new CategoriaNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var categoriaDominio = CategoriaDtoAssembler.getInstance().convertirADominio(datos);
            categoriaNegocio.modificarCategoria(idActor, id, categoriaDominio);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.CategoriaFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_MODIFICANDO_CATEGORIA;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void activarCategoria(UUID idActor, UUID id) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var categoriaNegocio = new CategoriaNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            categoriaNegocio.activarCategoria(idActor, id);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.CategoriaFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_ACTIVANDO_CATEGORIA;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void desactivarCategoria(UUID idActor, UUID id) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var categoriaNegocio = new CategoriaNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            categoriaNegocio.desactivarCategoria(idActor, id);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.CategoriaFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_DESACTIVANDO_CATEGORIA;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<CategoriaDto> consultarActivas() {
        var daoFactory = DAOFactory.obtenerFactoria();
        var categoriaNegocio = new CategoriaNegocioImpl(daoFactory);

        try {
            var categoriasEncontradas = categoriaNegocio.consultarActivas();
            return CategoriaDtoAssembler.getInstance().convertirADTO(categoriasEncontradas);
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.CategoriaFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_CATEGORIAS;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<CategoriaDto> consultarTodas(UUID idActor) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var categoriaNegocio = new CategoriaNegocioImpl(daoFactory);

        try {
            var categoriasEncontradas = categoriaNegocio.consultarTodas(idActor);
            return CategoriaDtoAssembler.getInstance().convertirADTO(categoriasEncontradas);
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.CategoriaFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_CATEGORIAS;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
