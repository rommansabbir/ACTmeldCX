package com.rommansabbir.actmeldcx.data

import com.rommansabbir.actmeldcx.base.History
import com.rommansabbir.actmeldcx.base.HistoryCacheObject
import com.rommansabbir.actmeldcx.base.functional.Either
import com.rommansabbir.storex.StoreX
import javax.inject.Inject

interface LocalHistoryRepository {
    /**
     * Save a new history to the local cache.
     *
     * Check for existing history [HistoryCacheObject] in the cache.
     *
     * if available add the new history to the existing history list & cache the object
     *
     * else create a new [HistoryCacheObject] and cache the object.
     *
     * @param history the object to be saved, [History]
     *
     * @return [Either<Exception, HistoryCacheObject>]
     */
    suspend fun newHistory(history: History): Either<Exception, HistoryCacheObject>

    /**
     * Get the cached history list from the cache.
     *
     * Look for the cached [HistoryCacheObject] from the cache & return the list.
     *
     * else return an empty list of [History]
     *
     * @return [Either<Exception, MutableList<History>>]
     */
    suspend fun getHistory(): Either<Exception, MutableList<History>>

    /**
     * Delete an existing history from the cache.
     *
     * Get the [HistoryCacheObject] from the cache & remove the marked [history] from the list
     * and cache the [HistoryCacheObject].
     *
     * @return [Either<Exception, HistoryCacheObject>]
     */
    suspend fun deleteHistory(history: History): Either<Exception, HistoryCacheObject>

    /**
     * Search history from the cache.
     *
     * Get the [HistoryCacheObject] from the cache & filter out the matched [History] object from the list
     * and then return the list.
     *
     * else return an empty list of [History]
     *
     * @return [Either<Exception, HistoryCacheObject>]
     */
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
                Either.Right(list.asReversed())
            } catch (e: Exception) {
                e.printStackTrace()
                list.clear()
                Either.Right(list.asReversed())
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