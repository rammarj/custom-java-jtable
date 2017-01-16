/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controls.JTable;

import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;

/**
 *
 * @author IOAKIN
 */
public abstract class JTablePanel extends javax.swing.JPanel{

    private static final long serialVersionUID = 4427765624210639870L;
    private DefaultTableModel defaultTableModel;
    protected JTable table;
    protected JPopupMenu jpm_menu;

    public JTablePanel(Object object[][], String cols[]) {
        
        jpm_menu = new JPopupMenu() {
            private static final long serialVersionUID = 1L;
            @Override
            public void edit() {
                editar();
            }
            @Override
            public void delete() {
                borrar();
            }
        };
        
        defaultTableModel = new DefaultTableModel(object, cols);        
        table = new JTable(defaultTableModel);
        table.setComponentPopupMenu(jpm_menu);
        table.getColumnModel().getColumn(0).setMaxWidth(20);
        table.getColumnModel().getColumn(1).setMaxWidth(60);
        table.getColumnModel().getColumn(1).setMaxWidth(60);
        table.getColumnModel().getColumn(0).setResizable(false);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //table.setGridColor(HF_COLOR);
        
        table.setForeground(new java.awt.Color(0, 51, 102));
        table.setShowHorizontalLines(false);
        //table.setFont(DEFAULT_LABEL_FONT);
        JScrollPane jScrollPane2 = new JScrollPane();
        javax.swing.Box.Filler filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0));
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        jScrollPane2.setViewportView(table);
        jScrollPane2.setMinimumSize(new Dimension(300, 100));
        jScrollPane2.setPreferredSize(new Dimension(500, 200));

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(filler1)
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0)
                        .addComponent(jScrollPane2))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(filler1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)
                        .addContainerGap())
        );
        //setBackground(BACKGROUND_COLOR);
    }

    public void addRow(Object rowData[]) {
        Vector<Object> vector = new Vector<Object>();
        for (int i = 0; i < rowData.length; i++) {
            vector.add(rowData[i]);            
        }
        defaultTableModel.addRow(vector);
    }

    public void removeRow(int index) throws IndexOutOfBoundsException{
        try {
            defaultTableModel.removeRow(index);            
        } catch (Exception e) {
            throw new IndexOutOfBoundsException("No index found in table.");
        }
    }    
    
    public void removeAllRows(){
        while(defaultTableModel.getRowCount()!=0){
            defaultTableModel.removeRow(0);            
        }
    }
    
    public void addTableModelListener(TableModelListener listener){
        defaultTableModel.addTableModelListener(listener);
    }
        
    public JTable getTable() {
        return table;
    }
    
    public void addMouseListener(MouseListener ml){
        table.addMouseListener(ml);
    }
    
    
    public void setColumnSize(int col, int size){
        table.getColumnModel().getColumn(col).setMaxWidth(size);
    }
    public abstract void editar();
    public abstract void borrar();
}
