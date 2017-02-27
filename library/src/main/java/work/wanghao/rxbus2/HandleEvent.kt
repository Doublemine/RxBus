package work.wanghao.rxbus2

import android.util.Log
import io.reactivex.disposables.Disposable
import java.lang.reflect.Method

/**
 * @author doublemine
 *         Created  on 2017/02/08 09:27.
 *         Summary:
 */
class HandleEvent constructor(target: Any, method: Method, threadMode: ThreadMode) {
  private val TAG = HandleEvent::class.java.simpleName
  private var target: Any
  private var method: Method
  private var threadMode: ThreadMode
  private var mDisposable: Disposable? = null

  init {
    this.target = target
    this.method = method
    this.threadMode = threadMode
    this.method.isAccessible = true
    bindObserver(method.parameterTypes[0])
  }

  private fun bindObserver(clazz: Class<*>) {
    mDisposable = RxBus.get().asFlowableFilterType(clazz)
        .observeOn(getScheduler(threadMode))
        .subscribe { any ->
          handleEvent(any)
        }
  }

  private fun handleEvent(event: Any) {
    if (BuildConfig.DEBUG) Log.d(TAG,
        "invoke handle Event for target= ${target.javaClass.simpleName} the fun=$method")
    method.invoke(target, event)
  }

  fun getDisposable(): Disposable? {
    return mDisposable
  }

  override fun equals(other: Any?): Boolean {
    if (other == null) return false
    if (javaClass != other.javaClass) return false
    return method == (other as HandleEvent).method && target == other.target
  }

  override fun hashCode(): Int {
    var result = target.hashCode()
    result = 31 * result + method.hashCode()
    result = 31 * result + threadMode.hashCode()
    return result
  }
}