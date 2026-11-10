package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.AutenticacionNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autenticacion.AsegurarCuentaActivaRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autenticacion.AsegurarUsuarioRegistradoRule;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.UUID;

public class AutenticacionNegocioImpl implements AutenticacionNegocio {

    private static final String TIPO_OBJETO = "Usuario";
    private static final String ACCION_CORREO_VERIFICADO = "CorreoVerificado";
    private static final String ESTADO_SIN_VERIFICAR = "SIN_VERIFICAR";
    private static final String ESTADO_VERIFICADO = "VERIFICADO";

    private final DAOFactory daoFactory;
    private final AuditoriaNegocio auditoriaNegocio;

    public AutenticacionNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public UUID obtenerIdUsuarioRegistrado(String correo) {
        var usuario = consultarUsuarioPorCorreo(correo);
        AsegurarUsuarioRegistradoRule.obtenerInstancia().ejecutar(usuario);
        return usuario.getId();
    }

    @Override
    public void confirmarCorreoVerificadoPorGoogle(UsuarioDominio usuario) {
        if (UtilId.esValorDefecto(usuario.getId()) || usuario.isCorreoVerificado()) {
            return;
        }
        usuario.verificarCorreo();
        daoFactory.obtenerUsuarioDAO().actualizar(usuario.getId(), UsuarioEntidadAssembler.getInstance().convertirAEntidad(usuario));

        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(usuario.getId()).build())
                .accion(ACCION_CORREO_VERIFICADO)
                .tipoObjeto(TIPO_OBJETO)
                .idObjeto(usuario.getId())
                .estadoAnterior(ESTADO_SIN_VERIFICAR)
                .estadoNuevo(ESTADO_VERIFICADO)
                .build();
        auditoriaNegocio.registrar(auditoria);
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
