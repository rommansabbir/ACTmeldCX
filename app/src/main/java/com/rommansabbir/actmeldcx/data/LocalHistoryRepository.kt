package com.rommansabbir.actmeldcx.data

import com.rommansabbir.actmeldcx.base.History
import com.rommansabbir.actmeldcx.base.HistoryCacheObject
import com.rommansabbir.actmeldcx.base.functional.Either
import com.rommansabbir.storex.StoreX
import javax.inject.Inject

interface LocalHistoryRepository {
    suspend fun newHistory(history: History): Either<Exception, HistoryCacheObject>
    suspend fun getHistory(): Either<Exception, MutableList<History>>
    suspend fun deleteHistory(history: History): Either<Exception, HistoryCacheObject>
    suspend fun searchHistory(query: String): Either<Exception, MutableList<History>>
}

class LocalHistoryRepositoryImpl @Inject constructor(private val storeX: StoreX) :
    LocalHistoryRepository {
    override suspend fun newHistory(history: History): Either<Exception, HistoryCacheObject> {
        return try {
            var oldObject: HistoryCacheObject
            try {
                oldObject = storeX.get(HISTORY_KEY, HistoryCacheObject::class.java)
                oldObject.list.add(history)
            } catch (e: Exception) {
                oldObject = HistoryCacheObject(mutableListOf(history))
            }
            return try {
                storeX.put(HISTORY_KEY, oldObject)
                Either.Right(oldObject)
            } catch (e: Exception) {
                e.printStackTrace()
                Either.Left(e)
            }
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    override suspend fun getHistory(): Either<Exception, MutableList<History>> {
        return try {
            val list = mutableListOf<History>()
            return try {
                list.clear()
                list.addAll(storeX.get(HISTORY_KEY, HistoryCacheObject::class.java).list)
                Either.Right(list)
            } catch (e: Exception) {
                e.printStackTrace()
                list.clear()
                Either.Right(list)
            }
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    override suspend fun deleteHistory(history: History): Either<Exception, HistoryCacheObject> {
        return try {
            var oldObject: HistoryCacheObject
            try {
                oldObject = storeX.get(HISTORY_KEY, HistoryCacheObject::class.java)
                oldObject.list.remove(history)
            } catch (e: Exception) {
                e.printStackTrace()
                oldObject = HistoryCacheObject(mutableListOf())
            }
            return try {
                storeX.put(HISTORY_KEY, oldObject)
                Either.Right(oldObject)
            } catch (e: Exception) {
                e.printStackTrace()
                Either.Left(e)
            }
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    override suspend fun searchHistory(query: String): Either<Exception, MutableList<History>> {
        return try {
            val list = mutableListOf<History>()
            return try {
                list.clear()
                storeX.get(HISTORY_KEY, HistoryCacheObject::class.java).list.forEach {
                    if (it.url.contains(query, true)) {
                        list.add(it)
                    }
                }
                Either.Right(list)
            } catch (e: Exception) {
                e.printStackTrace()
                list.clear()
                Either.Right(list)
            }
        } catch (e: Exception) {
            Either.Left(e)
        }
    }

    companion object {
        private const val HISTORY_KEY = "_history"
    }

}