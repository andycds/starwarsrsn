package br.pro.software.starwarsrsn.repository;

import br.pro.software.starwarsrsn.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
}
