package BLL;

import BE.Operator;
import BE.Sleeve;
import DAL.SleeveDBManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Observable;

/**
 * Buisness Logic Layer SleeveLogManager klassen.
 *
 * @author Daniel, Klaus, Mak, Rashid
 */
public class SleeveLogManager extends Observable
{

    private SleeveDBManager accessor;
    private static SleeveLogManager instance;

    private SleeveLogManager() throws IOException
    {
        accessor = SleeveDBManager.getInstance();
    }

    /**
     * Metode som returnerer den eneste instans af klassen.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static SleeveLogManager getInstance() throws FileNotFoundException, IOException
    {
        if (instance == null)
        {
            instance = new SleeveLogManager();
        }
        return instance;
    }

    /**
     * Metode som tilføjer et objekt af sleevelog med de givne informationer.
     *
     * @param id
     * @param op
     * @param hasCut
     * @param timeSpent
     * @throws SQLException
     */
    public void addLog(int id, Operator op, int hasCut, int timeSpent) throws SQLException
    {
        accessor.addLog(id, op, hasCut, timeSpent);
    }

    /**
     * Metode som returnerer mængden af sleeves skåret af den givne operatør.
     *
     * @param s
     * @param opid
     * @throws SQLException
     */
    public int getQuantity(Sleeve s, int opid) throws SQLException
    {
        return accessor.getQuantity(s, opid);
    }
}
