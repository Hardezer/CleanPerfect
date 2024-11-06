package com.example.cleanperfectback.Repositories;

import com.example.cleanperfectback.Entities.Ropa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RopaRepository extends JpaRepository<Ropa, Long> {
    List<Ropa> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndTalla(String nombre, String talla);
    List<Ropa> findByNombreAndTalla(String nombre, String talla);
    List<Ropa> findAllByOrderById();
}
