import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Sach> danhSach = new ArrayList<>();

    public static void main(String[] args) {
        int luaChon;

        do {
            hienThiMenu();
            System.out.print("Nhập lựa chọn: ");
            luaChon = nhapSoNguyen();

            switch (luaChon) {
                case 1:
                    nhapDanhSach();
                    break;

                case 2:
                    themSach();
                    break;

                case 3:
                    hienThiDanhSach();
                    break;

                case 4:
                    suaSach();
                    break;

                case 5:
                    xoaSach();
                    break;

                case 0:
                    System.out.println("Đã thoát chương trình!");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (luaChon != 0);

        scanner.close();
    }

    public static void hienThiMenu() {
        System.out.println("\n========== QUẢN LÝ SÁCH ==========");
        System.out.println("1. Nhập danh sách sách");
        System.out.println("2. Thêm một cuốn sách");
        System.out.println("3. Hiển thị danh sách");
        System.out.println("4. Sửa thông tin sách");
        System.out.println("5. Xóa sách");
        System.out.println("0. Thoát");
        System.out.println("==================================");
    }

    public static int nhapSoNguyen() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Vui lòng nhập số nguyên: ");
            }
        }
    }

    // Case 1: Nhập nhiều sách
    public static void nhapDanhSach() {
        System.out.print("Nhập số lượng sách: ");
        int soLuong = nhapSoNguyen();

        for (int i = 0; i < soLuong; i++) {
            System.out.println("\nNhập sách thứ " + (i + 1));

            Sach sach = taoSachMoi();

            if (sach != null) {
                danhSach.add(sach);
            }
        }
    }

    // Case 2: Thêm một sách
    public static void themSach() {
        System.out.println("\n===== THÊM SÁCH =====");

        Sach sach = taoSachMoi();

();

        if (sach != null) {
            danhSach.add(sach);
            System.out.println("Thêm sách thành công!");
        }
    }

    public static Sach taoSachMoi() {
        System.out.print("Nhập mã sách: ");
        String maSach = scanner.nextLine().trim();

        if (timSachTheoMa(maSach) != null) {
            System.out.println("Mã sách đã tồn tại!");
            return null;
        }

        System.out.print("Nhập tên sách: ");
        String tenSach = scanner.nextLine();

        System.out.print("Nhập tác giả: ");
        String tacGia = scanner.nextLine();

        return new Sach(maSach, tenSach, tacGia);
    }

    // Case 3: Hiển thị
    public static void hienThiDanhSach() {
        if (danhSach.isEmpty()) {
            System.out.println("Danh sách sách đang trống!");
            return;
        }

        System.out.println("\n================ DANH SÁCH SÁCH ================");
        System.out.printf("%-12s %-30s %-25s%n",
                "Mã sách", "Tên sách", "Tác giả");
        System.out.println("--------------------------------------------------------------");

        for (Sach sach : danhSach) {
            sach.hienThiThongTin();
        }
    }

    // Case 4: Sửa sách theo mã
    public static void suaSach() {
        System.out.print("Nhập mã sách cần sửa: ");
        String maSach = scanner.nextLine().trim();

        Sach sach = timSachTheoMa(maSach);

        if (sach == null) {
            System.out.println("Không tìm thấy sách có mã " + maSach);
            return;
        }

        System.out.print("Nhập tên sách mới: ");
        String tenSachMoi = scanner.nextLine();

        System.out.print("Nhập tác giả mới: ");
        String tacGiaMoi = scanner.nextLine();

        sach.setTenSach(tenSachMoi);
        sach.setTacGia(tacGiaMoi);

        System.out.println("Sửa thông tin sách thành công!");
    }

    // Case 5: Xóa sách theo mã
    public static void xoaSach() {
        System.out.print("Nhập mã sách cần xóa: ");
        String maSach = scanner.nextLine().trim();

        Sach sach = timSachTheoMa(maSach);

        if (sach == null) {
            System.out.println("Không tìm thấy sách có mã " + maSach);
            return;
        }

        danhSach.remove(sach);
        System.out.println("Xóa sách thành công!");
    }

    // Tìm sách theo mã
    public static Sach timSachTheoMa(String maSach) {
        for (Sach sach : danhSach) {
            if (sach.getMaSach().equalsIgnoreCase(maSach)) {
                return sach;
            }
        }

        return null;
    }
}