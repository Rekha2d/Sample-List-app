package com.android.rekha.samplelist;

import android.os.Bundle;

/**
 * OnChangeListener is a callback for actions change performed on patient list.
 * Created by rekha on 7/9/2018.
 */

public interface OnChangeListener {
     void onDelete(int index);
     void onUpdate(Bundle value);
}
