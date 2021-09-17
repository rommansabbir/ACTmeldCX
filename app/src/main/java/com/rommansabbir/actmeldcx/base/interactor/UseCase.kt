package com.rommansabbir.actmeldcx.base.interactor

import com.rommansabbir.actmeldcx.base.functional.Either
import kotlinx.coroutines.*
import java.lang.Exception

abstract class UseCase<out Type, in Params> where Type : Any {

    private val mainJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + mainJob)

    abstract suspend fun run(params: Params): Either<Exception, Type>

    operator fun invoke(params: Params, onResult: (Either<Exception, Type>) -> Unit = {}) =
        uiScope.launch { onResult(withContext(Dispatchers.IO) { run(params) }) }

    class None
}