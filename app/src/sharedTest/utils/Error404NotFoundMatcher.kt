package utils

import com.tolmachevroman.restaurants.datasources.webservice.Resource
import org.mockito.ArgumentMatcher

/**
 * Created by romantolmachev on 22/11/2017.
 */
class Error404NotFoundMatcher<T> : ArgumentMatcher<Resource<T>> {
    override fun matches(argument: Resource<T>): Boolean =
            (argument.status == Resource.Status.ERROR && argument.error!!.code == 404)
}