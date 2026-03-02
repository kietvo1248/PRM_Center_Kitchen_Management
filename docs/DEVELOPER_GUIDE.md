# Hướng dẫn Phát triển Dự án PRM Center Kitchen Management

## 1. Cấu trúc Project (Source Code Structure)
Ứng dụng được tổ chức theo mô hình phân lớp rõ ràng tại package `com.example.prm_center_kitchen_management`:

- `activity/`: Chứa các màn hình của ứng dụng.
    - `auth/`: Xử lý đăng nhập (`LoginActivity`).
    - `roles/`: Các màn hình riêng biệt cho từng vai trò (`AdminMenuActivity`, `ManagerMenuActivity`, `SupplyCoordinatorActivity`, v.v.).
- `api/`: Cấu hình mạng.
    - `ApiClient`: Khởi tạo Retrofit client.
    - `ApiService`: Định nghĩa các Endpoint gọi đến server.
- `model/`: Các lớp dữ liệu (POJO) để parse JSON.
    - `request/`: Chứa các object gửi đi (vd: `LoginRequest`).
    - `response/`: Chứa các object nhận về (vd: `AuthData`).
- `utils/`: Các tiện ích bổ trợ.
    - `SessionManager`: Quản lý lưu trữ SharedPreferences, Token và thông tin người dùng.

## 2. Luồng xử lý chính
1. **Đăng nhập:** `LoginActivity` nhận input -> gọi `ApiService.login()` -> Lưu token vào `SessionManager` -> Chuyển hướng màn hình dựa trên `role`.
2. **Phân quyền:** Sau khi login, ứng dụng kiểm tra `role` từ response để mở đúng Activity trong thư mục `roles/`.

## 3. Cấu hình Agent (Dành cho AI/Developer)
Để Agent hiểu và hỗ trợ tốt nhất cho dự án này, hãy tuân thủ các quy tắc sau:

- **Namespace:** Luôn sử dụng `com.example.prm_center_kitchen_management`.
- **UI/Layout:** Ưu tiên sử dụng Material Design components. Theme màu chính là Đỏ (#D32F2F) - đặc trưng của thương hiệu gà rán VFC.
- **Networking:** Tất cả các request phải đi qua `ApiService`. Không khởi tạo Retrofit riêng lẻ trong Activity.
- **Security:** Token phải được lấy từ `SessionManager` và đính kèm vào header nếu cần (sử dụng Interceptor trong `ApiClient`).
- **BuildConfig:** Sử dụng `BuildConfig.API_BASE_URL` cho tất cả các endpoint. Giá trị này được cấu hình trong `local.properties`.

## 4. Cách thêm tính năng mới
1. Định nghĩa model trong `model/`.
2. Thêm endpoint vào `ApiService`.
3. Tạo Activity/Fragment mới trong thư mục `activity/` tương ứng.
4. Cập nhật Layout trong `res/layout/`.


## [Architecture & Coding Rules]
khi code hoặc refactor cần tuân thủ các nguyên tắc sau:
1. **Design Pattern:** áp dụng OOAD, SOLID, DRY (Don't Repeat Yourself), KISS (Keep It Simple, Stupid), và YAGNI.
2. **Package Isolation:** Bất kỳ chức năng mới nào cũng phải tách biệt (Model riêng, Adapter riêng, Fragment/Activity riêng). KHÔNG viết code logic API trực tiếp vào Activity.
3. **Naming Convention:** - XML Files: Bắt buộc dùng tiền tố (VD: `activity_admin_menu.xml`, `fragment_order_list.xml`, `item_product.xml`).
   - Java Classes: Dùng PascalCase chuẩn (VD: `OrderAdapter`, `InventoryFragment`).
4. **Networking:** Gọi mạng qua `Retrofit` phải dùng `.enqueue()` (Bất đồng bộ) để không block Main Thread. Token phải được tự động lấy từ `SessionManager`.
5. **UI/UX:** Thiết kế giao diện theo User-Centered Design (UCD), Consistency và Minimalism.

## [Current Module Roles (The 5 Roles)]
Hệ thống xử lý 5 roles:
1. `admin` -> AdminMenuActivity
2. `manager` -> ManagerMenuActivity
3. `supply_coordinator` -> SupplyCoordinatorActivity
4. `central_kitchen_staff` -> KitchenStaffActivity
5. `franchise_store_staff` -> FranchiseStaffActivity

## [Next Implementation Focus]
*(Cập nhật phần này trước khi giao task mới cho Agent)*
- Task hiện tại: Tích hợp API cho chức năng ... (Nhập kho/Xuất kho/Quản lý đơn hàng).
- API Endpoint: `/...`
- Yêu cầu kỹ thuật: Sử dụng `RecyclerView` hiển thị danh sách, lồng ghép `BottomNavigationView` và `Fragment` để điều hướng trong từng Role Activity.

---
*Tài liệu này được cập nhật tự động để hỗ trợ quá trình phát triển.*
