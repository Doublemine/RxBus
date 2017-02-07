### RxBus [![](https://jitpack.io/v/Doublemine/RxBus.svg)](https://jitpack.io/#Doublemine/RxBus)


 A lightweight Event Bus, Base On  Rxjava 2.x.




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
	        compile 'com.github.Doublemine:RxBus:2.0.1'

	}
```

#### Steop 3. How To Use:

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

