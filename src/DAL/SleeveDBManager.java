package DAL;

import BE.Material;
import BE.Operator;
import BE.Order;
import BE.Sleeve;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Data Access Layer SleeveDBManager klassen.
 *
 * @author Daniel, Klaus, Mak, Rashid
 */
public class SleeveDBManager
{

    private static final String ID = "id";
    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";
    private static final String THICKNESS = "thickness";
    private static final String CIRCUMFERENCE = "circumference";
    private static final String MATERIAL_ID = "materialId";
    private static final String P_ORDER_ID = "pOrderId";
    private static final String NAME = "name";
    private Connector connector;
    private static SleeveDBManager instance;

    public SleeveDBManager() throws IOException
    {
        connector = Connector.getInstance();
    }

    /**
     * Metode som returnerer den eneste instans af klassen.
     *
     * @throws IOException
     */
    public static SleeveDBManager getInstance() throws IOException
    {
        if (instance == null)
        {
            instance = new SleeveDBManager();
        }
        return instance;
    }

    /**
     * Metode som forbinder til databasen, henter alle sleeves i forhold til det
     * valgte ordre og gemmer dem i en arrayliste.
     *
     * @param o
     * @throws SQLException
     */
    public ArrayList<Sleeve> getSleevesByOrder(Order o) throws SQLException
    {
        try (Connection con = connector.getConnection())
        {
            String sql = "SELECT Sleeve.*, Material.Name FROM Sleeve, Material, ProductionOrder WHERE Sleeve.materialId = Material.id AND Sleeve.pOrderId = ProductionOrder.pOrderId AND ProductionOrder.pOrder = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, o.getOrderName());
            ResultSet rs = ps.executeQuery();

            ArrayList<Sleeve> sleeves = new ArrayList<>();
            while (rs.next())
            {
                sleeves.add(getOneSleeve(rs));
            }
            return sleeves;
        }
    }

    /**
     * Metode som returnerer et sleeve objekt fra et resultset.
     *
     * @param rs
     * @throws SQLException
     */
    public Sleeve getOneSleeve(ResultSet rs) throws SQLException
    {
        int id = rs.getInt(ID);
        GregorianCalendar gc = null;
        Date date = rs.getTimestamp(START_TIME);
        if (date != null)
        {
            gc = new GregorianCalendar();
            gc.setTime(date);
        }
        GregorianCalendar gc2 = null;
        date = rs.getTimestamp(END_TIME);
        if (date != null)
        {
            gc2 = new GregorianCalendar();
            gc2.setTime(date);
        }
        double thickness = rs.getDouble(THICKNESS);
        double circumference = rs.getDouble(CIRCUMFERENCE);
        int materialId = rs.getInt(MATERIAL_ID);
        int pOrderId = rs.getInt(P_ORDER_ID);
        String materialName = rs.getString(NAME);

        return new Sleeve(id, gc, gc2, thickness, circumference, materialId, pOrderId, new Material(materialName));
    }

    /**
     * Metode som forbinder til databasen og opdaterer start tiden på det valgte
     * sleeve.
     *
     * @param s
     * @throws SQLException
     */
    public void updateSleeveStartTim(Sleeve s) throws SQLException
    {
        try (Connection con = connector.getConnection())
        {
            String sql = "UPDATE Sleeve SET startTime = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            Date startDate = new Date();
            Timestamp startTime = new Timestamp(startDate.getTime());
            ps.setTimestamp(1, startTime);
            ps.setInt(2, s.getId());

            int affectedrows = ps.executeUpdate();
            if (affectedrows == 0)
            {
                throw new SQLException("Unable to update sleeve start time");
            }
        }
    }

    /**
     * Metode som forbinder til databasen og opdaterer slut tiden på det valgte
     * sleeve.
     *
     * @param s
     * @throws SQLException
     */
    public void updateSleeveEndTime(Sleeve s) throws SQLException
    {
        try (Connection con = connector.getConnection())
        {
            String sql = "UPDATE Sleeve SET endTime = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            Date endDate = new Date();
            Timestamp endTime = new Timestamp(endDate.getTime());
            ps.setTimestamp(1, endTime);
            ps.setInt(2, s.getId());

            int affectedrows = ps.executeUpdate();
            if (affectedrows == 0)
            {
                throw new SQLException("Unable to update sleeve end time");
            }
        }
    }

    /**
     * Metode som forbinder til databasen og tilføjer en række informationer til
     * sleevelog tabellen.
     *
     * @param id
     * @param op
     * @param hasCut
     * @param timeSpent
     * @throws SQLException
     */
    public void addLog(int id, Operator op, int hasCut, int timeSpent) throws SQLException
    {
        try (Connection con = connector.getConnection())
        {
            String sql = "INSERT INTO SleeveLog (sleeveId, quantity, operatorId, timeSpent) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setInt(2, hasCut);
            ps.setInt(3, op.getId());
            ps.setInt(4, timeSpent);

            int affectedRows = ps.executeUpdate();
            if (affectedRows <= 0)
            {
                throw new SQLException();
            }
        }
    }

    /**
     * Metode som forbinder til databasen, henter mængden af sleeves skåret ud
     * fra den givne sleeve og operatør id.
     *
     * @param s
     * @param opid
     * @throws SQLException
     */
    public int getQuantity(Sleeve s, int opid) throws SQLException
    {
        try (Connection con = connector.getConnection())
        {
            String sql = "SELECT * FROM SleeveLog WHERE sleeveid = ? AND operatorId = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, s.getId());
            ps.setInt(2, opid);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                int quantity = rs.getInt("quantity");
                return quantity;
            }
            return 0;
        }
    }
}
