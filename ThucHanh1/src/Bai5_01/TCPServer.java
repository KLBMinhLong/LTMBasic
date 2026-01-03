/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bai5_01;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author LENOVO
 */
public class TCPServer {

    static final int PORT = 1111; // Cổng mà server sẽ lắng nghe kết nối
    private ServerSocket server = null; // ServerSocket để quản lý kết nối từ client

    // Constructor khởi tạo server
    public TCPServer() {
        try {
            server = new ServerSocket(PORT); // Khởi tạo ServerSocket trên cổng 1234
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra màn hình nếu ServerSocket không thể khởi tạo
        }
    }

    // Phương thức xử lý hành động và tạo luồng cho mỗi client
    public void action() {
        Socket socket = null; // Biến để lưu kết nối từ client
        int i = 0; // Biến đếm số client kết nối tới
        System.out.println("Server Listening..."); // Thông báo server đang lắng nghe

        try {
            // Vòng lặp chờ kết nối từ client
            while ((socket = server.accept()) != null) {
                // Tạo luồng riêng để xử lý cho từng client
                new ServerThread(socket, "Client#" + i);
                System.out.printf("Thread for client %d generating...\n", i++); // Thông báo tạo luồng
            }
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi khi lắng nghe hoặc tạo kết nối
        }
    }

    // Hàm main để chạy server
    public static void main(String[] args) {
        new TCPServer().action(); // Tạo đối tượng TCPServer và gọi phương thức action
    }
}

