package application;

import java.util.Locale;
import java.util.Scanner;

import model.entities.Cliente;
import model.services.ContaService;
import model.services.LoginService;

public class Main {
	
	public static void main(String[] args) {
		
		Locale.setDefault(Locale.US);
		Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
		
		LoginService loginService = new LoginService(scanner);
		Cliente clienteAutenticado = loginService.login();
		
		ContaService contaService = new ContaService(scanner, clienteAutenticado);

			contaService.menu();
	}
}
