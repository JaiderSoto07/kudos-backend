package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.CalificacionDominio;
import co.edu.uco.kudos.dto.ValoracionesDto;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.calificacion.ValidarEstrellasRule;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ValoracionesDtoAssembler {

    private static final ValoracionesDtoAssembler INSTANCIA = new ValoracionesDtoAssembler();

    private ValoracionesDtoAssembler() {
    }

    public static ValoracionesDtoAssembler getInstance() {
        return INSTANCIA;
    }

    public ValoracionesDto armar(List<CalificacionDominio> calificaciones, UUID idActor) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(calificaciones, new ArrayList<CalificacionDominio>());
        var distribucion = new ArrayList<Integer>();
        for (var estrella = ValidarEstrellasRule.ESTRELLAS_MINIMAS; estrella <= ValidarEstrellasRule.ESTRELLAS_MAXIMAS; estrella++) {
            distribucion.add(0);
        }

        var suma = 0;
        var miCalificacion = 0;
        for (var calificacion : listaSegura) {
            var posicion = calificacion.getEstrellas() - ValidarEstrellasRule.ESTRELLAS_MINIMAS;
            distribucion.set(posicion, distribucion.get(posicion) + 1);
            suma += calificacion.getEstrellas();
            if (calificacion.getAutor().getId().equals(idActor)) {
                miCalificacion = calificacion.getEstrellas();
            }
        }

        var dto = new ValoracionesDto();
        dto.setCantidad(listaSegura.size());
        dto.setPromedio(listaSegura.isEmpty()
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(suma).divide(BigDecimal.valueOf(listaSegura.size()), 2, RoundingMode.HALF_UP));
        dto.setDistribucion(distribucion);
        dto.setMiCalificacion(miCalificacion);
        return dto;
    }
}
