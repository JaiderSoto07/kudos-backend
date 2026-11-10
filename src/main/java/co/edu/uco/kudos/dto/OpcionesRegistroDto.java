package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public class OpcionesRegistroDto {

    private List<String> roles;
    private List<FacultadDto> facultades;
    private List<ProgramaAcademicoDto> programas;

    public OpcionesRegistroDto() {
        setRoles(new ArrayList<>());
        setFacultades(new ArrayList<>());
        setProgramas(new ArrayList<>());
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(roles, new ArrayList<>());
    }

    public List<FacultadDto> getFacultades() {
        return facultades;
    }

    public void setFacultades(List<FacultadDto> facultades) {
        this.facultades = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(facultades, new ArrayList<>());
    }

    public List<ProgramaAcademicoDto> getProgramas() {
        return programas;
    }

    public void setProgramas(List<ProgramaAcademicoDto> programas) {
        this.programas = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(programas, new ArrayList<>());
    }
}
