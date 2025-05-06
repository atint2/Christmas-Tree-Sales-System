package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

public class TreeTableModel {
    private final SimpleStringProperty barcode;
    private final SimpleStringProperty treeType;
    private final SimpleStringProperty status;
    private final SimpleStringProperty notes;

    //----------------------------------------------------------------------------
    public TreeTableModel(Vector<String> scoutData)
    {
        barcode =  new SimpleStringProperty(scoutData.elementAt(0));
        treeType =  new SimpleStringProperty(scoutData.elementAt(1));
        notes =  new SimpleStringProperty(scoutData.elementAt(2));
        status =  new SimpleStringProperty(scoutData.elementAt(3));
    }

    //----------------------------------------------------------------------------
    public String getBarcode() {
        return barcode.get();
    }

    //----------------------------------------------------------------------------
    public String getTreeType() {
        return treeType.get();
    }

    //----------------------------------------------------------------------------
    public void setNotes(String name) {
        notes.set(name);
    }

    //----------------------------------------------------------------------------
    public String getStatus() {
        return status.get();
    }

}
