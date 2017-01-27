/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controls.JTable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/**
 *
 * @author IOAKIN
 */
public abstract class JPopupMenu extends javax.swing.JPopupMenu implements ActionListener{
    private static final long serialVersionUID = 8555424897561527441L;

    private  JMenuItem editItem = new JMenuItem("edit");
    private  JMenuItem deleteItem = new JMenuItem("delete");
    
    public JPopupMenu() {
        add(editItem);
        add(deleteItem);
        deleteItem.addActionListener(this);
        editItem.addActionListener(this);
    }

    public abstract void edit();
    public abstract void delete();
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.editItem){
            edit();
        }else if(e.getSource() == this.deleteItem){
            delete();
        }
    }
    
    
    
}
