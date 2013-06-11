/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Models;

import BE.StockItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Daniel, Klaus, Mak, Rashid
 */
public class StockList2TableModel extends AbstractTableModel
{    
    private final String[] header = {"ChargeNo", "MaterialName", "Thickness", "Length", "Width", "StockQuantity"};
    private final Class[] columnTypes = {String.class, String.class, double.class, double.class, double.class, double.class};

    private ArrayList<StockItem> info;

    public StockList2TableModel(ArrayList<StockItem> allInfo)
    {
        info = allInfo;
    }

    public StockList2TableModel(List<StockItem> all)
    {
        info = (ArrayList<StockItem>) all;
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
       StockItem s = info.get(row);
       switch (col)
       {
           case 0: return s.getChargeNo();
           case 1: return s.getMaterial().getName();
           case 2: return s.getCoilType().getThickness();
           case 3: return s.getLength();
           case 4: return s.getCoilType().getWidth();
           case 5: return s.getStockQuantity();
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
        StockItem s = info.get(row);
        switch (col)
        {
           case 0: s.getChargeNo(); break;
           case 1: s.getMaterial().getName(); break;
           case 2: s.getCoilType().getThickness(); break;
           case 3: s.getLength(); break;
           case 4: s.getCoilType().getWidth(); break;
           case 5: s.getStockQuantity(); break;
        }
    }

    public StockItem getEventsByRow(int row)
    {
        return info.get(row);
    }

    public void setCollection(Collection<StockItem> stockItem)
    {
        info = new ArrayList<>(stockItem);
        fireTableDataChanged();
    }
}
