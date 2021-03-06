package com.testing.user.rx.timeout;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.testing.user.rx.NameRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserPresenter {

  public interface Listener {
    void onUserNameLoaded(String name);

    void onGettingUserNameError(String message);
  }

  private final Listener listener;
  private final NameRepository nameRepository;

  public UserPresenter(Listener listener, NameRepository nameRepository) {
    this.listener = listener;
    this.nameRepository = nameRepository;
  }

  public void getUserName() {
    nameRepository
        .getName()
        .timeout(2, SECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            listener::onUserNameLoaded,
            error -> listener.onGettingUserNameError(error.getMessage()));
  }
}
