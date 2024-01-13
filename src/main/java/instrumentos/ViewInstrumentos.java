package instrumentos;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumnModel;
import java.awt.Desktop;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
public class ViewInstrumentos implements Observer{
    private JPanel panel;
    private JLabel serieLbl;
    private JTextField serie;
    private JLabel descripcionLbl;
    private JTextField descripcion;
    private JTextField maximo;
    private JLabel minimoLbl;
    private JTextField minimo;
    private JLabel toleranciaLbl;
    private JTextField tolerancia;
    private JLabel maximoLbl;
    private JLabel tipoLbl;
    private JButton guardar;
    private JButton borrar;
    private JButton limpiar;
    private JLabel descripcionBusLbl;
    private JTextField descripcionBus;
    private JButton reporte;
    private JButton buscar;
    private JTable tabla;
    private JComboBox tipo;
    //----------------------------------------------------------------------------------------------------------------------
    ControllerInstrumentos controller;
    ModelInstrumentos model;
    //----------------------------------------------------------------------------------------------------------------------
    public ViewInstrumentos(){
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Instrumento filter = new Instrumento();
                filter.setDescripcion(descripcionBus.getText());
                try {
                    if (isValid()) {
                        controller.save(filter);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        borrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Instrumento filter= new Instrumento();
                try {
                    controller.delete(filter);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { controller.clear(); }
        });
        reporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(Desktop.isDesktopSupported()){
                        controller.createDocument();
                        File archivo = new File("Instrumentos.pdf");
                        Desktop.getDesktop().open(archivo);
                    }
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Instrumento filter= new Instrumento();
                    filter.setDescripcion(descripcionBus.getText());
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
                tipo.setSelectedItem(controller.getSelected());
                isValid();
            }
        });
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                try {
                    controller.shown();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                actualizarComboBox();
            }
        });
    }
    public JPanel getPanel() {
        return panel;
    }
    public JTextField getSerie(){return serie;}
    public JTextField getDescripcion(){return descripcion;}
    public JTextField getMinimo(){return minimo;}
    public JTextField getMaximo(){return maximo;}
    public JTextField getTolerancia(){return tolerancia;}
    public TipoInstrumento getTipo(){return (TipoInstrumento) tipo.getSelectedItem();}
    public void setController(ControllerInstrumentos controller) {
        this.controller = controller;
    }
    public void setModel(ModelInstrumentos model) {
        this.model = model;
        model.addObserver(this);
    }

    private boolean isValid(){
        boolean valid = true;
        if(serie.getText().isEmpty()){
            serieLbl.setBackground(Color.red);
            serie.setToolTipText("Serie requerida");
            valid = false;
        }else{
            serieLbl.setBackground(panel.getBackground());
            serie.setToolTipText(null);
        }
        if(descripcion.getText().isEmpty()){
            descripcionLbl.setBackground(Color.red);
            descripcion.setToolTipText("Descripcion requerida");
            valid = false;
        }else{
            descripcionLbl.setBackground(panel.getBackground());
            descripcion.setToolTipText(null);
        }
        if(maximo.getText().isEmpty()){
            maximoLbl.setBackground(Color.red);
            maximo.setToolTipText("Máximo requerido");
            valid = false;
        }else{
            maximoLbl.setBackground(panel.getBackground());
            maximo.setToolTipText(null);
        }
        if(minimo.getText().isEmpty()){
            minimoLbl.setBackground(Color.red);
            minimo.setToolTipText("Mínimo requerido");
            valid = false;
        }else{
            minimoLbl.setBackground(panel.getBackground());
            minimo.setToolTipText(null);
        }
        if(tolerancia.getText().isEmpty()){
            toleranciaLbl.setBackground(Color.red);
            tolerancia.setToolTipText("Tolerancia requerida");
            valid = false;
        }else{
            toleranciaLbl.setBackground(panel.getBackground());
            tolerancia.setToolTipText(null);
        }
        return valid;
    }
    private void actualizarComboBox(){
        List<TipoInstrumento> tipos = controller.getTiposInstrumentos();
        tipo.removeAllItems();
        for(TipoInstrumento tip : tipos){
            tipo.addItem(tip);
        }
    }
    //----------------------------------------------------------------------------------------------------------------------
    @Override
    public void update(Observable updatedModel, Object properties) {
        int changedProps = (int) properties;
        if ((changedProps & ModelInstrumentos.LIST) == ModelInstrumentos.LIST) {
            int[] cols = {TableModelInstrumentos.SERIE, TableModelInstrumentos.DESCRIPCION, TableModelInstrumentos.MINIMO, TableModelInstrumentos.MAXIMO, TableModelInstrumentos.TOLERANCIA};
            tabla.setModel(new TableModelInstrumentos(cols, model.getList()));
            tabla.setRowHeight(30);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(4).setPreferredWidth(200);
        }
        if ((changedProps & ModelInstrumentos.CURRENT) == ModelInstrumentos.CURRENT) {
            serie.setText(model.getCurrent().getSerie());
            descripcion.setText(model.getCurrent().getDescripcion());
            minimo.setText(String.valueOf(model.getCurrent().getMinimo()));
            maximo.setText(String.valueOf(model.getCurrent().getMaximo()));
            tolerancia.setText(String.valueOf(model.getCurrent().getTolerancia()));
            TipoInstrumento currentTipo = model.getCurrent().getTipo();
            if (currentTipo != null) {
                tipo.setSelectedItem(currentTipo.getNombre());
            } else {
                tipo.setSelectedItem(controller.getTiposInstrumentos());
            }
        }
        if(model.getMode() == Application.MODE_EDIT){
            serie.setEnabled(false);
            borrar.setEnabled(true);
        } else {
            serie.setEnabled(true);
            borrar.setEnabled(false);
        }
        this.panel.revalidate();
    }
}
