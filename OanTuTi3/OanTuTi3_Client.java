import java.io.*;
import java.net.*;
import java.util.Scanner;

public class OanTuTi3_Client {
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static Scanner scanner;
    private static final int MAX_RETRIES = 3;
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        
        try {
            // Kết nối đến server
            if (!connectToServer()) {
                System.out.println("✗ Không thể kết nối đến server. Thoát chương trình.");
                return;
            }
            
            System.out.println("✓ Đã kết nối thành công đến server!\n");
            
            // Nhận thông báo chào mừng
            String line;
            while ((line = in.readLine()) != null) {
                if (line.equals("NHẬP_TÊN")) {
                    break;
                }
                System.out.println(line);
            }
            
            // Nhập tên
            String playerName = "";
            while (playerName.trim().isEmpty()) {
                System.out.print("\nNhập tên của bạn: ");
                playerName = scanner.nextLine().trim();
                if (playerName.isEmpty()) {
                    System.out.println("⚠ Tên không được để trống! Vui lòng nhập lại.");
                }
            }
            out.println(playerName);
            
            // Nhận thông báo sau khi đặt tên và đợi người chơi khác
            while ((line = in.readLine()) != null) {
                if (line.startsWith("════════ VÁN CHƠI")) {
                    System.out.println("\n" + line);
                    break;
                }
                System.out.println(line);
            }
            
            // Vòng lặp chính của game
            while (true) {
                String message = in.readLine();
                
                if (message == null) {
                    System.out.println("\n✗ Mất kết nối với server!");
                    break;
                }
                
                if (message.equals("BẮT_ĐẦU")) {
                    // Nhập lựa chọn
                    String choice = getPlayerChoice();
                    out.println(choice);
                    
                    // Nhận và hiển thị kết quả
                    boolean shouldContinue = displayGameResult();
                    
                    if (!shouldContinue) {
                        break;
                    }
                    
                } else {
                    System.out.println(message);
                }
            }
            
            // Nhận tin nhắn kết thúc
            String finalMessage;
            while ((finalMessage = in.readLine()) != null) {
                System.out.println(finalMessage);
            }
            
        } catch (IOException e) {
            System.err.println("Lỗi kết nối: " + e.getMessage());
        } finally {
            cleanup();
        }
    }
    
    private static boolean connectToServer() {
        String serverAddress = "localhost";
        int serverPort = 1238;
        int retries = 0;
        
        while (retries < MAX_RETRIES) {
            try {
                System.out.println("Đang kết nối đến " + serverAddress + ":" + serverPort + "...");
                socket = new Socket();
                socket.connect(new InetSocketAddress(serverAddress, serverPort), 5000);
                
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                return true;
                
            } catch (IOException e) {
                retries++;
                System.out.println("✗ Không thể kết nối (lần thử " + retries + "/" + MAX_RETRIES + ")");
                
                if (retries < MAX_RETRIES) {
                    System.out.print("Nhập địa chỉ IP server (hoặc Enter để thử lại với " + serverAddress + "): ");
                    String input = scanner.nextLine().trim();
                    if (!input.isEmpty()) {
                        serverAddress = input;
                    }
                } else {
                    System.out.println("Đã hết số lần thử kết nối.");
                    return false;
                }
            }
        }
        
        return false;
    }
    
    private static String getPlayerChoice() {
        String choice = "";
        boolean validChoice = false;
        
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│        NHẬP LỰA CHỌN CỦA BẠN          │");
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("│  1. BÚA  ✊  (đập vỡ kéo)              │");
        System.out.println("│  2. BAO  ✋  (gói búa)                 │");
        System.out.println("│  3. KÉO  ✌  (cắt bao)                  │");
        System.out.println("└─────────────────────────────────────────┘");
        
        while (!validChoice) {
            System.out.print("Chọn (1/2/3 hoặc BÚA/BAO/KÉO): ");
            choice = scanner.nextLine().trim().toUpperCase();
            
            switch (choice) {
                case "1":
                case "BUA":
                case "BÚA":
                    choice = "BÚA";
                    validChoice = true;
                    break;
                case "2":
                case "BAO":
                    choice = "BAO";
                    validChoice = true;
                    break;
                case "3":
                case "KEO":
                case "KÉO":
                    choice = "KÉO";
                    validChoice = true;
                    break;
                default:
                    System.out.println("⚠ Lựa chọn không hợp lệ! Vui lòng chọn lại.");
            }
        }
        
        System.out.println("✓ Bạn đã chọn: " + choice);
        return choice;
    }
    
    private static boolean displayGameResult() throws IOException {
        String line;
        
        while ((line = in.readLine()) != null) {
            // Nếu nhận GAME_END ở bất kỳ đâu, kết thúc ngay
            if (line.equals("GAME_END")) {
                String endReason = in.readLine();
                if (endReason != null) {
                    System.out.println(endReason);
                }
                return false;
            }
            
            // Nếu gặp prompt chơi tiếp, xử lý luôn
            if (line.equals("CHƠI_TIẾP")) {
                String answer = "";
                while (!answer.equalsIgnoreCase("Y") && !answer.equalsIgnoreCase("N")) {
                    System.out.print("\n┌─────────────────────────────────────────┐\n");
                    System.out.print("│ Chơi tiếp? (Y = Có / N = Không): ");
                    answer = scanner.nextLine().trim().toUpperCase();
                    if (!answer.equals("Y") && !answer.equals("N")) {
                        System.out.println("│ ⚠ Vui lòng chỉ nhập Y hoặc N!         │");
                        System.out.print("└─────────────────────────────────────────┘\n");
                    }
                }
                out.println(answer);
                
                if (answer.equalsIgnoreCase("N")) {
                    return false; // Người chơi chọn thoát
                }
                
                // Đọc thông báo cập nhật từ server về việc ai sẵn sàng
                while ((line = in.readLine()) != null) {
                    // Nếu nhận được tín hiệu GAME_END, kết thúc ngay
                    if (line.equals("GAME_END")) {
                        String endReason = in.readLine();
                        if (endReason != null) {
                            System.out.println(endReason);
                        }
                        return false;
                    }
                    
                    // Nếu gặp vòng chơi mới, tiếp tục game
                    if (line.startsWith("════════ VÁN CHƠI")) {
                        System.out.println("\n" + line);
                        return true;
                    }
                    
                    // Hiển thị thông báo cập nhật
                    System.out.println(line);
                }
                
                return false; // Mất kết nối
            }
            
            // Nếu gặp vòng chơi mới, dừng đọc
            if (line.startsWith("════════ VÁN CHƠI")) {
                System.out.println("\n" + line);
                return true; // Tiếp tục chơi
            }
            
            // In các dòng kết quả
            System.out.println(line);
            
            // Dừng đọc sau khi hết bảng điểm
            if (line.contains("└────────────────────────────────────────────────────────┘")) {
                // Tiếp tục đọc để tìm CHƠI_TIẾP
                continue;
            }
        }
        
        return false; // Mất kết nối
    }
    
    private static void cleanup() {
        try {
            if (scanner != null) scanner.close();
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("\n✓ Đã ngắt kết nối!");
        } catch (IOException e) {
            System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
        }
    }
}
