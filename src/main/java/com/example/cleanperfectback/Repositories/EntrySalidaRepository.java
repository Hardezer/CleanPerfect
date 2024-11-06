package com.example.cleanperfectback.Repositories;

import com.example.cleanperfectback.Entities.EntrySalida;
import com.example.cleanperfectback.Entities.EntrySalidaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntrySalidaRepository extends JpaRepository<EntrySalida, Long> {

    List<EntrySalida> findAllByOrderById();
}
