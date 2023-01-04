package id.kelompok7.kreditmobil.model;

public class ModelHistory {

    public String brand;
    public String merk;
    public String dp;
    public String cicilan;

    public ModelHistory(String brand, String merk, String dp, String cicilan) {
        this.brand = brand;
        this.merk = merk;
        this.dp = dp;
        this.cicilan = cicilan;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getCicilan() {
        return cicilan;
    }

    public void setCicilan(String cicilan) {
        this.cicilan = cicilan;
    }
}
