/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bai4;

import java.io.File;
import java.io.FileReader;
import javax.swing.JTextField;

/**
 *
 * @author Admin
 */
public class FileTReader extends Thread{ 
    private String strElement;
    private String path;
    private JTextField txtResult;

    public FileTReader(String path, JTextField txtResult) { 
        this.path = path;
        this.txtResult = txtResult;
    }

    public void readFile() {
        try {
            File file = new File(path);
            // Lỗi tiềm ẩn: Tên lớp trong ảnh là FileTReader,
            // nhưng dòng dưới đây sử dụng java.io.FileReader (tôi giả định)
            // Tuy nhiên, để chép lại y hệt:
            FileReader fr = new FileReader(file); 
            StringBuffer sb = new StringBuffer();
            char ca[] = new char[5];
            while (fr.ready()) {
                int len = fr.read(ca);
                sb.append(ca,0,len);
                strElement = sb+"";
            }
            txtResult.setText(strElement);
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        readFile();
        System.out.print(strElement+" ");
    }
}
