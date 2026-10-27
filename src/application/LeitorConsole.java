package application;

import java.util.InputMismatchException;
import java.util.Scanner;

public class LeitorConsole {

    @SuppressWarnings("unused")
    private final Scanner scanner;

    public LeitorConsole(Scanner scanner) {
        this.scanner = scanner;
    }

    public double lerValor() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println("Erro: Número inválido. Tente Novamente.");
            }
        }
    }

    public int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println("Erro: Número inválido. Tente Novamente.");
            }
        }
    }

    public Long lerLong() {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println("Erro: Número inválido. Tente Novamente.");
            }
        }
    }

    public String lerTexto() {
        return scanner.nextLine();
    }

}
