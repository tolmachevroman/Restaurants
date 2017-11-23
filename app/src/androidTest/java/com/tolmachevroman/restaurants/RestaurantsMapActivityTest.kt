package com.tolmachevroman.restaurants

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.*
import com.tolmachevroman.restaurants.views.RestaurantsMapActivity
import org.hamcrest.CoreMatchers
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

        val markers = device.findObjects(By.descContains("cuisine").pkg(packageName))
        Assert.assertNotNull(markers)

        for(marker in markers) {
            Assert.assertThat(marker.contentDescription, CoreMatchers.containsString("cuisine:1"))
        }
    }

    /**
     * Wait till map and markers get loaded; click on a marker; check that RestaurantDetail fragment
     * is open
     */
    @Test
    fun opensRestaurantDetailFragmentOnMarkerClick() {
        Thread.sleep(2000)

        val markers = device.findObjects(By.descContains("cuisine").pkg(packageName))
        markers[0].click()
        device.wait(Until.hasObject(By.res("$packageName:id/restaurant_content").pkg(packageName)), 2000)

        val image = device.findObject(UiSelector().resourceId("$packageName:id/image"))
        val name = device.findObject(UiSelector().resourceId("$packageName:id/name"))
        val description = device.findObject(UiSelector().resourceId("$packageName:id/description"))

        Assert.assertTrue(image.exists())
        Assert.assertTrue(name.exists())
        Assert.assertTrue(description.exists())
    }
}