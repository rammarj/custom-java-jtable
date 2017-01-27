/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controls.JTable;

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
    protected JTablePanel table;
    protected JPanel toDoPanel, actionsPanel, searchPanel;
    private JComboBox searchOptionCombo;
    private JLabel stadisticsInfo;
    //private JButton btn_refresh;
    protected JPanel optionsPanel;
    private JCheckBox selectCheck;
    private final String selectString = "select all", deselectString = "deselect";
    /**
     * Build a new <code>JTable</code> object with custom columns and data to show.
     * @param object the data to show in table.
     * @param cols table column names.
     */
    public JTable(Object object[][], String cols[]) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        table = new JTablePanel(object, cols) {
            private static final long serialVersionUID = 702314805164153195L;
            @Override
            public void edit() {
                edit();
            }
            @Override
            public void delete() {
                if(getSelectedRow()==-1){
                    JOptionPane.showMessageDialog(this, "No row selected","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int result = JOptionPane.showConfirmDialog(this,
                    "Are you sure deleting \""
                            +getValueAt(getSelectedRow(), 1)+"\"?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                        delete();                
                        refreshTable();
                }
            }
        };
        //panel de toDoPanel y busqueda
        toDoPanel = new JPanel(new GridLayout(1, 2));
        toDoPanel.setMaximumSize(new Dimension(100000, 20));
        //acciones.setBackground(BACKGROUND_COLOR);

        searchOptionCombo = new JComboBox(new String[]{"search at: "});
        //box.setFont(DEFAULT_COMBO_FONT);

        for (int i = 1; i < cols.length; i++) {
            Object col = cols[i];
            searchOptionCombo.addItem(col);
        }
        searchOptionCombo.addItem("all rows");
        //panel de opciones 
        optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,10));
        //pnl_options.setBackground(BACKGROUND_COLOR);

        selectCheck = new JCheckBox(selectString);
        //chbx_seleccionar.setFont(DEFAULT_LABEL_FONT);
        //chbx_seleccionar.setBackground(BACKGROUND_COLOR);
        selectCheck.addItemListener(this);

        optionsPanel.add(selectCheck);

        //panel de stadisticsPanel
        JPanel stadisticsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //estadisticas.setBackground(BACKGROUND_COLOR);
        stadisticsPanel.setMaximumSize(new Dimension(100000, 20));

        stadisticsInfo = new JLabel();
        //estadis.setFont(DEFAULT_LABEL_FONT);
        stadisticsPanel.add(stadisticsInfo);

        //panel de toDoPanel
        actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String[] items = new String[]{"Actions", "delete"};
        accion = new JComboBox<>(items);
        //accion.setFont(DEFAULT_COMBO_FONT);
        accion_submit = new JButton("apply");
//        btn_refresh = new JButton("actualizar table");
//        btn_refresh.setFont(DEFAULT_BUTTON_FONT);
        
        //accion_submit.setFont(DEFAULT_BUTTON_FONT);

        actionsPanel.add(accion);
        actionsPanel.add(accion_submit);
        //actions.add(btn_refresh);
        //panel de busqueda
        searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

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
        searchPanel.add(buscar);
        //search.add(searchOptionCombo);
        //search.add(buscar_submit);
        toDoPanel.add(actionsPanel);
        toDoPanel.add(searchPanel);

        add(stadisticsPanel);
        add(toDoPanel);
        add(table);
        add(optionsPanel);

        //setBackgroundColor(BACKGROUND_COLOR);
    }
    /**
     * Adds a row to table.
     * @param rowData the data to will be added. The number of elements in the object array must be equals to number of columns in table
     */
    public void addRow(Object rowData[]) {
        table.addRow(rowData);
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
     * @return The <code>String</code> wrote in searchPanel section
     */
    protected String getSelectedSearch() {
        return (String) searchOptionCombo.getSelectedItem();
    }
    /**
     * Method called when a 'click' has ocurred. This method can be overwriten by subclasses 
     * ,but the new method must call the original method with <code>super</code> modifier.     * 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        /*if (e.getSource() == this.buscar_submit) {
            searchPanel();
        }else 
            */
        if (e.getSource() == this.accion_submit) {
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
        for (int i = 0; i < table.getTable().getRowCount(); i++) {
            if ((boolean) table.getTable().getValueAt(i, 0) == true) {
                list.add(table.getTable().getValueAt(i, column));
            }            
        }
        return list.toArray();
    }
    /**
     * Called when a ´click´ has ocurred in the searchPanel button.
     */
    protected abstract void search();
    /**
     * Called when a ´click´ has ocurred in the Refresh button. 
     */
    public abstract void refreshTable();
    
    public void setBackgroundColor(Color c) {
        searchPanel.setBackground(c);
        toDoPanel.setBackground(c);
        actionsPanel.setBackground(c);
    }
    /**
     * Called when the ´aply´ button is clicked. 
     */
    protected abstract void aplyAction();
    /**
     * Sets the estadistic label value.
     */
    public void setEstatisticsValue(String s) {
        stadisticsInfo.setText(s);
    }
    /**
     * Gets the selected row in table.
     * @return The row index.
     */
    public int getSelectedRow() {
        return table.getTable().getSelectedRow();
    }
    /**
     * Returns the cell value at <code>row</code> and <code>column</code>.
     * <p>
     * <b>Note</b>: The column is specified in the table view's display
     *              order, and not in the <code>TableModel</code>'s column
              order.  This is an important distinction because as the
              user rearranges the columns in the table,
              the column at a given index in the view will change.
              Meanwhile the user's actionsPanel never affect the model's
              column ordering.
     *
     * @param   row             the row whose value is to be queried
     * @param   column          the column whose value is to be queried
     * @return  the Object at the specified cell
     */
    public Object getValueAt(int row, int col) {
        try {
            return table.getTable().getValueAt(row, col);
        } catch (Exception e) {
            return new Object();
        }
    }
    /**
     * Sets selected value of all checkboxes in table.
     * @param sel if value is <code>true</code> sets selected all checkboxes, else, deselects all checkboxes.  
     */
    public void setSelectedAllRows(boolean sel) {
        int rowCount = table.getTable().getRowCount();
        for (int i = 0; i < rowCount; i++) {
            table.getTable().setValueAt(sel, i, 0);
            table.getTable().repaint();
        }
    }
    /**
     * Method called when a 'change' has ocurred. This method can be overwriten by subclasses 
     * ,but the new method must call the original method with <code>super</code> modifier.     * 
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == selectCheck) {
            setSelectedAllRows(selectCheck.isSelected());
            if (selectCheck.getActionCommand() == selectString) {
                selectCheck.setText(deselectString);
            } else {
                selectCheck.setText(selectString);
            }
            selectCheck.repaint();
        }
    }
    /**
     * Revoves a row in the specified index.
     * @throws IndexOutOfBoundsException if <code>index</code> value is <= -1 or higher to size of available elements.
     */
    public void removeRow(int index){
        table.removeRow(index);
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
        return table.getTable().getRowCount();        
    }
    /**
     * Adds a listener to the list that's notified each time a change
     * to the data model occurs.
     *
     * @param   listener  the TableModelListener
     */
    public void addTableListener(TableModelListener listener){
        table.addTableModelListener(listener);
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
       table.addMouseListener(listener);
    }
    /**
     * Removes all rows of table.
     */
    public void removeAllRows(){
        table.removeAllRows();
    }   
    
    public void setColumnSize(int col, int size){
        table.setColumnSize(col, size);
    }
    
    public void edit(){
        //not implemented
    }
    
    public void delete(){
        //not implemented 
    }
    
}
