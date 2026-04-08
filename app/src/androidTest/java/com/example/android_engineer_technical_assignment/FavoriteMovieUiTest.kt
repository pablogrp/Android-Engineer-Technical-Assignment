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
        Thread.sleep(2000) 

        composeTestRule.onNodeWithText("Avatar").performClick()
        Thread.sleep(2000) 

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithContentDescription("Add to Favourites")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithContentDescription("Add to Favourites").performClick()
        Thread.sleep(2000) 

        composeTestRule.onNodeWithText("Confirm").performClick()
        Thread.sleep(2000) 

        composeTestRule.onNodeWithContentDescription("Return").performClick()
        Thread.sleep(2000) 

        composeTestRule.onNode(hasScrollAction()).performTouchInput {
            swipeUp()
        }
        Thread.sleep(2000) 

        composeTestRule.onNodeWithContentDescription("See my favourites").performClick()
        Thread.sleep(2000) 

        composeTestRule.onNodeWithText("Avatar").assertIsDisplayed()
        Thread.sleep(2000)
    }

    @Test
    fun testFavoriteEmptyState_isDisplayed() {

        composeTestRule.onNode(hasScrollAction()).performTouchInput { swipeUp() }
        composeTestRule.onNodeWithContentDescription("See my favourites").performClick()
        Thread.sleep(2000)

        composeTestRule.onNodeWithText("No movie has been added to the list.").assertIsDisplayed()
        Thread.sleep(2000)
    }

    @Test
    fun testRemoveFavorite_fromFavoriteScreen() {

        composeTestRule.onNodeWithText("Avatar").performClick()
        composeTestRule.onNodeWithContentDescription("Add to Favourites").performClick()
        composeTestRule.onNodeWithText("Confirm").performClick()
        composeTestRule.onNodeWithContentDescription("Return").performClick()
        Thread.sleep(2000)

        composeTestRule.onNode(hasScrollAction()).performTouchInput { swipeUp() }
        composeTestRule.onNodeWithContentDescription("See my favourites").performClick()
        Thread.sleep(2000)

        composeTestRule.onNodeWithText("Avatar").performClick()
        Thread.sleep(2000)


        composeTestRule.onNodeWithContentDescription("Add to Favourites").performClick()
        composeTestRule.onNodeWithText("Confirm").performClick()
        Thread.sleep(2000)

        composeTestRule.onNodeWithContentDescription("Return").performClick()
        Thread.sleep(2000)

        composeTestRule.onNodeWithText("Avatar").assertDoesNotExist()
        composeTestRule.onNodeWithText("No movie has been added to the list.").assertIsDisplayed()
        Thread.sleep(2000)
    }

    @Test
    fun testNavigateBackFromFavorites_returnsToHome() {
        composeTestRule.onNode(hasScrollAction()).performTouchInput { swipeUp() }
        composeTestRule.onNodeWithContentDescription("See my favourites").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithContentDescription("Return").performClick()
        Thread.sleep(2000)

        composeTestRule.onNodeWithText("Search by name...").assertIsDisplayed()
        Thread.sleep(2000)
    }
}
