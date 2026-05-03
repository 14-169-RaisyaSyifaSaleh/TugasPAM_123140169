# Catatan Pintar AI (Tugas Praktikum)

Aplikasi manajemen catatan yang ditingkatkan dengan integrasi kecerdasan buatan (Gemini AI).

## Fitur AI Utama

### 1. Content Summarization (Ringkasan Catatan)
Meringkas isi catatan yang panjang secara otomatis menjadi poin-poin penting yang mudah dibaca.
- **Teknologi**: Google Gemini AI (gemini-1.5-flash).
- **Manfaat**: Menghemat waktu dalam meninjau catatan panjang.

### 2. Smart System Prompt
Menggunakan prompt sistem yang dirancang secara khusus untuk memastikan AI memberikan jawaban yang relevan, ramah, dan dalam Bahasa Indonesia.

## Persyaratan Teknis yang Dipenuhi

- [x] **Integrasi Gemini API**: Menggunakan model terbaru `gemini-1.5-flash`.
- [x] **Error Handling**: Implementasi penanganan error yang baik jika koneksi internet terputus atau API bermasalah.
- [x] **UI Responsif & Loading States**: Menggunakan `CircularProgressIndicator` dan dialog material 3 untuk memberikan umpan balik visual saat AI sedang bekerja.
- [x] **System Prompt Well-designed**: AI bertindak sebagai asisten catatan profesional.
- [x] **Dokumentasi README**: Fitur dijelaskan secara rinci di sini.

## Cara Penggunaan
1. Buka salah satu catatan yang ada.
2. Klik ikon ✨ (Magic/AI) di bilah navigasi atas.
3. Tunggu proses AI selesai (ditandai dengan loading indicator).
4. Hasil ringkasan akan muncul dalam dialog.

## Konfigurasi
Untuk menjalankan fitur AI, pastikan Anda telah memasukkan `API_KEY` di file `AppModule.kt`.
