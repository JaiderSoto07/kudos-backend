package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.CargoNegocio;
import co.edu.uco.kudos.negocio.negocio.ProgramaNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.FacultadEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ProgramaAcademicoEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autorizacion.AsegurarActorTieneRolRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarFacultadAdmiteProgramasRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarFacultadExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarNombreProgramaNoRepetidoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarProgramaActivoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarProgramaExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarProgramaSinVinculosRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.AsegurarPuedeVerProgramaRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion.ValidarNombreProgramaRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProgramaNegocioImpl implements ProgramaNegocio {

    private static final String TIPO_OBJETO = "ProgramaAcademico";
    private static final String ACCION_CREADO = "ProgramaCreado";
    private static final String ACCION_MODIFICADO = "ProgramaModificado";
    private static final String ACCION_INACTIVADO = "ProgramaInactivado";
    private static final String TIPO_FACULTAD = "Facultad";
    private static final String ACCION_FACULTAD_ACTIVADA = "FacultadActivada";
    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ESTADO_INACTIVO = "INACTIVO";

    private final DAOFactory daoFactory;
    private final CargoNegocio cargoNegocio;
    private final AuditoriaNegocio auditoriaNegocio;

    public ProgramaNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.cargoNegocio = new CargoNegocioImpl(daoFactory);
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public List<ProgramaAcademicoDominio> consultar(UUID idActor) {
        asegurarPuedeConsultar(idActor);
        var asignacionDelActor = cargoNegocio.consultarAsignacionVigente(idActor);
        var programas = ProgramaAcademicoEntidadAssembler.getInstance()
                .convertirADominio(daoFactory.obtenerProgramaAcademicoDAO().consultarTodos());

        var visibles = new ArrayList<ProgramaAcademicoDominio>();
        for (var programa : programas) {
            if (asignacionDelActor.puedeVerPrograma(programa)) {
                visibles.add(programa);
            }
        }
        return visibles;
    }

    @Override
    public ProgramaAcademicoDominio consultarPorId(UUID idActor, UUID idPrograma) {
        asegurarPuedeConsultar(idActor);
        var programa = consultarPrograma(idPrograma);
        AsegurarProgramaExisteRule.obtenerInstancia().ejecutar(programa);
        AsegurarPuedeVerProgramaRule.obtenerInstancia().ejecutar(cargoNegocio.consultarAsignacionVigente(idActor), programa);
        return programa;
    }

    @Override
    public void registrar(UUID idActor, ProgramaAcademicoDominio datos) {
        asegurarPuedeGestionar(idActor);
        ValidarNombreProgramaRule.obtenerInstancia().ejecutar(datos.getNombre());
        AsegurarNombreProgramaNoRepetidoRule.obtenerInstancia().ejecutar(datos.getNombre(), UtilId.VALOR_DEFECTO, daoFactory);
        var facultad = consultarFacultad(datos.getFacultad().getId());
        AsegurarFacultadExisteRule.obtenerInstancia().ejecutar(facultad);
        AsegurarFacultadAdmiteProgramasRule.obtenerInstancia().ejecutar(facultad);

        var programaNuevo = new ProgramaAcademicoDominio.Builder()
                .id(UtilId.generarId())
                .nombre(datos.getNombre())
                .facultad(facultad)
                .activo(true)
                .fechaCreacion(UtilFecha.fechaActual())
                .build();
        daoFactory.obtenerProgramaAcademicoDAO().crear(ProgramaAcademicoEntidadAssembler.getInstance().convertirAEntidad(programaNuevo));
        registrarAuditoria(idActor, ACCION_CREADO, TIPO_OBJETO, programaNuevo.getId(), UtilTexto.VACIO, programaNuevo.getNombre());

        var estadoAnterior = facultad.getEstado().name();
        facultad.activar();
        if (!estadoAnterior.equals(facultad.getEstado().name())) {
            daoFactory.obtenerFacultadDAO().actualizar(facultad.getId(), FacultadEntidadAssembler.getInstance().convertirAEntidad(facultad));
            registrarAuditoria(idActor, ACCION_FACULTAD_ACTIVADA, TIPO_FACULTAD, facultad.getId(), estadoAnterior, facultad.getEstado().name());
        }
    }

    @Override
    public void modificar(UUID idActor, UUID idPrograma, ProgramaAcademicoDominio datos) {
        asegurarPuedeGestionar(idActor);
        var programa = consultarPrograma(idPrograma);
        AsegurarProgramaExisteRule.obtenerInstancia().ejecutar(programa);
        ValidarNombreProgramaRule.obtenerInstancia().ejecutar(datos.getNombre());
        AsegurarNombreProgramaNoRepetidoRule.obtenerInstancia().ejecutar(datos.getNombre(), idPrograma, daoFactory);

        var nombreAnterior = programa.getNombre();
        programa.renombrar(datos.getNombre());
        guardar(programa);

        registrarAuditoria(idActor, ACCION_MODIFICADO, TIPO_OBJETO, idPrograma, nombreAnterior, programa.getNombre());
    }

    @Override
    public void inactivar(UUID idActor, UUID idPrograma) {
        asegurarPuedeGestionar(idActor);
        var programa = consultarPrograma(idPrograma);
        AsegurarProgramaExisteRule.obtenerInstancia().ejecutar(programa);
        AsegurarProgramaActivoRule.obtenerInstancia().ejecutar(programa);
        AsegurarProgramaSinVinculosRule.obtenerInstancia().ejecutar(idPrograma, daoFactory);

        cargoNegocio.cerrarCargoVigente(idActor, Rol.COORDINADOR_PROGRAMA, UtilId.VALOR_DEFECTO, idPrograma,
                CatalogoMensajes.Organizacion.MOTIVO_PROGRAMA_INACTIVADO);
        programa.inactivar();
        guardar(programa);

        registrarAuditoria(idActor, ACCION_INACTIVADO, TIPO_OBJETO, idPrograma, ESTADO_ACTIVO, ESTADO_INACTIVO);
    }

    @Override
    public List<AsignacionRolDominio> consultarCoordinadores(UUID idActor, UUID idPrograma) {
        consultarPorId(idActor, idPrograma);
        return cargoNegocio.consultarHistorial(Rol.COORDINADOR_PROGRAMA, UtilId.VALOR_DEFECTO, idPrograma);
    }

    private void asegurarPuedeConsultar(UUID idActor) {
        AsegurarActorTieneRolRule.obtenerInstancia().ejecutar(idActor, daoFactory, FacultadNegocioImpl.ROLES_CONSULTAN_ORGANIZACION,
                CatalogoMensajes.Organizacion.ACTOR_NO_CONSULTA_ORGANIZACION);
    }

    private void asegurarPuedeGestionar(UUID idActor) {
        AsegurarActorTieneRolRule.obtenerInstancia().ejecutar(idActor, daoFactory, FacultadNegocioImpl.ROLES_GESTIONAN_ORGANIZACION,
                CatalogoMensajes.Organizacion.ACTOR_NO_GESTIONA_ORGANIZACION);
    }

    private ProgramaAcademicoDominio consultarPrograma(UUID idPrograma) {
        return ProgramaAcademicoEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerProgramaAcademicoDAO().consultarPorId(idPrograma));
    }

    private FacultadDominio consultarFacultad(UUID idFacultad) {
        return FacultadEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerFacultadDAO().consultarPorId(idFacultad));
    }

    private void guardar(ProgramaAcademicoDominio programa) {
        daoFactory.obtenerProgramaAcademicoDAO().actualizar(programa.getId(), ProgramaAcademicoEntidadAssembler.getInstance().convertirAEntidad(programa));
    }

    private void registrarAuditoria(UUID idActor, String accion, String tipoObjeto, UUID idObjeto, String estadoAnterior, String estadoNuevo) {
        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idActor).build())
                .accion(accion)
                .tipoObjeto(tipoObjeto)
                .idObjeto(idObjeto)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
