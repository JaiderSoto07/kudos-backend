package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.UsuarioDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class UsuarioDtoAssembler implements DTOAssembler<UsuarioDominio, UsuarioDto> {

    private static final DTOAssembler<UsuarioDominio, UsuarioDto> INSTANCIA = new UsuarioDtoAssembler();

    private UsuarioDtoAssembler() {
    }

    public static DTOAssembler<UsuarioDominio, UsuarioDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public UsuarioDto convertirADTO(UsuarioDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new UsuarioDominio.Builder().build());
        var dto = new UsuarioDto();
        dto.setId(dominioSeguro.getId());
        dto.setNombres(dominioSeguro.getNombres());
        dto.setApellidos(dominioSeguro.getApellidos());
        dto.setCorreoInstitucional(dominioSeguro.getCorreoInstitucional());
        dto.setEstadoCuenta(UtilEnum.obtenerTexto(dominioSeguro.getEstadoCuenta()));
        return dto;
    }

    @Override
    public UsuarioDominio convertirADominio(UsuarioDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new UsuarioDto());
        return new UsuarioDominio.Builder()
                .id(dtoSeguro.getId())
                .nombres(dtoSeguro.getNombres())
                .apellidos(dtoSeguro.getApellidos())
                .correoInstitucional(dtoSeguro.getCorreoInstitucional())
                .estadoCuenta(UtilEnum.obtenerValor(EstadoCuenta.class, dtoSeguro.getEstadoCuenta(), EstadoCuenta.PENDIENTE_APROBACION))
                .build();
    }

    @Override
    public List<UsuarioDto> convertirADTO(List<UsuarioDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<UsuarioDominio>());
        var listaDto = new ArrayList<UsuarioDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
