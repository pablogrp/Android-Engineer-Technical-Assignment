package com.example.android_engineer_technical_assignment

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MovieUiTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testClearSearch_resetsList() {
        val searchText = "Avatar"

        composeTestRule.onNodeWithText("Search by name...").performTextInput(searchText)
        Thread.sleep(2000)

        composeTestRule.onNodeWithContentDescription("Clear search").performClick()
        Thread.sleep(2000)

        composeTestRule.onNodeWithText("Search by name...").assertIsDisplayed()
        Thread.sleep(2000)
    }

    @Test
    fun testSearchNoResults_showsEmptyMessage() {
        val query = "NonExistentMovie"
        composeTestRule.onNodeWithText("Search by name...").performTextInput(query)
        Thread.sleep(2000)

        composeTestRule.onNodeWithText("No results found for '$query'").assertIsDisplayed()
        Thread.sleep(2000)
    }

    @Test
    fun testNavigationToFavorites_isPossible() {

        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Avatar").fetchSemanticsNodes().isNotEmpty()
        }
        Thread.sleep(2000)
        composeTestRule.onNode(hasScrollAction()).performTouchInput {
            swipeUp()
        }
        Thread.sleep(2000)

        composeTestRule.onNodeWithContentDescription("See my favourites").performClick()
        Thread.sleep(2000)

        composeTestRule.onNodeWithText("Favourite Movies").assertIsDisplayed()
        Thread.sleep(2000)
    }
}
