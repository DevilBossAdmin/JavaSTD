import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===== NHẬP THÔNG TIN KHÁCH HÀNG =====");

        String cccd = nhapCccd();
        String hoTen = nhapKhongRong("Nhập họ và tên: ");
        String soDienThoai = nhapSoDienThoai();
        String email = nhapEmail();
        String diaChi = nhapKhongRong("Nhập địa chỉ: ");

        KhachHang khachHang = new KhachHang(
                cccd, hoTen, soDienThoai, email, diaChi
        );

        khachHang.hienThiThongTin();

        // Không thể truy cập trực tiếp vì cccd là private:
        // khachHang.cccd = "001234567890";

        scanner.close();
    }

    private static String nhapCccd() {
        while (true) {
            System.out.print("Nhập CCCD (12 chữ số): ");
            String cccd = scanner.nextLine().trim();

            if (cccd.matches("\\d{12}")) {
                return cccd;
            }

            System.out.println("Lỗi: CCCD phải gồm đúng 12 chữ số.");
        }
    }

    private static String nhapSoDienThoai() {
        while (true) {
            System.out.print("Nhập số điện thoại (10 chữ số): ");
            String soDienThoai = scanner.nextLine().trim();

            if (soDienThoai.matches("0\\d{9}")) {
                return soDienThoai;
            }

            System.out.println(
                    "Lỗi: Số điện thoại phải bắt đầu bằng 0 và gồm đúng 10 chữ số."
            );
        }
    }

    private static String nhapEmail() {
        while (true) {
            System.out.print("Nhập email: ");
            String email = scanner.nextLine().trim();

            if (email.matches(
                    "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            )) {
                return email;
            }

            System.out.println("Lỗi: Email không đúng định dạng.");
        }
    }

    private static String nhapKhongRong(String thongBao) {
        while (true) {
            System.out.print(thongBao);
            String giaTri = scanner.nextLine().trim();

            if (!giaTri.isEmpty()) {
                return giaTri;
            }

            System.out.println("Lỗi: Dữ liệu không được để trống.");
        }
    }
}
