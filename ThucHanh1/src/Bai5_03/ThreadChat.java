/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bai5_03;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Admin
 */
public class ThreadChat implements Runnable {
    private Scanner in = null;
    private Socket socket = null;
    public frmClient3 chat = null; // Tham chiếu tới form giao diện chính
    ServerSocket server = null;

    public ThreadChat() {
        try {
            // Tạo ServerSocket lắng nghe tại cổng 1234
            server = new ServerSocket(3333);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Khởi động luồng để nhận tin nhắn song song với giao diện
        new Thread(this).start();
    }

    public void run() {
        try {
            while (true) {
                // Chấp nhận kết nối từ máy khác đến
                while ((socket = server.accept()) != null) {
                    this.in = new Scanner(this.socket.getInputStream());
                    
                    // Đọc dòng tin nhắn nhận được
                    String chuoi = in.nextLine().trim();
                    
                    // Gọi hàm hiển thị tin nhắn lên giao diện
                    chat.HienThi(chuoi + "\n");
                }
            }
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình nhận
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {}
        }
    }
}
