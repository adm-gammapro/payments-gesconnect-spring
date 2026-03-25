package com.raissapayments.conector.rest.administrativo;

import com.raissapayments.conector.domain.dto.administrativo.request.CategoriaRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.response.CategoriaResponseDto;
import com.raissapayments.conector.service.administrativo.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Slf4j
public class CategoriaRest {
    private final CategoriaService categoriaService;

    @PostMapping("/create-categoria")
    public ResponseEntity<CategoriaResponseDto> createCategoria(@Valid @RequestBody CategoriaRequestDto requestDto) {
        log.info("Registrando categoría");
        CategoriaResponseDto resp = categoriaService.create(requestDto);
        log.info("Categoría registrada correctamente");
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/update-categoria")
    public ResponseEntity<CategoriaResponseDto> updateCategoria(@Valid @RequestBody CategoriaRequestDto requestDto) {
        log.info("Actualizando categoría");
        CategoriaResponseDto resp = categoriaService.update(requestDto);
        log.info("Categoría actualizada correctamente");
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/delete-categoria")
    public ResponseEntity<CategoriaResponseDto> deleteCategoria(@RequestBody CategoriaRequestDto requestDto) {
        return ResponseEntity.ok(categoriaService.delete(requestDto));
    }

    @PostMapping("/list-page-categoria")
    public ResponseEntity<Page<CategoriaResponseDto>> getPageCategorias(@RequestParam(required = false) String filtroDescripcion,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
        return ResponseEntity.ok(categoriaService.listPage(filtroDescripcion, pageable));
    }

    @GetMapping("/get-categoria")
    public ResponseEntity<CategoriaResponseDto> getCategoria(@RequestParam(name = "codigoCategoria") Long codigoCategoria) {
        return ResponseEntity.ok(categoriaService.get(codigoCategoria));
    }
}