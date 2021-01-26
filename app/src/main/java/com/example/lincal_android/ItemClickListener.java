package com.example.lincal_android;

import android.view.View;

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Interfície: ItemClickListener
Tipus: interface
Funció: respon quan els objectes esdeveniment i/o tasca han sigut clicats
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

public interface ItemClickListener {
    void onClick(View view, int position, boolean IsLongClick);
}
