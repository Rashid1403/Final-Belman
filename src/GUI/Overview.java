/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

// <editor-fold defaultstate="collapsed" desc="Imports">                          
import BE.Operator;
import BE.Order;
import BE.Sleeve;
import BE.StockItem;
import BLL.OperatorManager;
import BLL.OrderManager;
import BLL.SleeveLogManager;
import BLL.SleeveManager;
import BLL.StockItemManager;
import GUI.Models.OrderTablemodel;
import GUI.Models.ProductionSleeveTableModel;
import GUI.Models.StockList2TableModel;
import GUI.Models.StockListTableModel;
import GUI.Models.SleeveTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
// </editor-fold> 

/**
 * Graphical User Interface Overview klassen
 * 
 * @author Rashid, Daniel Mak og Klaus
 */
public class Overview extends javax.swing.JFrame implements Observer
{
    //<editor-fold defaultstate="collapsed" desc="Klasse Variabler">
    private ResourceBundle rb = null;
    static OrderManager managerOrder = null;
    static StockItemManager managerStockItem = null;
    private static SleeveLogManager managerSleeveLog = null;
    static SleeveManager managerSleeve = null;
    static OperatorManager managerOperator = null;
    private OrderTablemodel modelOrder = null;    
    private StockListTableModel modelEmptyStocklist = null;
    private ProductionSleeveTableModel modelProduction = null;
    private StockList2TableModel modelStocklist = null;
    private SleeveTableModel slmodel = null;
    private DateTime startTime, endTime;
    private DateTimeFormatter jodaTimeFormat = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm:ss");
    private int elapsedSec, elapsedMin, elapsedHour;
    private Sleeve sleeve;
    private Timer timer;
    private Order ord;
    private Sleeve s;
    private Operator operator;
    //private String operator;
    //</editor-fold>

