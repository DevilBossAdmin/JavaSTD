public class nhanvienvanphong extends nhanvien {

    private int soNgayLamViec;
    private double phuCap;

    public nhanvienvanphong(String maNV,
                            String hoTen,
                            int namSinh,
                            double luong,
                            int soNgayLamViec,
                            double phuCap) {

        super(maNV, hoTen, namSinh, luong);

        this.soNgayLamViec = soNgayLamViec;
        this.phuCap = phuCap;
    }

    public int getSoNgayLamViec() {
        return soNgayLamViec;
    }

    public void setSoNgayLamViec(int soNgayLamViec) {
        this.soNgayLamViec = soNgayLamViec;
    }

    public double getPhuCap() {
        return phuCap;
    }

    public void setPhuCap(double phuCap) {
        this.phuCap = phuCap;
    }

    public double tinhLuong() {
        return getLuong() + phuCap;
    }

    @Override
    public void hienThi() {

        super.hienThi();

        System.out.printf("%-15d %-15.0f %-15.0f\n",
                soNgayLamViec,
                phuCap,
                tinhLuong());
    }
}