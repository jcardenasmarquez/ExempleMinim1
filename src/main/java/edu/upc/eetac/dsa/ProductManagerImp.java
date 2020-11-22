package edu.upc.eetac.dsa;

import edu.upc.eetac.dsa.models.Pedido;
import edu.upc.eetac.dsa.models.Producto;
import edu.upc.eetac.dsa.models.ProductoCantidad;
import edu.upc.eetac.dsa.models.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import java.util.*;

public class ProductManagerImp implements ProductManager {

    //--Singleton: Referencia privada estatica a la unica instancia de la clase--//
    private static ProductManagerImp instancia;

    private static Logger log = LogManager.getLogger(ProductManagerImp.class);

    //---Estructuras de datos (atributos) del gestor---//
    private HashMap<String, User> tablaUsuarios = null;
    private Queue<Pedido> colaPedidos;
    private List<Producto> listaProductos;

    //----------Singleton: constructor privado----------//
    private ProductManagerImp(){
        this.tablaUsuarios = new HashMap<String,User>();
        this.colaPedidos = new LinkedList<Pedido>();
        this.listaProductos = new LinkedList<Producto>();
    }

    //---Singleton: metodo de acceso estático que devuelve una referencia a la (unica) instancia---//
    public static ProductManagerImp getInstance(){
        if (instancia == null) instancia = new ProductManagerImp();
        return instancia;
    }

    //---------Métodos no estáticos del gestor-----------
    public List<Producto> getProductsSortedAlphabetical() {
        return null;
    }

    public List<Producto> getProductsSortedByPrice() {
        if(this.listaProductos != null){
            List<Producto> resultat = this.listaProductos;
            resultat.sort(new Comparator<Producto>() {
                public int compare(Producto o1, Producto o2) {
                    return Double.compare(o1.getPrecio(),o2.getPrecio());
                }
            });
            log.info("Lista de productos ordenada por precio (ascendente):  ");
            log.info(logListaProductos((LinkedList<Producto>) resultat));

            return resultat; //200 OK PETITION

        }
        else {
            log.error("Lista productos vacía"); //404(Empty list)
            return null;
        }
    }

    public int takeOrder(Pedido p) {
        colaPedidos.add(p);

        log.info("Nuevo pedido añadido: ");
        log.info(logPedido(p));

        log.info("Cola actual:");
        log.info(logColaPedidos((LinkedList<Pedido>) this.colaPedidos));
        return 201; //OK ADDED
    }

    public int serveOrder() {
        if (checkPedidos()){
            Pedido pservido = this.colaPedidos.poll();
            procesarPedido(pservido);
            log.info("Pedido servido: ");
            log.info(logPedido(pservido));

            log.info("Cola actual:  ");
            log.info(logColaPedidos((LinkedList<Pedido>) this.colaPedidos));
            return 201; //OK served
        }
        else {
            log.error("Error --> Cola vacía");
            return 400; //Bad request
        }
    }


    public List<Pedido> getUserOrders(String idUser) throws NoUserException, NoPedidosException {
        if(tablaUsuarios.get(idUser) == null){
            log.error("Error. No se ha encontrado al usuario.");
            throw new NoUserException();
        }
        else
        if(tablaUsuarios.get(idUser).getOrdersHistory() == null) {
            log.error("Error. Cola de pedidos del usuario vacía.");
            throw new NoPedidosException();
        }
        else {
            log.info("Historial de pedidos del usuario " + tablaUsuarios.get(idUser).getNombre() + " :");
            log.info(logHistorialPedidos(tablaUsuarios.get(idUser)));
            return this.tablaUsuarios.get(idUser).getOrdersHistory();
        }
    }

    public List<Producto> getProductsSortedSale() throws NoProductosException {
        //ORDEN DESCENDENTE
        if(checkProductos()){
            log.info("Lista de productos normal: ");
            log.info(logListaProductos((LinkedList<Producto>) this.listaProductos));

            List<Producto> res = this.listaProductos;
            res.sort(new Comparator<Producto>() {
                @Override
                public int compare(Producto p1, Producto p2) {
                    return Integer.compare(p2.getNumVentas(), p1.getNumVentas());
                }
            });

            log.info("Lista de productos ordenada por ventas, orden descendente: ");
            log.info(logListaProductos((LinkedList<Producto>)res));

            return (LinkedList<Producto>) res;
        }
        else{
            log.error("Error. Lista de productos vacía.");
            throw new NoProductosException();
        }
    }

