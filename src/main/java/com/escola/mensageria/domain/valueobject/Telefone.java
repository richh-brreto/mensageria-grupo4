package com.escola.mensageria.domain.valueobject;

import com.escola.mensageria.domain.exception.TelefoneInvalidoException;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Telefone {

    private static final String CODIGO_PAIS_PADRAO = "55";
    private static final String PREFIXO_INTERNACIONAL = "+";
    private static final String PREFIXO_CHAMADA_INTERNACIONAL = "00";
    private static final String PREFIXO_WHATSAPP = "whatsapp:";

    private static final int TAMANHO_MINIMO_E164 = 8;
    private static final int TAMANHO_MAXIMO_E164 = 15;
    private static final int TAMANHO_LANDLINE = 10;
    private static final int TAMANHO_CELULAR = 11;

    private static final Pattern FORMATO_ACEITO = Pattern.compile("\\+?[0-9\\s\\-().]+");

    private final String valor;

    private Telefone(String valor) {
        this.valor = valor;
    }

    public static Telefone de(String numero) {
        String original = numero == null ? "" : numero.trim();

        if (original.isEmpty()) {
            throw new TelefoneInvalidoException(numero, "numero ausente.");
        }
        if (!FORMATO_ACEITO.matcher(original).matches()) {
            throw new TelefoneInvalidoException(numero, "contem caracteres nao suportados.");
        }

        String digitos = extrairDigitos(original);
        if (digitos.isEmpty()) {
            throw new TelefoneInvalidoException(numero, "nao contem digitos.");
        }

        boolean internacional = original.startsWith(PREFIXO_INTERNACIONAL)
                || digitos.startsWith(PREFIXO_CHAMADA_INTERNACIONAL);

        String base = internacional
                ? removerPrefixoChamadaInternacional(digitos)
                : aplicarCodigoBrasil(digitos);

        return new Telefone(PREFIXO_INTERNACIONAL + validarTamanho(base, numero));
    }

    public String getValor() {
        return valor;
    }

    public String comPrefixoWhatsApp() {
        return PREFIXO_WHATSAPP + valor;
    }

    private static String removerPrefixoChamadaInternacional(String digitos) {
        return digitos.startsWith(PREFIXO_CHAMADA_INTERNACIONAL)
                ? digitos.substring(PREFIXO_CHAMADA_INTERNACIONAL.length())
                : digitos;
    }

    private static String aplicarCodigoBrasil(String digitos) {
        String local = digitos.length() == TAMANHO_CELULAR + 1 && digitos.startsWith("0")
                ? digitos.substring(1)
                : digitos;

        boolean tamanhoLocal = local.length() == TAMANHO_LANDLINE
                || local.length() == TAMANHO_CELULAR;

        return tamanhoLocal ? CODIGO_PAIS_PADRAO + local : local;
    }

    private static String validarTamanho(String digitos, String numero) {
        if (digitos.length() < TAMANHO_MINIMO_E164 || digitos.length() > TAMANHO_MAXIMO_E164) {
            throw new TelefoneInvalidoException(
                    numero,
                    "deve conter entre " + TAMANHO_MINIMO_E164 + " e " + TAMANHO_MAXIMO_E164
                            + " digitos no formato E.164 (ex.: +5511999999999)."
            );
        }
        return digitos;
    }

    private static String extrairDigitos(String numero) {
        return numero.replaceAll("[^0-9]", "");
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        return outro instanceof Telefone telefone && valor.equals(telefone.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
