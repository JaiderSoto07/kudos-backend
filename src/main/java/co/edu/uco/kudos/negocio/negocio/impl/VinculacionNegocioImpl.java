package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.NotificacionDominio;
import co.edu.uco.kudos.dominio.TipoNotificacion;
import co.edu.uco.kudos.dominio.TipoVinculo;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dominio.VinculacionProgramaDominio;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.entidad.VinculacionProgramaEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.CargoNegocio;
import co.edu.uco.kudos.negocio.negocio.NotificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.ProgramaNegocio;
import co.edu.uco.kudos.negocio.negocio.VinculacionNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ProgramaAcademicoEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.VinculacionProgramaEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autorizacion.AsegurarActorTieneRolRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarActorCoordinaProgramaRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarPersonaRegistradaRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarProgramaActivoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarProgramaExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarRolCoincideConVinculoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarVinculoNoRepetidoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarVinculoVigenteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.ValidarMotivoDesvinculacionRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.List;
import java.util.UUID;

public class VinculacionNegocioImpl implements VinculacionNegocio {

    private static final String TIPO_OBJETO = "VinculacionPrograma";
    private static final String TIPO_USUARIO = "Usuario";
    private static final String ACCION_VINCULADO = "PersonaVinculada";
    private static final String ACCION_DESVINCULADO = "PersonaDesvinculada";
    private static final String ACCION_SUSPENDIDA = "CuentaSuspendida";
    private static final String ACCION_REACTIVADA = "CuentaReactivada";
    private static final String ESTADO_VIGENTE = "VIGENTE";
    private static final String ESTADO_CERRADO = "CERRADO";

    private final DAOFactory daoFactory;
    private final CargoNegocio cargoNegocio;
    private final ProgramaNegocio programaNegocio;
    private final NotificacionNegocio notificacionNegocio;
    private final AuditoriaNegocio auditoriaNegocio;

    public VinculacionNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.cargoNegocio = new CargoNegocioImpl(daoFactory);
        this.programaNegocio = new ProgramaNegocioImpl(daoFactory);
        this.notificacionNegocio = new NotificacionNegocioImpl(daoFactory);
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public List<VinculacionProgramaDominio> consultarDePrograma(UUID idActor, UUID idPrograma) {
        programaNegocio.consultarPorId(idActor, idPrograma);
        var filtro = new VinculacionProgramaEntidad.Builder().programa(new ProgramaAcademicoEntidad.Builder().id(idPrograma).build()).build();
        return consultar(filtro);
    }

    @Override
    public List<VinculacionProgramaDominio> consultarDeUsuario(UUID idActor, UUID idUsuario) {
        AsegurarActorTieneRolRule.obtenerInstancia().ejecutar(idActor, daoFactory, FacultadNegocioImpl.ROLES_CONSULTAN_ORGANIZACION,
                CatalogoMensajes.Organizacion.ACTOR_NO_CONSULTA_ORGANIZACION);
        return consultarPropios(idUsuario);
    }

    @Override
    public List<VinculacionProgramaDominio> consultarPropios(UUID idUsuario) {
        var filtro = new VinculacionProgramaEntidad.Builder().usuario(new UsuarioEntidad.Builder().id(idUsuario).build()).build();
        return consultar(filtro);
    }

    @Override
    public void vincular(UUID idActor, UUID idPrograma, String correo, String tipoVinculo) {
        var programa = ProgramaAcademicoEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerProgramaAcademicoDAO().consultarPorId(idPrograma));
        AsegurarProgramaExisteRule.obtenerInstancia().ejecutar(programa);
        AsegurarActorCoordinaProgramaRule.obtenerInstancia().ejecutar(cargoNegocio.consultarAsignacionVigente(idActor), idPrograma);
        AsegurarProgramaActivoRule.obtenerInstancia().ejecutar(programa);

        var persona = consultarPersonaPorCorreo(correo);
        AsegurarPersonaRegistradaRule.obtenerInstancia().ejecutar(persona);
        var asignacionDeLaPersona = cargoNegocio.consultarAsignacionVigente(persona.getId());
        var rolVigente = UtilId.esValorDefecto(asignacionDeLaPersona.getId()) ? null : asignacionDeLaPersona.getRol();
        AsegurarRolCoincideConVinculoRule.obtenerInstancia().ejecutar(rolVigente, tipoVinculo);
        AsegurarVinculoNoRepetidoRule.obtenerInstancia().ejecutar(persona.getId(), idPrograma, daoFactory);

        var vinculo = new VinculacionProgramaDominio.Builder()
                .id(UtilId.generarId())
                .usuario(persona)
                .programa(programa)
                .tipoVinculo(UtilEnum.obtenerValor(TipoVinculo.class, tipoVinculo, TipoVinculo.ESTUDIANTE))
                .fechaInicio(UtilFecha.fechaActual())
                .build();
        daoFactory.obtenerVinculacionProgramaDAO().crear(VinculacionProgramaEntidadAssembler.getInstance().convertirAEntidad(vinculo));
        registrarAuditoria(idActor, ACCION_VINCULADO, TIPO_OBJETO, vinculo.getId(), UtilTexto.VACIO, ESTADO_VIGENTE, programa.getNombre());

