package GUI;

//<editor-fold defaultstate="collapsed" desc="Imports">
import BE.Operator;
import BE.Order;
import BE.Sleeve;
import BLL.OrderManager;
import BLL.SleeveLogManager;
import BLL.SleeveManager;
import BLL.StockItemManager;
import GUI.Models.SleeveTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
//</editor-fold>

/**
 * Graphical User Interface OrderInfo klassen.
 *
 * @author Daniel, Klaus, Mak, Rashid
 */
public class OrderInfo extends javax.swing.JFrame implements Observer
{
    //<editor-fold defaultstate="collapsed" desc="Class variables">

    private Order order;
    private Sleeve sleeve;
    private Operator operator;
    private SleeveTableModel slmodel = null;
    private static SleeveManager managerSleeve = null;
    private static StockItemManager managerStockItem = null;
    private static SleeveLogManager managerSleeveLog = null;
    private static OrderManager managerOrder = null;
    private DateTime startTime, endTime;
    private DateTimeFormatter jodaTimeFormat = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");
    private int elapsedSec, elapsedMin, elapsedHour;
    private Timer timer;
    //</editor-fold>

    /**
     * Opretter en ny form af OrderInfo
     *
     * @param order o
     * @param sleeve s
     * @param operator op
     */
    //<editor-fold defaultstate="collapsed" desc="OrderInfor Constructor">
    public OrderInfo(Order o, Sleeve s, Operator op)
    {
        order = o;
        sleeve = s;
        operator = op;
        loadManagers();
        initComponents();
//        initialButtonState();
        windowClose();
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/icons/belman.png")).getImage());
        selectedOrderSleeve();
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
            managerSleeveLog = SleeveLogManager.getInstance();
            managerStockItem = StockItemManager.getInstance();
            managerSleeve = SleeveManager.getInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //</editor-fold>

//    /**
//     * Metode der sætter vores tre knapper til disabled
//     */
//    //<editor-fold defaultstate="collapsed" desc="Initial button state">
//    private void initialButtonState()
//    {
//        if (txtStartTime.getText().isEmpty())
//        {
//            btnStart.setEnabled(false);
//            btnPause.setEnabled(false);
//            btnFinish.setEnabled(false);
//        }
//    }
//    //</editor-fold>

    /**
     * Metode der udfylder tekstbokse i forhold til den valgte ordre, sleeve og
     * operatør. Hvis den valgte sleeves start og slut tid er forskellig fra null
     * bliver tekstboksene udyldt og knapperne gjort aktive. Knapperne styres
     * af ordrens status og forskel mellem conductedQuantity og ordrens quantity
     */
    //<editor-fold defaultstate="collapsed" desc="Selected Order / Sleeve">
    private void selectedOrderSleeve()
    {
//        txtOrderName.setText(order.getOrderName());
//        txtOrderId.setText(String.valueOf(order.getOrderId()));
        txtId.setText(String.valueOf(operator.getId()));
        txtName.setText(String.valueOf(operator.getFirstName()));
        txtLastName.setText(String.valueOf(operator.getLastName()));
        txtfErrors.setText(order.getErrorOccured());
//        lblSleeves.setText(String.valueOf("Sleeves to be made " + order.getConductedQuantity() + " / " + order.getQuantity()));
        
//        try
//        {
//            txtHasCut.setText(String.valueOf(managerSleeveLog.getQuantity(order.getSleeve(), operator.getId())));
            managerSleeve.addObserver(this);
//            slmodel = new SleeveTableModel(managerSleeve.getSleevesByOrder(order));
//            tblSleeve.setModel(slmodel);
            managerOrder.addObserver(this);
            managerStockItem.addObserver(this);
            
//            tblSleeve.getSelectionModel().addListSelectionListener(new ListSelectionListener()
//            {
//                @Override
//                public void valueChanged(ListSelectionEvent es)
//                {
//                    int selectedRow = tblSleeve.getSelectedRow();
//                    if (selectedRow == -1)
//                    {
//                        return;
//                    }
//                    sleeve = slmodel.getEventsByRow(selectedRow);
//                    if (sleeve.getStartTime() != null)
//                    {
//                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
//                        txtStartTime.setText(sdf.format(sleeve.getStartTime().getTime()));
//                        txtEndTime.setText(sdf.format(sleeve.getEndTime().getTime()));
                        
//                        btnPause.setEnabled(true);
//                        btnFinish.setEnabled(true);
//                        btnStart.setEnabled(true);
//                        
//                        String status = "Finished";
//                        if (order.getConductedQuantity() == order.getQuantity() && order.getStatus().equalsIgnoreCase(status))
//                        {
//                            btnPause.setEnabled(false);
//                            btnFinish.setEnabled(false);
//                            btnStart.setEnabled(false);
//                        }
//                        else if (order.getConductedQuantity() == order.getQuantity())
//                        {
//                            btnPause.setEnabled(false);
//                            btnFinish.setEnabled(true);
//                            btnStart.setEnabled(false);
//                        }
//                    }
//                    else
//                    {
//                        btnStart.setEnabled(true);
//                    }
//                }
//            });
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    }
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
                saveErrors();
                closePressed();
            }
        });
    }
    //</editor-fold>

    /**
     * Metode der viser en Error dialog hvis man prøver at lukke vinduet mens et
     * klip stadig er igang ellers viser den en confirmDialog, med ja og nej
     * muligheder
     */
    //<editor-fold defaultstate="collapsed" desc="Confirm dialog when closing the window">
    private void closePressed()
    {
//        String option = "In progress";
//        if (order.getStatus().equalsIgnoreCase(option))
//        {
//            String error = "Cut is still in progress";
//            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
//        }
//        else
//        {
            String message = "Are you sure you want to close the window?";
            int reply = JOptionPane.showConfirmDialog(null, message, getTitle(), JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION)
            {
                dispose();
            }
//        }
    }//</editor-fold>

    /**
     * Metode der får fat i teksten fra tekstfeltet Errors og prøver at gemme
     * den i den valgte ordre, og viser en besked alt efter hvordan det gik
     */
    //<editor-fold defaultstate="collapsed" desc="Save errors comitted">
    private void saveErrors()
    {
        String message = txtfErrors.getText();
        try
        {
            managerOrder.updateErrorMessage(order, message);
            order.setErrorOccured(message);
        }
        catch (Exception e)
        {
            String error = "Unable to update error message";
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
        String popup = "The error message has been saved";
        JOptionPane.showMessageDialog(this, popup, "Save successful", JOptionPane.INFORMATION_MESSAGE);
    }
    //</editor-fold>

    /**
     * Metode der, når man trykker finish, sætter ordrens status til finished og
     * sætter conducted quantity til det samme som quantity til den valgte ordre
     */
    //<editor-fold defaultstate="collapsed" desc="Finish open order">
    private void finishOrder()
    {
        String status = "Finished";
        order.setStatus(status.toUpperCase());
        int quantity = order.getQuantity();
        order.setConductedQuantity(quantity);
        try
        {
            managerOrder.update(order);
            dispose();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //</editor-fold>

//    /**
//     * Metode der enabler pause og finish og prøver at opdatere den valgte sleeve
//     * med startTime, hvis den fejler vises en fejlmeddelelse. Hvis timeSpent
//     * er tom startes en timer som vises med det valgte format. Hvis status på
//     * den valgte ordre er pending eller paused ændres den til in progress hvis 
//     * muligt ellers vises der en fejl.
//     */
//    //<editor-fold defaultstate="collapsed" desc="Start Button / Start Cut">
//    private void startCut()
//    {
//        btnPause.setEnabled(true);
//        btnFinish.setEnabled(true);
//        
//        try
//        {
//            txtStartTime.setText(jodaTimeFormat.print(startTime));
//            startTime = jodaTimeFormat.parseDateTime(txtStartTime.getText());
//            
//            GregorianCalendar startTimeCalendar = startTime.toGregorianCalendar();
//            
//            sleeve.setStartTime(startTimeCalendar);
//            managerSleeve.updateSleeveStartTime(sleeve);
//        }
//        catch (Exception e)
//        {
//            String message = "Unable to update sleeve with id " + sleeve.getId();
//            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
//        }
//        
//        if (txtTimeSpent.getText().isEmpty())
//        {
//            elapsedHour = 0;
//            elapsedMin = 0;
//            elapsedSec = 0;
//            timer = new Timer(1000, new ActionListener()
//            {
//                @Override
//                public void actionPerformed(ActionEvent e)
//                {
//                    elapsedSec++;
//                    if (elapsedSec > 59)
//                    {
//                        elapsedSec = 0;
//                        elapsedMin++;
//                    }
//                    if (elapsedMin > 59)
//                    {
//                        elapsedMin = 0;
//                        elapsedHour++;
//                    }
//                    String displayTimer = String.format("%02d:%02d:%02d", elapsedHour, elapsedMin, elapsedSec);
//                    txtTimeSpent.setText(displayTimer);
//                }
//            });
//            timer.setInitialDelay(0);
//            timer.start();
//        }
//        else
//        {
//            timer.start();
//        }
//        
//        String option = "Pending";
//        String option2 = "Paused";
//        if (order.getStatus().equalsIgnoreCase(option) || order.getStatus().equalsIgnoreCase(option2))
//        {
//            String status = "in Progress";
//            order.setStatus(status.toUpperCase());
//            try
//            {
//                managerOrder.updateStatus(order);
//            }
//            catch (Exception e)
//            {
//                String message = "Unable to update order";
//                JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//        else
//        {
//            String message = "Production Order " + order.getOrderId() + "'s status is already: In progress.";
//            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    //</editor-fold>
    
//    /**
//     * Metode der stopper timeren, sætter text field endTime til endTime og prøver
//     * at opdatere endTime på det valgte sleeve. Hvis status på den valgte sleeve
//     * er in progress bliver den sat til pause hvis muligt. Til sidst vil den
//     * lukke vinduet
//     */
//    //<editor-fold defaultstate="collapsed" desc="Pause Button / Pause Cut">
//    private void pauseCut()
//    {
//        timer.stop();
//        
//        try
//        {
////            txtEndTime.setText(jodaTimeFormat.print(endTime));
////            endTime = jodaTimeFormat.parseDateTime(txtEndTime.getText());
//            
//            GregorianCalendar endTimeCalendar = endTime.toGregorianCalendar();
//            
//            sleeve.setEndTime(endTimeCalendar);
//            managerSleeve.updateSleeveEndTime(sleeve);
//        }
//        catch (Exception e)
//        {
//            String message = "Unable to update sleeve with id " + sleeve.getId();
//            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
//        }
//        
//        String option = "In Progress";
//        if (order.getStatus().equalsIgnoreCase(option))
//        {
//            String status = "Paused";
//            order.setStatus(status.toUpperCase());
//            try
//            {
//                managerOrder.updateStatus(order);
//            }
//            catch (Exception e)
//            {
//                String message = "Unable to update order";
//                JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
//            }
//            new SleeveInfo(order, operator).setVisible(true);
//        }
//        else
//        {
//            String message = "Production Order " + order.getOrderId() + "'s status is not in progress or already paused.";
//            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
//        }
//        dispose();
//    }
//    //</editor-fold>
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlProductionInformation = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblEmployeeCutting = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblFirstName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblLastName = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        btnOk = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblErrors = new javax.swing.JLabel();
        scrpErrors = new javax.swing.JScrollPane();
        txtfErrors = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("GUI/Bundle"); // NOI18N
        setTitle(bundle.getString("OrderInfo.title")); // NOI18N
        setLocationByPlatform(true);

        pnlProductionInformation.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), bundle.getString("OrderInfo.pnlProductionInformation.border.title"))); // NOI18N

        lblEmployeeCutting.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblEmployeeCutting.setText(bundle.getString("OrderInfo.lblEmployeeCutting.text")); // NOI18N

        lblId.setText(bundle.getString("OrderInfo.lblId.text")); // NOI18N

        txtId.setEditable(false);

        lblFirstName.setText(bundle.getString("OrderInfo.lblFirstName.text")); // NOI18N

        txtName.setEditable(false);

        lblLastName.setText(bundle.getString("OrderInfo.lblLastName.text")); // NOI18N

        txtLastName.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFirstName)
                            .addComponent(lblId))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtId)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblLastName)
                        .addGap(71, 71, 71)
                        .addComponent(txtLastName))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblEmployeeCutting)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(37, 37, 37))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEmployeeCutting)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblId)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFirstName)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLastName, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlProductionInformationLayout = new javax.swing.GroupLayout(pnlProductionInformation);
        pnlProductionInformation.setLayout(pnlProductionInformationLayout);
        pnlProductionInformationLayout.setHorizontalGroup(
            pnlProductionInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlProductionInformationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlProductionInformationLayout.setVerticalGroup(
            pnlProductionInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProductionInformationLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnOk.setText(bundle.getString("OrderInfo.btnOk.text")); // NOI18N
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblErrors.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblErrors.setText(bundle.getString("OrderInfo.lblErrors.text")); // NOI18N

        txtfErrors.setColumns(20);
        txtfErrors.setRows(5);
        scrpErrors.setViewportView(txtfErrors);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblErrors)
                        .addGap(29, 29, 29))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(scrpErrors, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                        .addGap(15, 15, 15))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lblErrors)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrpErrors, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(585, Short.MAX_VALUE)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlProductionInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlProductionInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOk)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        saveErrors();
        closePressed();
    }//GEN-LAST:event_btnOkActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblEmployeeCutting;
    private javax.swing.JLabel lblErrors;
    private javax.swing.JLabel lblFirstName;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblLastName;
    private javax.swing.JPanel pnlProductionInformation;
    private javax.swing.JScrollPane scrpErrors;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextArea txtfErrors;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg)
    {
    }
}

