package work.wanghao.rxbus2

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author doublemine
 *         Created  on 2017/02/07 23:13.
 *         Summary:
 */
enum class ThreadMode {
  MAIN_THREAD, IO_THREAD, NEW_THREAD, COMPUTE_THREAD;
}

fun getScheduler(threadMode: ThreadMode): Scheduler {
  when (threadMode) {
    ThreadMode.MAIN_THREAD -> return AndroidSchedulers.mainThread()
    ThreadMode.IO_THREAD -> return Schedulers.io()
    ThreadMode.NEW_THREAD -> return Schedulers.newThread()
    ThreadMode.COMPUTE_THREAD -> return Schedulers.computation()
  }
}

