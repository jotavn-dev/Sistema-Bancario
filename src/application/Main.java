package application;

import java.util.Locale;
import java.util.Scanner;

import model.entities.Cliente;
import model.services.ContaService;
import model.services.LoginService;

public class Main {
	
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

		LeitorConsole leitor = new LeitorConsole(scanner);
		
		LoginService loginService = new LoginService(leitor);
		Cliente clienteLogado = loginService.login();
		
		ContaService contaService = new ContaService(leitor, clienteLogado);
		contaService.menu();
	}
}
