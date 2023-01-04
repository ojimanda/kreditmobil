package id.kelompok7.kreditmobil;


import android.net.Uri;

public class ListElement {
    public String brand;
    public String tipe;
    public String merk;
    public String harga;
    public String image;

    public String getBrand() {
        return brand;
    }

    public String getTipe() {
        return tipe;
    }

    public String getMerk() {
        return merk;
    }

    public String getHarga() {
        return harga;
    }

    public String getImage() {
        return image;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ListElement(String merk, String brand, String tipe, String harga, String image){
        this.brand = brand;
        this.merk = merk;
        this.tipe = tipe;
        this.harga = harga;
        this.image = image;
    }
}
