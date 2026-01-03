package remoteapp;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceATM extends Remote {
    // Thêm một người sử dụng mới
    public boolean themNguoiDung(NguoiDung nguoiDung) throws RemoteException;
    
    // Tìm người sử dụng theo tên và mật khẩu
    public NguoiDung timNguoiDung(String ten, String matKhau) throws RemoteException;

    // Tìm người sử dụng theo tên
    public NguoiDung timNguoiDungTheoTen(String ten) throws RemoteException;
    
    // Gửi tiền vào tài khoản
    public boolean guiTien(String ten, String matKhau, double soTien) throws RemoteException;
    
    // Rút tiền từ tài khoản
    public boolean rutTien(String ten, String matKhau, double soTien) throws RemoteException;
    
    // Lấy danh sách giao dịch của người dùng
    public List<GiaoDich> layDanhSachGiaoDich(String ten, String matKhau) throws RemoteException;

    // Lấy toàn bộ danh sách người dùng
    public List<NguoiDung> layDanhSachNguoiDung() throws RemoteException;
}
