package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacoes {

	public static boolean identificarRegex(String texto) {
		Pattern patternNumero = Pattern.compile("[^\\p{L}\\s]");
		Matcher temNumeroOuSimbolo = patternNumero.matcher(texto);

		return temNumeroOuSimbolo.find();
	}
	
	public static String formatadorCpf(Long cpf) {
		String cpfFormatado = String.valueOf(cpf);

		return String.format("%s.%s.%s-%s", cpfFormatado.substring(0, 3), cpfFormatado.substring(3, 6),
				cpfFormatado.substring(6, 9), cpfFormatado.substring(9, 11));
	}
}
