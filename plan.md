Got it — plain text, copy-paste ready. Everything below.

HOTEL ERP — COMPLETE SPECIFICATION

STACK
Frontend   : React + Tailwind + React Query + Axios
Backend    : Spring Boot (Java)
Database   : MySQL
ORM        : JPA + Hibernate
Migrations : Flyway
Auth       : Spring Security + JWT
Tenancy    : Shared DB — hotel_id on every table

JWT TOKEN PAYLOAD
json{
  "sub":       "user_12",
  "hotelId":   3,
  "role":      "RECEPTIONIST",
  "hotelName": "Soluxe Club",
  "exp":       1234567890
}
```

---

### USER ROLES
```
SUPER_ADMIN   — You. No hotelId. Sees all hotels.
HOTEL_ADMIN   — Owner/GM. Full access to their hotel.
MANAGER       — Day-to-day ops. Most access, no system settings.
RECEPTIONIST  — Check-in, check-out, reservations, guests.
CASHIER       — Folio charges, invoices, checkout.
WAITER        — Restaurant POS only.
STORE_KEEPER  — Inventory, suppliers, purchase orders.
HOUSEKEEPING  — Room status updates only.
```

---

### PERMISSIONS MATRIX
```
Module              SUPER  ADMIN  MANAGER  RECEPT  CASHIER  WAITER  STORE  HOUSE
Hotel Settings        ✅     ✅      ❌       ❌       ❌       ❌      ❌      ❌
Users                 ✅     ✅      ❌       ❌       ❌       ❌      ❌      ❌
Employees             ✅     ✅      ✅       ❌       ❌       ❌      ❌      ❌
Attendance            ✅     ✅      ✅      👁own    👁own    👁own   👁own  👁own
Leave                 ✅     ✅    ✅appr   ✏️req    ✏️req    ✏️req  ✏️req  ✏️req
Rooms                 ✅     ✅      ✅      👁        ❌       ❌      ❌    ✅stat
Guests                ✅     ✅      ✅       ✅       👁        ❌      ❌      ❌
Reservations          ✅     ✅      ✅       ✅       👁        ❌      ❌      ❌
Check-In              ✅     ✅      ✅       ✅        ❌       ❌      ❌      ❌
Check-Out             ✅     ✅      ✅       ✅        ✅       ❌      ❌      ❌
Folio Charges         ✅     ✅      ✅      ✏️add      ✅       ❌      ❌      ❌
Folio Invoice         ✅     ✅      ✅       👁        ✅       ❌      ❌      ❌
Restaurant POS        ✅     ✅      ✅        ❌        ✅       ✅      ❌      ❌
Menu Management       ✅     ✅      ✅        ❌        ❌       ❌      ❌      ❌
Venue Bookings        ✅     ✅      ✅        ✅      ✏️bill    ❌      ❌      ❌
Housekeeping          ✅     ✅      ✅        👁        ❌       ❌      ❌      ✅
Inventory             ✅     ✅      ✅        ❌        ❌       ❌      ✅      ❌
Suppliers             ✅     ✅      ✅        ❌        ❌       ❌      ✅      ❌
Purchase Orders       ✅     ✅    ✅appr      ❌        ❌       ❌    ✏️crt    ❌
Reports               ✅     ✅      ✅       👁lim     👁lim    ❌    👁stock   ❌
```

---

### MODULE 1 — HOTEL CORE
```
Hotel
  id                BIGINT PK
  name              VARCHAR(255)
  address           TEXT
  phone             VARCHAR(50)
  email             VARCHAR(255)
  vat_number        VARCHAR(100)
  logo              VARCHAR(500)
  currency          VARCHAR(10)       default: KES
  timezone          VARCHAR(100)      default: Africa/Nairobi
  invoice_footer    TEXT
  check_in_time     TIME              default: 12:00
  check_out_time    TIME              default: 10:00
  is_active         BOOLEAN
  created_at        DATETIME
  updated_at        DATETIME

HotelSettings
  id                    BIGINT PK
  hotel_id              BIGINT FK
  allow_walk_in         BOOLEAN
  allow_complimentary   BOOLEAN
  tax_rate              DECIMAL(5,2)
  service_charge_rate   DECIMAL(5,2)
  late_checkout_charge  DECIMAL(10,2)
  extra_bed_charge      DECIMAL(10,2)
