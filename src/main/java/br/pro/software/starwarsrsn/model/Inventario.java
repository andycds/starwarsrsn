package br.pro.software.starwarsrsn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Inventario {

    private static final int armaPontos = 4;
    private static final int municaoPontos = 3;
    private static final int aguaPontos = 2;
    private static final int comidaPontos = 1;

    @Id
    private long id;
    private int armas;
    private int municoes;
    private int aguas;
    private int comidas;

    public int calcularPontos() {
        return (armas * armaPontos) +
               (municoes * municaoPontos) +
               (aguas * aguaPontos) +
               (comidas * comidaPontos);
    }

    public void subtrair(Inventario outro) {
        this.armas -= outro.getArmas();
        this.municoes -= outro.getMunicoes();
        this.comidas -= outro.getComidas();
        this.aguas -= outro.getAguas();
    }

    public void adicionar(Inventario outro) {
        this.armas += outro.getArmas();
        this.municoes += outro.getMunicoes();
        this.comidas += outro.getComidas();
        this.aguas += outro.getAguas();
    }

    public boolean tem(Inventario outro) {
        return armas >= outro.armas &&
                municoes >= outro.municoes &&
                comidas >= outro.comidas &&
                aguas >= outro.aguas;
    }
}
