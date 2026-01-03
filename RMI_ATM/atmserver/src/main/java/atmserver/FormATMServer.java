package atmserver;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import remoteapp.GiaoDich;
import remoteapp.NguoiDung;

public class FormATMServer extends JFrame {
    private JButton btnStart;
    private JTable tblUsers;
    private DefaultTableModel userTableModel;
    private JTextArea txtUserDetail;
    private JTextField txtSearchName;
    private JTextField txtTenNew;
    private JTextField txtCmndNew;
    private JTextField txtMatKhauNew;
    private JTextField txtSoTienNew;
    private JTextField txtLaiSuatNew;
    private JLabel lblStatus;
    private ATMImpl atm;
    private boolean isRunning = false;
    
    public FormATMServer() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("ATM Server Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        
        add(buildHeaderPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel buildHeaderPanel() {
        JPanel panelTop = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("SERVER QUẢN LÝ ATM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        panelTop.add(lblTitle, BorderLayout.CENTER);
        
        btnStart = new JButton("Khởi động Server");
        btnStart.addActionListener(e -> startServer());
        panelTop.add(btnStart, BorderLayout.EAST);
        
        lblStatus = new JLabel("Chưa khởi động");
        lblStatus.setForeground(Color.RED);
        panelTop.add(lblStatus, BorderLayout.SOUTH);
        return panelTop;
    }
    
    private JPanel buildCenterPanel() {
        JPanel panelCenter = new JPanel(new GridLayout(1, 2, 10, 10));
        panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Bảng danh sách người dùng
        userTableModel = new DefaultTableModel(new Object[] {"Tên", "CMND", "Số dư", "Lãi suất"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblUsers = new JTable(userTableModel);
        tblUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUsers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    showSelectedUserDetail();
                }
            }
        });
        JScrollPane scrollTable = new JScrollPane(tblUsers);
        scrollTable.setBorder(BorderFactory.createTitledBorder("Danh sách người dùng"));
        panelCenter.add(scrollTable);
        
        // Khu vực thông tin chi tiết
        txtUserDetail = new JTextArea();
        txtUserDetail.setEditable(false);
        JScrollPane scrollDetail = new JScrollPane(txtUserDetail);
        scrollDetail.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));
        panelCenter.add(scrollDetail);
        
