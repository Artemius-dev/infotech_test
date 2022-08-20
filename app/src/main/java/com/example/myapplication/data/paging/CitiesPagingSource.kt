package com.example.myapplication.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.STARTING_KEY
import com.example.myapplication.data.room.models.CitiesModel
import com.example.myapplication.ui.CitiesRepository
import java.lang.Integer.max

class CitiesPagingSource(private val repository: CitiesRepository) : PagingSource<Int, CitiesModel>() {
    override fun getRefreshKey(state: PagingState<Int, CitiesModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val article = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(key = article.id - (state.config.pageSize / 2))
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CitiesModel> {
        // Start paging with the STARTING_KEY if this is the first load
        val start = params.key ?: STARTING_KEY
        // Load as many items as hinted by params.loadSize
        val range = start.until(start + params.loadSize)

        return LoadResult.Page(
            data = range.map { number ->
                repository.getCitie(number)
            },
            // Make sure we don't try to load items behind the STARTING_KEY
            prevKey = when (start) {
                STARTING_KEY -> null
                else -> ensureValidKey(key = range.first - params.loadSize)
            },
            nextKey = range.last + 1
        )
    }

    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}