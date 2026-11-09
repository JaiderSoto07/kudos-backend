package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.dto.CategoriaDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class CategoriaDtoAssembler implements DTOAssembler<CategoriaDominio, CategoriaDto> {

    private static final DTOAssembler<CategoriaDominio, CategoriaDto> INSTANCIA = new CategoriaDtoAssembler();

    private CategoriaDtoAssembler() {
    }

    public static DTOAssembler<CategoriaDominio, CategoriaDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public CategoriaDto convertirADTO(CategoriaDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new CategoriaDominio.Builder().build());
        var dto = new CategoriaDto();
        dto.setId(dominioSeguro.getId());
        dto.setNombre(dominioSeguro.getNombre());
        dto.setDescripcion(dominioSeguro.getDescripcion());
        dto.setActiva(dominioSeguro.isActiva());
        dto.setFechaCreacion(dominioSeguro.getFechaCreacion());
        return dto;
    }

    @Override
    public CategoriaDominio convertirADominio(CategoriaDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new CategoriaDto());
        return new CategoriaDominio.Builder()
                .id(dtoSeguro.getId())
                .nombre(dtoSeguro.getNombre())
                .descripcion(dtoSeguro.getDescripcion())
                .activa(dtoSeguro.isActiva())
                .fechaCreacion(dtoSeguro.getFechaCreacion())
                .build();
    }

    @Override
    public List<CategoriaDto> convertirADTO(List<CategoriaDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<CategoriaDominio>());
        var listaDto = new ArrayList<CategoriaDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
