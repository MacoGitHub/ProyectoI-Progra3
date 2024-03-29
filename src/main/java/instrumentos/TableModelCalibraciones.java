package instrumentos;
import javax.swing.table.AbstractTableModel;
import java.util.List;
public class TableModelCalibraciones extends AbstractTableModel implements javax.swing.table.TableModel{
    List<Calibraciones> rowsCalibraciones;
    int[] colsCalibraciones;
    String[] colNamesCalibraciones = new String[6];
    //----------------------------------------------------------------------------------------------------------------------
    public static final int NUMERO=0;
    public static final int MEDICIONES=1;
    public static final int FECHA=2;
    //----------------------------------------------------------------------------------------------------------------------
    public int getColumnCount() {
        return colsCalibraciones.length;
    }
    public String getColumnName(int col){
        return colNamesCalibraciones[colsCalibraciones[col]];
    }
    public int getRowCount() {
        return rowsCalibraciones.size();
    }
    public Class<?> getColumnClass(int col){
        switch (colsCalibraciones[col]){
            default: return super.getColumnClass(col);
        }
    }
    public Object getValueAt(int row, int col) {
        Calibraciones sucursal = rowsCalibraciones.get(row);
        switch (colsCalibraciones[col]){
            case NUMERO: return sucursal.getNumero();
            case MEDICIONES: return sucursal.getMediciones();
            case FECHA: return sucursal.getFecha();
            default: return "";
        }
    }
    //----------------------------------------------------------------------------------------------------------------------
    public TableModelCalibraciones(int[] cols, List<Calibraciones> rows){
        this.colsCalibraciones=cols;
        this.rowsCalibraciones=rows;
        initColNames();
    }
    private void initColNames(){
        colNamesCalibraciones[NUMERO]= "Numero";
        colNamesCalibraciones[MEDICIONES]= "Mediciones";
        colNamesCalibraciones[FECHA]= "Fecha";
    }
}
