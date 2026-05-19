package com.example.tugaspam3.data

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GeminiService(private val generativeModel: GenerativeModel) {

    private var chatSession: Chat? = null

    private val systemPrompt = """
        Anda adalah "Asisten Catatan AI" yang profesional dan ramah.
        Tugas Anda adalah membantu pengguna mengelola catatan dalam Bahasa Indonesia.
        
        Aturan Jawaban:
        1. Gunakan Bahasa Indonesia yang baik dan benar.
        2. Jika diminta meringkas, gunakan format poin-poin (bullet points).
        3. Jika memperbaiki tata bahasa, jelaskan bagian mana yang diperbaiki.
        4. Selalu berikan respon yang mendukung produktivitas.
    """.trimIndent()

    // Bonus: Streaming Response untuk Ringkasan
    fun summarizeNoteStream(content: String): Flow<String> {
        val prompt = "$systemPrompt\n\nTolong buatkan ringkasan dari catatan berikut:\n\n$content"
        return generativeModel.generateContentStream(prompt).map { it.text ?: "" }
    }

    // Bonus: Multi-turn Conversation (Chat)
    fun startNewSession(noteContent: String): Chat {
        val history = listOf(
            content(role = "user") { text("Ini adalah catatan saya: $noteContent") },
            content(role = "model") { text("Siap! Saya sudah memahami catatan Anda. Ada yang bisa saya bantu terkait catatan ini?") }
        )
        chatSession = generativeModel.startChat(history)
        return chatSession!!
    }

    fun sendMessageStream(message: String): Flow<String> {
        val session = chatSession ?: throw IllegalStateException("Chat session belum dimulai")
        return session.sendMessageStream(message).map { it.text ?: "" }
    }

    // Legacy support for non-streaming if needed
    suspend fun summarizeNote(content: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = generativeModel.generateContent("$systemPrompt\n\nRingkaslah:\n$content")
            Result.success(response.text ?: "Gagal memproses teks.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
