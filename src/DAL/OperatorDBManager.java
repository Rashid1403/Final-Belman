package DAL;

import BE.Operator;
import BE.Sleeve;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access Layer OperatorDBManager klassen.
 *
 * @author Daniel, Klaus, Mak, Rashid
 */
public class OperatorDBManager
{

    private static final String OPERATORID = "OperatorId";
    private static final String USERNAME = "username";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String SLEEVEID = "id";
    private static final String QUANTITYCUT = "quantityCut";
    private Connector connector;
    private static OperatorDBManager instance;

    public OperatorDBManager() throws IOException
    {
        connector = Connector.getInstance();
    }

    /**
     * Metode som returnerer den eneste instans af klassen.
     *
     * @throws IOException
     */
    public static OperatorDBManager getInstance() throws IOException
    {
        if (instance == null)
        {
            instance = new OperatorDBManager();
        }
        return instance;
    }

    /**
     * Metode som forbinder til databasen, henter alle operatører og gemmer dem
     * i en arrayliste, som den så returnerer.
     *
     * @throws SQLException
     */
    public ArrayList<Operator> getAllOperators() throws SQLException
    {
        try (Connection con = connector.getConnection())
        {
            String sql = "SELECT * FROM Operator";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            ArrayList<Operator> operators = new ArrayList<>();
            while (rs.next())
            {
                int operatorId = rs.getInt("operatorId");
                String username = rs.getString("username");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                int quantityCut = rs.getInt("quantityCut");

                Operator op = new Operator(operatorId, username, firstName, lastName, null, quantityCut);
                operators.add(op);
            }
            return operators;
        }
    }

    /**
     * Metode som forbinder til databasen, henter en operatør med det givne
     * brugernavn og gemmer operatøren i et objekt.
     *
     * @param username
     * @throws SQLServerException
     * @throws SQLException
     */
    public Operator get(String username) throws SQLServerException, SQLException
    {
        try (Connection con = connector.getConnection())
        {
            String sql = "SELECT * FROM Operator, Sleeve WHERE username = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                return getOneOperator(rs);
            }
            return null;
        }
    }

    /**
     * Metode som returnerer et Operator objekt fra et resultset.
     *
     * @param rs
     * @throws SQLException
     */
    private Operator getOneOperator(ResultSet rs) throws SQLException
    {
        int id = rs.getInt(OPERATORID);
        String username = rs.getString(USERNAME);
        String firstName = rs.getString(FIRST_NAME);
        String lastName = rs.getString(LAST_NAME);
        int sleeveId = rs.getInt(SLEEVEID);
        int quantityCut = rs.getInt(QUANTITYCUT);

        return new Operator(id, username, firstName, lastName, new Sleeve(sleeveId, null, null, -1, -1, -1, -1, null), quantityCut);
    }
}