    /** 
     * Opretter en nye form a Overview
     */
    //<editor-fold defaultstate="collapsed" desc="Overview constructor">
    public Overview()
    {
        loadManagers();
        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("GUI.Bundle");
        //        setExtendedState(MAXIMIZED_BOTH); MAXIMIZED WINDOW
        initComponents();
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/icons/belman.png")).getImage());
        initialButtonState1();
        initialButtonState2();
        windowClose();
        setLocationRelativeTo(null);
        productionSleeveListSelectioner1();
        productionSleeveListSelectioner2();
        pausedOrderTable();
        localeLanguage.setLocale(locale);
        mouseListener();
        mouseListener2();
        setTableColumnSize();
        setTableSelectionMode();
        comboboxModel();
        operator = (Operator) cbxOperator.getSelectedItem();
        updateGUILanguage();
//        selectedOrderSleeve1();
//        selectedOrderSleeve2();
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
            managerStockItem = StockItemManager.getInstance();
            managerSleeve = SleeveManager.getInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    /**
     * Metode der sætter kolonne størrelse på vores tabeller
     */
    // <editor-fold defaultstate="collapsed" desc="Set table column sizes">
    private void setTableColumnSize()     
    {
        tblProductionSleeve.getColumnModel().getColumn(0).setPreferredWidth(180);
        tblProductionSleeve.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblProductionSleeve.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblProductionSleeve.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblProductionSleeve.getColumnModel().getColumn(4).setPreferredWidth(150);
        tblProductionSleeve.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblProductionSleeve.getColumnModel().getColumn(6).setPreferredWidth(100);
        tblProductionSleeve.getColumnModel().getColumn(7).setPreferredWidth(100);
        tblProductionSleeve.getColumnModel().setColumnMargin(5);

        tblStockList.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblStockList.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblStockList.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblStockList.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblStockList.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblStockList.getColumnModel().getColumn(5).setPreferredWidth(80);

    }// </editor-fold> 
    
        /**
     * Metode der sætter vores tre knapper til disabled
     */
    //<editor-fold defaultstate="collapsed" desc="Initial button state">
    private void initialButtonState1()
    {
        if (txtStartTime17.getText().isEmpty())
        {
            btnStart17.setEnabled(false);
            btnPause17.setEnabled(false);
            btnFinish18.setEnabled(false);
        }
    }
    //</editor-fold>
    
     /**
     * Metode der sætter vores tre knapper til disabled
     */
    //<editor-fold defaultstate="collapsed" desc="Initial button state">
    private void initialButtonState2()
    {
        if (txtStartTime17.getText().isEmpty())
        {
            btnStart18.setEnabled(false);
            btnPause18.setEnabled(false);
            btnFinish19.setEnabled(false);
        }
    }
    //</editor-fold>

     /**
     * Metode der udfylder tekstbokse i forhold til den valgte ordre, sleeve og
     * operatør. Hvis den valgte sleeves start og slut tid er forskellig fra null
     * bliver tekstboksene udyldt og knapperne gjort aktive. Knapperne styres
     * af ordrens status og forskel mellem conductedQuantity og ordrens quantity
     */
    //<editor-fold defaultstate="collapsed" desc="Selected Order / Sleeve">
    private void selectedOrderSleeve1()
    {
        if(ord == null)
        {
            txtOrderName2.setText("");
            txtOrderId3.setText("");
            lblSleeves.setText("");
        }
        if(operator == null)
        {
            txtId.setText("");
            txtName.setText("");
            txtLastName.setText("");
        }
        else
        {
        txtOrderName2.setText(ord.getOrderName());
        //txtOrderId3.setText(String.valueOf(ord.getOrderId()));
        txtId.setText(String.valueOf(operator.getId()));
        txtName.setText(String.valueOf(operator.getFirstName()));
        txtLastName.setText(String.valueOf(operator.getLastName()));
        lblSleeves.setText(String.valueOf("Sleeves to be made " + ord.getConductedQuantity() + " / " + ord.getQuantity()));
    
        try
        {
//            txtHasCut1.setText(String.valueOf(managerSleeveLog.getQuantity(ord.getSleeve())));
            managerSleeve.addObserver(this);
            slmodel = new SleeveTableModel(managerSleeve.getSleevesByOrder(ord));
            tblSleeve2.setModel(slmodel);
            managerOrder.addObserver(this);
            managerStockItem.addObserver(this);
            
            tblSleeve2.getSelectionModel().addListSelectionListener(new ListSelectionListener()
            {
                @Override
                public void valueChanged(ListSelectionEvent es)
                {
                    int selectedRow = tblSleeve2.getSelectedRow();
                    if (selectedRow == -1)
                    {
                        return;
                    }
                    sleeve = slmodel.getEventsByRow(selectedRow);
                    if (sleeve.getStartTime() != null)
                    {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
                        txtStartTime17.setText(sdf.format(sleeve.getStartTime().getTime()));
                        txtEndTime17.setText(sdf.format(sleeve.getEndTime().getTime()));
                        
                        btnPause17.setEnabled(true);
                        btnFinish18.setEnabled(true);
                        btnStart17.setEnabled(true);
                        
                        String status = "Finished";
                        if (ord.getConductedQuantity() == ord.getQuantity() && ord.getStatus().equalsIgnoreCase(status))
                        {
                            btnPause17.setEnabled(false);
                            btnFinish18.setEnabled(false);
                            btnStart17.setEnabled(false);
                        }
                        else if (ord.getConductedQuantity() == ord.getQuantity())
                        {
                            btnPause17.setEnabled(false);
                            btnFinish18.setEnabled(true);
                            btnStart17.setEnabled(false);
                        }
                    }
                    else
                    {
                        btnStart17.setEnabled(true);
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    }
    //</editor-fold>
    
    
     /**
     * Metode der udfylder tekstbokse i forhold til den valgte ordre, sleeve og
     * operatør. Hvis den valgte sleeves start og slut tid er forskellig fra null
     * bliver tekstboksene udyldt og knapperne gjort aktive. Knapperne styres
     * af ordrens status og forskel mellem conductedQuantity og ordrens quantity
     */
    //<editor-fold defaultstate="collapsed" desc="Selected Order / Sleeve">
    private void selectedOrderSleeve2()
    {if(ord == null)
        {
            txtOrderName3.setText("");
            txtOrderId4.setText("");
            lblSleeves.setText("");
        }
        if(operator == null)
        {
            txtId1.setText("");
            txtName1.setText("");
            txtLastName1.setText("");
        }
        else
        {
        txtOrderName3.setText(ord.getOrderName());
        txtOrderId4.setText(String.valueOf(ord.getOrderId()));
        txtId1.setText(String.valueOf(operator.getId()));
        txtName1.setText(String.valueOf(operator.getFirstName()));
        txtLastName1.setText(String.valueOf(operator.getLastName()));
        //lblSleeves1.setText(String.valueOf("Sleeves to be made "));
        //+ ord.getConductedQuantity() + " / " + ord.getQuantity()));
        
        try
        {
         //   txtHasCut.setText(String.valueOf(managerSleeveLog.getQuantity(o.getSleeve())));
            managerSleeve.addObserver(this);
            slmodel = new SleeveTableModel(managerSleeve.getSleevesByOrder(ord));
            tblSleeve3.setModel(slmodel);
            managerOrder.addObserver(this);
            managerStockItem.addObserver(this);
            
            tblSleeve3.getSelectionModel().addListSelectionListener(new ListSelectionListener()
            {
                @Override
                public void valueChanged(ListSelectionEvent es)
                {
                    int selectedRow = tblSleeve3.getSelectedRow();
                    if (selectedRow == -1)
                    {
                        return;
                    }
                    sleeve = slmodel.getEventsByRow(selectedRow);
                    if (sleeve.getStartTime() != null)
                    {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
                        txtStartTime18.setText(sdf.format(sleeve.getStartTime().getTime()));
                        txtEndTime18.setText(sdf.format(sleeve.getEndTime().getTime()));
                        
                        btnPause18.setEnabled(true);
                        btnFinish19.setEnabled(true);
                        btnStart18.setEnabled(true);
                        
                        String status = "Finished";
                        if (ord.getConductedQuantity() == ord.getQuantity() && ord.getStatus().equalsIgnoreCase(status))
                        {
                            btnPause18.setEnabled(false);
                            btnFinish19.setEnabled(false);
                            btnStart18.setEnabled(false);
                        }
                        else if (ord.getConductedQuantity() == ord.getQuantity())
                        {
                            btnPause18.setEnabled(false);
                            btnFinish19.setEnabled(true);
                            btnStart18.setEnabled(false);
                        }
                    }
                    else
                    {
                        btnStart18.setEnabled(true);
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    }
    //</editor-fold>
    /**
     * Metode der styrer selection mode på vores tabeller
     */
    //<editor-fold defaultstate="collapsed" desc="Set table selection mode">
    private void setTableSelectionMode()
    {
        tblOrderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProductionSleeve.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblStockList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    //</editor-fold>

    /**
     * Metode der opdaterer sproget på overview
     */
    // <editor-fold defaultstate="collapsed" desc="Updates fields and labels when the language is changed"> 
    private void updateGUILanguage()                             
    {
        btnClose.setText(rb.getString("Overview.btnClose.text"));
        btnReset.setText(rb.getString("Overview.btnReset.text"));

        menuFile.setText(rb.getString("Overview.menuFile.text"));
        menuSettings.setText(rb.getString("Overview.menuSettings.text"));

        itemExit.setText(rb.getString("Overview.itemExit.text"));
        itemHelp.setText(rb.getString("Overview.itemHelp.text_1"));

        tpaneOverview.setTitleAt(0, rb.getString("Overview.pnlCutting2.TabConstraints.tabTitle"));
        lblLoggedIn.setText(rb.getString("Overview.lblLoggedIn.text") + operator);

        lblProductionOrder.setText(rb.getString("Overview.lblProductionOrder.text"));
        lblStock.setText(rb.getString("Overview.lblStock.text"));
        lblChangeOperator.setText(rb.getString("Overview.lblChangeOperator.text"));

        lblCustomerName.setText(rb.getString("Overview.lblCustomerName.text"));
        lblDate.setText(rb.getString("Overview.lblDate.text"));
        lblEmail.setText(rb.getString("Overview.lblEmail.text"));
        lblOrder.setText(rb.getString("Overview.lblOrder.text"));
        lblPhone.setText(rb.getString("Overview.lblPhone.text"));
        lblQuantity.setText(rb.getString("Overview.lblQuantity.text"));
        lblSalesOrderId.setText(rb.getString("Overview.lblSalesOrderId.text"));
        lblThickness.setText(rb.getString("Overview.lblThickness.text"));
        lblWidth.setText(rb.getString("Overview.lblWidth.text"));
        lblOrderName2.setText(rb.getString("Overview.lblOrderName2.text"));
        lblOrderId2.setText(rb.getString("Overview.lblOrderId2.text"));
//        pnlProductionInformation2.
//        txtOrderName2.setText(rb.getString(""));
        


//     TitledBorder border = (TitledBorder) pnlCustomerInfo.getBorder();

//          TitledBorder border2 = (TitledBorder) pnlMeasurements.getBorder();

//     TitledBorder border3 = (TitledBorder) pnlCutting2.getBorder();

//     TitledBorder border4 = (TitledBorder) pnlOrder.getBorder();

//     TitledBorder border5 = (TitledBorder) pnlOrderInfo.getBorder();
    }// </editor-fold> 

    /**
     * Metode der opdaterer vores LoggedIn label med vores operator string
     */
    //<editor-fold defaultstate="collapsed" desc="Update loggedIn label with Operator string">
    private void updateOperator()
    {
        lblLoggedIn.setText(rb.getString("Overview.lblLoggedIn.text") + operator);
    }
    //</editor-fold>

    /**
     * Metode der opdaterer vores operator string med den valgte operator i vores operator kombobox
     * Og derefter kalder updateOperator();
     */
    //<editor-fold defaultstate="collapsed" desc="Updates the Operator String from the Combobox">
    private void selectedOperator()
    {
        Operator selectedOperator = (Operator) cbxOperator.getSelectedItem();

        if (selectedOperator != null)
        {
//            operator = selectedOperator.toString();
            updateOperator();
            operator = selectedOperator;
        }
    }
    //</editor-fold>

    /**
     * Metode der styrer vores kombobox, som viser alle operators der hentes via
     * operatorManager og derefter kalder selectedOperator(); hver gang der vælges
     * en ny operator
     */
    //<editor-fold defaultstate="collapsed" desc="Combobox model">
    private void comboboxModel()
    {
        try
        {
            DefaultComboBoxModel model = new DefaultComboBoxModel(managerOperator.getAllOperators().toArray());
            cbxOperator.setModel(model);
            
            cbxOperator.addItemListener(new ItemListener()
            {
                @Override
                public void itemStateChanged(ItemEvent e)
                {
                    if (e.getStateChange() == ItemEvent.SELECTED)
                    {
                        selectedOperator();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    /**
     * Metode der viser en confirmDialog, med ja og nej muligheder
     */
    //<editor-fold defaultstate="collapsed" desc="Confirm dialog when closing the window">
    private void closePressed()
    {
        String message = "Are you sure you want to exit?";
        int reply = JOptionPane.showConfirmDialog(null, message, getTitle(), JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
    }
    //</editor-fold>

    /**
     * Metode der tilføjer en windowListener til vores Overview frame, der kalder 
     * closePressed(); hvis vinduet skulle blive lukket
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
     * Metoder til at oprette en liste over alle paused ordre. Bruger en listener
     * til at udfylde informationer i tekstbokse i forhold til den valgte ordre.
     */
    //<editor-fold defaultstate="collapsed" desc="Paused Order Table">
    private void pausedOrderTable()
    {
        try
        {
            managerOrder.addObserver(this);
            
            modelOrder = new OrderTablemodel(managerOrder.getPaused());
            tblOrderList.setModel(modelOrder);
            
            tblOrderList.getSelectionModel().addListSelectionListener(new ListSelectionListener()
            {
                @Override
                public void valueChanged(ListSelectionEvent es)
                {
                    int selectedRow = tblOrderList.getSelectedRow();
                    if (selectedRow == -1);
                    {
                        
                    }
                    if (es.getValueIsAdjusting() || selectedRow < 0)
                    {
                        txtOrderId.setText("");
                        txtDate.setText("");
                       // txtQuantity.setText("");
                        txtThickness.setText("");
                        txtWidth.setText("");
                        txtSalesOrderId.setText("");
                        txtCustomerName.setText("");
                        txtEmail.setText("");
                        txtPhone.setText("");
                    }
                    
                    Order o = modelOrder.getEventsByRow(selectedRow);
                    
                    try
                    {
                        txtOrderId.setText(String.valueOf(o.getOrderId()));
                        txtOrderId3.setText(String.valueOf(o.getOrderId()));
                        txtDate.setText(String.valueOf(o.printDate(o.getDueDate())));
                       // txtQuantity.setText(String.valueOf(o.getQuantity()));
                       // txtConductedQuantity.setText(String.valueOf(o.getConductedQuantity()));
                        lblSleeves2.setText(String.valueOf("Sleeves to be made " + o.getConductedQuantity() + " / " + o.getQuantity()));
                        
                        txtThickness.setText(String.valueOf(o.getSleeve().getThickness()));
                        txtWidth.setText(String.valueOf(o.getWidth()));
                        txtSalesOrderId.setText(String.valueOf(o.getSalesOrder().getsOrderId()));
                        txtCustomerName.setText(String.valueOf(o.getSalesOrder().getCustName()));
                        txtEmail.setText(String.valueOf(o.getSalesOrder().getEmail()));
                        txtPhone.setText(String.valueOf(o.getSalesOrder().getPhone()));
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    /**
     * Metoder der udfylder lister med alle ordre og alle stockitems. De to lister
     * opdateres afhængig af hinanden. Stocklist tabellen viser de stocks der passer
     * til den valgte ordre og omvendt.
     */
    //<editor-fold defaultstate="collapsed" desc="Production Sleevelist Listener">
    private void productionSleeveListSelectioner1()
    {

        try
        {
            managerStockItem.addObserver(this);
            modelStocklist = new StockList2TableModel(managerStockItem.getAll());
            tblStockList.setModel(modelStocklist);
            
            managerOrder.addObserver(this);
            modelProduction = new ProductionSleeveTableModel(managerOrder.getAll());
            tblProductionSleeve.setModel(modelProduction);
            
            tblProductionSleeve.getSelectionModel().addListSelectionListener(new ListSelectionListener()
            {
                @Override
                public void valueChanged(ListSelectionEvent es)
                {
                    int selectedRow = tblProductionSleeve.getSelectedRow();
                    if (selectedRow == -1)
                    {
                        return;
                    }
                    Order o = modelProduction.getEventsByRow(selectedRow);
                            ord = o;
                    selectedOrderSleeve1();
//                    selectedOrderSleeve2();
                            
                    try
                    {
                        if (!managerStockItem.getItemByOrder(o).isEmpty())
                        {
                            modelStocklist = new StockList2TableModel(managerStockItem.getItemByOrder(o));
                            tblStockList.setModel(modelStocklist);
                        }
                        else
                        {
                            modelEmptyStocklist = new StockListTableModel();
                            tblStockList.setModel(modelEmptyStocklist);
                        }
                        
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            managerStockItem.addObserver(this);
            modelStocklist = new StockList2TableModel(managerStockItem.getAll());
            tblStockList.setModel(modelStocklist);
            tblStockList.getSelectionModel().addListSelectionListener(new ListSelectionListener()
            {
                @Override
                public void valueChanged(ListSelectionEvent es)
                {
                    int selectedRow = tblStockList.getSelectedRow();
                    if (selectedRow == -1)
                    {
                        return;
                    }
                    StockItem s = modelStocklist.getEventsByRow(selectedRow);
                    try
                    {
                        if (!managerOrder.getOrderByStock(s).isEmpty())
                        {
                            modelProduction = new ProductionSleeveTableModel(managerOrder.getOrderByStock(s));
                            tblProductionSleeve.setModel(modelProduction);
                        }
                        else
                        {
                            modelEmptyStocklist = new StockListTableModel();
                            tblProductionSleeve.setModel(modelEmptyStocklist);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    
    
    /**
     * Metoder der udfylder lister med alle ordre og alle stockitems. De to lister
     * opdateres afhængig af hinanden. Stocklist tabellen viser de stocks der passer
     * til den valgte ordre og omvendt.
     */
    //<editor-fold defaultstate="collapsed" desc="Production Sleevelist Listener">
    private void productionSleeveListSelectioner2()
    {

        try
        {
            managerStockItem.addObserver(this);
            modelStocklist = new StockList2TableModel(managerStockItem.getAll());
            tblStockList.setModel(modelStocklist);
            
            managerOrder.addObserver(this);
            modelProduction = new ProductionSleeveTableModel(managerOrder.getAll());
            tblOrderList.setModel(modelProduction);
            
            tblOrderList.getSelectionModel().addListSelectionListener(new ListSelectionListener()
            {
                @Override
                public void valueChanged(ListSelectionEvent es)
                {
                    int selectedRow = tblOrderList.getSelectedRow();
                    if (selectedRow == -1)
                    {
                        return;
                    }
                    Order o = modelProduction.getEventsByRow(selectedRow);
                            ord = o;
//                    selectedOrderSleeve1();
                    selectedOrderSleeve2();
                            
                    try
                    {
                        if (!managerStockItem.getItemByOrder(o).isEmpty())
                        {
                            modelStocklist = new StockList2TableModel(managerStockItem.getItemByOrder(o));
                            tblStockList.setModel(modelStocklist);
                        }
                        else
                        {
                            modelEmptyStocklist = new StockListTableModel();
                            tblStockList.setModel(modelEmptyStocklist);
                        }
                        
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            managerStockItem.addObserver(this);
            modelStocklist = new StockList2TableModel(managerStockItem.getAll());
            tblStockList.setModel(modelStocklist);
            tblStockList.getSelectionModel().addListSelectionListener(new ListSelectionListener()
            {
                @Override
                public void valueChanged(ListSelectionEvent es)
                {
                    int selectedRow = tblStockList.getSelectedRow();
                    if (selectedRow == -1)
                    {
                        return;
                    }
                    StockItem s = modelStocklist.getEventsByRow(selectedRow);
                    try
                    {
                        if (!managerOrder.getOrderByStock(s).isEmpty())
                        {
                            modelProduction = new ProductionSleeveTableModel(managerOrder.getOrderByStock(s));
                            tblOrderList.setModel(modelProduction);
                        }
                        else
                        {
                            modelEmptyStocklist = new StockListTableModel();
                            tblOrderList.setModel(modelEmptyStocklist);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    /**
     * Metode der tilføjer en mouseListener til vores Production sleeve tabel, 
     * som gør det muligt at dobbeltklikke i tabellen. Ved dobbeltklik åbner
     * Orderinfo med tre parametre fra Overview (order, sleeve og operator).
     */
    //<editor-fold defaultstate="collapsed" desc="Mouse listener til Production Sleeve tabellen">
    private void mouseListener()
    {
        tblProductionSleeve.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent me)
            {
                if (me.getClickCount() == 2)
                {
                    int selectedRow = tblProductionSleeve.getSelectedRow();
                    ord = modelProduction.getEventsByRow(selectedRow);
                    
                    new OrderInfo(ord, s, operator).setVisible(true);
                }
                
            }
        });
    }
    //</editor-fold>
    
        /**
     * Metode der tilføjer en mouseListener til vores Production sleeve tabel, 
     * som gør det muligt at dobbeltklikke i tabellen. Ved dobbeltklik åbner
     * Orderinfo med tre parametre fra Overview (order, sleeve og operator).
     */
    //<editor-fold defaultstate="collapsed" desc="Mouse listener til Production Sleeve tabellen">
    private void mouseListener2()
    {
        tblOrderList.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent me)
            {
                if (me.getClickCount() == 2)
                {
                    int selectedRow = tblOrderList.getSelectedRow();
                    ord = modelProduction.getEventsByRow(selectedRow);
                    
                    new OrderInfo(ord, s, operator).setVisible(true);
                }
                
            }
        });
    }
    //</editor-fold>
    
    /**
     * Metode der nulstiller vores stocklist og productionsleeve tabeller til standard
     * via vores getAll metode i StockItemManager og OrderManager og fjerner selection
     * i de to tabeller
     */
    //<editor-fold defaultstate="collapsed" desc="Reset Stocklist and ProductionSleeve tables">
    private void resetTables()
    {
        try
        {
            modelStocklist = new StockList2TableModel(managerStockItem.getAll());
            tblStockList.setModel(modelStocklist);
            modelProduction = new ProductionSleeveTableModel(managerOrder.getAll());
            tblProductionSleeve.setModel(modelProduction);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            tblProductionSleeve.clearSelection();
            tblStockList.clearSelection();
        }
    }
    //</editor-fold>
    
    /**
     * Metode der styrer hvornår reset knappen skal vises, afhænger af hvilken
     * tab der er valgt i vores tabbed pane
     */
    //<editor-fold defaultstate="collapsed" desc="Reset Button">
    private void resetButton()
    {
        if (tpaneOverview.getSelectedIndex() == 0)
        {
            btnReset.setVisible(true);
        }
        if (tpaneOverview.getSelectedIndex() == 1)
        {
            btnReset.setVisible(false);
        }
    }
    //</editor-fold>
    
    @Override
    public void update(Observable o, Object arg)
    {
        if (o instanceof OrderManager)
        {
            try
            {
                modelOrder.setCollection(managerOrder.getPaused());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
//    /**
//     * Metode der får fat i teksten fra tekstfeltet Errors og prøver at gemme
//     * den i den valgte ordre, og viser en besked alt efter hvordan det gik
//     */
    
    /**
     * Metode der viser en Error dialog hvis man prøver at lukke vinduet mens et
     * klip stadig er igang ellers viser den en confirmDialog, med ja og nej
     * muligheder
     */
    //<editor-fold defaultstate="collapsed" desc="Confirm dialog when closing the window">
//    private void closePressed()
//    {
//        String option = "In progress";
//        if (o.getStatus().equalsIgnoreCase(option))
//        {
//            String error = "Cut is still in progress";
//            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
//        }
//        else
//        {
//            String message = "Are you sure you want to close the window?";
//            int reply = JOptionPane.showConfirmDialog(null, message, getTitle(), JOptionPane.YES_NO_OPTION);
//            if (reply == JOptionPane.YES_OPTION)
//            {
//                dispose();
//            }
//        }
//    }//</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="Save errors comitted">
//    private void saveErrors()
//    {
//        String message = txtfErrors.getText();
//        try
//        {
//            managerOrder.updateErrorMessage(o, message);
//            o.setErrorOccured(message);
//        }
//        catch (Exception e)
//        {
//            String error = "Unable to update error message";
//            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
//        }
//        String popup = "The error message has been saved";
//        JOptionPane.showMessageDialog(this, popup, "Save successful", JOptionPane.INFORMATION_MESSAGE);
//    }
//    //</editor-fold>

     /**
     * Metode der enabler pause og finish og prøver at opdatere den valgte sleeve
     * med startTime, hvis den fejler vises en fejlmeddelelse. Hvis timeSpent
     * er tom startes en timer som vises med det valgte format. Hvis status på
     * den valgte ordre er pending eller paused ændres den til in progress hvis 
     * muligt ellers vises der en fejl.
     */
    //<editor-fold defaultstate="collapsed" desc="Start Button / Start Cut">
    private void startCut()
    {
        btnPause17.setEnabled(true);
        btnFinish18.setEnabled(true);
        
        try
        {
            txtStartTime17.setText(jodaTimeFormat.print(startTime));
            startTime = jodaTimeFormat.parseDateTime(txtStartTime17.getText());
            
            GregorianCalendar startTimeCalendar = startTime.toGregorianCalendar();
            
            sleeve.setStartTime(startTimeCalendar);
            managerSleeve.updateSleeveStartTime(sleeve);
        }
        catch (Exception e)
        {
            String message = "Unable to update sleeve with id " + sleeve.getId();
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        if (txtTimeSpent17.getText().isEmpty())
        {
            elapsedHour = 0;
            elapsedMin = 0;
            elapsedSec = 0;
            timer = new Timer(1000, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    elapsedSec++;
                    if (elapsedSec > 59)
                    {
                        elapsedSec = 0;
                        elapsedMin++;
                    }
                    if (elapsedMin > 59)
                    {
                        elapsedMin = 0;
                        elapsedHour++;
                    }
                    String displayTimer = String.format("%02d:%02d:%02d", elapsedHour, elapsedMin, elapsedSec);
                    txtTimeSpent17.setText(displayTimer);
                }
            });
            timer.setInitialDelay(0);
            timer.start();
        }
        else
        {
            timer.start();
        }
        
        String option = "Pending";
        String option2 = "Paused";
        if (ord.getStatus().equalsIgnoreCase(option) || ord.getStatus().equalsIgnoreCase(option2))
        {
            String status = "in Progress";
            ord.setStatus(status.toUpperCase());
            try
            {
                managerOrder.updateStatus(ord);
            }
            catch (Exception e)
            {
                String message = "Unable to update order";
                JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            String message = "Production Order " + ord.getOrderId() + "'s status is already: In progress.";
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //</editor-fold>
    
    /**
     * Metode der stopper timeren, sætter text field endTime til endTime og prøver
     * at opdatere endTime på det valgte sleeve. Hvis status på den valgte sleeve
     * er in progress bliver den sat til pause hvis muligt. Til sidst vil den
     * lukke vinduet
     */
    //<editor-fold defaultstate="collapsed" desc="Pause Button / Pause Cut">
    private void pauseCut()
    {
        timer.stop();
        
        try
        {
            txtEndTime17.setText(jodaTimeFormat.print(endTime));
            endTime = jodaTimeFormat.parseDateTime(txtEndTime17.getText());
            
            GregorianCalendar endTimeCalendar = endTime.toGregorianCalendar();
            
            sleeve.setEndTime(endTimeCalendar);
            managerSleeve.updateSleeveEndTime(sleeve);
        }
        catch (Exception e)
        {
            String message = "Unable to update sleeve with id " + sleeve.getId();
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        String option = "In Progress";
        if (ord.getStatus().equalsIgnoreCase(option))
        {
            String status = "Paused";
            ord.setStatus(status.toUpperCase());
            try
            {
                managerOrder.updateStatus(ord);
            }
            catch (Exception e)
            {
                String message = "Unable to update order";
                JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
         //   new SleeveInfo(o, operator).setVisible(true);
        }
        else
        {
            String message = "Production Order " + ord.getOrderId() + "'s status is not in progress or already paused.";
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        dispose();
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
        ord.setStatus(status.toUpperCase());
        int quantity = ord.getQuantity();
        ord.setConductedQuantity(quantity);
        try
        {
            managerOrder.update(ord);
            dispose();
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        dateTime1 = new org.joda.time.DateTime();
        tpaneOverview = new javax.swing.JTabbedPane();
        pnlOrder = new javax.swing.JPanel();
        pnlOrderList = new javax.swing.JPanel();
        scrOrderList = new javax.swing.JScrollPane();
        tblOrderList = new javax.swing.JTable();
        pnlOrderInfo = new javax.swing.JPanel();
        lblOrder = new javax.swing.JLabel();
        txtOrderId = new javax.swing.JTextField();
        lblQuantity = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        pnlMeasurements = new javax.swing.JPanel();
        txtThickness = new javax.swing.JTextField();
        lblThickness = new javax.swing.JLabel();
        lblWidth = new javax.swing.JLabel();
        txtWidth = new javax.swing.JTextField();
        pnlCustomerInfo = new javax.swing.JPanel();
        lblSalesOrderId = new javax.swing.JLabel();
        lblCustomerName = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblPhone = new javax.swing.JLabel();
        txtSalesOrderId = new javax.swing.JTextField();
        txtCustomerName = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        pnlProductionInformation3 = new javax.swing.JPanel();
        lblOrderName3 = new javax.swing.JLabel();
        lblOrderId3 = new javax.swing.JLabel();
        txtOrderName3 = new javax.swing.JTextField();
        txtOrderId4 = new javax.swing.JTextField();
        scrpSleeve3 = new javax.swing.JScrollPane();
        tblSleeve3 = new javax.swing.JTable();
        pnlCuttingConsole18 = new javax.swing.JPanel();
        btnStart18 = new javax.swing.JButton();
        btnPause18 = new javax.swing.JButton();
        btnFinish19 = new javax.swing.JButton();
        lblEndTime18 = new javax.swing.JLabel();
        lblTimeSpent18 = new javax.swing.JLabel();
        txtStartTime18 = new javax.swing.JTextField();
        txtEndTime18 = new javax.swing.JTextField();
        txtTimeSpent18 = new javax.swing.JTextField();
        lblStartTime18 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lblId1 = new javax.swing.JLabel();
        txtId1 = new javax.swing.JTextField();
        lblFirstName1 = new javax.swing.JLabel();
        txtName1 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        lblLastName1 = new javax.swing.JLabel();
        txtLastName1 = new javax.swing.JTextField();
        lblHasCut1 = new javax.swing.JLabel();
        txtHasCut1 = new javax.swing.JTextField();
        lblSleeves2 = new javax.swing.JLabel();
        pnlCutting2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductionSleeve = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblStockList = new javax.swing.JTable();
        lblProductionOrder = new javax.swing.JLabel();
        lblStock = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        scrpSleeve2 = new javax.swing.JScrollPane();
        tblSleeve2 = new javax.swing.JTable();
        lblLastName = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        lblHasCut = new javax.swing.JLabel();
        txtHasCut = new javax.swing.JTextField();
        lblSleeves = new javax.swing.JLabel();
        pnlProductionInformation2 = new javax.swing.JPanel();
        lblOrderName2 = new javax.swing.JLabel();
        lblOrderId2 = new javax.swing.JLabel();
        txtOrderName2 = new javax.swing.JTextField();
        txtOrderId3 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblFirstName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        pnlCuttingConsole17 = new javax.swing.JPanel();
        btnStart17 = new javax.swing.JButton();
        btnPause17 = new javax.swing.JButton();
        btnFinish18 = new javax.swing.JButton();
        lblEndTime17 = new javax.swing.JLabel();
        lblTimeSpent17 = new javax.swing.JLabel();
        txtStartTime17 = new javax.swing.JTextField();
        txtEndTime17 = new javax.swing.JTextField();
        txtTimeSpent17 = new javax.swing.JTextField();
        lblStartTime17 = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        pnlLoggedIn = new javax.swing.JPanel();
        lblLoggedIn = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        localeLanguage = new com.toedter.components.JLocaleChooser();
        cbxOperator = new javax.swing.JComboBox();
        lblChangeOperator = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        itemExit = new javax.swing.JMenuItem();
        menuSettings = new javax.swing.JMenu();
        itemHelp = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("GUI/Bundle_en_GB"); // NOI18N
        setTitle(bundle.getString("Overview.title")); // NOI18N
        setResizable(false);

        tpaneOverview.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tpaneOverviewStateChanged(evt);
            }
        });

        tblOrderList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Order List:"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scrOrderList.setViewportView(tblOrderList);

        javax.swing.GroupLayout pnlOrderListLayout = new javax.swing.GroupLayout(pnlOrderList);
        pnlOrderList.setLayout(pnlOrderListLayout);
        pnlOrderListLayout.setHorizontalGroup(
            pnlOrderListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrOrderList, javax.swing.GroupLayout.DEFAULT_SIZE, 1248, Short.MAX_VALUE)
        );
        pnlOrderListLayout.setVerticalGroup(
            pnlOrderListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOrderListLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(scrOrderList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlOrderInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), bundle.getString("Overview.pnlOrderInfo.border.title"))); // NOI18N

        lblOrder.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblOrder.setText(bundle.getString("Overview.lblOrder.text")); // NOI18N

        txtOrderId.setEditable(false);

        lblQuantity.setText(bundle.getString("Overview.lblQuantity.text")); // NOI18N

        lblDate.setText(bundle.getString("Overview.lblDate.text")); // NOI18N

        txtDate.setEditable(false);

        pnlMeasurements.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), bundle.getString("Overview.pnlMeasurements.border.title"))); // NOI18N

        txtThickness.setEditable(false);

        lblThickness.setText(bundle.getString("Overview.lblThickness.text")); // NOI18N

        lblWidth.setText(bundle.getString("Overview.lblWidth.text")); // NOI18N

        txtWidth.setEditable(false);

        javax.swing.GroupLayout pnlMeasurementsLayout = new javax.swing.GroupLayout(pnlMeasurements);
        pnlMeasurements.setLayout(pnlMeasurementsLayout);
        pnlMeasurementsLayout.setHorizontalGroup(
            pnlMeasurementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMeasurementsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMeasurementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblThickness)
                    .addComponent(lblWidth))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlMeasurementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtThickness, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                    .addComponent(txtWidth))
                .addContainerGap())
        );
        pnlMeasurementsLayout.setVerticalGroup(
            pnlMeasurementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMeasurementsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMeasurementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtThickness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblThickness))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMeasurementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWidth)
                    .addComponent(txtWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pnlCustomerInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), bundle.getString("Overview.pnlCustomerInfo.border.title"))); // NOI18N

        lblSalesOrderId.setText(bundle.getString("Overview.lblSalesOrderId.text")); // NOI18N

        lblCustomerName.setText(bundle.getString("Overview.lblCustomerName.text")); // NOI18N

        lblEmail.setText(bundle.getString("Overview.lblEmail.text")); // NOI18N

        lblPhone.setText(bundle.getString("Overview.lblPhone.text")); // NOI18N

        txtSalesOrderId.setEditable(false);

        txtCustomerName.setEditable(false);

        txtEmail.setEditable(false);

        txtPhone.setEditable(false);

        javax.swing.GroupLayout pnlCustomerInfoLayout = new javax.swing.GroupLayout(pnlCustomerInfo);
        pnlCustomerInfo.setLayout(pnlCustomerInfoLayout);
        pnlCustomerInfoLayout.setHorizontalGroup(
            pnlCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCustomerInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCustomerInfoLayout.createSequentialGroup()
                        .addComponent(lblSalesOrderId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addComponent(txtSalesOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlCustomerInfoLayout.createSequentialGroup()
                        .addGroup(pnlCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEmail)
                            .addComponent(lblCustomerName)
                            .addComponent(lblPhone))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                            .addComponent(txtCustomerName)
                            .addComponent(txtPhone))))
                .addContainerGap())
        );
        pnlCustomerInfoLayout.setVerticalGroup(
            pnlCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCustomerInfoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnlCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSalesOrderId)
                    .addComponent(txtSalesOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCustomerName)
                    .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCustomerInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhone)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText(bundle.getString("Overview.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout pnlOrderInfoLayout = new javax.swing.GroupLayout(pnlOrderInfo);
        pnlOrderInfo.setLayout(pnlOrderInfoLayout);
        pnlOrderInfoLayout.setHorizontalGroup(
            pnlOrderInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOrderInfoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlOrderInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlCustomerInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlMeasurements, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlOrderInfoLayout.createSequentialGroup()
                        .addGroup(pnlOrderInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDate)
                            .addComponent(lblOrder)
                            .addComponent(lblQuantity))
                        .addGap(18, 18, 18)
                        .addGroup(pnlOrderInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlOrderInfoLayout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addComponent(jLabel1))
                            .addComponent(txtDate, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                            .addComponent(txtOrderId))
                        .addGap(21, 21, 21)))
                .addGap(31, 31, 31))
        );
        pnlOrderInfoLayout.setVerticalGroup(
            pnlOrderInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOrderInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOrderInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOrder)
                    .addComponent(txtOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlOrderInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDate)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlOrderInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblQuantity))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlMeasurements, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlCustomerInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlProductionInformation3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), bundle.getString("Overview.pnlProductionInformation3.border.title"))); // NOI18N

        lblOrderName3.setText(bundle.getString("Overview.lblOrderName3.text")); // NOI18N

        lblOrderId3.setText(bundle.getString("Overview.lblOrderId3.text")); // NOI18N

        txtOrderName3.setEditable(false);
        txtOrderName3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOrderName3ActionPerformed(evt);
            }
        });

        txtOrderId4.setEditable(false);

        javax.swing.GroupLayout pnlProductionInformation3Layout = new javax.swing.GroupLayout(pnlProductionInformation3);
        pnlProductionInformation3.setLayout(pnlProductionInformation3Layout);
        pnlProductionInformation3Layout.setHorizontalGroup(
            pnlProductionInformation3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProductionInformation3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlProductionInformation3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOrderName3)
                    .addComponent(lblOrderId3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProductionInformation3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtOrderId4, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(txtOrderName3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlProductionInformation3Layout.setVerticalGroup(
            pnlProductionInformation3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProductionInformation3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlProductionInformation3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOrderName3)
                    .addComponent(txtOrderName3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlProductionInformation3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOrderId3)
                    .addComponent(txtOrderId4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(112, 112, 112))
        );

        tblSleeve3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrpSleeve3.setViewportView(tblSleeve3);

        pnlCuttingConsole18.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), bundle.getString("Overview.pnlCuttingConsole18.border.border.title")))); // NOI18N

        btnStart18.setText(bundle.getString("Overview.btnStart18.text")); // NOI18N
        btnStart18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStart18ActionPerformed(evt);
            }
        });

        btnPause18.setText(bundle.getString("Overview.btnPause18.text")); // NOI18N
        btnPause18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPause18ActionPerformed(evt);
            }
        });

        btnFinish19.setText(bundle.getString("Overview.btnFinish19.text")); // NOI18N
        btnFinish19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinish19ActionPerformed(evt);
            }
        });

        lblEndTime18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblEndTime18.setText(bundle.getString("Overview.lblEndTime18.text")); // NOI18N

        lblTimeSpent18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTimeSpent18.setText(bundle.getString("Overview.lblTimeSpent18.text")); // NOI18N

        txtStartTime18.setEditable(false);

        txtEndTime18.setEditable(false);

        txtTimeSpent18.setEditable(false);

        lblStartTime18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblStartTime18.setText(bundle.getString("Overview.lblStartTime18.text")); // NOI18N

        javax.swing.GroupLayout pnlCuttingConsole18Layout = new javax.swing.GroupLayout(pnlCuttingConsole18);
        pnlCuttingConsole18.setLayout(pnlCuttingConsole18Layout);
        pnlCuttingConsole18Layout.setHorizontalGroup(
            pnlCuttingConsole18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCuttingConsole18Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlCuttingConsole18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCuttingConsole18Layout.createSequentialGroup()
                        .addComponent(btnStart18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPause18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFinish19)
                        .addGap(0, 14, Short.MAX_VALUE))
                    .addGroup(pnlCuttingConsole18Layout.createSequentialGroup()
                        .addGroup(pnlCuttingConsole18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblStartTime18)
                            .addGroup(pnlCuttingConsole18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblTimeSpent18, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblEndTime18, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlCuttingConsole18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtStartTime18, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtEndTime18)
                            .addComponent(txtTimeSpent18, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(26, 26, 26))
        );
        pnlCuttingConsole18Layout.setVerticalGroup(
            pnlCuttingConsole18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCuttingConsole18Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(pnlCuttingConsole18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStart18)
                    .addComponent(btnPause18)
                    .addComponent(btnFinish19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCuttingConsole18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStartTime18)
                    .addComponent(txtStartTime18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCuttingConsole18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEndTime18)
                    .addComponent(txtEndTime18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCuttingConsole18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTimeSpent18)
                    .addComponent(txtTimeSpent18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );

        lblId1.setText(bundle.getString("Overview.lblId1.text")); // NOI18N

        txtId1.setEditable(false);

        lblFirstName1.setText(bundle.getString("Overview.lblFirstName1.text")); // NOI18N

        txtName1.setEditable(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 211, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblFirstName1)
                        .addComponent(lblId1))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtId1, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                        .addComponent(txtName1, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                    .addGap(15, 15, 15)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(6, 6, 6)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblId1)
                        .addComponent(txtId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblFirstName1)
                        .addComponent(txtName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        lblLastName1.setText(bundle.getString("Overview.lblLastName1.text")); // NOI18N

        txtLastName1.setEditable(false);
        txtLastName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLastName1ActionPerformed(evt);
            }
        });

        lblHasCut1.setText(bundle.getString("Overview.lblHasCut1.text")); // NOI18N

        txtHasCut1.setEditable(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLastName1)
                    .addComponent(lblHasCut1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtLastName1, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(txtHasCut1))
                .addGap(58, 58, 58))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLastName1)
                    .addComponent(txtLastName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHasCut1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHasCut1))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        lblSleeves2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblSleeves2.setText(bundle.getString("Overview.lblSleeves2.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(pnlProductionInformation3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrpSleeve3, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSleeves2))
                        .addGap(18, 18, 18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(pnlCuttingConsole18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlProductionInformation3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblSleeves2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrpSleeve3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(pnlCuttingConsole18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlOrderLayout = new javax.swing.GroupLayout(pnlOrder);
        pnlOrder.setLayout(pnlOrderLayout);
        pnlOrderLayout.setHorizontalGroup(
            pnlOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOrderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlOrderList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlOrderLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 929, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlOrderInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
        pnlOrderLayout.setVerticalGroup(
            pnlOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOrderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlOrderInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlOrderLayout.createSequentialGroup()
                        .addComponent(pnlOrderList, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        tpaneOverview.addTab(bundle.getString("Overview.pnlOrder.TabConstraints.tabTitle_1"), pnlOrder); // NOI18N

        tblProductionSleeve.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblProductionSleeve);

        tblStockList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblStockList);

        lblProductionOrder.setText(bundle.getString("Overview.lblProductionOrder.text")); // NOI18N

        lblStock.setText(bundle.getString("Overview.lblStock.text")); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 290, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 275, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        tblSleeve2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrpSleeve2.setViewportView(tblSleeve2);

        lblLastName.setText(bundle.getString("Overview.lblLastName.text")); // NOI18N

        txtLastName.setEditable(false);
        txtLastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLastNameActionPerformed(evt);
            }
        });

        lblHasCut.setText(bundle.getString("Overview.lblHasCut.text")); // NOI18N

        txtHasCut.setEditable(false);

        lblSleeves.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblSleeves.setText(bundle.getString("Overview.lblSleeves.text")); // NOI18N

        pnlProductionInformation2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), bundle.getString("Overview.pnlProductionInformation2.border.title"))); // NOI18N

        lblOrderName2.setText(bundle.getString("Overview.lblOrderName2.text")); // NOI18N

        lblOrderId2.setText(bundle.getString("Overview.lblOrderId2.text")); // NOI18N

        txtOrderName2.setEditable(false);

        txtOrderId3.setEditable(false);

        javax.swing.GroupLayout pnlProductionInformation2Layout = new javax.swing.GroupLayout(pnlProductionInformation2);
        pnlProductionInformation2.setLayout(pnlProductionInformation2Layout);
        pnlProductionInformation2Layout.setHorizontalGroup(
            pnlProductionInformation2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProductionInformation2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlProductionInformation2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOrderName2)
                    .addComponent(lblOrderId2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProductionInformation2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtOrderId3, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(txtOrderName2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlProductionInformation2Layout.setVerticalGroup(
            pnlProductionInformation2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProductionInformation2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlProductionInformation2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOrderName2)
                    .addComponent(txtOrderName2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlProductionInformation2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOrderId2)
                    .addComponent(txtOrderId3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(112, 112, 112))
        );

        lblId.setText(bundle.getString("Overview.lblId.text")); // NOI18N

        txtId.setEditable(false);
        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
            }
        });

        lblFirstName.setText(bundle.getString("Overview.lblFirstName.text")); // NOI18N

        txtName.setEditable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblFirstName)
                        .addComponent(lblId))
                    .addContainerGap(130, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(9, 9, 9)
                    .addComponent(lblId)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(lblFirstName)
                    .addContainerGap(16, Short.MAX_VALUE)))
        );

