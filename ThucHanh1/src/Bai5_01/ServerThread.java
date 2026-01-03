/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bai5_01;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author LENOVO
 */
public class ServerThread implements Runnable {
    private Scanner in = null;           // Đối tượng để đọc dữ liệu từ client
    private PrintWriter out = null;      // Đối tượng để gửi dữ liệu tới client
    private Socket socket;               // Socket kết nối tới client
    private String name;                 // Tên của client (được đặt khi tạo thread)

    public ServerThread(Socket socket, String name) throws IOException {
        this.socket = socket;            // Lưu socket của client
        this.name = name;                // Lưu tên của client
        this.in = new Scanner(this.socket.getInputStream());  // Tạo Scanner để đọc dữ liệu từ client
        this.out = new PrintWriter(this.socket.getOutputStream(), true); // Tạo PrintWriter để gửi dữ liệu tới client
        new Thread(this::run).start();   // Khởi động một luồng mới để xử lý client
    }

    public void run() {
        try {
            while (true) { // Vòng lặp xử lý dữ liệu từ client
                String chuoi = in.nextLine();       // Đọc chuỗi từ client
                chuoi = chuoi.replaceAll("\\s+", ""); // Xóa toàn bộ khoảng trắng
                chuoi = chuoi.toUpperCase();          // Chuyển chuỗi thành chữ in hoa
                out.println(chuoi);                   // Gửi chuỗi đã xử lý trở lại client
            }
        } catch (Exception e) {
            System.out.println(name + " has departed"); // Thông báo khi client ngắt kết nối
        } finally {
            try {
                socket.close(); // Đóng kết nối với client
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
