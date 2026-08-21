public class KhachHang {
    private String cccd;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String diaChi;

    public KhachHang() {
    }

    public KhachHang(String cccd, String hoTen, String soDienThoai,
                     String email, String diaChi) {
        setCccd(cccd);
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setEmail(email);
        setDiaChi(diaChi);
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        if (cccd == null || !cccd.matches("\\d{12}")) {
            throw new IllegalArgumentException("CCCD phải gồm đúng 12 chữ số.");
        }
        this.cccd = cccd;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        this.hoTen = hoTen.trim();
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || !soDienThoai.matches("0\\d{9}")) {
            throw new IllegalArgumentException(
                    "Số điện thoại phải bắt đầu bằng 0 và gồm đúng 10 chữ số."
            );
        }
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null
                || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email không đúng định dạng.");
        }
        this.email = email.trim();
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        if (diaChi == null || diaChi.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống.");
        }
        this.diaChi = diaChi.trim();
    }

    public void hienThiThongTin() {
        System.out.println("\n===== THÔNG TIN KHÁCH HÀNG =====");
        System.out.println("CCCD          : " + cccd);
        System.out.println("Họ và tên     : " + hoTen);
        System.out.println("Số điện thoại : " + soDienThoai);
        System.out.println("Email         : " + email);
        System.out.println("Địa chỉ       : " + diaChi);
    }
}
