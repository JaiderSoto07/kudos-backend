package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class AsignacionRolEntidadAssembler implements EntidadAssembler<AsignacionRolDominio, AsignacionRolEntidad> {

    private static final EntidadAssembler<AsignacionRolDominio, AsignacionRolEntidad> INSTANCIA = new AsignacionRolEntidadAssembler();

    private AsignacionRolEntidadAssembler() {
    }

    public static EntidadAssembler<AsignacionRolDominio, AsignacionRolEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public AsignacionRolEntidad convertirAEntidad(AsignacionRolDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new AsignacionRolDominio.Builder().build());
        return new AsignacionRolEntidad.Builder()
                .id(dominioSeguro.getId())
                .usuario(UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getUsuario()))
                .rol(UtilEnum.obtenerTexto(dominioSeguro.getRol()))
                .facultad(FacultadEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getFacultad()))
                .programa(ProgramaAcademicoEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getPrograma()))
                .fechaInicio(dominioSeguro.getFechaInicio())
                .fechaFin(dominioSeguro.getFechaFin())
                .motivoFin(dominioSeguro.getMotivoFin())
                .build();
    }

    @Override
    public AsignacionRolDominio convertirADominio(AsignacionRolEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new AsignacionRolEntidad.Builder().build());
        return new AsignacionRolDominio.Builder()
                .id(entidadSegura.getId())
                .usuario(UsuarioEntidadAssembler.getInstance().convertirADominio(entidadSegura.getUsuario()))
                .rol(UtilEnum.obtenerValor(Rol.class, entidadSegura.getRol(), Rol.ESTUDIANTE))
                .facultad(FacultadEntidadAssembler.getInstance().convertirADominio(entidadSegura.getFacultad()))
                .programa(ProgramaAcademicoEntidadAssembler.getInstance().convertirADominio(entidadSegura.getPrograma()))
                .fechaInicio(entidadSegura.getFechaInicio())
                .fechaFin(entidadSegura.getFechaFin())
                .motivoFin(entidadSegura.getMotivoFin())
                .build();
    }

    @Override
    public List<AsignacionRolDominio> convertirADominio(List<AsignacionRolEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<AsignacionRolEntidad>());
        var listaDominio = new ArrayList<AsignacionRolDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
