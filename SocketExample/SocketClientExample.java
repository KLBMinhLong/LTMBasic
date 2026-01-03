import java.io.*;
import java.net.*;

public class SocketClientExample {
    public static void main(String[] args) {
        try {
            // Thông tin máy chủ cần kết nối
            String serverAddress = "172.20.10.3";
            int serverPort = 1234;
            
            // Tạo kết nối tới máy chủ
            Socket socket = new Socket(serverAddress, serverPort);
            
            // Lấy luồng vào và ra để giao tiếp với Server
            InputStream inputStream = socket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter out = new PrintWriter(outputStream, true);
            
            // Gửi thông điệp tới Server
            out.println("Xin chào, Server!");
            
            // Đọc phản hồi từ Server và in ra màn hình
            String serverResponse = in.readLine();
            System.out.println("Server: " + serverResponse);
            
            // Đóng kết nối
            socket.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
