package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.AutoridadDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class AutoridadDtoAssembler implements DTOAssembler<UsuarioDominio, AutoridadDto> {

    private static final DTOAssembler<UsuarioDominio, AutoridadDto> INSTANCIA = new AutoridadDtoAssembler();

    private AutoridadDtoAssembler() {
    }

    public static DTOAssembler<UsuarioDominio, AutoridadDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public AutoridadDto convertirADTO(UsuarioDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new UsuarioDominio.Builder().build());
        var dto = new AutoridadDto();
        dto.setNombres(dominioSeguro.getNombres());
        dto.setApellidos(dominioSeguro.getApellidos());
        dto.setCorreoInstitucional(dominioSeguro.getCorreoInstitucional());
        dto.setNumeroCelular(dominioSeguro.getNumeroCelular());
        return dto;
    }

    @Override
    public UsuarioDominio convertirADominio(AutoridadDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new AutoridadDto());
        return new UsuarioDominio.Builder()
                .nombres(dtoSeguro.getNombres())
                .apellidos(dtoSeguro.getApellidos())
                .correoInstitucional(dtoSeguro.getCorreoInstitucional())
                .numeroCelular(dtoSeguro.getNumeroCelular())
                .build();
    }

    @Override
    public List<AutoridadDto> convertirADTO(List<UsuarioDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<UsuarioDominio>());
        var listaDto = new ArrayList<AutoridadDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
