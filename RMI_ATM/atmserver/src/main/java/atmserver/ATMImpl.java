package atmserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import remoteapp.GiaoDich;
import remoteapp.InterfaceATM;
import remoteapp.NguoiDung;

public class ATMImpl extends UnicastRemoteObject implements InterfaceATM {
    // Danh sách người dùng trong hệ thống
    private List<NguoiDung> danhSachNguoiDung;
    
    // Constructor của lớp ATMImpl
    public ATMImpl() throws RemoteException {
        super();
        danhSachNguoiDung = new ArrayList<>();
        
        // Thêm một số tài khoản mẫu để test
        NguoiDung nd1 = new NguoiDung("Nguyen Van A", "123456789", "123", 1000000, 0.05);
        NguoiDung nd2 = new NguoiDung("Tran Thi B", "987654321", "456", 500000, 0.05);
        danhSachNguoiDung.add(nd1);
        danhSachNguoiDung.add(nd2);
    }
    
    // Thêm một người sử dụng mới vào hệ thống
    @Override
    public boolean themNguoiDung(NguoiDung nguoiDung) throws RemoteException {
        try {
            // Kiểm tra xem CMND đã tồn tại chưa
            for (NguoiDung nd : danhSachNguoiDung) {
                if (nd.getCmnd().equals(nguoiDung.getCmnd())) {
                    return false; // CMND đã tồn tại
                }
            }
            danhSachNguoiDung.add(nguoiDung);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Tìm người sử dụng theo tên hoặc CMND và mật khẩu
    @Override
    public NguoiDung timNguoiDung(String tenHoacCmnd, String matKhau) throws RemoteException {
        for (NguoiDung nd : danhSachNguoiDung) {
            if ((nd.getTen().equals(tenHoacCmnd) || nd.getCmnd().equals(tenHoacCmnd)) 
                && nd.getMatKhau().equals(matKhau)) {
                return nd;
            }
        }
        return null; // Không tìm thấy
    }

    // Tìm người sử dụng theo tên
    @Override
    public NguoiDung timNguoiDungTheoTen(String ten) throws RemoteException {
        for (NguoiDung nd : danhSachNguoiDung) {
            if (nd.getTen().equalsIgnoreCase(ten)) {
                return nd;
            }
        }
        return null;
    }
    
    // Gửi tiền vào tài khoản
    @Override
    public boolean guiTien(String tenHoacCmnd, String matKhau, double soTien) throws RemoteException {
        NguoiDung nd = timNguoiDung(tenHoacCmnd, matKhau);
        if (nd != null && soTien > 0) {
            nd.setSoTien(nd.getSoTien() + soTien);
            GiaoDich gd = new GiaoDich(soTien, "GUI_TIEN");
            nd.themGiaoDich(gd);
            return true;
        }
        return false;
    }
    
    // Rút tiền từ tài khoản
    @Override
    public boolean rutTien(String tenHoacCmnd, String matKhau, double soTien) throws RemoteException {
        NguoiDung nd = timNguoiDung(tenHoacCmnd, matKhau);
        if (nd != null && soTien > 0 && nd.getSoTien() >= soTien) {
            nd.setSoTien(nd.getSoTien() - soTien);
            GiaoDich gd = new GiaoDich(soTien, "RUT_TIEN");
            nd.themGiaoDich(gd);
            return true;
        }
        return false; // Số dư không đủ hoặc thông tin sai
    }
    
    // Lấy danh sách giao dịch của người dùng
    @Override
    public List<GiaoDich> layDanhSachGiaoDich(String tenHoacCmnd, String matKhau) throws RemoteException {
        NguoiDung nd = timNguoiDung(tenHoacCmnd, matKhau);
        if (nd != null) {
            return nd.getDanhSachGiaoDich();
        }
        return new ArrayList<>(); // Trả về danh sách rỗng nếu không tìm thấy
    }

    // Lấy toàn bộ danh sách người dùng
    @Override
    public List<NguoiDung> layDanhSachNguoiDung() throws RemoteException {
        return new ArrayList<>(danhSachNguoiDung);
    }
}
