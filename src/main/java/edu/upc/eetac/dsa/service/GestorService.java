package edu.upc.eetac.dsa.service;

import edu.upc.eetac.dsa.*;

import edu.upc.eetac.dsa.models.Pedido;
import edu.upc.eetac.dsa.models.Producto;
import edu.upc.eetac.dsa.models.ProductoCantidad;

import edu.upc.eetac.dsa.models.User;
import io.swagger.annotations.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Api(value = "/gestor", description = "Endpoint to Gestor Service")
@Path("/gestor")
public class GestorService {
    static final Logger logger = Logger.getLogger(GestorService.class);
    private ProductManager gestor;

    public GestorService() throws NoPedidosException{
        //Configuring Log4j, location of the log4j.properties file and must always be inside the src folder
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        this.gestor = ProductManagerImp.getInstance();
        if(gestor.getUsers().size() == 0){
            //SET UP de leas estructuras
            gestor.addUser("1312", "Juan");

            gestor.addProduct("VollDam", 2);
            gestor.addProduct("Estrella", 1.8);
            gestor.addProduct("Mahou", 1.75);


            Pedido p = new Pedido("1312");
            p.addDupla(3,"VollDam");
            p.addDupla(1,"Estrella");
            p.addDupla(5, "Mahou");

            gestor.takeOrder(p);
            gestor.serveOrder();
        }
    }
    @GET
    @ApiOperation(value = "Listado de usuarios", notes = "Lista")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Producto.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "No users")
    })
    @Path("/listUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() throws NoProductosException{

        List<User> usuarios = (List<User>) this.gestor.getUsers();
        GenericEntity<List<User>> entity = new GenericEntity<List<User>>(usuarios){};

        if(this.gestor.getUsers() != null)
            return  Response.status(201).entity(entity).build();
        else
            return Response.status(404).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "Listado de productos ordenados por precio ascendente", notes = "Lista")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Producto.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Lista de productos vacía")
    })
    @Path("/listProducts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSortedProd() throws NoProductosException{

        List<Producto> prods = this.gestor.getProductsSortedByPrice();
        GenericEntity<List<Producto>> entity = new GenericEntity<List<Producto>>(prods){};

        if(this.gestor.getProducts() != null)
            return  Response.status(201).entity(entity).build();
        else
            return Response.status(404).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "Listado (Historial) de Pedidos de un Usuario dado", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Pedido.class, responseContainer="List"),
            @ApiResponse(code = 404, message= "Usuario no encontrado. No existe o la tabla de usuarios está vacía.")
    })
    @Path("/getPedidosUsuario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPedidosUsr(@PathParam("id") String id) throws NoPedidosException, NoUserException {

        List<Pedido> pedidos = this.gestor.getUserOrders(id);
        GenericEntity<List<Pedido>> entity = new GenericEntity<List<Pedido>>(pedidos) {};

        if(this.gestor.getUsers() != null)
            return Response.status(201).entity(entity).build();
        else
            return Response.status(404).entity(entity).build();
    }
}
