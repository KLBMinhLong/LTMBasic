package remoteapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NguoiDung implements Serializable {
    private String ten;
    private String cmnd;
    private String matKhau;
    private double soTien;
    private double laiSuat;
    private List<GiaoDich> danhSachGiaoDich;
    
    // Constructor không đối số
    public NguoiDung() {
        this.ten = "";
        this.cmnd = "";
        this.matKhau = "";
        this.soTien = 0;
        this.laiSuat = 0;
        this.danhSachGiaoDich = new ArrayList<>();
    }
    
    // Constructor có đối số
    public NguoiDung(String ten, String cmnd, String matKhau, double soTien, double laiSuat) {
        this.ten = ten;
        this.cmnd = cmnd;
        this.matKhau = matKhau;
        this.soTien = soTien;
        this.laiSuat = laiSuat;
        this.danhSachGiaoDich = new ArrayList<>();
    }
    
    // Phương thức thêm giao dịch
    public void themGiaoDich(GiaoDich giaoDich) {
        danhSachGiaoDich.add(giaoDich);
    }
    
    // Getters và Setters
    public String getTen() {
        return ten;
    }
    
    public void setTen(String ten) {
        this.ten = ten;
    }
    
    public String getCmnd() {
        return cmnd;
    }
    
    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }
    
    public String getMatKhau() {
        return matKhau;
    }
    
    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
    
    public double getSoTien() {
        return soTien;
    }
    
    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }
    
    public double getLaiSuat() {
        return laiSuat;
    }
    
    public void setLaiSuat(double laiSuat) {
        this.laiSuat = laiSuat;
    }
    
    public List<GiaoDich> getDanhSachGiaoDich() {
        return danhSachGiaoDich;
    }
    
    public void setDanhSachGiaoDich(List<GiaoDich> danhSachGiaoDich) {
        this.danhSachGiaoDich = danhSachGiaoDich;
    }
}
