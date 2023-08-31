package com.saxipapsi.saxi_movie.di

import android.app.Application
import com.saxipapsi.saxi_movie.data.remote.networkModule
import com.saxipapsi.saxi_movie.data.repository.TMDBRepositoryImpl
import com.saxipapsi.saxi_movie.domain.repository.TMDBRepository
import com.saxipapsi.saxi_movie.domain.use_case.*
import com.saxipapsi.saxi_movie.presentation.get_content_credits.components.ContentCreditsViewModel
import com.saxipapsi.saxi_movie.presentation.get_content_details.components.GetContentDetailsViewModel
import com.saxipapsi.saxi_movie.presentation.get_content_list.components.GetContentListViewModel
import com.saxipapsi.saxi_movie.presentation.get_content_video.components.ContentVideoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

object KoinModules {

    fun init(app: Application) {
        startKoin() {
            androidLogger(Level.ERROR)
            androidContext(app)
            modules(
                networkModule,
                repositoryModule,
                useCasesModule,
                viewModelModule
            )
        }
    }
}

val repositoryModule = module {
    single { TMDBRepositoryImpl(get()) as TMDBRepository }
}

val useCasesModule = module {
    single { GetUpComingListUseCase(get()) }
    single { GetTrendingListUseCase(get()) }
    single { GetPopularListUseCase(get()) }
    single { GetTopRatedListUseCase(get()) }
    single { GetContentDetailsUseCase(get()) }
    single { GetContentVideoUsaCase(get()) }
    single { GetCreditsUseCase(get()) }
}

val viewModelModule = module {
    viewModel { GetContentListViewModel(get(), get(), get(), get()) }
    viewModel { GetContentDetailsViewModel(get()) }
    viewModel { ContentVideoViewModel(get()) }
    viewModel { ContentCreditsViewModel(get()) }
}
