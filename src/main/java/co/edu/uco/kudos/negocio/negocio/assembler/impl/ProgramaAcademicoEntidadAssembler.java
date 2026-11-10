package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class ProgramaAcademicoEntidadAssembler implements EntidadAssembler<ProgramaAcademicoDominio, ProgramaAcademicoEntidad> {

    private static final EntidadAssembler<ProgramaAcademicoDominio, ProgramaAcademicoEntidad> INSTANCIA = new ProgramaAcademicoEntidadAssembler();

    private ProgramaAcademicoEntidadAssembler() {
    }

    public static EntidadAssembler<ProgramaAcademicoDominio, ProgramaAcademicoEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public ProgramaAcademicoEntidad convertirAEntidad(ProgramaAcademicoDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new ProgramaAcademicoDominio.Builder().build());
        return new ProgramaAcademicoEntidad.Builder()
                .id(dominioSeguro.getId())
                .nombre(dominioSeguro.getNombre())
                .facultad(FacultadEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getFacultad()))
                .activo(dominioSeguro.isActivo())
                .fechaCreacion(dominioSeguro.getFechaCreacion())
                .build();
    }

    @Override
    public ProgramaAcademicoDominio convertirADominio(ProgramaAcademicoEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new ProgramaAcademicoEntidad.Builder().build());
        return new ProgramaAcademicoDominio.Builder()
                .id(entidadSegura.getId())
                .nombre(entidadSegura.getNombre())
                .facultad(FacultadEntidadAssembler.getInstance().convertirADominio(entidadSegura.getFacultad()))
                .activo(entidadSegura.isActivo())
                .fechaCreacion(entidadSegura.getFechaCreacion())
                .build();
    }

    @Override
    public List<ProgramaAcademicoDominio> convertirADominio(List<ProgramaAcademicoEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<ProgramaAcademicoEntidad>());
        var listaDominio = new ArrayList<ProgramaAcademicoDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
