package com.example.cleanperfectback.Services;

import com.example.cleanperfectback.Entities.Empresa;
import com.example.cleanperfectback.Repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.cleanperfectback.Entities.Categoria;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;

    public Categoria saveCategoria(Categoria categoria){
        return categoriaRepository.save(categoria);

    }

    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    public boolean deleteCategoria(Long id){
        if (!categoriaRepository.existsById(id)) {
            throw new IllegalStateException("La categoria con la id " + id + " no existe");
        }
        categoriaRepository.deleteById(id);
        return true;
    }




}
