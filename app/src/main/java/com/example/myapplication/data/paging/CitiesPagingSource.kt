package com.example.myapplication.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.STARTING_KEY
import com.example.myapplication.data.room.models.CitiesModel
import com.example.myapplication.ui.CitiesRepository
import java.lang.Integer.max

class CitiesPagingSource(
    private val repository: CitiesRepository,
    private val citieRequestState: CitieRequestState,
    private val citieNameCountry: String
) : PagingSource<Int, CitiesModel>() {
    override fun getRefreshKey(state: PagingState<Int, CitiesModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(20) ?: page.nextKey?.minus(20)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CitiesModel> {
        // Start paging with the STARTING_KEY if this is the first load
        val start = params.key ?: STARTING_KEY
        val pageSize = params.loadSize
        // Load as many items as hinted by params.loadSize
        val range = start.until(start + pageSize)

        return when (citieRequestState) {
            CitieRequestState.SEARCH -> {
                val getCities =
                    repository.getCitieWithName(citieNameCountry, range.first, pageSize)

                val nextKey = if (getCities.size < pageSize) null else start + pageSize

                LoadResult.Page(
                    data = getCities,
                    // Make sure we don't try to load items behind the STARTING_KEY
                    nextKey = nextKey,
                    prevKey = null,

                    )
            }
            CitieRequestState.FILTER -> {
                val getCities =
                    repository.filterCitiesByCountry(citieNameCountry, range.first, pageSize)

                val nextKey = if (getCities.size < pageSize) null else start + pageSize

                LoadResult.Page(
                    data = getCities,
                    // Make sure we don't try to load items behind the STARTING_KEY
                    nextKey = nextKey,
                    prevKey = null,

                    )
            }
            else -> {
                val getCities = repository.getCitiesInRange(range.first, pageSize)

                val nextKey = if (getCities.size < pageSize) null else start + pageSize

                LoadResult.Page(
                    data = getCities,
                    // Make sure we don't try to load items behind the STARTING_KEY
                    nextKey = nextKey,
                    prevKey = null
                )
            }
        }
    }

    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}