import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Bài tập Java OOP: Quản lý tài khoản ngân hàng.
 *
 * Chức năng chính:
 * 1. Tạo tài khoản và nhập đầy đủ thông tin khách hàng.
 * 2. Xem danh sách, xem chi tiết và tìm kiếm tài khoản.
 * 3. Nạp tiền, rút tiền và chuyển khoản.
 * 4. Xem lịch sử giao dịch.
 * 5. Cập nhật thông tin khách hàng.
 * 6. Khóa/mở khóa và đóng tài khoản.
 * 7. Cộng lãi hàng tháng cho tài khoản tiết kiệm.
 * 8. Thống kê và tự động lưu dữ liệu vào tệp.
 *
 * Chạy chương trình:
 * javac -encoding UTF-8 TaiKhoanNganHangApp.java
 * java TaiKhoanNganHangApp
 */
public class TaiKhoanNganHangApp {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final NumberFormat DINH_DANG_TIEN =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static final DateTimeFormatter DINH_DANG_NGAY =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DINH_DANG_NGAY_GIO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final String TEP_DU_LIEU = "du_lieu_ngan_hang.dat";

    private static final Pattern MA_KHACH_HANG_PATTERN = Pattern.compile("[A-Za-z0-9_-]{3,20}");
    private static final Pattern HO_TEN_PATTERN = Pattern.compile("[\\p{L} .'-]{2,100}");
    private static final Pattern CCCD_PATTERN = Pattern.compile("\\d{12}");
    private static final Pattern SO_DIEN_THOAI_PATTERN = Pattern.compile("0\\d{9}");
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern SO_TAI_KHOAN_PATTERN = Pattern.compile("\\d{8,16}");

    private static QuanLyNganHang quanLy;

