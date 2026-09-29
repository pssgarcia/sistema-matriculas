package matriculas.cli;

import matriculas.excecao.AutenticacaoException;
import matriculas.modelo.Aluno;
import matriculas.modelo.Professor;
import matriculas.modelo.Secretaria;
import matriculas.modelo.Usuario;

public class TelaInicial {

    private final Contexto contexto;
    private final Console console;

    public TelaInicial(Contexto contexto) {
        this.contexto = contexto;
        this.console = contexto.console();
    }

    public void executar() {
        while (true) {
            console.titulo("Sistema de Matrículas - Universidade");
            console.mensagem(" 1 - Entrar");
            console.mensagem(" 0 - Sair do sistema");
            if (console.lerOpcao(1) == 0) {
                console.mensagem("Até logo!");
                return;
            }
            entrar();
        }
    }

    private void entrar() {
        String login = console.lerTexto("Login");
        String senha = console.lerTexto("Senha");
        Usuario usuario;
        try {
            usuario = contexto.servicoAutenticacao().autenticar(login, senha);
        } catch (AutenticacaoException e) {
            console.erro(e.getMessage());
            return;
        }
        console.sucesso("Bem-vindo(a), " + usuario.getNome() + "!");
        menuDo(usuario).executar();
        contexto.servicoAutenticacao().encerrarSessao(usuario);
        console.mensagem("Sessão encerrada.");
    }

    private Menu menuDo(Usuario usuario) {
        if (usuario instanceof Aluno aluno) {
            return new MenuAluno(contexto, aluno);
        }
        if (usuario instanceof Professor professor) {
            return new MenuProfessor(contexto, professor);
        }
        return new MenuSecretaria(contexto, (Secretaria) usuario);
    }
}
