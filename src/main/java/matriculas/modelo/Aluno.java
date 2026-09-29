package matriculas.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Aluno extends Usuario {

    private String matricula;
    private Curso curso;
    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno() {
    }

    public Aluno(Long id, String nome, String login, String senha, String matricula, Curso curso) {
        super(id, nome, login, senha);
        this.matricula = matricula;
        this.curso = curso;
    }

    public Matricula matricularEm(Disciplina disciplina) {
        if (!podeSeMatricularEm(disciplina)) {
            throw new IllegalStateException("O aluno não pode se matricular na disciplina " + disciplina.getCodigo());
        }
        Matricula nova = new Matricula(null, this, disciplina, LocalDateTime.now());
        matriculas.add(nova);
        disciplina.adicionarMatricula(nova);
        return nova;
    }

    public void cancelarMatricula(Disciplina disciplina) {
        buscarMatriculaAtiva(disciplina)
                .orElseThrow(() -> new IllegalStateException(
                        "O aluno não está matriculado na disciplina " + disciplina.getCodigo()))
                .cancelar();
    }

    public Optional<Matricula> buscarMatriculaAtiva(Disciplina disciplina) {
        return matriculas.stream()
                .filter(m -> m.estaAtiva() && m.getDisciplina() == disciplina)
                .findFirst();
    }

    public List<Matricula> consultarMatriculasAtivas() {
        return matriculas.stream().filter(Matricula::estaAtiva).toList();
    }

    public List<Disciplina> consultarDisciplinasMatriculadas() {
        return consultarMatriculasAtivas().stream().map(Matricula::getDisciplina).toList();
    }

    public boolean podeSeMatricularEm(Disciplina disciplina) {
        return disciplina.estaDisponivelParaMatricula()
                && !estaMatriculadoEm(disciplina)
                && totalMatriculadasPorTipo(disciplina.getTipo()) < disciplina.getTipo().getLimitePorAluno();
    }

    public int totalMatriculadasPorTipo(TipoDisciplina tipo) {
        return (int) consultarMatriculasAtivas().stream()
                .filter(m -> m.getDisciplina().getTipo() == tipo)
                .count();
    }

    public boolean estaMatriculadoEm(Disciplina disciplina) {
        return buscarMatriculaAtiva(disciplina).isPresent();
    }

    @Override
    public String getPerfil() {
        return "ALUNO";
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }
}