    //Funciones para modificar los atributos----------

    public int addUser(String id, String nom) {
        User u = new User(id, nom);
        try{
            this.tablaUsuarios.put(id,u);
            log.info("Usuario añadido: "+ u.getNombre());
            return 201; //Ok created
        }
        catch (IndexOutOfBoundsException e){
            log.error("Error tabla usuarios");
            return 507; //Insufficeint storage
        }
        catch (IllegalArgumentException e){
            log.error("Incorrect format exception");
            return 400; //Bad request
        }
    }

    public HashMap<String, User> getUsers(){
        return this.tablaUsuarios;
    }

    public void addProduct(String nom, double preu) {
        Producto p = new Producto(nom, preu);
        try {
            this.listaProductos.add(p);
            log.info("Producto añadido  " + p.getIdProducto());
        }
        catch (IndexOutOfBoundsException e){
            log.error("Error");
        }
    }

    public Producto getProduct(String idProducto) {
        for(Producto p: this.listaProductos){
            if(p.getIdProducto().equals(idProducto)) return p; //Busqueda
        }
        return null;
    }

    public List<Producto> getProducts() {
        return this.listaProductos;
    }

    public void liberarRecursos() {
        this.tablaUsuarios.clear();
        this.listaProductos.clear();
        this.colaPedidos.clear();
    }

    //----------Funciones auxiliares------------

    private boolean checkPedidos(){ //True si hay pedidos
        return this.colaPedidos.size() != 0;
    }

    private boolean checkProductos(){ //TRUE SI HAY PRODUCTOS (NO ESTÁ VACÍA)
        return this.listaProductos.size() != 0;
    }

    private void procesarPedido(Pedido p){
        String idUser = p.getIdUser();
        User user = this.tablaUsuarios.get(idUser);
        user.registerOrder(p);

        for (int i = 0; i<p.getListaSize(); i++){
            ProductoCantidad dupla = p.getSomethingFromLista(i);
            String idprod = dupla.getIdProducto();
            int num = dupla.getNumeroProd();

            //Buscar producto
            for (Producto product: this.listaProductos){
                if (product.getIdProducto().equals(idprod))
                    product.incrementarVentas(num);
            }
        }
    }


    //---------Funciones auxiliares para el log---------
    private String logListaProductos (LinkedList<Producto> lista){
        String res = "";
        for (Producto pr : lista){
            res = res + "Producto: " + pr.getIdProducto() + " | Numero de ventas: " + pr.getNumVentas() + " | Precio: " + pr.getPrecio() + " // ";
        }
        return res;
    }

    private String logPedido(Pedido p) {
        String res = "Usuario: " + this.tablaUsuarios.get(p.getIdUser()).getNombre() + " //";
        for (int i = 0; i < p.getListaSize(); i++) {
            res = res + "Producto: " + p.getSomethingFromLista(i).getIdProducto() + ", Cantidad: " + p.getSomethingFromLista(i).getNumeroProd() + " ||";
        }
        return res;
    }

    private String logColaPedidos(LinkedList<Pedido> cola){
        String res = "";
        for (Pedido pe : cola){
            res = res + logPedido(pe) + "---|| ";
        }
        return res;
    }

    private String logHistorialPedidos(User u){
        String res = "";
        LinkedList<Pedido> lista = u.getOrdersHistory();
        for(int i = 0; i < lista.size(); i++){
            res = res + "Pedido " + i + " :";
            for (int j = 0; j < lista.get(i).getListaSize() ; j++){
                res = res + " Producto: " + lista.get(i).getSomethingFromLista(j).getIdProducto() + " , Cantidad: " + lista.get(i).getSomethingFromLista(j).getNumeroProd() + " ||";
            }
            res = res + "---|| ";
        }
        return res;
    }

}
