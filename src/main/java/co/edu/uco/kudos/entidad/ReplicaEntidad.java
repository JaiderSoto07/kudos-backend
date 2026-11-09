package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilNumero;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public final class ReplicaEntidad {

    private final UUID id;
    private final UsuarioEntidad autor;
    private final KudoEntidad kudo;
    private final UUID idReplicaPadre;
    private final String contenido;
    private final LocalDateTime fechaCreacion;
    private final String estado;
    private final BigDecimal promedioCalificacion;
    private final int cantidadCalificaciones;

    private ReplicaEntidad(Builder builder) {
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

    public UUID getId() {
        return id;
    }

    public UsuarioEntidad getAutor() {
        return autor;
    }

    public KudoEntidad getKudo() {
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

    public String getEstado() {
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
        private UsuarioEntidad autor;
        private KudoEntidad kudo;
        private UUID idReplicaPadre;
        private String contenido;
        private LocalDateTime fechaCreacion;
        private String estado;
        private BigDecimal promedioCalificacion;
        private int cantidadCalificaciones;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            autor = new UsuarioEntidad.Builder().build();
            kudo = new KudoEntidad.Builder().build();
            idReplicaPadre = UtilId.VALOR_DEFECTO;
            contenido = UtilTexto.VACIO;
            fechaCreacion = UtilFecha.FECHA_HORA_POR_DEFECTO;
            estado = "CREADO";
            promedioCalificacion = BigDecimal.ZERO;
            cantidadCalificaciones = UtilNumero.CERO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder autor(UsuarioEntidad autor) {
            this.autor = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(autor, new UsuarioEntidad.Builder().build());
            return this;
        }

        public Builder kudo(KudoEntidad kudo) {
            this.kudo = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(kudo, new KudoEntidad.Builder().build());
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

        public Builder estado(String estado) {
            this.estado = UtilTexto.getUtilTexto().quitarEspaciosEnBlancoOValorDefecto(estado, "CREADO");
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

        public ReplicaEntidad build() {
            return new ReplicaEntidad(this);
        }
    }
}
