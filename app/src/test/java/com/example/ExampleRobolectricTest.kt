package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Mindful Emotions", appName)
  }

  @Test
  fun `test quotes repository not empty`() {
    val quote = com.example.data.QuotesRepository.getRandomQuote()
    org.junit.Assert.assertNotNull(quote)
    org.junit.Assert.assertTrue(quote.text.isNotEmpty())
  }

  @Test
  fun `test mindfulness prompts repository has 1000 prompts`() {
    val count = com.example.data.MindfulnessPromptsRepository.getCount()
    assertEquals(1000, count)
    val todayPrompt = com.example.data.MindfulnessPromptsRepository.getTodayPrompt()
    org.junit.Assert.assertTrue(todayPrompt.isNotBlank())
  }
}
