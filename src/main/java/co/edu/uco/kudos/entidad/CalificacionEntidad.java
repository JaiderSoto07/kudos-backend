package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilNumero;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class CalificacionEntidad {

    private final UUID id;
    private final UsuarioEntidad autor;
    private final KudoEntidad kudo;
    private final ReplicaEntidad replica;
    private final int estrellas;
    private final LocalDateTime fechaCalificacion;
    private final LocalDateTime fechaModificacion;
    private final boolean activa;

    private CalificacionEntidad(Builder builder) {
        this.id = builder.id;
        this.autor = builder.autor;
        this.kudo = builder.kudo;
        this.replica = builder.replica;
        this.estrellas = builder.estrellas;
        this.fechaCalificacion = builder.fechaCalificacion;
        this.fechaModificacion = builder.fechaModificacion;
        this.activa = builder.activa;
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

    public ReplicaEntidad getReplica() {
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
        private UsuarioEntidad autor;
        private KudoEntidad kudo;
        private ReplicaEntidad replica;
        private int estrellas;
        private LocalDateTime fechaCalificacion;
        private LocalDateTime fechaModificacion;
        private boolean activa;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            autor = new UsuarioEntidad.Builder().build();
            kudo = new KudoEntidad.Builder().build();
            replica = new ReplicaEntidad.Builder().build();
            estrellas = UtilNumero.CERO;
            fechaCalificacion = UtilFecha.FECHA_HORA_POR_DEFECTO;
            fechaModificacion = UtilFecha.FECHA_HORA_POR_DEFECTO;
            activa = true;
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

        public Builder replica(ReplicaEntidad replica) {
            this.replica = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(replica, new ReplicaEntidad.Builder().build());
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

        public CalificacionEntidad build() {
            return new CalificacionEntidad(this);
        }
    }
}
