package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class isletme {


    private final StringProperty name;
    private final StringProperty tarih;
    private final StringProperty urun;
    private final StringProperty urun_kdv;
    private final StringProperty urun_fiyat;
    private final StringProperty fiyat;
    private final StringProperty fis;



    public isletme(String name,String tarih,String urun ,String fiyat, String fis,String urun_fiyat,String urun_kdv){

        this.name  = new SimpleStringProperty(name);
        this.tarih = new SimpleStringProperty(tarih);
        this.urun  = new SimpleStringProperty(urun);
        this.fiyat = new SimpleStringProperty(fiyat);
        this.fis   = new SimpleStringProperty(fis);
        this.urun_fiyat=new SimpleStringProperty(urun_fiyat);
        this.urun_kdv=new SimpleStringProperty(urun_kdv);

    }
    //getter
    public String getName() {
        return name.get();
    }

    public String getFis() {
        return fis.get();
    }

    public String getFiyat() {
        return fiyat.get();
    }

    public String getTarih() {
        return tarih.get();
    }

    public String getUrun() {
        return urun.get();
    } public String getUrunkdv() {
        return urun_kdv.get();
    }public String getUrunfiyat() {
        return urun_fiyat.get();
    }

    //setter
    public void  setName(String value) {
        name.set(value);
    }
    public void setTarih(String value){
        tarih.set(value);
    }
    public void setUrun(String urun) {
        this.urun.set(urun);
    }

    public void setFiyat(String fiyat) {
        this.fiyat.set(fiyat);
    }
    public void setFis(String fis) {
        this.fis.set(fis);
    }
    public void setUrunkdv(String urunkdv) {
        this.urun_kdv.set(urunkdv);
    }public void setUrunfiyat(String urunfiyat) {
        this.urun_fiyat.set(urunfiyat);
    }

    //Prpperty
    public StringProperty nameProperty() {
        return name;
    }
    public StringProperty fisProperty() {
        return fis;
    }
    public StringProperty tarihProperty() {
        return tarih;
    }
    public StringProperty urunProperty() {
        return urun;
    }
    public StringProperty fiyatProperty() {
        return fiyat;
    }public StringProperty urun_kdvProperty() {
        return urun_kdv;
    }
    public StringProperty urun_fiyatProperty() {
        return urun_fiyat;
    }
}