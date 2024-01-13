package instrumentos;
import java.util.List;
import java.util.Observer;
public class ModelInstrumentos extends java.util.Observable {
    List <Instrumento> listInstrumento;
    List<Calibraciones> list;
    List<TipoInstrumento> listTypes;
    Instrumento current;
    TipoInstrumento selected;
    List<Calibraciones> calibraciones;
    int mode = 1;
    int changedProps = NONE;
    //----------------------------------------------------------------------------------------------------------------------
    public static int NONE=0;
    public static int LIST=1;
    public static int LIST_TYPE = 3;
    public static int CURRENT=2;
    public static int LIST2 = 4;
    //----------------------------------------------------------------------------------------------------------------------
    public ModelInstrumentos() {
    }
    public void init(List<Instrumento> list, List<Calibraciones> list2){
        setList2(list2);
        setList(list);
        setCurrent(new Instrumento());
    }
    public void init2(List<Calibraciones> list){}

    public List<Calibraciones> getList2() {
        return list;
    }
    public void setList2(List<Calibraciones> list){
        this.list = list;
        changedProps +=LIST2;
    }
    public List<Instrumento> getList() {
        return listInstrumento;
    }
    public List<TipoInstrumento> getListTypes(){return listTypes;}
    public void setList(List<Instrumento> list){
        this.listInstrumento = list;
        changedProps +=LIST;
    }
    public void setListType(List<TipoInstrumento> list){
        this.listTypes = list;
        changedProps +=LIST_TYPE;
    }
    public Instrumento getCurrent() {
        return current;
    }
    public TipoInstrumento getSelected() {
        return selected;
    }
    public void setSelected(TipoInstrumento selected) {
        this.selected = selected;
    }
    public void setMode(int m){
        mode = m;
    }
    public int getMode(){
        return mode;
    }
    public void setCurrent(Instrumento current) {
        changedProps +=CURRENT;
        this.current = current;
    }

    public void commit(){
        setChanged();
        notifyObservers(changedProps);
        changedProps = NONE;
    }

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
        commit();
    }
}
