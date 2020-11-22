package edu.upc.eetac.dsa;

//Imports
import edu.upc.eetac.dsa.models.Pedido;
import edu.upc.eetac.dsa.models.Producto;
import edu.upc.eetac.dsa.models.User;

import java.util.HashMap;
import java.util.List;

public interface ProductManager {
    //Listado de productos ordenado (ascendentemente) por precio
    List<Producto> getProductsSortedByPrice();
    //Realizar un pedido por parte de un usuario identificado
    int takeOrder(Pedido p);
    //Servir un pedido
    int serveOrder(); //Se realizan en orden de llegadas
    //Listado de pedidos de un usuario que ya hayan sido realizados
    List<Pedido> getUserOrders(String idUser) throws NoUserException, NoPedidosException;
    //Listado de productos ordenado (descendentemente) por número de ventas
    List<Producto> getProductsSortedSale() throws NoProductosException;

    //Extras que no dice el enuncicado explicitamente pero necesitaremos
    public int addUser(String id, String nom);
    public HashMap<String, User> getUsers();
    public void addProduct(String nom, double preu);
    Producto getProduct(String idProducto);
    List<Producto> getProducts();

    public void liberarRecursos();
}
