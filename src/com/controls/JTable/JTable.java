/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controls.JTable;

import com.controls.JTable.JTablePanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TableModelListener;
/**
 *  Contains necesary components for manage data of a database. This component contains some methos that should be ovewritten
 *  by other classes. 
 *  
 *  @see #search() 
 *  @see #refreshTable() 
 *  @see #aplyAction() 
 *  @since 1.0
 * @author IOAKIN
 */
public abstract class JTable extends JPanel implements ActionListener, ItemListener {

    private static final long serialVersionUID = -6379975727135678527L;
    
    private JComboBox<String> accion;
    private JButton accion_submit;
    private JTextField buscar;
//    private JButton buscar_submit;
    /**
     *  The <code>JTablePanel</code> used by this Component
     *  @see JTablePanel
     */
    protected JTablePanel tabla;
    protected JPanel acciones, actions, search;
    private JComboBox box;
    private JLabel estadis;
    //private JButton btn_refresh;
    protected JPanel pnl_options;
    private JCheckBox chbx_seleccionar;
    private final String seleccionar = "seleccionar todo", deseleccionar = "deseleccionar todo";
    /**
     * Build a new <code>JTable</code> object with custom columns and data to show.
     * @param object the data to show in table.
     * @param cols table column names.
     */
    public JTable(Object object[][], String cols[]) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        tabla = new JTablePanel(object, cols) {
            private static final long serialVersionUID = 702314805164153195L;
            @Override
            public void editar() {
                edit();
            }
            @Override
            public void borrar() {
                if(getSelectedRow()==-1){
                    JOptionPane.showMessageDialog(this, "No seleccionó un registro","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int showConfirmDialog = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que quiere eliminar el registro\n con numero de identificacion \""
                            +getValueAt(getSelectedRow(), 1)+"\"?", "Confirmacion", JOptionPane.YES_NO_CANCEL_OPTION);
                if (showConfirmDialog == JOptionPane.YES_OPTION) {
                        delete();                
                        refreshTable();
                }
            }
        };
        //panel de acciones y busqueda
        acciones = new JPanel(new GridLayout(1, 2));
        acciones.setMaximumSize(new Dimension(100000, 20));
        //acciones.setBackground(BACKGROUND_COLOR);

        box = new JComboBox(new String[]{"buscar en: "});
        //box.setFont(DEFAULT_COMBO_FONT);

        for (int i = 1; i < cols.length; i++) {
            Object col = cols[i];
            box.addItem(col);
        }
        box.addItem("todos los registros");
        //panel de opciones 
        pnl_options = new JPanel(new FlowLayout(FlowLayout.LEFT,5,10));
        //pnl_options.setBackground(BACKGROUND_COLOR);

        chbx_seleccionar = new JCheckBox(seleccionar);
        //chbx_seleccionar.setFont(DEFAULT_LABEL_FONT);
        //chbx_seleccionar.setBackground(BACKGROUND_COLOR);
        chbx_seleccionar.addItemListener(this);

        pnl_options.add(chbx_seleccionar);

        //panel de estadisticas
        JPanel estadisticas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //estadisticas.setBackground(BACKGROUND_COLOR);
        estadisticas.setMaximumSize(new Dimension(100000, 20));

        estadis = new JLabel();
        //estadis.setFont(DEFAULT_LABEL_FONT);
        estadisticas.add(estadis);

        //panel de acciones
        actions = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String[] items = new String[]{"acciones en lote", "borrar"};
        accion = new JComboBox<>(items);
        //accion.setFont(DEFAULT_COMBO_FONT);
        accion_submit = new JButton("aplicar");
//        btn_refresh = new JButton("actualizar tabla");
//        btn_refresh.setFont(DEFAULT_BUTTON_FONT);
        
        //accion_submit.setFont(DEFAULT_BUTTON_FONT);

        actions.add(accion);
        actions.add(accion_submit);
        //actions.add(btn_refresh);
        //panel de busqueda
        search = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        buscar = new JTextField(10);
        //buscar.setFont(DEFAULT_LABEL_FONT);
        buscar.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (!e.isActionKey())
                    search();
            }
            
        });
//
//        buscar_submit = new JButton("buscar");
//        buscar_submit.setIcon(SEARCH_ICON);
//        buscar_submit.setFont(DEFAULT_BUTTON_FONT);

        //ads de listeners
