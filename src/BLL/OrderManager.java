package BLL;

import BE.Order;
import BE.StockItem;
import DAL.ProductionOrderDBManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Buisness Logic Layer OrderManager klassen.
 *
 * @author Daniel, Klaus, Mak, Rashid
 */
public class OrderManager extends Observable
{

    private ProductionOrderDBManager accessor;
    private static OrderManager instance;

    private OrderManager() throws IOException
    {
        accessor = ProductionOrderDBManager.getInstance();
    }

    /**
     * Metode som henter den eneste instans af klassen.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static OrderManager getInstance() throws FileNotFoundException, IOException
    {
        if (instance == null)
        {
            instance = new OrderManager();
        }
        return instance;
    }

    /**
     * Metode som henter alle pausede ordre.
     *
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public ArrayList<Order> getPaused() throws IOException, SQLException
    {
        return accessor.getPaused();
    }

    /**
     * Metode som opdaterer den givne ordre.
     *
     * @param order
     * @throws SQLException
     */
    public void update(Order order) throws SQLException
    {
        try
        {
            accessor.update(order);
            setChanged();
            notifyObservers();
        }
        catch (SQLException e)
        {
        }
    }

    /**
     * Metode som returnere alle ordre.
     *
     * @throws IOException
     * @throws SQLException
     */
    public ArrayList<Order> getAll() throws IOException, SQLException
    {
        return accessor.getAll();
    }

    /**
     * Metode som opdaterer status på det givne ordre.
     *
     * @param o
     * @throws SQLException
     */
    public void updateStatus(Order o) throws SQLException
    {
        accessor.updateStatus(o);
        setChanged();
        notifyObservers();
    }

    /**
     * Metode som opdatarer fejlbeskeden til den givne ordre.
     *
     * @param o
     * @param message
     * @throws SQLException
     */
    public void updateErrorMessage(Order o, String message) throws SQLException
    {
        accessor.updateErrorMessage(o, message);
        setChanged();
        notifyObservers();
    }

    /**
     * Metode som returnere ordre i forhold til den valgte lagervare.
     *
     * @param s
     * @throws IOException
     * @throws SQLException
     */
    public ArrayList<Order> getOrderByStock(StockItem s) throws IOException, SQLException
    {
        return accessor.getOrderByStock(s);
    }
}
