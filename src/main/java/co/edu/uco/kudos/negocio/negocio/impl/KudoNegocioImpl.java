package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.NotificacionDominio;
import co.edu.uco.kudos.dominio.TipoNotificacion;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.CategoriaEntidad;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.KudoNegocio;
import co.edu.uco.kudos.negocio.negocio.NotificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.CategoriaEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.KudoEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo.AsegurarCategoriaDisponibleRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo.AsegurarDestinatarioActivoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo.AsegurarDestinatarioDistintoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo.AsegurarKudoExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo.ValidarContenidoRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KudoNegocioImpl implements KudoNegocio {

    public static final String HISTORIAL_RECIBIDOS = "recibidos";
    public static final String HISTORIAL_EMITIDOS = "emitidos";
    private static final String TIPO_OBJETO = "Kudo";
    private static final String ACCION_PUBLICADO = "KudoPublicado";

    private final DAOFactory daoFactory;
    private final NotificacionNegocio notificacionNegocio;
    private final AuditoriaNegocio auditoriaNegocio;

    public KudoNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.notificacionNegocio = new NotificacionNegocioImpl(daoFactory);
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public void crear(UUID idActor, KudoDominio datos) {
        var idDestinatario = datos.getDestinatario().getId();
        AsegurarDestinatarioDistintoRule.obtenerInstancia().ejecutar(idActor, idDestinatario);
        var destinatario = UsuarioEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerUsuarioDAO().consultarPorId(idDestinatario));
        AsegurarDestinatarioActivoRule.obtenerInstancia().ejecutar(destinatario);
        var idCategoria = datos.getCategoria().getId();
        var categoria = consultarCategoria(idCategoria);
        AsegurarCategoriaDisponibleRule.obtenerInstancia().ejecutar(idCategoria, categoria);
        ValidarContenidoRule.obtenerInstancia().ejecutar(datos.getContenido());

        var kudo = new KudoDominio.Builder()
                .id(UtilId.generarId())
                .autor(new UsuarioDominio.Builder().id(idActor).build())
                .destinatario(destinatario)
                .categoria(categoria)
                .contenido(datos.getContenido())
                .fechaCreacion(UtilFecha.fechaActual())
                .estado(EstadoContenido.CREADO)
                .build();
        kudo.publicar();
        daoFactory.obtenerKudoDAO().crear(KudoEntidadAssembler.getInstance().convertirAEntidad(kudo));

        var autor = UsuarioEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerUsuarioDAO().consultarPorId(idActor));
        notificarDestinatario(kudo, autor, categoria);
        registrarAuditoria(idActor, kudo);
    }

    @Override
    public List<KudoDominio> consultarMuro(UUID idCategoria, UUID idPersona) {
        var kudos = new ArrayList<KudoDominio>();
        if (UtilId.esValorDefecto(UtilId.valorDefecto(idPersona))) {
            agregarVisibles(kudos, filtro(idCategoria, null, null));
        } else {
            agregarVisibles(kudos, filtro(idCategoria, idPersona, null));
            agregarVisibles(kudos, filtro(idCategoria, null, idPersona));
        }
        kudos.sort((primero, segundo) -> segundo.getFechaCreacion().compareTo(primero.getFechaCreacion()));
        return kudos;
    }

    @Override
    public KudoDominio consultar(UUID idActor, UUID idKudo) {
        var kudo = KudoEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerKudoDAO().consultarPorId(idKudo));
        AsegurarKudoExisteRule.obtenerInstancia().ejecutar(kudo);
        if (!kudo.esAutor(idActor)) {
            kudo.registrarVisualizacion();
            daoFactory.obtenerKudoDAO().actualizar(kudo.getId(), KudoEntidadAssembler.getInstance().convertirAEntidad(kudo));
        }
        return kudo;
    }

    @Override
    public List<KudoDominio> consultarHistorial(UUID idPersona, String tipo) {
        var kudos = new ArrayList<KudoDominio>();
        if (!HISTORIAL_EMITIDOS.equalsIgnoreCase(UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(tipo))) {
            agregarVisibles(kudos, filtro(null, null, idPersona));
        }
        if (!HISTORIAL_RECIBIDOS.equalsIgnoreCase(UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(tipo))) {
            agregarVisibles(kudos, filtro(null, idPersona, null));
        }
        kudos.sort((primero, segundo) -> segundo.getFechaCreacion().compareTo(primero.getFechaCreacion()));
        return kudos;
    }

    private KudoEntidad filtro(UUID idCategoria, UUID idAutor, UUID idDestinatario) {
        return new KudoEntidad.Builder()
                .categoria(new CategoriaEntidad.Builder().id(idCategoria).build())
                .autor(new UsuarioEntidad.Builder().id(idAutor).build())
                .destinatario(new UsuarioEntidad.Builder().id(idDestinatario).build())
                .build();
    }

    private void agregarVisibles(List<KudoDominio> kudos, KudoEntidad filtro) {
        for (var kudo : KudoEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerKudoDAO().consultarPorFiltro(filtro))) {
            if (kudo.getEstado() != EstadoContenido.VETADO && kudo.getEstado() != EstadoContenido.CREADO && !contiene(kudos, kudo.getId())) {
                kudos.add(kudo);
            }
        }
    }

    private boolean contiene(List<KudoDominio> kudos, UUID idKudo) {
        for (var kudo : kudos) {
            if (kudo.getId().equals(idKudo)) {
                return true;
            }
        }
        return false;
    }

    private CategoriaDominio consultarCategoria(UUID idCategoria) {
        return CategoriaEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerCategoriaDAO().consultarPorId(UtilId.valorDefecto(idCategoria)));
    }

    private void notificarDestinatario(KudoDominio kudo, UsuarioDominio autor, CategoriaDominio categoria) {
        var notificacion = new NotificacionDominio.Builder()
                .destinatario(kudo.getDestinatario())
                .tipo(TipoNotificacion.KUDO_RECIBIDO)
                .mensaje(String.format(CatalogoMensajes.Kudo.MENSAJE_KUDO_RECIBIDO, autor.nombreCompleto(), categoria.getNombre()))
                .tipoReferencia(TIPO_OBJETO)
                .idReferencia(kudo.getId())
                .build();
        notificacionNegocio.notificar(notificacion);
    }

    private void registrarAuditoria(UUID idActor, KudoDominio kudo) {
        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idActor).build())
                .accion(ACCION_PUBLICADO)
                .tipoObjeto(TIPO_OBJETO)
                .idObjeto(kudo.getId())
                .estadoAnterior(EstadoContenido.CREADO.name())
                .estadoNuevo(kudo.getEstado().name())
                .detalle(kudo.getDestinatario().getId().toString())
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