        if (persona.getEstadoCuenta() == EstadoCuenta.SUSPENDIDA) {
            persona.reactivar();
            guardarUsuario(persona);
            notificar(persona.getId(), TipoNotificacion.CUENTA_REACTIVADA,
                    String.format(CatalogoMensajes.Organizacion.MENSAJE_CUENTA_REACTIVADA, programa.getNombre()));
            registrarAuditoria(idActor, ACCION_REACTIVADA, TIPO_USUARIO, persona.getId(), EstadoCuenta.SUSPENDIDA.name(),
                    persona.getEstadoCuenta().name(), programa.getNombre());
        }
    }

    @Override
    public void desvincular(UUID idActor, UUID idVinculo, String motivo) {
        var vinculo = VinculacionProgramaEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerVinculacionProgramaDAO().consultarPorId(idVinculo));
        AsegurarVinculoVigenteRule.obtenerInstancia().ejecutar(vinculo);
        AsegurarActorCoordinaProgramaRule.obtenerInstancia().ejecutar(cargoNegocio.consultarAsignacionVigente(idActor), vinculo.getPrograma().getId());
        ValidarMotivoDesvinculacionRule.obtenerInstancia().ejecutar(motivo);

        vinculo.desvincular(motivo);
        daoFactory.obtenerVinculacionProgramaDAO().actualizar(idVinculo, VinculacionProgramaEntidadAssembler.getInstance().convertirAEntidad(vinculo));
        registrarAuditoria(idActor, ACCION_DESVINCULADO, TIPO_OBJETO, idVinculo, ESTADO_VIGENTE, ESTADO_CERRADO, vinculo.getMotivoDesvinculacion());

        var persona = consultarPersona(vinculo.getUsuario().getId());
        if (!tieneVinculosVigentes(persona.getId()) && puedeSuspenderse(persona)) {
            var estadoAnterior = persona.getEstadoCuenta().name();
            persona.suspender();
            guardarUsuario(persona);
            notificar(persona.getId(), TipoNotificacion.CUENTA_SUSPENDIDA,
                    String.format(CatalogoMensajes.Organizacion.MENSAJE_CUENTA_SUSPENDIDA, vinculo.getMotivoDesvinculacion()));
            registrarAuditoria(idActor, ACCION_SUSPENDIDA, TIPO_USUARIO, persona.getId(), estadoAnterior, EstadoCuenta.SUSPENDIDA.name(),
                    vinculo.getMotivoDesvinculacion());
        }
    }

    private boolean tieneVinculosVigentes(UUID idUsuario) {
        for (var vinculo : consultarPropios(idUsuario)) {
            if (vinculo.estaVigente()) {
                return true;
            }
        }
        return false;
    }

    private boolean puedeSuspenderse(UsuarioDominio persona) {
        return persona.getEstadoCuenta() != EstadoCuenta.SUSPENDIDA && persona.getEstadoCuenta() != EstadoCuenta.DESACTIVADA;
    }

    private List<VinculacionProgramaDominio> consultar(VinculacionProgramaEntidad filtro) {
        return VinculacionProgramaEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerVinculacionProgramaDAO().consultarPorFiltro(filtro));
    }

    private UsuarioDominio consultarPersonaPorCorreo(String correo) {
        var correoLimpio = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(correo).toLowerCase();
        if (UtilTexto.getUtilTexto().esVacia(correoLimpio)) {
            return new UsuarioDominio.Builder().build();
        }
        var filtro = new UsuarioEntidad.Builder().correoInstitucional(correoLimpio).build();
        var encontrados = daoFactory.obtenerUsuarioDAO().consultarPorFiltro(filtro);
        return encontrados.isEmpty() ? new UsuarioDominio.Builder().build() : UsuarioEntidadAssembler.getInstance().convertirADominio(encontrados.getFirst());
    }

    private UsuarioDominio consultarPersona(UUID idUsuario) {
        return UsuarioEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerUsuarioDAO().consultarPorId(idUsuario));
    }

    private void guardarUsuario(UsuarioDominio usuario) {
        daoFactory.obtenerUsuarioDAO().actualizar(usuario.getId(), UsuarioEntidadAssembler.getInstance().convertirAEntidad(usuario));
    }

    private void notificar(UUID idDestinatario, TipoNotificacion tipo, String mensaje) {
        var notificacion = new NotificacionDominio.Builder()
                .destinatario(new UsuarioDominio.Builder().id(idDestinatario).build())
                .tipo(tipo)
                .mensaje(mensaje)
                .tipoReferencia(TIPO_USUARIO)
                .idReferencia(idDestinatario)
                .build();
        notificacionNegocio.notificar(notificacion);
    }

    private void registrarAuditoria(UUID idActor, String accion, String tipoObjeto, UUID idObjeto, String estadoAnterior,
            String estadoNuevo, String detalle) {
        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idActor).build())
                .accion(accion)
                .tipoObjeto(tipoObjeto)
                .idObjeto(idObjeto)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .detalle(detalle)
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
