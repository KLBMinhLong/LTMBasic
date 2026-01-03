package atmserver;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerATM {
    public static void main(String args[]) {
        try {
            // Khởi tạo đối tượng thực thi logic ATM
            ATMImpl atm = new ATMImpl();
            
            // Tạo Registry tại cổng 3456
            Registry r = LocateRegistry.createRegistry(3456);
            
            // Đăng ký đối tượng với tên định danh "rmiATM"
            r.bind("rmiATM", atm);
            
            System.out.println("Server ATM đã khởi động thành công!");
            System.out.println("Server đang lắng nghe tại cổng 3456...");
            
        } catch (Exception ex) {
            // In lỗi nếu quá trình khởi tạo hoặc đăng ký thất bại
            System.out.println("Lỗi khởi động server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
