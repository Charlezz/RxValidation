package com.charlezz.rxvalidation;

import androidx.lifecycle.MutableLiveData;

public class InitMutableLiveData<T> extends MutableLiveData<T>{
    public InitMutableLiveData(T initialValue){
        setValue(initialValue);
    }
}
