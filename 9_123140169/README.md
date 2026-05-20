# Tugas PAM - Integrasi AI (Tugas 9)

**Nama:** Raisya Syifa Saleh
**NIM:** 123140169
**Kelas:** Pengembangan Aplikasi Mobile RB

## Fitur AI: Smart Note Assistant
Aplikasi ini sekarang terintegrasi dengan **Gemini AI** untuk membantu pengguna mengelola catatan mereka secara lebih cerdas.

### Fitur Utama AI:
1.  **Smart Chatbot/Assistant**: Pengguna dapat berdiskusi dengan AI mengenai isi catatan tertentu.
2.  **Content Summarization**: AI dapat merangkum isi catatan yang panjang menjadi poin-poin penting.
3.  **Multi-turn Conversation**: Percakapan berkelanjutan yang mengingat konteks catatan (Fitur Bonus).
4.  **Streaming Response**: Jawaban AI ditampilkan secara real-time saat sedang dibuat (Fitur Bonus).

### Detail Implementasi:
*   **Gemini API**: Menggunakan SDK `generativeai` versi 0.9.0.
*   **Prompt Engineering**: Menggunakan *System Instruction* yang dirancang khusus agar AI bertindak sebagai asisten catatan yang profesional dan ringkas.
*   **Error Handling**: Implementasi status UI (Idle, Loading, Success, Error) untuk menangani kegagalan API atau masalah jaringan secara anggun.
*   **UI/UX**: Dialog chat yang responsif dengan *loading indicators* dan bubble chat yang intuitif.

### Cara Penggunaan:
1.  Buka salah satu catatan yang ada di daftar.
2.  Klik ikon **AI (Sparkles/AutoAwesome)** di Top Bar.
3.  Ketik pertanyaan atau minta rangkuman di kolom chat yang muncul.

---
*Catatan: API Key Gemini perlu dikonfigurasi di `AppModule.kt` agar fitur ini dapat berjalan.*
