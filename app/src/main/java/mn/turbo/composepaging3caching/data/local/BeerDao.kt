package mn.turbo.composepaging3caching.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface BeerDao {
    @Upsert
    suspend fun upsertAll(beers: List<BeerEntity>)

    @Query("select * from beers")
    fun pagingSource(): PagingSource<Int, BeerEntity>

    @Query("delete from beers")
    suspend fun clearAll()
}
