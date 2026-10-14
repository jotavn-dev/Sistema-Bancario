package model.entities;

public class Cliente {

	private String name;
	private String cpf;
	
	public Cliente(String name, String cpf) {
		this.name = name;
		this.cpf = cpf;
	}

	public String getName() {
		return name;
	}

	public String getCpf() {
		String cpfFormatado =
		String.format("%s.***.***-%s", 
				cpf.substring(0, 3),
				cpf.substring(12));
		
		return cpfFormatado;
	}
}
