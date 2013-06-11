package BE;

import java.util.GregorianCalendar;

/**
 * Buisness Entity Sleeve klassen.
 * @author Daniel, Klaus, Mak, Rashid
 */
public class Sleeve {

    private final int id;
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;
    private double thickness;
    private double circumference;
    private int materialId;
    private int pOrderId;
    private Material material;

    /**
     * Den overordnede konstruktør til Sleeve.
     *
     * @param id
     * @param startTime
     * @param endTime
     * @param thickness
     * @param circumference
     * @param materialId
     * @param pOrderId
     */
    
    public Sleeve(int id, GregorianCalendar startTime, GregorianCalendar endTime, double thickness, double circumference, int materialId, int pOrderId, Material material)
    {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.thickness = thickness;
        this.circumference = circumference;
        this.materialId = materialId;
        this.pOrderId = pOrderId;
        this.material = material;

    }

    /**
     * Anden konstruktør til sleeve klassen.
     * @param id
     * @param startTime
     * @param endTime
     * @param thickness
     * @param circumference
     * @param materialId
     * @param pOrderId
     */
    public Sleeve(int id, GregorianCalendar startTime, GregorianCalendar endTime, double thickness, double circumference, int materialId, int pOrderId)
    {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.thickness = thickness;
        this.circumference = circumference;
        this.materialId = materialId;
        this.pOrderId = pOrderId; 
   
    }  

   
    /**
     * Metode som returnere sleevens id.
     */
    public int getId() 
    {
        return id;
    }

    /**
     * Metode som returnere start tiden på sleevet.
     */
    public GregorianCalendar getStartTime() 
    {
        return startTime;
    }

    /**
     * Metode som sætter start tiden på sleevet til værdien i parameteren.
     */
    public void setStartTime(GregorianCalendar startTime) 
    {
        this.startTime = startTime;
    }

    /**
     * Metode som returnere slut tiden.
     */
    public GregorianCalendar getEndTime() 
    {
        return endTime;
    }

    /**
     * Metode som sætter slut tiden på sleevet til værdien i parameteren.
     */
    public void setEndTime(GregorianCalendar endTime) 
    {
        this.endTime = endTime;
    }

    /**
     * Metode som returnere sleevets tykkelse.
     */
    public double getThickness() 
    {
        return thickness;
    }
   
    /**
     * Metode som returnere sleevets omkreds.
     */
    public double getCircumference() 
    {
        return circumference;
    }
  
    /**
     * Metode som returnere materiale id på sleevet.
     */
    public int getMaterialId() 
    {
        return materialId;
    }
   

    /**
     * Metode som returnere productionorder id på sleevet.
     */
    public int getpOrderId() 
    {
        return pOrderId;
    }    

    /**
     * Metode som returnere et objekt af klassen Material. 
     */
    public Material getMaterial() 
    {
        return material;
    }

}
