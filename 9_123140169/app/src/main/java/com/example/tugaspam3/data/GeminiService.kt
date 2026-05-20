package com.example.tugaspam3.data

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Service class to handle Gemini AI operations.
 * Implements AI integration and Prompt Engineering requirements.
 */
class GeminiService(apiKey: String) {
    
    // System instruction to guide the AI's behavior
    private val systemInstruction = """
        You are a helpful and intelligent Personal Note Assistant. 
        Your goal is to help users manage, understand, and improve their personal notes.
        When a user provides a note, you can summarize it, explain complex parts, 
        suggest improvements, or answer questions based on the note's content.
        
        Keep your responses:
        1. Concise and relevant.
        2. Professional yet friendly.
        3. Structured (use bullet points if necessary).
        
        If the user asks something unrelated to the note or productivity, 
        politely guide them back to the topic of their notes.
    """.trimIndent()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey,
        systemInstruction = content { text(systemInstruction) },
        safetySettings = listOf(
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE)
        )
    )

    /**
     * Summarizes the note content.
     */
    suspend fun summarizeNote(content: String): String? {
        val prompt = "Please summarize this note accurately: \n\n$content"
        return try {
            val response = generativeModel.generateContent(prompt)
            response.text
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Starts a multi-turn conversation about a specific note.
     * Bonus: Multi-turn conversation and Streaming response.
     */
    fun chatWithNote(history: List<Pair<String, String>>, message: String): Flow<String> {
        val chat = generativeModel.startChat(
            history = history.map { (role, text) ->
                content(role) { text(text) }
            }
        )
        return chat.sendMessageStream(message).map { it.text ?: "" }
    }
}
