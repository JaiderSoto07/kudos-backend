package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDominioExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;

public enum EstadoContenido {

    CREADO {
        @Override
        public EstadoContenido publicar() {
            return VISIBLE;
        }
    },
    VISIBLE {
        @Override
        public EstadoContenido reportar() {
            return REPORTADO;
        }

        @Override
        public EstadoContenido vetar() {
            return VETADO;
        }
    },
    REPORTADO {
        @Override
        public EstadoContenido reportar() {
            return REPORTADO;
        }

        @Override
        public EstadoContenido iniciarRevision() {
            return EN_REVISION;
        }

        @Override
        public EstadoContenido vetar() {
            return VETADO;
        }
    },
    EN_REVISION {
        @Override
        public EstadoContenido reportar() {
            return EN_REVISION;
        }

        @Override
        public EstadoContenido liberar() {
            return VISIBLE;
        }

        @Override
        public EstadoContenido vetar() {
            return VETADO;
        }
    },
    VETADO {
        @Override
        public boolean admiteInteraccion() {
            return false;
        }
    };

    public EstadoContenido publicar() {
        throw cambioNoPermitido("publicar");
    }

    public EstadoContenido reportar() {
        throw cambioNoPermitido("reportar");
    }

    public EstadoContenido iniciarRevision() {
        throw cambioNoPermitido("iniciar la revisión de");
    }

    public EstadoContenido liberar() {
        throw cambioNoPermitido("liberar");
    }

    public EstadoContenido vetar() {
        throw cambioNoPermitido("vetar");
    }

    public boolean admiteInteraccion() {
        return true;
    }

    private KudosExcepcion cambioNoPermitido(String accion) {
        return KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.CONTENIDO_CAMBIO_NO_PERMITIDO.formatted(accion, name()));
    }
}
