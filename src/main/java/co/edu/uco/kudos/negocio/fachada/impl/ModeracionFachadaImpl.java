package co.edu.uco.kudos.negocio.fachada.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dto.DecisionDto;
import co.edu.uco.kudos.dto.VetoDto;
import co.edu.uco.kudos.negocio.fachada.ModeracionFachada;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.VetoDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.ModeracionNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.ModeracionNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ModeracionFachadaImpl implements ModeracionFachada {

    @Override
    public void iniciarRevisionKudo(UUID idActor, UUID idKudo) {
        ejecutarEnTransaccion(negocio -> negocio.iniciarRevisionKudo(idActor, idKudo));
    }

    @Override
    public void iniciarRevisionReplica(UUID idActor, UUID idReplica) {
        ejecutarEnTransaccion(negocio -> negocio.iniciarRevisionReplica(idActor, idReplica));
    }

    @Override
    public void vetarKudo(UUID idActor, UUID idKudo, DecisionDto datos) {
        var motivo = obtenerMotivo(datos);
        ejecutarEnTransaccion(negocio -> negocio.vetarKudo(idActor, idKudo, motivo));
    }

    @Override
    public void vetarReplica(UUID idActor, UUID idReplica, DecisionDto datos) {
        var motivo = obtenerMotivo(datos);
        ejecutarEnTransaccion(negocio -> negocio.vetarReplica(idActor, idReplica, motivo));
    }

    @Override
    public void liberarKudo(UUID idActor, UUID idKudo, DecisionDto datos) {
        var motivo = obtenerMotivo(datos);
        ejecutarEnTransaccion(negocio -> negocio.liberarKudo(idActor, idKudo, motivo));
    }

    @Override
    public void liberarReplica(UUID idActor, UUID idReplica, DecisionDto datos) {
        var motivo = obtenerMotivo(datos);
        ejecutarEnTransaccion(negocio -> negocio.liberarReplica(idActor, idReplica, motivo));
    }

    @Override
    public List<VetoDto> consultarVetos(UUID idActor) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var moderacionNegocio = new ModeracionNegocioImpl(daoFactory);

        try {
            var vetos = new ArrayList<VetoDto>();
            for (var moderacion : moderacionNegocio.consultarVetos(idActor)) {
                vetos.add(VetoDtoAssembler.getInstance().armar(moderacion, moderacionNegocio.consultarReportesResueltos(moderacion.getId())));
            }
            return vetos;
        } catch (KudosExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            var mensajeUsuario = CatalogoMensajes.ModeracionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    private String obtenerMotivo(DecisionDto datos) {
        return UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(datos, new DecisionDto()).getMotivo();
    }

    private void ejecutarEnTransaccion(Consumer<ModeracionNegocio> operacion) {
        var daoFactory = DAOFactory.obtenerFactoria();
        var moderacionNegocio = new ModeracionNegocioImpl(daoFactory);

        daoFactory.iniciarTransaccion();
        try {
            operacion.accept(moderacionNegocio);
            daoFactory.confirmarTransaccion();
        } catch (KudosExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;
        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            var mensajeUsuario = CatalogoMensajes.ModeracionFachada.USUARIO_ERROR_PROBLEMA_INESPERADO_MODERANDO;
            throw KudosFachadaExcepcion.crear(mensajeUsuario, excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
