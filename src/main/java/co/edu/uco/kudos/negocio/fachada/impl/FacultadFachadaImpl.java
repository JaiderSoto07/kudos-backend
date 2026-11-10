package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dto.CargoDto;
import co.edu.uco.kudos.dto.FacultadDto;
import co.edu.uco.kudos.negocio.fachada.FacultadFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.CargoDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.FacultadDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.CargoNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.FacultadNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FacultadFachadaImpl implements FacultadFachada {

    @Override
    public List<FacultadDto> consultar(UUID idActor) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var facultadNegocio = new FacultadNegocioImpl(daoFactory);
        var cargoNegocio = new CargoNegocioImpl(daoFactory);

        try {
            var cargos = cargoNegocio.consultarCargosVigentes();
            var resultado = new ArrayList<FacultadDto>();
            for (var facultad : facultadNegocio.consultar(idActor)) {
                var dto = FacultadDtoAssembler.getInstance().convertirADTO(facultad);
                dto.setCantidadProgramas(facultadNegocio.contarProgramasActivos(facultad.getId()));
                for (var cargo : cargos) {
                    if (cargo.getRol() == Rol.COORDINADOR_FACULTAD && cargo.getFacultad().getId().equals(facultad.getId())) {
                        dto.setNombreCoordinador(cargo.getUsuario().nombreCompleto());
                    }
                }
                resultado.add(dto);
            }
            return resultado;
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
    public void registrar(UUID idActor, FacultadDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var facultadNegocio = new FacultadNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            facultadNegocio.registrar(idActor, FacultadDtoAssembler.getInstance().convertirADominio(datos));
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.OrganizacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_GUARDANDO_FACULTAD;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void modificar(UUID idActor, UUID idFacultad, FacultadDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var facultadNegocio = new FacultadNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            facultadNegocio.modificar(idActor, idFacultad, FacultadDtoAssembler.getInstance().convertirADominio(datos));
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.OrganizacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_GUARDANDO_FACULTAD;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void inactivar(UUID idActor, UUID idFacultad) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var facultadNegocio = new FacultadNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            facultadNegocio.inactivar(idActor, idFacultad);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.OrganizacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_GUARDANDO_FACULTAD;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<CargoDto> consultarCoordinadores(UUID idActor, UUID idFacultad) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var facultadNegocio = new FacultadNegocioImpl(daoFactory);

        try {
            return CargoDtoAssembler.getInstance().convertirADTO(facultadNegocio.consultarCoordinadores(idActor, idFacultad));
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