//        buscar_submit.addActionListener(this);
        accion_submit.addActionListener(this);
        //btn_refresh.addActionListener(this);

        //search.add(new JLabel(Constants.SEARCH_ICON));
        search.add(buscar);
        //search.add(box);
        //search.add(buscar_submit);
        acciones.add(actions);
        acciones.add(search);

        add(estadisticas);
        add(acciones);
        add(tabla);
        add(pnl_options);

        //setBackgroundColor(BACKGROUND_COLOR);
    }
    /**
     * Adds a row to table.
     * @param rowData the data to will be added. The number of elements in the object array must be equals to number of columns in table
     */
    public void addRow(Object rowData[]) {
        tabla.addRow(rowData);
    }
    /**
     * Gets the action selected in the Combobox.
     * @return The action selected as <code>String</code>
     */
    protected String getSelectedAction() {
        return (String) accion.getSelectedItem();
    }
    /**
     * Gets the searched <code>String</code>
     * @return The <code>String</code> wrote in search section
     */
    protected String getSelectedSearch() {
        return (String) box.getSelectedItem();
    }
    /**
     * Method called when a 'click' has ocurred. This method can be overwriten by subclasses 
     * ,but the new method must call the original method with <code>super</code> modifier.     * 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        /*if (e.getSource() == this.buscar_submit) {
            search();
        }else 
            */if (e.getSource() == this.accion_submit) {
            aplyAction();
        }/*else if(e.getSource() == this.btn_refresh){
            refreshTable();
        }*/
    }
    /**
     * Gets a <code>Object</code> array with selected values in the especified column.
     *  @return The array with selected values in the especified column
     */
    public Object[] getSelectedValues(int column){
        LinkedList<Object> list = new LinkedList<>();
        for (int i = 0; i < tabla.getTable().getRowCount(); i++) {
            if ((boolean) tabla.getTable().getValueAt(i, 0) == true) {
                list.add(tabla.getTable().getValueAt(i, column));
            }            
        }
        return list.toArray();
    }
    /**
     * Called when a ´click´ has ocurred in the search button.
     */
    protected abstract void search();
    /**
     * Called when a ´click´ has ocurred in the Refresh button. 
     */
    public abstract void refreshTable();
    
    public void setBackgroundColor(Color c) {
        search.setBackground(c);
        acciones.setBackground(c);
        actions.setBackground(c);
    }
    /**
     * Called when the ´aply´ button is clicked. 
     */
    protected abstract void aplyAction();
    /**
     * Sets the estadistic label value.
     */
    public void setEstatisticsValue(String s) {
        estadis.setText(s);
    }
    /**
     * Gets the selected row in table.
     * @return The row index.
     */
    public int getSelectedRow() {
        return tabla.getTable().getSelectedRow();
    }
    /**
     * Returns the cell value at <code>row</code> and <code>column</code>.
     * <p>
     * <b>Note</b>: The column is specified in the table view's display
     *              order, and not in the <code>TableModel</code>'s column
     *              order.  This is an important distinction because as the
     *              user rearranges the columns in the table,
     *              the column at a given index in the view will change.
     *              Meanwhile the user's actions never affect the model's
     *              column ordering.
     *
     * @param   row             the row whose value is to be queried
     * @param   column          the column whose value is to be queried
     * @return  the Object at the specified cell
     */
    public Object getValueAt(int row, int col) {
        try {
            return tabla.getTable().getValueAt(row, col);
        } catch (Exception e) {
            return new Object();
        }
    }
    /**
     * Sets selected value of all checkboxes in table.
     * @param sel if value is <code>true</code> sets selected all checkboxes, else, deselects all checkboxes.  
     */
    public void setSelectedAllRows(boolean sel) {
        int rowCount = tabla.getTable().getRowCount();
        for (int i = 0; i < rowCount; i++) {
            tabla.getTable().setValueAt(sel, i, 0);
            tabla.getTable().repaint();
        }
    }
    /**
     * Method called when a 'change' has ocurred. This method can be overwriten by subclasses 
     * ,but the new method must call the original method with <code>super</code> modifier.     * 
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == chbx_seleccionar) {
            setSelectedAllRows(chbx_seleccionar.isSelected());
            if (chbx_seleccionar.getActionCommand() == seleccionar) {
                chbx_seleccionar.setText(deseleccionar);
            } else {
                chbx_seleccionar.setText(seleccionar);
            }
            chbx_seleccionar.repaint();
        }
    }
    /**
     * Revoves a row in the specified index.
     * @throws IndexOutOfBoundsException if <code>index</code> value is <= -1 or higher to size of available elements.
     */
    public void removeRow(int index){
        tabla.removeRow(index);
    }
    /**
     * Returns the number of rows that can be shown in the
     * <code>JTable</code>, given unlimited space.  If a
     * <code>RowSorter</code> with a filter has been specified, the
     * number of rows returned may differ from that of the underlying
     * <code>TableModel</code>.
     *
     * @return the number of rows shown in the <code>JTable</code>
     */
    public int getRowCount(){
        return tabla.getTable().getRowCount();        
    }
    /**
     * Adds a listener to the list that's notified each time a change
     * to the data model occurs.
     *
     * @param   listener  the TableModelListener
     */
    public void addTableListener(TableModelListener listener){
        tabla.addTableModelListener(listener);
    }
    /**
     * Adds the specified mouse listener to receive mouse events from
     * this component.
     * If listener <code>l</code> is <code>null</code>,
     * no exception is thrown and no action is performed.
     * <p>Refer to <a href="doc-files/AWTThreadIssues.html#ListenersThreads"
     * >AWT Threading Issues</a> for details on AWT's threading model.
     *
     * @param    listener    the mouse listener
     * @see      java.awt.event.MouseEvent
     * @see      java.awt.event.MouseListener
     * @see      #removeMouseListener
     * @see      #getMouseListeners
     * @since    JDK1.1
     */
    @Override
    public void addMouseListener(MouseListener listener){
       tabla.addMouseListener(listener);
    }
    /**
     * Removes all rows of table.
     */
    public void removeAllRows(){
        tabla.removeAllRows();
    }   
    
    public void setColumnSize(int col, int size){
        tabla.setColumnSize(col, size);
    }
    
    public void edit(){
        //not implemented yet
    }
    
    public void delete(){
        //not implemented yet
    }
    
}
