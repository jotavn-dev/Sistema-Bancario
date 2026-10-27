package util;

import application.LeitorConsole;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacoes {

	private static final Scanner scanner = new Scanner(System.in);
	private static final LeitorConsole leitor = new LeitorConsole(scanner);

	public static void identificarRegex(String texto) {
		while (true) {
			Pattern patternNumero = Pattern.compile("[^\\p{L}\\s]");
			Matcher temNumeroOuSimbolo = patternNumero.matcher(texto);

			if (temNumeroOuSimbolo.find()) {
				System.out.println("Digite somente letras!");
				System.out.print("Tente Novamente: ");
				texto = leitor.lerTexto();
				continue;
			}
			break;
		}
	}
	
	public static String formatadorCpf(Long cpf) {
		String cpfFormatado = String.valueOf(cpf);

		return String.format("%s.%s.%s-%s", cpfFormatado.substring(0, 3), cpfFormatado.substring(3, 6),
				cpfFormatado.substring(6, 9), cpfFormatado.substring(9, 11));
	}
}