        return panelCenter;
    }
    
    private JPanel buildBottomPanel() {
        JPanel panelBottom = new JPanel(new BorderLayout(10, 10));
        panelBottom.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        panelBottom.add(buildSearchPanel(), BorderLayout.NORTH);
        panelBottom.add(buildAddPanel(), BorderLayout.CENTER);
        return panelBottom;
    }
    
    private JPanel buildSearchPanel() {
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSearch.setBorder(BorderFactory.createTitledBorder("Tìm kiếm theo tên"));
        panelSearch.add(new JLabel("Tên:"));
        txtSearchName = new JTextField(20);
        panelSearch.add(txtSearchName);
        JButton btnSearch = new JButton("Tìm");
        btnSearch.addActionListener(e -> handleSearch());
        panelSearch.add(btnSearch);
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> refreshUserTable());
        panelSearch.add(btnRefresh);
        return panelSearch;
    }
    
    private JPanel buildAddPanel() {
        JPanel panelAdd = new JPanel(new GridLayout(2, 1, 5, 5));
        panelAdd.setBorder(BorderFactory.createTitledBorder("Thêm người dùng"));
        
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Tên:"));
        txtTenNew = new JTextField(12);
        row1.add(txtTenNew);
        row1.add(new JLabel("CMND:"));
        txtCmndNew = new JTextField(12);
        row1.add(txtCmndNew);
        row1.add(new JLabel("Mật khẩu:"));
        txtMatKhauNew = new JTextField(10);
        row1.add(txtMatKhauNew);
        panelAdd.add(row1);
        
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Số tiền:"));
        txtSoTienNew = new JTextField(10);
        row2.add(txtSoTienNew);
        row2.add(new JLabel("Lãi suất:"));
        txtLaiSuatNew = new JTextField(6);
        row2.add(txtLaiSuatNew);
        JButton btnAdd = new JButton("Thêm");
        btnAdd.addActionListener(e -> handleAddUser());
        row2.add(btnAdd);
        panelAdd.add(row2);
        
        return panelAdd;
    }
    
    private void startServer() {
        if (isRunning) {
            return;
        }
        try {
            atm = new ATMImpl();
            Registry r = LocateRegistry.createRegistry(3456);
            r.bind("rmiATM", atm);
            btnStart.setEnabled(false);
            btnStart.setText("Server đang chạy");
            lblStatus.setText("Đang chạy tại cổng 3456 (rmiATM)");
            lblStatus.setForeground(new Color(0, 128, 0));
            isRunning = true;
            refreshUserTable();
            txtUserDetail.setText("Chọn người dùng để xem chi tiết hoặc tìm kiếm theo tên.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi động server: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void refreshUserTable() {
        if (!isRunning || atm == null) {
            JOptionPane.showMessageDialog(this, "Hãy khởi động server trước.");
            return;
        }
        try {
            List<NguoiDung> users = atm.layDanhSachNguoiDung();
            userTableModel.setRowCount(0);
            for (NguoiDung nd : users) {
                userTableModel.addRow(new Object[] { nd.getTen(), nd.getCmnd(), formatMoney(nd.getSoTien()), nd.getLaiSuat() });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showSelectedUserDetail() {
        int row = tblUsers.getSelectedRow();
        if (row < 0 || atm == null) {
            return;
        }
        String ten = (String) userTableModel.getValueAt(row, 0);
        try {
            NguoiDung nd = atm.timNguoiDungTheoTen(ten);
            if (nd != null) {
                txtUserDetail.setText(buildDetail(nd));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải thông tin: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleSearch() {
        if (!isRunning || atm == null) {
            JOptionPane.showMessageDialog(this, "Hãy khởi động server trước.");
            return;
        }
        String ten = txtSearchName.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập tên cần tìm.");
            return;
        }
        try {
            NguoiDung nd = atm.timNguoiDungTheoTen(ten);
            if (nd != null) {
                txtUserDetail.setText(buildDetail(nd));
                selectRowByName(nd.getTen());
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy người dùng.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleAddUser() {
        if (!isRunning || atm == null) {
            JOptionPane.showMessageDialog(this, "Hãy khởi động server trước.");
            return;
        }
        String ten = txtTenNew.getText().trim();
        String cmnd = txtCmndNew.getText().trim();
        String mk = txtMatKhauNew.getText().trim();
        String soTienStr = txtSoTienNew.getText().trim();
        String laiSuatStr = txtLaiSuatNew.getText().trim();
        if (ten.isEmpty() || cmnd.isEmpty() || mk.isEmpty() || soTienStr.isEmpty() || laiSuatStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin.");
            return;
        }
        try {
            double soTien = Double.parseDouble(soTienStr);
            double laiSuat = Double.parseDouble(laiSuatStr);
            if (soTien < 0 || laiSuat < 0) {
                JOptionPane.showMessageDialog(this, "Số tiền và lãi suất phải không âm.");
                return;
            }
            NguoiDung nd = new NguoiDung(ten, cmnd, mk, soTien, laiSuat);
            boolean ok = atm.themNguoiDung(nd);
            if (ok) {
                refreshUserTable();
                clearAddForm();
                JOptionPane.showMessageDialog(this, "Thêm người dùng thành công.");
            } else {
                JOptionPane.showMessageDialog(this, "CMND đã tồn tại.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Số tiền hoặc lãi suất không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm người dùng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void selectRowByName(String ten) {
        for (int i = 0; i < userTableModel.getRowCount(); i++) {
            if (userTableModel.getValueAt(i, 0).toString().equalsIgnoreCase(ten)) {
                tblUsers.setRowSelectionInterval(i, i);
                return;
            }
        }
    }
    
    private void clearAddForm() {
        txtTenNew.setText("");
        txtCmndNew.setText("");
        txtMatKhauNew.setText("");
        txtSoTienNew.setText("");
        txtLaiSuatNew.setText("");
    }
    
    private String buildDetail(NguoiDung nd) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tên: ").append(nd.getTen()).append("\n");
        sb.append("CMND: ").append(nd.getCmnd()).append("\n");
        sb.append("Số dư: ").append(formatMoney(nd.getSoTien())).append("\n");
        sb.append("Lãi suất: ").append(nd.getLaiSuat()).append("\n");
        sb.append("Giao dịch:\n");
        List<GiaoDich> gds = nd.getDanhSachGiaoDich();
        if (gds.isEmpty()) {
            sb.append("- Chưa có giao dịch\n");
        } else {
            for (GiaoDich gd : gds) {
                sb.append("- ").append(gd.toString()).append("\n");
            }
        }
        return sb.toString();
    }
    
    private String formatMoney(double value) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(value) + " VND";
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormATMServer form = new FormATMServer();
            form.setVisible(true);
        });
    }
}
