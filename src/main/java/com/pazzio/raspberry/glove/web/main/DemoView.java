package com.pazzio.raspberry.glove.web.main;

import com.pazzio.raspberry.glove.dtos.AccountDto;
import com.pazzio.raspberry.glove.services.AccountService;
import com.pazzio.raspberry.glove.web.create.CreateView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("main")
@PageTitle("Dashboard")
public class DemoView extends VerticalLayout implements HasUrlParameter<String> {

    private AccountDto accountDto = new AccountDto();

    private AccountService accountService;

    public DemoView(@Autowired AccountService accountService) {
        setSizeFull();
        this.accountService = accountService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        accountDto.setToken(s);
        accountDto = accountService.getAccount(accountDto);
        buildTopBar();
        buildMainLayout();
    }

    public void buildTopBar() {
        Button createButton = new Button("Create Loadout");
        H3 name = new H3();
        name.setText(String.format("Logged in as %s", accountDto.username));
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setWidth("100%");
        createButton.getElement().getStyle().set("margin-left", "auto");
        createButton.setIcon(new Icon(VaadinIcon.PLUS));
        createButton.addClickListener(e -> createButton.getUI().ifPresent(ui -> ui.navigate(CreateView.class, accountDto.token)));
        createButton.getElement().getStyle().set("margin-right", "60px");
        topBar.add(name);
        topBar.add(createButton);
        topBar.setAlignItems(Alignment.AUTO);
        topBar.setHeight("20%");
        add(topBar);
    }

    public void buildMainLayout() {
        if (accountDto.getLoadoutList() == null || accountDto.getLoadoutList().isEmpty()) {
            add(new Paragraph("Please create some loadouts!"));
        }
    }
}
