package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.AutoridadNegocio;
import co.edu.uco.kudos.negocio.negocio.VerificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.AsignacionRolEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion.AsegurarRolDeAutoridadRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion.AsegurarRolUnicoVigenteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autorizacion.AsegurarActorTieneRolRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarCelularNoRepetidoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarCorreoNoRepetidoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.ValidarCelularRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.ValidarCorreoInstitucionalRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.ValidarDatosPersonalesRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.List;
import java.util.UUID;

public class AutoridadNegocioImpl implements AutoridadNegocio {

    private static final List<Rol> ROLES_QUE_REGISTRAN_AUTORIDADES = List.of(Rol.ADMINISTRADOR);
    private static final String TIPO_OBJETO = "Usuario";
    private static final String ACCION_AUTORIDAD_REGISTRADA = "AutoridadRegistrada";

    private final DAOFactory daoFactory;
    private final VerificacionNegocio verificacionNegocio;
    private final AuditoriaNegocio auditoriaNegocio;

    public AutoridadNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.verificacionNegocio = new VerificacionNegocioImpl(daoFactory);
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public void registrarAutoridad(UUID idActor, String rol, UsuarioDominio datos) {
        AsegurarActorTieneRolRule.obtenerInstancia().ejecutar(idActor, daoFactory, ROLES_QUE_REGISTRAN_AUTORIDADES,
                CatalogoMensajes.Aprobacion.ACTOR_NO_REGISTRA_AUTORIDADES);
        AsegurarRolDeAutoridadRule.obtenerInstancia().ejecutar(rol);
        var rolAutoridad = UtilEnum.obtenerValor(Rol.class, rol, Rol.ADMINISTRADOR);

        var correo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(datos.getCorreoInstitucional()).toLowerCase();
        var celular = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(datos.getNumeroCelular());
        ValidarDatosPersonalesRule.obtenerInstancia().ejecutar(datos.getNombres(), datos.getApellidos());
        ValidarCorreoInstitucionalRule.obtenerInstancia().ejecutar(correo, daoFactory);
        AsegurarCorreoNoRepetidoRule.obtenerInstancia().ejecutar(correo, UtilId.VALOR_DEFECTO, daoFactory);
        ValidarCelularRule.obtenerInstancia().ejecutar(celular);
        AsegurarCelularNoRepetidoRule.obtenerInstancia().ejecutar(celular, UtilId.VALOR_DEFECTO, daoFactory);
        AsegurarRolUnicoVigenteRule.obtenerInstancia().ejecutar(rolAutoridad, daoFactory);

        var usuarioNuevo = new UsuarioDominio.Builder()
                .id(UtilId.generarId())
                .nombres(datos.getNombres())
                .apellidos(datos.getApellidos())
                .correoInstitucional(correo)
                .correoVerificado(false)
                .numeroCelular(celular)
                .celularVerificado(false)
                .aprobada(true)
                .estadoCuenta(EstadoCuenta.PENDIENTE_VERIFICACION)
                .fechaRegistro(UtilFecha.fechaActual())
                .build();
        daoFactory.obtenerUsuarioDAO().crear(UsuarioEntidadAssembler.getInstance().convertirAEntidad(usuarioNuevo));

        var asignacion = new AsignacionRolDominio.Builder()
                .id(UtilId.generarId())
                .usuario(usuarioNuevo)
                .rol(rolAutoridad)
                .fechaInicio(UtilFecha.fechaActual())
                .build();
        daoFactory.obtenerAsignacionRolDAO().crear(AsignacionRolEntidadAssembler.getInstance().convertirAEntidad(asignacion));

        verificacionNegocio.enviarCodigoCelular(usuarioNuevo);

        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idActor).build())
                .accion(ACCION_AUTORIDAD_REGISTRADA)
                .tipoObjeto(TIPO_OBJETO)
                .idObjeto(usuarioNuevo.getId())
                .estadoAnterior(UtilTexto.VACIO)
                .estadoNuevo(EstadoCuenta.PENDIENTE_VERIFICACION.name())
                .detalle(rolAutoridad.name())
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
