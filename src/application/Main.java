package application;

import java.util.Locale;
import java.util.Scanner;

import model.services.ContaService;
import model.services.LoginService;

public class Main {
	
	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner scanner = new Scanner(System.in);
		
		LoginService loginService = new LoginService();
		ContaService contaService = new ContaService();
		
		
			loginService.login();
			contaService.menu();
	}
}
