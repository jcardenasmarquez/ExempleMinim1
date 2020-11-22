package edu.upc.eetac.dsa.models;

import java.util.LinkedList;
import java.util.List;

public class Pedido {
    private String idUser;
    private List<ProductoCantidad> lista = null;

    public Pedido(String id){
        this.idUser = id;
        this.lista = new LinkedList<ProductoCantidad>();
    }

    public String getIdUser(){
        return this.idUser;
    }

    public void addDupla(int n, String idProducto){
        ProductoCantidad  dupla = new ProductoCantidad(n, idProducto);
        this.lista.add(dupla);
    }

    public ProductoCantidad getSomethingFromLista(int i){
        return this.lista.get(i);
    }

    public int getListaSize(){
        return this.lista.size();
    }
}
