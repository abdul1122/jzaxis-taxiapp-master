package taxiapp.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Nisar on 12/1/2015.
 */
public class GenericTextWatcher implements TextWatcher {

    private EditText et;

    public GenericTextWatcher(EditText et) {
        this.et = et;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        et.setError(null);

    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {

    }
}