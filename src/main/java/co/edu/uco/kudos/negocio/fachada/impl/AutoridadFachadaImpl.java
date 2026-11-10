package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dto.AutoridadDto;
import co.edu.uco.kudos.negocio.fachada.AutoridadFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.AutoridadDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.AutoridadNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.UUID;

public class AutoridadFachadaImpl implements AutoridadFachada {

    @Override
    public void registrarAutoridad(UUID idActor, AutoridadDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var autoridadNegocio = new AutoridadNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var datosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(datos, new AutoridadDto());
            var usuario = AutoridadDtoAssembler.getInstance().convertirADominio(datosSeguros);
            autoridadNegocio.registrarAutoridad(idActor, datosSeguros.getRol(), usuario);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.AprobacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_REGISTRANDO_AUTORIDAD;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
