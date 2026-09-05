import java.util.ArrayList;
import java.util.Scanner;

public class main {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<nhanvienvanphong> ds = new ArrayList<>();

    //=============================
    // Nhập 1 nhân viên
    //=============================
    public static nhanvienvanphong nhapnhanvien() {

        System.out.print("Ma NV: ");
        String maNV = sc.nextLine();

        System.out.print("Ho ten: ");
        String hoTen = sc.nextLine();

        System.out.print("Nam sinh: ");
        int namSinh = Integer.parseInt(sc.nextLine());

        System.out.print("Luong: ");
        double luong = Double.parseDouble(sc.nextLine());

        System.out.print("So ngay lam viec: ");
        int soNgayLamViec = Integer.parseInt(sc.nextLine());

        System.out.print("Phu cap: ");
        double phuCap = Double.parseDouble(sc.nextLine());

        return new nhanvienvanphong(
                maNV,
                hoTen,
                namSinh,
                luong,
                soNgayLamViec,
                phuCap);
    }

    //=============================
    // Nhập danh sách
    //=============================
    public static void nhapDanhSach() {

        System.out.print("Nhap so luong nhan vien: ");
        int n = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < n; i++) {

            System.out.println("\nNhan vien thu " + (i + 1));

            ds.add(nhapnhanvien());
        }

        System.out.println("Nhap danh sach thanh cong!");
    }

    //=============================
    // Hiển thị
    //=============================
    public static void hienThiDanhSach() {

        if (ds.isEmpty()) {
            System.out.println("Danh sach rong!");
            return;
        }

        System.out.println("\n================ DANH SACH NHAN VIEN ================");

        System.out.printf("%-10s %-25s %-10s %-15s %-15s %-15s %-15s\n",
                "MaNV",
                "HoTen",
                "NamSinh",
                "Luong",
                "NgayLam",
                "PhuCap",
                "TongLuong");

        for (nhanvienvanphong nv : ds) {
            nv.hienThi();
        }
    }

    //=============================
    // Thêm nhân viên
    //=============================
    public static void themnhanvien() {

        System.out.println("\nNhap thong tin nhan vien moi:");

        ds.add(nhapnhanvien());

        System.out.println("Them thanh cong!");
    }

    //=============================
    // Xóa nhân viên
    //=============================
    public static void xoanhanvien() {

        if (ds.isEmpty()) {
            System.out.println("Danh sach rong!");
            return;
        }

        System.out.print("Nhap ma NV can xoa: ");
        String ma = sc.nextLine();

        boolean found = false;

        for (int i = 0; i < ds.size(); i++) {

            if (ds.get(i).getMaNV().equalsIgnoreCase(ma)) {

                ds.remove(i);

                found = true;

                System.out.println("Xoa thanh cong!");

                break;
            }
        }

        if (!found) {
            System.out.println("Khong tim thay ma nhan vien!");
        }
    }

    //=============================
    // Menu
    //=============================
    public static void menu() {

        int chon;

        do {

            System.out.println("\n========== MENU ==========");
            System.out.println("1. Nhap danh sach");
            System.out.println("2. Hien thi danh sach");
            System.out.println("3. Them nhan vien");
            System.out.println("4. Xoa nhan vien");
            System.out.println("0. Thoat");
            System.out.print("Lua chon: ");

            try {

                chon = Integer.parseInt(sc.nextLine());

                switch (chon) {

                    case 1:
                        nhapDanhSach();
                        break;

                    case 2:
                        hienThiDanhSach();
                        break;

                    case 3:
                        themnhanvien();
                        break;

                    case 4:
                        xoanhanvien();
                        break;

                    case 0:
                        System.out.println("Tam biet!");
                        break;

                    default:
                        System.out.println("Lua chon khong hop le!");
                }

            } catch (NumberFormatException e) {

                System.out.println("Vui long nhap so!");

                chon = -1;
            }

        } while (chon != 0);
    }

    public static void main(String[] args) {
        menu();
    }
}