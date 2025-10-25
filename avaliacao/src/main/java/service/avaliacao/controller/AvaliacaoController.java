package service.avaliacao.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.avaliacao.dto.AvaliacaoRequisicaoDto;
import service.avaliacao.dto.AvaliacaoRespostaDto;
import service.avaliacao.service.AvaliacaoService;

import java.util.UUID;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping("/nova-avaliacao")
    public ResponseEntity<AvaliacaoRespostaDto> criarAvaliacao(
            @Valid @RequestBody AvaliacaoRequisicaoDto requisicaoDto,
            @RequestParam("participanteId") UUID autorId
    ) {
        AvaliacaoRespostaDto avaliacaoCriada = avaliacaoService.criarAvaliacao(requisicaoDto, autorId);
        return new ResponseEntity<>(avaliacaoCriada, HttpStatus.CREATED);
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<Page<AvaliacaoRespostaDto>> buscarAvaliacoesPorEvento(
            @PathVariable Long eventoId, Pageable pageable
    ) {
        Page<AvaliacaoRespostaDto> avaliacoes = avaliacaoService.buscarAvaliacoesPorEvento(eventoId, pageable);
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/minhas-avaliacoes")
    public ResponseEntity<Page<AvaliacaoRespostaDto>> buscarMinhasAvaliacoes(
            @RequestParam("participanteId") UUID autorId, Pageable pageable
    ) {
        Page<AvaliacaoRespostaDto> avaliacoes = avaliacaoService.buscarAvaliacoesPorAutor(autorId, pageable);
        return ResponseEntity.ok(avaliacoes);
    }

    @DeleteMapping("/{avaliacaoId}")
    public ResponseEntity<Void> deletarAvaliacao(
            @PathVariable Long avaliacaoId,
            @RequestParam("participanteId") UUID autorId
    ) {
        avaliacaoService.deletarAvaliacao(avaliacaoId, autorId);
        return ResponseEntity.noContent().build();
    }
}