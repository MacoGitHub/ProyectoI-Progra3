package instrumentos;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.Desktop;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
public class ViewTipos implements Observer{
    private JPanel panel;
    private JLabel codigoLbl;
    private JTextField codigo;
    private JLabel nombreLbl;
    private JTextField nombre;
    private JLabel unidadLbl;
    private JTextField unidad;
    private JButton guardar;
    private JButton limpiar;
    private JButton borrar;
    private JLabel nombreBusLbl;
    private JTextField nombreBus;
    private JButton reporte;
    private JButton buscarBoton;
    private JTable tabla;
    //----------------------------------------------------------------------------------------------------------------------
    ControllerTipos controller;
    ModelTipos model;
    //----------------------------------------------------------------------------------------------------------------------
    public ViewTipos(){
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TipoInstrumento filter = new TipoInstrumento();
                filter.setNombre(buscarBoton.getText());
                try {
                    if (isValid()) {
                        controller.save(filter);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { controller.clear(); }
        });
        borrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TipoInstrumento filter= new TipoInstrumento();
                try {
                    controller.delete(filter);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        reporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.createDocument();
                    if(Desktop.isDesktopSupported()){
                        File archivo = new File("Tipos.pdf");
                        Desktop.getDesktop().open(archivo);
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buscarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TipoInstrumento filter= new TipoInstrumento();
                    filter.setNombre(nombreBus.getText());
                    controller.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabla.getSelectedRow();
                controller.edit(row);
                isValid();
            }
        });
    }
    public JPanel getPanel() {
        return panel;
    }
    public JTextField getCodigo(){return codigo;}
    public JTextField getNombre(){return nombre;}
    public JTextField getUnidad(){return unidad;}
    public void setController(ControllerTipos controller) {
        this.controller = controller;
    }
    public void setModel(ModelTipos model) {
        this.model = model;
        model.addObserver(this);
    }
    private boolean isValid(){
        boolean valid = true;
        if(codigo.getText().isEmpty()){
            codigoLbl.setBackground(Color.red);
            codigo.setToolTipText("Codigo requerido");
            valid = false;
        }else{
            codigoLbl.setBackground(panel.getBackground());
            codigo.setToolTipText(null);
        }
        if(nombre.getText().isEmpty()){
            nombreLbl.setBackground(Color.red);
            nombre.setToolTipText("Nombre requerido");
            valid = false;
        }else{
            nombreLbl.setBackground(panel.getBackground());
            nombre.setToolTipText(null);
        }
        if(unidad.getText().isEmpty()){
            unidadLbl.setBackground(Color.red);
            unidad.setToolTipText("Unidad requerida");
            valid = false;
        }else{
            unidadLbl.setBackground(panel.getBackground());
            unidad.setToolTipText(null);
        }
        return valid;
    }
    //----------------------------------------------------------------------------------------------------------------------
    @Override
    public void update(Observable updatedModel, Object properties) {
        int changedProps = (int) properties;
        if ((changedProps & ModelTipos.LIST) == ModelTipos.LIST) {
            int[] cols = {TableModelTipos.CODIGO, TableModelTipos.NOMBRE, TableModelTipos.UNIDAD};
            tabla.setModel(new TableModelTipos(cols, model.getList()));
            tabla.setRowHeight(30);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(2).setPreferredWidth(200);
        }
        if ((changedProps & ModelTipos.CURRENT) == ModelTipos.CURRENT) {
            codigo.setText(model.getCurrent().getCodigo());
            nombre.setText(model.getCurrent().getNombre());
            unidad.setText(model.getCurrent().getUnidad());
        }
        if(model.getMode() == Application.MODE_EDIT){
            codigo.setEnabled(false);
            borrar.setEnabled(true);
        } else {
            codigo.setEnabled(true);
            borrar.setEnabled(false);
        }
        this.panel.revalidate();
    }
}
