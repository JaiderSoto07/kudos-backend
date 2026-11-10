package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dominio.TipoVinculo;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dominio.VinculacionProgramaDominio;
import co.edu.uco.kudos.dto.VinculoDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class VinculoDtoAssembler implements DTOAssembler<VinculacionProgramaDominio, VinculoDto> {

    private static final DTOAssembler<VinculacionProgramaDominio, VinculoDto> INSTANCIA = new VinculoDtoAssembler();

    private VinculoDtoAssembler() {
    }

    public static DTOAssembler<VinculacionProgramaDominio, VinculoDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public VinculoDto convertirADTO(VinculacionProgramaDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new VinculacionProgramaDominio.Builder().build());
        var dto = new VinculoDto();
        dto.setId(dominioSeguro.getId());
        dto.setIdUsuario(dominioSeguro.getUsuario().getId());
        dto.setNombrePersona(dominioSeguro.getUsuario().nombreCompleto());
        dto.setCorreo(dominioSeguro.getUsuario().getCorreoInstitucional());
        dto.setTipoVinculo(UtilEnum.obtenerTexto(dominioSeguro.getTipoVinculo()));
        dto.setIdPrograma(dominioSeguro.getPrograma().getId());
        dto.setNombrePrograma(dominioSeguro.getPrograma().getNombre());
        dto.setFechaInicio(dominioSeguro.getFechaInicio());
        dto.setFechaFin(dominioSeguro.getFechaFin());
        dto.setMotivoDesvinculacion(dominioSeguro.getMotivoDesvinculacion());
        dto.setVigente(dominioSeguro.estaVigente());
        return dto;
    }

    @Override
    public VinculacionProgramaDominio convertirADominio(VinculoDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new VinculoDto());
        return new VinculacionProgramaDominio.Builder()
                .id(dtoSeguro.getId())
                .usuario(new UsuarioDominio.Builder().id(dtoSeguro.getIdUsuario()).correoInstitucional(dtoSeguro.getCorreo()).build())
                .programa(new ProgramaAcademicoDominio.Builder().id(dtoSeguro.getIdPrograma()).nombre(dtoSeguro.getNombrePrograma()).build())
                .tipoVinculo(UtilEnum.obtenerValor(TipoVinculo.class, dtoSeguro.getTipoVinculo(), TipoVinculo.ESTUDIANTE))
                .fechaInicio(dtoSeguro.getFechaInicio())
                .fechaFin(dtoSeguro.getFechaFin())
                .motivoDesvinculacion(dtoSeguro.getMotivoDesvinculacion())
                .build();
    }

    @Override
    public List<VinculoDto> convertirADTO(List<VinculacionProgramaDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<VinculacionProgramaDominio>());
        var listaDto = new ArrayList<VinculoDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
