# 🤖 SYSTEM PROMPT & AGENT CONTEXT 

## [Project Overview]
- **Name:** PRM_Center_Kitchen_Management (Android Native Java).
- **Current State:** Authentication (Login) and Role-based Routing using Retrofit have been successfully implemented.
- **Backend API Base URL:** `https://wdp301-api.onrender.com/wdp301-api/v1/`

## [Architecture & Coding Rules]
The Agent MUST strictly adhere to the following principles when generating or refactoring code:
1. **Software Engineering Principles:** Strictly apply OOAD, SOLID, DRY (Don't Repeat Yourself), KISS (Keep It Simple, Stupid), and YAGNI.
2. **Package Isolation (Separation of Concerns):** Any new feature must be modularized (separate Models, Adapters, and Fragments/Activities). DO NOT write API calling logic directly inside UI controllers (Activities/Fragments).
3. **Naming Conventions:** - XML Files: Must use resource-type prefixes (e.g., `activity_admin_menu.xml`, `fragment_order_list.xml`, `item_product.xml`).
   - Java Classes: Use standard PascalCase (e.g., `OrderAdapter`, `InventoryFragment`).
4. **Networking:** Network calls via `Retrofit` MUST use `.enqueue()` (Asynchronous execution) to avoid blocking the Main Thread. Auth tokens must be automatically retrieved from the `SessionManager`.
5. **UI/UX:** Design user interfaces based on User-Centered Design (UCD), Consistency, and Minimalism.

## [Current Module Roles (The 5 Roles)]
The system handles 5 specific user roles and their corresponding landing activities:
1. `admin` -> AdminMenuActivity
2. `manager` -> ManagerMenuActivity
3. `supply_coordinator` -> SupplyCoordinatorActivity
4. `central_kitchen_staff` -> KitchenStaffActivity
5. `franchise_store_staff` -> FranchiseStaffActivity

## [Next Implementation Focus]
*(Update this section before assigning a new task to the Agent)*
- **Current Task:** Integrate API for ... (e.g., Inbound Logistics / Outbound / Order Management).
- **API Endpoint:** `/...`
- **Technical Requirements:** Use `RecyclerView` to display data lists, integrate `BottomNavigationView` with `Fragment` for inner-role navigation.