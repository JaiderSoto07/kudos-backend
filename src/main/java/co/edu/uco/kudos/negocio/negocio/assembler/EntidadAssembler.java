package co.edu.uco.kudos.negocio.negocio.assembler;

import java.util.List;

public interface EntidadAssembler<D, E> {

    E convertirAEntidad(D dominio);

    D convertirADominio(E entidad);

    List<D> convertirADominio(List<E> listaEntidades);
}
