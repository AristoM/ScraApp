package com.scraapp.mediators;

import android.content.Intent;
import android.location.Location;

public interface CallbackMediator {

        void locationNoThanks();

        void locationOk();

        void autoCompleteCallback(Intent data);


}
