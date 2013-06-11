/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

//<editor-fold defaultstate="collapsed" desc="Imports">
import BE.Operator;
import BE.Order;
import BLL.OperatorManager;
import BLL.OrderManager;
import BLL.SleeveLogManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
//</editor-fold>

/**
 * Graphical User Interface SleeveInfor klassen
 *
 * @author Daniel, Klaus, Mak, Rashid
 */
public class SleeveInfo extends JDialog
{
    //<editor-fold defaultstate="collapsed" desc="Class variables">

    private Order order;
    private Operator operator;
    private OrderManager managerOrder = null;
    private OperatorManager managerOperator = null;
    private SleeveLogManager managerSleeveLog = null;
    //</editor-fold>

    /**
     * Opretter en ny form a SleeveInfo
     *
     * @param order o
     * @param operator op
     */
    //<editor-fold defaultstate="collapsed" desc="SleeveInfo constructor">
    public SleeveInfo(Order o, Operator op)
    {
        setModal(true);
        operator = op;
        order = o;
        loadManagers();
        initComponents();
        windowClose();
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/icons/belman.png")).getImage());
        numbersOnlyKeyListener();
        setLocationByPlatform(true);
        txtOf.setText(String.valueOf(o.getQuantity()));
        addEnterKeyListener();
    }
    //</editor-fold>

    /**
     * Metode der loader vores managers
     */
    //<editor-fold defaultstate="collapsed" desc="Load Managers">
    private void loadManagers()
    {
        try
        {
            managerOrder = OrderManager.getInstance();
            managerOperator = OperatorManager.getInstance();
            managerSleeveLog = SleeveLogManager.getInstance();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //</editor-fold>
    
    /**
     * Metode der tilføjer en windowListener til vores OrderInfo frame, der
     * kalder closePressed(); hvis vinduet skulle blive lukket
     */
    //<editor-fold defaultstate="collapsed" desc="Windowlistener - Window closing">
    private void windowClose()
    {
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closePressed();
            }
        });
    }
    //</editor-fold>
    
    /**
     * Metode der viser en Error dialog hvis man ikke har indtastet antal klip
     * ellers virker det som at trykke ok
     */
    //<editor-fold defaultstate="collapsed" desc="Confirm dialog when closing the window">
    private void closePressed()
    {
        if (txtSleevesMade.getText().trim().isEmpty())
        {
            String error = "Please input sleeves cut";
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            btnOk.doClick();
        }
    }//</editor-fold>

    /**
     * Metode der tilføjer en key listener til txtSleevesmade som gør det muligt
     * at trykke enter istedet for at trykke på Ok knappen
     */
    //<editor-fold defaultstate="collapsed" desc="Enter key listener">
    private void addEnterKeyListener()
    {
        KeyListener enterKey = new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent ke)
            {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    btnOk.doClick();
                }
            }
        };
        txtSleevesMade.addKeyListener(enterKey);
    }
    //</editor-fold>

    /**
     * Metode der tilføjer en key listener som kun tager imod tal
     */
    //<editor-fold defaultstate="collapsed" desc="Numbers only key listener">
    private void numbersOnlyKeyListener()
    {
        txtSleevesMade.addKeyListener(new gui.NumbersOnlyKeyListener());
    }
    //</editor-fold>
    
    /**
     * Metode der viser en fejl hvis tekstfeltet er tom ellers vil den valgte
     * ordre blive opdateret med amountCut hvis det er under det antal der skal
     * klippes i ordren, hvis amount er lig med der der skal klippes bliver
     * ordren opdateret med antallet og status bliver opdateret til finished.
     * Hvis amountCut overstiger antallet på ordren vil der blive vist en fejl.
     */
    //<editor-fold defaultstate="collapsed" desc="Button Ok / Save Cut">
    private void saveCut()
    {
        if (txtSleevesMade.getText().trim().isEmpty())
        {
            String message = "Please input sleeves cut";
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            int amountCut = Integer.parseInt(txtSleevesMade.getText()) + order.getConductedQuantity();
            
            if (amountCut < order.getQuantity())
            {
                order.setConductedQuantity(amountCut);
                try
                {
                    managerOrder.update(order);
                    managerSleeveLog.addLog(order.getSleeve().getId(), operator, amountCut, 0);
                    dispose();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (amountCut == order.getQuantity())
            {
                order.setConductedQuantity(amountCut);
                String status = "Finished";
                order.setStatus(status.toUpperCase());
                try
                {
                    managerOrder.update(order);
                    managerSleeveLog.addLog(order.getSleeve().getId(), operator, amountCut, 0);
                    dispose();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                String message = "You cant cut more than the total quantity";
                JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //</editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnOk = new javax.swing.JButton();
        lblSleevesMade = new javax.swing.JLabel();
        txtSleevesMade = new javax.swing.JTextField();
        lblOf = new javax.swing.JLabel();
        txtOf = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Belman Manager");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOkActionPerformed(evt);
            }
        });

        lblSleevesMade.setText("Sleeves Made:");

        lblOf.setText("Of");

        txtOf.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblSleevesMade)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSleevesMade, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOf)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtOf, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(93, 93, 93)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSleevesMade)
                    .addComponent(txtSleevesMade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOf)
                    .addComponent(txtOf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOk)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        saveCut();
    }//GEN-LAST:event_btnOkActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblOf;
    private javax.swing.JLabel lblSleevesMade;
    private javax.swing.JTextField txtOf;
    private javax.swing.JTextField txtSleevesMade;
    // End of variables declaration//GEN-END:variables
}
