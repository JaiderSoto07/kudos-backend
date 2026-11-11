package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public class PaginaAuditoriaDto {

    private List<AuditoriaDto> registros;
    private int pagina;
    private int totalPaginas;
    private int totalRegistros;

    public PaginaAuditoriaDto() {
        setRegistros(new ArrayList<>());
        setPagina(0);
        setTotalPaginas(0);
        setTotalRegistros(0);
    }

    public List<AuditoriaDto> getRegistros() {
        return registros;
    }

    public void setRegistros(List<AuditoriaDto> registros) {
        this.registros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(registros, new ArrayList<>());
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }
}