        pnlCuttingConsole17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), bundle.getString("Overview.pnlCuttingConsole17.border.border.title")))); // NOI18N

        btnStart17.setText(bundle.getString("Overview.btnStart17.text")); // NOI18N
        btnStart17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStart17ActionPerformed(evt);
            }
        });

        btnPause17.setText(bundle.getString("Overview.btnPause17.text")); // NOI18N
        btnPause17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPause17ActionPerformed(evt);
            }
        });

        btnFinish18.setText(bundle.getString("Overview.btnFinish18.text")); // NOI18N
        btnFinish18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinish18ActionPerformed(evt);
            }
        });

        lblEndTime17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblEndTime17.setText(bundle.getString("Overview.lblEndTime17.text")); // NOI18N

        lblTimeSpent17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTimeSpent17.setText(bundle.getString("Overview.lblTimeSpent17.text")); // NOI18N

        txtStartTime17.setEditable(false);

        txtEndTime17.setEditable(false);

        txtTimeSpent17.setEditable(false);

        lblStartTime17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblStartTime17.setText(bundle.getString("Overview.lblStartTime17.text")); // NOI18N

        javax.swing.GroupLayout pnlCuttingConsole17Layout = new javax.swing.GroupLayout(pnlCuttingConsole17);
        pnlCuttingConsole17.setLayout(pnlCuttingConsole17Layout);
        pnlCuttingConsole17Layout.setHorizontalGroup(
            pnlCuttingConsole17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCuttingConsole17Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlCuttingConsole17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCuttingConsole17Layout.createSequentialGroup()
                        .addComponent(btnStart17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPause17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFinish18)
                        .addGap(0, 14, Short.MAX_VALUE))
                    .addGroup(pnlCuttingConsole17Layout.createSequentialGroup()
                        .addGroup(pnlCuttingConsole17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblStartTime17)
                            .addGroup(pnlCuttingConsole17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblTimeSpent17, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblEndTime17, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlCuttingConsole17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtStartTime17, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtEndTime17)
                            .addComponent(txtTimeSpent17, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(26, 26, 26))
        );
        pnlCuttingConsole17Layout.setVerticalGroup(
            pnlCuttingConsole17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCuttingConsole17Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(pnlCuttingConsole17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStart17)
                    .addComponent(btnPause17)
                    .addComponent(btnFinish18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCuttingConsole17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStartTime17)
                    .addComponent(txtStartTime17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCuttingConsole17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEndTime17)
                    .addComponent(txtEndTime17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCuttingConsole17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTimeSpent17)
                    .addComponent(txtTimeSpent17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLastName)
                            .addComponent(lblHasCut))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtLastName)
                            .addComponent(txtHasCut, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(pnlProductionInformation2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSleeves)
                            .addComponent(scrpSleeve2, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(pnlCuttingConsole17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(lblSleeves)
                .addGap(3, 3, 3)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pnlProductionInformation2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scrpSleeve2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblLastName)
                                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtHasCut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblHasCut)))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(pnlCuttingConsole17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlCutting2Layout = new javax.swing.GroupLayout(pnlCutting2);
        pnlCutting2.setLayout(pnlCutting2Layout);
        pnlCutting2Layout.setHorizontalGroup(
            pnlCutting2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCutting2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCutting2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProductionOrder)
                    .addGroup(pnlCutting2Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 924, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(586, 586, 586)
                        .addGroup(pnlCutting2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlCutting2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 999, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(pnlCutting2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblStock))))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        pnlCutting2Layout.setVerticalGroup(
            pnlCutting2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCutting2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCutting2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProductionOrder)
                    .addComponent(lblStock))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCutting2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCutting2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlCutting2Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tpaneOverview.addTab(bundle.getString("Overview.pnlCutting2.TabConstraints.tabTitle"), pnlCutting2); // NOI18N

        btnClose.setText(bundle.getString("Overview.btnClose.text")); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        pnlLoggedIn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlLoggedIn.setPreferredSize(new java.awt.Dimension(245, 19));

        lblLoggedIn.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblLoggedIn.setText(bundle.getString("Overview.lblLoggedIn.text")); // NOI18N

        javax.swing.GroupLayout pnlLoggedInLayout = new javax.swing.GroupLayout(pnlLoggedIn);
        pnlLoggedIn.setLayout(pnlLoggedInLayout);
        pnlLoggedInLayout.setHorizontalGroup(
            pnlLoggedInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoggedInLayout.createSequentialGroup()
                .addComponent(lblLoggedIn)
                .addGap(0, 168, Short.MAX_VALUE))
        );
        pnlLoggedInLayout.setVerticalGroup(
            pnlLoggedInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoggedInLayout.createSequentialGroup()
                .addComponent(lblLoggedIn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnReset.setText(bundle.getString("Overview.btnReset.text")); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        localeLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                localeLanguageActionPerformed(evt);
            }
        });

        cbxOperator.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblChangeOperator.setText(bundle.getString("Overview.lblChangeOperator.text")); // NOI18N

        menuFile.setText(bundle.getString("Overview.menuFile.text")); // NOI18N

        itemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.ALT_MASK));
        itemExit.setText(bundle.getString("Overview.itemExit.text")); // NOI18N
        itemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemExitActionPerformed(evt);
            }
        });
        menuFile.add(itemExit);

        menuBar.add(menuFile);

        menuSettings.setText(bundle.getString("Overview.menuSettings.text")); // NOI18N

        itemHelp.setText(bundle.getString("Overview.itemHelp.text_1")); // NOI18N
        itemHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemHelpActionPerformed(evt);
            }
        });
        menuSettings.add(itemHelp);

        menuBar.add(menuSettings);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(tpaneOverview)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlLoggedIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblChangeOperator)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(localeLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(localeLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbxOperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblChangeOperator))
                    .addComponent(pnlLoggedIn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(tpaneOverview, javax.swing.GroupLayout.PREFERRED_SIZE, 705, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClose)
                    .addComponent(btnReset))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemExitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_itemExitActionPerformed
    {//GEN-HEADEREND:event_itemExitActionPerformed
        closePressed();
    }//GEN-LAST:event_itemExitActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCloseActionPerformed
    {//GEN-HEADEREND:event_btnCloseActionPerformed
        closePressed();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void localeLanguageActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_localeLanguageActionPerformed
    {//GEN-HEADEREND:event_localeLanguageActionPerformed
        rb = ResourceBundle.getBundle("GUI.Bundle", localeLanguage.getLocale());
        updateGUILanguage();
    }//GEN-LAST:event_localeLanguageActionPerformed

    private void tpaneOverviewStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_tpaneOverviewStateChanged
    {//GEN-HEADEREND:event_tpaneOverviewStateChanged
        resetButton();
    }//GEN-LAST:event_tpaneOverviewStateChanged

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnResetActionPerformed
    {//GEN-HEADEREND:event_btnResetActionPerformed
        resetTables();
    }//GEN-LAST:event_btnResetActionPerformed

    private void itemHelpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_itemHelpActionPerformed
    {//GEN-HEADEREND:event_itemHelpActionPerformed
        About.getInstance().setVisible(true);
    }//GEN-LAST:event_itemHelpActionPerformed

    private void btnStart17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStart17ActionPerformed
        startCut();
    }//GEN-LAST:event_btnStart17ActionPerformed

    private void btnPause17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPause17ActionPerformed
        pauseCut();
    }//GEN-LAST:event_btnPause17ActionPerformed

    private void btnFinish18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinish18ActionPerformed
        finishOrder();
    }//GEN-LAST:event_btnFinish18ActionPerformed

    private void btnStart18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStart18ActionPerformed
        startCut();
    }//GEN-LAST:event_btnStart18ActionPerformed

    private void btnPause18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPause18ActionPerformed
        pauseCut();
    }//GEN-LAST:event_btnPause18ActionPerformed

    private void btnFinish19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinish19ActionPerformed
        finishOrder();
    }//GEN-LAST:event_btnFinish19ActionPerformed

    private void txtLastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLastNameActionPerformed

    private void txtLastName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLastName1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLastName1ActionPerformed

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void txtOrderName3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrderName3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrderName3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnFinish18;
    private javax.swing.JButton btnFinish19;
    private javax.swing.JButton btnPause17;
    private javax.swing.JButton btnPause18;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnStart17;
    private javax.swing.JButton btnStart18;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbxOperator;
    private org.joda.time.DateTime dateTime1;
    private javax.swing.JMenuItem itemExit;
    private javax.swing.JMenuItem itemHelp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblChangeOperator;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEndTime17;
    private javax.swing.JLabel lblEndTime18;
    private javax.swing.JLabel lblFirstName;
    private javax.swing.JLabel lblFirstName1;
    private javax.swing.JLabel lblHasCut;
    private javax.swing.JLabel lblHasCut1;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblId1;
    private javax.swing.JLabel lblLastName;
    private javax.swing.JLabel lblLastName1;
    private javax.swing.JLabel lblLoggedIn;
    private javax.swing.JLabel lblOrder;
    private javax.swing.JLabel lblOrderId2;
    private javax.swing.JLabel lblOrderId3;
    private javax.swing.JLabel lblOrderName2;
    private javax.swing.JLabel lblOrderName3;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblProductionOrder;
    private javax.swing.JLabel lblQuantity;
    private javax.swing.JLabel lblSalesOrderId;
    private javax.swing.JLabel lblSleeves;
    private javax.swing.JLabel lblSleeves2;
    private javax.swing.JLabel lblStartTime17;
    private javax.swing.JLabel lblStartTime18;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblThickness;
    private javax.swing.JLabel lblTimeSpent17;
    private javax.swing.JLabel lblTimeSpent18;
    private javax.swing.JLabel lblWidth;
    private com.toedter.components.JLocaleChooser localeLanguage;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuSettings;
    private javax.swing.JPanel pnlCustomerInfo;
    private javax.swing.JPanel pnlCutting2;
    private javax.swing.JPanel pnlCuttingConsole17;
    private javax.swing.JPanel pnlCuttingConsole18;
    private javax.swing.JPanel pnlLoggedIn;
    private javax.swing.JPanel pnlMeasurements;
    private javax.swing.JPanel pnlOrder;
    private javax.swing.JPanel pnlOrderInfo;
    private javax.swing.JPanel pnlOrderList;
    private javax.swing.JPanel pnlProductionInformation2;
    private javax.swing.JPanel pnlProductionInformation3;
    private javax.swing.JScrollPane scrOrderList;
    private javax.swing.JScrollPane scrpSleeve2;
    private javax.swing.JScrollPane scrpSleeve3;
    private javax.swing.JTable tblOrderList;
    private javax.swing.JTable tblProductionSleeve;
    private javax.swing.JTable tblSleeve2;
    private javax.swing.JTable tblSleeve3;
    private javax.swing.JTable tblStockList;
    private javax.swing.JTabbedPane tpaneOverview;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEndTime17;
    private javax.swing.JTextField txtEndTime18;
    private javax.swing.JTextField txtHasCut;
    private javax.swing.JTextField txtHasCut1;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtId1;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtLastName1;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtName1;
    private javax.swing.JTextField txtOrderId;
    private javax.swing.JTextField txtOrderId3;
    private javax.swing.JTextField txtOrderId4;
    private javax.swing.JTextField txtOrderName2;
    private javax.swing.JTextField txtOrderName3;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtSalesOrderId;
    private javax.swing.JTextField txtStartTime17;
    private javax.swing.JTextField txtStartTime18;
    private javax.swing.JTextField txtThickness;
    private javax.swing.JTextField txtTimeSpent17;
    private javax.swing.JTextField txtTimeSpent18;
    private javax.swing.JTextField txtWidth;
    // End of variables declaration//GEN-END:variables
}
