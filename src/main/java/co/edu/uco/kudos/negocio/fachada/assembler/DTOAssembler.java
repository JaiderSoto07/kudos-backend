package co.edu.uco.kudos.negocio.fachada.assembler;

import java.util.List;

public interface DTOAssembler<D, E> {

    E convertirADTO(D dominio);

    D convertirADominio(E dto);

    List<E> convertirADTO(List<D> listaDominios);
}
