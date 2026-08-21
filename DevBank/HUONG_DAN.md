# MiniBank Console V2 — Chặng 1, bước 1

## Mục tiêu

- Tạo lớp `KhachHang`.
- Bảo vệ thuộc tính bằng `private`.
- Sử dụng constructor, getter và setter.
- Kiểm tra CCCD, số điện thoại, email và dữ liệu rỗng.
- Nhập dữ liệu bằng `Scanner` và kiểm thử chương trình.

## Cấu trúc

```text
MiniBank_Stage1_Buoc1/
├── KhachHang.java
├── Main.java
└── HUONG_DAN.md
```

## Biên dịch và chạy

Mở Terminal tại thư mục dự án rồi nhập:

```bash
javac -encoding UTF-8 KhachHang.java Main.java
java Main
```

## Dữ liệu hợp lệ để thử

```text
CCCD: 001204012345
Họ tên: Bùi Văn Bình
Số điện thoại: 0987654321
Email: binh@gmail.com
Địa chỉ: Hà Nội
```

## Dữ liệu sai nên thử

- CCCD `123`: chương trình bắt nhập lại.
- CCCD `00120abc2345`: chương trình bắt nhập lại.
- Số điện thoại `987654321`: chương trình bắt nhập lại.
- Email `binh@gmail`: chương trình bắt nhập lại.
- Họ tên hoặc địa chỉ để trống: chương trình bắt nhập lại.

## Kiến thức chính

`private` ngăn lớp bên ngoài truy cập trực tiếp dữ liệu. `Main` không thể viết
`khachHang.cccd = "..."`; dữ liệu chỉ được đưa vào qua constructor hoặc setter
có kiểm tra điều kiện.

Constructor không tham số tạo đối tượng rỗng. Constructor có tham số nhận đủ
thông tin và gọi các setter để kiểm tra dữ liệu trước khi gán.

`this.cccd = cccd` phân biệt thuộc tính `cccd` của đối tượng với tham số `cccd`
được truyền vào phương thức.

`throw new IllegalArgumentException(...)` dừng việc gán khi dữ liệu không hợp
lệ, tránh tạo đối tượng khách hàng chứa dữ liệu sai.
