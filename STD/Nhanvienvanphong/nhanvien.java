public class nhanvien {

    private String maNV;
    private String hoTen;
    private int namSinh;
    private double luong;

    public nhanvien(String maNV, String hoTen, int namSinh, double luong) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.namSinh = namSinh;
        this.luong = luong;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public int getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(int namSinh) {
        this.namSinh = namSinh;
    }

    public double getLuong() {
        return luong;
    }

    public void setLuong(double luong) {
        this.luong = luong;
    }

    public void hienThi() {
        System.out.printf("%-10s %-25s %-10d %-15.0f",
                maNV, hoTen, namSinh, luong);
    }
}