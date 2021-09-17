package com.rommansabbir.actmeldcx.usecase

import com.rommansabbir.actmeldcx.base.History
import com.rommansabbir.actmeldcx.base.functional.Either
import com.rommansabbir.actmeldcx.base.interactor.UseCase
import com.rommansabbir.actmeldcx.data.LocalHistoryRepository
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(private val repository: LocalHistoryRepository) :
    UseCase<MutableList<History>, UseCase.None>() {
    override suspend fun run(params: None): Either<Exception, MutableList<History>> =
        repository.getHistory()

    companion object {
        fun executor(
            useCase: GetHistoryUseCase,
            onSuccess: (MutableList<History>) -> Unit,
            onError: (Exception) -> Unit
        ) {
            useCase(None()) { either ->
                either.either({ onError.invoke(it) },
                    { onSuccess.invoke(it) })
            }
        }
    }
}