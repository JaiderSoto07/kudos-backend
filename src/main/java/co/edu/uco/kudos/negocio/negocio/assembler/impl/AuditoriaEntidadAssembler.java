package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.entidad.AuditoriaEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class AuditoriaEntidadAssembler implements EntidadAssembler<AuditoriaDominio, AuditoriaEntidad> {

    private static final EntidadAssembler<AuditoriaDominio, AuditoriaEntidad> INSTANCIA = new AuditoriaEntidadAssembler();

    private AuditoriaEntidadAssembler() {
    }

    public static EntidadAssembler<AuditoriaDominio, AuditoriaEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public AuditoriaEntidad convertirAEntidad(AuditoriaDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new AuditoriaDominio.Builder().build());
        return new AuditoriaEntidad.Builder()
                .id(dominioSeguro.getId())
                .fechaHora(dominioSeguro.getFechaHora())
                .actor(UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getActor()))
                .accion(dominioSeguro.getAccion())
                .tipoObjeto(dominioSeguro.getTipoObjeto())
                .idObjeto(dominioSeguro.getIdObjeto())
                .estadoAnterior(dominioSeguro.getEstadoAnterior())
                .estadoNuevo(dominioSeguro.getEstadoNuevo())
                .motivo(dominioSeguro.getMotivo())
                .detalle(dominioSeguro.getDetalle())
                .build();
    }

    @Override
    public AuditoriaDominio convertirADominio(AuditoriaEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new AuditoriaEntidad.Builder().build());
        return new AuditoriaDominio.Builder()
                .id(entidadSegura.getId())
                .fechaHora(entidadSegura.getFechaHora())
                .actor(UsuarioEntidadAssembler.getInstance().convertirADominio(entidadSegura.getActor()))
                .accion(entidadSegura.getAccion())
                .tipoObjeto(entidadSegura.getTipoObjeto())
                .idObjeto(entidadSegura.getIdObjeto())
                .estadoAnterior(entidadSegura.getEstadoAnterior())
                .estadoNuevo(entidadSegura.getEstadoNuevo())
                .motivo(entidadSegura.getMotivo())
                .detalle(entidadSegura.getDetalle())
                .build();
    }

    @Override
    public List<AuditoriaDominio> convertirADominio(List<AuditoriaEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<AuditoriaEntidad>());
        var listaDominio = new ArrayList<AuditoriaDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
