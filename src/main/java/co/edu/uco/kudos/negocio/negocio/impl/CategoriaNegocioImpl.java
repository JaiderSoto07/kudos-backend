package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.CategoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.CategoriaEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autorizacion.AsegurarActorTieneRolRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.categoria.AsegurarCategoriaExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.categoria.AsegurarIdCategoriaNoExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.categoria.AsegurarNombreCategoriaNoRepetidoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.categoria.ValidarDatosCategoriaRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.List;
import java.util.UUID;

public class CategoriaNegocioImpl implements CategoriaNegocio {

    private static final String TIPO_OBJETO = "Categoria";
    private static final String ACCION_CREADA = "CategoriaCreada";
    private static final String ACCION_MODIFICADA = "CategoriaModificada";
    private static final String ACCION_ACTIVADA = "CategoriaActivada";
    private static final String ACCION_DESACTIVADA = "CategoriaDesactivada";
    private static final String ESTADO_ACTIVA = "ACTIVA";
    private static final String ESTADO_INACTIVA = "INACTIVA";
    private static final List<Rol> ROLES_GESTORES_CATEGORIAS = List.of(Rol.ADMINISTRADOR, Rol.RECTOR, Rol.COORDINADOR_ACADEMICO);

    private final DAOFactory daoFactory;
    private final AuditoriaNegocio auditoriaNegocio;

    public CategoriaNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public void registrarNuevaCategoria(UUID idActor, CategoriaDominio datos) {
        asegurarActorGestionaCategorias(idActor);
        ValidarDatosCategoriaRule.obtenerInstancia().ejecutar(datos);
        AsegurarNombreCategoriaNoRepetidoRule.obtenerInstancia().ejecutar(datos.getNombre(), UtilId.VALOR_DEFECTO, daoFactory);
        var idCategoria = generarIdCategoriaUnico();
        AsegurarIdCategoriaNoExisteRule.obtenerInstancia().ejecutar(idCategoria, daoFactory);

        var categoriaNueva = new CategoriaDominio.Builder()
                .id(idCategoria)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .activa(true)
                .fechaCreacion(UtilFecha.fechaActual())
                .build();
        var categoriaEntidad = CategoriaEntidadAssembler.getInstance().convertirAEntidad(categoriaNueva);
        daoFactory.obtenerCategoriaDAO().crear(categoriaEntidad);

        registrarAuditoria(idActor, ACCION_CREADA, idCategoria, UtilTexto.VACIO, categoriaNueva.getNombre());
    }

    @Override
    public void modificarCategoria(UUID idActor, UUID id, CategoriaDominio datos) {
        asegurarActorGestionaCategorias(idActor);
        AsegurarCategoriaExisteRule.obtenerInstancia().ejecutar(id, daoFactory);
        ValidarDatosCategoriaRule.obtenerInstancia().ejecutar(datos);
        AsegurarNombreCategoriaNoRepetidoRule.obtenerInstancia().ejecutar(datos.getNombre(), id, daoFactory);

        var categoria = consultarCategoria(id);
        var nombreAnterior = categoria.getNombre();
        categoria.modificar(datos.getNombre(), datos.getDescripcion());
        guardarCambios(categoria);

        registrarAuditoria(idActor, ACCION_MODIFICADA, id, nombreAnterior, categoria.getNombre());
    }

    @Override
    public void activarCategoria(UUID idActor, UUID id) {
        asegurarActorGestionaCategorias(idActor);
        AsegurarCategoriaExisteRule.obtenerInstancia().ejecutar(id, daoFactory);

        var categoria = consultarCategoria(id);
        categoria.activar();
        guardarCambios(categoria);

        registrarAuditoria(idActor, ACCION_ACTIVADA, id, ESTADO_INACTIVA, ESTADO_ACTIVA);
    }

    @Override
    public void desactivarCategoria(UUID idActor, UUID id) {
        asegurarActorGestionaCategorias(idActor);
        AsegurarCategoriaExisteRule.obtenerInstancia().ejecutar(id, daoFactory);

        var categoria = consultarCategoria(id);
        categoria.desactivar();
        guardarCambios(categoria);

        registrarAuditoria(idActor, ACCION_DESACTIVADA, id, ESTADO_ACTIVA, ESTADO_INACTIVA);
    }

    @Override
    public List<CategoriaDominio> consultarActivas() {
        var categoriasEncontradas = daoFactory.obtenerCategoriaDAO().consultarActivos();
        return CategoriaEntidadAssembler.getInstance().convertirADominio(categoriasEncontradas);
    }

    @Override
    public List<CategoriaDominio> consultarTodas(UUID idActor) {
        asegurarActorGestionaCategorias(idActor);
        var categoriasEncontradas = daoFactory.obtenerCategoriaDAO().consultarTodos();
        return CategoriaEntidadAssembler.getInstance().convertirADominio(categoriasEncontradas);
    }

    private void asegurarActorGestionaCategorias(UUID idActor) {
        AsegurarActorTieneRolRule.obtenerInstancia().ejecutar(idActor, daoFactory, ROLES_GESTORES_CATEGORIAS,
                CatalogoMensajes.CategoriaNegocioImpl.ACTOR_NO_PUEDE_GESTIONAR_CATEGORIAS);
    }

    private UUID generarIdCategoriaUnico() {
        return UtilId.generarId();
    }

    private CategoriaDominio consultarCategoria(UUID id) {
        var categoriaEntidad = daoFactory.obtenerCategoriaDAO().consultarPorId(id);
        return CategoriaEntidadAssembler.getInstance().convertirADominio(categoriaEntidad);
    }

    private void guardarCambios(CategoriaDominio categoria) {
        var categoriaEntidad = CategoriaEntidadAssembler.getInstance().convertirAEntidad(categoria);
        daoFactory.obtenerCategoriaDAO().actualizar(categoria.getId(), categoriaEntidad);
    }

    private void registrarAuditoria(UUID idActor, String accion, UUID idCategoria, String estadoAnterior, String estadoNuevo) {
        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idActor).build())
                .accion(accion)
                .tipoObjeto(TIPO_OBJETO)
                .idObjeto(idCategoria)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