```

---

### MODULE 2 — USERS & AUTH
```
User
  id              BIGINT PK
  hotel_id        BIGINT FK         null if SUPER_ADMIN
  employee_id     BIGINT FK         nullable
  username        VARCHAR(100)      unique per hotel
  password_hash   VARCHAR(255)      bcrypt
  role            ENUM              SUPER_ADMIN, HOTEL_ADMIN, MANAGER,
                                    RECEPTIONIST, CASHIER, WAITER,
                                    STORE_KEEPER, HOUSEKEEPING
  is_active       BOOLEAN
  last_login      DATETIME          nullable
  created_at      DATETIME
```

---

### MODULE 3 — EMPLOYEES
```
Employee
  id                BIGINT PK
  hotel_id          BIGINT FK
  full_name         VARCHAR(255)
  gender            ENUM            MALE, FEMALE, OTHER
  date_of_birth     DATE            nullable
  phone             VARCHAR(50)
  email             VARCHAR(255)    nullable
  address           TEXT            nullable
  department        ENUM            FRONT_OFFICE, RESTAURANT,
                                    HOUSEKEEPING, KITCHEN,
                                    STORE, MANAGEMENT
  designation       VARCHAR(100)    e.g. Receptionist, Chef
  date_of_joining   DATE
  basic_salary      DECIMAL(12,2)
  photo             VARCHAR(500)
  is_active         BOOLEAN

AttendanceRecord
  id              BIGINT PK
  hotel_id        BIGINT FK
  employee_id     BIGINT FK
  date            DATE
  clock_in        TIME              nullable
  clock_out       TIME              nullable
  hours_worked    DECIMAL(4,2)      computed on clock-out
  status          ENUM              PRESENT, ABSENT, HALF_DAY,
                                    LEAVE, HOLIDAY
  notes           TEXT              nullable
  recorded_by     BIGINT FK         user

LeaveRequest
  id              BIGINT PK
  hotel_id        BIGINT FK
  employee_id     BIGINT FK
  leave_type      ENUM              ANNUAL, SICK, EMERGENCY, UNPAID
  date_from       DATE
  date_to         DATE
  reason          TEXT
  status          ENUM              PENDING, APPROVED, REJECTED
  approved_by     BIGINT FK         user, nullable
  created_at      DATETIME
```

---

### MODULE 4 — ROOMS
```
RoomType
  id              BIGINT PK
  hotel_id        BIGINT FK
  name            VARCHAR(100)      e.g. Deluxe Twin, Suite
  description     TEXT              nullable
  max_adults      INT
  max_children    INT
  default_rate    DECIMAL(10,2)

RoomPricePlan
  id              BIGINT PK
  hotel_id        BIGINT FK
  room_type_id    BIGINT FK
  plan_code       ENUM              ROOM_ONLY, BED_BREAKFAST,
                                    HALF_BOARD, FULL_BOARD
  plan_name       VARCHAR(100)
  rate_per_night  DECIMAL(10,2)

Room
  id              BIGINT PK
  hotel_id        BIGINT FK
  room_type_id    BIGINT FK
  room_number     VARCHAR(20)
  floor           VARCHAR(20)       nullable
  status          ENUM              VACANT_CLEAN, VACANT_DIRTY,
                                    OCCUPIED_CLEAN, OCCUPIED_DIRTY,
                                    MAINTENANCE, DO_NOT_DISTURB
```

---

### MODULE 5 — GUESTS
```
Guest
  id                BIGINT PK
  hotel_id          BIGINT FK
  full_name         VARCHAR(255)
  gender            ENUM            MALE, FEMALE, OTHER
  date_of_birth     DATE            nullable
  nationality       VARCHAR(100)    nullable
  id_type           ENUM            PASSPORT, NATIONAL_ID,
                                    DRIVING_LICENSE
  id_number         VARCHAR(100)
  phone             VARCHAR(50)
  email             VARCHAR(255)    nullable
  address           TEXT            nullable
  city              VARCHAR(100)    nullable
  country           VARCHAR(100)    nullable
  company           VARCHAR(255)    nullable
  occupation        VARCHAR(100)    nullable
  purpose_of_visit  VARCHAR(255)    nullable
  is_blocked        BOOLEAN
  blocked_reason    TEXT            nullable
  notes             TEXT            nullable

