package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dominio.EstadoFacultad;
import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

public final class AsegurarFacultadNoInactivaRule implements Rule<FacultadDominio> {

    private static final AsegurarFacultadNoInactivaRule INSTANCIA = new AsegurarFacultadNoInactivaRule();

    private AsegurarFacultadNoInactivaRule() {
    }

    public static AsegurarFacultadNoInactivaRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(FacultadDominio... datos) {
        if (datos[0].getEstado() == EstadoFacultad.INACTIVA) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.FACULTAD_YA_INACTIVA);
        }
    }
}
