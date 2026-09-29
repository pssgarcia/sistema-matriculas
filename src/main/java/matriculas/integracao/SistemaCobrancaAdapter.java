package matriculas.integracao;

import matriculas.modelo.Matricula;
import matriculas.persistencia.FormatoArquivo;
import matriculas.persistencia.PersistenciaException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Simula a integração com o sistema de cobrança externo registrando cada notificação
 * em um arquivo de remessa, que seria consumido pelo sistema de cobrança.
 */
public class SistemaCobrancaAdapter implements SistemaCobranca {

    private final Path arquivoRemessa;

    public SistemaCobrancaAdapter(Path arquivoRemessa) {
        this.arquivoRemessa = arquivoRemessa;
    }

    @Override
    public void notificarMatricula(Matricula matricula) {
        registrar("MATRICULA", matricula);
    }

    @Override
    public void notificarCancelamento(Matricula matricula) {
        registrar("CANCELAMENTO", matricula);
    }

    private void registrar(String evento, Matricula matricula) {
        String linha = FormatoArquivo.juntar(LocalDateTime.now(), evento, matricula.getAluno().getMatricula(),
                matricula.getAluno().getNome(), matricula.getDisciplina().getCodigo(),
                matricula.getDisciplina().getCreditos());
        try {
            if (arquivoRemessa.getParent() != null) {
                Files.createDirectories(arquivoRemessa.getParent());
            }
            Files.write(arquivoRemessa, List.of(linha), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao notificar o sistema de cobrança", e);
        }
    }

    public Path getArquivoRemessa() {
        return arquivoRemessa;
    }
}
