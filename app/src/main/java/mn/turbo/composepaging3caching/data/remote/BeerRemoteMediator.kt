package mn.turbo.composepaging3caching.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import mn.turbo.composepaging3caching.data.local.BeerDatabase
import mn.turbo.composepaging3caching.data.local.BeerEntity
import mn.turbo.composepaging3caching.data.mapper.toBeerEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val beerDatabase: BeerDatabase,
    private val beerApi: BeerApi
) : RemoteMediator<Int, BeerEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BeerEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND ->
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            val beers = beerApi.getBeers(
                page = loadKey,
                pageCount = state.config.pageSize
            )

            beerDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    beerDatabase.beerDao.clearAll()
                }

                val beerEntities = beers.map { it.toBeerEntity() }
                beerDatabase.beerDao.upsertAll(beerEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = beers.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

}
