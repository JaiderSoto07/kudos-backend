package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dto.CuentaDto;
import co.edu.uco.kudos.negocio.fachada.CuentaFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.CuentaDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.AutenticacionNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.CuentaNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.util.UUID;

public class CuentaFachadaImpl implements CuentaFachada {

    @Override
    public CuentaDto consultarCuenta(UUID idUsuario) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var cuentaNegocio = new CuentaNegocioImpl(daoFactory);
        var autenticacionNegocio = new AutenticacionNegocioImpl(daoFactory);

        try {
            var cuenta = CuentaDtoAssembler.getInstance().convertirADTO(cuentaNegocio.consultarUsuario(idUsuario));
            cuenta.setRol(UtilEnum.obtenerTexto(autenticacionNegocio.consultarRolVigente(idUsuario)));

            var solicitud = cuentaNegocio.consultarUltimaSolicitud(idUsuario);
            if (!UtilId.esValorDefecto(solicitud.getId())) {
                cuenta.setRolSolicitado(UtilEnum.obtenerTexto(solicitud.getRolSolicitado()));
                cuenta.setEstadoSolicitud(UtilEnum.obtenerTexto(solicitud.getEstado()));
                cuenta.setMotivoRechazo(solicitud.getMotivoRechazo());
            }
            cuenta.setUltimoCodigoEnviado(cuentaNegocio.consultarUltimoCodigoEnviado(idUsuario));
            return cuenta;
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.IdentidadFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_CUENTA;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void actualizarDatosPersonales(UUID idUsuario, CuentaDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var cuentaNegocio = new CuentaNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var usuario = CuentaDtoAssembler.getInstance().convertirADominio(datos);
            cuentaNegocio.actualizarDatosPersonales(idUsuario, usuario);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.IdentidadFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_ACTUALIZANDO_CUENTA;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void cambiarCelular(UUID idUsuario, CuentaDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var cuentaNegocio = new CuentaNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var usuario = CuentaDtoAssembler.getInstance().convertirADominio(datos);
            cuentaNegocio.cambiarCelular(idUsuario, usuario);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.IdentidadFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_ACTUALIZANDO_CUENTA;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void cambiarCorreo(UUID idUsuario, CuentaDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var cuentaNegocio = new CuentaNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var usuario = CuentaDtoAssembler.getInstance().convertirADominio(datos);
            cuentaNegocio.cambiarCorreo(idUsuario, usuario);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.IdentidadFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_ACTUALIZANDO_CUENTA;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
