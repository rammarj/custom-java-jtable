/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controls.JTable;

import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author IOAKIN
 */
public class DefaultTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -5690946625346692211L;

    private Vector<Vector<Object>> n;
    private String names[] = null;

    public DefaultTableModel(Object[][] data, String[] cols) {
        names = cols;
        this.n = new Vector<Vector<Object>>();
        for (int i = 0; i < data.length; i++) {
            Vector<Object> s = new Vector();
            for (int j = 0; j < data[i].length; j++) {
                s.add(data[i][j]);
            }
            this.n.addElement(s);
        }
    }

    @Override
    public int getColumnCount() {
        return names.length;
    }

    @Override
    public int getRowCount() {
        return this.n.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        try {
            Vector<Object> elementAt = this.n.elementAt(row);
            return elementAt.elementAt(col);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return names[column];
    }

    @Override
    public Class getColumnClass(int c) {
        try {
            return getValueAt(0, c).getClass();
        } catch (Exception e) {
            return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (getColumnClass(col) == Boolean.class || getColumnClass(col) == JComboBox.class) {
            return true;
        }
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        Vector<Object> elementAt = this.n.elementAt(row);
        elementAt.set(column, aValue);
        this.n.setElementAt(elementAt, row);
        fireTableCellUpdated(row, column);
    }

    public void addRow(Vector<Object> rowData) {
        n.add(rowData);
        fireTableDataChanged();
    }

    public void removeRow(int row){
        this.n.removeElementAt(row);      
        fireTableRowsDeleted(row, row);
    }    
    
    
}
