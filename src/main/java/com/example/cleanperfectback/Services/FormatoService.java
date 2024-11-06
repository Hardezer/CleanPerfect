package com.example.cleanperfectback.Services;

import com.example.cleanperfectback.Entities.Formato;
import com.example.cleanperfectback.Repositories.FormatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormatoService {

    @Autowired
    private FormatoRepository formatoRepository;

    public Formato saveFormato(Formato formato){
        return formatoRepository.save(formato);
    }

    public List<Formato> findAllFormatos(){
        return formatoRepository.findAll();
    }

    public List<Formato> findFormatoByCategoria(Long categoria){
        return formatoRepository.findByCategoriaOrderByTamano(categoria);
    }
}
