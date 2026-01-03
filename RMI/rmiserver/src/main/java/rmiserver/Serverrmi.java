/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmiserver;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Serverrmi {
    public static void main(String args[]) {
        try {
            // Khởi tạo đối tượng thực thi logic
            Calc c = new Calc();
            
            // Tạo Registry tại cổng 3456
            Registry r = LocateRegistry.createRegistry(3456);
            
            // Đăng ký đối tượng với tên định danh "rmiCalc"
            r.bind("rmiCalc", c);
            
        } catch (Exception ex) {
            // In lỗi nếu quá trình khởi tạo hoặc đăng ký thất bại
            System.out.println(ex);
        }
    }
}
