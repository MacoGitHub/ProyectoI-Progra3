package instrumentos;
import java.util.List;
import java.util.Observer;
public class ModelCalibraciones extends java.util.Observable{
    List<Calibraciones> list;
    Calibraciones current;
    Instrumento selected;
    int mode = 1; // 1 = No editado.
    int changedProps = NONE;
    //----------------------------------------------------------------------------------------------------------------------
    public static int NONE=0;
    public static int LIST=1;
    public static int CURRENT=2;
    //----------------------------------------------------------------------------------------------------------------------
    public ModelCalibraciones() {
    }
    public void setCurrent(Calibraciones current) {
        changedProps +=CURRENT;
        this.current = current;
    }
    public Calibraciones getCurrent() {
        return current;
    }
    public Instrumento getSelected() {
        return selected;
    }
    public void setSelected(Instrumento selected) {
        this.selected = selected;
    }
    public void setMode(int m){
        mode = m;
    }
    public int getMode(){
        return mode;
    }
    public List<Calibraciones> getList() {
        return list;
    }
    public void setProps(){
        changedProps += LIST;
    }
    public void crearNumeros(){
        if(mode!=1){
            int i=1;
            for(Calibraciones cal : selected.getListCalibracion()) {
                cal.setNumero(String.valueOf(i));
                i++;
            }
        } else { current.setNumero("0"); }
    }
    public Calibraciones getNext() {
        if (current!=null&&list!=null){
            int currentyIndex = list.indexOf(current);
            if(currentyIndex >= 0 && currentyIndex < list.size()-1){
                return list.get(currentyIndex+1);
            }
        }
        return null;
    }
    public void commit(){
        setChanged();
        notifyObservers(changedProps);
        changedProps = NONE;
    }
    //----------------------------------------------------------------------------------------------------------------------
    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
        commit();
    }
}
