package com.example.cleanperfectback.Repositories;

import com.example.cleanperfectback.Entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {


    Producto findFirstByCodigoBarra(Long codigo_barra);
    //Producto getByCodigoBarra(Long codigo_barra);
    List<Producto> findAllByOrderById();
    Optional<Producto> findByCodigoBarra(Long codigoBarra);
}
