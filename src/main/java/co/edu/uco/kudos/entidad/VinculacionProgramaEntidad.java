package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class VinculacionProgramaEntidad {

    private final UUID id;
    private final UsuarioEntidad usuario;
    private final ProgramaAcademicoEntidad programa;
    private final String tipoVinculo;
    private final LocalDateTime fechaInicio;
    private final LocalDateTime fechaFin;
    private final String motivoDesvinculacion;

    private VinculacionProgramaEntidad(Builder builder) {
        this.id = builder.id;
        this.usuario = builder.usuario;
        this.programa = builder.programa;
        this.tipoVinculo = builder.tipoVinculo;
        this.fechaInicio = builder.fechaInicio;
        this.fechaFin = builder.fechaFin;
        this.motivoDesvinculacion = builder.motivoDesvinculacion;
    }

    public UUID getId() {
        return id;
    }

    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public ProgramaAcademicoEntidad getPrograma() {
        return programa;
    }

    public String getTipoVinculo() {
        return tipoVinculo;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public String getMotivoDesvinculacion() {
        return motivoDesvinculacion;
    }

    public static final class Builder {

        private UUID id;
        private UsuarioEntidad usuario;
        private ProgramaAcademicoEntidad programa;
        private String tipoVinculo;
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaFin;
        private String motivoDesvinculacion;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            usuario = new UsuarioEntidad.Builder().build();
            programa = new ProgramaAcademicoEntidad.Builder().build();
            tipoVinculo = UtilTexto.VACIO;
            fechaInicio = UtilFecha.FECHA_HORA_POR_DEFECTO;
            fechaFin = UtilFecha.FECHA_HORA_POR_DEFECTO;
            motivoDesvinculacion = UtilTexto.VACIO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder usuario(UsuarioEntidad usuario) {
            this.usuario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(usuario, new UsuarioEntidad.Builder().build());
            return this;
        }

        public Builder programa(ProgramaAcademicoEntidad programa) {
            this.programa = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(programa, new ProgramaAcademicoEntidad.Builder().build());
            return this;
        }

        public Builder tipoVinculo(String tipoVinculo) {
            this.tipoVinculo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(tipoVinculo);
            return this;
        }

        public Builder fechaInicio(LocalDateTime fechaInicio) {
            this.fechaInicio = UtilFecha.valorDefecto(fechaInicio);
            return this;
        }

        public Builder fechaFin(LocalDateTime fechaFin) {
            this.fechaFin = UtilFecha.valorDefecto(fechaFin);
            return this;
        }

        public Builder motivoDesvinculacion(String motivoDesvinculacion) {
            this.motivoDesvinculacion = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivoDesvinculacion);
            return this;
        }

        public VinculacionProgramaEntidad build() {
            return new VinculacionProgramaEntidad(this);
        }
    }
}
