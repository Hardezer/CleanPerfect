package com.example.cleanperfectback.Repositories;

import com.example.cleanperfectback.Entities.Salida;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface SalidaRepository extends JpaRepository<Salida, Long> {
    // Métodos personalizados pueden ser añadidos aquí
    List<Salida> findByEmpresaId(Long empresaId);

    List<Salida> findByEmpresaIdAndFechaBetween(Long empresaId, LocalDate startDate, LocalDate endDate);

    List<Salida> findAllByOrderById();
}
