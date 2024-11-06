package com.example.cleanperfectback.Services;

import com.example.cleanperfectback.Controllers.SalidaController;
import com.example.cleanperfectback.Entities.*;
import com.example.cleanperfectback.Repositories.DetalleSalidaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DetalleSalidaService {

    @Autowired
    private DetalleSalidaRepository detalleSalidaRepository;
    @Autowired
    private RopaService ropaService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private MaquinariaService maquinariaService;

    @Transactional
    public int createDetalleSalida(Salida salida, List<SalidaController.DTO> detalleSalida) {
        List<DetalleSalida> detalles = new ArrayList<>();
        int totalCost = 0;

        for (SalidaController.DTO x : detalleSalida) {
            DetalleSalida entry = new DetalleSalida();
            entry.setSalida(salida);
            entry.setCantidad(Long.valueOf(x.getCantidad()));
            if (x.getNombre().equals("Insumo")) {
                x.setTipoItem("Producto");
            } else if (x.getFechaSalida() != null) {
                x.setTipoItem("Maquinaria");
            } else {
                x.setTipoItem("Ropa");
            }
            switch (x.getTipoItem()) {
                case "Producto":
                    Producto producto = productoService.findProductoByCodigoBarra(Long.valueOf(x.getIdTalla()));
                    entry.setItemSalida(producto);
                    productoService.updateStock(producto, -entry.getCantidad().intValue());
                    entry.setCostoTotal((int) (producto.getCostoDeCompra() * entry.getCantidad()));
                    totalCost += entry.getCostoTotal();
                    break;
                case "Ropa":
                    Ropa ropa = ropaService.findByNombreAndTalla(x.getNombre(), x.getIdTalla());
                    entry.setItemSalida(ropa);
                    ropaService.updateStock(ropa, -entry.getCantidad().intValue());
                    entry.setCostoTotal((int) (ropa.getPrecio() * entry.getCantidad()));
                    totalCost += entry.getCostoTotal();
                    break;
                case "Maquinaria":
                    Maquinaria maquinaria = maquinariaService.findFirstByNombre(x.getIdTalla());
                    entry.setItemSalida(maquinaria);
                    maquinariaService.cambiarEstado(maquinaria.getId(), "En uso");
                    maquinaria.setFechaSalida(x.getFechaSalida());
                    maquinaria.setFechaReingreso(x.getFechaVuelta());
                    maquinaria.setEmpresa(salida.getEmpresa().getId());
                    maquinariaService.save(maquinaria);
                    entry.setCostoTotal((int) (maquinaria.getCostoUso() * entry.getCantidad()));
                    totalCost += entry.getCostoTotal();
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de item desconocido: " + x.getTipoItem());
            }
            detalles.add(entry);
            detalleSalidaRepository.save(entry);
        }

        return totalCost;
    }

    public List<DetalleSalida> getDetallesBySalidaId(Long salidaId) {
        return detalleSalidaRepository.findAll().stream()
                .filter(detalle -> detalle.getSalida().getId().equals(salidaId))
                .collect(Collectors.toList());
    }

    public int getTotalCostBySalidaId(Long salidaId) {
        Integer totalCost = detalleSalidaRepository.getTotalCostBySalidaId(salidaId);
        return totalCost != null ? totalCost : 0;
    }

    public String revisarStock(List<SalidaController.DTO> detalle) {
        String item = null;
        for (SalidaController.DTO x : detalle) {
            if (x.getNombre().equals("Insumo")) {
                Producto producto = productoService.findProductoByCodigoBarra(Long.valueOf(x.getIdTalla()));
                if (producto.getCantidad() < Long.parseLong(x.getCantidad())) {
                    item = "No hay suficiente stock de " + producto.getNombre() + " " + producto.getCodigoBarra();
                    break;
                }
            } else if (x.getFechaSalida() == null) {
                Ropa ropa = ropaService.findByNombreAndTalla(x.getNombre(), x.getIdTalla());
                if (ropa.getCantidad() < Long.parseLong(x.getCantidad())) {
                    item = "No hay suficiente stock de " + ropa.getNombre() + " " + ropa.getTalla();
                    break;
                }
            }
        }
        return item;
    }
}


