package BLL;

import BE.Order;
import BE.Sleeve;
import DAL.SleeveDBManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Buisness Logic Layer SleeveManager klassen.
 *
 * @author Daniel, Klaus, Mak, Rashid
 */
public class SleeveManager extends Observable
{

    private SleeveDBManager accessor;
    private static SleeveManager instance;

    private SleeveManager() throws IOException
    {
        accessor = SleeveDBManager.getInstance();
    }

    /**
     * Metode som returnerer det eneste instans af klassen.
     *
     * @throws IOException
     */
    public static SleeveManager getInstance() throws IOException
    {
        if (instance == null)
        {
            instance = new SleeveManager();
        }
        return instance;
    }

    /**
     * Metode som returnerer sleeves i forhold til den valgte ordre.
     *
     * @param order
     * @throws IOException
     * @throws SQLException
     */
    public ArrayList<Sleeve> getSleevesByOrder(Order order) throws IOException, SQLException
    {
        return accessor.getSleevesByOrder(order);
    }

    /**
     * Metode som opdaterer start tiden på det valgte sleeve.
     *
     * @param sleeve
     * @throws SQLException
     */
    public void updateSleeveStartTime(Sleeve sleeve) throws SQLException
    {
        accessor.updateSleeveStartTim(sleeve);
    }

    /**
     * Metode som opdaterer slut tiden på det valgte sleeve.
     *
     * @param sleeve
     * @throws SQLException
     */
    public void updateSleeveEndTime(Sleeve sleeve) throws SQLException
    {
        accessor.updateSleeveEndTime(sleeve);
    }
}
