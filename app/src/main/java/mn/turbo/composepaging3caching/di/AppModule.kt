package mn.turbo.composepaging3caching.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import mn.turbo.composepaging3caching.data.local.BeerDatabase
import mn.turbo.composepaging3caching.data.local.BeerEntity
import mn.turbo.composepaging3caching.data.remote.BeerApi
import mn.turbo.composepaging3caching.data.remote.BeerRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBeerDatabase(
        @ApplicationContext context: Context
    ): BeerDatabase {
        return Room.databaseBuilder(
            context,
            BeerDatabase::class.java,
            name = "beers.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBeerApi(): BeerApi {
        return Retrofit.Builder()
            .baseUrl(BeerApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideBeerPager(
        beerDatabase: BeerDatabase,
        beerApi: BeerApi
    ): Pager<Int, BeerEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = BeerRemoteMediator(
                beerDatabase,
                beerApi
            ),
            pagingSourceFactory = {
                beerDatabase.beerDao.pagingSource()
            }
        )
    }
}
