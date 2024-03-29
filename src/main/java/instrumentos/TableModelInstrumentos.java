package instrumentos;
import javax.swing.table.AbstractTableModel;
import java.util.List;
public class TableModelInstrumentos extends AbstractTableModel implements javax.swing.table.TableModel {
    List<Instrumento> rows;
    int[] cols;
    String[] colNames = new String[7];
    //----------------------------------------------------------------------------------------------------------------------
    public int getColumnCount() {
        return cols.length;
    }
    public String getColumnName(int col){
        return colNames[cols[col]];
    }
    public Class<?> getColumnClass(int col){
        switch (cols[col]){
            default: return super.getColumnClass(col);
        }
    }
    public int getRowCount() {
        return rows.size();
    }
    public Object getValueAt(int row, int col) {
        Instrumento instru = rows.get(row);
        switch (cols[col]){
            case SERIE: return instru.getSerie();
            case DESCRIPCION: return instru.getDescripcion();
            case MINIMO: return instru.getMinimo();
            case MAXIMO: return instru.getMaximo();
            case TOLERANCIA: return instru.getTolerancia();
            case TIPO: return instru.getTipo();
            default: return "";
        }
    }
    //----------------------------------------------------------------------------------------------------------------------
    public static final int SERIE=0;
    public static final int DESCRIPCION=1;
    public static final int MINIMO=2;
    public static final int MAXIMO=3;
    public static final int TOLERANCIA=4;
    public static final int TIPO=5;
    //----------------------------------------------------------------------------------------------------------------------
    public TableModelInstrumentos(int[] cols, List<Instrumento> rows){
        this.cols=cols;
        this.rows=rows;
        initColNames();
    }
    private void initColNames(){
        colNames[SERIE]= "No. Serie";
        colNames[DESCRIPCION]= "Descripción";
        colNames[MINIMO]= "Mínimo";
        colNames[MAXIMO]= "Máximo";
        colNames[TOLERANCIA]= "Tolerancia";
    }
}
