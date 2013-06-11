package BE;

/**
 * Buisness entity CoilType klassen
 *
 * @author Daniel, Klaus, Mak, Rashid
 */
public class CoilType
{

    private final int id;
    private String code;
    private double width;
    private double thickness;
    private int materialId;

    /**
     * Den overordnede konstruktør til CoilType.
     *
     * @param id
     * @param code
     * @param width
     * @param thickness
     * @param materialId
     */
    public CoilType(int id, String code, double width, double thickness, int materialId)
    {
        this.id = id;
        this.code = code;
        this.width = width;
        this.thickness = thickness;
        this.materialId = materialId;
    }

    /**
     * Anden konstruktør som opretter et CoilType object
     *
     * @param id
     * @param c
     */
    public CoilType(int id, CoilType c)
    {
        this(id,
                c.getCode(),
                c.getWidth(),
                c.getThickness(),
                c.getMaterialId());
    }

    
    /**
     * Tredje konstruktør, som opretter et CoilType objekt med id -1
     *
     * @param code
     * @param width
     * @param thickness
     * @param materialId
     */
    public CoilType(String code, double width, double thickness, int materialId)
    {
        this(-1, code, width, thickness, materialId);
    }

    
    /**
     * Metode som returnere koden for coiltypen.
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * Metode som returnere bredden for coiltypen.
     */
    public double getWidth()
    {
        return width;
    }

    /**
     * Metode som returnere tykkelsen for coiltypen.
     */
    public double getThickness()
    {
        return thickness;
    }

    /**
     * Metode som returnere materiale id'et for coiltypen.
     */
    public int getMaterialId()
    {
        return materialId;
    }
}
