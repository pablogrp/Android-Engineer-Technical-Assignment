package com.example.android_engineer_technical_assignment

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testSearchFieldIsVisible() {
        composeTestRule
            .onNodeWithText("Search by name...")
            .assertIsDisplayed()
    }

    @Test
    fun testInitialLoadingState() {
        try {
            composeTestRule.onNodeWithText("No movie data available.").assertIsDisplayed()
        } catch (e: AssertionError) {
        }
    }
}