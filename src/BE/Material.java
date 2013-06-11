package BE;

/**
 * Buisness entity Material klassen
 * @author Daniel, Klaus, Mak, Rashid
 */
public class Material
{
    private int id;
    private double density;
    private String name;

    /**
     * Den overordnede konstruktør til Material
     *
     * @param id
     * @param density
     * @param name
     */
    public Material(int id, double density, String name)
    {
        this.id = id;
        this.density = density;
        this.name = name;
    }

    /**
     * Anden konstruktør til Material som laver et material objekt med id -1
     * @param density
     * @param name
     */
    public Material(double density, String name)
    {
        this(-1, density, name);
    }

    /**
     * Tredje Material konstruktør
     * @param name
     */
    public Material(String name)
    {
        this.name = name;
    }

    /**
     * Metode som returnere navnet på materialet.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Metode som returnere id på materialet.
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * Metode som formatere navnet til en læselig streng.
     * @return 
     */
    @Override
    public String toString()
    {
        return String.format(" %-5s", name);
    }
}
