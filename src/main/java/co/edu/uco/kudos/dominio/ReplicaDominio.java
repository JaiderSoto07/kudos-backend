package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilNumero;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class ReplicaDominio {

    private final UUID id;
    private final UsuarioDominio autor;
    private final KudoDominio kudo;
    private final UUID idReplicaPadre;
    private final String contenido;
    private final LocalDateTime fechaCreacion;
    private EstadoContenido estado;
    private BigDecimal promedioCalificacion;
    private int cantidadCalificaciones;

    private ReplicaDominio(Builder builder) {
        this.id = builder.id;
        this.autor = builder.autor;
        this.kudo = builder.kudo;
        this.idReplicaPadre = builder.idReplicaPadre;
        this.contenido = builder.contenido;
        this.fechaCreacion = builder.fechaCreacion;
        this.estado = builder.estado;
        this.promedioCalificacion = builder.promedioCalificacion;
        this.cantidadCalificaciones = builder.cantidadCalificaciones;
    }

    public void publicar() {
        estado = estado.publicar();
    }

    public void reportar() {
        estado = estado.reportar();
    }

    public void iniciarRevision() {
        estado = estado.iniciarRevision();
    }

    public void liberar() {
        estado = estado.liberar();
    }

    public void vetar() {
        estado = estado.vetar();
    }

    public boolean admiteInteraccion() {
        return estado.admiteInteraccion();
    }

    public boolean esAutor(UUID idUsuario) {
        return autor.getId().equals(idUsuario);
    }

    public void actualizarResumenCalificaciones(List<Integer> estrellasActivas) {
        cantidadCalificaciones = estrellasActivas.size();
        var suma = estrellasActivas.stream().mapToInt(Integer::intValue).sum();
        promedioCalificacion = cantidadCalificaciones == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(suma).divide(BigDecimal.valueOf(cantidadCalificaciones), 2, java.math.RoundingMode.HALF_UP);
    }

    public boolean respondeAlKudo() {
        return UtilId.esValorDefecto(idReplicaPadre);
    }

    public UUID getId() {
        return id;
    }

    public UsuarioDominio getAutor() {
        return autor;
    }

    public KudoDominio getKudo() {
        return kudo;
    }

    public UUID getIdReplicaPadre() {
        return idReplicaPadre;
    }

    public String getContenido() {
        return contenido;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public EstadoContenido getEstado() {
        return estado;
    }

    public BigDecimal getPromedioCalificacion() {
        return promedioCalificacion;
    }

    public int getCantidadCalificaciones() {
        return cantidadCalificaciones;
    }

    public static final class Builder {

        private UUID id;
        private UsuarioDominio autor;
        private KudoDominio kudo;
        private UUID idReplicaPadre;
        private String contenido;
        private LocalDateTime fechaCreacion;
        private EstadoContenido estado;
        private BigDecimal promedioCalificacion;
        private int cantidadCalificaciones;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            autor = new UsuarioDominio.Builder().build();
            kudo = new KudoDominio.Builder().build();
            idReplicaPadre = UtilId.VALOR_DEFECTO;
            contenido = UtilTexto.VACIO;
            fechaCreacion = UtilFecha.FECHA_HORA_POR_DEFECTO;
            estado = EstadoContenido.CREADO;
            promedioCalificacion = BigDecimal.ZERO;
            cantidadCalificaciones = UtilNumero.CERO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder autor(UsuarioDominio autor) {
            this.autor = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(autor, new UsuarioDominio.Builder().build());
            return this;
        }

        public Builder kudo(KudoDominio kudo) {
            this.kudo = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(kudo, new KudoDominio.Builder().build());
            return this;
        }

        public Builder idReplicaPadre(UUID idReplicaPadre) {
            this.idReplicaPadre = UtilId.valorDefecto(idReplicaPadre);
            return this;
        }

        public Builder contenido(String contenido) {
            this.contenido = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(contenido);
            return this;
        }

        public Builder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = UtilFecha.valorDefecto(fechaCreacion);
            return this;
        }

        public Builder estado(EstadoContenido estado) {
            this.estado = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estado, EstadoContenido.CREADO);
            return this;
        }

        public Builder promedioCalificacion(BigDecimal promedioCalificacion) {
            this.promedioCalificacion = UtilNumero.obtenerValorDefectoSiEsNuloONegativo(promedioCalificacion, BigDecimal.ZERO);
            return this;
        }

        public Builder cantidadCalificaciones(Integer cantidadCalificaciones) {
            this.cantidadCalificaciones = UtilNumero.obtenerValorDefectoSiEsNuloONegativo(cantidadCalificaciones, UtilNumero.CERO);
            return this;
        }

        public ReplicaDominio build() {
            return new ReplicaDominio(this);
        }
    }
}
