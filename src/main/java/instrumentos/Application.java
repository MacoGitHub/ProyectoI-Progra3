package instrumentos;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.ImageIcon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.awt.event.*;
public class Application {
    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");}
        catch (Exception ex) {};

        window = new JFrame();
        window.setContentPane(new JTabbedPane());
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Service.instance().stop();
            }
        });
        ViewAcercaDe acercaView= new ViewAcercaDe();

        //Tipos
        ModelTipos tiposModel= new ModelTipos();
        ViewTipos tiposView = new ViewTipos();
        tiposController = new ControllerTipos(tiposView,tiposModel);

        //Instrumentos
        ViewInstrumentos InstrumentosView = new ViewInstrumentos();
        ModelInstrumentos InstrumentosModel = new ModelInstrumentos();
        InstrumentosController = new ControllerInstrumentos(InstrumentosView, InstrumentosModel);

        //Calibraciones
        ViewCalibraciones calibracionesView = new ViewCalibraciones();
        ModelCalibraciones calibracionesModel = new ModelCalibraciones();
        calibracionesController = new ControllerCalibraciones(calibracionesModel, calibracionesView);
        calibracionesController.setController(InstrumentosController);
        window.getContentPane().add("Tipos de Instrumento",tiposView.getPanel());
        window.getContentPane().add("Instrumentos", InstrumentosView.getPanel());
        window.getContentPane().add("Calibraciones", calibracionesView.getPanel());
        window.getContentPane().add("Acerca de", acercaView.getPanel());

        window.setSize(900,450);
        window.setResizable(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setIconImage((new ImageIcon(Application.class.getResource("../icons/icon.png"))).getImage());
        window.setTitle("SILAB: Sistema de Laboratorio Industrial");
        window.setVisible(true);
    }
    public static ControllerTipos tiposController;
    public static ControllerInstrumentos InstrumentosController;
    public static ControllerCalibraciones calibracionesController;
    public static int MODE_EDIT = 2;
    public static JFrame window;
}
