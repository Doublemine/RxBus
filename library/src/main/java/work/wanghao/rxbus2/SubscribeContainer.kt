package work.wanghao.rxbus2

import io.reactivex.disposables.CompositeDisposable
import java.util.*

/**
 * @author doublemine
 *         Created  on 2017/02/08 09:53.
 *         Summary:
 */

class SubscribeContainer constructor(target: Any, compositeDisposable: CompositeDisposable,
    methodContainer: HashSet<HandleEvent>) {


  private var mCompositeDisposable: CompositeDisposable
  private var mMethodContainer: HashSet<HandleEvent>
  private var mTarget: Any

  init {
    this.mCompositeDisposable = compositeDisposable
    this.mMethodContainer = methodContainer
    this.mTarget = target
  }


  fun getCompositeDisposable(): CompositeDisposable {
    return mCompositeDisposable
  }

}