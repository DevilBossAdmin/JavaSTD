import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Ứng dụng quản lý nhân viên giản đơn.
 * - Lớp trừu tượng NhanVien định nghĩa contract tinhLuong() và hienThiThongTin().
 * - Lớp NhanVienVanPhong mở rộng NhanVien và tính lương theo số ngày * lương theo ngày.
 * - Main cung cấp menu: thêm, liệt kê, lưu/đọc CSV.
 */
public class NhanVienApp {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<NhanVien> DANH_SACH = new ArrayList<>();
    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public static void main(String[] args) {
        System.out.println("=== Ứng dụng quản lý nhân viên (nhanh, mẫu) ===");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = SCANNER.nextLine().trim();
            switch (choice) {
                case "1":
                    themNhanVienVanPhong();
                    break;
                case "2":
                    lietKeNhanVien();
                    break;
                case "3":
                    luuCSV();
                    break;
                case "4":
                    napCSV();
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
        System.out.println("Thoát ứng dụng. Tạm biệt!");
        SCANNER.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Thêm nhân viên văn phòng");
        System.out.println("2. Liệt kê nhân viên");
        System.out.println("3. Lưu danh sách ra CSV");
        System.out.println("4. Nạp danh sách từ CSV");
        System.out.println("5. Thoát");
        System.out.print("Chọn (1-5): ");
    }

    private static void themNhanVienVanPhong() {
        System.out.println("-- Thêm nhân viên văn phòng --");
        String manv = readNonEmptyString("Nhập mã nhân viên: ");
        String hoten = readNonEmptyString("Nhập họ tên: ");
        int namsinh = readYear("Nhập năm sinh: ");
        BigDecimal luongcoban = readNonNegativeBigDecimal("Nhập lương cơ bản: ");
        int songaylamviec = readNonNegativeInt("Nhập số ngày làm việc: ");
        BigDecimal luongtheongay = readNonNegativeBigDecimal("Nhập lương theo ngày: ");

        NhanVienVanPhong nv = new NhanVienVanPhong(manv, hoten, namsinh, luongcoban, songaylamviec, luongtheongay);
        DANH_SACH.add(nv);
        System.out.println("Đã thêm nhân viên:");
        nv.hienThiThongTin();
    }

    private static void lietKeNhanVien() {
        System.out.println("-- Danh sách nhân viên --");
        if (DANH_SACH.isEmpty()) {
            System.out.println("Chưa có nhân viên nào.");
            return;
        }
        int idx = 1;
        for (NhanVien nv : DANH_SACH) {
            System.out.println("--- Nhân viên #" + idx++ + " ---");
            nv.hienThiThongTin();
            System.out.println();
        }
    }

    private static void luuCSV() {
        System.out.print("Nhập đường dẫn file CSV để lưu (ví dụ: data.csv): ");
        String path = SCANNER.nextLine().trim();
        if (path.isEmpty()) {
            System.out.println("Đường dẫn không hợp lệ.");
            return;
        }
        File file = new File(path);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            // Header
            bw.write("manv,hoten,namsinh,luongcoban,songaylamviec,luongtheongay");
            bw.newLine();
            for (NhanVien nv : DANH_SACH) {
                if (nv instanceof NhanVienVanPhong) {
                    bw.write(((NhanVienVanPhong) nv).toCSV());
                    bw.newLine();
                }
            }
            System.out.println("Lưu thành công vào: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu file: " + e.getMessage());
        }
    }

