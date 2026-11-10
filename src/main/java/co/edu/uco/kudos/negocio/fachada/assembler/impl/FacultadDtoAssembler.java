package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.EstadoFacultad;
import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dto.FacultadDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class FacultadDtoAssembler implements DTOAssembler<FacultadDominio, FacultadDto> {

    private static final DTOAssembler<FacultadDominio, FacultadDto> INSTANCIA = new FacultadDtoAssembler();

    private FacultadDtoAssembler() {
    }

    public static DTOAssembler<FacultadDominio, FacultadDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public FacultadDto convertirADTO(FacultadDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new FacultadDominio.Builder().build());
        var dto = new FacultadDto();
        dto.setId(dominioSeguro.getId());
        dto.setNombre(dominioSeguro.getNombre());
        dto.setEstado(UtilEnum.obtenerTexto(dominioSeguro.getEstado()));
        return dto;
    }

    @Override
    public FacultadDominio convertirADominio(FacultadDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new FacultadDto());
        return new FacultadDominio.Builder()
                .id(dtoSeguro.getId())
                .nombre(dtoSeguro.getNombre())
                .estado(UtilEnum.obtenerValor(EstadoFacultad.class, dtoSeguro.getEstado(), EstadoFacultad.EN_CONFORMACION))
                .build();
    }

    @Override
    public List<FacultadDto> convertirADTO(List<FacultadDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<FacultadDominio>());
        var listaDto = new ArrayList<FacultadDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
