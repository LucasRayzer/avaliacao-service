package service.avaliacao.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AvaliacaoRespostaDto {
    private Long id;
    private Integer nota;
    private String comentario;
    private Long eventoId;
    private Long autorId;
    private LocalDateTime dataAvaliacao;
}