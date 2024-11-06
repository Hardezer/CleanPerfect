package com.example.cleanperfectback.Repositories;

import com.example.cleanperfectback.Entities.Formato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormatoRepository extends JpaRepository<Formato, Long> {
    List<Formato> findByCategoriaOrderByTamano(Long categoria);
    List<Formato> findAllByOrderById();
}
