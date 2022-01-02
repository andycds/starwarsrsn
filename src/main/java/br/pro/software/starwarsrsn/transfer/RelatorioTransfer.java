package br.pro.software.starwarsrsn.transfer;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class RelatorioTransfer {

    private double porcentagemTraidores;
    private double porcentagemLeais;
    @Getter
    private double mediaItensArmas;
    @Getter
    private double mediaItensMunicoess;
    @Getter
    private double mediaItensAguas;
    @Getter
    private double mediaItensComida;
    @Getter
    private double pontosPerdidosTraidores;

    public double getPorcentagemTraidores() {
        return porcentagemTraidores * 100;
    }

    public double getPorcentagemLeais() {
        return porcentagemLeais * 100;
    }

}
