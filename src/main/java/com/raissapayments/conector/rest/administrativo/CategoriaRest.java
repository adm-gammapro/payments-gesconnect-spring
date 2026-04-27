package com.raissapayments.conector.rest.administrativo;

import com.raissapayments.conector.domain.dto.administrativo.request.CategoriaRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.request.CategoriaSearchDto;
import com.raissapayments.conector.domain.dto.administrativo.request.CategoriaUsuarioRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.request.VinculoCategoriaUsuarioRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.response.CategoriaResponseDto;
import com.raissapayments.conector.domain.dto.administrativo.response.VinculoCategoriaUsuarioResponseDto;
import com.raissapayments.conector.service.administrativo.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<Page<CategoriaResponseDto>> getPageCategorias(@RequestBody CategoriaSearchDto searchDto) {
        return ResponseEntity.ok(categoriaService.listPage(searchDto));
    }

    @PostMapping("/get-categoria")
    public ResponseEntity<CategoriaResponseDto> getCategoria(@RequestBody CategoriaRequestDto requestDto) {
        return ResponseEntity.ok(categoriaService.get(requestDto.getCodigo()));
    }

    @PostMapping("/list-categoria")
    public ResponseEntity<List<CategoriaResponseDto>> list() {
        return ResponseEntity.ok(categoriaService.listCategorias());
    }

    @PostMapping("/list-vinculo-categoria-usuario")
    public ResponseEntity<VinculoCategoriaUsuarioResponseDto> listVinculoCategoriaUsuario(@RequestBody VinculoCategoriaUsuarioRequestDto requestDto) {
        return ResponseEntity.ok(categoriaService.listVinculoCategoriaUsuario(requestDto));
    }

    @PostMapping("/vincular-categoria-usuario")
    public ResponseEntity<Boolean> vincularCategoriaUsuario(@RequestBody CategoriaUsuarioRequestDto requestDto) {
        return ResponseEntity.ok(categoriaService.vincularCategoriaUsuario(requestDto));
    }

    @PostMapping("/desvincular-categoria-usuario")
    public ResponseEntity<Boolean> desvincularCategoriaUsuario(@RequestBody CategoriaUsuarioRequestDto requestDto) {
        return ResponseEntity.ok(categoriaService.desvincularCategoriaUsuario(requestDto));
    }
}