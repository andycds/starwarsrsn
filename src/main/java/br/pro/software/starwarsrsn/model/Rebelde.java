package br.pro.software.starwarsrsn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Rebelde {
    private static final int MAX_REPORTE_TRAIDOR = 3;

    @Id
    @Getter
    private long id;
    @Getter
    private String nome;
    @Getter
    private Date nascimento;
    @Getter
    @Column(length = 1)
    private String genero;
    @OneToOne
    @JoinColumn(name = "localizacao_id")
    @Getter
    @Setter
    private Localizacao localizacao;
    @OneToOne
    @JoinColumn(name = "inventario_id")
    @Getter
    private Inventario inventario;
    @Column(columnDefinition = "integer default 0")
    private int quantidadeReportado = 0;

    public boolean isTraidor() {
        return quantidadeReportado >= MAX_REPORTE_TRAIDOR;
    }

    public long getIdade() {
        return Period.between(nascimento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                LocalDate.now()).getYears();
    }

    public void reportarComoTraidor() {
        if (quantidadeReportado < MAX_REPORTE_TRAIDOR) {
            quantidadeReportado++;
        }
    }

}
