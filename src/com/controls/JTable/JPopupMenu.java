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

    private  JMenuItem jmn_edit = new JMenuItem("editar");
    private  JMenuItem jmn_del = new JMenuItem("eliminar");
    
    public JPopupMenu() {
        add(jmn_edit);
        add(jmn_del);
        jmn_del.addActionListener(this);
        jmn_edit.addActionListener(this);
    }

    public abstract void edit();
    public abstract void delete();
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.jmn_edit){
            edit();
        }else if(e.getSource() == this.jmn_del){
            delete();
        }
    }
    
    
    
}
