package br.pro.software.starwarsrsn.resource;

import br.pro.software.starwarsrsn.model.Inventario;
import br.pro.software.starwarsrsn.model.Localizacao;
import br.pro.software.starwarsrsn.model.Rebelde;
import br.pro.software.starwarsrsn.repository.InventarioRepository;
import br.pro.software.starwarsrsn.repository.LocalizacaoRepository;
import br.pro.software.starwarsrsn.repository.RebeldeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rebelde")
public class RebeldeResource {

    @Autowired
    private RebeldeRepository rebeldeRepository;

    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    @Autowired
    private InventarioRepository inventarioRepository;


    /* Adicionar rebeldes
     Um rebelde deve ter um nome, idade, gênero,
     localização (latitude, longitude e nome, na galáxia, da base ao qual faz parte).
     Um rebelde também possui um inventário que deverá ser passado na requisição com os recursos em sua posse.
     */
    @PostMapping
    public Rebelde adicionar(@RequestBody Rebelde novoRebelde) {
        inventarioRepository.save(novoRebelde.getInventario());
        localizacaoRepository.save(novoRebelde.getLocalizacao());
        return rebeldeRepository.save(novoRebelde);
    }

    /* - Atualizar localização do rebelde
Um rebelde deve possuir a capacidade de reportar sua última localização, armazenando a nova
latitude/longitude/nome (não é necessário rastrear as localizações, apenas sobrescrever a última é o
suficiente).
*/
    @PutMapping(value = "/{id}/localizacao")
    public ResponseEntity<Rebelde> atualizarLocalizacao(@PathVariable Long id, @RequestBody Localizacao localizacao) {
        Optional<Rebelde> r = rebeldeRepository.findById(id);
        if (r.isPresent()) {
            Rebelde rp = r.get();
            localizacaoRepository.save(localizacao);
            rp.setLocalizacao(localizacao);
            rebeldeRepository.save(rp);
            return new ResponseEntity<>(rp, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /*
    - Reportar o rebelde como um traidor
    Eventualmente algum rebelde irá trair a resistência e se aliar ao império. Quando isso acontecer, nós
    precisamos informar que o rebelde é um traidor.
    Um traidor não pode negociar os recursos com os demais rebeldes, não pode manipular seu inventário, nem
    ser exibido em relatórios.
    Um rebelde é marcado como traidor quando, ao menos, três outros rebeldes reportarem a traição.
    Uma vez marcado como traidor, os itens do inventário se tornam inacessíveis (eles não podem ser
            negociados com os demais).
    */
    @PutMapping(value = "/{id}/reportartraidor")
    public ResponseEntity<String> atualizarLocalizacao(@PathVariable Long id) {
        Optional<Rebelde> r = rebeldeRepository.findById(id);
        if (r.isPresent()) {
            Rebelde rp = r.get();
            rp.reportarComoTraidor();
            rebeldeRepository.save(rp);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{primeiro}/negociar/{segundo}")
    public ResponseEntity<Rebelde> negociar(@PathVariable Long primeiro, @PathVariable Long segundo,
                                            @RequestBody List<Inventario> inventarios) {
        Optional<Rebelde> primeiroRebelde = rebeldeRepository.findById(primeiro);
        Optional<Rebelde> segundoRebelde = rebeldeRepository.findById(segundo);
        if (primeiroRebelde.isPresent() && segundoRebelde.isPresent()) {
            if (efetuarNegociacao(primeiroRebelde.get(), inventarios.get(0), segundoRebelde.get(), inventarios.get(1))) {
                return new ResponseEntity<>(primeiroRebelde.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    private boolean efetuarNegociacao(Rebelde primeiro, Inventario inventarioPrimeiro,
                                      Rebelde segundo, Inventario inventarioSegundo) {
        if (inventarioPrimeiro.calcularPontos() == inventarioSegundo.calcularPontos() &&
                inventarioPrimeiro.tem(inventarioPrimeiro) &&
                inventarioSegundo.tem(inventarioSegundo)) {
            primeiro.getInventario().subtrair(inventarioPrimeiro);
            segundo.getInventario().adicionar(inventarioPrimeiro);
            primeiro.getInventario().adicionar(inventarioSegundo);
            segundo.getInventario().subtrair(inventarioSegundo);
            rebeldeRepository.save(primeiro);
            rebeldeRepository.save(segundo);
            return true;
        }
        return false;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rebelde> buscarPeloId(@PathVariable Long id) {
        Optional<Rebelde> r = rebeldeRepository.findById(id);
        return r.map(rebelde -> new ResponseEntity<>(rebelde, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}
