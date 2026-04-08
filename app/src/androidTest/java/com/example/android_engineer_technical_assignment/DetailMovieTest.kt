package com.example.android_engineer_technical_assignment

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DetailMovieTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testMovieDetailNavigation_integration() {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Inception").fetchSemanticsNodes().isNotEmpty()
        }
        Thread.sleep(2000)

        composeTestRule.onNodeWithText("Inception").performClick()
        Thread.sleep(2000)

        composeTestRule.onNodeWithText("Inception").assertIsDisplayed()
        Thread.sleep(2000)

        composeTestRule.onNodeWithContentDescription("Return").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Search by name...").assertIsDisplayed()
        Thread.sleep(2000)
    }

    @Test
    fun testMovieDetailContent_isCorrect() {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Avatar").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Avatar").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Avatar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Overview 1").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Poster").assertIsDisplayed()
        Thread.sleep(2000)
    }

    @Test
    fun testFavoriteDecline_doesNotAddMovie() {
        composeTestRule.onNodeWithText("Inception").performClick()
        Thread.sleep(2000)

        composeTestRule.onNodeWithContentDescription("Add to Favourites").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Decline").performClick()
        Thread.sleep(2000)

        composeTestRule.onNodeWithContentDescription("Return").performClick()
        Thread.sleep(2000)

        composeTestRule.onNode(hasScrollAction()).performTouchInput { swipeUp() }
        composeTestRule.onNodeWithContentDescription("See my favourites").performClick()
        Thread.sleep(2000)

        composeTestRule.onNodeWithText("Inception").assertDoesNotExist()
        Thread.sleep(2000)
    }
}
