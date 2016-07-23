package work.wanghao.rxbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SecondActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second);
  }

  private void initText() {
    StackTraceElement stackTraceElement[] = new Throwable().getStackTrace();
    for (int i = 0; i < stackTraceElement.length; i++) {
      StackTraceElement element = stackTraceElement[i];
      Log.d("getClassName", "element.getClassName()-->" + i + "-->" + element.getClassName());
      Log.d("getMethodName", "element.getMethodName()" + i + "-->" + element.getMethodName());
      Log.d("getLineNumber", "element.getLineNumber()" + i + "-->" + element.getLineNumber() + "");
      Log.d("getFileName", "element.getFileName()" + i + "-->" + element.getFileName());
    }
  }
}
