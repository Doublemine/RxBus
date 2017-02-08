### RxBus [![](https://jitpack.io/v/Doublemine/RxBus.svg)](https://jitpack.io/#Doublemine/RxBus)






### Usage:

#### Step 1. Add the JitPack repository to your build file

```gradle
	allprojects {
		repositories {
			maven { url "https://jitpack.io" }
		}
	}
```

#### Step 2. Add the dependency

```gradle
	dependencies {

	 compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
         compile 'io.reactivex.rxjava2:rxjava:2.0.5'
         compile 'com.jakewharton.rxrelay2:rxrelay:2.0.0'
	 compile 'com.github.Doublemine:RxBus:2.1.0'

	}
```

#### Step 3. How To Use:


#### Recommend (Like event bus API)

 - Define events:


 ```java
  public class MessageEvent { /* Additional fields if needed */ }
 ```

 - Prepare subscribers: Declare and annotate your subscribing method.
 
 

**NOTE: The method of subscribed only support single argument.**

 ```java
 @Subscribe(threadMode = ThreadMode.MAIN_THREAD)
 public void onMessageEvent(MessageEvent event) {/* Do something */};
 ```
 
 

Register and unregister your subscriber. For example on Android, activities and fragments should usually register according to their life cycle:



```java
@Override
public void onStart() {
    super.onStart();
    RxBus.Companion.get().register(this);
}

@Override
public void onStop() {
    super.onStop();
    RxBus.Companion.get().unRegister(this);
}
```

 - Post events:

 ```java
 RxBus.Companion.get().post(new MessageEvent());
 ```






#### UN-Recommend (The Old API)

 - post a event Msg

```java

/**
     *
     * do something
     *
     * */

    RxBus.Companion.get().post(new EventMsg("send some message..."));

```


 - receive event Msg

```java
  CompositeDisposable  compositeDisposable = new CompositeDisposable();

    /**
    *
    * some code...
    *
    */
  compositeDisposable.add(RxBus.Companion.get()
          .asFlowableFilterType(EventMsg.class)
          .subscribeOn(AndroidSchedulers.mainThread())
          .subscribe(new Consumer<Object>() {
            @Override public void accept(Object o) throws Exception {
              EventMsg eventMsg = (EventMsg) o;
              /**
                * do something
               **/
            }
          }));
```


#### TODO-List

 -  Support Sticky Event
