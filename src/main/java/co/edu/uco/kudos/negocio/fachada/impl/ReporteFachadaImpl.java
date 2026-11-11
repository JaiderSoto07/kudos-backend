package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dto.ContenidoReportadoDto;
import co.edu.uco.kudos.dto.ReporteDto;
import co.edu.uco.kudos.negocio.fachada.ReporteFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ContenidoReportadoDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ReporteDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.ReporteNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.List;
import java.util.UUID;

public class ReporteFachadaImpl implements ReporteFachada {

    @Override
    public void reportarKudo(UUID idActor, UUID idKudo, ReporteDto datos) {
        reportar(idActor, idKudo, datos, true);
    }

    @Override
    public void reportarReplica(UUID idActor, UUID idReplica, ReporteDto datos) {
        reportar(idActor, idReplica, datos, false);
    }

    @Override
    public List<ReporteDto> consultarMisReportes(UUID idActor) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var reporteNegocio = new ReporteNegocioImpl(daoFactory);

        try {
            return ReporteDtoAssembler.getInstance().convertirADTO(reporteNegocio.consultarMisReportes(idActor));
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.ModeracionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<ContenidoReportadoDto> consultarReportesAbiertos(UUID idActor) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var reporteNegocio = new ReporteNegocioImpl(daoFactory);

        try {
            return ContenidoReportadoDtoAssembler.getInstance().agrupar(reporteNegocio.consultarReportesAbiertos(idActor));
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.ModeracionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    private void reportar(UUID idActor, UUID idContenido, ReporteDto datos, boolean esKudo) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var reporteNegocio = new ReporteNegocioImpl(daoFactory);
        var datosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(datos, new ReporteDto());

        daoFactory.iniciarTransaccion();
        try {
            if (esKudo) {
                reporteNegocio.reportarKudo(idActor, idContenido, datosSeguros.getMotivo(), datosSeguros.getDescripcion());
            } else {
                reporteNegocio.reportarReplica(idActor, idContenido, datosSeguros.getMotivo(), datosSeguros.getDescripcion());
            }
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.ModeracionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_REPORTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
