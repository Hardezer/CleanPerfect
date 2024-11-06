package com.example.cleanperfectback.Services;

import com.example.cleanperfectback.Controllers.EntradasController;
import com.example.cleanperfectback.Entities.Entrada;
import com.example.cleanperfectback.Entities.Entry;
import com.example.cleanperfectback.Entities.Ropa;
import com.example.cleanperfectback.Repositories.EntradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EntradaService {
    @Autowired
    private EntradaRepository entradaRepository;
    @Autowired
    private EntryService entryService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private RopaService ropaService;
    @Autowired
    private MaquinariaService maquinariaService;

    public void registrarEntrada(List<EntradasController.DTO> entradas) {
        Entrada entrada = crearEntrada();
        System.out.println("Entrada");
        System.out.println(entrada);
        List<Entry> pruductos = new ArrayList<>();
        List<Entry> ropas = new ArrayList<>();
        List<Entry> maquinarias = new ArrayList<>();
        entradas.forEach(x -> {
            Entry entry = new Entry();
            System.out.println("Entrada for");
            System.out.println(x);
            if(x.getNombre().startsWith("Maquinaria")){
                entry.setIdEntrada(entrada.getId());
                entry.setIdProducto(Long.valueOf(x.getIdTalla()));
                entry.setCantidad(Long.valueOf(x.getCantidad()));
                entry.setTipo(2);
                maquinarias.add(entry);
            } else if(!x.getNombre().equals("Insumo")){
                Ropa ropa = ropaService.findByNombreAndTalla(x.getNombre(),x.getIdTalla());
                if(ropa != null){
                    entry.setIdEntrada(entrada.getId());
                    entry.setIdProducto(ropa.getId());
                    entry.setCantidad(Long.valueOf(x.getCantidad()));
                    entry.setTipo(1);
                    System.out.println(entry);
                    ropas.add(entry);
                }
            }else{
                entry.setIdEntrada(entrada.getId());
                entry.setTipo(0);
                entry.setIdProducto(Long.valueOf(x.getIdTalla()));
                entry.setCantidad(Long.valueOf(x.getCantidad()));
                pruductos.add(entry);
            }
        });
        entryService.guardarEntries(pruductos);
        entryService.guardarEntries(ropas);
        entryService.guardarEntries(maquinarias);
        productoService.actualizarStocks(pruductos);
        ropaService.actualizarStocks(ropas);
        maquinariaService.actualizarStocks(maquinarias);
    }



    public Entrada crearEntrada() {
        Entrada entrada = new Entrada();
        entrada.setFecha(LocalDate.now());
        return entradaRepository.save(entrada);
    }

    public List<Entrada> getAllEntradas() {
        return entradaRepository.findAll();
    }
}

