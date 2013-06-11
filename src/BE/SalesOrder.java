package BE;

/**
 * Buisness Entity SalesOrder klassen
 * @author Daniel, Klaus, Mak, Rashid
 */
public class SalesOrder 
{
    private final int sOrderId;
    private String custName;
    private String email;
    private int phone;
    
    /**
     * Den overordnede konstruktør til salesOrder klassen
     * @param sOrderId
     * @param custName
     * @param email
     * @param phone
     */
    public SalesOrder(int sOrderId, String custName, String email, int phone)
    {
        this.sOrderId = sOrderId;
        this.custName = custName;
        this.email = email;
        this.phone = phone;        
    }

    /**
     * Metode som returnere sOrderId.
     */
    public int getsOrderId()
    {
        return sOrderId;
    }      

    /**
     * Metode som returnere email.
     */
    public String getEmail()
    {
        return email;
    } 

    /**
     * Metode som returnere telefonnummeret.
     */
    public int getPhone()
    {
        return phone;
    }
   
    /**
     * Metode som returnere objektet i en Streng.
     * @return 
     */
    @Override
    public String toString()
    {
        return String.format("%-5d %-5s %-5s %-5d", sOrderId, getCustName(), email, phone);
    }

    /**
     * Metode som returnere kunde navnet.
     */
    public String getCustName()
    {
        return custName;
    }
    
}
