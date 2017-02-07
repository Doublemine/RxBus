package work.wanghao.rxbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import work.wanghao.rxbus1.OnRxBusEvent;
import work.wanghao.rxbus1.RxBus;
import work.wanghao.rxbus1.RxBusEvent;

public class SecondActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second);
  }

  private void onEventMsg() {

    RxBus.getDefault().doOnMainThread(EventMsg.class, new OnRxBusEvent() {
      @Override public void onEvent(RxBusEvent rxBusEvent) {
        EventMsg eventMsg = (EventMsg) rxBusEvent;
        /**
         * do something
         **/
      }
    });
  }

  private void sendEventMsg() {

    /**
     *
     * do something
     *
     * the EventMsg mush extends RxBusEvent
     *
     * */

    RxBus.getDefault().post(new EventMsg("some event message.."));
  }

  /**
   * On Activity
   */
  @Override protected void onDestroy() {
    super.onDestroy();
    RxBus.getDefault().release(this);
  }
}
