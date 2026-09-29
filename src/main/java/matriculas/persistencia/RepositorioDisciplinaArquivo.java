package matriculas.persistencia;

import matriculas.modelo.Curso;
import matriculas.modelo.Disciplina;
import matriculas.modelo.Professor;
import matriculas.modelo.StatusDisciplina;
import matriculas.modelo.TipoDisciplina;
import matriculas.repositorio.RepositorioCurso;
import matriculas.repositorio.RepositorioDisciplina;
import matriculas.repositorio.RepositorioUsuario;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class RepositorioDisciplinaArquivo extends RepositorioArquivo<Disciplina> implements RepositorioDisciplina {

    private final RepositorioCurso repositorioCurso;
    private final RepositorioUsuario repositorioUsuario;

    public RepositorioDisciplinaArquivo(Path arquivo, RepositorioCurso repositorioCurso,
                                        RepositorioUsuario repositorioUsuario) {
        super(arquivo);
        this.repositorioCurso = repositorioCurso;
        this.repositorioUsuario = repositorioUsuario;
        carregar();
    }

    @Override
    public Optional<Disciplina> buscarPorCodigo(String codigo) {
        return todas().filter(disciplina -> disciplina.getCodigo().equalsIgnoreCase(codigo)).findFirst();
    }

    @Override
    public List<Disciplina> buscarPorProfessor(Professor professor) {
        return todas().filter(disciplina -> professor.equals(disciplina.getProfessor())).toList();
    }

    @Override
    public List<Disciplina> buscarPorCurso(Curso curso) {
        return todas().filter(disciplina -> disciplina.getCurso() == curso).toList();
    }

    @Override
    protected Long idDe(Disciplina disciplina) {
        return disciplina.getId();
    }

    @Override
    protected void atribuirId(Disciplina disciplina, Long id) {
        disciplina.setId(id);
    }

    @Override
    protected String serializar(Disciplina disciplina) {
        return FormatoArquivo.juntar(disciplina.getId(), disciplina.getCodigo(), disciplina.getNome(),
                disciplina.getCreditos(), disciplina.getTipo(), disciplina.getStatus(),
                disciplina.getCurso().getId(), disciplina.getProfessor().getId());
    }

    @Override
    protected Disciplina desserializar(String[] campos) {
        Long cursoId = Long.valueOf(campos[6]);
        Long professorId = Long.valueOf(campos[7]);
        Curso curso = repositorioCurso.buscarPorId(cursoId)
                .orElseThrow(() -> new IllegalStateException("Curso " + cursoId + " não encontrado"));
        Professor professor = repositorioUsuario.buscarPorId(professorId)
                .filter(Professor.class::isInstance)
                .map(Professor.class::cast)
                .orElseThrow(() -> new IllegalStateException("Professor " + professorId + " não encontrado"));

        Disciplina disciplina = new Disciplina(Long.valueOf(campos[0]), campos[1], campos[2],
                Integer.parseInt(campos[3]), TipoDisciplina.valueOf(campos[4]), curso, professor);
        disciplina.setStatus(StatusDisciplina.valueOf(campos[5]));
        curso.adicionarDisciplina(disciplina);
        professor.adicionarDisciplina(disciplina);
        return disciplina;
    }
}
