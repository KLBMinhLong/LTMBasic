package remoteapp;

import java.io.Serializable;
import java.util.Date;

public class GiaoDich implements Serializable {
    private Date ngayGiaoDich;
    private double soTien;
    private String kieuGiaoDich; // "RUT_TIEN" hoặc "GUI_TIEN"
    
    // Constructor không đối số
    public GiaoDich() {
        this.ngayGiaoDich = new Date();
        this.soTien = 0;
        this.kieuGiaoDich = "";
    }
    
    // Constructor có đối số
    public GiaoDich(double soTien, String kieuGiaoDich) {
        this.ngayGiaoDich = new Date();
        this.soTien = soTien;
        this.kieuGiaoDich = kieuGiaoDich;
    }
    
    // Getters và Setters
    public Date getNgayGiaoDich() {
        return ngayGiaoDich;
    }
    
    public void setNgayGiaoDich(Date ngayGiaoDich) {
        this.ngayGiaoDich = ngayGiaoDich;
    }
    
    public double getSoTien() {
        return soTien;
    }
    
    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }
    
    public String getKieuGiaoDich() {
        return kieuGiaoDich;
    }
    
    public void setKieuGiaoDich(String kieuGiaoDich) {
        this.kieuGiaoDich = kieuGiaoDich;
    }
    
    @Override
    public String toString() {
        return ngayGiaoDich + " - " + kieuGiaoDich + ": " + soTien + " VND";
    }
}
