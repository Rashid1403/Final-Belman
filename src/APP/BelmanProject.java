package APP;

import GUI.Overview;
import java.awt.EventQueue;
import javax.swing.UIManager;

/**
 * Belman Project 2013
 * @author Daniel, Klaus, Mak, Rashid
 * @version 1.0a
 */
public class BelmanProject
{
    /**
     * Belman Project main metode
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                   new Overview().setVisible(true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}