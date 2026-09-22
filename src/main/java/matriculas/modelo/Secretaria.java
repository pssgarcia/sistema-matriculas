package matriculas.modelo;

public class Secretaria extends Usuario {

    private String setor;

    public Secretaria() {
    }

    public Secretaria(Long id, String nome, String login, String senha, String setor) {
        super(id, nome, login, senha);
        this.setor = setor;
    }

    @Override
    public String getPerfil() {
        return "SECRETARIA";
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }
}
