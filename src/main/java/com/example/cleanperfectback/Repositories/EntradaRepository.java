package com.example.cleanperfectback.Repositories;

import com.example.cleanperfectback.Entities.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntradaRepository extends JpaRepository<Entrada, Long> {
    List<Entrada> findAllByOrderById();
}
