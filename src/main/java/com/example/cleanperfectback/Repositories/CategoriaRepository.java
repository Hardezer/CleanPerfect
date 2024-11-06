package com.example.cleanperfectback.Repositories;

import com.example.cleanperfectback.Entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
    boolean existsByNombre(String nombre);
    Categoria findFirstByNombre(String nombre);

    List<Categoria> findAllByOrderById();
}
