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
class FavoriteMovieUiTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testFullFavoriteFlow_integration() {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Avatar").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Avatar").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithContentDescription("Add to Favourites")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithContentDescription("Add to Favourites").performClick()

        composeTestRule.onNodeWithText("Confirm").performClick()

        composeTestRule.onNodeWithContentDescription("Return").performClick()

        composeTestRule.onNodeWithContentDescription("See my favourites").performClick()

        composeTestRule.onNodeWithText("Avatar").assertIsDisplayed()
    }
}