GuestDocument
  id              BIGINT PK
  hotel_id        BIGINT FK
  guest_id        BIGINT FK
  document_type   ENUM            ID_FRONT, ID_BACK, PASSPORT, PHOTO
  file_path       VARCHAR(500)
  uploaded_at     DATETIME
```

---

### MODULE 6 — RESERVATIONS
```
Reservation
  id                BIGINT PK
  hotel_id          BIGINT FK
  guest_id          BIGINT FK
  room_type_id      BIGINT FK       type requested, not specific room
  plan_code         ENUM            ROOM_ONLY, BED_BREAKFAST,
                                    HALF_BOARD, FULL_BOARD
  date_in           DATE
  date_out          DATE
  adults            INT
  children          INT             default 0
  special_request   TEXT            nullable
  business_source   ENUM            WALK_IN, PHONE, AGENT,
                                    ONLINE, CORPORATE
  agent_name        VARCHAR(255)    nullable
  commission_pct    DECIMAL(5,2)    nullable
  status            ENUM            PENDING, CONFIRMED, CHECKED_IN,
                                    CHECKED_OUT, CANCELLED, NO_SHOW
  created_by        BIGINT FK       user
  notes             TEXT            nullable
  created_at        DATETIME
```

---

### MODULE 7 — STAY (CHECK-IN / CHECK-OUT)
```
Stay
  id                    BIGINT PK
  hotel_id              BIGINT FK
  reservation_id        BIGINT FK       nullable — null for walk-ins
  guest_id              BIGINT FK
  room_id               BIGINT FK       specific room assigned
  plan_code             ENUM
  rate_per_night        DECIMAL(10,2)   locked at check-in
  date_in               DATETIME
  date_out              DATETIME        expected checkout
  actual_date_out       DATETIME        filled at checkout
  adults                INT
  children              INT
  is_complimentary      BOOLEAN
  business_source       ENUM
  arriving_from         VARCHAR(255)    nullable
  next_destination      VARCHAR(255)    nullable
  arrival_flight_no     VARCHAR(50)     nullable
  departure_flight_no   VARCHAR(50)     nullable
  card_encoded          BOOLEAN         Archie — future integration
  checked_in_by         BIGINT FK       user
  checked_out_by        BIGINT FK       user, nullable
  status                ENUM            ACTIVE, CHECKED_OUT, CANCELLED
  notes                 TEXT            nullable

StayAdditionalGuest
  id              BIGINT PK
  hotel_id        BIGINT FK
  stay_id         BIGINT FK
  full_name       VARCHAR(255)
  id_type         ENUM              nullable
  id_number       VARCHAR(100)      nullable
```

---

### MODULE 8 — FOLIO & BILLING
```
Folio
  id                    BIGINT PK
  hotel_id              BIGINT FK
  stay_id               BIGINT FK       nullable
  venue_booking_id      BIGINT FK       nullable
  dining_session_id     BIGINT FK       nullable
  folio_type            ENUM            STAY, VENUE, DINING
  status                ENUM            OPEN, CLOSED
  opened_at             DATETIME
  closed_at             DATETIME        nullable
  total_amount          DECIMAL(12,2)   sum of all charges

FolioCharge
  id              BIGINT PK
  hotel_id        BIGINT FK
  folio_id        BIGINT FK
  charge_type     ENUM            ROOM, FOOD, LAUNDRY, ROOM_SERVICE,
                                  CONFERENCE, DAMAGE,
                                  LATE_CHECKOUT, OTHER
  description     VARCHAR(255)
  quantity        DECIMAL(8,2)
  unit_price      DECIMAL(10,2)
  discount_pct    DECIMAL(5,2)    default 0
  tax_pct         DECIMAL(5,2)    default 0
  total_amount    DECIMAL(12,2)   computed
  charged_at      DATETIME
  added_by        BIGINT FK       user
