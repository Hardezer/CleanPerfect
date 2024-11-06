package com.example.cleanperfectback.Controllers;

import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.example.cleanperfectback.Entities.*;
import com.example.cleanperfectback.Services.DetalleSalidaService;
import com.example.cleanperfectback.Services.EmpresaService;
import com.example.cleanperfectback.Services.ProductoService;
import com.example.cleanperfectback.Services.SalidaService;
import com.example.cleanperfectback.Services.RopaService;
import com.example.cleanperfectback.Services.MaquinariaService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private SalidaService salidaService;
    @Autowired
    private DetalleSalidaService detalleSalidaService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private RopaService ropaService;
    @Autowired
    private MaquinariaService maquinariaService;

    @GetMapping("/salidasPorEmpresa")
    public ModelAndView getSalidasPorEmpresa() {
        List<Empresa> empresas = empresaService.getAllEmpresas();
        List<EmpresaSalidasDTO> reporte = empresas.stream().map(empresa -> {
            List<Salida> salidas = salidaService.getSalidasByEmpresaId(empresa.getId());
            List<SalidaDTO> salidasDTO = salidas.stream().map(salida -> {
                List<DetalleSalida> detalles = detalleSalidaService.getDetallesBySalidaId(salida.getId());
                List<DetalleSalidaDTO> detallesDTO = detalles.stream().map(detalle -> {
                    try {
                        String nombreItem = getItemName(detalle.getItemSalida());
                        DetalleSalidaDTO detalleDTO = new DetalleSalidaDTO();
                        detalleDTO.setItemSalidaId(detalle.getItemSalida().getId());
                        detalleDTO.setItemSalidaNombre(nombreItem);
                        detalleDTO.setCantidad(detalle.getCantidad());
                        detalleDTO.setCostoTotal(detalle.getCostoTotal());
                        return detalleDTO;
                    } catch (RuntimeException e) {
                        return new DetalleSalidaDTO();
                    }
                }).collect(Collectors.toList());
                SalidaDTO salidaDTO = new SalidaDTO();
                salidaDTO.setSalidaId(salida.getId());
                salidaDTO.setFecha(salida.getFecha());
                salidaDTO.setCosto(salida.getCosto());
                salidaDTO.setDetallesSalida(detallesDTO);
                return salidaDTO;
            }).collect(Collectors.toList());
            EmpresaSalidasDTO empresaDTO = new EmpresaSalidasDTO();
            empresaDTO.setEmpresaId(empresa.getId());
            empresaDTO.setNombreEmpresa(empresa.getNombre());
            empresaDTO.setEstadoEmpresa(empresa.getEstado());
            empresaDTO.setSalidas(salidasDTO);
            return empresaDTO;
        }).collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("reporte", reporte);
        modelAndView.setViewName("reporte");
        return modelAndView;
    }

    @GetMapping("/salidasSimplificado")
    public ModelAndView getSalidasSimplificado(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        List<Empresa> empresas = empresaService.getAllEmpresas();
        List<EmpresaSalidasDTO> reporte = empresas.stream()
                .map(empresa -> {
                    List<Salida> salidas = salidaService.getSalidasByEmpresaIdAndFechaBetween(empresa.getId(), startDate, endDate);
                    if (salidas.isEmpty()) {
                        return null;
                    }
                    List<SalidaDTO> salidasDTO = salidas.stream().map(salida -> {
                        List<DetalleSalida> detalles = detalleSalidaService.getDetallesBySalidaId(salida.getId());
                        List<DetalleSalidaDTO> detallesDTO = detalles.stream().map(detalle -> {
                            try {
                                String nombreItem = getItemName(detalle.getItemSalida());
                                DetalleSalidaDTO detalleDTO = new DetalleSalidaDTO();
                                detalleDTO.setItemSalidaId(detalle.getItemSalida().getId());
                                detalleDTO.setItemSalidaNombre(nombreItem);
                                detalleDTO.setCantidad(detalle.getCantidad());
                                detalleDTO.setCostoTotal(detalle.getCostoTotal());
                                return detalleDTO;
                            } catch (RuntimeException e) {
                                return new DetalleSalidaDTO();
                            }
                        }).collect(Collectors.toList());
                        SalidaDTO salidaDTO = new SalidaDTO();
                        salidaDTO.setSalidaId(salida.getId());
                        salidaDTO.setFecha(salida.getFecha());
                        salidaDTO.setCosto(salida.getCosto());
                        salidaDTO.setDetallesSalida(detallesDTO);
                        return salidaDTO;
                    }).collect(Collectors.toList());
                    EmpresaSalidasDTO empresaDTO = new EmpresaSalidasDTO();
                    empresaDTO.setEmpresaId(empresa.getId());
                    empresaDTO.setNombreEmpresa(empresa.getNombre());
                    empresaDTO.setEstadoEmpresa(empresa.getEstado());
                    empresaDTO.setSalidas(salidasDTO);
                    double costoTotal = salidasDTO.stream().mapToDouble(SalidaDTO::getCosto).sum();
                    empresaDTO.setCostoTotal(costoTotal);
                    return empresaDTO;
                })
                .filter(empresaDTO -> empresaDTO != null)
                .collect(Collectors.toList());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("reporte", reporte);
        modelAndView.setViewName("reporte_simplificado");
        return modelAndView;
    }

    private String getItemName(ItemSalida itemSalida) {
        if (itemSalida instanceof Producto) {
            Producto producto = (Producto) itemSalida;
            return productoService.getNombreProductoCompleto(producto.getId());
        } else if (itemSalida instanceof Ropa) {
            Ropa ropa = (Ropa) itemSalida;
            return ropa.getNombre();
        } else if (itemSalida instanceof Maquinaria) {
            Maquinaria maquinaria = (Maquinaria) itemSalida;
            return maquinaria.getNombre();
        } else {
            throw new IllegalArgumentException("Tipo de item desconocido");
        }
    }

    // Método para convertir la imagen a base64
    private String encodeImageToBase64(String imagePath) {
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/descargar-reporte")
    public void downloadPDF(@RequestParam("startDate") String startDate,
                            @RequestParam("endDate") String endDate,
                            HttpServletResponse response) throws IOException {

        List<Empresa> empresas = empresaService.getAllEmpresas();
        List<EmpresaSalidasDTO> reporte = empresas.stream()
                .map(empresa -> {
                    List<Salida> salidas = salidaService.getSalidasByEmpresaIdAndFechaBetween(empresa.getId(), startDate, endDate);
                    if (salidas.isEmpty()) {
                        return null;
                    }
                    List<SalidaDTO> salidasDTO = salidas.stream().map(salida -> {
                        List<DetalleSalida> detalles = detalleSalidaService.getDetallesBySalidaId(salida.getId());
                        List<DetalleSalidaDTO> detallesDTO = detalles.stream().map(detalle -> {
                            try {
                                String nombreItem = getItemName(detalle.getItemSalida());
                                DetalleSalidaDTO detalleDTO = new DetalleSalidaDTO();
                                detalleDTO.setItemSalidaId(detalle.getItemSalida().getId());
                                detalleDTO.setItemSalidaNombre(nombreItem);
                                detalleDTO.setCantidad(detalle.getCantidad());
                                detalleDTO.setCostoTotal(detalle.getCostoTotal());
                                return detalleDTO;
                            } catch (RuntimeException e) {
                                return new DetalleSalidaDTO();
                            }
                        }).collect(Collectors.toList());
                        SalidaDTO salidaDTO = new SalidaDTO();
                        salidaDTO.setSalidaId(salida.getId());
                        salidaDTO.setFecha(salida.getFecha());
                        salidaDTO.setCosto(salida.getCosto());
                        salidaDTO.setDetallesSalida(detallesDTO);
                        return salidaDTO;
                    }).collect(Collectors.toList());
                    EmpresaSalidasDTO empresaDTO = new EmpresaSalidasDTO();
                    empresaDTO.setEmpresaId(empresa.getId());
                    empresaDTO.setNombreEmpresa(empresa.getNombre());
                    empresaDTO.setEstadoEmpresa(empresa.getEstado());
                    empresaDTO.setSalidas(salidasDTO);
                    double costoTotal = salidasDTO.stream().mapToDouble(SalidaDTO::getCosto).sum();
                    empresaDTO.setCostoTotal(costoTotal);
                    return empresaDTO;
                })
                .filter(empresaDTO -> empresaDTO != null)
                .collect(Collectors.toList());

        // Generar el HTML para el reporte simplificado con las fechas
        String html = generateHtmlForSimplifiedReport(reporte, startDate, endDate);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte-simplificado.pdf");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(baos);

        ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
        out.close();
    }

    private String generateHtmlForSimplifiedReport(List<EmpresaSalidasDTO> empresas, String startDate, String endDate) {
        String logoBase64 = encodeImageToBase64("src/main/resources/static/css/Logo_cleanperfect.png");
        String logoImage = "data:image/png;base64," + logoBase64;

        // Genera el HTML basado en la lista de empresas filtradas
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta charset=\"UTF-8\"/><title>Reporte de Salidas</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #ffffff; color: #333; margin: 0; padding: 20px; }");
        html.append(".container { width: 80%; margin: 0 auto; background: white; padding: 20px; border: 1px solid #ddd; border-radius: 10px; position: relative; }");
        html.append(".logo-container { width: 100%; text-align: left; padding: 20px; box-sizing: border-box; }"); // Contenedor para el logo
        html.append(".logo { width: 150px; }"); // Estilo para el logo
        html.append(".header { text-align: center; padding: 10px 0; border-bottom: 1px solid #ddd; margin-bottom: 20px; position: relative; }");
        html.append(".header h1 { margin: 0; font-size: 20px; color: #333; }"); // Reduced the font size to 20px
        html.append(".header h2 { margin: 0; font-size: 16px; color: #333; margin-top: 10px; }"); // Added subtitle for date range
        html.append(".empresa-header { background-color: #007bff; color: white; padding: 15px; margin-top: 10px; border: 1px solid #ddd; border-radius: 5px; }");
        html.append(".empresa-header h2 { margin: 0; font-size: 18px; color: #fff; }"); // Set header text color to white
        html.append(".salida-detail { padding: 10px 15px; border-bottom: 1px solid #ddd; margin-bottom: 10px; }");
        html.append(".salida-detail span { display: block; font-size: 14px; }");
        html.append(".activo { color: #32CD32; }"); // Light green color for "Activo"
        html.append(".inactivo { color: #FF6347; }"); // Light red color for "Inactivo"
        html.append("</style>");
        html.append("</head><body>");
        html.append("<div class=\"logo-container\"><img src=\"").append(logoImage).append("\" class=\"logo\" alt=\"CleanPerfect Logo\"/></div>");
        html.append("<div class=\"container\"><div class=\"header\"><h1>Reporte de Salidas</h1>");
        html.append("<h2>Rango de fechas: ").append(startDate).append(" a ").append(endDate).append("</h2>"); // Add date range to header
        html.append("</div>");

        empresas.forEach(empresa -> {
            html.append("<div class=\"empresa-header\"><h2>");
            html.append(empresa.getNombreEmpresa());
            html.append(" (<span class=\"").append(empresa.getEstadoEmpresa().equals("Activo") ? "activo" : "inactivo").append("\">");
            html.append(empresa.getEstadoEmpresa()).append("</span>), ");
            html.append("Total Costo de Salidas: ").append(formatCurrency(empresa.getCostoTotal())).append("</h2></div><div>");

            empresa.getSalidas().forEach(salida -> {
                html.append("<div class=\"salida-detail\">");
                html.append("<span>Fecha: ").append(salida.getFecha()).append(" - Costo Total de Salida: ").append(formatCurrency(salida.getCosto())).append("</span>");
                html.append("</div>");
            });

            html.append("</div>");
        });

        html.append("</div></body></html>");
        return html.toString();
    }

    private String formatCurrency(double amount) {
        Locale chile = new Locale("es", "CL");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(chile);
        return currencyFormatter.format(amount);
    }
}
