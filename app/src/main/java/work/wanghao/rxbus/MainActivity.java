package work.wanghao.rxbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import work.wanghao.rxbus2.RxBus;

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

    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        RxBus.Companion.get().post(new EventMsg("测试方法"));
      }
    });
    init();
  }

  private void init() {
    mCompositeDisposable.add(RxBus.Companion.get()
        .asFlowableFilterType(EventMsg.class)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            EventMsg eventMsg = (EventMsg) o;
            mTextView.setText(eventMsg.mString);
          }
        }));
  }

  @Override protected void onStop() {
    super.onStop();
    mCompositeDisposable.dispose();
  }
}
