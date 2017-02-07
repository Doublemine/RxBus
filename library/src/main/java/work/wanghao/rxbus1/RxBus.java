package work.wanghao.rxbus1;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
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
  private Map<String, Object> mTags;

  private final Map<String, List<Subscription>> mClassListMap;

  private RxBus() {
    mBus = new SerializedSubject<>(PublishSubject.create());
    mClassListMap = new HashMap<>();
  }

  public static RxBus getDefault() {

    if (mDefaultInstance == null) {
      synchronized (RxBus.class) {
        if (mDefaultInstance == null) {
          mDefaultInstance = new RxBus();
        }
      }
    }
    return mDefaultInstance;
  }

  public void doOnMainThread(final Class<? extends RxBusEvent> eventTypeClass,
      final OnRxBusEvent rxBusResponse) {
    doOnScheduler(AndroidSchedulers.mainThread(), eventTypeClass, rxBusResponse);
  }

  public void doOnIOThread(final Class<? extends RxBusEvent> eventTypeClass,
      final OnRxBusEvent rxBusResponse) {
    doOnScheduler(Schedulers.io(), eventTypeClass, rxBusResponse);
  }

  public void doOnNewThread(final Class<? extends RxBusEvent> eventTypeClass,
      final OnRxBusEvent rxBusResponse) {
    doOnScheduler(Schedulers.newThread(), eventTypeClass, rxBusResponse);
  }

  public void doOnComputation(final Class<? extends RxBusEvent> eventTypeClass,
      final OnRxBusEvent rxBusResponse) {
    doOnScheduler(Schedulers.computation(), eventTypeClass, rxBusResponse);
  }

  @SuppressWarnings("unchecked") public void doOnScheduler(final Scheduler schedulers,
      final Class<? extends RxBusEvent> eventTypeClass, final OnRxBusEvent rxBusResponse) {
    String className = getCallerName();
    List<Subscription> list = null;
    if (mClassListMap.containsKey(className)) {
      list = mClassListMap.get(className);
    } else {
      list = new ArrayList<>();
      mClassListMap.put(className, list);
    }
    Subscription subscription =
        mBus.ofType(eventTypeClass).observeOn(schedulers).subscribe(new Action1<RxBusEvent>() {
          @Override public void call(RxBusEvent rxEvent) {
            rxBusResponse.onEvent(rxEvent);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            Log.e("RxBus", "出现错误，执行续订:" + throwable.getMessage());
            doOnScheduler(schedulers, eventTypeClass, rxBusResponse);
          }
        }, new Action0() {
          @Override public void call() {
            doOnScheduler(schedulers, eventTypeClass, rxBusResponse);
          }
        });
    list.add(subscription);
  }

  public void doOnMainThread(final String tag, final OnRxBusTagEvent rxBusResponse) {
    doOnScheduler(AndroidSchedulers.mainThread(), tag, rxBusResponse);
  }

  public void doOnIOThread(final String tag, final OnRxBusTagEvent rxBusResponse) {
    doOnScheduler(Schedulers.io(), tag, rxBusResponse);
  }

  public void doOnNewThread(final String tag, final OnRxBusTagEvent rxBusResponse) {
    doOnScheduler(Schedulers.newThread(), tag, rxBusResponse);
  }

  public void doOnComputation(final String tag, final OnRxBusTagEvent rxBusResponse) {
    doOnScheduler(Schedulers.computation(), tag, rxBusResponse);
  }

  @SuppressWarnings("unchecked")
  public void doOnScheduler(final Scheduler schedulers, final String tag,
      final OnRxBusTagEvent onRxBusTagEvent) {
    String className = getCallerName();
    List<Subscription> list = null;
    if (mClassListMap.containsKey(className)) {
      list = mClassListMap.get(className);
    } else {
      list = new ArrayList<>();
      mClassListMap.put(className, list);
    }
    Subscription subscription = mBus.observeOn(schedulers).subscribe(new Action1() {
      @Override public void call(Object rxEvent) {
        if (mTags != null && mTags.containsKey(tag)) onRxBusTagEvent.onEvent(rxEvent);
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        if (BuildConfig.DEBUG) throwable.printStackTrace();
        Log.e("RxBus", "出现错误，执行续订:" + throwable.toString());
        doOnScheduler(schedulers, tag, onRxBusTagEvent);
      }
    }, new Action0() {
      @Override public void call() {
        doOnScheduler(schedulers, tag, onRxBusTagEvent);
      }
    });
    list.add(subscription);
  }

  @SuppressWarnings("unchecked") public void post(final String tag, final Object eventMsg) {
    mBus.onNext(eventMsg);
    if (mTags == null) mTags = new HashMap<>();
    if (!mTags.containsKey(tag)) {
      mTags.put(tag, eventMsg);
    }
  }

  @SuppressWarnings("unchecked") public void post(RxBusEvent eventMsg) {
    mBus.onNext(eventMsg);
  }

  public void release(Object target) {
    if (mClassListMap.containsKey(target.getClass().getName())) {
      List<Subscription> list = mClassListMap.get(target.getClass().getName());
      if (list != null && list.size() > 0) {
        for (Subscription subscription : list) {
          if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
          }
        }
      }
    }
    if (mClassListMap.containsKey("NoneOfClass")) {
      List<Subscription> list = mClassListMap.get("NoneOfClass");
      if (list != null && list.size() > 0) {
        for (Subscription subscription : list) {
          if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
          }
        }
      }
    }
  }

  private String getCallerName() {
    StackTraceElement stackTraceElement[] = new Throwable().getStackTrace();
    for (int i = 0; i < stackTraceElement.length; i++) {
      if (!this.getClass().getName().equals(stackTraceElement[i].getClassName())) {
        return stackTraceElement[i].getClassName();
      }
    }
    return "NoneOfClass";
  }

  public boolean hasObservers() {
    return mBus.hasObservers();
  }

  /**
   * <b>不推荐此方法</b>
   *
   * @param eventType 事件类型
   */
  @SuppressWarnings("unchecked") @Deprecated public <T> Observable<T> toObservable(
      Class<T> eventType) {
    return mBus.ofType(eventType);
  }
}
