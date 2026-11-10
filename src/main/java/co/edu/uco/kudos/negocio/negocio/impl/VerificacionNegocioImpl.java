package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.CanalVerificacion;
import co.edu.uco.kudos.dominio.CodigoVerificacionDominio;
import co.edu.uco.kudos.dominio.ParametroGlobalDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.CodigoVerificacionEntidad;
import co.edu.uco.kudos.entidad.ParametroGlobalEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.VerificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.CodigoVerificacionEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ParametroGlobalEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.enviador.EnviadorNotificacion;
import co.edu.uco.kudos.negocio.negocio.enviador.impl.EnviadorNotificacionSimulado;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autenticacion.AsegurarUsuarioRegistradoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarCelularPendienteDeVerificarRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarCodigoVigenteRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

public class VerificacionNegocioImpl implements VerificacionNegocio {

    private static final SecureRandom GENERADOR_CODIGOS = new SecureRandom();
    private static final int LIMITE_CODIGO = 1_000_000;
    private static final String FORMATO_CODIGO = "%06d";
    private static final int VIGENCIA_POR_DEFECTO = 15;
    private static final int INTENTOS_POR_DEFECTO = 5;
    private static final String TIPO_OBJETO = "Usuario";
    private static final String ACCION_CELULAR_VERIFICADO = "CelularVerificado";
    private static final String ESTADO_SIN_VERIFICAR = "SIN_VERIFICAR";
    private static final String ESTADO_VERIFICADO = "VERIFICADO";

    private final DAOFactory daoFactory;
    private final EnviadorNotificacion enviadorNotificacion;
    private final AuditoriaNegocio auditoriaNegocio;

    public VerificacionNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.enviadorNotificacion = new EnviadorNotificacionSimulado(new NotificacionNegocioImpl(daoFactory));
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public void enviarCodigoCelular(UsuarioDominio usuario) {
        invalidarCodigosPendientes(usuario.getId());

        var vigenciaMinutos = consultarParametroEntero(ParametroGlobalDominio.VIGENCIA_CODIGO_MINUTOS, VIGENCIA_POR_DEFECTO);
        var ahora = UtilFecha.fechaActual();
        var codigoNuevo = new CodigoVerificacionDominio.Builder()
                .id(UtilId.generarId())
                .usuario(usuario)
                .canal(CanalVerificacion.CELULAR)
                .destino(usuario.getNumeroCelular())
                .codigo(generarCodigo())
                .fechaGeneracion(ahora)
                .fechaExpiracion(ahora.plusMinutes(vigenciaMinutos))
                .intentosFallidos(0)
                .usado(false)
                .build();
        daoFactory.obtenerCodigoVerificacionDAO().crear(CodigoVerificacionEntidadAssembler.getInstance().convertirAEntidad(codigoNuevo));

        var mensaje = String.format(CatalogoMensajes.Identidad.MENSAJE_CODIGO_VERIFICACION, codigoNuevo.getCodigo(), vigenciaMinutos);
        enviadorNotificacion.enviarCodigo(codigoNuevo, mensaje);
    }

    @Override
    public boolean verificarCelular(UUID idUsuario, String codigoIngresado) {
        var maximoIntentos = consultarParametroEntero(ParametroGlobalDominio.MAX_INTENTOS_VERIFICACION, INTENTOS_POR_DEFECTO);
        var codigo = consultarCodigoPendiente(idUsuario);
        AsegurarCodigoVigenteRule.obtenerInstancia().ejecutar(codigo, UtilFecha.fechaActual(), maximoIntentos);

        if (!codigo.getCodigo().equals(UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(codigoIngresado))) {
            codigo.registrarIntentoFallido();
            guardarCodigo(codigo);
            return false;
        }

        codigo.marcarUsado();
        guardarCodigo(codigo);

        var usuario = consultarUsuario(idUsuario);
        usuario.verificarCelular();
        daoFactory.obtenerUsuarioDAO().actualizar(usuario.getId(), UsuarioEntidadAssembler.getInstance().convertirAEntidad(usuario));

        registrarAuditoria(idUsuario);
        return true;
    }

    @Override
    public void reenviarCodigoCelular(UUID idUsuario) {
        var usuario = consultarUsuario(idUsuario);
        AsegurarUsuarioRegistradoRule.obtenerInstancia().ejecutar(usuario);
        AsegurarCelularPendienteDeVerificarRule.obtenerInstancia().ejecutar(usuario);

        enviarCodigoCelular(usuario);
    }

    private String generarCodigo() {
        return String.format(FORMATO_CODIGO, GENERADOR_CODIGOS.nextInt(LIMITE_CODIGO));
    }

    private void invalidarCodigosPendientes(UUID idUsuario) {
        for (var codigo : consultarCodigosCelular(idUsuario)) {
            if (!codigo.isUsado()) {
                codigo.marcarUsado();
                guardarCodigo(codigo);
            }
        }
    }

    @Override
    public CodigoVerificacionDominio consultarCodigoPendiente(UUID idUsuario) {
        for (var codigo : consultarCodigosCelular(idUsuario)) {
            if (!codigo.isUsado()) {
                return codigo;
            }
        }
        return new CodigoVerificacionDominio.Builder().build();
    }

    private List<CodigoVerificacionDominio> consultarCodigosCelular(UUID idUsuario) {
        var filtro = new CodigoVerificacionEntidad.Builder()
                .usuario(new UsuarioEntidad.Builder().id(idUsuario).build())
                .canal(UtilEnum.obtenerTexto(CanalVerificacion.CELULAR))
                .build();
        var codigos = daoFactory.obtenerCodigoVerificacionDAO().consultarPorFiltro(filtro);
        return CodigoVerificacionEntidadAssembler.getInstance().convertirADominio(codigos);
    }

    private void guardarCodigo(CodigoVerificacionDominio codigo) {
        daoFactory.obtenerCodigoVerificacionDAO().actualizar(codigo.getId(), CodigoVerificacionEntidadAssembler.getInstance().convertirAEntidad(codigo));
    }

    private UsuarioDominio consultarUsuario(UUID idUsuario) {
        var usuarioEntidad = daoFactory.obtenerUsuarioDAO().consultarPorId(idUsuario);
        return UsuarioEntidadAssembler.getInstance().convertirADominio(usuarioEntidad);
    }

    private int consultarParametroEntero(String clave, int valorPorDefecto) {
        var filtro = new ParametroGlobalEntidad.Builder().clave(clave).build();
        var parametros = daoFactory.obtenerParametroGlobalDAO().consultarPorFiltro(filtro);
        if (parametros.isEmpty()) {
            return valorPorDefecto;
        }
        return ParametroGlobalEntidadAssembler.getInstance().convertirADominio(parametros.getFirst()).valorComoEntero();
    }

    private void registrarAuditoria(UUID idUsuario) {
        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idUsuario).build())
                .accion(ACCION_CELULAR_VERIFICADO)
                .tipoObjeto(TIPO_OBJETO)
                .idObjeto(idUsuario)
                .estadoAnterior(ESTADO_SIN_VERIFICAR)
                .estadoNuevo(ESTADO_VERIFICADO)
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
