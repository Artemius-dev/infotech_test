package com.example.myapplication.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.myapplication.data.room.models.CitiesModel
import com.example.myapplication.data.room.CitiesRoomDatabase
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CitiesRemoteMediator(
    private val database: CitiesRoomDatabase,
) : RemoteMediator<Int, CitiesModel>() {
    val userDao = database.citiesDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CitiesModel>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()

                    if (lastItem == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    lastItem.id
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = false
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}