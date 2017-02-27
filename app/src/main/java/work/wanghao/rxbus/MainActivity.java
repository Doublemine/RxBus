package work.wanghao.rxbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import work.wanghao.rxbus2.RxBus;
import work.wanghao.rxbus2.Subscribe;
import work.wanghao.rxbus2.ThreadMode;

public class MainActivity extends AppCompatActivity {

  TextView mTextView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mTextView = (TextView) findViewById(R.id.text);
    Button button = (Button) findViewById(R.id.btn);
    assert button != null;
    RxBus.Companion.get().register(this);
    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RxBus.Companion.get().post(new EventMsg("测试方法"));
      }
    });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    RxBus.Companion.get().unRegister(this);
  }

  @Subscribe(threadMode = ThreadMode.IO) public void testIO(EventMsg eventMsg) {
    Log.d("TAG", eventMsg.mString + "IO");
  }

  @Subscribe(threadMode = ThreadMode.NEW) public void testNew(EventMsg eventMsg) {
    Log.d("TAG", eventMsg.mString + "NEW");
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void testUI(EventMsg eventMsg) {
    mTextView.setText(eventMsg.mString);
  }

  @Subscribe(threadMode = ThreadMode.COMPUTE) public void testCompute(EventMsg eventMsg) {
    Log.d("TAG", eventMsg.mString + "COMPUTE");
  }
}
