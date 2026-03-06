# 🤖 SYSTEM PROMPT & AGENT CONTEXT

## [1. Project Overview]
- **Project Name:** PRM_Center_Kitchen_Management
- **Platform:** Android Native (Java, SDK 34+)
- **Architecture:** MVVM / MVC (Clean Architecture approach with UI/Logic separation)
- **Backend API Base URL:** `https://wdp301-api.onrender.com/wdp301-api/v1/`
- **Current State:** The Base Architecture is fully established. Authentication, Token Management, Router (`MainActivity`), Base Navigation Framework, and Role-based Dynamic Routing are implemented.

## [2. Core Design Principles]
All code generated or refactored MUST strictly adhere to the following principles:
- **Software Engineering:** - **SOLID** Principles.
  - **DRY** (Don't Repeat Yourself): Utilize Base classes and shared utilities.
  - **KISS** (Keep It Simple, Stupid): Avoid over-engineering.
  - **YAGNI** (You Ain't Gonna Need It): Implement only what is explicitly required.
  - **OOAD** (Object-Oriented Analysis and Design): Clearly define *what* the system does (OOA) before *how* it is built (OOD).
- **UI/UX (User-Centered Design):** Maintain Consistency and Minimalism across all screens.
- **System & Performance:** Security from the start (Token validation), Scalability (Modular package structure), and Speed optimization.

## [3. Codebase Architecture & Rules]

### 3.1. UI & Navigation Architecture
- **`BaseActivity`:** All standalone activities MUST extend `BaseActivity` to inherit common UI state logic (`showLoading()`, `hideLoading()`, `showToast()`, `handleApiError()`).
- **`BaseNavigationActivity`:** All role-based main screens MUST extend this class. It provides the framework for `BottomNavigationView` and `FrameLayout` fragment hosting.
  - Subclasses must implement `getContentViewId()` to provide their specific XML layout (e.g., `R.layout.activity_manager_menu`).
  - Subclasses must implement `setupBottomNavigation()` to inflate their specific menu and handle fragment routing.
- **Fragments:** Features are built as Fragments, NOT Activities.
  - **Structure:** Fragments MUST be categorized by role inside `fragment/roles/<role_name>/` (e.g., `fragment/roles/manager/ManagerDashboardFragment.java`).
  - **Shared Fragments:** Features shared across multiple roles (like `ProfileFragment`) MUST be placed in `fragment/share/`.

### 3.2. Networking & API (Retrofit)
- **Interceptor:** The `ApiClient` automatically injects the `Bearer` token from `SessionManager` into headers. DO NOT manually pass tokens in `ApiService` interface methods.
- **Execution:** All network calls MUST be asynchronous using `.enqueue()`. NEVER block the Main UI Thread.
- **Response Handling:** - Validate `response.isSuccessful()` and `response.body() != null` before processing.
  - Handle Token Expiration (401 Unauthorized) gracefully by clearing `SessionManager` and routing the user back to `LoginActivity`.

### 3.3. Naming Conventions & Package Structure
- **Packages:** `activity`, `fragment`, `api`, `model` (split into `request` and `response`), `utils`.
- **Java Classes:** `PascalCase` (e.g., `ProfileFragment`, `LoginResponse`).
- **XML Layouts:** `snake_case` with explicit type prefixes:
  - `activity_<name>.xml` (e.g., `activity_manager_menu.xml`)
  - `fragment_<name>.xml` (e.g., `fragment_profile.xml`)
  - `dialog_<name>.xml` (e.g., `dialog_update_profile.xml`)
  - `item_<name>.xml` (for RecyclerView items)

## [4. User Roles (The 5 Core Actors)]
The system routes users to different Dashboard Activities based on their decoded JWT role:
1. `admin` -> `AdminMenuActivity`
2. `manager` -> `ManagerMenuActivity`
3. `supply_coordinator` -> `SupplyCoordinatorActivity`
4. `central_kitchen_staff` -> `KitchenStaffActivity`
5. `franchise_store_staff` -> `FranchiseStaffActivity`

## [5. Next Implementation Focus]
*(Developer: Update this section before assigning a new task to the Agent)*
- **Current Task:** [Describe the feature to be built, e.g., Implement Order List for Manager]
- **Target Role:** [e.g., Manager]
- **API Endpoint:** `[GET] /api/...`
- **Technical Requirements:** [e.g., Use RecyclerView, Create Item Layout, Bind Data]