package net.caffee.places.architecture

import android.content.Context
import net.caffee.places.repo.Repository
import net.caffee.places.repo.db.LocalRepository
import net.caffee.places.repo.remote.RemoteRepository
import net.caffee.places.repo.sp.SharedPrefRepo
import net.caffee.places.repo.storage.Storage


object Injection {
    fun provideRepository(context: Context): Repository {
        return Repository.getInstance(RemoteRepository, LocalRepository, SharedPrefRepo, Storage)
    }
}
