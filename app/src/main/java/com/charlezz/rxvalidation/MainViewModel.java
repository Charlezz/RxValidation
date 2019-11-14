package com.charlezz.rxvalidation;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class MainViewModel extends AndroidViewModel {
    public static final String TAG = MainViewModel.class.getSimpleName();

    private final BehaviorSubject<String> name = BehaviorSubject.createDefault("Charles");
    private final BehaviorSubject<String> email = BehaviorSubject.createDefault("android@charlezz.com");
    private final BehaviorSubject<String> phone = BehaviorSubject.createDefault("010-1234-5678");

    private final MutableLiveData<Boolean> btnEnabled = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();


    public MainViewModel(Application application) {
        super(application);
        btnEnabled.setValue(false);
        disposables.add(
                Observable.combineLatest(
                        getNameValidator(),
                        getPhoneValidator(),
                        getEmailValidator(),
                        (isNameValid, isPhoneValid, isEmailValid) -> isNameValid && isPhoneValid && isEmailValid
                ).subscribe(result -> {
                    Log.e(TAG,"final result = "+result);
                    btnEnabled.postValue(result);
                }));
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.onNext(name);
    }

    public String getEmail() {
        return email.getValue();
    }

    public void setEmail(String email) {
        this.email.onNext(email);
    }

    public String getPhone() {
        return phone.getValue();
    }

    public void setPhone(String phone) {
        this.phone.onNext(phone);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    private Observable<Boolean> getNameValidator() {
        return name.map(str->{
            boolean result = !TextUtils.isEmpty(str);
            Log.e(TAG,"name = "+result);
            return result;
        });
    }

    private Observable<Boolean> getPhoneValidator() {
        return phone.map(phone -> {
            boolean result = Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", phone);
            Log.e(TAG,"phone = "+result);
            return result;
        });
    }

    private Observable<Boolean> getEmailValidator() {
        return email.map(mail -> {
            boolean result = android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches();
            Log.e(TAG,"email = "+result);
            return result;
        });
    }

    public LiveData<Boolean> getBtnEnabled() {
        return btnEnabled;
    }

    public void showToast(){
        Toast.makeText(getApplication(), "가입성공!",Toast.LENGTH_SHORT).show();
    }
}
