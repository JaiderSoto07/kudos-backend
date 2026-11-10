package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.EstadoFacultad;
import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.FacultadEntidad;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.CargoNegocio;
import co.edu.uco.kudos.negocio.negocio.FacultadNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.FacultadEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autorizacion.AsegurarActorTieneRolRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarFacultadExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarFacultadNoInactivaRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarFacultadSinProgramasActivosRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarNombreFacultadNoRepetidoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarPuedeVerFacultadRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.ValidarNombreFacultadRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FacultadNegocioImpl implements FacultadNegocio {

    static final List<Rol> ROLES_CONSULTAN_ORGANIZACION = List.of(Rol.ADMINISTRADOR, Rol.RECTOR, Rol.COORDINADOR_ACADEMICO,
            Rol.COORDINADOR_FACULTAD, Rol.COORDINADOR_PROGRAMA);
    static final List<Rol> ROLES_GESTIONAN_ORGANIZACION = List.of(Rol.COORDINADOR_ACADEMICO);
    private static final String TIPO_OBJETO = "Facultad";
    private static final String ACCION_CREADA = "FacultadCreada";
    private static final String ACCION_MODIFICADA = "FacultadModificada";
    private static final String ACCION_INACTIVADA = "FacultadInactivada";

    private final DAOFactory daoFactory;
    private final CargoNegocio cargoNegocio;
    private final AuditoriaNegocio auditoriaNegocio;

    public FacultadNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.cargoNegocio = new CargoNegocioImpl(daoFactory);
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public List<FacultadDominio> consultar(UUID idActor) {
        asegurarPuedeConsultar(idActor);
        var asignacionDelActor = cargoNegocio.consultarAsignacionVigente(idActor);
        var facultades = FacultadEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerFacultadDAO().consultarTodos());

        var visibles = new ArrayList<FacultadDominio>();
        for (var facultad : facultades) {
            if (asignacionDelActor.puedeVerFacultad(facultad.getId())) {
                visibles.add(facultad);
            }
        }
        return visibles;
    }

    @Override
    public void registrar(UUID idActor, FacultadDominio datos) {
        asegurarPuedeGestionar(idActor);
        ValidarNombreFacultadRule.obtenerInstancia().ejecutar(datos.getNombre());
        AsegurarNombreFacultadNoRepetidoRule.obtenerInstancia().ejecutar(datos.getNombre(), UtilId.VALOR_DEFECTO, daoFactory);

        var facultadNueva = new FacultadDominio.Builder()
                .id(UtilId.generarId())
                .nombre(datos.getNombre())
                .estado(EstadoFacultad.EN_CONFORMACION)
                .fechaCreacion(UtilFecha.fechaActual())
                .build();
        daoFactory.obtenerFacultadDAO().crear(FacultadEntidadAssembler.getInstance().convertirAEntidad(facultadNueva));

        registrarAuditoria(idActor, ACCION_CREADA, facultadNueva.getId(), UtilTexto.VACIO, facultadNueva.getNombre());
    }

    @Override
    public void modificar(UUID idActor, UUID idFacultad, FacultadDominio datos) {
        asegurarPuedeGestionar(idActor);
        var facultad = consultarFacultad(idFacultad);
        AsegurarFacultadExisteRule.obtenerInstancia().ejecutar(facultad);
        ValidarNombreFacultadRule.obtenerInstancia().ejecutar(datos.getNombre());
        AsegurarNombreFacultadNoRepetidoRule.obtenerInstancia().ejecutar(datos.getNombre(), idFacultad, daoFactory);

        var nombreAnterior = facultad.getNombre();
        facultad.renombrar(datos.getNombre());
        guardar(facultad);

        registrarAuditoria(idActor, ACCION_MODIFICADA, idFacultad, nombreAnterior, facultad.getNombre());
    }

    @Override
    public void inactivar(UUID idActor, UUID idFacultad) {
        asegurarPuedeGestionar(idActor);
        var facultad = consultarFacultad(idFacultad);
        AsegurarFacultadExisteRule.obtenerInstancia().ejecutar(facultad);
        AsegurarFacultadNoInactivaRule.obtenerInstancia().ejecutar(facultad);
        AsegurarFacultadSinProgramasActivosRule.obtenerInstancia().ejecutar(idFacultad, daoFactory);

        var estadoAnterior = facultad.getEstado().name();
        cargoNegocio.cerrarCargoVigente(idActor, Rol.COORDINADOR_FACULTAD, idFacultad, UtilId.VALOR_DEFECTO,
                CatalogoMensajes.Organizacion.MOTIVO_FACULTAD_INACTIVADA);
        facultad.inactivar();
        guardar(facultad);

        registrarAuditoria(idActor, ACCION_INACTIVADA, idFacultad, estadoAnterior, facultad.getEstado().name());
    }

    @Override
    public List<AsignacionRolDominio> consultarCoordinadores(UUID idActor, UUID idFacultad) {
        asegurarPuedeConsultar(idActor);
        var facultad = consultarFacultad(idFacultad);
        AsegurarFacultadExisteRule.obtenerInstancia().ejecutar(facultad);
        AsegurarPuedeVerFacultadRule.obtenerInstancia().ejecutar(cargoNegocio.consultarAsignacionVigente(idActor), idFacultad);
        return cargoNegocio.consultarHistorial(Rol.COORDINADOR_FACULTAD, idFacultad, UtilId.VALOR_DEFECTO);
    }

    @Override
    public int contarProgramasActivos(UUID idFacultad) {
        var filtro = new ProgramaAcademicoEntidad.Builder().facultad(new FacultadEntidad.Builder().id(idFacultad).build()).build();
        var activos = 0;
        for (var programa : daoFactory.obtenerProgramaAcademicoDAO().consultarPorFiltro(filtro)) {
            if (programa.isActivo() && programa.getFacultad().getId().equals(idFacultad)) {
                activos++;
            }
        }
        return activos;
    }

    private void asegurarPuedeConsultar(UUID idActor) {
        AsegurarActorTieneRolRule.obtenerInstancia().ejecutar(idActor, daoFactory, ROLES_CONSULTAN_ORGANIZACION,
                CatalogoMensajes.Organizacion.ACTOR_NO_CONSULTA_ORGANIZACION);
    }

    private void asegurarPuedeGestionar(UUID idActor) {
        AsegurarActorTieneRolRule.obtenerInstancia().ejecutar(idActor, daoFactory, ROLES_GESTIONAN_ORGANIZACION,
                CatalogoMensajes.Organizacion.ACTOR_NO_GESTIONA_ORGANIZACION);
    }

    private FacultadDominio consultarFacultad(UUID idFacultad) {
        return FacultadEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerFacultadDAO().consultarPorId(idFacultad));
    }

    private void guardar(FacultadDominio facultad) {
        daoFactory.obtenerFacultadDAO().actualizar(facultad.getId(), FacultadEntidadAssembler.getInstance().convertirAEntidad(facultad));
    }

    private void registrarAuditoria(UUID idActor, String accion, UUID idFacultad, String estadoAnterior, String estadoNuevo) {
        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idActor).build())
                .accion(accion)
                .tipoObjeto(TIPO_OBJETO)
                .idObjeto(idFacultad)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
