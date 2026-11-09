package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilNumero;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public final class KudoEntidad {

    private final UUID id;
    private final UsuarioEntidad autor;
    private final UsuarioEntidad destinatario;
    private final CategoriaEntidad categoria;
    private final String contenido;
    private final LocalDateTime fechaCreacion;
    private final String estado;
    private final int cantidadVisualizaciones;
    private final BigDecimal promedioCalificacion;
    private final int cantidadCalificaciones;

    private KudoEntidad(Builder builder) {
        this.id = builder.id;
        this.autor = builder.autor;
        this.destinatario = builder.destinatario;
        this.categoria = builder.categoria;
        this.contenido = builder.contenido;
        this.fechaCreacion = builder.fechaCreacion;
        this.estado = builder.estado;
        this.cantidadVisualizaciones = builder.cantidadVisualizaciones;
        this.promedioCalificacion = builder.promedioCalificacion;
        this.cantidadCalificaciones = builder.cantidadCalificaciones;
    }

    public UUID getId() {
        return id;
    }

    public UsuarioEntidad getAutor() {
        return autor;
    }

    public UsuarioEntidad getDestinatario() {
        return destinatario;
    }

    public CategoriaEntidad getCategoria() {
        return categoria;
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

    public int getCantidadVisualizaciones() {
        return cantidadVisualizaciones;
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
        private UsuarioEntidad destinatario;
        private CategoriaEntidad categoria;
        private String contenido;
        private LocalDateTime fechaCreacion;
        private String estado;
        private int cantidadVisualizaciones;
        private BigDecimal promedioCalificacion;
        private int cantidadCalificaciones;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            autor = new UsuarioEntidad.Builder().build();
            destinatario = new UsuarioEntidad.Builder().build();
            categoria = new CategoriaEntidad.Builder().build();
            contenido = UtilTexto.VACIO;
            fechaCreacion = UtilFecha.FECHA_HORA_POR_DEFECTO;
            estado = UtilTexto.VACIO;
            cantidadVisualizaciones = UtilNumero.CERO;
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

        public Builder destinatario(UsuarioEntidad destinatario) {
            this.destinatario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(destinatario, new UsuarioEntidad.Builder().build());
            return this;
        }

        public Builder categoria(CategoriaEntidad categoria) {
            this.categoria = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(categoria, new CategoriaEntidad.Builder().build());
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
            this.estado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estado);
            return this;
        }

        public Builder cantidadVisualizaciones(Integer cantidadVisualizaciones) {
            this.cantidadVisualizaciones = UtilNumero.obtenerValorDefectoSiEsNuloONegativo(cantidadVisualizaciones, UtilNumero.CERO);
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

        public KudoEntidad build() {
            return new KudoEntidad(this);
        }
    }
}
