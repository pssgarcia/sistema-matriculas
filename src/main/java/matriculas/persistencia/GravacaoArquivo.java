package matriculas.persistencia;

import java.io.IOException;
import java.nio.file.Path;

/**
 * No Windows, antivírus e indexadores podem manter um arquivo recém-gravado aberto por alguns
 * milissegundos, fazendo a gravação seguinte falhar. Por isso cada gravação é repetida algumas vezes.
 */
public final class GravacaoArquivo {

    private static final int TENTATIVAS = 10;
    private static final long ESPERA_MS = 50;

    private GravacaoArquivo() {
    }

    public static void comNovasTentativas(Path arquivo, Gravacao gravacao) {
        for (int tentativa = 1; ; tentativa++) {
            try {
                gravacao.executar();
                return;
            } catch (IOException e) {
                if (tentativa == TENTATIVAS) {
                    throw new PersistenciaException("Falha ao gravar o arquivo " + arquivo, e);
                }
                aguardar(tentativa);
            }
        }
    }

    private static void aguardar(int tentativa) {
        try {
            Thread.sleep(ESPERA_MS * tentativa);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @FunctionalInterface
    public interface Gravacao {
        void executar() throws IOException;
    }
}
