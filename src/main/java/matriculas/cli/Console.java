package matriculas.cli;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Console {

    public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Scanner entrada;
    private final PrintStream saida;

    public Console() {
        this(new Scanner(System.in, codificacaoDaEntrada()), System.out);
    }

    public Console(Scanner entrada, PrintStream saida) {
        this.entrada = entrada;
        this.saida = saida;
    }

    public String lerTexto(String rotulo) {
        while (true) {
            String valor = lerTextoOpcional(rotulo);
            if (!valor.isEmpty()) {
                return valor;
            }
            erro("Campo obrigatório.");
        }
    }

    public String lerTextoOpcional(String rotulo) {
        saida.print(rotulo + ": ");
        saida.flush();
        if (!entrada.hasNextLine()) {
            throw new EntradaEncerradaException();
        }
        return entrada.nextLine().trim();
    }

    public int lerInteiro(String rotulo) {
        while (true) {
            String valor = lerTexto(rotulo);
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                erro("Informe um número inteiro.");
            }
        }
    }

    public int lerOpcao(int maior) {
        while (true) {
            int opcao = lerInteiro("Opção");
            if (opcao >= 0 && opcao <= maior) {
                return opcao;
            }
            erro("Opção inválida.");
        }
    }

    public LocalDate lerData(String rotulo, LocalDate padrao) {
        while (true) {
            String valor = lerTextoOpcional(rotulo + " (dd/mm/aaaa" + (padrao == null ? "" : ", Enter = "
                    + padrao.format(FORMATO_DATA)) + ")");
            if (valor.isEmpty() && padrao != null) {
                return padrao;
            }
            try {
                return LocalDate.parse(valor, FORMATO_DATA);
            } catch (DateTimeParseException e) {
                erro("Data inválida.");
            }
        }
    }

    public boolean confirmar(String pergunta) {
        return lerTexto(pergunta + " (s/n)").toLowerCase().startsWith("s");
    }

    public void titulo(String texto) {
        saida.println();
        saida.println("=".repeat(texto.length() + 8));
        saida.println("    " + texto);
        saida.println("=".repeat(texto.length() + 8));
    }

    public void mensagem(String texto) {
        saida.println(texto);
    }

    public void sucesso(String texto) {
        saida.println("[OK] " + texto);
    }

    public void erro(String texto) {
        saida.println("[ERRO] " + texto);
    }

    public void tabela(List<String> cabecalho, List<List<String>> linhas) {
        if (linhas.isEmpty()) {
            saida.println("(nenhum registro)");
            return;
        }
        int[] larguras = new int[cabecalho.size()];
        for (int i = 0; i < larguras.length; i++) {
            larguras[i] = cabecalho.get(i).length();
            for (List<String> linha : linhas) {
                larguras[i] = Math.max(larguras[i], linha.get(i).length());
            }
        }
        imprimirLinha(cabecalho, larguras);
        StringBuilder separador = new StringBuilder();
        for (int largura : larguras) {
            separador.append("-".repeat(largura)).append("  ");
        }
        saida.println(separador.toString().stripTrailing());
        linhas.forEach(linha -> imprimirLinha(linha, larguras));
    }

    private void imprimirLinha(List<String> colunas, int[] larguras) {
        StringBuilder linha = new StringBuilder();
        for (int i = 0; i < colunas.size(); i++) {
            linha.append(String.format("%-" + larguras[i] + "s  ", colunas.get(i)));
        }
        saida.println(linha.toString().stripTrailing());
    }

    private static Charset codificacaoDaEntrada() {
        String nome = System.getProperty("stdin.encoding", System.getProperty("stdout.encoding"));
        try {
            return nome == null ? Charset.defaultCharset() : Charset.forName(nome);
        } catch (IllegalArgumentException e) {
            return Charset.defaultCharset();
        }
    }
}
