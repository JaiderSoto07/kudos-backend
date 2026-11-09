package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.CategoriaDto;
import co.edu.uco.kudos.negocio.fachada.CategoriaFachada;
import co.edu.uco.kudos.negocio.fachada.impl.CategoriaFachadaImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaControlador extends AbstractRestController {

    private final CategoriaFachada fachada = new CategoriaFachadaImpl();

    @GetMapping
    public ResponseEntity<RespuestaOk<CategoriaDto>> consultarActivas() {
        var categorias = fachada.consultarActivas();
        return ResponseEntity.ok(RespuestaOk.crear(categorias));
    }
}
