package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.dominio.TipoNotificacion;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.SolicitudRegistroEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.CuentaNegocio;
import co.edu.uco.kudos.negocio.negocio.NotificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.VerificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.SolicitudRegistroEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autenticacion.AsegurarUsuarioRegistradoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarCelularNoRepetidoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarCorreoNoRepetidoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarValorDiferenteAlActualRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.ValidarCelularRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.ValidarCorreoInstitucionalRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.ValidarDatosPersonalesRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.UUID;

public class CuentaNegocioImpl implements CuentaNegocio {

    private static final String TIPO_OBJETO = "Usuario";
    private static final String ACCION_DATOS_ACTUALIZADOS = "DatosPersonalesActualizados";
    private static final String ACCION_CELULAR_CAMBIADO = "CelularCambiado";
    private static final String ACCION_CORREO_CAMBIADO = "CorreoCambiado";

    private final DAOFactory daoFactory;
    private final VerificacionNegocio verificacionNegocio;
    private final NotificacionNegocio notificacionNegocio;
    private final AuditoriaNegocio auditoriaNegocio;

    public CuentaNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.verificacionNegocio = new VerificacionNegocioImpl(daoFactory);
        this.notificacionNegocio = new NotificacionNegocioImpl(daoFactory);
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public UsuarioDominio consultarUsuario(UUID idUsuario) {
        var usuario = UsuarioEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerUsuarioDAO().consultarPorId(idUsuario));
        AsegurarUsuarioRegistradoRule.obtenerInstancia().ejecutar(usuario);
        return usuario;
    }

    @Override
    public SolicitudRegistroDominio consultarUltimaSolicitud(UUID idUsuario) {
        var filtro = new SolicitudRegistroEntidad.Builder()
                .solicitante(new UsuarioEntidad.Builder().id(idUsuario).build())
                .build();
        var solicitudes = daoFactory.obtenerSolicitudRegistroDAO().consultarPorFiltro(filtro);
        if (solicitudes.isEmpty()) {
            return new SolicitudRegistroDominio.Builder().build();
        }
        return SolicitudRegistroEntidadAssembler.getInstance().convertirADominio(solicitudes.getFirst());
    }

    @Override
    public String consultarUltimoCodigoEnviado(UUID idUsuario) {
        var usuario = consultarUsuario(idUsuario);
        var codigoPendiente = verificacionNegocio.consultarCodigoPendiente(idUsuario);
        if (usuario.isCelularVerificado() || UtilId.esValorDefecto(codigoPendiente.getId())) {
            return UtilTexto.VACIO;
        }
        for (var notificacion : notificacionNegocio.consultarPorTipo(idUsuario, TipoNotificacion.CODIGO_VERIFICACION)) {
            if (notificacion.getIdReferencia().equals(codigoPendiente.getId())) {
                return notificacion.getMensaje();
            }
        }
        return UtilTexto.VACIO;
    }

    @Override
    public void actualizarDatosPersonales(UUID idUsuario, UsuarioDominio datos) {
        var usuario = consultarUsuario(idUsuario);
        ValidarDatosPersonalesRule.obtenerInstancia().ejecutar(datos.getNombres(), datos.getApellidos());

        var nombreAnterior = usuario.nombreCompleto();
        usuario.actualizarDatosPersonales(datos.getNombres(), datos.getApellidos());
        guardarCambios(usuario);

        registrarAuditoria(idUsuario, ACCION_DATOS_ACTUALIZADOS, nombreAnterior, usuario.nombreCompleto());
    }

    @Override
    public void cambiarCelular(UUID idUsuario, UsuarioDominio datos) {
        var usuario = consultarUsuario(idUsuario);
        var celularNuevo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(datos.getNumeroCelular());
        ValidarCelularRule.obtenerInstancia().ejecutar(celularNuevo);
        AsegurarValorDiferenteAlActualRule.obtenerInstancia().ejecutar(celularNuevo, usuario.getNumeroCelular(),
                CatalogoMensajes.Identidad.CELULAR_IGUAL_AL_ACTUAL);
        AsegurarCelularNoRepetidoRule.obtenerInstancia().ejecutar(celularNuevo, idUsuario, daoFactory);

        var celularAnterior = usuario.getNumeroCelular();
        usuario.cambiarCelular(celularNuevo);
        guardarCambios(usuario);
        verificacionNegocio.enviarCodigoCelular(usuario);

        registrarAuditoria(idUsuario, ACCION_CELULAR_CAMBIADO, celularAnterior, usuario.getNumeroCelular());
    }

    @Override
    public void cambiarCorreo(UUID idUsuario, UsuarioDominio datos) {
        var usuario = consultarUsuario(idUsuario);
        var correoNuevo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(datos.getCorreoInstitucional()).toLowerCase();
        ValidarCorreoInstitucionalRule.obtenerInstancia().ejecutar(correoNuevo, daoFactory);
        AsegurarValorDiferenteAlActualRule.obtenerInstancia().ejecutar(correoNuevo, usuario.getCorreoInstitucional(),
                CatalogoMensajes.Identidad.CORREO_IGUAL_AL_ACTUAL);
        AsegurarCorreoNoRepetidoRule.obtenerInstancia().ejecutar(correoNuevo, idUsuario, daoFactory);

        var correoAnterior = usuario.getCorreoInstitucional();
        usuario.cambiarCorreo(correoNuevo);
        guardarCambios(usuario);

        registrarAuditoria(idUsuario, ACCION_CORREO_CAMBIADO, correoAnterior, usuario.getCorreoInstitucional());
    }

    private void guardarCambios(UsuarioDominio usuario) {
        daoFactory.obtenerUsuarioDAO().actualizar(usuario.getId(), UsuarioEntidadAssembler.getInstance().convertirAEntidad(usuario));
    }

    private void registrarAuditoria(UUID idUsuario, String accion, String estadoAnterior, String estadoNuevo) {
        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idUsuario).build())
                .accion(accion)
                .tipoObjeto(TIPO_OBJETO)
                .idObjeto(idUsuario)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
