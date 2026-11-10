package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public class PaginaKudosDto {

    private List<KudoDto> kudos;
    private int pagina;
    private int totalPaginas;
    private int totalKudos;

    public PaginaKudosDto() {
        setKudos(new ArrayList<>());
        setPagina(1);
        setTotalPaginas(0);
        setTotalKudos(0);
    }

    public List<KudoDto> getKudos() {
        return kudos;
    }

    public void setKudos(List<KudoDto> kudos) {
        this.kudos = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(kudos, new ArrayList<>());
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = Math.max(pagina, 1);
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = Math.max(totalPaginas, 0);
    }

    public int getTotalKudos() {
        return totalKudos;
    }

    public void setTotalKudos(int totalKudos) {
        this.totalKudos = Math.max(totalKudos, 0);
    }
}
