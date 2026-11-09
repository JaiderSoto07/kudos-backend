package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilNumero;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class CalificacionDominio {

    private final UUID id;
    private final UsuarioDominio autor;
    private final KudoDominio kudo;
    private final ReplicaDominio replica;
    private int estrellas;
    private final LocalDateTime fechaCalificacion;
    private LocalDateTime fechaModificacion;
    private final boolean activa;

    private CalificacionDominio(Builder builder) {
        this.id = builder.id;
        this.autor = builder.autor;
        this.kudo = builder.kudo;
        this.replica = builder.replica;
        this.estrellas = builder.estrellas;
        this.fechaCalificacion = builder.fechaCalificacion;
        this.fechaModificacion = builder.fechaModificacion;
        this.activa = builder.activa;
    }

    public void modificarEstrellas(int nuevasEstrellas) {
        estrellas = nuevasEstrellas;
        fechaModificacion = UtilFecha.fechaActual();
    }

    public boolean esSobreKudo() {
        return !UtilId.esValorDefecto(kudo.getId());
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

    public ReplicaDominio getReplica() {
        return replica;
    }

    public int getEstrellas() {
        return estrellas;
    }

    public LocalDateTime getFechaCalificacion() {
        return fechaCalificacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public boolean isActiva() {
        return activa;
    }

    public static final class Builder {

        private UUID id;
        private UsuarioDominio autor;
        private KudoDominio kudo;
        private ReplicaDominio replica;
        private int estrellas;
        private LocalDateTime fechaCalificacion;
        private LocalDateTime fechaModificacion;
        private boolean activa;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            autor = new UsuarioDominio.Builder().build();
            kudo = new KudoDominio.Builder().build();
            replica = new ReplicaDominio.Builder().build();
            estrellas = UtilNumero.CERO;
            fechaCalificacion = UtilFecha.FECHA_HORA_POR_DEFECTO;
            fechaModificacion = UtilFecha.FECHA_HORA_POR_DEFECTO;
            activa = true;
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

        public Builder replica(ReplicaDominio replica) {
            this.replica = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(replica, new ReplicaDominio.Builder().build());
            return this;
        }

        public Builder estrellas(Integer estrellas) {
            this.estrellas = UtilNumero.obtenerValorDefectoSiEsNuloONegativo(estrellas, UtilNumero.CERO);
            return this;
        }

        public Builder fechaCalificacion(LocalDateTime fechaCalificacion) {
            this.fechaCalificacion = UtilFecha.valorDefecto(fechaCalificacion);
            return this;
        }

        public Builder fechaModificacion(LocalDateTime fechaModificacion) {
            this.fechaModificacion = UtilFecha.valorDefecto(fechaModificacion);
            return this;
        }

        public Builder activa(boolean activa) {
            this.activa = activa;
            return this;
        }

        public CalificacionDominio build() {
            return new CalificacionDominio(this);
        }
    }
}
