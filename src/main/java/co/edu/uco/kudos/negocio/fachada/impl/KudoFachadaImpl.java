package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dto.KudoDto;
import co.edu.uco.kudos.dto.PaginaKudosDto;
import co.edu.uco.kudos.dto.PersonaDto;
import co.edu.uco.kudos.negocio.fachada.KudoFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.KudoDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.PersonaDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.KudoNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.PersonaNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;

import java.util.List;
import java.util.UUID;

public class KudoFachadaImpl implements KudoFachada {

    public static final int KUDOS_POR_PAGINA = 10;

    @Override
    public void crear(UUID idActor, KudoDto datos) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var kudoNegocio = new KudoNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            kudoNegocio.crear(idActor, KudoDtoAssembler.getInstance().convertirADominio(datos));
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.KudoFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CREANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public PaginaKudosDto consultarMuro(UUID idCategoria, UUID idPersona, int pagina) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var kudoNegocio = new KudoNegocioImpl(daoFactory);

        try {
            return armarPagina(kudoNegocio.consultarMuro(idCategoria, idPersona), pagina);
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.KudoFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public KudoDto consultar(UUID idActor, UUID idKudo) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var kudoNegocio = new KudoNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            var kudo = KudoDtoAssembler.getInstance().convertirADTO(kudoNegocio.consultar(idActor, idKudo));
            daoFactory.confirmarTransaccion();
            return kudo;
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.KudoFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<KudoDto> consultarHistorial(UUID idPersona, String tipo) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var kudoNegocio = new KudoNegocioImpl(daoFactory);

        try {
            return KudoDtoAssembler.getInstance().convertirADTO(kudoNegocio.consultarHistorial(idPersona, tipo));
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.KudoFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<PersonaDto> buscarPersonas(UUID idActor, String texto) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var personaNegocio = new PersonaNegocioImpl(daoFactory);

        try {
            return PersonaDtoAssembler.getInstance().convertirADTO(personaNegocio.buscarActivas(idActor, texto));
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.KudoFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_PERSONAS;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    private PaginaKudosDto armarPagina(List<KudoDominio> kudos, int paginaPedida) {
        var totalPaginas = (kudos.size() + KUDOS_POR_PAGINA - 1) / KUDOS_POR_PAGINA;
        var pagina = Math.clamp(paginaPedida, 1, Math.max(totalPaginas, 1));
        var desde = (pagina - 1) * KUDOS_POR_PAGINA;
        var hasta = Math.min(desde + KUDOS_POR_PAGINA, kudos.size());

        var resultado = new PaginaKudosDto();
        resultado.setKudos(KudoDtoAssembler.getInstance().convertirADTO(kudos.subList(desde, hasta)));
        resultado.setPagina(pagina);
        resultado.setTotalPaginas(totalPaginas);
        resultado.setTotalKudos(kudos.size());
        return resultado;
    }
}
