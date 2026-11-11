package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dto.PaginaAuditoriaDto;
import co.edu.uco.kudos.negocio.fachada.AuditoriaFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.AuditoriaDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.AuditoriaNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class AuditoriaFachadaImpl implements AuditoriaFachada {

    private static final int REGISTROS_POR_PAGINA = 20;

    @Override
    public PaginaAuditoriaDto consultar(UUID idActor, LocalDate desde, LocalDate hasta, String actor, String accion, String tipoObjeto, int pagina) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);

        try {
            return armarPagina(auditoriaNegocio.consultar(idActor, desde, hasta, actor, accion, tipoObjeto), pagina);
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.AuditoriaFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    private PaginaAuditoriaDto armarPagina(List<AuditoriaDominio> registros, int paginaPedida) {
        var totalPaginas = (registros.size() + REGISTROS_POR_PAGINA - 1) / REGISTROS_POR_PAGINA;
        var pagina = Math.clamp(paginaPedida, 1, Math.max(totalPaginas, 1));
        var desde = (pagina - 1) * REGISTROS_POR_PAGINA;
        var hasta = Math.min(desde + REGISTROS_POR_PAGINA, registros.size());

        var resultado = new PaginaAuditoriaDto();
        resultado.setRegistros(AuditoriaDtoAssembler.getInstance().convertirADTO(registros.subList(desde, hasta)));
        resultado.setPagina(pagina);
        resultado.setTotalPaginas(totalPaginas);
        resultado.setTotalRegistros(registros.size());
        return resultado;
    }
}
