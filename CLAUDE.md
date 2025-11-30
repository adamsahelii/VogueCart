# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

VogueCart is an Android e-commerce mobile application built with Java. It's a shopping app that allows users to browse products across multiple categories (Fashion, Shoes, Electronics, Sports, Cosmetics), manage favorites, handle shopping cart operations, and complete purchases. The app communicates with a PHP backend API.

**Package**: `com.myproject.project_279`
**Min SDK**: 24 (Android 7.0)
**Target SDK**: 34 (Android 14)
**Compile SDK**: 34

## Common Commands

### Build and Run
```bash
# Build the project (multi-line)
./gradlew build

# Build debug APK (multi-line)
./gradlew assembleDebug

# Build debug APK (single-line)
./gradlew assembleDebug

# Build release APK (multi-line)
./gradlew assembleRelease

# Build release APK (single-line)
./gradlew assembleRelease

# Clean build (multi-line)
./gradlew clean

# Clean and build (single-line)
./gradlew clean build
```

### Testing
```bash
# Run all tests (multi-line)
./gradlew test

# Run all tests (single-line)
./gradlew test

# Run instrumentation tests (multi-line)
./gradlew connectedAndroidTest

# Run instrumentation tests (single-line)
./gradlew connectedAndroidTest

# Run specific test class (multi-line)
./gradlew test --tests com.myproject.project_279.ExampleUnitTest

# Run specific test class (single-line)
./gradlew test --tests com.myproject.project_279.ExampleUnitTest
```

### Dependencies
```bash
# Check for dependency updates (multi-line)
./gradlew dependencyUpdates

# View dependency tree (multi-line)
./gradlew app:dependencies

# View dependency tree (single-line)
./gradlew app:dependencies
```

## Architecture

### Application Flow
1. **Entry Point**: `MainActivity` - Welcome screen with Sign In/Sign Up options
2. **Authentication**: `SignInActivity` and `SignUpActivity` handle user registration/login
3. **Main Hub**: `MainPageActivity` - Primary navigation hub with:
   - Search functionality
   - Category browsing (Fashion, Shoes, Electronics, Sports, Cosmetics)
   - Bottom navigation (Home, Favorites, Scan, Cart, Profile)
   - Advertisement carousel with dot indicators

### Navigation Structure
- **Bottom Navigation Bar** (available from MainPageActivity):
  - Home → `MainPageActivity`
  - Favorites → `FavoritesActivity`
  - Scan → `ProductPageActivity`
  - Cart → `CartFragment`
  - Profile → `ProfilePage`

- **Category Pages** (all accessible from MainPageActivity):
  - Fashion → `FashionCategoryActivity`
  - Shoes → `ShoesCategoryActivity`
  - Electronics → `ElectronicsCategoryActivity`
  - Sports → `SportsCategoryActivity`
  - Cosmetics → `CosmeticsCategoryActivity`

- **Other Activities**:
  - `SearchResultsActivity` - Displays search results
  - `ProductPageActivity` - Individual product details
  - `CartActivity` / `CartFragment` - Shopping cart management
  - `OrderDetailActivity` - Order details view
  - `ChangePasswordActivity` - Password change functionality
  - `UploadPage` - Product upload functionality

### Networking Layer

**Backend Connection**:
- Base URL: `http://10.0.2.2/snapshop-api/` (Android emulator localhost)
- Technology: Retrofit 2 with Gson converter
- Client: `ApiClient.java` provides singleton Retrofit instance
- Service: `ApiService.java` defines all API endpoints

**API Endpoints**:
- **Products**:
  - GET `products.php` - Fetch all items or search with `?query=`
  - GET `product.php?id=` - Fetch single item by ID
- **Authentication**:
  - POST `login.php` - User login
  - POST `register.php` - User registration
- **Favorites**:
  - GET `favorites_get.php?user_id=` - Get user favorites
  - POST `favorites_add.php` - Add favorite (user_id, item_id)
  - POST `favorites_remove.php` - Remove favorite (user_id, item_id)
- **Cart**:
  - GET `cart_get.php?user_id=` - Get user cart
  - POST `cart_add.php` - Add to cart (user_id, item_id, quantity)
  - POST `cart_remove.php` - Remove from cart (user_id, item_id)

**Response Models**:
- `Item` - Product data model (implements Parcelable for Intent passing)
- `LoginResponse` - Authentication response
- `FavoritesResponse` - Favorites list response
- `CartResponse` - Cart data response
- `SearchResponse` - Search results response
- `SimpleResponse` - Generic API response

### Data Management

**Local Helpers** (SharedPreferences-based):
- `FavoritesHelper` - Manages local favorites state
- `AddToCartHelper` - Manages local cart operations

**Adapters** (RecyclerView):
- `SearchAdapter` - Search results display
- `FavoritesAdapter` - Favorites list display
- `AddToCartAdapter` - Cart items display
- `CheckoutAdapter` - Checkout items display
- `AdsPagerAdapter` - Advertisement carousel (ViewPager2)

### Core Data Model

`Item.java` is the central data model with fields:
- `id` (int) - Product identifier
- `name` (String) - Product name
- `price` (String) - Product price
- `image_url` (String) - Product image URL (matches backend field name)
- `category_name` (String) - Category classification
- `quantity` (int) - Item quantity (for cart operations)

**Note**: Item implements Parcelable for efficient passing between Activities via Intents.

## Key Dependencies

From `app/build.gradle.kts` and `gradle/libs.versions.toml`:

- **UI**: Material Design 1.12.0, ConstraintLayout 2.1.4
- **Networking**: Retrofit 2.9.0, OkHttp 4.9.1 (with logging interceptor)
- **Image Loading**: Glide 4.12.0 & 4.13.0
- **Carousel**: DotsIndicator 4.2
- **Testing**: JUnit 4.13.2, Espresso 3.6.1

## Development Notes

### Backend Configuration
- The app expects a PHP backend running at `http://10.0.2.2/snapshop-api/`
- 10.0.2.2 is the Android emulator's alias for the host machine's localhost
- For physical devices, update the BASE_URL in `ApiClient.java`
- Required permissions: INTERNET, ACCESS_NETWORK_STATE

### State Management
- Uses `onSaveInstanceState` / `onRestoreInstanceState` for activity state preservation
- Example in `MainPageActivity`: preserves search text and checkbox states across configuration changes

### Image Loading
- Glide is used throughout the app for efficient image loading from URLs
- Images are loaded from the backend via `image_url` field in Item model

### UI Patterns
- Bottom navigation implemented with ImageButtons and manual click listeners
- Category browsing uses ImageView click listeners
- Advertisement carousel uses ViewPager2 with custom dot indicators
- Search functionality uses EditText with search icon click listener
