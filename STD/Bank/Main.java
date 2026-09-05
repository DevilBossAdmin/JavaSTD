import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== NHẬP THÔNG TIN TÀI KHOẢN =====");
        String soTaiKhoan = nhapChuoi(scanner, "Nhập số tài khoản: ");
        String tenChuTaiKhoan = nhapChuoi(scanner, "Nhập tên chủ tài khoản: ");
        double soDuBanDau = nhapSoKhongAm(scanner, "Nhập số dư ban đầu: ");
        String matKhau = nhapMatKhau(scanner);

        TaiKhoan taiKhoan = new TaiKhoan(
                soTaiKhoan,
                tenChuTaiKhoan,
                soDuBanDau,
                matKhau
        );

        System.out.println("\nTạo tài khoản thành công!");

        int luaChon;
        do {
            hienThiMenu();
            luaChon = nhapSoNguyen(scanner, "Nhập lựa chọn: ");

            switch (luaChon) {
                case 1:
                    taiKhoan.hienThiThongTin();
                    break;

                case 2:
                    double soTienNap = nhapSo(scanner, "Nhập số tiền cần nạp: ");
                    taiKhoan.napTien(soTienNap);
                    break;

                case 3:
                    double soTienRut = nhapSo(scanner, "Nhập số tiền cần rút: ");
                    String matKhauNhap = null;

                    if (soTienRut >= 5_000_000 && soTienRut <= taiKhoan.getSoDu()) {
                        System.out.print("Nhập mật khẩu để xác thực: ");
                        matKhauNhap = scanner.nextLine();
                    }

                    taiKhoan.rutTien(soTienRut, matKhauNhap);
                    break;

                case 4:
                    System.out.print("Nhập mật khẩu cũ: ");
                    String matKhauCu = scanner.nextLine();
                    System.out.print("Nhập mật khẩu mới: ");
                    String matKhauMoi = scanner.nextLine();
                    taiKhoan.doiMatKhau(matKhauCu, matKhauMoi);
                    break;

                case 0:
                    System.out.println("Đã thoát chương trình.");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại!");
            }
        } while (luaChon != 0);

        scanner.close();

        // Không thể viết: taiKhoan.soDu = 1_000_000_000;
        // Vì soDu là thuộc tính private của lớp TaiKhoan.
    }

    public static void hienThiMenu() {
        System.out.println("\n========== MENU ==========");
        System.out.println("1. Hiển thị thông tin tài khoản");
        System.out.println("2. Nạp tiền");
        System.out.println("3. Rút tiền");
        System.out.println("4. Đổi mật khẩu");
        System.out.println("0. Thoát");
    }

    public static String nhapChuoi(Scanner scanner, String thongBao) {
        while (true) {
            System.out.print(thongBao);
            String giaTri = scanner.nextLine().trim();

            if (!giaTri.isEmpty()) {
                return giaTri;
            }

            System.out.println("Thông tin không được để trống!");
        }
    }

    public static String nhapMatKhau(Scanner scanner) {
        while (true) {
            System.out.print("Nhập mật khẩu (ít nhất 6 ký tự): ");
            String matKhau = scanner.nextLine();

            if (matKhau.length() >= 6) {
                return matKhau;
            }

            System.out.println("Mật khẩu phải có ít nhất 6 ký tự!");
        }
    }

    public static int nhapSoNguyen(Scanner scanner, String thongBao) {
        while (true) {
            try {
                System.out.print(thongBao);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số nguyên!");
            }
        }
    }

    public static double nhapSo(Scanner scanner, String thongBao) {
        while (true) {
            try {
                System.out.print(thongBao);
                double giaTri = Double.parseDouble(scanner.nextLine());

                if (Double.isNaN(giaTri) || Double.isInfinite(giaTri)) {
                    System.out.println("Vui lòng nhập một số hữu hạn!");
                    continue;
                }

                return giaTri;
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số hợp lệ!");
            }
        }
    }

    public static double nhapSoKhongAm(Scanner scanner, String thongBao) {
        while (true) {
            double giaTri = nhapSo(scanner, thongBao);

            if (giaTri >= 0) {
                return giaTri;
            }

            System.out.println("Số dư ban đầu không được âm!");
        }
    }
}
