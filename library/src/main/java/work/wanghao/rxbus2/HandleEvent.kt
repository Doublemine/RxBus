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
  private var mTarget: Any = target
  private var mMethod: Method = method
  private var mThreadMode: ThreadMode = threadMode
  var mDisposable: Disposable? = null
    private set

  init {
    this.mMethod.isAccessible = true
    bindObserver(method.parameterTypes[0])
  }

  private fun bindObserver(clazz: Class<*>) {
    mDisposable = RxBus.get().asFlowableFilterType(clazz)
        .observeOn(getScheduler(mThreadMode))
        .subscribe { any ->
          handleEvent(any)
        }
  }

  private fun handleEvent(event: Any) {
    if (BuildConfig.DEBUG) Log.d(TAG,
        "invoke handle Event for mTarget= ${mTarget.javaClass.simpleName} the fun=$mMethod")
    mMethod.invoke(mTarget, event)
  }

  override fun equals(other: Any?): Boolean {
    if (other is HandleEvent) {
      return mTarget == other.mTarget && mMethod == other.mMethod
    } else {
      return false
    }
  }

  override fun hashCode(): Int {
    var result = mTarget.hashCode()
    result += mMethod.hashCode()
    return result
  }
}