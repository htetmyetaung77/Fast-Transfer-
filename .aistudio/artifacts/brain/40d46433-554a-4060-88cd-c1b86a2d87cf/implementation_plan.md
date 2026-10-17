# Real Device App Listing & APK Extraction

Implement real device package manager queries (`packageManager.getInstalledPackages`) to list all installed applications on the device, with category filters (User Apps vs System Apps), search capabilities, and APK backup/extraction for instant P2P transfer.

## User Review & Critical Decisions

> [!IMPORTANT]
> The app will query the device's actual package manager to list installed applications, allowing users to filter by User Apps or System Apps, search by name, extract APKs, and share them instantly via P2P.

- **Confirmed Decision 1**: Filter categories for User Installed Apps vs System Apps.
- **Confirmed Decision 2**: Internal app backup storage with direct handoff to the 100 MB/s P2P transfer station.

## 1. Overview & Core Concept

- **What It Does**: Queries `PackageManager` to retrieve all installed apps, extracts package info (app name, package name, version, icon, source directory), and provides backup and direct sharing.
- **Key Value**: Real utility for backing up installed apps and sharing APKs offline.

## 2. User Experience & Visual Design

- **AppsShareScreen Updates**:
  - Filter tabs: "User Apps" and "System Apps".
  - Search bar for quick app lookup.
  - App item cards showing app icon, name, package name, size, version, and "Extract & Share" action button.
  - Success toast / banner upon successful APK extraction.

## 3. Technical Implementation

- Create `AppInfo` data model and update `FlashShareViewModel` to load real installed apps via `PackageManager` (with robust fallback sample data if running in sandbox environments where package query flags need permissions).
- Implement APK extraction action that saves the APK reference to backup storage and queues it for P2P transfer.
