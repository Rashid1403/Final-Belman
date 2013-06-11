package BLL;

import BE.Order;
import BE.StockItem;
import DAL.StockItemDBManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Buisness Logic Layer StockItemManager klassen.
 *
 * @author Daniel, Klaus, Mak, Rashid
 */
public class StockItemManager extends Observable
{

    private static StockItemDBManager accessor;
    private static StockItemManager instance;

    public StockItemManager() throws IOException
    {
        accessor = StockItemDBManager.getInstance();
    }

    /**
     * Metode som returnerer den eneste instans af klassen.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static StockItemManager getInstance() throws FileNotFoundException, IOException
    {
        if (instance == null)
        {
            instance = new StockItemManager();
        }
        return instance;
    }

    /**
     * Metode som returnerer alle lagervarer.
     *
     * @throws IOException
     * @throws SQLException
     */
    public ArrayList<StockItem> getAll() throws IOException, SQLException
    {
        return accessor.getAllItems();
    }

    /**
     * Metode som returnerer lagervarer i forhold til den valgte ordre.
     *
     * @param o
     * @throws IOException
     * @throws SQLException
     */
    public ArrayList<StockItem> getItemByOrder(Order o) throws IOException, SQLException
    {
        return accessor.getItemByOrder(o);
    }
}
