package levilin.moviedatabase.data.remote

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RemoteRepository @Inject constructor(val remoteDataSource: RemoteDataSource)