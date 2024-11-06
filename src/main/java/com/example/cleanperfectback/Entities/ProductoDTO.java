package com.example.cleanperfectback.Entities;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductoDTO {
    private Long id;
    private int costoDeCompra;
    private int cantidad;
    private String tipo;
    private Long codigoBarra;
    private String categoria;
    private String formato;

    public ProductoDTO(Long id, int costoDeCompra, int cantidad, String tipo, Long codigoBarra, String categoria, String formato) {
        this.id = id;
        this.costoDeCompra = costoDeCompra;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.codigoBarra = codigoBarra;
        this.categoria = categoria;
        this.formato = formato;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCostoDeCompra() {
        return costoDeCompra;
    }

    public void setCostoDeCompra(int costoDeCompra) {
        this.costoDeCompra = costoDeCompra;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(Long codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }
}
