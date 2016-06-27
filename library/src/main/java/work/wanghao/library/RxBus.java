package work.wanghao.library;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Create on: 2016-06-03
 * Author: wangh
 * Summary: TODO
 */
public class RxBus {

  private static volatile RxBus mDefaultInstance;

  private final Subject mBus;

  public RxBus() {
    mBus = new SerializedSubject<>(PublishSubject.create());
  }

  public static RxBus getDefault() {
    RxBus rxBus = mDefaultInstance;
    if (mDefaultInstance == null) {
      synchronized (RxBus.class) {
        rxBus = mDefaultInstance;
        if (mDefaultInstance == null) {
          rxBus = new RxBus();
          mDefaultInstance = rxBus;
        }
      }
    }
    return rxBus;
  }

  public void post(Object event) {
    mBus.onNext(event);
  }

  public <T> Observable<T> toObserverable(Class<T> eventType) {
    return mBus.ofType(eventType);
  }
}
