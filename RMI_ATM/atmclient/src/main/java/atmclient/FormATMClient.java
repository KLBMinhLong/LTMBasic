package atmclient;

import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import remoteapp.GiaoDich;
import remoteapp.InterfaceATM;
import remoteapp.NguoiDung;

public class FormATMClient extends JFrame {
    private InterfaceATM atmService;
    private NguoiDung nguoiDungHienTai;
    
    // Components cho màn hình đăng nhập
    private JPanel panelDangNhap;
    private JTextField txtTen;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
    
    // Components cho màn hình chính
    private JPanel panelChinh;
    private JLabel lblChaoMung;
    private JLabel lblSoDu;
    private JButton btnGuiTien;
    private JButton btnRutTien;
    private JButton btnXemGiaoDich;
    private JButton btnDangXuat;
    private JTextArea txtGiaoDich;
    
    public FormATMClient() {
        initComponents();
        connectToServer();
    }
    
    private void connectToServer() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 3456);
            atmService = (InterfaceATM) registry.lookup("rmiATM");
            JOptionPane.showMessageDialog(this, "Kết nối đến server thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối đến server: " + e.getMessage(), 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initComponents() {
        setTitle("Máy ATM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLayout(new CardLayout());
        
        // Tạo màn hình đăng nhập
        taoPanelDangNhap();
        
        // Tạo màn hình chính
        taoPanelChinh();
        
        // Hiển thị màn hình đăng nhập
        add(panelDangNhap, "DangNhap");
        add(panelChinh, "Chinh");
        
        hienThiManHinh("DangNhap");
        setLocationRelativeTo(null);
    }
    
    private void taoPanelDangNhap() {
        panelDangNhap = new JPanel(new BorderLayout(10, 10));
        panelDangNhap.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel tiêu đề
        JPanel panelTop = new JPanel();
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP MÁY ATM");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        panelTop.add(lblTitle);
        panelDangNhap.add(panelTop, BorderLayout.NORTH);
        
        // Panel nhập liệu
        JPanel panelInput = new JPanel(new GridLayout(3, 2, 10, 10));
        panelInput.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        
        panelInput.add(new JLabel("Tên hoặc CMND:"));
        txtTen = new JTextField();
        panelInput.add(txtTen);
        
        panelInput.add(new JLabel("Mật khẩu:"));
        txtMatKhau = new JPasswordField();
        panelInput.add(txtMatKhau);
        
        panelDangNhap.add(panelInput, BorderLayout.CENTER);
        
        // Panel nút
        JPanel panelButton = new JPanel();
        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setPreferredSize(new Dimension(150, 40));
        btnDangNhap.addActionListener(e -> xuLyDangNhap());
        panelButton.add(btnDangNhap);
        panelDangNhap.add(panelButton, BorderLayout.SOUTH);
    }
    
    private void taoPanelChinh() {
        panelChinh = new JPanel(new BorderLayout(10, 10));
        panelChinh.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel tiêu đề
        JPanel panelTop = new JPanel(new GridLayout(2, 1));
        lblChaoMung = new JLabel("Xin chào!");
        lblChaoMung.setFont(new Font("Arial", Font.BOLD, 16));
        lblSoDu = new JLabel("Số dư: 0 VND");
        lblSoDu.setFont(new Font("Arial", Font.PLAIN, 14));
        panelTop.add(lblChaoMung);
        panelTop.add(lblSoDu);
        panelChinh.add(panelTop, BorderLayout.NORTH);
        
        // Panel các nút chức năng
        JPanel panelButtons = new JPanel(new GridLayout(2, 2, 10, 10));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        btnGuiTien = new JButton("Gửi tiền");
        btnGuiTien.addActionListener(e -> xuLyGuiTien());
        panelButtons.add(btnGuiTien);
        
        btnRutTien = new JButton("Rút tiền");
        btnRutTien.addActionListener(e -> xuLyRutTien());
        panelButtons.add(btnRutTien);
        
        btnXemGiaoDich = new JButton("Xem giao dịch");
        btnXemGiaoDich.addActionListener(e -> xuLyXemGiaoDich());
        panelButtons.add(btnXemGiaoDich);
        
        btnDangXuat = new JButton("Đăng xuất");
        btnDangXuat.addActionListener(e -> xuLyDangXuat());
        panelButtons.add(btnDangXuat);
        
        panelChinh.add(panelButtons, BorderLayout.CENTER);
        
        // Panel hiển thị giao dịch
        JPanel panelGiaoDich = new JPanel(new BorderLayout());
        panelGiaoDich.add(new JLabel("Lịch sử giao dịch:"), BorderLayout.NORTH);
        txtGiaoDich = new JTextArea();
        txtGiaoDich.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtGiaoDich);
        panelGiaoDich.add(scrollPane, BorderLayout.CENTER);
        panelChinh.add(panelGiaoDich, BorderLayout.SOUTH);
    }
    
    private void xuLyDangNhap() {
        try {
            String tenHoacCmnd = txtTen.getText().trim();
            String matKhau = new String(txtMatKhau.getPassword());
            
            if (tenHoacCmnd.isEmpty() || matKhau.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            
            nguoiDungHienTai = atmService.timNguoiDung(tenHoacCmnd, matKhau);
            
            if (nguoiDungHienTai != null) {
                lblChaoMung.setText("Xin chào, " + nguoiDungHienTai.getTen() + "!");
                capNhatSoDu();
                hienThiManHinh("Chinh");
                txtTen.setText("");
                txtMatKhau.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Tên hoặc mật khẩu không đúng!", 
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi đăng nhập: " + e.getMessage(), 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void xuLyGuiTien() {
        try {
            String soTienStr = JOptionPane.showInputDialog(this, "Nhập số tiền cần gửi:");
            if (soTienStr != null && !soTienStr.isEmpty()) {
                double soTien = Double.parseDouble(soTienStr);
                
                if (soTien <= 0) {
                    JOptionPane.showMessageDialog(this, "Số tiền phải lớn hơn 0!");
                    return;
                }
                
                boolean ketQua = atmService.guiTien(nguoiDungHienTai.getTen(), 
                                                   nguoiDungHienTai.getMatKhau(), soTien);
                
                if (ketQua) {
                    nguoiDungHienTai = atmService.timNguoiDung(nguoiDungHienTai.getTen(), 
                                                              nguoiDungHienTai.getMatKhau());
                    capNhatSoDu();
                    JOptionPane.showMessageDialog(this, "Gửi tiền thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Gửi tiền thất bại!", 
                                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ!", 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void xuLyRutTien() {
        try {
            String soTienStr = JOptionPane.showInputDialog(this, "Nhập số tiền cần rút:");
            if (soTienStr != null && !soTienStr.isEmpty()) {
                double soTien = Double.parseDouble(soTienStr);
                
                if (soTien <= 0) {
                    JOptionPane.showMessageDialog(this, "Số tiền phải lớn hơn 0!");
                    return;
                }
                
                boolean ketQua = atmService.rutTien(nguoiDungHienTai.getTen(), 
                                                   nguoiDungHienTai.getMatKhau(), soTien);
                
                if (ketQua) {
                    nguoiDungHienTai = atmService.timNguoiDung(nguoiDungHienTai.getTen(), 
                                                              nguoiDungHienTai.getMatKhau());
                    capNhatSoDu();
                    JOptionPane.showMessageDialog(this, "Rút tiền thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Rút tiền thất bại! Kiểm tra số dư.", 
                                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ!", 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void xuLyXemGiaoDich() {
        try {
            List<GiaoDich> danhSach = atmService.layDanhSachGiaoDich(nguoiDungHienTai.getTen(), 
                                                                     nguoiDungHienTai.getMatKhau());
            
            txtGiaoDich.setText("");
            if (danhSach.isEmpty()) {
                txtGiaoDich.setText("Chưa có giao dịch nào.");
            } else {
                for (GiaoDich gd : danhSach) {
                    txtGiaoDich.append(gd.toString() + "\n");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void xuLyDangXuat() {
        nguoiDungHienTai = null;
        txtGiaoDich.setText("");
        hienThiManHinh("DangNhap");
    }
    
    private void capNhatSoDu() {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        lblSoDu.setText("Số dư: " + formatter.format(nguoiDungHienTai.getSoTien()) + " VND");
    }
    
    private void hienThiManHinh(String tenManHinh) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), tenManHinh);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormATMClient form = new FormATMClient();
            form.setVisible(true);
        });
    }
}
