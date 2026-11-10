package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.TipoVinculo;
import co.edu.uco.kudos.dominio.VinculacionProgramaDominio;
import co.edu.uco.kudos.entidad.VinculacionProgramaEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class VinculacionProgramaEntidadAssembler implements EntidadAssembler<VinculacionProgramaDominio, VinculacionProgramaEntidad> {

    private static final EntidadAssembler<VinculacionProgramaDominio, VinculacionProgramaEntidad> INSTANCIA = new VinculacionProgramaEntidadAssembler();

    private VinculacionProgramaEntidadAssembler() {
    }

    public static EntidadAssembler<VinculacionProgramaDominio, VinculacionProgramaEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public VinculacionProgramaEntidad convertirAEntidad(VinculacionProgramaDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new VinculacionProgramaDominio.Builder().build());
        return new VinculacionProgramaEntidad.Builder()
                .id(dominioSeguro.getId())
                .usuario(UsuarioEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getUsuario()))
                .programa(ProgramaAcademicoEntidadAssembler.getInstance().convertirAEntidad(dominioSeguro.getPrograma()))
                .tipoVinculo(UtilEnum.obtenerTexto(dominioSeguro.getTipoVinculo()))
                .fechaInicio(dominioSeguro.getFechaInicio())
                .fechaFin(dominioSeguro.getFechaFin())
                .motivoDesvinculacion(dominioSeguro.getMotivoDesvinculacion())
                .build();
    }

    @Override
    public VinculacionProgramaDominio convertirADominio(VinculacionProgramaEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new VinculacionProgramaEntidad.Builder().build());
        return new VinculacionProgramaDominio.Builder()
                .id(entidadSegura.getId())
                .usuario(UsuarioEntidadAssembler.getInstance().convertirADominio(entidadSegura.getUsuario()))
                .programa(ProgramaAcademicoEntidadAssembler.getInstance().convertirADominio(entidadSegura.getPrograma()))
                .tipoVinculo(UtilEnum.obtenerValor(TipoVinculo.class, entidadSegura.getTipoVinculo(), TipoVinculo.ESTUDIANTE))
                .fechaInicio(entidadSegura.getFechaInicio())
                .fechaFin(entidadSegura.getFechaFin())
                .motivoDesvinculacion(entidadSegura.getMotivoDesvinculacion())
                .build();
    }

    @Override
    public List<VinculacionProgramaDominio> convertirADominio(List<VinculacionProgramaEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<VinculacionProgramaEntidad>());
        var listaDominio = new ArrayList<VinculacionProgramaDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
