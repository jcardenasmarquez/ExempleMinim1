import edu.upc.eetac.dsa.ProductManagerImp;
import edu.upc.eetac.dsa.models.Pedido;

import edu.upc.eetac.dsa.models.User;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TestPedidos {
    // THE QUICK REMINDER: Remember to name the test class public smh
    //Log4j Logger initialization
    private static Logger logger = Logger.getLogger(ProductManagerImp.class);

    //Product Manager
    public ProductManagerImp gestor = null;

    //Estructura de datos
    public Pedido p = null;

    @Before
    public void setUp () {
        //Configuring Log4j
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        logger.debug("Debug Test Message!");
        logger.info("Info Test Message!");
        logger.warn("Warning Test Message!");
        logger.error("Error Test Message!");

        //Instancing productmanager implementation
        gestor = ProductManagerImp.getInstance();

        gestor.addUser("1312","Juan");

        gestor.addProduct("VollDam",2.0);
        gestor.addProduct("Estrella",1.8);
        gestor.addProduct("SanMiguel",1.7);
        gestor.addProduct("Heineken",1.9);

        p = new Pedido("1312");
        p.addDupla(30,"VollDam");
        p.addDupla(10,"Estrella");

        gestor.takeOrder(p);

    }

    @After
    public void tearDown(){
        gestor.liberarRecursos();
    }

    @Test
    public void takeOrderTest(){
        //Comprobar que se ha hecho bien el pedido del setUp
        Assert.assertEquals("1312",p.getIdUser());
        Assert.assertEquals("Estrella",p.getSomethingFromLista(1).getIdProducto());
        Assert.assertEquals(2,p.getListaSize());

        //Comprobar que se han introducido bien los datos en las estructuras del gestor
        Assert.assertEquals("Juan",gestor.getUsers().get("1312").getNombre());
        Assert.assertEquals("SanMiguel", gestor.getProducts().get(2).getIdProducto());
        Assert.assertEquals(1.8,1.8, gestor.getProducts().get(1).getPrecio());
    }

    @Test
    public void serveOrderTest(){
        gestor.serveOrder();
        Assert.assertEquals(30, gestor.getProduct("VollDam").getNumVentas());
    }

}
