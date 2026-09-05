import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        QuanLyNhanVien ql = new QuanLyNhanVien();

        int chon;

        do {

            System.out.println("\n===== QUAN LY NHAN VIEN =====");
            System.out.println("1. Them nhan vien");
            System.out.println("2. Hien thi danh sach");
            System.out.println("3. Tim nhan vien");
            System.out.println("4. Xoa nhan vien");
            System.out.println("5. Luu danh sach vao file");
            System.out.println("6. Doc danh sach tu file");
            System.out.println("0. Thoat");

            System.out.print("Nhap lua chon: ");
            chon = Integer.parseInt(sc.nextLine());

            switch (chon) {

                case 1:
                    NhanVien nv = new NhanVien();
                    nv.nhap();
                    ql.themNhanVien(nv);
                    break;

                case 2:
                    ql.hienThiDanhSach();
                    break;

                case 3:
                    System.out.print("Nhap ma NV can tim: ");
                    String maTim = sc.nextLine();
                    ql.timNhanVien(maTim);
                    break;

                case 4:
                    System.out.print("Nhap ma NV can xoa: ");
                    String maXoa = sc.nextLine();
                    ql.xoaNhanVien(maXoa);
                    break;

                case 5:
                    ql.luuFile("nhanvien.dat");
                    break;

                case 6:
                    ql.docFile("nhanvien.dat");
                    break;

                case 0:
                    System.out.println("Ket thuc chuong trinh!");
                    break;

                default:
                    System.out.println("Lua chon khong hop le!");
            }

        } while (chon != 0);
    }
}