    private static void napCSV() {
        System.out.print("Nhập đường dẫn file CSV để nạp (ví dụ: data.csv): ");
        String path = SCANNER.nextLine().trim();
        if (path.isEmpty()) {
            System.out.println("Đường dẫn không hợp lệ.");
            return;
        }
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            System.out.println("File không tồn tại: " + file.getAbsolutePath());
            return;
        }
        List<NhanVien> loaded = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // header
            int lineNo = 1;
            while ((line = br.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    NhanVienVanPhong nv = NhanVienVanPhong.fromCSV(line);
                    loaded.add(nv);
                } catch (IllegalArgumentException ex) {
                    System.out.println("Bỏ qua dòng " + lineNo + ": " + ex.getMessage());
                }
            }
            DANH_SACH.clear();
            DANH_SACH.addAll(loaded);
            System.out.println("Nạp xong. Đã nạp " + loaded.size() + " nhân viên.");
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }

    // ------- Input helpers -------
    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            if (!line.isEmpty()) return line;
            System.out.println("Giá trị không được để trống. Hãy thử lại.");
        }
    }

    private static int readYear(String prompt) {
        int current = Year.now().getValue();
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            try {
                int y = Integer.parseInt(line);
                if (y < 1900 || y > current) {
                    System.out.println("Năm sinh không hợp lệ. Phải trong khoảng 1900 - " + current);
                    continue;
                }
                return y;
            } catch (NumberFormatException ex) {
                System.out.println("Vui lòng nhập một số nguyên cho năm.");
            }
        }
    }

    private static int readNonNegativeInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < 0) {
                    System.out.println("Giá trị phải >= 0.");
                    continue;
                }
                return v;
            } catch (NumberFormatException ex) {
                System.out.println("Vui lòng nhập một số nguyên.");
            }
        }
    }

    private static BigDecimal readNonNegativeBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            try {
                BigDecimal v = new BigDecimal(line);
                if (v.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Giá trị phải >= 0.");
                    continue;
                }
                return v;
            } catch (Exception ex) {
                System.out.println("Vui lòng nhập số hợp lệ (ví dụ: 12345.67).");
            }
        }
    }

    // ------- Abstract model and implementation -------
    /**
     * Lớp trừu tượng NhanVien định nghĩa các thuộc tính cơ bản và hợp đồng tính lương.
     */
    public static abstract class NhanVien {
        private String maNV;
        private String hoTen;
        private int namSinh;
        private BigDecimal luongCoBan;

        public NhanVien(String maNV, String hoTen, int namSinh, BigDecimal luongCoBan) {
            this.maNV = maNV;
            this.hoTen = hoTen;
            this.namSinh = namSinh;
            this.luongCoBan = luongCoBan;
        }

        public String getMaNV() {
            return maNV;
        }

        public String getHoTen() {
            return hoTen;
        }

        public int getNamSinh() {
            return namSinh;
        }

        public BigDecimal getLuongCoBan() {
            return luongCoBan;
        }

        public abstract BigDecimal tinhLuong();

        public void hienThiThongTin() {
            System.out.println("Mã nhân viên: " + maNV);
            System.out.println("Họ tên: " + hoTen);
            System.out.println("Năm sinh: " + namSinh);
            System.out.println("Lương cơ bản: " + CURRENCY.format(luongCoBan));
        }
    }

    /**
     * Nhân viên văn phòng: lương theo ngày.
     */
    public static class NhanVienVanPhong extends NhanVien {
        private int soNgayLamViec;
        private BigDecimal luongTheoNgay;

        public NhanVienVanPhong(String maNV, String hoTen, int namSinh, BigDecimal luongCoBan, int soNgayLamViec, BigDecimal luongTheoNgay) {
            super(maNV, hoTen, namSinh, luongCoBan);
            this.soNgayLamViec = soNgayLamViec;
            this.luongTheoNgay = luongTheoNgay;
        }

        public int getSoNgayLamViec() {
            return soNgayLamViec;
        }

        public BigDecimal getLuongTheoNgay() {
            return luongTheoNgay;
        }

        @Override
        public BigDecimal tinhLuong() {
            return luongTheoNgay.multiply(BigDecimal.valueOf(soNgayLamViec));
        }

        @Override
        public void hienThiThongTin() {
            super.hienThiThongTin();
            System.out.println("Số ngày làm việc: " + soNgayLamViec);
            System.out.println("Lương theo ngày: " + CURRENCY.format(luongTheoNgay));
            System.out.println("Tổng lương: " + CURRENCY.format(tinhLuong()));
        }

        /**
         * Chuyển thành dòng CSV (không có escape phức tạp: giả sử không có dấu phẩy trong dữ liệu đơn giản này).
         */
        public String toCSV() {
            return String.join(",",
                    escapeCSV(getMaNV()),
                    escapeCSV(getHoTen()),
                    String.valueOf(getNamSinh()),
                    getLuongCoBan().toPlainString(),
                    String.valueOf(soNgayLamViec),
                    luongTheoNgay.toPlainString());
        }

        public static NhanVienVanPhong fromCSV(String csvLine) {
            String[] parts = csvLine.split(",");
            if (parts.length != 6) {
                throw new IllegalArgumentException("Dòng CSV không đủ cột");
            }
            String maNV = unescapeCSV(parts[0]);
            String hoTen = unescapeCSV(parts[1]);
            int namSinh;
            BigDecimal luongCoBan;
            int soNgay;
            BigDecimal luongNgay;
            try {
                namSinh = Integer.parseInt(parts[2]);
                luongCoBan = new BigDecimal(parts[3]);
                soNgay = Integer.parseInt(parts[4]);
                luongNgay = new BigDecimal(parts[5]);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Dữ liệu trong CSV không hợp lệ: " + ex.getMessage());
            }
            // Basic validation
            int current = Year.now().getValue();
            if (namSinh < 1900 || namSinh > current) throw new IllegalArgumentException("Năm sinh không hợp lệ: " + namSinh);
            if (luongCoBan.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Lương cơ bản âm");
            if (soNgay < 0) throw new IllegalArgumentException("Số ngày âm");
            if (luongNgay.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Lương theo ngày âm");

            return new NhanVienVanPhong(maNV, hoTen, namSinh, luongCoBan, soNgay, luongNgay);
        }

        private static String escapeCSV(String s) {
            if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
                s = s.replace("\"", "\"\"");
                return "\"" + s + "\"";
            }
            return s;
        }

        private static String unescapeCSV(String s) {
            s = s.trim();
            if (s.startsWith("\"") && s.endsWith("\"")) {
                s = s.substring(1, s.length() - 1).replace("\"\"", "\"");
            }
            return s;
        }
    }
}
