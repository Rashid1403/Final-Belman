package DAL;

import BE.CoilType;
import BE.Material;
import BE.Order;
import BE.Sleeve;
import BE.StockItem;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access Layer StockItemDBManager klassen.
 * @author Daniel, Klaus, Mak, Rashid
 */

public class StockItemDBManager
{
    private static final String ID = "id";
    private static final String CHARGE_NO = "chargeNo";
    private static final String LENGTH = "length";
    private static final String STOCK_QUANTITY = "stockQuantity";
    private static final String COIL_TYPE_ID = "coilTypeId";
    private static final String SLEEVE_ID = "sleeveId";
    private static final String CODE = "code";
    private static final String WIDTH = "width";
    private static final String THICKNESS = "thickness";
    private static final String DENSITY = "density";
    private static final String NAME = "name";
    private static final String MATERIAL_ID = "materialId";    
    
    private Connector connector;
    private static StockItemDBManager instance;

    public StockItemDBManager() throws IOException
    {
        connector = Connector.getInstance();
    }

    /**
     * Metode som returner den eneste instans af klassen.
     * @throws IOException
     */
    public static StockItemDBManager getInstance() throws IOException
    {
        if (instance == null)
        {
            instance = new StockItemDBManager();
        }
        return instance;
    }
       
    /**
     * Metode som forbinder til databasen, henter alle lagervare og gemmer dem
     * i en arrayliste.
     * @throws SQLException
     * @throws IOException
     */
    public ArrayList<StockItem> getAllItems() throws SQLException, IOException
    {
        try (Connection con = connector.getConnection())
        {
            String sql = "SELECT * FROM StockItem, CoilType, Material WHERE Coiltype.MaterialId = Material.Id AND CoilType.Id = StockItem.coilTypeId ORDER BY CoilType.thickness";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            ArrayList<StockItem> items = new ArrayList<>();
            while (rs.next())
            {
                items.add(getOneItem(rs));
            }
            return items;
        }
    }           
    
    /**
     * Metode som forbinder til databasen, henter alle lagervarer i forhold til
     * den valgte ordre og gemmer dem i en arrayliste.
     * @param o
     * @throws SQLServerException
     * @throws SQLException
     * @throws IOException
     */
    public ArrayList<StockItem> getItemByOrder(Order o) throws SQLServerException, SQLException, IOException
    {
        try (Connection con = connector.getConnection())
        {
            String sql = "SELECT * FROM StockItem, CoilType, Material, Sleeve, ProductionOrder WHERE ProductionOrder.pOrderId = Sleeve.pOrderId AND Sleeve.materialId = Material.id AND Material.id = CoilType.materialId AND CoilType.id = StockItem.coilTypeId AND Sleeve.thickness = CoilType.thickness AND ProductionOrder.pOrder = ? ORDER BY CoilType.materialId, CoilType.thickness";        
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, o.getOrderName());

            ResultSet rs = ps.executeQuery();

            ArrayList<StockItem> items = new ArrayList<>();
            while (rs.next())
            {
                items.add(getOneItem(rs));
            }
            return items;
        }
    }

    /**
     * Metode som returnerer et lagervare objekt fra et resultset.
     * @param rs
     * @throws SQLException
     * @throws IOException
     */
    protected StockItem getOneItem(ResultSet rs) throws SQLException, IOException
    {
        int id = rs.getInt(ID);
        String chargeNo = rs.getString(CHARGE_NO);
        double length = rs.getDouble(LENGTH);
        int stockQuantity = rs.getInt(STOCK_QUANTITY);
        int coilTypeId = rs.getInt(COIL_TYPE_ID);
        int sleeveId = rs.getInt(SLEEVE_ID);
        String code = rs.getString(CODE);
        double width = rs.getDouble(WIDTH);
        double thickness = rs.getDouble(THICKNESS);
        double density = rs.getDouble(DENSITY);
        String name = rs.getString(NAME);
        int materialId = rs.getInt(MATERIAL_ID);

        return new StockItem(id, chargeNo, length, stockQuantity, coilTypeId, sleeveId, new CoilType(code, width, thickness, materialId), new Material(density, name));
    }
}
