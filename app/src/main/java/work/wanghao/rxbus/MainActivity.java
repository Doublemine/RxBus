package work.wanghao.rxbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import io.reactivex.disposables.CompositeDisposable;
import work.wanghao.rxbus2.RxBus;
import work.wanghao.rxbus2.Subscribe;
import work.wanghao.rxbus2.ThreadMode;

public class MainActivity extends AppCompatActivity {

  TextView mTextView;
  private CompositeDisposable mCompositeDisposable;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mCompositeDisposable = new CompositeDisposable();
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

  @Override protected void onStop() {
    super.onStop();
    mCompositeDisposable.dispose();
    RxBus.Companion.get().unRegister(this);
  }

  @Subscribe(threadMode = ThreadMode.MAIN_THREAD) public void test(EventMsg eventMsg) {
    mTextView.setText(eventMsg.mString);
  }
}