```

---

### MODULE 9 — RESTAURANT & POS
```
MenuCategory
  id              BIGINT PK
  hotel_id        BIGINT FK
  name            VARCHAR(100)    e.g. Breakfast, Mains, Bar
  sort_order      INT

MenuItem
  id              BIGINT PK
  hotel_id        BIGINT FK
  category_id     BIGINT FK
  name            VARCHAR(255)
  description     TEXT            nullable
  price           DECIMAL(10,2)
  tax_pct         DECIMAL(5,2)    default 0
  is_available    BOOLEAN

RestaurantTable
  id              BIGINT PK
  hotel_id        BIGINT FK
  table_name      VARCHAR(50)     e.g. Table 1, Poolside 2
  capacity        INT
  location        ENUM            MAIN, POOLSIDE, ROOFTOP, BAR
  status          ENUM            AVAILABLE, OCCUPIED, RESERVED

DiningSession
  id              BIGINT PK
  hotel_id        BIGINT FK
  table_id        BIGINT FK
  stay_id         BIGINT FK       nullable — null for walk-ins
  guest_name      VARCHAR(255)    for walk-ins
  guest_phone     VARCHAR(50)     nullable
  billing_type    ENUM            CHARGE_TO_ROOM, PAY_NOW
  served_by       BIGINT FK       waiter user
  opened_at       DATETIME
  closed_at       DATETIME        nullable
  status          ENUM            OPEN, BILLED, PAID
  total_amount    DECIMAL(12,2)

DiningOrder
  id              BIGINT PK
  hotel_id        BIGINT FK
  session_id      BIGINT FK
  menu_item_id    BIGINT FK
  quantity        INT
  unit_price      DECIMAL(10,2)   locked at order time
  discount_pct    DECIMAL(5,2)    default 0
  tax_pct         DECIMAL(5,2)
  total_amount    DECIMAL(12,2)
  notes           TEXT            e.g. no onions
  status          ENUM            PENDING, PREPARING,
                                  SERVED, CANCELLED
  ordered_at      DATETIME
```

---

### MODULE 10 — VENUES
```
Venue
  id              BIGINT PK
  hotel_id        BIGINT FK
  name            VARCHAR(255)    e.g. Main Hall, Garden, Boardroom
  type            ENUM            CONFERENCE, GARDEN, BOARDROOM
  capacity        INT
  rate_per_hour   DECIMAL(10,2)   nullable
  rate_per_day    DECIMAL(10,2)   nullable
  description     TEXT            nullable

VenueBooking
  id                BIGINT PK
  hotel_id          BIGINT FK
  venue_id          BIGINT FK
  client_name       VARCHAR(255)
  client_phone      VARCHAR(50)
  client_company    VARCHAR(255)    nullable
  event_type        ENUM            CONFERENCE, WEDDING, PARTY,
                                    MEETING, OTHER
  date_in           DATE
  date_out          DATE
  start_time        TIME
  end_time          TIME
  expected_guests   INT
  setup_type        ENUM            THEATER, CLASSROOM,
                                    BANQUET, BOARDROOM
  deposit           DECIMAL(12,2)   nullable
  total_amount      DECIMAL(12,2)
  status            ENUM            TENTATIVE, CONFIRMED,
                                    COMPLETED, CANCELLED
  notes             TEXT            nullable
  created_by        BIGINT FK       user

VenueBookingCharge
  id                  BIGINT PK
  hotel_id            BIGINT FK
  venue_booking_id    BIGINT FK
  description         VARCHAR(255)    e.g. Catering, AV Equipment
  quantity            DECIMAL(8,2)
  unit_price          DECIMAL(10,2)
  total_amount        DECIMAL(12,2)
```

---

### MODULE 11 — HOUSEKEEPING
```
HousekeepingLog
  id              BIGINT PK
  hotel_id        BIGINT FK
  room_id         BIGINT FK
  action          ENUM            CLEANED, INSPECTED,
                                  MAINTENANCE_REQUESTED,
                                  MAINTENANCE_RESOLVED,
                                  DO_NOT_DISTURB
  notes           TEXT            nullable
  performed_by    BIGINT FK       user
  performed_at    DATETIME
