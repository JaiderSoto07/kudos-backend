package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.AuditoriaEntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

public class AuditoriaNegocioImpl implements AuditoriaNegocio {

    private final DAOFactory daoFactory;

    public AuditoriaNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void registrar(AuditoriaDominio datos) {
        var auditoriaConIdYFecha = new AuditoriaDominio.Builder()
                .id(UtilId.generarId())
                .fechaHora(UtilFecha.fechaActual())
                .actor(datos.getActor())
                .accion(datos.getAccion())
                .tipoObjeto(datos.getTipoObjeto())
                .idObjeto(datos.getIdObjeto())
                .estadoAnterior(datos.getEstadoAnterior())
                .estadoNuevo(datos.getEstadoNuevo())
                .motivo(datos.getMotivo())
                .detalle(datos.getDetalle())
                .build();

        var auditoriaEntidad = AuditoriaEntidadAssembler.getInstance().convertirAEntidad(auditoriaConIdYFecha);
        daoFactory.obtenerAuditoriaDAO().crear(auditoriaEntidad);
    }
}
