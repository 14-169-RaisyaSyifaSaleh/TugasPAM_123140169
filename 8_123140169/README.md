# Tugas 8 Pengembangan Aplikasi Mobile RB 

**Nama:** Raditya Alrasyid Nugroho  
**NIM:** 123140125  
**Kelas:** Pengembangan Aplikasi Mobile RB

# 📱 Tugas Praktikum Minggu 8 - Platform Specific Features
## 📌 Deskripsi Tugas
Pada tugas ini dilakukan pengembangan aplikasi Notes App dengan menambahkan fitur platform-specific menggunakan pendekatan Dependency Injection (Koin) serta implementasi fitur device dan network.

## 🎯 Tujuan
- Mengimplementasikan Dependency Injection menggunakan Koin
- Mengakses informasi perangkat (Device Info)
- Mendeteksi status jaringan (Network Monitor)
- Menampilkan informasi device dan status network di UI

---
## ⚙️ Fitur yang Diimplementasikan

### 1. Dependency Injection (Koin)
- Menggunakan Koin untuk mengelola dependency
- Semua dependency di-inject melalui module Koin

### 2. Device Info (Expect/Actual)
- Menggunakan konsep `expect/actual`
- Menampilkan informasi device seperti:
  - OS
  - Versi
  - Device Name

### 3. Network Monitor (Expect/Actual)
- Mendeteksi status jaringan (Online / Offline)
- Implementasi berbeda untuk setiap platform

### 4. Settings Screen
- Menampilkan informasi device
- UI sederhana untuk menampilkan data device

### 5. Network Status Indicator
- Indikator status network pada main screen
- Update secara real-time

---

## 🏗️ Arsitektur
Aplikasi menggunakan pendekatan modular dengan:
- Presentation Layer (UI)
- Domain Layer
- Data Layer
- Dependency Injection (Koin)

---

## 📸 Screenshot

### Device Info
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/69dbb6e6-6e7a-441b-a10d-554b0d315edb" />

### Network Indicator
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/2337e24a-de90-445a-aa0a-66fc793b658b" />

## 🚀 Cara Menjalankan Project

### 1. Clone repository:
```bash
git clone https://github.com/username/repository.git
```
### 2. Buka project di Android Studio
### 3. Sync Gradle
### 4. Jalankan aplikasi di emulator / device

## 🎥 Video Demo
https://s.itera.id/6k3Cdu
