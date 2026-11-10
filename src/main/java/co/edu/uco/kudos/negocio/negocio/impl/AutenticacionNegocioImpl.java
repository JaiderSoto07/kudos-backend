package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.AutenticacionNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autenticacion.AsegurarCuentaActivaRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autenticacion.AsegurarUsuarioRegistradoRule;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.UUID;

public class AutenticacionNegocioImpl implements AutenticacionNegocio {

    private final DAOFactory daoFactory;

    public AutenticacionNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public UsuarioDominio consultarUsuarioPorCorreo(String correo) {
        var correoNormalizado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(correo).toLowerCase();
        if (UtilTexto.getUtilTexto().esVacia(correoNormalizado)) {
            return new UsuarioDominio.Builder().build();
        }

        var filtro = new UsuarioEntidad.Builder().correoInstitucional(correoNormalizado).build();
        var usuariosEncontrados = daoFactory.obtenerUsuarioDAO().consultarPorFiltro(filtro);
        if (usuariosEncontrados.isEmpty()) {
            return new UsuarioDominio.Builder().build();
        }
        return UsuarioEntidadAssembler.getInstance().convertirADominio(usuariosEncontrados.getFirst());
    }

    @Override
    public Rol consultarRolVigente(UUID idUsuario) {
        var filtro = new AsignacionRolEntidad.Builder()
                .usuario(new UsuarioEntidad.Builder().id(idUsuario).build())
                .build();
        var asignaciones = daoFactory.obtenerAsignacionRolDAO().consultarPorFiltro(filtro);

        for (var asignacion : asignaciones) {
            if (UtilFecha.esValorDefecto(asignacion.getFechaFin())) {
                return UtilEnum.obtenerValor(Rol.class, asignacion.getRol(), null);
            }
        }
        return null;
    }

    @Override
    public UUID obtenerIdActorActivo(String correo) {
        var usuario = consultarUsuarioPorCorreo(correo);
        AsegurarUsuarioRegistradoRule.obtenerInstancia().ejecutar(usuario);
        AsegurarCuentaActivaRule.obtenerInstancia().ejecutar(usuario);
        return usuario.getId();
    }
}
