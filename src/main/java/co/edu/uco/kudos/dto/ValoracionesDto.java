package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ValoracionesDto {

    private BigDecimal promedio;
    private int cantidad;
    private List<Integer> distribucion;
    private int miCalificacion;

    public ValoracionesDto() {
        setPromedio(BigDecimal.ZERO);
        setCantidad(0);
        setDistribucion(new ArrayList<>());
        setMiCalificacion(0);
    }

    public BigDecimal getPromedio() {
        return promedio;
    }

    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio == null ? BigDecimal.ZERO : promedio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = Math.max(cantidad, 0);
    }

    public List<Integer> getDistribucion() {
        return distribucion;
    }

    public void setDistribucion(List<Integer> distribucion) {
        this.distribucion = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(distribucion, new ArrayList<>());
    }

    public int getMiCalificacion() {
        return miCalificacion;
    }

    public void setMiCalificacion(int miCalificacion) {
        this.miCalificacion = Math.max(miCalificacion, 0);
    }
}
