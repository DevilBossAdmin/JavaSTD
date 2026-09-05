import java.io.Serializable;
import java.util.Scanner;

public class NhanVien implements Serializable {
    private String maNV;
    private String hoTen;
    private int tuoi;
    private double luong;

    public NhanVien() {
    }

    public NhanVien(String maNV, String hoTen, int tuoi, double luong) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.tuoi = tuoi;
        this.luong = luong;
    }

    public void nhap() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhap ma NV: ");
        maNV = sc.nextLine();

        System.out.print("Nhap ho ten: ");
        hoTen = sc.nextLine();

        System.out.print("Nhap tuoi: ");
        tuoi = Integer.parseInt(sc.nextLine());

        System.out.print("Nhap luong: ");
        luong = Double.parseDouble(sc.nextLine());
    }

    public void xuat() {
        System.out.printf("%-10s %-25s %-10d %-15.2f\n",
                maNV, hoTen, tuoi, luong);
    }

    public String getMaNV() {
        return maNV;
    }
}