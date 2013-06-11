package BLL;

import BE.Operator;
import DAL.OperatorDBManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Buisness Logic Layer OperatorManager klassen.
 *
 * @author Daniel, Klaus, Mak, Rashid
 */
public class OperatorManager extends Observable
{

    private OperatorDBManager accessor;
    private static OperatorManager instance;

    public OperatorManager() throws IOException
    {
        accessor = OperatorDBManager.getInstance();
    }

    /**
     * Metode til at returnere den eneste instans af denne klasse.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static OperatorManager getInstance() throws FileNotFoundException, IOException
    {
        if (instance == null)
        {
            instance = new OperatorManager();
        }
        return instance;
    }

    /**
     * Metode som returnere alle operatorere.
     *
     * @throws SQLException
     */
    public ArrayList<Operator> getAllOperators() throws SQLException
    {
        return accessor.getAllOperators();
    }

    /**
     * Metode som returnere en operator med det givne brugernavn.
     *
     * @param username
     * @return
     * @throws SQLException
     */
    public Operator get(String username) throws SQLException
    {
        return accessor.get(username);
    }
}
