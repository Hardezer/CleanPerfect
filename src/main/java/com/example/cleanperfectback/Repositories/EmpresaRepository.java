package com.example.cleanperfectback.Repositories;

import com.example.cleanperfectback.Entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa,Long> {
    Empresa findByNombre(String nombre);
    List<Empresa> findAllByOrderById();
    List<Empresa> findByEstado(String estado);

    //boolean existsByNombre(String nombre);
}
