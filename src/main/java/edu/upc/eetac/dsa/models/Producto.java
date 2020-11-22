package edu.upc.eetac.dsa.models;

public class Producto {

    private String idProducto;
    private int numVentas;
    private double precio;

    public Producto(String id, double price){
        this.idProducto = id;
        this.precio = price;
        this.numVentas = 0; //Al crear un producto inicialmente tenemos 0 ventas
    }

    /*public Producto(){
        //Emptu constructor
    } */

    public String getIdProducto() {
        return idProducto;
    }

    public int getNumVentas() {
        return numVentas;
    }

    public double getPrecio() {
        return precio;
    }

    public void incrementarVentas(int n){
        this.numVentas = numVentas + n;
    }
}
