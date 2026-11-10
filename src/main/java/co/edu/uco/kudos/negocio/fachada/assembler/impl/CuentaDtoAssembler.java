package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.CuentaDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class CuentaDtoAssembler implements DTOAssembler<UsuarioDominio, CuentaDto> {

    private static final DTOAssembler<UsuarioDominio, CuentaDto> INSTANCIA = new CuentaDtoAssembler();

    private CuentaDtoAssembler() {
    }

    public static DTOAssembler<UsuarioDominio, CuentaDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public CuentaDto convertirADTO(UsuarioDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new UsuarioDominio.Builder().build());
        var dto = new CuentaDto();
        dto.setId(dominioSeguro.getId());
        dto.setNombres(dominioSeguro.getNombres());
        dto.setApellidos(dominioSeguro.getApellidos());
        dto.setCorreoInstitucional(dominioSeguro.getCorreoInstitucional());
        dto.setCorreoVerificado(dominioSeguro.isCorreoVerificado());
        dto.setNumeroCelular(dominioSeguro.getNumeroCelular());
        dto.setCelularVerificado(dominioSeguro.isCelularVerificado());
        dto.setAprobada(dominioSeguro.isAprobada());
        dto.setEstadoCuenta(UtilEnum.obtenerTexto(dominioSeguro.getEstadoCuenta()));
        dto.setMensajeEstado(dominioSeguro.motivoParaNoIngresar());
        return dto;
    }

    @Override
    public UsuarioDominio convertirADominio(CuentaDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new CuentaDto());
        return new UsuarioDominio.Builder()
                .id(dtoSeguro.getId())
                .nombres(dtoSeguro.getNombres())
                .apellidos(dtoSeguro.getApellidos())
                .correoInstitucional(dtoSeguro.getCorreoInstitucional())
                .numeroCelular(dtoSeguro.getNumeroCelular())
                .build();
    }

    @Override
    public List<CuentaDto> convertirADTO(List<UsuarioDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<UsuarioDominio>());
        var listaDto = new ArrayList<CuentaDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
