package matriculas.persistencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Converte registros em linhas de texto com campos separados por ';'.
 * Os caracteres especiais ('\', ';' e quebras de linha) são escapados com '\'.
 * Um campo vazio representa o valor nulo.
 */
public final class FormatoArquivo {

    public static final char SEPARADOR = ';';
    private static final char ESCAPE = '\\';

    private FormatoArquivo() {
    }

    public static String juntar(Object... campos) {
        StringBuilder linha = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) {
                linha.append(SEPARADOR);
            }
            linha.append(escapar(campos[i] == null ? "" : campos[i].toString()));
        }
        return linha.toString();
    }

    public static String[] separar(String linha) {
        List<String> campos = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == ESCAPE && i + 1 < linha.length()) {
                char proximo = linha.charAt(++i);
                atual.append(proximo == 'n' ? '\n' : proximo);
            } else if (c == SEPARADOR) {
                campos.add(atual.toString());
                atual.setLength(0);
            } else {
                atual.append(c);
            }
        }
        campos.add(atual.toString());
        return campos.toArray(String[]::new);
    }

    public static Long paraLong(String campo) {
        return campo.isEmpty() ? null : Long.valueOf(campo);
    }

    public static String paraTexto(String campo) {
        return campo.isEmpty() ? null : campo;
    }

    private static String escapar(String valor) {
        StringBuilder escapado = new StringBuilder();
        for (char c : valor.toCharArray()) {
            if (c == ESCAPE || c == SEPARADOR) {
                escapado.append(ESCAPE).append(c);
            } else if (c == '\n') {
                escapado.append(ESCAPE).append('n');
            } else if (c != '\r') {
                escapado.append(c);
            }
        }
        return escapado.toString();
    }
}
