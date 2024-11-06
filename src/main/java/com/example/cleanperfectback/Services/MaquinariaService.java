package com.example.cleanperfectback.Services;

import com.example.cleanperfectback.Entities.Entry;
import com.example.cleanperfectback.Entities.Maquinaria;
import com.example.cleanperfectback.Repositories.MaquinariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaquinariaService {

    @Autowired
    private MaquinariaRepository maquinariaRepository;

    public List<Maquinaria> findAll() {
        return maquinariaRepository.findAll();
    }

    public Optional<Maquinaria> findById(Long id) {
        return maquinariaRepository.findById(id);
    }

    public Maquinaria save(Maquinaria maquinaria) {
        return maquinariaRepository.save(maquinaria);
    }

    public void deleteById(Long id) {
        maquinariaRepository.deleteById(id);
    }

    public Optional<Maquinaria> cambiarEstado(Long id) {
        return maquinariaRepository.findById(id)
                .map(maquinaria -> {
                    String nuevoEstado = maquinaria.getEstado().equals("Disponible") ? "No disponible" : "Disponible";
                    maquinaria.setEstado(nuevoEstado);

                    // Al cambiar el estado establece estos campos a null
                    maquinaria.setFechaSalida(null);
                    maquinaria.setFechaReingreso(null); // Asegúrate de usar el nombre correcto del campo
                    maquinaria.setEmpresa(null);

                    return maquinariaRepository.save(maquinaria);
                });
    }

    public Optional<Maquinaria> cambiarEstado(Long id, String estado) {
        return maquinariaRepository.findById(id)
                .map(maquinaria -> {
                    maquinaria.setEstado(estado);
                    return maquinariaRepository.save(maquinaria);
                });
    }
    public Optional<Maquinaria> findByNombre(Long id) {
        return maquinariaRepository.findById(id);
    }

    public List<Maquinaria> findByNombreAndEstado(String nombre, String estado) {
        return maquinariaRepository.findByNombreContainingIgnoreCaseAndEstado(nombre, estado);
    }

    public Maquinaria findFirstByNombre(String nombre) {
        return maquinariaRepository.findFirstByNombre(nombre);
    }

    public void actualizarStocks(List<Entry> maquinarias) {
        maquinarias.forEach(entry -> {
            Maquinaria maquinaria = maquinariaRepository.findById(entry.getIdProducto()).get();
            maquinaria.setFechaReingreso(null);
            maquinaria.setEstado("Disponible");
            maquinaria.setEmpresa(null);
            maquinaria.setFechaSalida(null);
            maquinariaRepository.save(maquinaria);
        });
    }
}
