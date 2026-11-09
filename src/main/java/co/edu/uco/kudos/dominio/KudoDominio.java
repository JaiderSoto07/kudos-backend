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

public final class KudoDominio {

    private final UUID id;
    private final UsuarioDominio autor;
    private final UsuarioDominio destinatario;
    private final CategoriaDominio categoria;
    private final String contenido;
    private final LocalDateTime fechaCreacion;
    private EstadoContenido estado;
    private int cantidadVisualizaciones;
    private BigDecimal promedioCalificacion;
    private int cantidadCalificaciones;

    private KudoDominio(Builder builder) {
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

    public void registrarVisualizacion() {
        cantidadVisualizaciones++;
    }

    public UUID getId() {
        return id;
    }

    public UsuarioDominio getAutor() {
        return autor;
    }

    public UsuarioDominio getDestinatario() {
        return destinatario;
    }

    public CategoriaDominio getCategoria() {
        return categoria;
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
        private UsuarioDominio autor;
        private UsuarioDominio destinatario;
        private CategoriaDominio categoria;
        private String contenido;
        private LocalDateTime fechaCreacion;
        private EstadoContenido estado;
        private int cantidadVisualizaciones;
        private BigDecimal promedioCalificacion;
        private int cantidadCalificaciones;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            autor = new UsuarioDominio.Builder().build();
            destinatario = new UsuarioDominio.Builder().build();
            categoria = new CategoriaDominio.Builder().build();
            contenido = UtilTexto.VACIO;
            fechaCreacion = UtilFecha.FECHA_HORA_POR_DEFECTO;
            estado = EstadoContenido.CREADO;
            cantidadVisualizaciones = UtilNumero.CERO;
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

        public Builder destinatario(UsuarioDominio destinatario) {
            this.destinatario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(destinatario, new UsuarioDominio.Builder().build());
            return this;
        }

        public Builder categoria(CategoriaDominio categoria) {
            this.categoria = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(categoria, new CategoriaDominio.Builder().build());
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

        public KudoDominio build() {
            return new KudoDominio(this);
        }
    }
}
