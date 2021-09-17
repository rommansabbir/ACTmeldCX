package com.rommansabbir.actmeldcx.usecase

import com.rommansabbir.actmeldcx.base.History
import com.rommansabbir.actmeldcx.base.HistoryCacheObject
import com.rommansabbir.actmeldcx.base.functional.Either
import com.rommansabbir.actmeldcx.base.interactor.UseCase
import com.rommansabbir.actmeldcx.data.LocalHistoryRepository
import javax.inject.Inject

class DeleteNewHistoryUseCase @Inject constructor(private val repo: LocalHistoryRepository) :
    UseCase<HistoryCacheObject, History>() {
    override suspend fun run(params: History): Either<Exception, HistoryCacheObject> =
        repo.deleteHistory(params)

    companion object {
        fun executor(
            useCase: DeleteNewHistoryUseCase,
            params: History,
            onSuccess: (HistoryCacheObject) -> Unit,
            onError: (Exception) -> Unit
        ) {
            useCase(params) { either ->
                either.either({ onError.invoke(it) },
                    { onSuccess.invoke(it) })
            }
        }
    }
}