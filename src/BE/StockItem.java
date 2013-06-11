package BE;

/**
 * Buisness Entity StockItem klassen.
 * @author Daniel, Klaus, Mak, Rashid
 */
public class StockItem
{
    private final int id;
    private String chargeNo;
    private double length;
    private double stockQuantity;
    private int coilTypeId;
    private int sleeveId;
    private CoilType coilType;
    private Material material;

    /**
     * Den overordnede konstruktør for StockItem.
     * @param id
     * @param chargeNo
     * @param length
     * @param stockQuantity
     * @param coilTypeId
     * @param sleeveId
     * @param coilType
     * @param material
     */
    public StockItem(int id, String chargeNo, double length, double stockQuantity, int coilTypeId, int sleeveId, CoilType coilType, Material material)
    {
        this.id = id;
        this.chargeNo = chargeNo;
        this.length = length;
        this.stockQuantity = stockQuantity;
        this.coilTypeId = coilTypeId;
        this.sleeveId = sleeveId;
        this.coilType = coilType;
        this.material = material;
    }

    /**
     * Anden konstruktør til StockItem.
     * @param id
     * @param item
     */
    public StockItem(int id, StockItem item)
    {
        this(id,
                item.getChargeNo(),
                item.getLength(),
                item.getStockQuantity(),
                item.getCoilTypeId(),
                item.getSleeveId(),
                item.getCoilType(),
                item.getMaterial());                
    }

    /**
     * Metode som returnere lagervarens id. 
     */
    public int getId()
    {
        return id;
    }

    /**
     * Metode som returnere lagervarens chargeNo.
     */
    public String getChargeNo()
    {
        return chargeNo;
    }

    /**
     * Metode som returnere lagervarens længde.
     */
    public double getLength()
    {
        return length;
    }


    /**
     * Metode som returnere mængden af lagervareren.
     */
    public double getStockQuantity()
    {
        return stockQuantity;
    }

    /**
     * Metode som returnere lagervarens coilTypeId.
     */
    public int getCoilTypeId()
    {
        return coilTypeId;
    }

    /**
     * Metode som returnere lagervarens sleeveId.
     */
    public int getSleeveId()
    {
        return sleeveId;
    }


    /**
     * Metode som returnere et objekt af klassen CoilType.
     */
    public CoilType getCoilType()
    {
        return coilType;
    }


    /**
     * Metode som returnere et objekt af klassen Material.
     */
    public Material getMaterial()
    {
        return material;
    }

}
