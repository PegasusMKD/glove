package com.pazzio.raspberry.glove.web.create;

import com.github.juchar.colorpicker.ColorPickerField;
import com.pazzio.raspberry.glove.dtos.AccountDto;
import com.pazzio.raspberry.glove.dtos.LoadoutDto;
import com.pazzio.raspberry.glove.dtos.RGBValueDto;
import com.pazzio.raspberry.glove.services.AccountService;
import com.pazzio.raspberry.glove.web.main.DemoView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;


@Route("create")
public class CreateView extends VerticalLayout implements HasUrlParameter<String> {

    private AccountDto accountDto = new AccountDto();
    private LoadoutDto loadoutDto = new LoadoutDto();

    private AccountService accountService;

    private Integer idx;

    public CreateView(@Autowired AccountService accountService) {
        setSizeFull();
        this.accountService = accountService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String token) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters.getParameters();

        accountDto.setToken(parametersMap.get("token").get(0));
        this.accountDto = accountService.getAccount(accountDto);
        if(parametersMap.get("idx") != null){
            idx = Integer.parseInt(parametersMap.get("idx").get(0));
            loadoutDto = accountDto.getLoadoutList().get(idx);
        }

        buildLoadout();
    }

    public List<Button> buildRGBLayouts(HorizontalLayout loadout) {
        VerticalLayout mainRGBLayout = new VerticalLayout();
        mainRGBLayout.add(new H3("Enter the values for each finger"));
        List<RGBValueDto> rgbValues = new ArrayList<>();
        List<Button> saveButtons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Binder<RGBValueDto> rgbValueDtoBinder = new Binder<>(RGBValueDto.class);
            if(idx != null){
                rgbValues.add(i, loadoutDto.getRgbValues().get(i));
            } else {
                rgbValues.add(i, new RGBValueDto());
            }

            HorizontalLayout rgbLayout = new HorizontalLayout();
            Label fingerLabel = new Label(String.format("Finger %d", i + 1));

            NumberField activeTime = new NumberField("Active Time (in seconds)");
            if(idx == null){
                activeTime.setValue(0.0);
            }
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
            rgbValueDtoBinder.forField(pauseTime).bind(RGBValueDto::getPauseTime, RGBValueDto::setPauseTime);
            if(idx == null){
                pauseTime.setValue(0.0);
            }

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
            });
            if(idx != null){
                rgbValueDtoBinder.readBean(rgbValues.get(i));
            }
            saveButtons.add(getValues);
            rgbLayout.add(fingerLabel, activeTime, colorPicker, pauseTime, getValues);
            rgbLayout.setAlignItems(Alignment.END);
            mainRGBLayout.add(rgbLayout);
        }
        loadout.add(mainRGBLayout);
        loadoutDto.setRgbValues(rgbValues);
        return saveButtons;
    }

    public void buildLoadout() {
        VerticalLayout loadout = new VerticalLayout();

        Button mainSaveButton = new Button("Save");
        mainSaveButton.setIcon(new Icon(VaadinIcon.ARCHIVE));
        mainSaveButton.getElement().getStyle().set("margin-left", "auto");
        loadout.add(mainSaveButton);

        loadout.add(new Span("The legend/explanation of the fields:"));
        loadout.add(new Span("- Active Time - how much time (in seconds) do you want to have that finger's LED be on (you can enter decimals)"));
        loadout.add(new Span("- Color - You open the color picker, pick a color that you want the LED to display"));
        loadout.add(new Span("- Pause Time - How much time do you want the LED of that particular finger to be off"));

        HorizontalLayout lowerLoadout = new HorizontalLayout();

        List<Button> saveButtons = buildRGBLayouts(lowerLoadout);
        List<NumberField> pauseValues = buildPauseValues(lowerLoadout);
        Checkbox active = new Checkbox("Loadout is active", true);

        loadout.add(active);
        loadout.add(lowerLoadout);

        mainSaveButton.addClickListener(e -> {
            List<Double> realPauseValues = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                saveButtons.get(i).click();
                realPauseValues.add(pauseValues.get(i).getValue());
            }
            loadoutDto.setPauseValues(realPauseValues);
            loadoutDto.setActive(active.getValue());
            if(idx == null){
                accountDto.getLoadoutList().add(loadoutDto);
            }
            loadoutDto.getRgbValues().sort(Comparator.comparing(o -> o.finger));
            accountService.save(accountDto);
            mainSaveButton.getUI().ifPresent(ui -> ui.navigate(DemoView.class, accountDto.token));
        });

        add(loadout);
    }

    public List<NumberField> buildPauseValues(HorizontalLayout loadout) {
        VerticalLayout mainPauseLayout = new VerticalLayout();
        List<NumberField> pauseValues = new ArrayList<>();
        mainPauseLayout.add(new H3("Enter how much time you'd like the script to wait between activating between each finger"));
        for (int i = 0; i < 4; i++) {
            HorizontalLayout row = new HorizontalLayout();
            Label fingerStart = new Label(String.format("Finger %d", i + 1));
            NumberField pauseValue = new NumberField();
            if(idx == null){
                pauseValue.setValue(0.0);
            } else {
                pauseValue.setValue(loadoutDto.getPauseValues().get(i));
            }
            pauseValues.add(i, pauseValue);
            Label fingerEnd = new Label(String.format("Finger %d", i + 2));
            row.setAlignItems(Alignment.CENTER);
            row.add(fingerStart, pauseValue, fingerEnd);
            mainPauseLayout.add(row);
        }
        HorizontalLayout row = new HorizontalLayout();
        Label fingerStart = new Label("Finger 5");
        NumberField pauseValue = new NumberField();
        if(idx == null){
            pauseValue.setValue(0.0);
        } else {
            pauseValue.setValue(loadoutDto.getPauseValues().get(4));
        }
        pauseValues.add(4, pauseValue);
        Label fingerEnd = new Label("Finger 1");
        row.setAlignItems(Alignment.CENTER);
        row.add(fingerStart, pauseValue, fingerEnd);
        mainPauseLayout.add(row);
        loadout.add(mainPauseLayout);
        return pauseValues;
    }

}
