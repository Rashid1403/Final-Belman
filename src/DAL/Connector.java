package DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

/**
 * Data Access Layer Connector klasse.
 * @author Daniel, Klaus, Mak, Rashid
 */

public class Connector
{
    private static final String ServerName = "SERVER";
    private static final String PortNumber = "PORT";
    private static final String DatabaseName = "DATABASE";
    private static final String User = "USER";
    private static final String Password = "PASS";
    private static final String InstanceName = "INSTANCE";
    
    private Properties config;
    private static SQLServerDataSource datasource;
    private static Connector instance = null;
    
    private Connector() throws FileNotFoundException, IOException
    {
        config = new Properties();
        config.load(new FileReader(System.getProperty( "user.dir" ) + "/config.cfg" ) );
        datasource = new SQLServerDataSource();
        datasource.setServerName(config.getProperty(ServerName));
        datasource.setPortNumber(Integer.valueOf(config.getProperty(PortNumber)));
        datasource.setDatabaseName(config.getProperty(DatabaseName));
        datasource.setUser(config.getProperty(User));
        datasource.setPassword(config.getProperty(Password));
        datasource.setInstanceName(config.getProperty(InstanceName));
    }
    
    /**
     * Metode som returnerer det eneste instans af klassen.
     * @throws FileNotFoundException
     * @throws IOException
     */
    protected static Connector getInstance() throws FileNotFoundException, IOException
    {
        if (instance == null)
        {
            instance = new Connector();
        }
        return instance;
    }
    
    /**
     * Metode som opretter forbindelse til databasen.
     * @throws SQLServerException
     */
    protected Connection getConnection() throws SQLServerException
    {
        return datasource.getConnection();
    }
}
