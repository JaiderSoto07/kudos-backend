package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class NotificacionDominio {

    private final UUID id;
    private final UsuarioDominio destinatario;
    private final TipoNotificacion tipo;
    private final String mensaje;
    private final String tipoReferencia;
    private final UUID idReferencia;
    private final LocalDateTime fechaCreacion;
    private boolean leida;
    private LocalDateTime fechaLectura;

    private NotificacionDominio(Builder builder) {
        this.id = builder.id;
        this.destinatario = builder.destinatario;
        this.tipo = builder.tipo;
        this.mensaje = builder.mensaje;
        this.tipoReferencia = builder.tipoReferencia;
        this.idReferencia = builder.idReferencia;
        this.fechaCreacion = builder.fechaCreacion;
        this.leida = builder.leida;
        this.fechaLectura = builder.fechaLectura;
    }

    public void marcarLeida() {
        if (!leida) {
            leida = true;
            fechaLectura = UtilFecha.fechaActual();
        }
    }

    public UUID getId() {
        return id;
    }

    public UsuarioDominio getDestinatario() {
        return destinatario;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getTipoReferencia() {
        return tipoReferencia;
    }

    public UUID getIdReferencia() {
        return idReferencia;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public boolean isLeida() {
        return leida;
    }

    public LocalDateTime getFechaLectura() {
        return fechaLectura;
    }

    public static final class Builder {

        private UUID id;
        private UsuarioDominio destinatario;
        private TipoNotificacion tipo;
        private String mensaje;
        private String tipoReferencia;
        private UUID idReferencia;
        private LocalDateTime fechaCreacion;
        private boolean leida;
        private LocalDateTime fechaLectura;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            destinatario = new UsuarioDominio.Builder().build();
            tipo = TipoNotificacion.KUDO_RECIBIDO;
            mensaje = UtilTexto.VACIO;
            tipoReferencia = UtilTexto.VACIO;
            idReferencia = UtilId.VALOR_DEFECTO;
            fechaCreacion = UtilFecha.FECHA_HORA_POR_DEFECTO;
            leida = false;
            fechaLectura = UtilFecha.FECHA_HORA_POR_DEFECTO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder destinatario(UsuarioDominio destinatario) {
            this.destinatario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(destinatario, new UsuarioDominio.Builder().build());
            return this;
        }

        public Builder tipo(TipoNotificacion tipo) {
            this.tipo = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(tipo, TipoNotificacion.KUDO_RECIBIDO);
            return this;
        }

        public Builder mensaje(String mensaje) {
            this.mensaje = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(mensaje);
            return this;
        }

        public Builder tipoReferencia(String tipoReferencia) {
            this.tipoReferencia = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(tipoReferencia);
            return this;
        }

        public Builder idReferencia(UUID idReferencia) {
            this.idReferencia = UtilId.valorDefecto(idReferencia);
            return this;
        }

        public Builder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = UtilFecha.valorDefecto(fechaCreacion);
            return this;
        }

        public Builder leida(boolean leida) {
            this.leida = leida;
            return this;
        }

        public Builder fechaLectura(LocalDateTime fechaLectura) {
            this.fechaLectura = UtilFecha.valorDefecto(fechaLectura);
            return this;
        }

        public NotificacionDominio build() {
            return new NotificacionDominio(this);
        }
    }
}
