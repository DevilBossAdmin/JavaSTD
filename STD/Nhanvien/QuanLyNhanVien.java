import java.io.*;
import java.util.ArrayList;

public class QuanLyNhanVien {

    private ArrayList<NhanVien> ds = new ArrayList<>();

    // Thêm nhân viên
    public void themNhanVien(NhanVien nv) {
        ds.add(nv);
    }

    // Hiển thị danh sách
    public void hienThiDanhSach() {

        if (ds.isEmpty()) {
            System.out.println("Danh sach rong!");
            return;
        }

        System.out.printf("%-10s %-25s %-10s %-15s\n",
                "Ma NV", "Ho Ten", "Tuoi", "Luong");

        for (NhanVien nv : ds) {
            nv.xuat();
        }
    }

    // Tìm nhân viên
    public void timNhanVien(String maNV) {

        boolean found = false;

        for (NhanVien nv : ds) {
            if (nv.getMaNV().equalsIgnoreCase(maNV)) {

                System.out.println("Thong tin nhan vien:");

                System.out.printf("%-10s %-25s %-10s %-15s\n",
                        "Ma NV", "Ho Ten", "Tuoi", "Luong");

                nv.xuat();

                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Khong tim thay nhan vien!");
        }
    }

    // Xóa nhân viên
    public void xoaNhanVien(String maNV) {

        for (NhanVien nv : ds) {

            if (nv.getMaNV().equalsIgnoreCase(maNV)) {
                ds.remove(nv);
                System.out.println("Xoa thanh cong!");
                return;
            }
        }

        System.out.println("Khong tim thay nhan vien!");
    }

    // Lưu file
    public void luuFile(String tenFile) {

        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(
                            new FileOutputStream(tenFile));

            oos.writeObject(ds);
            oos.close();

            System.out.println("Luu file thanh cong!");

        } catch (Exception e) {
            System.out.println("Loi luu file!");
        }
    }

    // Đọc file
    public void docFile(String tenFile) {

        try {
            ObjectInputStream ois =
                    new ObjectInputStream(
                            new FileInputStream(tenFile));

            ds = (ArrayList<NhanVien>) ois.readObject();

            ois.close();

            System.out.println("Doc file thanh cong!");

        } catch (Exception e) {
            System.out.println("Loi doc file!");
        }
    }
}