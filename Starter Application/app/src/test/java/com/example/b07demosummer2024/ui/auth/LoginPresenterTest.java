package com.example.b07demosummer2024.ui.auth;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import android.text.TextUtils;
import com.google.firebase.auth.AuthResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.MockedStatic;

public class LoginPresenterTest {
    @Mock LoginContract.View mockView;
    @Mock AuthRepository     mockRepo;
    @Captor ArgumentCaptor<AuthRepository.AuthCallback> callbackCaptor;

    private LoginPresenter presenter;
    private MockedStatic<TextUtils> textUtilsMock;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // stub out TextUtils.isEmpty(...) so presenter can run in plain JVM tests
        textUtilsMock = Mockito.mockStatic(TextUtils.class);
        textUtilsMock
                .when(() -> TextUtils.isEmpty(any(CharSequence.class)))
                .thenAnswer(inv -> {
                    CharSequence arg = inv.getArgument(0);
                    return arg == null || arg.length() == 0;
                });

        presenter = new LoginPresenter(mockRepo);
        presenter.attachView(mockView);
    }

    @After
    public void tearDown() {
        textUtilsMock.close();
    }

    @Test
    public void emptyEmail_showsEmailErrorOnly() {
        presenter.onLoginClicked("", "validPass");

        verify(mockView).showEmailError("Email is required");
        verify(mockView, never()).showPasswordError(anyString());
        verifyNoInteractions(mockRepo);
    }

    @Test
    public void shortPassword_showsPasswordErrorOnly() {
        presenter.onLoginClicked("a@b.com", "123");

        verify(mockView).showPasswordError("Password must be at least 6 characters");
        verify(mockView, never()).showEmailError(anyString());
        verifyNoInteractions(mockRepo);
    }

    @Test
    public void bothEmpty_showsBothErrorsInOrder() {
        presenter.onLoginClicked("", "");

        InOrder inOrder = inOrder(mockView);
        inOrder.verify(mockView).showEmailError("Email is required");
        inOrder.verify(mockView).showPasswordError("Password must be at least 6 characters");

        verifyNoInteractions(mockRepo);
    }

    @Test
    public void validCredentials_showsProgress_thenCallsRepo() {
        String email = "u@u.com";
        String pass  = "abcdef";

        presenter.onLoginClicked(email, pass);

        verify(mockView).showProgress(true);
        verify(mockRepo).signIn(eq(email), eq(pass), callbackCaptor.capture());
        verify(mockView, never()).showProgress(false);
        verify(mockView, never()).onLoginSuccess();
        verify(mockView, never()).showGeneralError(anyString());
    }

    @Test
    public void onSignInSuccess_hidesProgress_andNotifiesView() {
        presenter.onLoginClicked("u@u.com", "123456");
        verify(mockRepo).signIn(anyString(), anyString(), callbackCaptor.capture());

        AuthResult dummy = mock(AuthResult.class);
        callbackCaptor.getValue().onSuccess(dummy);

        InOrder inOrder = inOrder(mockView);
        inOrder.verify(mockView).showProgress(true);
        inOrder.verify(mockView).showProgress(false);
        inOrder.verify(mockView).onLoginSuccess();
    }

    @Test
    public void onSignInFailure_hidesProgress_andShowsGeneralError() {
        presenter.onLoginClicked("u@u.com", "123456");
        verify(mockRepo).signIn(anyString(), anyString(), callbackCaptor.capture());

        Exception ex = new Exception("Network error");
        callbackCaptor.getValue().onFailure(ex);

        InOrder inOrder = inOrder(mockView);
        inOrder.verify(mockView).showProgress(true);
        inOrder.verify(mockView).showProgress(false);
        inOrder.verify(mockView).showGeneralError("Network error");
    }

    @Test
    public void detachView_beforeLogin_ignoresAll() {
        presenter.detachView();
        presenter.onLoginClicked("x@x.com", "abcdef");

        verifyNoInteractions(mockView, mockRepo);
    }

    @Test
    public void detachView_beforeCallback_preventsViewCalls() {
        presenter.onLoginClicked("y@y.com", "654321");
        verify(mockRepo).signIn(anyString(), anyString(), callbackCaptor.capture());

        // clear the initial showProgress(true) call
        clearInvocations(mockView);

        presenter.detachView();
        callbackCaptor.getValue().onSuccess(mock(AuthResult.class));
        callbackCaptor.getValue().onFailure(new Exception("oops"));

        verifyNoInteractions(mockView);
    }
}
