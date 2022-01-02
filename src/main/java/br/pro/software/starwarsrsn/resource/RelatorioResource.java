package br.pro.software.starwarsrsn.resource;

import br.pro.software.starwarsrsn.model.Rebelde;
import br.pro.software.starwarsrsn.repository.RebeldeRepository;
import br.pro.software.starwarsrsn.transfer.RelatorioTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/relatorio")
public class RelatorioResource {

    @Autowired
    RebeldeRepository rebeldeRepository;

    @GetMapping
    public RelatorioTransfer total() {
        List<Rebelde> rebeldes = rebeldeRepository.findAll();
        double quantidadeTotal = rebeldes.size();
        List<Rebelde> traidores = rebeldes.stream().filter(Rebelde::isTraidor).collect(Collectors.toList());
        List<Rebelde> leais = rebeldes.stream().filter(r -> !r.isTraidor()).collect(Collectors.toList());

        double porcentagemLeais = leais.size() / quantidadeTotal;
        double porcentagemTraidores = traidores.size() / quantidadeTotal;
        long pontosPerdidosTraidores = traidores.stream().mapToLong(r-> r.getInventario().calcularPontos()).sum();
        long armas = 0, municoes = 0, aguas = 0, comidas = 0;
        for (Rebelde rebelde: leais) {
            armas += rebelde.getInventario().getArmas();
            municoes += rebelde.getInventario().getMunicoes();
            aguas += rebelde.getInventario().getAguas();
            comidas += rebelde.getInventario().getComidas();
        }

        return new RelatorioTransfer(porcentagemTraidores, porcentagemLeais, armas / leais.size(),
                municoes / leais.size(), aguas / leais.size(),
                comidas / leais.size(), pontosPerdidosTraidores);

    }

}
