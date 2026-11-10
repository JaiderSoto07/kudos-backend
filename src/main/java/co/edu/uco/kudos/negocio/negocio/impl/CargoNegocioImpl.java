package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.entidad.FacultadEntidad;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.CargoNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.AsignacionRolEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CargoNegocioImpl implements CargoNegocio {

    private static final List<Rol> CARGOS = List.of(Rol.RECTOR, Rol.COORDINADOR_ACADEMICO, Rol.COORDINADOR_FACULTAD, Rol.COORDINADOR_PROGRAMA);
    private static final String TIPO_OBJETO = "AsignacionRol";
    private static final String ACCION_CARGO_CERRADO = "CargoCerrado";
    private static final String ESTADO_VIGENTE = "VIGENTE";
    private static final String ESTADO_CERRADO = "CERRADO";

    private final DAOFactory daoFactory;
    private final AuditoriaNegocio auditoriaNegocio;

    public CargoNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public AsignacionRolDominio consultarAsignacionVigente(UUID idUsuario) {
        var filtro = new AsignacionRolEntidad.Builder().usuario(new UsuarioEntidad.Builder().id(idUsuario).build()).build();
        for (var asignacion : consultar(filtro)) {
            if (asignacion.estaVigente()) {
                return asignacion;
            }
        }
        return new AsignacionRolDominio.Builder().build();
    }

    @Override
    public List<AsignacionRolDominio> consultarCargosVigentes() {
        var vigentes = new ArrayList<AsignacionRolDominio>();
        for (var cargo : CARGOS) {
            for (var asignacion : consultar(new AsignacionRolEntidad.Builder().rol(cargo.name()).build())) {
                if (asignacion.estaVigente()) {
                    vigentes.add(asignacion);
                }
            }
        }
        return vigentes;
    }

    @Override
    public List<AsignacionRolDominio> consultarHistorial(Rol rol, UUID idFacultad, UUID idPrograma) {
        var filtro = new AsignacionRolEntidad.Builder()
                .rol(rol.name())
                .facultad(new FacultadEntidad.Builder().id(idFacultad).build())
                .programa(new ProgramaAcademicoEntidad.Builder().id(idPrograma).build())
                .build();
        return consultar(filtro);
    }

    @Override
    public void cerrarCargoVigente(UUID idActor, Rol rol, UUID idFacultad, UUID idPrograma, String motivo) {
        for (var asignacion : consultarHistorial(rol, idFacultad, idPrograma)) {
            if (asignacion.estaVigente()) {
                asignacion.cerrar(motivo);
                daoFactory.obtenerAsignacionRolDAO().actualizar(asignacion.getId(), AsignacionRolEntidadAssembler.getInstance().convertirAEntidad(asignacion));
                desactivarTitular(asignacion.getUsuario().getId());
                registrarAuditoria(idActor, asignacion);
            }
        }
    }

    private void desactivarTitular(UUID idUsuario) {
        var titular = UsuarioEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerUsuarioDAO().consultarPorId(idUsuario));
        if (titular.getEstadoCuenta() != EstadoCuenta.DESACTIVADA) {
            titular.desactivar();
            daoFactory.obtenerUsuarioDAO().actualizar(titular.getId(), UsuarioEntidadAssembler.getInstance().convertirAEntidad(titular));
        }
    }

    private List<AsignacionRolDominio> consultar(AsignacionRolEntidad filtro) {
        return AsignacionRolEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerAsignacionRolDAO().consultarPorFiltro(filtro));
    }

    private void registrarAuditoria(UUID idActor, AsignacionRolDominio asignacion) {
        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idActor).build())
                .accion(ACCION_CARGO_CERRADO)
                .tipoObjeto(TIPO_OBJETO)
                .idObjeto(asignacion.getId())
                .estadoAnterior(ESTADO_VIGENTE)
                .estadoNuevo(ESTADO_CERRADO)
                .motivo(asignacion.getMotivoFin())
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
