# Tugas 7 Pengembangan Aplikasi Mobile RB 

**Nama:** Raditya Alrasyid Nugroho  
**NIM:** 123140125  
**Kelas:** Pengembangan Aplikasi Mobile RB

# 📱 Notes App - Praktikum Minggu 7

## 📌 Deskripsi
Aplikasi **Notes App** ini merupakan pengembangan dari aplikasi sebelumnya dengan menambahkan fitur database lokal, pencarian, pengaturan, serta konsep offline-first.
Aplikasi ini memungkinkan pengguna untuk membuat, mengedit, menghapus, dan mencari catatan dengan mudah serta tetap dapat digunakan tanpa koneksi internet.

---

## 🚀 Fitur Utama

- ✅ Penyimpanan data menggunakan **SQLDelight**
- ✅ CRUD (Create, Read, Update, Delete) Notes
- ✅ Fitur pencarian (Search Notes)
- ✅ Settings menggunakan **DataStore**
  - Dark / Light Mode
  - Sort Order
- ✅ Offline-first (data tersimpan lokal)
- ✅ UI State Handling:
  - Loading
  - Empty
  - Content

---

## 🏗️ Teknologi yang Digunakan

- Kotlin
- Jetpack Compose
- SQLDelight
- DataStore Preferences
- MVVM Architecture

---

## 🗄️ Database Schema

```sql
CREATE TABLE notes (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    created_at TEXT NOT NULL
);
```
## 🚀 Cara Menjalankan Aplikasi
1. Clone repository:
   ```bash
   git clone https://github.com/rdtngh/PraktikumPAM_123140125.git
2. Buka project di Android Studio
3. Sync Gradle
4. Jalankan aplikasi di emulator atau device

## 📸 Screenshot Aplikasi

### ➕ Add / Edit Note
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/d36b2afc-b8b2-474f-90b2-7002d030acc8" />
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/2366d279-e10f-4595-8f81-7feed14e5110" />

### 🔍 Search Notes
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/1ae01c7f-a2af-48fb-ac60-be86e67aae19" />

### ⚙️ Settings
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/f3993d5c-4a54-44bb-930d-bce0d5d03d0c" />

### Video Demonstrasi
https://s.itera.id/K5q2S8
