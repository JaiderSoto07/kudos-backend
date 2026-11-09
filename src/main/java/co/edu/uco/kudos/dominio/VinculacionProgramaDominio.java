package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDominioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class VinculacionProgramaDominio {

    private final UUID id;
    private final UsuarioDominio usuario;
    private final ProgramaAcademicoDominio programa;
    private final TipoVinculo tipoVinculo;
    private final LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String motivoDesvinculacion;

    private VinculacionProgramaDominio(Builder builder) {
        this.id = builder.id;
        this.usuario = builder.usuario;
        this.programa = builder.programa;
        this.tipoVinculo = builder.tipoVinculo;
        this.fechaInicio = builder.fechaInicio;
        this.fechaFin = builder.fechaFin;
        this.motivoDesvinculacion = builder.motivoDesvinculacion;
    }

    public boolean estaVigente() {
        return UtilFecha.esValorDefecto(fechaFin);
    }

    public void desvincular(String motivo) {
        if (!estaVigente()) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.VINCULO_YA_CERRADO);
        }
        motivoDesvinculacion = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivo);
        fechaFin = UtilFecha.fechaActual();
    }

    public UUID getId() {
        return id;
    }

    public UsuarioDominio getUsuario() {
        return usuario;
    }

    public ProgramaAcademicoDominio getPrograma() {
        return programa;
    }

    public TipoVinculo getTipoVinculo() {
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
        private UsuarioDominio usuario;
        private ProgramaAcademicoDominio programa;
        private TipoVinculo tipoVinculo;
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaFin;
        private String motivoDesvinculacion;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            usuario = new UsuarioDominio.Builder().build();
            programa = new ProgramaAcademicoDominio.Builder().build();
            tipoVinculo = TipoVinculo.ESTUDIANTE;
            fechaInicio = UtilFecha.FECHA_HORA_POR_DEFECTO;
            fechaFin = UtilFecha.FECHA_HORA_POR_DEFECTO;
            motivoDesvinculacion = UtilTexto.VACIO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder usuario(UsuarioDominio usuario) {
            this.usuario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(usuario, new UsuarioDominio.Builder().build());
            return this;
        }

        public Builder programa(ProgramaAcademicoDominio programa) {
            this.programa = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(programa, new ProgramaAcademicoDominio.Builder().build());
            return this;
        }

        public Builder tipoVinculo(TipoVinculo tipoVinculo) {
            this.tipoVinculo = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(tipoVinculo, TipoVinculo.ESTUDIANTE);
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

        public VinculacionProgramaDominio build() {
            return new VinculacionProgramaDominio(this);
        }
    }
}
