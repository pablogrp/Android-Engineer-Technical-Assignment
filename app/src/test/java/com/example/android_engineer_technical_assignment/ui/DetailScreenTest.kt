package com.example.android_engineer_technical_assignment.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.android_engineer_technical_assignment.ui.screens.DetailScreenContent
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun detailScreen_displaysTitleAndOverview() {
        composeTestRule.setContent {
            Android_Engineer_Technical_AssignmentTheme {
                DetailScreenContent(
                    title = "Example1",
                    posterPath = "",
                    overview = "...",
                    isFavorite = false,
                    isLoading = false,
                    onBack = {},
                    onFavoriteClick = {}
                )
            }
        }


        composeTestRule.onNodeWithText("Example1").assertIsDisplayed()
        composeTestRule.onNodeWithText("...").assertIsDisplayed()
    }

    @Test
    fun detailScreen_showsFavoriteIconAsSelected() {
        composeTestRule.setContent {
            Android_Engineer_Technical_AssignmentTheme {
                DetailScreenContent(
                    title = "Movie Title",
                    posterPath = "",
                    overview = "Overview",
                    isFavorite = true,
                    isLoading = false,
                    onBack = {},
                    onFavoriteClick = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Add to Favourites").assertIsDisplayed()
    }
}
