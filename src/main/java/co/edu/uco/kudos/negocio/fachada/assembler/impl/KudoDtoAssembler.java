package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.KudoDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class KudoDtoAssembler implements DTOAssembler<KudoDominio, KudoDto> {

    private static final DTOAssembler<KudoDominio, KudoDto> INSTANCIA = new KudoDtoAssembler();

    private KudoDtoAssembler() {
    }

    public static DTOAssembler<KudoDominio, KudoDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public KudoDto convertirADTO(KudoDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new KudoDominio.Builder().build());
        var vetado = dominioSeguro.getEstado() == EstadoContenido.VETADO;
        var dto = new KudoDto();
        dto.setId(dominioSeguro.getId());
        dto.setIdAutor(dominioSeguro.getAutor().getId());
        dto.setNombreAutor(dominioSeguro.getAutor().nombreCompleto());
        dto.setIdDestinatario(dominioSeguro.getDestinatario().getId());
        dto.setNombreDestinatario(dominioSeguro.getDestinatario().nombreCompleto());
        dto.setIdCategoria(dominioSeguro.getCategoria().getId());
        dto.setNombreCategoria(dominioSeguro.getCategoria().getNombre());
        dto.setContenido(vetado ? CatalogoMensajes.Replica.CONTENIDO_VETADO : dominioSeguro.getContenido());
        dto.setFechaCreacion(dominioSeguro.getFechaCreacion());
        dto.setEstado(UtilEnum.obtenerTexto(dominioSeguro.getEstado()));
        dto.setVetado(vetado);
        dto.setCantidadVisualizaciones(dominioSeguro.getCantidadVisualizaciones());
        dto.setPromedioCalificacion(dominioSeguro.getPromedioCalificacion());
        dto.setCantidadCalificaciones(dominioSeguro.getCantidadCalificaciones());
        return dto;
    }

    @Override
    public KudoDominio convertirADominio(KudoDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new KudoDto());
        return new KudoDominio.Builder()
                .id(dtoSeguro.getId())
                .autor(new UsuarioDominio.Builder().id(dtoSeguro.getIdAutor()).build())
                .destinatario(new UsuarioDominio.Builder().id(dtoSeguro.getIdDestinatario()).build())
                .categoria(new CategoriaDominio.Builder().id(dtoSeguro.getIdCategoria()).build())
                .contenido(dtoSeguro.getContenido())
                .build();
    }

    @Override
    public List<KudoDto> convertirADTO(List<KudoDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<KudoDominio>());
        var listaDto = new ArrayList<KudoDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
