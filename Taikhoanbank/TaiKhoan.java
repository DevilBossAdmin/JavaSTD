public class TaiKhoan {
    // Các thuộc tính private để bảo vệ dữ liệu bên trong tài khoản.
    private String soTaiKhoan;
    private String tenChuTaiKhoan;
    private double soDu;
    private String matKhau;

    public TaiKhoan(String soTaiKhoan, String tenChuTaiKhoan,
                    double soDu, String matKhau) {
        if (soDu < 0) {
            throw new IllegalArgumentException("Số dư ban đầu không được âm.");
        }
        if (matKhau == null || matKhau.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự.");
        }

        this.soTaiKhoan = soTaiKhoan;
        this.tenChuTaiKhoan = tenChuTaiKhoan;
        this.soDu = soDu;
        this.matKhau = matKhau;
    }

    // Chỉ có getter, không có setSoDu().
    public double getSoDu() {
        return soDu;
    }

    public boolean napTien(double soTien) {
        if (soTien <= 0) {
            System.out.println("Nạp tiền thất bại: số tiền phải lớn hơn 0.");
            return false;
        }

        soDu += soTien;
        System.out.printf("Nạp thành công: %,.0f đồng.%n", soTien);
        return true;
    }

    public boolean rutTien(double soTien, String matKhauNhap) {
        if (soTien <= 0) {
            System.out.println("Rút tiền thất bại: số tiền phải lớn hơn 0.");
            return false;
        }

        if (soTien > soDu) {
            System.out.println("Rút tiền thất bại: số dư không đủ.");
            return false;
        }

        if (soTien >= 5_000_000 && !matKhau.equals(matKhauNhap)) {
            System.out.println("Rút tiền thất bại: mật khẩu không đúng.");
            return false;
        }

        soDu -= soTien;
        System.out.printf("Rút thành công: %,.0f đồng.%n", soTien);
        return true;
    }

    public boolean doiMatKhau(String matKhauCu, String matKhauMoi) {
        if (!matKhau.equals(matKhauCu)) {
            System.out.println("Đổi mật khẩu thất bại: mật khẩu cũ không đúng.");
            return false;
        }

        if (matKhauMoi == null || matKhauMoi.length() < 6) {
            System.out.println("Đổi mật khẩu thất bại: mật khẩu mới phải có ít nhất 6 ký tự.");
            return false;
        }

        if (matKhauMoi.equals(matKhau)) {
            System.out.println("Đổi mật khẩu thất bại: mật khẩu mới không được trùng mật khẩu cũ.");
            return false;
        }

        matKhau = matKhauMoi;
        System.out.println("Đổi mật khẩu thành công.");
        return true;
    }

    public void hienThiThongTin() {
        System.out.println("---------- THÔNG TIN TÀI KHOẢN ----------");
        System.out.println("Số tài khoản: " + soTaiKhoan);
        System.out.println("Tên chủ tài khoản: " + tenChuTaiKhoan);
        System.out.printf("Số dư: %,.0f đồng%n", soDu);
    }
}
