/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Models;

import BE.Sleeve;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Daniel, Klaus, Mak, Rashid
 */

public class SleeveTableModel extends AbstractTableModel
{   
    private final String[] header = {"Sleeve Id", "Material", "Thickness"};
    private final Class[] columnTypes = {Integer.class, String.class, double.class};

    private ArrayList<Sleeve> s;

    public SleeveTableModel(ArrayList<Sleeve> allSleeves)
    {
        s = allSleeves;
    }

    public SleeveTableModel(List<Sleeve> allSleeves)
    {
        s = (ArrayList<Sleeve>) allSleeves;
    }

    @Override
    public int getRowCount()
    {
        return s.size();
    }

    @Override
    public int getColumnCount()
    {
        return header.length;
    }

    @Override
    public Object getValueAt(int row, int col)
    {
       
       Sleeve sleeve = s.get(row);
       switch (col)
       {
           case 0 : return sleeve.getId();  
           case 1 : return sleeve.getMaterial().getName();
           case 2 : return sleeve.getThickness();
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
        Sleeve sleeve = s.get(row);
        switch (col)
        {
            case 0 : sleeve.getId(); break;
            case 1 : sleeve.getMaterial().getName(); break;
            case 2 : sleeve.getThickness(); break;
        }
    }

    public Sleeve getEventsByRow(int row)
    {
        return s.get(row);
    }

    public void setCollection(Collection<Sleeve> sleeve)
    {
        s = new ArrayList<>(sleeve);
        fireTableDataChanged();
    }
}
