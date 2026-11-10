package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.PersonaDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class PersonaDtoAssembler implements DTOAssembler<UsuarioDominio, PersonaDto> {

    private static final DTOAssembler<UsuarioDominio, PersonaDto> INSTANCIA = new PersonaDtoAssembler();

    private PersonaDtoAssembler() {
    }

    public static DTOAssembler<UsuarioDominio, PersonaDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public PersonaDto convertirADTO(UsuarioDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new UsuarioDominio.Builder().build());
        var dto = new PersonaDto();
        dto.setId(dominioSeguro.getId());
        dto.setNombre(dominioSeguro.nombreCompleto());
        dto.setCorreo(dominioSeguro.getCorreoInstitucional());
        return dto;
    }

    @Override
    public UsuarioDominio convertirADominio(PersonaDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new PersonaDto());
        return new UsuarioDominio.Builder().id(dtoSeguro.getId()).correoInstitucional(dtoSeguro.getCorreo()).build();
    }

    @Override
    public List<PersonaDto> convertirADTO(List<UsuarioDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<UsuarioDominio>());
        var listaDto = new ArrayList<PersonaDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