    public static void main(String[] args) {
        khoiTaoDuLieu();

        boolean dangChay = true;
        while (dangChay) {
            inMenuChinh();
            int luaChon = nhapSoNguyenTrongKhoang("Chọn chức năng: ", 0, 13);

            try {
                switch (luaChon) {
                    case 1:
                        taoTaiKhoan();
                        break;
                    case 2:
                        hienThiDanhSachTaiKhoan();
                        break;
                    case 3:
                        xemChiTietTaiKhoan();
                        break;
                    case 4:
                        napTien();
                        break;
                    case 5:
                        rutTien();
                        break;
                    case 6:
                        chuyenKhoan();
                        break;
                    case 7:
                        capNhatThongTinKhachHang();
                        break;
                    case 8:
                        khoaHoacMoKhoaTaiKhoan();
                        break;
                    case 9:
                        xemLichSuGiaoDich();
                        break;
                    case 10:
                        congLaiTietKiem();
                        break;
                    case 11:
                        timKiemTaiKhoan();
                        break;
                    case 12:
                        dongTaiKhoan();
                        break;
                    case 13:
                        hienThiThongKe();
                        break;
                    case 0:
                        luuDuLieu();
                        dangChay = false;
                        break;
                    default:
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Lỗi: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            }
        }

        SCANNER.close();
        System.out.println("Đã lưu dữ liệu. Hẹn gặp lại!");
    }

    private static void khoiTaoDuLieu() {
        try {
            quanLy = QuanLyNganHang.docDuLieu(TEP_DU_LIEU);
            if (new File(TEP_DU_LIEU).exists()) {
                System.out.println("Đã nạp " + quanLy.getSoLuongTaiKhoan() + " tài khoản từ dữ liệu cũ.");
            }
        } catch (IOException e) {
            quanLy = new QuanLyNganHang();
            System.out.println("Không thể đọc tệp dữ liệu. Chương trình sẽ tạo dữ liệu mới.");
            System.out.println("Chi tiết: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            quanLy = new QuanLyNganHang();
            System.out.println("Dữ liệu cũ không tương thích. Chương trình sẽ tạo dữ liệu mới.");
        }

        System.out.println("===============================================");
        System.out.println("     CHƯƠNG TRÌNH QUẢN LÝ TÀI KHOẢN NGÂN HÀNG");
        System.out.println("===============================================");
    }

    private static void inMenuChinh() {
        System.out.println();
        System.out.println("================ MENU ================");
        System.out.println("1.  Tạo tài khoản ngân hàng");
        System.out.println("2.  Hiển thị danh sách tài khoản");
        System.out.println("3.  Xem chi tiết tài khoản");
        System.out.println("4.  Nạp tiền");
        System.out.println("5.  Rút tiền");
        System.out.println("6.  Chuyển khoản");
        System.out.println("7.  Cập nhật thông tin khách hàng");
        System.out.println("8.  Khóa hoặc mở khóa tài khoản");
        System.out.println("9.  Xem lịch sử giao dịch");
        System.out.println("10. Cộng lãi tài khoản tiết kiệm");
        System.out.println("11. Tìm kiếm tài khoản");
        System.out.println("12. Đóng tài khoản");
        System.out.println("13. Thống kê hệ thống");
        System.out.println("0.  Lưu và thoát");
        System.out.println("======================================");
    }

    private static void taoTaiKhoan() {
        System.out.println("\n--- TẠO TÀI KHOẢN MỚI ---");
        String soCCCD = nhapTheoMau(
                "Số CCCD (12 chữ số): ",
                CCCD_PATTERN,
                "CCCD phải gồm đúng 12 chữ số."
        );

        KhachHang khachHang;
        Optional<KhachHang> khachHangCu = quanLy.timKhachHangTheoCCCD(soCCCD);
        if (khachHangCu.isPresent()) {
            khachHang = khachHangCu.get();
            System.out.println("Đã tìm thấy khách hàng: " + khachHang.getHoTen()
                    + " - Mã KH: " + khachHang.getMaKhachHang());
            if (!nhapXacNhan("Dùng thông tin khách hàng này để mở thêm tài khoản? (Y/N): ")) {
                System.out.println("Đã hủy tạo tài khoản.");
                return;
            }
        } else {
            System.out.println("\nNhập thông tin khách hàng:");
            String maKhachHang = nhapMaKhachHangKhongTrung();
            String hoTen = nhapTheoMau(
                    "Họ và tên: ",
                    HO_TEN_PATTERN,
                    "Họ tên chỉ nên chứa chữ cái, khoảng trắng và dài ít nhất 2 ký tự."
            );
            LocalDate ngaySinh = nhapNgaySinh("Ngày sinh (dd/MM/yyyy, khách hàng từ 18 tuổi): ");
            String gioiTinh = nhapGioiTinh();
            String soDienThoai = nhapTheoMau(
                    "Số điện thoại (10 chữ số, bắt đầu bằng 0): ",
                    SO_DIEN_THOAI_PATTERN,
                    "Số điện thoại không hợp lệ."
            );
            String email = nhapTheoMau(
                    "Email: ",
                    EMAIL_PATTERN,
                    "Email không hợp lệ."
            );
            String diaChi = nhapChuoiBatBuoc("Địa chỉ: ");

            khachHang = new KhachHang(
                    maKhachHang,
                    hoTen,
                    soCCCD,
                    ngaySinh,
                    gioiTinh,
                    soDienThoai,
                    email,
                    diaChi
            );
        }

        System.out.println("\nNhập thông tin tài khoản:");
        String soTaiKhoan = nhapSoTaiKhoanKhongTrung();
        LoaiTaiKhoan loaiTaiKhoan = nhapLoaiTaiKhoan();
        BigDecimal soDuBanDau = nhapSoTien("Số dư ban đầu (VND): ", true);
        BigDecimal laiSuatNam = BigDecimal.ZERO;

        if (loaiTaiKhoan == LoaiTaiKhoan.TIET_KIEM) {
            laiSuatNam = nhapLaiSuat("Lãi suất năm (%): ");
        }

        TaiKhoanNganHang taiKhoan = new TaiKhoanNganHang(
                soTaiKhoan,
                khachHang,
                loaiTaiKhoan,
                soDuBanDau,
                laiSuatNam
        );
        quanLy.themTaiKhoan(taiKhoan);
        luuDuLieuSauThayDoi();

        System.out.println("Tạo tài khoản thành công!");
        taiKhoan.hienThiChiTiet();
    }

    private static void hienThiDanhSachTaiKhoan() {
        System.out.println("\n--- DANH SÁCH TÀI KHOẢN ---");
        List<TaiKhoanNganHang> danhSach = quanLy.getDanhSachTaiKhoan();
        if (danhSach.isEmpty()) {
            System.out.println("Chưa có tài khoản nào.");
            return;
        }

        System.out.printf("%-16s %-25s %-16s %-20s %-14s%n",
                "SỐ TÀI KHOẢN", "KHÁCH HÀNG", "LOẠI", "SỐ DƯ", "TRẠNG THÁI");
        System.out.println("-----------------------------------------------------------------------------------------------");
        for (TaiKhoanNganHang taiKhoan : danhSach) {
            System.out.printf("%-16s %-25s %-16s %-20s %-14s%n",
                    taiKhoan.getSoTaiKhoan(),
                    rutGon(taiKhoan.getKhachHang().getHoTen(), 24),
                    taiKhoan.getLoaiTaiKhoan().getTenHienThi(),
                    dinhDangTien(taiKhoan.getSoDu()),
                    taiKhoan.getTrangThai().getTenHienThi());
        }
        System.out.println("Tổng số: " + danhSach.size() + " tài khoản.");
    }

    private static void xemChiTietTaiKhoan() {
        System.out.println("\n--- CHI TIẾT TÀI KHOẢN ---");
        TaiKhoanNganHang taiKhoan = nhapVaTimTaiKhoan("Nhập số tài khoản: ");
        taiKhoan.hienThiChiTiet();
    }

    private static void napTien() {
        System.out.println("\n--- NẠP TIỀN ---");
        TaiKhoanNganHang taiKhoan = nhapVaTimTaiKhoan("Số tài khoản nhận tiền: ");
        BigDecimal soTien = nhapSoTien("Số tiền cần nạp (VND): ", false);
        String noiDung = nhapNoiDungTuyChon("Nội dung (Enter để dùng nội dung mặc định): ", "Nạp tiền vào tài khoản");

        quanLy.napTien(taiKhoan.getSoTaiKhoan(), soTien, noiDung);
        luuDuLieuSauThayDoi();
        System.out.println("Nạp tiền thành công. Số dư mới: " + dinhDangTien(taiKhoan.getSoDu()));
    }

    private static void rutTien() {
        System.out.println("\n--- RÚT TIỀN ---");
        TaiKhoanNganHang taiKhoan = nhapVaTimTaiKhoan("Số tài khoản rút tiền: ");
        BigDecimal soTien = nhapSoTien("Số tiền cần rút (VND): ", false);
        String noiDung = nhapNoiDungTuyChon("Nội dung (Enter để dùng nội dung mặc định): ", "Rút tiền khỏi tài khoản");

        quanLy.rutTien(taiKhoan.getSoTaiKhoan(), soTien, noiDung);
        luuDuLieuSauThayDoi();
        System.out.println("Rút tiền thành công. Số dư mới: " + dinhDangTien(taiKhoan.getSoDu()));
    }

    private static void chuyenKhoan() {
        System.out.println("\n--- CHUYỂN KHOẢN ---");
        TaiKhoanNganHang taiKhoanNguon = nhapVaTimTaiKhoan("Số tài khoản gửi: ");
        TaiKhoanNganHang taiKhoanDich = nhapVaTimTaiKhoan("Số tài khoản nhận: ");
        BigDecimal soTien = nhapSoTien("Số tiền chuyển (VND): ", false);
        String noiDung = nhapNoiDungTuyChon("Nội dung chuyển khoản: ", "Chuyển khoản");

        System.out.println("Người nhận: " + taiKhoanDich.getKhachHang().getHoTen());
        System.out.println("Số tiền: " + dinhDangTien(soTien));
        if (!nhapXacNhan("Xác nhận chuyển khoản? (Y/N): ")) {
            System.out.println("Đã hủy giao dịch.");
            return;
        }

        quanLy.chuyenKhoan(
                taiKhoanNguon.getSoTaiKhoan(),
                taiKhoanDich.getSoTaiKhoan(),
                soTien,
                noiDung
        );
        luuDuLieuSauThayDoi();
        System.out.println("Chuyển khoản thành công. Số dư tài khoản gửi: "
                + dinhDangTien(taiKhoanNguon.getSoDu()));
    }

    private static void capNhatThongTinKhachHang() {
        System.out.println("\n--- CẬP NHẬT THÔNG TIN KHÁCH HÀNG ---");
        TaiKhoanNganHang taiKhoan = nhapVaTimTaiKhoan("Nhập số tài khoản: ");
        KhachHang khachHang = taiKhoan.getKhachHang();

        boolean tiepTuc = true;
        while (tiepTuc) {
            System.out.println("\nKhách hàng: " + khachHang.getHoTen()
                    + " - Mã KH: " + khachHang.getMaKhachHang());
            System.out.println("1. Mã khách hàng");
            System.out.println("2. Họ và tên");
            System.out.println("3. Số CCCD");
            System.out.println("4. Ngày sinh");
            System.out.println("5. Giới tính");
            System.out.println("6. Số điện thoại");
            System.out.println("7. Email");
            System.out.println("8. Địa chỉ");
            System.out.println("0. Quay lại menu chính");
            int luaChon = nhapSoNguyenTrongKhoang("Chọn thông tin cần sửa: ", 0, 8);

            switch (luaChon) {
                case 1:
                    String maMoi = nhapTheoMau(
                            "Mã khách hàng mới: ",
                            MA_KHACH_HANG_PATTERN,
                            "Mã gồm 3-20 chữ cái, chữ số, dấu gạch ngang hoặc gạch dưới."
                    ).toUpperCase(Locale.ROOT);
                    quanLy.capNhatMaKhachHang(khachHang, maMoi);
                    break;
                case 2:
                    khachHang.setHoTen(nhapTheoMau(
                            "Họ tên mới: ",
                            HO_TEN_PATTERN,
                            "Họ tên không hợp lệ."
                    ));
                    break;
                case 3:
                    String cccdMoi = nhapTheoMau(
                            "CCCD mới: ",
                            CCCD_PATTERN,
                            "CCCD phải gồm đúng 12 chữ số."
                    );
                    quanLy.capNhatCCCD(khachHang, cccdMoi);
                    break;
                case 4:
                    khachHang.setNgaySinh(nhapNgaySinh("Ngày sinh mới (dd/MM/yyyy): "));
                    break;
                case 5:
                    khachHang.setGioiTinh(nhapGioiTinh());
                    break;
                case 6:
                    khachHang.setSoDienThoai(nhapTheoMau(
                            "Số điện thoại mới: ",
                            SO_DIEN_THOAI_PATTERN,
                            "Số điện thoại không hợp lệ."
                    ));
                    break;
                case 7:
                    khachHang.setEmail(nhapTheoMau(
                            "Email mới: ",
                            EMAIL_PATTERN,
                            "Email không hợp lệ."
                    ));
                    break;
                case 8:
                    khachHang.setDiaChi(nhapChuoiBatBuoc("Địa chỉ mới: "));
                    break;
                case 0:
                    tiepTuc = false;
                    continue;
                default:
                    break;
            }

            luuDuLieuSauThayDoi();
            System.out.println("Cập nhật thành công.");
        }
    }

    private static void khoaHoacMoKhoaTaiKhoan() {
        System.out.println("\n--- KHÓA / MỞ KHÓA TÀI KHOẢN ---");
        TaiKhoanNganHang taiKhoan = nhapVaTimTaiKhoan("Nhập số tài khoản: ");

        if (taiKhoan.getTrangThai() == TrangThaiTaiKhoan.HOAT_DONG) {
            if (!nhapXacNhan("Tài khoản đang hoạt động. Bạn muốn khóa? (Y/N): ")) {
                System.out.println("Không có thay đổi.");
                return;
            }
            taiKhoan.khoaTaiKhoan();
            System.out.println("Đã khóa tài khoản.");
        } else {
            if (!nhapXacNhan("Tài khoản đang bị khóa. Bạn muốn mở khóa? (Y/N): ")) {
                System.out.println("Không có thay đổi.");
                return;
            }
            taiKhoan.moKhoaTaiKhoan();
            System.out.println("Đã mở khóa tài khoản.");
        }
        luuDuLieuSauThayDoi();
    }

    private static void xemLichSuGiaoDich() {
        System.out.println("\n--- LỊCH SỬ GIAO DỊCH ---");
        TaiKhoanNganHang taiKhoan = nhapVaTimTaiKhoan("Nhập số tài khoản: ");
        List<GiaoDich> lichSu = new ArrayList<GiaoDich>(taiKhoan.getLichSuGiaoDich());

        if (lichSu.isEmpty()) {
            System.out.println("Tài khoản chưa có giao dịch.");
            return;
        }

        Collections.reverse(lichSu);
        System.out.println("Tài khoản: " + taiKhoan.getSoTaiKhoan()
                + " - " + taiKhoan.getKhachHang().getHoTen());
        for (GiaoDich giaoDich : lichSu) {
            giaoDich.hienThi();
        }
        System.out.println("Tổng số giao dịch: " + lichSu.size());
    }

    private static void congLaiTietKiem() {
        System.out.println("\n--- CỘNG LÃI TIẾT KIỆM ---");
        TaiKhoanNganHang taiKhoan = nhapVaTimTaiKhoan("Nhập số tài khoản tiết kiệm: ");
        BigDecimal tienLaiDuKien = taiKhoan.tinhLaiHangThang();

        System.out.println("Số dư hiện tại: " + dinhDangTien(taiKhoan.getSoDu()));
        System.out.println("Lãi suất năm: " + taiKhoan.getLaiSuatNam().stripTrailingZeros().toPlainString() + "%");
        System.out.println("Tiền lãi tháng này: " + dinhDangTien(tienLaiDuKien));
        if (!nhapXacNhan("Xác nhận cộng lãi? (Y/N): ")) {
            System.out.println("Đã hủy thao tác.");
            return;
        }

        BigDecimal tienLai = taiKhoan.congLaiHangThang();
        luuDuLieuSauThayDoi();
        System.out.println("Đã cộng " + dinhDangTien(tienLai)
                + ". Số dư mới: " + dinhDangTien(taiKhoan.getSoDu()));
    }

    private static void timKiemTaiKhoan() {
        System.out.println("\n--- TÌM KIẾM TÀI KHOẢN ---");
        String tuKhoa = nhapChuoiBatBuoc(
                "Nhập số tài khoản, mã KH, họ tên, CCCD hoặc số điện thoại: "
        );
        List<TaiKhoanNganHang> ketQua = quanLy.timKiem(tuKhoa);

        if (ketQua.isEmpty()) {
            System.out.println("Không tìm thấy kết quả phù hợp.");
            return;
        }

        for (TaiKhoanNganHang taiKhoan : ketQua) {
            System.out.println("----------------------------------------");
            System.out.println("Số tài khoản: " + taiKhoan.getSoTaiKhoan());
            System.out.println("Mã khách hàng: " + taiKhoan.getKhachHang().getMaKhachHang());
            System.out.println("Họ tên: " + taiKhoan.getKhachHang().getHoTen());
            System.out.println("CCCD: " + taiKhoan.getKhachHang().getSoCCCD());
            System.out.println("Số dư: " + dinhDangTien(taiKhoan.getSoDu()));
            System.out.println("Trạng thái: " + taiKhoan.getTrangThai().getTenHienThi());
        }
        System.out.println("Tìm thấy " + ketQua.size() + " tài khoản.");
    }

    private static void dongTaiKhoan() {
        System.out.println("\n--- ĐÓNG TÀI KHOẢN ---");
        TaiKhoanNganHang taiKhoan = nhapVaTimTaiKhoan("Nhập số tài khoản cần đóng: ");
        taiKhoan.hienThiTomTat();

        if (taiKhoan.getSoDu().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException(
                    "Chỉ có thể đóng tài khoản khi số dư bằng 0. Số dư hiện tại: "
                            + dinhDangTien(taiKhoan.getSoDu())
            );
        }
        if (!nhapXacNhan("Bạn chắc chắn muốn đóng tài khoản này? (Y/N): ")) {
            System.out.println("Đã hủy thao tác.");
            return;
        }

        quanLy.dongTaiKhoan(taiKhoan.getSoTaiKhoan());
        luuDuLieuSauThayDoi();
        System.out.println("Đã đóng tài khoản " + taiKhoan.getSoTaiKhoan() + ".");
    }

    private static void hienThiThongKe() {
        System.out.println("\n--- THỐNG KÊ HỆ THỐNG ---");
        ThongKeTaiKhoan thongKe = quanLy.thongKe();
        System.out.println("Tổng số tài khoản: " + thongKe.getTongSoTaiKhoan());
        System.out.println("Tài khoản hoạt động: " + thongKe.getSoTaiKhoanHoatDong());
        System.out.println("Tài khoản bị khóa: " + thongKe.getSoTaiKhoanBiKhoa());
        System.out.println("Tài khoản thanh toán: " + thongKe.getSoTaiKhoanThanhToan());
        System.out.println("Tài khoản tiết kiệm: " + thongKe.getSoTaiKhoanTietKiem());
        System.out.println("Tổng số dư toàn hệ thống: " + dinhDangTien(thongKe.getTongSoDu()));
    }

    private static TaiKhoanNganHang nhapVaTimTaiKhoan(String thongBao) {
        String soTaiKhoan = nhapTheoMau(
                thongBao,
                SO_TAI_KHOAN_PATTERN,
                "Số tài khoản phải gồm 8-16 chữ số."
        );
        return quanLy.layTaiKhoanBatBuoc(soTaiKhoan);
    }

    private static String nhapMaKhachHangKhongTrung() {
        while (true) {
            String ma = nhapTheoMau(
                    "Mã khách hàng (3-20 ký tự): ",
                    MA_KHACH_HANG_PATTERN,
                    "Mã gồm chữ cái, chữ số, dấu gạch ngang hoặc gạch dưới."
            ).toUpperCase(Locale.ROOT);
            if (!quanLy.tonTaiMaKhachHang(ma)) {
                return ma;
            }
            System.out.println("Mã khách hàng đã tồn tại. Vui lòng nhập mã khác.");
        }
    }

    private static String nhapSoTaiKhoanKhongTrung() {
        while (true) {
            String soTaiKhoan = nhapTheoMau(
                    "Số tài khoản (8-16 chữ số): ",
                    SO_TAI_KHOAN_PATTERN,
                    "Số tài khoản phải gồm 8-16 chữ số."
            );
            if (!quanLy.tonTaiSoTaiKhoan(soTaiKhoan)) {
                return soTaiKhoan;
            }
            System.out.println("Số tài khoản đã tồn tại. Vui lòng nhập số khác.");
        }
    }

    private static LoaiTaiKhoan nhapLoaiTaiKhoan() {
        System.out.println("1. Tài khoản thanh toán");
        System.out.println("2. Tài khoản tiết kiệm");
        int luaChon = nhapSoNguyenTrongKhoang("Chọn loại tài khoản: ", 1, 2);
        return luaChon == 1 ? LoaiTaiKhoan.THANH_TOAN : LoaiTaiKhoan.TIET_KIEM;
    }

    private static String nhapGioiTinh() {
        System.out.println("1. Nam");
        System.out.println("2. Nữ");
        System.out.println("3. Khác");
        int luaChon = nhapSoNguyenTrongKhoang("Chọn giới tính: ", 1, 3);
        if (luaChon == 1) {
            return "Nam";
        }
        if (luaChon == 2) {
            return "Nữ";
        }
        return "Khác";
    }

    private static String nhapChuoiBatBuoc(String thongBao) {
        while (true) {
            System.out.print(thongBao);
            String giaTri = SCANNER.nextLine().trim();
            if (!giaTri.isEmpty()) {
                return giaTri;
            }
            System.out.println("Giá trị không được để trống.");
        }
    }

    private static String nhapTheoMau(String thongBao, Pattern pattern, String thongBaoLoi) {
        while (true) {
            String giaTri = nhapChuoiBatBuoc(thongBao);
            if (pattern.matcher(giaTri).matches()) {
                return giaTri;
            }
            System.out.println(thongBaoLoi);
        }
    }

    private static int nhapSoNguyenTrongKhoang(String thongBao, int min, int max) {
        while (true) {
            System.out.print(thongBao);
            String giaTri = SCANNER.nextLine().trim();
            try {
                int so = Integer.parseInt(giaTri);
                if (so >= min && so <= max) {
                    return so;
                }
            } catch (NumberFormatException e) {
                // Hiển thị thông báo chung ở dưới.
            }
            System.out.println("Vui lòng nhập số nguyên từ " + min + " đến " + max + ".");
        }
    }

    private static LocalDate nhapNgaySinh(String thongBao) {
        while (true) {
            System.out.print(thongBao);
            String giaTri = SCANNER.nextLine().trim();
            try {
                LocalDate ngaySinh = LocalDate.parse(giaTri, DINH_DANG_NGAY);
                LocalDate homNay = LocalDate.now();
                int tuoi = Period.between(ngaySinh, homNay).getYears();
                if (ngaySinh.isAfter(homNay)) {
                    System.out.println("Ngày sinh không được lớn hơn ngày hiện tại.");
                } else if (tuoi < 18) {
                    System.out.println("Khách hàng phải đủ 18 tuổi.");
                } else if (tuoi > 120) {
                    System.out.println("Ngày sinh không hợp lý.");
                } else {
                    return ngaySinh;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Ngày không hợp lệ. Ví dụ đúng: 25/12/2000.");
            }
        }
    }

    private static BigDecimal nhapSoTien(String thongBao, boolean choPhepBangKhong) {
        while (true) {
            System.out.print(thongBao);
            String giaTri = SCANNER.nextLine().trim().replace(" ", "");
            try {
                if (!giaTri.matches("\\d+|\\d{1,3}([.,]\\d{3})+")) {
                    throw new NumberFormatException();
                }
                BigDecimal soTien = new BigDecimal(giaTri.replace(".", "").replace(",", ""));
                int soSanh = soTien.compareTo(BigDecimal.ZERO);
                if ((choPhepBangKhong && soSanh >= 0) || (!choPhepBangKhong && soSanh > 0)) {
                    return soTien.setScale(0, RoundingMode.UNNECESSARY);
                }
            } catch (ArithmeticException e) {
                // Hiển thị thông báo chung ở dưới.
            } catch (NumberFormatException e) {
                // Hiển thị thông báo chung ở dưới.
            }
            System.out.println(choPhepBangKhong
                    ? "Số tiền phải là số nguyên không âm. Ví dụ: 1000000 hoặc 1.000.000."
                    : "Số tiền phải là số nguyên lớn hơn 0. Ví dụ: 1000000 hoặc 1.000.000.");
        }
    }

    private static BigDecimal nhapLaiSuat(String thongBao) {
        while (true) {
            System.out.print(thongBao);
            String giaTri = SCANNER.nextLine().trim().replace(",", ".");
            try {
                BigDecimal laiSuat = new BigDecimal(giaTri);
                if (laiSuat.compareTo(BigDecimal.ZERO) >= 0
                        && laiSuat.compareTo(new BigDecimal("100")) <= 0) {
                    return laiSuat;
                }
            } catch (NumberFormatException e) {
                // Hiển thị thông báo chung ở dưới.
            }
            System.out.println("Lãi suất phải nằm trong khoảng 0-100. Ví dụ: 5.5");
        }
    }

    private static String nhapNoiDungTuyChon(String thongBao, String macDinh) {
        System.out.print(thongBao);
        String noiDung = SCANNER.nextLine().trim();
        return noiDung.isEmpty() ? macDinh : noiDung;
    }

    private static boolean nhapXacNhan(String thongBao) {
        while (true) {
            System.out.print(thongBao);
            String giaTri = SCANNER.nextLine().trim();
            if (giaTri.equalsIgnoreCase("Y") || giaTri.equalsIgnoreCase("C")) {
                return true;
            }
            if (giaTri.equalsIgnoreCase("N") || giaTri.equalsIgnoreCase("K")) {
                return false;
            }
            System.out.println("Vui lòng nhập Y (có) hoặc N (không).");
        }
    }

    private static void luuDuLieuSauThayDoi() {
        try {
            quanLy.luuDuLieu(TEP_DU_LIEU);
        } catch (IOException e) {
            System.out.println("Cảnh báo: chưa thể tự động lưu dữ liệu: " + e.getMessage());
        }
    }

    private static void luuDuLieu() {
        try {
            quanLy.luuDuLieu(TEP_DU_LIEU);
        } catch (IOException e) {
            System.out.println("Không thể lưu dữ liệu: " + e.getMessage());
        }
    }

    private static String dinhDangTien(BigDecimal soTien) {
        return DINH_DANG_TIEN.format(soTien);
    }

    private static String rutGon(String giaTri, int doDaiToiDa) {
        if (giaTri.length() <= doDaiToiDa) {
            return giaTri;
        }
        return giaTri.substring(0, doDaiToiDa - 3) + "...";
    }

    enum LoaiTaiKhoan {
        THANH_TOAN("Thanh toán"),
        TIET_KIEM("Tiết kiệm");

        private final String tenHienThi;

        LoaiTaiKhoan(String tenHienThi) {
            this.tenHienThi = tenHienThi;
        }

        public String getTenHienThi() {
            return tenHienThi;
        }
    }

    enum TrangThaiTaiKhoan {
        HOAT_DONG("Hoạt động"),
        BI_KHOA("Bị khóa");

        private final String tenHienThi;

        TrangThaiTaiKhoan(String tenHienThi) {
            this.tenHienThi = tenHienThi;
        }

        public String getTenHienThi() {
            return tenHienThi;
        }
    }

    enum LoaiGiaoDich {
        SO_DU_BAN_DAU("Số dư ban đầu"),
        NAP_TIEN("Nạp tiền"),
        RUT_TIEN("Rút tiền"),
        CHUYEN_DI("Chuyển đi"),
        NHAN_CHUYEN_KHOAN("Nhận chuyển khoản"),
        CONG_LAI("Cộng lãi");

        private final String tenHienThi;

        LoaiGiaoDich(String tenHienThi) {
            this.tenHienThi = tenHienThi;
        }

        public String getTenHienThi() {
            return tenHienThi;
        }
    }

    static class KhachHang implements Serializable {
        private static final long serialVersionUID = 1L;

        private String maKhachHang;
        private String hoTen;
        private String soCCCD;
        private LocalDate ngaySinh;
        private String gioiTinh;
        private String soDienThoai;
        private String email;
        private String diaChi;

        public KhachHang() {
            // Constructor không tham số phục vụ yêu cầu bài tập OOP.
        }

        public KhachHang(String maKhachHang, String hoTen, String soCCCD,
                         LocalDate ngaySinh, String gioiTinh, String soDienThoai,
                         String email, String diaChi) {
            this.maKhachHang = maKhachHang;
            this.hoTen = hoTen;
            this.soCCCD = soCCCD;
            this.ngaySinh = ngaySinh;
            this.gioiTinh = gioiTinh;
            this.soDienThoai = soDienThoai;
            this.email = email;
            this.diaChi = diaChi;
        }

        public void hienThiThongTin() {
            System.out.println("Mã khách hàng: " + maKhachHang);
            System.out.println("Họ và tên: " + hoTen);
            System.out.println("Số CCCD: " + soCCCD);
            System.out.println("Ngày sinh: " + ngaySinh.format(DINH_DANG_NGAY));
            System.out.println("Tuổi: " + Period.between(ngaySinh, LocalDate.now()).getYears());
            System.out.println("Giới tính: " + gioiTinh);
            System.out.println("Số điện thoại: " + soDienThoai);
            System.out.println("Email: " + email);
            System.out.println("Địa chỉ: " + diaChi);
        }

        public String getMaKhachHang() {
            return maKhachHang;
        }

        public void setMaKhachHang(String maKhachHang) {
            this.maKhachHang = maKhachHang;
        }

        public String getHoTen() {
            return hoTen;
        }

        public void setHoTen(String hoTen) {
            this.hoTen = hoTen;
        }

        public String getSoCCCD() {
            return soCCCD;
        }

        public void setSoCCCD(String soCCCD) {
            this.soCCCD = soCCCD;
        }

        public LocalDate getNgaySinh() {
            return ngaySinh;
        }

        public void setNgaySinh(LocalDate ngaySinh) {
            this.ngaySinh = ngaySinh;
        }

        public String getGioiTinh() {
            return gioiTinh;
        }

        public void setGioiTinh(String gioiTinh) {
            this.gioiTinh = gioiTinh;
        }

        public String getSoDienThoai() {
            return soDienThoai;
        }

        public void setSoDienThoai(String soDienThoai) {
            this.soDienThoai = soDienThoai;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDiaChi() {
            return diaChi;
        }

        public void setDiaChi(String diaChi) {
            this.diaChi = diaChi;
        }
    }

    static class GiaoDich implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String maGiaoDich;
        private final LocalDateTime thoiGian;
        private final LoaiGiaoDich loaiGiaoDich;
        private final BigDecimal soTien;
        private final BigDecimal soDuTruoc;
        private final BigDecimal soDuSau;
        private final String noiDung;
        private final String taiKhoanLienQuan;

        public GiaoDich(LoaiGiaoDich loaiGiaoDich, BigDecimal soTien,
                        BigDecimal soDuTruoc, BigDecimal soDuSau,
                        String noiDung, String taiKhoanLienQuan) {
            this.maGiaoDich = "GD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
            this.thoiGian = LocalDateTime.now();
            this.loaiGiaoDich = loaiGiaoDich;
            this.soTien = soTien;
            this.soDuTruoc = soDuTruoc;
            this.soDuSau = soDuSau;
            this.noiDung = noiDung;
            this.taiKhoanLienQuan = taiKhoanLienQuan;
        }

        public void hienThi() {
            System.out.println("----------------------------------------");
            System.out.println("Mã giao dịch: " + maGiaoDich);
            System.out.println("Thời gian: " + thoiGian.format(DINH_DANG_NGAY_GIO));
            System.out.println("Loại: " + loaiGiaoDich.getTenHienThi());
            System.out.println("Số tiền: " + dinhDangTien(soTien));
            System.out.println("Số dư trước: " + dinhDangTien(soDuTruoc));
            System.out.println("Số dư sau: " + dinhDangTien(soDuSau));
            System.out.println("Nội dung: " + noiDung);
            if (taiKhoanLienQuan != null && !taiKhoanLienQuan.isEmpty()) {
                System.out.println("Tài khoản liên quan: " + taiKhoanLienQuan);
            }
        }
    }

    static class TaiKhoanNganHang implements Serializable {
        private static final long serialVersionUID = 1L;

        private String soTaiKhoan;
        private KhachHang khachHang;
        private LoaiTaiKhoan loaiTaiKhoan;
        private BigDecimal soDu;
        private BigDecimal laiSuatNam;
        private TrangThaiTaiKhoan trangThai;
        private LocalDate ngayMoTaiKhoan;
        private List<GiaoDich> lichSuGiaoDich;

        public TaiKhoanNganHang() {
            this.soDu = BigDecimal.ZERO;
            this.laiSuatNam = BigDecimal.ZERO;
            this.trangThai = TrangThaiTaiKhoan.HOAT_DONG;
            this.ngayMoTaiKhoan = LocalDate.now();
            this.lichSuGiaoDich = new ArrayList<GiaoDich>();
        }

        public TaiKhoanNganHang(String soTaiKhoan, KhachHang khachHang,
                                LoaiTaiKhoan loaiTaiKhoan, BigDecimal soDuBanDau,
                                BigDecimal laiSuatNam) {
            this();
            this.soTaiKhoan = soTaiKhoan;
            this.khachHang = khachHang;
            this.loaiTaiKhoan = loaiTaiKhoan;
            this.soDu = soDuBanDau;
            this.laiSuatNam = loaiTaiKhoan == LoaiTaiKhoan.TIET_KIEM
                    ? laiSuatNam : BigDecimal.ZERO;

            ghiGiaoDich(
                    LoaiGiaoDich.SO_DU_BAN_DAU,
                    soDuBanDau,
                    BigDecimal.ZERO,
                    soDuBanDau,
                    "Khởi tạo tài khoản",
                    null
            );
        }

        public void napTien(BigDecimal soTien, String noiDung) {
            kiemTraDangHoatDong();
            kiemTraSoTienDuong(soTien);
            BigDecimal soDuTruoc = soDu;
            soDu = soDu.add(soTien);
            ghiGiaoDich(LoaiGiaoDich.NAP_TIEN, soTien, soDuTruoc, soDu, noiDung, null);
        }

        public void rutTien(BigDecimal soTien, String noiDung) {
            kiemTraCoTheGhiNo(soTien);
            BigDecimal soDuTruoc = soDu;
            soDu = soDu.subtract(soTien);
            ghiGiaoDich(LoaiGiaoDich.RUT_TIEN, soTien, soDuTruoc, soDu, noiDung, null);
        }

        public void ghiNoChuyenKhoan(BigDecimal soTien, String noiDung, String taiKhoanDich) {
            kiemTraCoTheGhiNo(soTien);
            BigDecimal soDuTruoc = soDu;
            soDu = soDu.subtract(soTien);
            ghiGiaoDich(LoaiGiaoDich.CHUYEN_DI, soTien, soDuTruoc, soDu, noiDung, taiKhoanDich);
        }

        public void ghiCoChuyenKhoan(BigDecimal soTien, String noiDung, String taiKhoanNguon) {
            kiemTraDangHoatDong();
            kiemTraSoTienDuong(soTien);
            BigDecimal soDuTruoc = soDu;
            soDu = soDu.add(soTien);
            ghiGiaoDich(
                    LoaiGiaoDich.NHAN_CHUYEN_KHOAN,
                    soTien,
                    soDuTruoc,
                    soDu,
                    noiDung,
                    taiKhoanNguon
            );
        }

        public BigDecimal tinhLaiHangThang() {
            kiemTraDangHoatDong();
            if (loaiTaiKhoan != LoaiTaiKhoan.TIET_KIEM) {
                throw new IllegalArgumentException("Chỉ tài khoản tiết kiệm mới được cộng lãi.");
            }
            if (soDu.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Tài khoản không có số dư để tính lãi.");
            }
            if (laiSuatNam.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Lãi suất phải lớn hơn 0 để phát sinh tiền lãi.");
            }

            return soDu.multiply(laiSuatNam)
                    .divide(new BigDecimal("1200"), 0, RoundingMode.HALF_UP);
        }

        public BigDecimal congLaiHangThang() {
            BigDecimal tienLai = tinhLaiHangThang();
            if (tienLai.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Tiền lãi làm tròn bằng 0, không thể cộng lãi.");
            }
            BigDecimal soDuTruoc = soDu;
            soDu = soDu.add(tienLai);
            ghiGiaoDich(
                    LoaiGiaoDich.CONG_LAI,
                    tienLai,
                    soDuTruoc,
                    soDu,
                    "Cộng lãi tiết kiệm hàng tháng",
                    null
            );
            return tienLai;
        }

        public void khoaTaiKhoan() {
            if (trangThai == TrangThaiTaiKhoan.BI_KHOA) {
                throw new IllegalArgumentException("Tài khoản đã bị khóa.");
            }
            trangThai = TrangThaiTaiKhoan.BI_KHOA;
        }

        public void moKhoaTaiKhoan() {
            if (trangThai == TrangThaiTaiKhoan.HOAT_DONG) {
                throw new IllegalArgumentException("Tài khoản đang hoạt động.");
            }
            trangThai = TrangThaiTaiKhoan.HOAT_DONG;
        }

        public void kiemTraDangHoatDong() {
            if (trangThai != TrangThaiTaiKhoan.HOAT_DONG) {
                throw new IllegalArgumentException("Tài khoản " + soTaiKhoan + " đang bị khóa.");
            }
        }

        public void kiemTraCoTheGhiNo(BigDecimal soTien) {
            kiemTraDangHoatDong();
            kiemTraSoTienDuong(soTien);
            if (soDu.compareTo(soTien) < 0) {
                throw new IllegalArgumentException(
                        "Số dư không đủ. Số dư hiện tại: " + dinhDangTien(soDu)
                );
            }
        }

        private void kiemTraSoTienDuong(BigDecimal soTien) {
            if (soTien == null || soTien.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Số tiền giao dịch phải lớn hơn 0.");
            }
        }

        private void ghiGiaoDich(LoaiGiaoDich loai, BigDecimal soTien,
                                  BigDecimal soDuTruoc, BigDecimal soDuSau,
                                  String noiDung, String taiKhoanLienQuan) {
            lichSuGiaoDich.add(new GiaoDich(
                    loai,
                    soTien,
                    soDuTruoc,
                    soDuSau,
                    noiDung,
                    taiKhoanLienQuan
            ));
        }

        public void hienThiTomTat() {
            System.out.println("Số tài khoản: " + soTaiKhoan);
            System.out.println("Chủ tài khoản: " + khachHang.getHoTen());
            System.out.println("Loại tài khoản: " + loaiTaiKhoan.getTenHienThi());
            System.out.println("Số dư: " + dinhDangTien(soDu));
            System.out.println("Trạng thái: " + trangThai.getTenHienThi());
        }

        public void hienThiChiTiet() {
            System.out.println("\n[THÔNG TIN KHÁCH HÀNG]");
            khachHang.hienThiThongTin();
            System.out.println("\n[THÔNG TIN TÀI KHOẢN]");
            System.out.println("Số tài khoản: " + soTaiKhoan);
            System.out.println("Loại tài khoản: " + loaiTaiKhoan.getTenHienThi());
            System.out.println("Số dư: " + dinhDangTien(soDu));
            if (loaiTaiKhoan == LoaiTaiKhoan.TIET_KIEM) {
                System.out.println("Lãi suất năm: "
                        + laiSuatNam.stripTrailingZeros().toPlainString() + "%");
            }
            System.out.println("Trạng thái: " + trangThai.getTenHienThi());
            System.out.println("Ngày mở: " + ngayMoTaiKhoan.format(DINH_DANG_NGAY));
            System.out.println("Số giao dịch: " + lichSuGiaoDich.size());
        }

        public String getSoTaiKhoan() {
            return soTaiKhoan;
        }

        public KhachHang getKhachHang() {
            return khachHang;
        }

        public LoaiTaiKhoan getLoaiTaiKhoan() {
            return loaiTaiKhoan;
        }

        public BigDecimal getSoDu() {
            return soDu;
        }

        public BigDecimal getLaiSuatNam() {
            return laiSuatNam;
        }

        public TrangThaiTaiKhoan getTrangThai() {
            return trangThai;
        }

        public List<GiaoDich> getLichSuGiaoDich() {
            return Collections.unmodifiableList(lichSuGiaoDich);
        }
    }

    static class QuanLyNganHang implements Serializable {
        private static final long serialVersionUID = 1L;

        private final Map<String, TaiKhoanNganHang> danhSachTaiKhoan;

        public QuanLyNganHang() {
            danhSachTaiKhoan = new LinkedHashMap<String, TaiKhoanNganHang>();
        }

        public void themTaiKhoan(TaiKhoanNganHang taiKhoan) {
            if (taiKhoan == null) {
                throw new IllegalArgumentException("Thông tin tài khoản không được để trống.");
            }
            if (tonTaiSoTaiKhoan(taiKhoan.getSoTaiKhoan())) {
                throw new IllegalArgumentException("Số tài khoản đã tồn tại.");
            }

            Optional<KhachHang> khachHangTheoCCCD =
                    timKhachHangTheoCCCD(taiKhoan.getKhachHang().getSoCCCD());
            if (khachHangTheoCCCD.isPresent()
                    && khachHangTheoCCCD.get() != taiKhoan.getKhachHang()) {
                throw new IllegalArgumentException("CCCD đã thuộc về một khách hàng khác.");
            }

            Optional<KhachHang> khachHangTheoMa =
                    timKhachHangTheoMa(taiKhoan.getKhachHang().getMaKhachHang());
            if (khachHangTheoMa.isPresent()
                    && khachHangTheoMa.get() != taiKhoan.getKhachHang()) {
                throw new IllegalArgumentException("Mã khách hàng đã tồn tại.");
            }
            danhSachTaiKhoan.put(taiKhoan.getSoTaiKhoan(), taiKhoan);
        }

        public void napTien(String soTaiKhoan, BigDecimal soTien, String noiDung) {
            layTaiKhoanBatBuoc(soTaiKhoan).napTien(soTien, noiDung);
        }

        public void rutTien(String soTaiKhoan, BigDecimal soTien, String noiDung) {
            layTaiKhoanBatBuoc(soTaiKhoan).rutTien(soTien, noiDung);
        }

        public void chuyenKhoan(String soTaiKhoanNguon, String soTaiKhoanDich,
                                BigDecimal soTien, String noiDung) {
            if (soTaiKhoanNguon.equals(soTaiKhoanDich)) {
                throw new IllegalArgumentException("Không thể chuyển tiền đến chính tài khoản gửi.");
            }

            TaiKhoanNganHang taiKhoanNguon = layTaiKhoanBatBuoc(soTaiKhoanNguon);
            TaiKhoanNganHang taiKhoanDich = layTaiKhoanBatBuoc(soTaiKhoanDich);

            taiKhoanNguon.kiemTraCoTheGhiNo(soTien);
            taiKhoanDich.kiemTraDangHoatDong();
            taiKhoanNguon.ghiNoChuyenKhoan(soTien, noiDung, soTaiKhoanDich);
            taiKhoanDich.ghiCoChuyenKhoan(soTien, noiDung, soTaiKhoanNguon);
        }

        public void dongTaiKhoan(String soTaiKhoan) {
            TaiKhoanNganHang taiKhoan = layTaiKhoanBatBuoc(soTaiKhoan);
            if (taiKhoan.getSoDu().compareTo(BigDecimal.ZERO) != 0) {
                throw new IllegalArgumentException("Số dư phải bằng 0 trước khi đóng tài khoản.");
            }
            danhSachTaiKhoan.remove(soTaiKhoan);
        }

        public void capNhatMaKhachHang(KhachHang khachHang, String maMoi) {
            Optional<KhachHang> daTonTai = timKhachHangTheoMa(maMoi);
            if (daTonTai.isPresent() && daTonTai.get() != khachHang) {
                throw new IllegalArgumentException("Mã khách hàng đã được sử dụng.");
            }
            khachHang.setMaKhachHang(maMoi);
        }

        public void capNhatCCCD(KhachHang khachHang, String cccdMoi) {
            Optional<KhachHang> daTonTai = timKhachHangTheoCCCD(cccdMoi);
            if (daTonTai.isPresent() && daTonTai.get() != khachHang) {
                throw new IllegalArgumentException("CCCD đã được sử dụng.");
            }
            khachHang.setSoCCCD(cccdMoi);
        }

        public TaiKhoanNganHang layTaiKhoanBatBuoc(String soTaiKhoan) {
            TaiKhoanNganHang taiKhoan = danhSachTaiKhoan.get(soTaiKhoan);
            if (taiKhoan == null) {
                throw new IllegalArgumentException("Không tìm thấy tài khoản " + soTaiKhoan + ".");
            }
            return taiKhoan;
        }

        public Optional<KhachHang> timKhachHangTheoCCCD(String soCCCD) {
            for (TaiKhoanNganHang taiKhoan : danhSachTaiKhoan.values()) {
                if (taiKhoan.getKhachHang().getSoCCCD().equals(soCCCD)) {
                    return Optional.of(taiKhoan.getKhachHang());
                }
            }
            return Optional.empty();
        }

        public Optional<KhachHang> timKhachHangTheoMa(String maKhachHang) {
            for (TaiKhoanNganHang taiKhoan : danhSachTaiKhoan.values()) {
                if (taiKhoan.getKhachHang().getMaKhachHang().equalsIgnoreCase(maKhachHang)) {
                    return Optional.of(taiKhoan.getKhachHang());
                }
            }
            return Optional.empty();
        }

        public List<TaiKhoanNganHang> timKiem(String tuKhoa) {
            String tuKhoaChuanHoa = tuKhoa.toLowerCase(new Locale("vi", "VN"));
            List<TaiKhoanNganHang> ketQua = new ArrayList<TaiKhoanNganHang>();
            for (TaiKhoanNganHang taiKhoan : danhSachTaiKhoan.values()) {
                KhachHang khachHang = taiKhoan.getKhachHang();
                if (taiKhoan.getSoTaiKhoan().contains(tuKhoa)
                        || khachHang.getMaKhachHang().toLowerCase(Locale.ROOT).contains(tuKhoaChuanHoa)
                        || khachHang.getHoTen().toLowerCase(new Locale("vi", "VN")).contains(tuKhoaChuanHoa)
                        || khachHang.getSoCCCD().contains(tuKhoa)
                        || khachHang.getSoDienThoai().contains(tuKhoa)) {
                    ketQua.add(taiKhoan);
                }
            }
            return ketQua;
        }

        public List<TaiKhoanNganHang> getDanhSachTaiKhoan() {
            List<TaiKhoanNganHang> danhSach =
                    new ArrayList<TaiKhoanNganHang>(danhSachTaiKhoan.values());
            Collections.sort(danhSach, new Comparator<TaiKhoanNganHang>() {
                @Override
                public int compare(TaiKhoanNganHang taiKhoan1, TaiKhoanNganHang taiKhoan2) {
                    return taiKhoan1.getSoTaiKhoan().compareTo(taiKhoan2.getSoTaiKhoan());
                }
            });
            return danhSach;
        }

        public boolean tonTaiSoTaiKhoan(String soTaiKhoan) {
            return danhSachTaiKhoan.containsKey(soTaiKhoan);
        }

        public boolean tonTaiMaKhachHang(String maKhachHang) {
            return timKhachHangTheoMa(maKhachHang).isPresent();
        }

        public int getSoLuongTaiKhoan() {
            return danhSachTaiKhoan.size();
        }

        public ThongKeTaiKhoan thongKe() {
            int hoatDong = 0;
            int biKhoa = 0;
            int thanhToan = 0;
            int tietKiem = 0;
            BigDecimal tongSoDu = BigDecimal.ZERO;

            for (TaiKhoanNganHang taiKhoan : danhSachTaiKhoan.values()) {
                if (taiKhoan.getTrangThai() == TrangThaiTaiKhoan.HOAT_DONG) {
                    hoatDong++;
                } else {
                    biKhoa++;
                }

                if (taiKhoan.getLoaiTaiKhoan() == LoaiTaiKhoan.THANH_TOAN) {
                    thanhToan++;
                } else {
                    tietKiem++;
                }
                tongSoDu = tongSoDu.add(taiKhoan.getSoDu());
            }

            return new ThongKeTaiKhoan(
                    danhSachTaiKhoan.size(),
                    hoatDong,
                    biKhoa,
                    thanhToan,
                    tietKiem,
                    tongSoDu
            );
        }

        public void luuDuLieu(String duongDan) throws IOException {
            ObjectOutputStream output = null;
            try {
                output = new ObjectOutputStream(
                        new BufferedOutputStream(new FileOutputStream(duongDan))
                );
                output.writeObject(this);
            } finally {
                if (output != null) {
                    output.close();
                }
            }
        }

        public static QuanLyNganHang docDuLieu(String duongDan)
                throws IOException, ClassNotFoundException {
            File tep = new File(duongDan);
            if (!tep.exists()) {
                return new QuanLyNganHang();
            }

            ObjectInputStream input = null;
            try {
                input = new ObjectInputStream(
                        new BufferedInputStream(new FileInputStream(tep))
                );
                Object duLieu = input.readObject();
                if (!(duLieu instanceof QuanLyNganHang)) {
                    throw new IOException("Nội dung tệp không phải dữ liệu quản lý ngân hàng.");
                }
                return (QuanLyNganHang) duLieu;
            } finally {
                if (input != null) {
                    input.close();
                }
            }
        }
    }

    static class ThongKeTaiKhoan {
        private final int tongSoTaiKhoan;
        private final int soTaiKhoanHoatDong;
        private final int soTaiKhoanBiKhoa;
        private final int soTaiKhoanThanhToan;
        private final int soTaiKhoanTietKiem;
        private final BigDecimal tongSoDu;

        public ThongKeTaiKhoan(int tongSoTaiKhoan, int soTaiKhoanHoatDong,
                               int soTaiKhoanBiKhoa, int soTaiKhoanThanhToan,
                               int soTaiKhoanTietKiem, BigDecimal tongSoDu) {
            this.tongSoTaiKhoan = tongSoTaiKhoan;
            this.soTaiKhoanHoatDong = soTaiKhoanHoatDong;
            this.soTaiKhoanBiKhoa = soTaiKhoanBiKhoa;
            this.soTaiKhoanThanhToan = soTaiKhoanThanhToan;
            this.soTaiKhoanTietKiem = soTaiKhoanTietKiem;
            this.tongSoDu = tongSoDu;
        }

        public int getTongSoTaiKhoan() {
            return tongSoTaiKhoan;
        }

        public int getSoTaiKhoanHoatDong() {
            return soTaiKhoanHoatDong;
        }

        public int getSoTaiKhoanBiKhoa() {
            return soTaiKhoanBiKhoa;
        }

        public int getSoTaiKhoanThanhToan() {
            return soTaiKhoanThanhToan;
        }

        public int getSoTaiKhoanTietKiem() {
            return soTaiKhoanTietKiem;
        }

        public BigDecimal getTongSoDu() {
            return tongSoDu;
        }
    }
}
