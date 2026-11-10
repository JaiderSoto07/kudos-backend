package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dto.CargoDto;
import co.edu.uco.kudos.dto.ProgramaAcademicoDto;
import co.edu.uco.kudos.negocio.fachada.ProgramaFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.CargoDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ProgramaAcademicoDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.CargoNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.ProgramaNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProgramaFachadaImpl implements ProgramaFachada {

    @Override
    public List<ProgramaAcademicoDto> consultar(UUID idActor) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var programaNegocio = new ProgramaNegocioImpl(daoFactory);
        var cargoNegocio = new CargoNegocioImpl(daoFactory);

        try {
            var cargos = cargoNegocio.consultarCargosVigentes();
            var resultado = new ArrayList<ProgramaAcademicoDto>();
            for (var programa : programaNegocio.consultar(idActor)) {
                resultado.add(armarDto(programa, cargos));
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
    public ProgramaAcademicoDto consultarPorId(UUID idActor, UUID idPrograma) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var programaNegocio = new ProgramaNegocioImpl(daoFactory);
        var cargoNegocio = new CargoNegocioImpl(daoFactory);

        try {
            return armarDto(programaNegocio.consultarPorId(idActor, idPrograma), cargoNegocio.consultarCargosVigentes());
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
    public void registrar(UUID idActor, ProgramaAcademicoDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var programaNegocio = new ProgramaNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            programaNegocio.registrar(idActor, ProgramaAcademicoDtoAssembler.getInstance().convertirADominio(datos));
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.OrganizacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_GUARDANDO_PROGRAMA;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void modificar(UUID idActor, UUID idPrograma, ProgramaAcademicoDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var programaNegocio = new ProgramaNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            programaNegocio.modificar(idActor, idPrograma, ProgramaAcademicoDtoAssembler.getInstance().convertirADominio(datos));
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.OrganizacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_GUARDANDO_PROGRAMA;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void inactivar(UUID idActor, UUID idPrograma) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var programaNegocio = new ProgramaNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            programaNegocio.inactivar(idActor, idPrograma);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.OrganizacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_GUARDANDO_PROGRAMA;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<CargoDto> consultarCoordinadores(UUID idActor, UUID idPrograma) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var programaNegocio = new ProgramaNegocioImpl(daoFactory);

        try {
            return CargoDtoAssembler.getInstance().convertirADTO(programaNegocio.consultarCoordinadores(idActor, idPrograma));
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.OrganizacionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    private ProgramaAcademicoDto armarDto(ProgramaAcademicoDominio programa, List<AsignacionRolDominio> cargos) {
        var dto = ProgramaAcademicoDtoAssembler.getInstance().convertirADTO(programa);
        for (var cargo : cargos) {
            if (cargo.getRol() == Rol.COORDINADOR_PROGRAMA && cargo.getPrograma().getId().equals(programa.getId())) {
                dto.setNombreCoordinador(cargo.getUsuario().nombreCompleto());
            }
        }
        return dto;
    }
}
