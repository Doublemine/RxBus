package work.wanghao.rxbus2

import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.ConcurrentHashMap

/**
 * @author doublemine
 *         Created  on 2017/02/07 15:17.
 *         Summary:
 */

class RxBus private constructor() {

  companion object {
    fun get(): RxBus {
      return Inner.single
    }
  }

  private object Inner {
    val single = RxBus()
  }

  private val mBus: Relay<Any> = PublishRelay.create<Any>().toSerialized()
  private val mSubSConcurrentHashMap = ConcurrentHashMap<Any, SubscribeContainer>()

  fun post(event: Any) {
    mBus.accept(event)
  }

  fun asFlowable(): Flowable<Any> {
    return mBus.toFlowable(BackpressureStrategy.LATEST)
  }

  fun hasObservers(): Boolean {
    return mBus.hasObservers()
  }

  fun asFlowableFilterType(obj: Class<*>): Flowable<*> {
    return mBus.toFlowable(BackpressureStrategy.LATEST).ofType(obj)
  }

  fun register(any: Any) {
    val compositeDisposable = CompositeDisposable()
    mSubSConcurrentHashMap.put(any, findAnnotatedMethods(any, compositeDisposable))
  }

  fun unRegister(any: Any) {
    val targetContainer = mSubSConcurrentHashMap[any]
    if (targetContainer != null) {
      targetContainer.getCompositeDisposable().dispose()
      mSubSConcurrentHashMap.remove(any)
      if (BuildConfig.DEBUG) Log.d(RxBus::class.java.simpleName,
          "Unregister the target=${any.javaClass.simpleName}")
    }
  }

}