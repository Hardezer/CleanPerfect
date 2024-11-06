package com.example.cleanperfectback.Services;

import com.example.cleanperfectback.Entities.*;
import com.example.cleanperfectback.Repositories.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileManagerService {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private FormatoRepository formatoRepository;
    @Autowired
    private MaquinariaRepository maquinariaRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private RopaRepository ropaRepository;

    public byte[] respaldar() throws IOException{
        List<Categoria> categorias = categoriaRepository.findAllByOrderById();
        List<Empresa> empresas = empresaRepository.findAllByOrderById();
        List<Formato> formatos = formatoRepository.findAllByOrderById();
        List<Maquinaria> maquinarias = maquinariaRepository.findAllByOrderById();
        List<Producto> productos = productoRepository.findAllByOrderById();
        List<Ropa> ropas = ropaRepository.findAllByOrderById();
        Workbook workbook = new XSSFWorkbook();

        List<Sheet> sheets = List.of(
                workbook.createSheet("Categorias"),
                workbook.createSheet("Empresas"),
                workbook.createSheet("Formatos"),
                workbook.createSheet("Maquinarias"),
                workbook.createSheet("Productos"),
                workbook.createSheet("Ropas")
        );
        //categorias
        List<String> cabeceras = List.of("ID","Nombre");
        crearCabecera(cabeceras,sheets.get(0));
        //empresas
        cabeceras = List.of("ID","Nombre","Estado");
        crearCabecera(cabeceras,sheets.get(1));
        //formatos
        cabeceras = List.of("ID","Tamaño","Unidad de Medida","ID Categoria");
        crearCabecera(cabeceras,sheets.get(2));
        //maquinarias
        cabeceras = List.of("ID","Nombre","Costo de Uso","Estado","Fecha de Salida","Fecha de Reingreso","ID Empresa");
        crearCabecera(cabeceras,sheets.get(3));
        //productos
        cabeceras = List.of("ID","costo de Compra","Cantidad","Tipo","Codigo de Barra","ID Categoria","ID Formato");
        crearCabecera(cabeceras,sheets.get(4));
        //ropas
        cabeceras = List.of("ID","Nombre","Talla","Precio","Cantidad");
        crearCabecera(cabeceras,sheets.get(5));

        rellenarDatos(categorias,sheets.get(0));
        rellenarDatos(empresas,sheets.get(1));
        rellenarDatos(formatos,sheets.get(2));
        rellenarDatos(maquinarias,sheets.get(3));
        rellenarDatos(productos,sheets.get(4));
        rellenarDatos(ropas,sheets.get(5));

        for (int i = 0; i < sheets.size(); i++) {
            sheets.get(i).autoSizeColumn(i);
        }
        // Escribir el archivo en un ByteArrayOutputStream
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            workbook.close();
            return byteArrayOutputStream.toByteArray();
        }
    }

    public void cargarDatosDesdeExcel(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());

        //categorías
        Sheet sheet = workbook.getSheet("Categorias");
        List<Categoria> categorias = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Categoria categoria = new Categoria();
            categoria.setId((long) row.getCell(0).getNumericCellValue());
            categoria.setNombre(row.getCell(1).getStringCellValue());
            categorias.add(categoria);
        }
        categoriaRepository.saveAll(categorias);

        //empresas
        sheet = workbook.getSheet("Empresas");
        List<Empresa> empresas = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Empresa empresa = new Empresa();
            empresa.setId((long) row.getCell(0).getNumericCellValue());
            empresa.setNombre(row.getCell(1).getStringCellValue());
            empresa.setEstado(row.getCell(2).getStringCellValue());
            empresas.add(empresa);
        }
        empresaRepository.saveAll(empresas);

        //formatos
        sheet = workbook.getSheet("Formatos");
        List<Formato> formatos = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Formato formato = new Formato();
            formato.setId((long) row.getCell(0).getNumericCellValue());
            formato.setTamano(getCellValueAsString(row.getCell(1)));
            formato.setUnidadDeMedida(row.getCell(2).getStringCellValue());
            formato.setCategoria((long) row.getCell(3).getNumericCellValue());
            formatos.add(formato);
        }
        formatoRepository.saveAll(formatos);

        //maquinarias
        sheet = workbook.getSheet("Maquinarias");
        List<Maquinaria> maquinarias = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Maquinaria maquinaria = new Maquinaria();
            maquinaria.setId((long) row.getCell(0).getNumericCellValue());
            maquinaria.setNombre(row.getCell(1).getStringCellValue());
            maquinaria.setCostoUso((int) row.getCell(2).getNumericCellValue());
            maquinaria.setEstado(row.getCell(3).getStringCellValue());
            maquinaria.setFechaSalida(getCellValueAsDate(row.getCell(4)));
            maquinaria.setFechaReingreso(getCellValueAsDate(row.getCell(5)));
            maquinaria.setEmpresa((long) row.getCell(6).getNumericCellValue());
            maquinarias.add(maquinaria);
        }
        maquinariaRepository.saveAll(maquinarias);

        //productos
        sheet = workbook.getSheet("Productos");
        List<Producto> productos = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Producto producto = new Producto();
            producto.setId((long) row.getCell(0).getNumericCellValue());
            producto.setCostoDeCompra((int) row.getCell(1).getNumericCellValue());
            producto.setCantidad((int) row.getCell(2).getNumericCellValue());
            producto.setTipo(String.valueOf(row.getCell(3).getStringCellValue()));
            producto.setCodigoBarra((long) row.getCell(4).getNumericCellValue());
            producto.setCategoria((long) row.getCell(5).getNumericCellValue());
            producto.setFormato((long) row.getCell(6).getNumericCellValue());
            productos.add(producto);
        }
        productoRepository.saveAll(productos);

        //ropas
        sheet = workbook.getSheet("Ropas");
        List<Ropa> ropas = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Ropa ropa = new Ropa();
            ropa.setId((long) row.getCell(0).getNumericCellValue());
            ropa.setNombre(row.getCell(1).getStringCellValue());
            ropa.setTalla(row.getCell(2).getStringCellValue());
            ropa.setPrecio((long) row.getCell(3).getNumericCellValue());
            ropa.setCantidad((long) row.getCell(4).getNumericCellValue());
            ropas.add(ropa);
        }
        ropaRepository.saveAll(ropas);

        workbook.close();
    }

    public LocalDate getCellValueAsDate(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        return cell.getLocalDateTimeCellValue().toLocalDate();
    }

    public String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            default:
                return "";
        }
    }

    public void crearCabecera(List<String> cabeceras, Sheet hoja) {
        Row headerRow = hoja.createRow(0);
        for (int i = 0; i < cabeceras.size(); i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(cabeceras.get(i));
        }
    }

    public  <T> void rellenarDatos(List<T> registros, Sheet hoja) {
        int rowNum = 1;
        for (Object registro : registros) {
            Row row = hoja.createRow(rowNum++);

            if (registro instanceof Categoria) {
                Categoria categoria = (Categoria) registro;
                row.createCell(0).setCellValue(categoria.getId());
                row.createCell(1).setCellValue(categoria.getNombre());
            } else if (registro instanceof Empresa) {
                Empresa empresa = (Empresa) registro;
                row.createCell(0).setCellValue(empresa.getId());
                row.createCell(1).setCellValue(empresa.getNombre());
                row.createCell(2).setCellValue(empresa.getEstado());
            }  else if (registro instanceof Formato) {
                Formato formato = (Formato) registro;
                row.createCell(0).setCellValue(formato.getId());
                row.createCell(1).setCellValue(formato.getTamano());
                row.createCell(2).setCellValue(formato.getUnidadDeMedida());
                row.createCell(3).setCellValue(formato.getCategoria());
            } else if (registro instanceof Maquinaria) {
                Maquinaria maquinaria = (Maquinaria) registro;
                row.createCell(0).setCellValue(maquinaria.getId());
                row.createCell(1).setCellValue(maquinaria.getNombre());
                row.createCell(2).setCellValue(maquinaria.getCostoUso());
                row.createCell(3).setCellValue(maquinaria.getEstado());
                row.createCell(4).setCellValue(maquinaria.getFechaSalida());
                row.createCell(5).setCellValue(maquinaria.getFechaReingreso());
                row.createCell(6).setCellValue(maquinaria.getEmpresa() != null ? maquinaria.getEmpresa() : 0);
            } else if (registro instanceof Producto) {
                Producto producto = (Producto) registro;
                row.createCell(0).setCellValue(producto.getId());
                row.createCell(1).setCellValue(producto.getCostoDeCompra());
                row.createCell(2).setCellValue(producto.getCantidad());
                row.createCell(3).setCellValue(producto.getTipo());
                row.createCell(4).setCellValue(producto.getCodigoBarra());
                row.createCell(5).setCellValue(producto.getCategoria());
                row.createCell(6).setCellValue(producto.getFormato());
            } else if (registro instanceof Ropa) {
                Ropa ropa = (Ropa) registro;
                row.createCell(0).setCellValue(ropa.getId());
                row.createCell(1).setCellValue(ropa.getNombre());
                row.createCell(2).setCellValue(ropa.getTalla());
                row.createCell(3).setCellValue(ropa.getPrecio());
                row.createCell(4).setCellValue(ropa.getCantidad());
            }

        }


    }


}
