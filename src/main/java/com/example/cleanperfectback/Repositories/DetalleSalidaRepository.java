package com.example.cleanperfectback.Repositories;

import com.example.cleanperfectback.Entities.DetalleSalida;
import com.example.cleanperfectback.Entities.Salida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetalleSalidaRepository extends JpaRepository<DetalleSalida, Long> {
    // Métodos personalizados pueden ser añadidos aquí
    List<DetalleSalida> findAllByOrderById();
    @Query("SELECT SUM(d.costoTotal) FROM DetalleSalida d WHERE d.salida.id = :salidaId")
    Integer getTotalCostBySalidaId(@Param("salidaId") Long salidaId);

    @Query("SELECT 'Total' as tipo, CAST(SUM(p.cantidad) AS long) as totalStock, CAST(0 AS long) as stockSalido, " +
            "CAST(GREATEST(SUM(p.cantidad) - COALESCE(SUM(d.cantidad), 0), 0) AS long) as totalSinSalida, 'Total' as nombre " +
            "FROM Producto p " +
            "LEFT JOIN DetalleSalida d ON p.id = d.itemSalida.id " +
            "WHERE p.tipo = 'insumo' " +
            "UNION ALL " +
            "SELECT p.tipo, CAST(0 AS long) as totalStock, COALESCE(CAST(SUM(d.cantidad) AS long), CAST(0 AS long)) as stockSalido, " +
            "CAST(0 AS long) as totalSinSalida, e.nombre " +
            "FROM Producto p " +
            "LEFT JOIN DetalleSalida d ON p.id = d.itemSalida.id " +
            "LEFT JOIN Salida s ON d.salida.id = s.id " +
            "LEFT JOIN Empresa e ON s.empresa.id = e.id " +
            "WHERE p.tipo = 'insumo' " +
            "GROUP BY p.tipo, e.nombre")
    List<Object[]> findStockDataInsumos();

    @Query("SELECT 'Total' as tipo, CAST(SUM(p.cantidad) AS long) as totalStock, CAST(0 AS long) as stockSalido, " +
            "CAST(GREATEST(SUM(p.cantidad) - COALESCE(SUM(d.cantidad), 0), 0) AS long) as totalSinSalida, 'Total' as nombre " +
            "FROM Ropa p " +
            "LEFT JOIN DetalleSalida d ON p.id = d.itemSalida.id " +
            "UNION ALL " +
            "SELECT 'ropa' as tipo, CAST(0 AS long) as totalStock, COALESCE(CAST(SUM(d.cantidad) AS long), CAST(0 AS long)) as stockSalido, " +
            "CAST(0 AS long) as totalSinSalida, e.nombre " +
            "FROM Ropa p " +
            "LEFT JOIN DetalleSalida d ON p.id = d.itemSalida.id " +
            "LEFT JOIN Salida s ON d.salida.id = s.id " +
            "LEFT JOIN Empresa e ON s.empresa.id = e.id " +
            "GROUP BY e.nombre")
    List<Object[]> findStockDataRopa();




}