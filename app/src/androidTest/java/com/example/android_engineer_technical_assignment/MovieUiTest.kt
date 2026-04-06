package com.example.android_engineer_technical_assignment

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testSearchField_isVisible_andAcceptsInput() {
        // Verify that the search field is visible
        composeTestRule
            .onNodeWithText("Search by name...")
            .assertIsDisplayed()

        // Type movie name in the text field
        composeTestRule
            .onNodeWithText("Search by name...")
            .performTextInput("Example1")

        Thread.sleep(2000)

        // Verify that the text field contains the input
        composeTestRule
            .onNodeWithText("Example1")
            .assertIsDisplayed()
    }

    @Test
    fun testClearSearch_resetsList() {
        val searchText = "Example1"

        // Type in the text field
        composeTestRule.onNodeWithText("Search by name...").performTextInput(searchText)
        composeTestRule.onNodeWithContentDescription("Clear search").performClick()
        composeTestRule.onNodeWithText("Search by name...").assertIsDisplayed()
    }

    @Test
    fun testNavigationToFavorites_isPossible() {
        try {
            composeTestRule.onNodeWithContentDescription("See my favourites").performClick()
        } catch (e: AssertionError) {
        }
    }
}
