package com.pazzio.raspberry.glove.web.login;

import com.pazzio.raspberry.glove.dtos.AccountDto;
import com.pazzio.raspberry.glove.services.AccountService;
import com.pazzio.raspberry.glove.web.main.DemoView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout {

    private AccountService accountService;

    public LoginView(@Autowired AccountService accountService) {
        setSizeFull();
        this.accountService = accountService;
        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(e -> {
            AccountDto res = login(e);
            if (res != null) {
                loginForm.getUI().ifPresent(ui -> ui.navigate(DemoView.class, res.token));
            }

        });
        FlexLayout flexLayout = new FlexLayout();
        flexLayout.setSizeFull();
        flexLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        flexLayout.setAlignItems(Alignment.CENTER);
        flexLayout.add(loginForm);

        Component loginInformation = buildLoginInformation();

        add(loginInformation);
        add(flexLayout);
    }

    private AccountDto login(AbstractLogin.LoginEvent loginEvent) {
        AccountDto accountDto = new AccountDto();
        accountDto.setUsername(loginEvent.getUsername());
        accountDto.setPassword(loginEvent.getPassword());
        AccountDto result = accountService.getAccount(accountDto);
        System.out.println(result);
        if (result.token != null) {
            return result;
        }
        return null;
    }

    public Component buildLoginInformation() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setClassName("login-information");
        H1 loginHeader = new H1("Login Information");
        loginHeader.setWidth("100%");
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button registerPage = new Button("Register", buttonClickEvent -> getUI().get().navigate("register"));
        registerPage.setText("Register");
        Paragraph p = new Paragraph();
        p.setText("if you don't have an account, register here:");
        horizontalLayout.add(p);
        horizontalLayout.add(registerPage);
        verticalLayout.add(loginHeader);
        verticalLayout.add(horizontalLayout);
        return verticalLayout;
    }

}
