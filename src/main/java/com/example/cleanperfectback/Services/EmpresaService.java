package com.example.cleanperfectback.Services;

import com.example.cleanperfectback.Entities.Empresa;
import com.example.cleanperfectback.Entities.Producto;
import com.example.cleanperfectback.Repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    EmpresaRepository empresaRepository;


    //Guardar empresa si no existe una con el mismo nombre
    public boolean saveEmpresa(Empresa empresa){
        Empresa existeEmpresa = getEmpresaByNombre(empresa.getNombre());
        if(existeEmpresa != null){
            if("Inactivo".equalsIgnoreCase(existeEmpresa.getEstado())){
                System.out.println("Ya existe una empresa que estaba Inactiva con el nombre " + empresa.getNombre());
                return cambioEstado(existeEmpresa.getId());
            }else {
                System.out.println("Ya existe una empresa con el nombre " + empresa.getNombre());
                return false;
            }
        }
        empresa.setEstado("Activo");
        empresaRepository.save(empresa);
        return true;
    }

    public Empresa getEmpresaByNombre(String nombre){
        return empresaRepository.findByNombre(nombre);
    }

    public List<Empresa> getAllEmpresas() {
        return empresaRepository.findAll();
    }

    public boolean deleteEmpresa(Long id){
        if (!empresaRepository.existsById(id)) {
            throw new IllegalStateException("La empresa con la id " + id + " no existe");
        }
        empresaRepository.deleteById(id);
        return true;
    }
    public Optional<Empresa> findById(Long id) {
        return empresaRepository.findById(id);
    }

    public Empresa updateEmpresa(Long id, Empresa empresa) {
        Empresa existingEmpresa = empresaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("El producto con la id " + id + " no existe"));

        existingEmpresa.setNombre(empresa.getNombre());
        existingEmpresa.setEstado(empresa.getEstado());

        return empresaRepository.save(existingEmpresa);
    }
    public boolean cambioEstado(Long id) {
        Optional<Empresa> optionalEmpresa = empresaRepository.findById(id);
        if (optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();
            if ("Activo".equalsIgnoreCase(empresa.getEstado())) {
                empresa.setEstado("Inactivo");
            } else if ("Inactivo".equalsIgnoreCase(empresa.getEstado())) {
                empresa.setEstado("Activo");
            } else {
                return false;
            }
            empresaRepository.save(empresa);
            return true;
        }
        return false;
    }

    public List<Empresa> getEmpresasActivas() {
        return empresaRepository.findByEstado("Activo");
    }

    public Optional<String> getNombreById(Long id) {
        return empresaRepository.findById(id).map(Empresa::getNombre);
    }
}

