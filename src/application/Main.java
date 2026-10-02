package application;

import java.util.Locale;
import java.util.Scanner;

import model.services.ContaService;
import model.services.LoginService;

public class Main {
	
	public static void main(String[] args) {
		
		Locale.setDefault(Locale.US);
		Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
		
		LoginService loginService = new LoginService(scanner);
		ContaService contaService = new ContaService(scanner);
		
			loginService.login();
			contaService.menu();
	}
}
