package work.wanghao.rxbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import work.wanghao.library.OnRxBusEvent;
import work.wanghao.library.RxBus;
import work.wanghao.library.RxBusEvent;

public class MainActivity extends AppCompatActivity {

  TextView mTextView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mTextView = (TextView) findViewById(R.id.text);
    Button button = (Button) findViewById(R.id.btn);
    assert button != null;

    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RxBus.getDefault().post(new EventMsg("测试方法"));
      }
    });
    init();
  }

  private void init() {
    RxBus.getDefault().doOnMainThread(Test.class, new OnRxBusEvent() {
      @Override public void onEvent(RxBusEvent rxBusEvent) {
        EventMsg eventMsg = (EventMsg) rxBusEvent;
        mTextView.setText(eventMsg.mString);
      }
    });
  }

  @Override protected void onStop() {
    super.onStop();
    RxBus.getDefault().release(this);
  }
}
