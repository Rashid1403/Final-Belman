package BE;

/**
 * Buisness entity Operator klassen
 * @author Daniel, Klaus, Mak, Rashid
 */
public class Operator
{
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private Sleeve sleeve;
    private int quantityCut;

    /**
     * Den overordnede konstruktør for Operator klassen
     * @param id
     * @param username
     * @param firstName
     * @param lastName
     * @param sleeve
     * @param quantityCut
     */
    public Operator(int id, String username, String firstName, String lastName, Sleeve sleeve, int quantityCut)
    {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sleeve = sleeve;
        this.quantityCut = quantityCut;
    }
  

    /**
     * Metode som returnere operatorens fornavn
     */
    public String getFirstName()
    {
        return firstName;
    } 

    /**
     * Metode som returnere operatorens efternavn
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Metode som returnere operatorens ID
     */
    public int getId()
    {
        return id;
    }
    /**
     * Metode som returnere brugernavnet som en streng.
     * @return 
     */
    @Override
    public String toString()
    {
        return username;
    }

}
