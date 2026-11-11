package co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.EstadoReporte;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.entidad.ReplicaEntidad;
import co.edu.uco.kudos.entidad.ReporteEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;

import java.util.UUID;

public final class AsegurarSinReporteAbiertoRule implements Rule<Object> {

    private static final AsegurarSinReporteAbiertoRule INSTANCIA = new AsegurarSinReporteAbiertoRule();

    private AsegurarSinReporteAbiertoRule() {
    }

    public static AsegurarSinReporteAbiertoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var daoFactory = (DAOFactory) datos[0];
        var idReportante = (UUID) datos[1];
        var idKudo = (UUID) datos[2];
        var idReplica = (UUID) datos[3];

        var filtro = new ReporteEntidad.Builder()
                .reportante(new UsuarioEntidad.Builder().id(idReportante).build())
                .kudo(new KudoEntidad.Builder().id(idKudo).build())
                .replica(new ReplicaEntidad.Builder().id(idReplica).build())
                .build();
        for (var reporte : daoFactory.obtenerReporteDAO().consultarPorFiltro(filtro)) {
            if (UtilEnum.obtenerValor(EstadoReporte.class, reporte.getEstado(), EstadoReporte.ACEPTADO).estaAbierto()) {
                throw KudosNegocioExcepcion.crear(CatalogoMensajes.Moderacion.REPORTE_ABIERTO);
            }
        }
    }
}
