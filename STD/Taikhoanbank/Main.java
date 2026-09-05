public class Main {
    public static void main(String[] args) {
        // 1. Tạo một tài khoản ngân hàng.
        TaiKhoan taiKhoan = new TaiKhoan(
                "123456789",
                "Bùi Văn Bình",
                10_000_000,
                "123456"
        );

        // 2. Hiển thị thông tin tài khoản.
        System.out.println("1. Thông tin ban đầu");
        taiKhoan.hienThiThongTin();

        // 3. Thực hiện nạp tiền.
        System.out.println("\n2. Nạp 2.000.000 đồng");
        taiKhoan.napTien(2_000_000);

        // 4. Thực hiện rút tiền hợp lệ dưới 5.000.000 đồng.
        System.out.println("\n3. Rút 1.000.000 đồng");
        taiKhoan.rutTien(1_000_000, null);

        // 5. Thử rút số tiền lớn hơn số dư.
        System.out.println("\n4. Thử rút 100.000.000 đồng");
        taiKhoan.rutTien(100_000_000, null);

        // 6. Thử rút từ 5.000.000 đồng và nhập sai mật khẩu.
        System.out.println("\n5. Thử rút 5.000.000 đồng bằng mật khẩu sai");
        taiKhoan.rutTien(5_000_000, "000000");

        // Thử phương thức đổi mật khẩu.
        System.out.println("\n6. Đổi mật khẩu");
        taiKhoan.doiMatKhau("123456", "654321");

        // 7. Hiển thị lại thông tin sau các giao dịch.
        System.out.println("\n7. Thông tin sau các giao dịch");
        taiKhoan.hienThiThongTin();

        // Câu lệnh dưới đây gây lỗi biên dịch nếu bỏ dấu //:
        // taiKhoan.soDu = 1_000_000_000;
        // Lý do: soDu được khai báo private trong lớp TaiKhoan.
    }
}
