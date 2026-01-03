/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bai6_01;

import java.io.*;
import java.net.*;
/**
 *
 * @author Admin
 */
public class UDPServer {
    static final int PORT = 6111; // Khai báo Port sử dụng
    private DatagramSocket socket = null; // Khai báo DatagramSocket để lưu kết nối

    public UDPServer() {
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
                chuoi = chuoi.toUpperCase(); // Chuyển thành chữ in hoa
                
                if (!chuoi.equals("")) {
                    send(chuoi, host, port);
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
