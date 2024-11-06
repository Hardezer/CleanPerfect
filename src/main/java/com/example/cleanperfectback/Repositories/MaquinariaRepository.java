package com.example.cleanperfectback.Repositories;

import com.example.cleanperfectback.Entities.Maquinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaquinariaRepository extends JpaRepository<Maquinaria, Long> {
    List<Maquinaria> findAllByOrderById();
    List<Maquinaria> findByNombre(String nombre);
    List<Maquinaria> findByNombreAndEstadoContainingIgnoreCase(String nombre, String estado); // Nuevo método
    List<Maquinaria> findByNombreContainingIgnoreCaseAndEstado(String nombre,String estado);

    Maquinaria findFirstByNombre(String nombre);
}

