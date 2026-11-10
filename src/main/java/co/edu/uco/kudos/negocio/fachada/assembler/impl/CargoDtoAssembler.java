package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.CargoDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class CargoDtoAssembler implements DTOAssembler<AsignacionRolDominio, CargoDto> {

    private static final DTOAssembler<AsignacionRolDominio, CargoDto> INSTANCIA = new CargoDtoAssembler();

    private CargoDtoAssembler() {
    }

    public static DTOAssembler<AsignacionRolDominio, CargoDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public CargoDto convertirADTO(AsignacionRolDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new AsignacionRolDominio.Builder().build());
        var dto = new CargoDto();
        dto.setIdUsuario(dominioSeguro.getUsuario().getId());
        dto.setNombrePersona(dominioSeguro.getUsuario().nombreCompleto());
        dto.setCorreo(dominioSeguro.getUsuario().getCorreoInstitucional());
        dto.setRol(UtilEnum.obtenerTexto(dominioSeguro.getRol()));
        dto.setIdFacultad(dominioSeguro.getFacultad().getId());
        dto.setNombreFacultad(dominioSeguro.getFacultad().getNombre());
        dto.setIdPrograma(dominioSeguro.getPrograma().getId());
        dto.setNombrePrograma(dominioSeguro.getPrograma().getNombre());
        dto.setFechaInicio(dominioSeguro.getFechaInicio());
        dto.setFechaFin(dominioSeguro.getFechaFin());
        dto.setMotivoFin(dominioSeguro.getMotivoFin());
        dto.setVigente(dominioSeguro.estaVigente());
        return dto;
    }

    @Override
    public AsignacionRolDominio convertirADominio(CargoDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new CargoDto());
        return new AsignacionRolDominio.Builder()
                .usuario(new UsuarioDominio.Builder().id(dtoSeguro.getIdUsuario()).build())
                .rol(UtilEnum.obtenerValor(Rol.class, dtoSeguro.getRol(), Rol.ESTUDIANTE))
                .facultad(new FacultadDominio.Builder().id(dtoSeguro.getIdFacultad()).nombre(dtoSeguro.getNombreFacultad()).build())
                .programa(new ProgramaAcademicoDominio.Builder().id(dtoSeguro.getIdPrograma()).nombre(dtoSeguro.getNombrePrograma()).build())
                .fechaInicio(dtoSeguro.getFechaInicio())
                .fechaFin(dtoSeguro.getFechaFin())
                .motivoFin(dtoSeguro.getMotivoFin())
                .build();
    }

    @Override
    public List<CargoDto> convertirADTO(List<AsignacionRolDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<AsignacionRolDominio>());
        var listaDto = new ArrayList<CargoDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
