package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dto.CargoDto;
import co.edu.uco.kudos.dto.DesvinculacionDto;
import co.edu.uco.kudos.dto.VinculoDto;
import co.edu.uco.kudos.negocio.fachada.VinculacionFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.CargoDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.VinculoDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.CargoNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.VinculacionNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.List;
import java.util.UUID;

public class VinculacionFachadaImpl implements VinculacionFachada {

    @Override
    public List<VinculoDto> consultarDePrograma(UUID idActor, UUID idPrograma) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var vinculacionNegocio = new VinculacionNegocioImpl(daoFactory);

        try {
            return VinculoDtoAssembler.getInstance().convertirADTO(vinculacionNegocio.consultarDePrograma(idActor, idPrograma));
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.OrganizacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<VinculoDto> consultarDeUsuario(UUID idActor, UUID idUsuario) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var vinculacionNegocio = new VinculacionNegocioImpl(daoFactory);

        try {
            return VinculoDtoAssembler.getInstance().convertirADTO(vinculacionNegocio.consultarDeUsuario(idActor, idUsuario));
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.OrganizacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<VinculoDto> consultarPropios(UUID idUsuario) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var vinculacionNegocio = new VinculacionNegocioImpl(daoFactory);

        try {
            return VinculoDtoAssembler.getInstance().convertirADTO(vinculacionNegocio.consultarPropios(idUsuario));
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.OrganizacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void vincular(UUID idActor, UUID idPrograma, VinculoDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var vinculacionNegocio = new VinculacionNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var datosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(datos, new VinculoDto());
            vinculacionNegocio.vincular(idActor, idPrograma, datosSeguros.getCorreo(), datosSeguros.getTipoVinculo());
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.OrganizacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_GUARDANDO_VINCULO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void desvincular(UUID idActor, UUID idVinculo, DesvinculacionDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var vinculacionNegocio = new VinculacionNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var datosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(datos, new DesvinculacionDto());
            vinculacionNegocio.desvincular(idActor, idVinculo, datosSeguros.getMotivo());
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.OrganizacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_GUARDANDO_VINCULO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<CargoDto> consultarEstructura() {
        var daoFactory = DAOFactory.obtenerFactoria();
        var cargoNegocio = new CargoNegocioImpl(daoFactory);

        try {
            return CargoDtoAssembler.getInstance().convertirADTO(cargoNegocio.consultarCargosVigentes());
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.OrganizacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
