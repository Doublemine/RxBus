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
	        compile 'io.reactivex:rxandroid:1.2.1'
            compile 'io.reactivex:rxjava:1.1.6'
	        compile 'com.github.Doublemine:RxBus:0.0.2'
	}
```

#### Steop 3. How To Use:

 - post a event Msg

```java

/**
     *
     * do something
     * 
     * the EventMsg mush extends RxBusEvent
     *
     * */

    RxBus.getDefault().post(new EventMsg("some event message.."));

```

>**The EventMsg must extends RxBusEvent**

 - do event Msg

```java

RxBus.getDefault().doOnMainThread(EventMsg.class, new OnRxBusEvent() {
      @Override public void onEvent(RxBusEvent rxBusEvent) {
        EventMsg eventMsg = (EventMsg) rxBusEvent;
        /**
         * do something
         **/
      }
    });

```

 - release

```java
/**
   * On Activity
   */
  @Override protected void onDestroy() {
    super.onDestroy();
    RxBus.getDefault().release(this);
  }

```

The more detail usage ,please visit the source Code.

