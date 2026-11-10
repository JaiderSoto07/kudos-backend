package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dto.CodigoVerificacionDto;
import co.edu.uco.kudos.negocio.fachada.VerificacionFachada;
import co.edu.uco.kudos.negocio.negocio.impl.VerificacionNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.UUID;

public class VerificacionFachadaImpl implements VerificacionFachada {

    @Override
    public boolean verificarCelular(UUID idUsuario, CodigoVerificacionDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var verificacionNegocio = new VerificacionNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var datosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(datos, new CodigoVerificacionDto());
            var verificado = verificacionNegocio.verificarCelular(idUsuario, datosSeguros.getCodigo());
            daoFactory.confirmarTransaccion();
            return verificado;
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.IdentidadFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_VERIFICANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void reenviarCodigoCelular(UUID idUsuario) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var verificacionNegocio = new VerificacionNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            verificacionNegocio.reenviarCodigoCelular(idUsuario);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.IdentidadFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_ENVIANDO_CODIGO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
