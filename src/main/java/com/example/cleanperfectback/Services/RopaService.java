package com.example.cleanperfectback.Services;

import com.example.cleanperfectback.Entities.Entry;
import com.example.cleanperfectback.Entities.EntrySalida;
import com.example.cleanperfectback.Entities.Ropa;
import com.example.cleanperfectback.Repositories.RopaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RopaService {
    @Autowired
    RopaRepository ropaRepository;

    public boolean saveRopa(Ropa ropa) {
        if (yaExiste(ropa)) {
            return false;
        }
        ropaRepository.save(ropa);
        return true;
    }

    public boolean yaExiste(Ropa ropa) {
        return ropaRepository.existsByNombreAndTalla(ropa.getNombre(), ropa.getTalla());
    }

    public boolean actualizarCantidad(Long id, Long cantidad) {
        Ropa ropa = ropaRepository.findById(id).orElse(null);
        if (ropa == null) {
            return false;
        }

        // Manejar el caso donde la cantidad puede ser null
        if (ropa.getCantidad() == null) {
            ropa.setCantidad(0L);
        }

        ropa.setCantidad(ropa.getCantidad() + cantidad);
        ropaRepository.save(ropa);
        return true;
    }

    public List<Ropa> buscarPorNombreYTalla(String nombre, String talla) {
        return ropaRepository.findByNombreAndTalla(nombre, talla);
    }

    public Ropa updateRopa(Long id, Ropa ropa) {
        if (!ropaRepository.existsById(id)) {
            return null;
        }
        ropa.setId(id);
        return ropaRepository.save(ropa);
    }

    public void deleteRopa(Long id) {
        if (!ropaRepository.existsById(id)) {
            throw new IllegalStateException("La ropa con la id " + id + " no existe");
        }
        ropaRepository.deleteById(id);
    }

    public List<Ropa> findAllRopas() {
        return ropaRepository.findAll();
    }

    public Ropa findById(Long id) {
        return ropaRepository.findById(id).orElse(null);
    }

    public List<Ropa> buscarPorNombre(String nombre) {
        return ropaRepository.findByNombreContainingIgnoreCase(nombre);
    }


    public Ropa findByNombreAndTalla(String nombre, String talla) {
        return ropaRepository.findByNombreAndTalla(nombre, talla).get(0);
    }

    public void actualizarStocks(List<Entry> ropas) {
        ropas.forEach(x -> {
            Ropa ropa = findById(x.getIdProducto());
            ropa.setCantidad(ropa.getCantidad() + x.getCantidad());
            ropaRepository.save(ropa);
        });
    }

    public void actualizarStocksSalida(List<EntrySalida> salidas) {
        salidas.forEach(x -> {
            Ropa ropa = ropaRepository.findById(x.getId().getIdProducto()).orElse(null);
            if (ropa != null) {
                ropa.setCantidad(ropa.getCantidad() - x.getCantidad());
                ropaRepository.save(ropa);
            }
        });
    }

    public boolean cambiaCantidad(Long id) {
        Ropa ropa = ropaRepository.findById(id).orElse(null);
        if (ropa == null) {
            return false;
        }

        // Verificar si la cantidad ya es 0
        if (ropa.getCantidad() == 0L) {
            return false; // No hace nada si la cantidad ya es 0
        }

        ropa.setCantidad(0L);
        ropaRepository.save(ropa);
        return true;
    }

    public void updateStock(Ropa ropa, int i) {
        ropa.setCantidad(ropa.getCantidad() + i);
        ropaRepository.save(ropa);
    }
}

