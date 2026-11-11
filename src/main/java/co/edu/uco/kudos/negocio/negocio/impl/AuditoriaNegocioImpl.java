package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.entidad.AuditoriaEntidad;
import co.edu.uco.kudos.entidad.FacultadEntidad;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.entidad.VinculacionProgramaEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.AuditoriaEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.auditoria.ValidarRangoFechasRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autorizacion.AsegurarActorTieneRolRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuditoriaNegocioImpl implements AuditoriaNegocio {

    static final List<Rol> ROLES_CONSULTAN_AUDITORIA = List.of(Rol.ADMINISTRADOR, Rol.RECTOR, Rol.COORDINADOR_ACADEMICO,
            Rol.COORDINADOR_FACULTAD, Rol.COORDINADOR_PROGRAMA);

    private static final List<Rol> ROLES_VEN_TODO = List.of(Rol.RECTOR, Rol.COORDINADOR_ACADEMICO);
    private static final List<String> TIPOS_DEL_ADMINISTRADOR = List.of("Usuario", "AsignacionRol", "Categoria", "SolicitudRegistro");

    private final DAOFactory daoFactory;

    public AuditoriaNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void registrar(AuditoriaDominio datos) {
        var auditoriaConIdYFecha = new AuditoriaDominio.Builder()
                .id(UtilId.generarId())
                .fechaHora(UtilFecha.fechaActual())
                .actor(datos.getActor())
                .accion(datos.getAccion())
                .tipoObjeto(datos.getTipoObjeto())
                .idObjeto(datos.getIdObjeto())
                .estadoAnterior(datos.getEstadoAnterior())
                .estadoNuevo(datos.getEstadoNuevo())
                .motivo(datos.getMotivo())
                .detalle(datos.getDetalle())
                .build();

        var auditoriaEntidad = AuditoriaEntidadAssembler.getInstance().convertirAEntidad(auditoriaConIdYFecha);
        daoFactory.obtenerAuditoriaDAO().crear(auditoriaEntidad);
    }

    @Override
    public List<AuditoriaDominio> consultar(UUID idActor, LocalDate desde, LocalDate hasta, String actor, String accion, String tipoObjeto) {
        AsegurarActorTieneRolRule.obtenerInstancia().ejecutar(idActor, daoFactory, ROLES_CONSULTAN_AUDITORIA,
                CatalogoMensajes.Auditoria.ACTOR_NO_PUEDE_CONSULTAR);
        ValidarRangoFechasRule.obtenerInstancia().ejecutar(desde, hasta);

        var asignacion = new CargoNegocioImpl(daoFactory).consultarAsignacionVigente(idActor);
        var actoresDelAmbito = consultarActoresDelAmbito(asignacion);
        var buscado = UtilTexto.getUtilTexto().normalizarParaBuscar(actor);
        var filtro = new AuditoriaEntidad.Builder()
                .accion(accion)
                .tipoObjeto(tipoObjeto)
                .build();

        var encontrados = new ArrayList<AuditoriaDominio>();
        for (var registro : AuditoriaEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerAuditoriaDAO().consultarPorFiltro(filtro))) {
            if (estaEnElAmbito(registro, asignacion.getRol(), actoresDelAmbito) && estaEnElRango(registro, desde, hasta)
                    && UtilTexto.getUtilTexto().normalizarParaBuscar(registro.getActor().nombreCompleto()).contains(buscado)) {
                encontrados.add(registro);
            }
        }
        return encontrados;
    }

    private boolean estaEnElAmbito(AuditoriaDominio registro, Rol rol, List<UUID> actoresDelAmbito) {
        if (ROLES_VEN_TODO.contains(rol)) {
            return true;
        }
        if (rol == Rol.ADMINISTRADOR) {
            return TIPOS_DEL_ADMINISTRADOR.contains(registro.getTipoObjeto());
        }
        return actoresDelAmbito.contains(registro.getActor().getId());
    }

    private boolean estaEnElRango(AuditoriaDominio registro, LocalDate desde, LocalDate hasta) {
        var fecha = registro.getFechaHora().toLocalDate();
        return (desde == null || !fecha.isBefore(desde)) && (hasta == null || !fecha.isAfter(hasta));
    }

    private List<UUID> consultarActoresDelAmbito(AsignacionRolDominio asignacion) {
        var actores = new ArrayList<UUID>();
        actores.add(asignacion.getUsuario().getId());
        for (var idPrograma : consultarProgramasDelAmbito(asignacion)) {
            var programa = new ProgramaAcademicoEntidad.Builder().id(idPrograma).build();
            for (var vinculacion : daoFactory.obtenerVinculacionProgramaDAO().consultarPorFiltro(new VinculacionProgramaEntidad.Builder().programa(programa).build())) {
                agregarSiFalta(actores, vinculacion.getUsuario().getId());
            }
            for (var cargo : daoFactory.obtenerAsignacionRolDAO().consultarPorFiltro(new AsignacionRolEntidad.Builder().programa(programa).build())) {
                agregarSiFalta(actores, cargo.getUsuario().getId());
            }
        }
        return actores;
    }

    private List<UUID> consultarProgramasDelAmbito(AsignacionRolDominio asignacion) {
        var programas = new ArrayList<UUID>();
        if (asignacion.getRol() == Rol.COORDINADOR_PROGRAMA) {
            programas.add(asignacion.getPrograma().getId());
        } else if (asignacion.getRol() == Rol.COORDINADOR_FACULTAD) {
            var filtro = new ProgramaAcademicoEntidad.Builder()
                    .facultad(new FacultadEntidad.Builder().id(asignacion.getFacultad().getId()).build())
                    .build();
            for (var programa : daoFactory.obtenerProgramaAcademicoDAO().consultarPorFiltro(filtro)) {
                programas.add(programa.getId());
            }
        }
        return programas;
    }

    private void agregarSiFalta(List<UUID> actores, UUID idUsuario) {
        if (!actores.contains(idUsuario)) {
            actores.add(idUsuario);
        }
    }
}
