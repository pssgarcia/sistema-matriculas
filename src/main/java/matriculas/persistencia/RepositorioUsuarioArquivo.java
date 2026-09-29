package matriculas.persistencia;

import matriculas.modelo.Aluno;
import matriculas.modelo.Curso;
import matriculas.modelo.Professor;
import matriculas.modelo.Secretaria;
import matriculas.modelo.Usuario;
import matriculas.repositorio.RepositorioCurso;
import matriculas.repositorio.RepositorioUsuario;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class RepositorioUsuarioArquivo extends RepositorioArquivo<Usuario> implements RepositorioUsuario {

    private final RepositorioCurso repositorioCurso;

    public RepositorioUsuarioArquivo(Path arquivo, RepositorioCurso repositorioCurso) {
        super(arquivo);
        this.repositorioCurso = repositorioCurso;
        carregar();
    }

    @Override
    public Optional<Usuario> buscarPorLogin(String login) {
        return todas().filter(usuario -> usuario.getLogin().equalsIgnoreCase(login)).findFirst();
    }

    @Override
    public <U extends Usuario> List<U> buscarPorPerfil(Class<U> perfil) {
        return todas().filter(perfil::isInstance).map(perfil::cast).toList();
    }

    @Override
    protected Long idDe(Usuario usuario) {
        return usuario.getId();
    }

    @Override
    protected void atribuirId(Usuario usuario, Long id) {
        usuario.setId(id);
    }

    @Override
    protected String serializar(Usuario usuario) {
        Object campo1 = null;
        Object campo2 = null;
        if (usuario instanceof Aluno aluno) {
            campo1 = aluno.getMatricula();
            campo2 = aluno.getCurso() == null ? null : aluno.getCurso().getId();
        } else if (usuario instanceof Professor professor) {
            campo1 = professor.getSiape();
            campo2 = professor.getDepartamento();
        } else if (usuario instanceof Secretaria secretaria) {
            campo1 = secretaria.getSetor();
        }
        return FormatoArquivo.juntar(usuario.getId(), usuario.getPerfil(), usuario.getNome(), usuario.getLogin(),
                usuario.getHashSenha(), campo1, campo2);
    }

    @Override
    protected Usuario desserializar(String[] campos) {
        Usuario usuario = switch (campos[1]) {
            case "ALUNO" -> {
                Aluno aluno = new Aluno();
                aluno.setMatricula(campos[5]);
                Long cursoId = FormatoArquivo.paraLong(campos[6]);
                if (cursoId != null) {
                    Curso curso = repositorioCurso.buscarPorId(cursoId)
                            .orElseThrow(() -> new IllegalStateException("Curso " + cursoId + " não encontrado"));
                    aluno.setCurso(curso);
                }
                yield aluno;
            }
            case "PROFESSOR" -> {
                Professor professor = new Professor();
                professor.setSiape(campos[5]);
                professor.setDepartamento(campos[6]);
                yield professor;
            }
            case "SECRETARIA" -> {
                Secretaria secretaria = new Secretaria();
                secretaria.setSetor(campos[5]);
                yield secretaria;
            }
            default -> throw new IllegalStateException("Perfil desconhecido: " + campos[1]);
        };
        usuario.setId(Long.valueOf(campos[0]));
        usuario.setNome(campos[2]);
        usuario.setLogin(campos[3]);
        usuario.setHashSenha(campos[4]);
        return usuario;
    }
}