```

---

### MODULE 12 — INVENTORY
```
Supplier
  id              BIGINT PK
  hotel_id        BIGINT FK
  name            VARCHAR(255)
  phone           VARCHAR(50)
  email           VARCHAR(255)    nullable
  address         TEXT            nullable
  notes           TEXT            nullable

InventoryCategory
  id              BIGINT PK
  hotel_id        BIGINT FK
  name            VARCHAR(100)    e.g. Kitchen, Housekeeping, Bar

InventoryItem
  id                BIGINT PK
  hotel_id          BIGINT FK
  category_id       BIGINT FK
  supplier_id       BIGINT FK       default supplier, nullable
  name              VARCHAR(255)
  unit              ENUM            KG, LITRES, PIECES,
                                    BOXES, PACKETS, OTHER
  current_stock     DECIMAL(10,2)
  minimum_stock     DECIMAL(10,2)   alert threshold
  notes             TEXT            nullable

PurchaseOrder
  id                BIGINT PK
  hotel_id          BIGINT FK
  supplier_id       BIGINT FK
  order_date        DATE
  expected_date     DATE            nullable
  status            ENUM            PENDING, RECEIVED, CANCELLED
  notes             TEXT            nullable
  created_by        BIGINT FK       user

PurchaseOrderItem
  id                    BIGINT PK
  hotel_id              BIGINT FK
  purchase_order_id     BIGINT FK
  inventory_item_id     BIGINT FK
  quantity_ordered      DECIMAL(10,2)
  quantity_received     DECIMAL(10,2)   nullable, filled on receipt
  unit_price            DECIMAL(10,2)
```

---

### ENTITY COUNT
```
Module 1  — Hotel Core          : 2
Module 2  — Users & Auth        : 1
Module 3  — Employees           : 3
Module 4  — Rooms               : 3
Module 5  — Guests              : 2
Module 6  — Reservations        : 1
Module 7  — Stay                : 2
Module 8  — Folio & Billing     : 2
Module 9  — Restaurant & POS    : 5
Module 10 — Venues              : 3
Module 11 — Housekeeping        : 1
Module 12 — Inventory           : 5
─────────────────────────────────
TOTAL                           : 30 entities
```

---

### SPRING BOOT PROJECT STRUCTURE
```
hotel-saas/
├── src/main/java/com/hotel/
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   └── FlywayConfig.java
│   ├── security/
│   │   ├── JwtFilter.java
│   │   ├── JwtUtil.java
│   │   └── TenantContext.java
│   ├── shared/
│   │   ├── BaseEntity.java
│   │   ├── ApiResponse.java
│   │   └── enums/
│   └── modules/
│       ├── hotel/
│       ├── auth/
│       ├── employee/
│       │   ├── attendance/
│       │   └── leave/
│       ├── room/
│       ├── guest/
│       ├── reservation/
│       ├── stay/
│       ├── folio/
│       ├── restaurant/
│       │   ├── menu/
│       │   ├── table/
│       │   └── order/
│       ├── venue/
│       ├── housekeeping/
│       └── inventory/
│           ├── supplier/
│           └── purchaseorder/
├── src/main/resources/
│   ├── db/migration/
│   │   ├── V1__create_hotel.sql
│   │   ├── V2__create_users.sql
│   │   ├── V3__create_employees.sql
│   │   ├── V4__create_rooms.sql
│   │   ├── V5__create_guests.sql
│   │   ├── V6__create_reservations.sql
│   │   ├── V7__create_stays.sql
│   │   ├── V8__create_folio.sql
│   │   ├── V9__create_restaurant.sql
│   │   ├── V10__create_venues.sql
│   │   ├── V11__create_housekeeping.sql
│   │   └── V12__create_inventory.sql
│   └── application.yml
└── pom.xml
```

---

### SPRINT PLAN
```
Sprint 1 — The Spine
  Hotel Core, Auth, Employees, Rooms, Guests, Reservations

Sprint 2 — The Money
  Check-In, Check-Out, Folio, Charges, Invoice generation

Sprint 3 — Food
  Menu, Restaurant Tables, POS, Walk-in Dining, Room Service

Sprint 4 — Spaces & Stock
  Venues, Attendance, Leave, Inventory, Suppliers, POs

Sprint 5 — Polish
  Dashboard, Reports, Housekeeping board, edge cases, testing