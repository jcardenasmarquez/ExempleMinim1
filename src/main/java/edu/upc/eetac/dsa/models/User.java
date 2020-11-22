package edu.upc.eetac.dsa.models;

import java.util.LinkedList;
import java.util.List;

public class User {

    private  String idUsuario;
    private String nombre;
    private List<Pedido> pedidos; //Histórico pedidos

    public User(String id, String nom){
        this.idUsuario = id;
        this.nombre = nom;
        pedidos = new LinkedList<Pedido>();
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void registerOrder(Pedido p){
        pedidos.add(p);
    }

    public LinkedList<Pedido> getOrdersHistory(){
        return (LinkedList<Pedido>) this.pedidos;
    }
}
