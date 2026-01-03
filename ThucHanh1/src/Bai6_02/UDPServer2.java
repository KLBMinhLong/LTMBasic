package Bai6_02;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;
/**
 *
 * @author Admin
 */
public class UDPServer2 {
    static final int PORT = 6222; // Khai báo Port sử dụng
    private DatagramSocket socket = null; // Khai báo DatagramSocket để lưu kết nối

    public UDPServer2() {
        try {
            socket = new DatagramSocket(PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void action() {
        InetAddress host = null;
        int port;
        String chuoi = ""; // Khai báo biến để lưu chuỗi dữ liệu
        try {
            System.out.println("Server is listening");
            while (true) { // vòng lặp chờ
                DatagramPacket packet = receive(); // Nhận dữ liệu từ client truyền qua
                host = packet.getAddress(); // Lấy thông tin địa chỉ của máy client
                port = packet.getPort(); // Lấy thông tin port của máy client
                
                // Lấy dữ liệu của máy client
                chuoi = new String(packet.getData()).trim(); 
                
                if (!chuoi.equals("")) {
                    // Sử dụng Scanner để tách chuỗi theo ký tự @
                    Scanner sc = new Scanner(chuoi);
                    sc.useDelimiter("@"); // Cắt chuỗi theo ký tự @
                    
                    int so1 = sc.nextInt(); // Lấy so1 là phần trước chữ @ đầu tiên
                    String pheptoan = sc.next(); // Phép toán là phần trước chữ @ thứ hai
                    int so2 = sc.nextInt(); // so2 là phần trước chữ @ thứ ba
                    
                    // Thực hiện phép tính dựa trên toán tử
                    if (pheptoan.equals("+")) { // Nếu phép toán là phép cộng
                        chuoi = (so1 + so2) + "";
                    } else if (pheptoan.equals("-")) { // Nếu phép toán là phép trừ
                        chuoi = (so1 - so2) + "";
                    } else if (pheptoan.equals("*")) { // Nếu phép toán là phép nhân
                        chuoi = (so1 * so2) + "";
                    } else if (pheptoan.equals("/")) { // Nếu phép toán là phép chia
                        chuoi = ((float) so1 / so2) + "";
                    }
                    
                    send(chuoi, host, port); // Truyền chuỗi trả về cho client
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private void send(String chuoi, InetAddress host, int port) throws IOException {
        byte[] buffer = chuoi.getBytes(); // chuyển chuỗi truyền thành byte
        // Sau đó đưa chuỗi truyền vào gói tin gửi đi
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, host, port);
        socket.send(packet);
    }

    private DatagramPacket receive() throws IOException {
        byte[] buffer = new byte[65507]; // Khai báo mảng byte nhận
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet); // Nhận dữ liệu
        return packet;
    }
}
