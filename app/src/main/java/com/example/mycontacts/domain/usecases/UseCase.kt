package com.example.mycontacts.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class UseCase<out Type, in Params>(
    private val mainDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) where Type : Any {

    abstract suspend fun run(params: Params): Result<Type>

    operator fun invoke(
        params: Params,
        coroutineScope: CoroutineScope,
        onResult: (Result<Type>) -> Unit = {}
    ) {
        coroutineScope.launch(mainDispatcher) {
            val deferred = async(ioDispatcher) {
                run(params)
            }
            onResult(deferred.await())
        }
    }

    class None
}