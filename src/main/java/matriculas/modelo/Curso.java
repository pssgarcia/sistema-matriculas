package matriculas.modelo;

import java.util.ArrayList;
import java.util.List;

public class Curso {

    private Long id;
    private String codigo;
    private String nome;
    private int creditos;
    private final List<Disciplina> disciplinas = new ArrayList<>();

    public Curso() {
    }

    public Curso(Long id, String codigo, String nome, int creditos) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.creditos = creditos;
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar vínculo de disciplina ao curso");
    }

    public void removerDisciplina(Disciplina disciplina) {
        throw new UnsupportedOperationException("TODO: implementar remoção de disciplina do curso");
    }

    public List<Disciplina> listarDisciplinasPorTipo(TipoDisciplina tipo) {
        throw new UnsupportedOperationException("TODO: implementar filtro de disciplinas por tipo");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }
}
