package BE;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Buisness Entity Order klassen
 * @author Daniel, Klaus, Mak, Rashid
 */
public class Order
{
    private final int sOrderId;
    private final int orderId;
    private String orderName;
    private GregorianCalendar dueDate;
    private int quantity;
    private int conductedQuantity;

    private double width;
    private String status;
    private boolean Urgent;
    private String paused;
    private SalesOrder salesOrder;
    private Sleeve sleeve;
    private String errorOccured;

    /**
     * Overodnede konstruktør for Order.
     *
     * @param sOrderId
     * @param orderId
     * @param orderName
     * @param dueDate
     * @param quantity
     * @param conductedQuantity
     * @param thickness
     * @param width
     * @param status
     */
    public Order(int sOrderId, int orderId, String orderName, GregorianCalendar dueDate, int quantity, int conductedQuantity, double width, String status, boolean Urgent, SalesOrder salesOrder, Sleeve sleeve, String errorOccured)
    {
        this.sOrderId = sOrderId;
        this.orderId = orderId;
        this.orderName = orderName;
        this.dueDate = dueDate;
        this.quantity = quantity;
        this.conductedQuantity = conductedQuantity;
        this.width = width;
        this.status = status;
        this.Urgent = Urgent;
        this.salesOrder = salesOrder;
        this.sleeve = sleeve;
        this.errorOccured = errorOccured;
    }

    /**
     * Anden konstruktør for Order
     * @param orderId
     * @param o
     */
    public Order(int orderId, Order o)
    {
        this(orderId,
                o.getOrderId(),
                o.getOrderName(),
                o.getDueDate(),
                o.getQuantity(),
                o.getConductedQuantity(),
                o.getWidth(),
                o.getStatus(),
                o.isUrgent(),
                o.getSalesOrder(),
                o.getSleeve(),
                o.getErrorOccured());
                
    }

    /**
     * Printer datoen ud i en læselig format
     * @param gc
     * @return
     */
    public String printDate(GregorianCalendar gc)
    {
        return String.format("%02d-%02d-%04d",
                gc.get(Calendar.DAY_OF_MONTH),
                gc.get(Calendar.MONTH),
                gc.get(Calendar.YEAR));
    }

    /**
     * Metode som returnere sOrderId for ordren.
     */
    public int getsOrderId()
    {
        return sOrderId;
    }

    /**
     * Metode som returnere orderId for ordren
     */
    public int getOrderId()
    {
        return orderId;
    }

    /**
     * Metode som returnere orde navnet.
     */
    public String getOrderName()
    {
        return orderName;
    }


    /**
     * Metode som returnere dueDate for ordren.
     */
    public GregorianCalendar getDueDate()
    {
        return dueDate;
    }


    /**
     * Metode som returnere quantity for ordren.
     */
    public int getQuantity()
    {
        return quantity;
    }
   
     /**
     * Metode som returnere conductedQuantity for ordren.
     */
    public int getConductedQuantity()
    {
        return conductedQuantity;
    }
       
    /**
     * Metode som sætter conductedQuantity til værdien som parameteren hasCut har.
     */
    public void setConductedQuantity(int hasCut)
    {
        this.conductedQuantity = hasCut;
    }


    /**
     * Metode som returnere bredden for ordren.
     */
    public double getWidth()
    {
        return width;
    }

    /**
     * Metode som returnere status for ordren.
     */
    public String getStatus()
    {
        return status;
    }
    
    /**
     * Metode som returnere et salesOrder objekt.
     */
    public SalesOrder getSalesOrder()
    {
        return salesOrder;
    }

    /**
     * Metode som sætter status til parameterens værdi.
     * @param status the status to set
     */
    public void setStatus(String status)
    {
        this.status = status;
    } 

    /**
     * Metode som returnere et sleeve objekt.
     */
    public Sleeve getSleeve()
    {
        return sleeve;
    }
   

    /**
     * Metode som returnere om ordren er en hasteOrdre
     */
    public boolean isUrgent()
    {
        return Urgent;
    }    

    /**
     * Metode som returnere ordrens fejl.
     */
    public String getErrorOccured()
    {
        return errorOccured;
    }

    /**
     * Metode som sætter errorOccured til parameterens værdi.
     */
    public void setErrorOccured(String errorOccured)
    {
        this.errorOccured = errorOccured;
    }
}
