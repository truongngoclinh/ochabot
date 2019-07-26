package vn.ochabot.seaconnect.core

/**
 * @author linhtruong
 */
interface ItemInteractor<T> {
    fun onItemClick(data: T)
}