package com.rommansabbir.actmeldcx.usecase

import com.rommansabbir.actmeldcx.base.History
import com.rommansabbir.actmeldcx.base.functional.Either
import com.rommansabbir.actmeldcx.base.interactor.UseCase
import com.rommansabbir.actmeldcx.data.LocalHistoryRepository
import javax.inject.Inject

class SearchHistoryUseCase @Inject constructor(private val repository: LocalHistoryRepository) :
    UseCase<MutableList<History>, String>() {
    override suspend fun run(params: String): Either<Exception, MutableList<History>> =
        repository.searchHistory(params)

    companion object {
        fun executor(
            useCase: SearchHistoryUseCase,
            query: String,
            onSuccess: (MutableList<History>) -> Unit,
            onError: (Exception) -> Unit
        ) {
            useCase(query) { either ->
                either.either({ onError.invoke(it) },
                    { onSuccess.invoke(it) })
            }
        }
    }
}