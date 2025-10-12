package service.avaliacao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.avaliacao.dto.AvaliacaoRequisicaoDto;
import service.avaliacao.dto.AvaliacaoRespostaDto;
import service.avaliacao.exception.RecursoNaoEncontradoException;
import service.avaliacao.model.Avaliacao;
import service.avaliacao.repository.AvaliacaoRepository;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;

    @Transactional
    public AvaliacaoRespostaDto criarAvaliacao(AvaliacaoRequisicaoDto requisicaoDto, Long autorId) {

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(requisicaoDto.getNota());
        avaliacao.setComentario(requisicaoDto.getComentario());
        avaliacao.setEventoId(requisicaoDto.getEventoId());
        avaliacao.setAutorId(autorId);
        avaliacao.setDataAvaliacao(LocalDateTime.now());

        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);
        return paraRespostaDto(avaliacaoSalva);
    }

    @Transactional(readOnly = true)
    public Page<AvaliacaoRespostaDto> buscarAvaliacoesPorEvento(Long eventoId, Pageable pageable) {
        return avaliacaoRepository.findByEventoId(eventoId, pageable).map(this::paraRespostaDto);
    }

    @Transactional(readOnly = true)
    public Page<AvaliacaoRespostaDto> buscarAvaliacoesPorAutor(Long autorId, Pageable pageable) {
        return avaliacaoRepository.findByAutorId(autorId, pageable).map(this::paraRespostaDto);
    }

    @Transactional
    public void deletarAvaliacao(Long avaliacaoId, Long autorId) {
        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Avaliação não encontrada com ID: " + avaliacaoId));

        // apenas o próprio autor pode deletar sua avaliação
        if (!avaliacao.getAutorId().equals(autorId)) {
            throw new SecurityException("Usuário não autorizado a deletar esta avaliação.");
        }

        avaliacaoRepository.delete(avaliacao);
    }

    private AvaliacaoRespostaDto paraRespostaDto(Avaliacao avaliacao) {
        AvaliacaoRespostaDto dto = new AvaliacaoRespostaDto();
        dto.setId(avaliacao.getId());
        dto.setNota(avaliacao.getNota());
        dto.setComentario(avaliacao.getComentario());
        dto.setEventoId(avaliacao.getEventoId());
        dto.setAutorId(avaliacao.getAutorId());
        dto.setDataAvaliacao(avaliacao.getDataAvaliacao());
        return dto;
    }
}