package br.pro.software.starwarsrsn.repository;

import br.pro.software.starwarsrsn.model.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {
}
