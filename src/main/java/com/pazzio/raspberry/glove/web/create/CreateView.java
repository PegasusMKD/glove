package com.pazzio.raspberry.glove.web.create;

import com.github.juchar.colorpicker.ColorPickerField;
import com.pazzio.raspberry.glove.dtos.AccountDto;
import com.pazzio.raspberry.glove.dtos.LoadoutDto;
import com.pazzio.raspberry.glove.dtos.RGBValueDto;
import com.pazzio.raspberry.glove.services.AccountService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;


@Route("create")
public class CreateView extends VerticalLayout implements HasUrlParameter<String> {

    private AccountDto accountDto = new AccountDto();
    private LoadoutDto loadoutDto = new LoadoutDto();

    private AccountService accountService;

    public CreateView(@Autowired AccountService accountService) {
        setSizeFull();
        this.accountService = accountService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String token) {
        accountDto.setToken(token);
        this.accountDto = accountService.getAccount(accountDto);
        buildLoadout();
    }

    public void buildExplanation() {

    }

    public List<Button> buildRGBLayouts(VerticalLayout loadout) {
        List<RGBValueDto> rgbValues = new ArrayList<>();
        List<Button> saveButtons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Binder<RGBValueDto> rgbValueDtoBinder = new Binder<>(RGBValueDto.class);
            rgbValues.add(i, new RGBValueDto());

            HorizontalLayout rgbLayout = new HorizontalLayout();
            Label fingerLabel = new Label(String.format("Finger %d", i + 1));

            NumberField activeTime = new NumberField("Active Time (in seconds)");
            activeTime.setValue(0.0);
            rgbValueDtoBinder.forField(activeTime).bind(RGBValueDto::getActiveTime, RGBValueDto::setActiveTime);

            ColorPickerField colorPicker = new ColorPickerField("Color", Color.BLUE, "#fff");

            rgbValueDtoBinder.bind(colorPicker,
                    rgbValueDto -> new Color(ofNullable(rgbValueDto.red).orElse(0), ofNullable(rgbValueDto.green).orElse(0), ofNullable(rgbValueDto.blue).orElse(0)),
                    ((rgbValueDto, color) -> {
                        rgbValueDto.red = color.getRed();
                        rgbValueDto.green = color.getGreen();
                        rgbValueDto.blue = color.getBlue();
                    }));

            NumberField pauseTime = new NumberField("Pause Time (in seconds)");
            pauseTime.setValue(0.0);
            rgbValueDtoBinder.forField(pauseTime).bind(RGBValueDto::getPauseTime, RGBValueDto::setPauseTime);


            Button getValues = new Button("Get values TEST");
            getValues.setVisible(false);

            int finalI = i;
            getValues.addClickListener(e -> {
                RGBValueDto rgbValue = rgbValues.get(finalI);
                try {
                    rgbValueDtoBinder.writeBean(rgbValue);
                    rgbValue.finger = finalI + 1;
                } catch (ValidationException ex) {
                    ex.printStackTrace();
                }
                System.out.println(rgbValue);

            });

            saveButtons.add(getValues);
            rgbLayout.add(fingerLabel, activeTime, colorPicker, pauseTime, getValues);
            rgbLayout.setAlignItems(Alignment.END);
            loadout.add(rgbLayout);
        }
        loadoutDto.setRgbValues(rgbValues);
        return saveButtons;
    }

    public void buildLoadout() {
        VerticalLayout loadout = new VerticalLayout();
        Button mainSaveButton = new Button("Save");
        mainSaveButton.setIcon(new Icon(VaadinIcon.ARCHIVE));
        loadout.add(mainSaveButton);
        List<Button> saveButtons = buildRGBLayouts(loadout);
        mainSaveButton.addClickListener(e -> {
            for (Button button : saveButtons) {
                button.click();
            }
            System.out.println(loadoutDto.getRgbValues());
        });
        add(loadout);
    }

    public void buildTopLoadout() {
//        Checkbox active = new Checkbox("Loadout is active:", true);

    }

}
