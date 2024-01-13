package instrumentos;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
public class ViewCalibraciones implements Observer{
    private JPanel panel;
    private JLabel textoRojo;
    private JPanel panelMediciones;
    private JTable table1;
    private JTextField numero;
    private JTextField mediciones;
    private JTextField fecha;
    private JLabel numeroLbl;
    private JLabel medicionesLbl;
    private JLabel fechaLbl;
    private JTable tablaMedidas;
    private JButton guardar;
    private JButton limpiar;
    private JButton borrar;
    private JLabel fechaBusLbl;
    private JTextField fechaBus;
    private JButton reporte;
    private JButton buscar;
    private JTable tabla;

    ControllerCalibraciones controller;
    ModelCalibraciones model;
    //----------------------------------------------------------------------------------------------------------------------
    public ViewCalibraciones(){
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                try {
                    Calibraciones filter = new Calibraciones();
                    if (controller.getSelectedInstrumento().getTipo() == null) {
                        tabla.setVisible(false);
                        throw new Exception("NO ELIGIO UN INSTRUMENTO");
                    } else{
                        controller.setSelectedInstrumento();
                        tabla.setVisible(true);
                        tablaMedidas.setVisible(false);
                        controller.shown();
                        if (!controller.getSelectedInstrumento().getListCalibracion().isEmpty()) {
                            filter.setFecha("");
                            controller.search(filter);
                        }

                        textoRojo.setText(controller.getSelectedInstrumento().toString());
                        textoRojo.setForeground(Color.red);
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                controller.clear();
                controller.setCurrent(new Calibraciones());
                fecha.setEnabled(true);
                mediciones.setEnabled(true);

                super.componentHidden(e);
            }
        });
        panelMediciones.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) { super.componentShown(e); }
        });
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calibraciones filter = new Calibraciones();
                filter.setNumero(fechaBus.getText());
                try{
                    if(controller.getSelectedInstrumento().getTipo()==null){
                        throw new Exception("No se puede guardar");
                    }
                    if(isValid()){
                        controller.save(filter);
                        controller.CreateMeasure();
                    }
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);

                }
            }
        });
        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
                textoRojo.setText(controller.getSelectedInstrumento().toString());
            }
        });
        borrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calibraciones filter = new Calibraciones();
                try{
                    controller.delete(filter);
                    textoRojo.setText(controller.getSelectedInstrumento().toString());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        reporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.createDocument();
                    if(Desktop.isDesktopSupported()){
                        File archivo = new File("Calibraciones.pdf");
                        Desktop.getDesktop().open(archivo);
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Calibraciones filter = new Calibraciones();
                    filter.setFecha(fechaBus.getText());
                    controller.search(filter);
                    textoRojo.setText(controller.getSelectedInstrumento().toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        tablaMedidas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                controller.editarMedidas();
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
    public JPanel getPanel(){return panel;}
    public JTextField getNumero(){ return numero; }
    public JTextField getFecha(){ return fecha; }
    public JTextField getMediciones(){ return mediciones; }

    public void setController(ControllerCalibraciones controller) {
        this.controller = controller;
    }
    public void setModel(ModelCalibraciones model) {
        this.model = model;
        model.addObserver(this);
    }

    private boolean isValid(){
        boolean valid = true;
        if(numero.getText().isEmpty()){
            numeroLbl.setBackground(Color.red);
            numero.setToolTipText("Numero requerido");
            valid = false;
        } else {
            numeroLbl.setBackground(panel.getBackground());
            numero.setToolTipText(null);
        }
        if(mediciones.getText().isEmpty()){
            medicionesLbl.setBackground(Color.red);
            mediciones.setToolTipText("Medicion requerida");
            valid = false;
        } else {
            medicionesLbl.setBackground(panel.getBackground());
            mediciones.setToolTipText(null);
        }
        if(fecha.getText().isEmpty()){
            fechaLbl.setBackground(Color.red);
            fecha.setToolTipText("Fecha requerida");
            valid = false;
        } else {
            fechaLbl.setBackground(panel.getBackground());
            fecha.setToolTipText(null);
        }
        return valid;
    }
    //----------------------------------------------------------------------------------------------------------------------
    @Override
    public void update(Observable updatedModel, Object properties){
        if(controller != null) {
            int changedProps = (int) properties;
            if ((changedProps & ModelCalibraciones.LIST) == ModelCalibraciones.LIST) {
                int[] cols = {TableModelCalibraciones.NUMERO, TableModelCalibraciones.FECHA, TableModelCalibraciones.MEDICIONES};
                tabla.setModel(new TableModelCalibraciones(cols, controller.obtenerListaInstrumentos()));
                tabla.setRowHeight(30);

                TableColumnModel columnModel = tabla.getColumnModel();
                columnModel.getColumn(2).setPreferredWidth(200);
            }
            if ((changedProps & ModelCalibraciones.CURRENT) == ModelCalibraciones.CURRENT) {
                controller.crearNum();
                numero.setText(model.getCurrent().getNumero());
                mediciones.setText(String.valueOf(model.getCurrent().getMediciones()));
                fecha.setText(model.getCurrent().getFecha());

                int[] colsMed = {TableModelMediciones.MEDIDA, TableModelMediciones.REFERENCIA, TableModelMediciones.LECTURA};
                tablaMedidas.setModel(new TableModelMediciones(colsMed, controller.obtenerListaMedidas()));
                tablaMedidas.setRowHeight(10);
                TableColumnModel columnModelMed = tablaMedidas.getColumnModel();
                columnModelMed.getColumn(2).setPreferredWidth(100);
            }
            if (model.getMode() == Application.MODE_EDIT) {
                numero.setEnabled(false);
                borrar.setEnabled(true);
                panelMediciones.setVisible(true);
                tablaMedidas.setVisible(true);
            } else {
                numero.setEnabled(false);
                borrar.setEnabled(false);
                panelMediciones.setVisible(false);
                tablaMedidas.setVisible(false);
                textoRojo.setText(null);
            }
            this.panel.revalidate();
        }
    }
}
