package taxiapp.utils;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by hassanjamil on 2015-12-03.
 */
public class EditTextUtils {

    /**
     * If field is empty than an error message will be generated to the field based on its hint.
     * @param editText
     * @return
     */
    public static boolean isFieldEmpty(EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.requestFocus();
            editText.setError(editText.getHint().toString()+" is missing");
            return true;
        }
        else
            return false;
    }

}
