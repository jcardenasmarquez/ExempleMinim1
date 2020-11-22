package edu.upc.eetac.dsa.models;

public class ProductoCantidad {
    private int numeroProd;
    private String idProducto;

    public ProductoCantidad(int n, String id){
        this.numeroProd = n;
        this.idProducto = id;
    }

    public int getNumeroProd(){
        return this.numeroProd;
    }
    public String getIdProducto(){
        return this.idProducto;
    }
}
