package com.example.mycontacts.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Before

open class BaseTest {
    lateinit var ioDispatcher: CoroutineDispatcher
    lateinit var mainDispatcher: CoroutineDispatcher
    lateinit var scope: CoroutineScope


    @Before
    open fun setup() {
        ioDispatcher = Dispatchers.Unconfined
        mainDispatcher = Dispatchers.Unconfined
        scope = CoroutineScope(Dispatchers.Unconfined)
    }
}