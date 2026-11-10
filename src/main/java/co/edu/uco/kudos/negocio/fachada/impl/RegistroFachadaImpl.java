package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dto.OpcionesRegistroDto;
import co.edu.uco.kudos.dto.RegistroDto;
import co.edu.uco.kudos.negocio.fachada.RegistroFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.FacultadDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ProgramaAcademicoDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.RegistroDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.RegistroNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.UUID;

public class RegistroFachadaImpl implements RegistroFachada {

    @Override
    public void registrarSolicitud(String correo, RegistroDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var registroNegocio = new RegistroNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var datosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(datos, new RegistroDto());
            var solicitud = RegistroDtoAssembler.getInstance().convertirADominio(datosSeguros);
            registroNegocio.registrarSolicitud(correo, datosSeguros.getRolSolicitado(), solicitud);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.IdentidadFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_REGISTRANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void registrarNuevaSolicitud(UUID idUsuario, RegistroDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var registroNegocio = new RegistroNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var datosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(datos, new RegistroDto());
            var solicitud = RegistroDtoAssembler.getInstance().convertirADominio(datosSeguros);
            registroNegocio.registrarNuevaSolicitud(idUsuario, datosSeguros.getRolSolicitado(), solicitud);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.IdentidadFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_REGISTRANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public OpcionesRegistroDto consultarOpciones() {
        var daoFactory = DAOFactory.obtenerFactoria();
        var registroNegocio = new RegistroNegocioImpl(daoFactory);

        try {
            var opciones = new OpcionesRegistroDto();
            var roles = new ArrayList<String>();
            for (var rol : Rol.values()) {
                if (rol.esAutorregistrable()) {
                    roles.add(rol.name());
                }
            }
            opciones.setRoles(roles);
            opciones.setFacultades(FacultadDtoAssembler.getInstance().convertirADTO(registroNegocio.consultarFacultadesDisponibles()));
            opciones.setProgramas(ProgramaAcademicoDtoAssembler.getInstance().convertirADTO(registroNegocio.consultarProgramasDisponibles()));
            return opciones;
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.IdentidadFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_OPCIONES;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
