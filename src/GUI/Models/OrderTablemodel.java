/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Models;

import BE.Order;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Daniel, Klaus, Mak, Rashid
 */

public class OrderTablemodel extends AbstractTableModel
{
    private final String[] header = {"Order", "DueDate", "MaterialName", "Circumference", "Width", "Quantity", "Cut", "Status", "Urgent"};
    private final Class[] columnTypes = {Integer.class, String.class, String.class, double.class, double.class, Integer.class, Integer.class, String.class, Boolean.class};
    
    private ArrayList<Order> info;

    public OrderTablemodel(ArrayList<Order> allInfo)
    {
        info = allInfo;
    }

    public OrderTablemodel(List<Order> all)
    {
        info = (ArrayList<Order>) all;
    }

    @Override
    public int getRowCount()
    {
        return info.size();
    }

    @Override
    public int getColumnCount()
    {
        return header.length;
    }

    @Override
    public Object getValueAt(int row, int col)
    {
       
       Order o = info.get(row);
       switch (col)
       {
           case 0 : return o.getOrderName();
           case 1 : return o.printDate(o.getDueDate());
           case 2 : return o.getSleeve().getMaterial().getName();
           case 3 : return o.getSleeve().getCircumference();
           case 4 : return o.getWidth();
           case 5 : return o.getQuantity();
           case 6 : return o.getConductedQuantity();
           case 7 : return o.getStatus();  
           case 8 : return o.isUrgent();               
       }
       return null;
    }

    @Override
    public String getColumnName(int col)
    {
        return header[col];
    }

    @Override
    public Class<?> getColumnClass(int col)
    {
        return columnTypes[col];
    }

    @Override
    public boolean isCellEditable(int row, int col)
    {
        return false;
    }

    @Override
    public void setValueAt(Object o, int row, int col)
    {
        Order or = info.get(row);
        switch (col)
        {
           case 0 : or.getOrderName(); break;
           case 1 : or.getDueDate(); break;
           case 2 : or.getSleeve().getMaterial().getName(); break;
           case 3 : or.getSleeve().getCircumference(); break;
           case 4 : or.getWidth(); break;
           case 5 : or.getQuantity(); break;
           case 6 : or.getConductedQuantity(); break;
           case 7 : or.getStatus(); break;
           case 8 : or.isUrgent(); break;
        }
    }

    public Order getEventsByRow(int row)
    {
        return info.get(row);
    }
     
    public void setCollection(Collection<Order> order)
    {
        info = new ArrayList<>(order);
        fireTableDataChanged();
    }
}
