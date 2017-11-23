package com.tolmachevroman.restaurants

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.By
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiObject
import android.support.test.uiautomator.UiSelector
import com.tolmachevroman.restaurants.views.RestaurantsMapActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by romantolmachev on 23/11/2017.
 */
@RunWith(AndroidJUnit4::class)
class RestaurantsMapActivityTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(RestaurantsMapActivity::class.java)

    companion object {
        val packageName = BuildConfig.APPLICATION_ID
    }
    private lateinit var device: UiDevice

    private lateinit var spinner: UiObject
    private lateinit var map: UiObject

    @Before
    fun setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        spinner = device.findObject(UiSelector().resourceId("$packageName:id/spinner"))
        map = device.findObject(UiSelector().resourceId("$packageName:id/map"))
    }

    /**
     * Check that all UI items exist within the screen
     */
    @Test
    fun uiElementsExist() {
        val title = device.findObject(UiSelector().resourceId("$packageName:id/title"))
        Assert.assertTrue(title.exists())
        Assert.assertTrue(map.exists())
        Assert.assertTrue(spinner.exists())
    }

    /**
     * Wait till map gets loaded; check that it has markers
     */
    @Test
    fun markersLoadedOntoMap() {
        Thread.sleep(2000)
        Assert.assertTrue(map.childCount > 0)
    }

    /**
     * Wait till map and markers get loaded; click on Spinner; select item with cuisine = 1; check
     * that items on map all have cuisine = 1
     */
    @Test
    fun filterMarkersByClickingSpinner() {
        Thread.sleep(2000)

        spinner.click()
        val cuisines = device.findObjects(By.res("android:id/text1").pkg(packageName))
        cuisines[1].click()

        Thread.sleep(1000)

        val markers = device.findObjects(By.descContains("marker").pkg(packageName))
        for(marker in markers) {
            Assert.assertEquals(marker.contentDescription, "cuisine:1")
        }
    }
}