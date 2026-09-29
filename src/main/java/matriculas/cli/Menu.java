package matriculas.cli;

import matriculas.excecao.MatriculaException;
import matriculas.modelo.Usuario;
import matriculas.persistencia.PersistenciaException;
import java.util.List;

/**
 * Laço genérico de menu: exibe as opções, executa a ação escolhida e apresenta ao usuário
 * as violações de regra de negócio sem encerrar o programa.
 */
public abstract class Menu {

    protected final Contexto contexto;
    protected final Console console;

    protected Menu(Contexto contexto) {
        this.contexto = contexto;
        this.console = contexto.console();
    }

    protected abstract String titulo();

    protected abstract List<Opcao> opcoes();

    public void executar() {
        List<Opcao> opcoes = opcoes();
        while (true) {
            console.titulo(titulo());
            for (int i = 0; i < opcoes.size(); i++) {
                console.mensagem(String.format("%2d - %s", i + 1, opcoes.get(i).descricao()));
            }
            console.mensagem(String.format("%2d - %s", 0, rotuloSaida()));
            int escolha = console.lerOpcao(opcoes.size());
            if (escolha == 0) {
                return;
            }
            Opcao opcao = opcoes.get(escolha - 1);
            console.titulo(opcao.descricao());
            try {
                opcao.acao().run();
            } catch (MatriculaException | IllegalArgumentException | IllegalStateException e) {
                console.erro(e.getMessage());
            } catch (PersistenciaException e) {
                console.erro(e.getMessage() + " (" + e.getCause().getMessage() + ")");
            }
        }
    }

    protected String rotuloSaida() {
        return "Sair";
    }

    protected void alterarSenha(Usuario usuario) {
        String atual = console.lerTexto("Senha atual");
        String nova = console.lerTexto("Nova senha");
        contexto.servicoAutenticacao().alterarSenha(usuario, atual, nova);
        console.sucesso("Senha alterada.");
    }

    protected record Opcao(String descricao, Runnable acao) {
    }
}
