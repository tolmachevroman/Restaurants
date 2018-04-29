package com.tolmachevroman.restaurants.test

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiObject
import android.support.test.uiautomator.UiSelector
import com.tolmachevroman.restaurants.BuildConfig
import com.tolmachevroman.restaurants.views.RestaurantsMapActivity
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import junit.framework.Assert.assertTrue
import org.junit.Rule

class AllRestaurantsStepsTest {

    companion object {
        const val packageName = BuildConfig.APPLICATION_ID
    }

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(RestaurantsMapActivity::class.java)

    lateinit var activity: RestaurantsMapActivity

    private lateinit var device: UiDevice
    private lateinit var spinner: UiObject
    private lateinit var map: UiObject

    @Before
    fun setup() {
        activityTestRule.launchActivity(Intent())
        activity = activityTestRule.activity
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        spinner = device.findObject(UiSelector().resourceId("$packageName:id/spinner"))
        map = device.findObject(UiSelector().resourceId("$packageName:id/map"))
    }

    @After
    fun tearDown() {
        activityTestRule.finishActivity()
    }

    @Given("^The map is loaded")
    fun map_is_loaded() {
        Thread.sleep(2000)
        assertTrue(map.exists())
        assertTrue(map.childCount > 0)
    }

    @Then("^All restaurants are shown on the map")
    fun all_restaurants_are_shown_on_the_map() {
        assertTrue(map.childCount > 0)
    }
}