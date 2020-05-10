package com.pazzio.raspberry.glove.web.main;

import com.github.juchar.colorpicker.ColorPickerField;
import com.pazzio.raspberry.glove.dtos.AccountDto;
import com.pazzio.raspberry.glove.dtos.LoadoutDto;
import com.pazzio.raspberry.glove.dtos.RGBValueDto;
import com.pazzio.raspberry.glove.services.AccountService;
import com.pazzio.raspberry.glove.web.create.CreateView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


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
        name.setText(String.format("Logged in as %s (serial number: %s)", accountDto.username, accountDto.serialNumber));
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setWidth("100%");
        createButton.getElement().getStyle().set("margin-left", "auto");
        createButton.setIcon(new Icon(VaadinIcon.PLUS));
        createButton.addClickListener(e -> createButton.getUI().ifPresent(ui -> {
            Map<String, List<String>> queryParameters = new HashMap<>();
            List<String> token = new ArrayList<String>(){{
                add(accountDto.token);
            }};
            queryParameters.put("token", token);

            ui.navigate("create", new QueryParameters(queryParameters));
        }));
        createButton.getElement().getStyle().set("margin-right", "60px");
        topBar.add(name);
        topBar.add(createButton);
        topBar.setAlignItems(Alignment.AUTO);
        topBar.setHeight("20%");
        add(topBar);
    }

    public void buildMainLayout() {
        VerticalLayout mainLayout = new VerticalLayout();
        if (accountDto.getLoadoutList() == null || accountDto.getLoadoutList().isEmpty()) {
            add(new Paragraph("Please create some loadouts!"));
            return;
        }
        HorizontalLayout row = new HorizontalLayout();
        int numberInRow = 0;
        ArrayList<LoadoutDto> loadoutDtoList = (ArrayList<LoadoutDto>) accountDto.getLoadoutList();
        for (int i=0;i<accountDto.getLoadoutList().size(); i++) {
            LoadoutDto loadoutDto = loadoutDtoList.get(i);
            VerticalLayout loadoutLayout = buildLoadoutBox(loadoutDto, i);
            row.add(loadoutLayout);
            numberInRow++;
            if (numberInRow == 4) {
                mainLayout.add(row);
                row = new HorizontalLayout();
                numberInRow = 0;
            }
        }
        if (numberInRow != 0) {
            mainLayout.add(row);
        }
        add(mainLayout);
    }


    public VerticalLayout buildLoadoutBox(LoadoutDto loadoutDto, Integer idx) {
        VerticalLayout loadoutLayout = new VerticalLayout();
        Button edit = new Button("Edit");
        edit.setIcon(new Icon(VaadinIcon.PENCIL));
        edit.addClickListener(e -> {
            Map<String, List<String>> queryParameters = new HashMap<>();
            List<String> token = new ArrayList<String>(){{
                add(accountDto.token);
            }};
            queryParameters.put("token", token);
            List<String> idxList = new ArrayList<String>(){{
                add(idx.toString());
            }};
            queryParameters.put("idx", idxList);
            edit.getUI().ifPresent(ui -> ui.navigate("create", new QueryParameters(queryParameters)));
        });
        loadoutLayout.add(edit);
        Checkbox active = new Checkbox("Loadout is active", loadoutDto.active);
        active.setReadOnly(true);
        loadoutLayout.add(active);
        for (RGBValueDto rgbValueDto : loadoutDto.getRgbValues()) {
            HorizontalLayout rgbLayout = new HorizontalLayout();

            Label name = new Label(String.format("Finger %d", rgbValueDto.finger));
            rgbLayout.add(name);
            ColorPickerField colorPickerField = new ColorPickerField("as Color", new Color(rgbValueDto.red, rgbValueDto.green, rgbValueDto.blue), "#fff");
            colorPickerField.setReadOnly(true);
            rgbLayout.add(colorPickerField);
            rgbLayout.setAlignItems(Alignment.END);
            loadoutLayout.add(rgbLayout);
        }
        loadoutLayout.getElement().getStyle().set("border", "2px solid black");
        loadoutLayout.getElement().getStyle().set("margin-right","7em");
        return loadoutLayout;
    }

}
