package com.pazzio.raspberry.glove.web.register;

import com.pazzio.raspberry.glove.dtos.AccountDto;
import com.pazzio.raspberry.glove.services.AccountService;
import com.pazzio.raspberry.glove.web.main.DemoView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("register")
public class RegisterView extends VerticalLayout {

    private AccountService accountService;

    public RegisterView(@Autowired AccountService accountService) {
        this.accountService = accountService;
        setSizeFull();
        Element loginForm = buildLoginForm();
        setClassName("login-view");

        H2 headerForLogin = new H2("Enter your data please!");
        VerticalLayout flexLayout = new VerticalLayout();
        flexLayout.setSizeFull();
        flexLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        flexLayout.setAlignItems(Alignment.CENTER);
        flexLayout.add(headerForLogin);
        flexLayout.getElement().appendChild(loginForm);
        Component loginDetails = buildLoginInformation();
        add(loginDetails);
        add(flexLayout);

    }

    public Element buildLoginForm() {
        TextField userNameTextField = new TextField();
        userNameTextField.setPlaceholder("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPlaceholder("Password");

        TextField serialNumberField = new TextField();
        serialNumberField.setPlaceholder("Serial Number of Device:");

        Button submitButton = new Button("Login");
        submitButton.addClickListener(e -> {
            AccountDto accountDto = new AccountDto();
            accountDto.setUsername(userNameTextField.getValue());
            accountDto.setPassword(passwordField.getValue());
            accountDto.setSerialNumber(serialNumberField.getValue());
            AccountDto result = accountService.save(accountDto);
            System.out.println(result);
            submitButton.getUI().ifPresent(ui -> ui.navigate(DemoView.class, result.token));
        });


        FormLayout formLayout = new FormLayout(); //
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("7em", 1),
                new FormLayout.ResponsiveStep("7em", 1),
                new FormLayout.ResponsiveStep("7em", 1),
                new FormLayout.ResponsiveStep("7em", 1)
        );
        formLayout.add(userNameTextField, serialNumberField, passwordField, submitButton);

        Element formElement = new Element("form"); //
        formElement.setAttribute("method", "post");
        formElement.setAttribute("action", "login");
        formElement.appendChild(formLayout.getElement());

        Element ironForm = new Element("iron-form"); //
        ironForm.setAttribute("id", "ironform");
        ironForm.setAttribute("allow-redirect", true); //
        ironForm.appendChild(formElement);

        return ironForm;
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
