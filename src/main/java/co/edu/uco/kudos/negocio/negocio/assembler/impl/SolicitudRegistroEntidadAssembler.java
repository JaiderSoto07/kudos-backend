package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.EstadoSolicitud;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.entidad.SolicitudRegistroEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class SolicitudRegistroEntidadAssembler implements EntidadAssembler<SolicitudRegistroDominio, SolicitudRegistroEntidad> {

    private static final EntidadAssembler<SolicitudRegistroDominio, SolicitudRegistroEntidad> INSTANCIA = new SolicitudRegistroEntidadAssembler();

    private SolicitudRegistroEntidadAssembler() {
    }

    public static EntidadAssembler<SolicitudRegistroDominio, SolicitudRegistroEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public SolicitudRegistroEntidad convertirAEntidad(SolicitudRegistroDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new SolicitudRegistroDominio.Builder().build());
        return new SolicitudRegistroEntidad.Builder()
                .id(dominioSeguro.getId())
                .solicitante(UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getSolicitante()))
                .rolSolicitado(UtilEnum.obtenerTexto(dominioSeguro.getRolSolicitado()))
                .facultad(FacultadEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getFacultad()))
                .programa(ProgramaAcademicoEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getPrograma()))
                .estado(UtilEnum.obtenerTexto(dominioSeguro.getEstado()))
                .fechaSolicitud(dominioSeguro.getFechaSolicitud())
                .decididaPor(UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getDecididaPor()))
                .fechaDecision(dominioSeguro.getFechaDecision())
                .motivoRechazo(dominioSeguro.getMotivoRechazo())
                .build();
    }

    @Override
    public SolicitudRegistroDominio convertirADominio(SolicitudRegistroEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new SolicitudRegistroEntidad.Builder().build());
        return new SolicitudRegistroDominio.Builder()
                .id(entidadSegura.getId())
                .solicitante(UsuarioEntidadAssembler.getInstance().convertirADominio(entidadSegura.getSolicitante()))
                .rolSolicitado(UtilEnum.obtenerValor(Rol.class, entidadSegura.getRolSolicitado(), Rol.ESTUDIANTE))
                .facultad(FacultadEntidadAssembler.getInstance().convertirADominio(entidadSegura.getFacultad()))
                .programa(ProgramaAcademicoEntidadAssembler.getInstance().convertirADominio(entidadSegura.getPrograma()))
                .estado(UtilEnum.obtenerValor(EstadoSolicitud.class, entidadSegura.getEstado(), EstadoSolicitud.PENDIENTE))
                .fechaSolicitud(entidadSegura.getFechaSolicitud())
                .decididaPor(UsuarioEntidadAssembler.getInstance().convertirADominio(entidadSegura.getDecididaPor()))
                .fechaDecision(entidadSegura.getFechaDecision())
                .motivoRechazo(entidadSegura.getMotivoRechazo())
                .build();
    }

    @Override
    public List<SolicitudRegistroDominio> convertirADominio(List<SolicitudRegistroEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<SolicitudRegistroEntidad>());
        var listaDominio = new ArrayList<SolicitudRegistroDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
