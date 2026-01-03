/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package remoteapp;

import java.io.Serializable;

public class PhanSo implements Serializable {
    int tu;
    int mau;

    // Constructor không đối số
    public PhanSo() {
        tu = 0; 
        mau = 1;
    }

    // Constructor có đối số
    public PhanSo(int t, int m) {
        tu = t;
        mau = m;
    }

    // Phương thức cộng hai phân số
    public PhanSo sum(PhanSo b) {
        PhanSo c = new PhanSo();
        c.setTu(tu * b.getMau() + b.getTu() * mau);
        c.setMau(mau * b.getMau());
        return c;
    }

    // Setter cho tử số
    public void setTu(int t) {
        tu = t;
    }

    // Setter cho mẫu số
    public void setMau(int m) {
        mau = m;
    }

    // Getter cho tử số
    public int getTu() {
        return tu;
    }

    // Getter cho mẫu số
    public int getMau() {
        return mau;
    }
}
