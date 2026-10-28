package model.entities;

import model.services.TaxaPagamento;

import java.util.ArrayList;
import java.util.List;

public class Emprestimo {

    private Double valor;
    private Integer prazo;
    private Double juros;
    private List<String> fluxoParcelas = new ArrayList<>();

    private TaxaPagamento taxaPagamento;

    public Emprestimo(Double valor, Integer prazo, TaxaPagamento taxaPagamento) {
        this.valor = valor;
        this.prazo = prazo;
        this.taxaPagamento = taxaPagamento;
    }

    public List<String> getFluxoParcelas() { return fluxoParcelas; }

    public Double getValor() { return valor; }

    public Integer getPrazo() { return prazo; }

    public Double getJuros() { return juros; }

    protected void calcularJuros() {
        double taxa = 1 + taxaPagamento.tax(valor);

        this.juros = valor * Math.pow(taxa, prazo);
    }

    private Double calcularParcela() {
        return juros / prazo;
    }

    protected void addParcela() {
        for (int numeral = 1; numeral <= prazo; numeral++) {

            fluxoParcelas.add(String.format("\n%s - parcela - R$ %.2f",
                    numeral, calcularParcela()));
        }
    }

    public void consultarParcela() {
        for (String consulta : fluxoParcelas) {
            System.out.println(consulta);
        }
    }
}