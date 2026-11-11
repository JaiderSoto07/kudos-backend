package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.PersonaNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonaNegocioImpl implements PersonaNegocio {

    private static final int MAXIMO_RESULTADOS = 20;

    private final DAOFactory daoFactory;

    public PersonaNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<UsuarioDominio> buscarActivas(UUID idActor, String texto) {
        var buscado = normalizar(texto);
        var filtro = new UsuarioEntidad.Builder().estadoCuenta(EstadoCuenta.ACTIVA.name()).build();
        var activas = UsuarioEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerUsuarioDAO().consultarPorFiltro(filtro));

        var encontradas = new ArrayList<UsuarioDominio>();
        for (var persona : activas) {
            var coincide = buscado.isEmpty()
                    || normalizar(persona.nombreCompleto()).contains(buscado)
                    || normalizar(persona.getCorreoInstitucional()).contains(buscado);
            if (coincide && persona.estaActiva() && !persona.getId().equals(idActor) && encontradas.size() < MAXIMO_RESULTADOS) {
                encontradas.add(persona);
            }
        }
        return encontradas;
    }

    private String normalizar(String valor) {
        return UtilTexto.getUtilTexto().normalizarParaBuscar(valor);
    }
}
