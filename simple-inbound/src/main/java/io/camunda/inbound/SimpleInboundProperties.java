package io.camunda.inbound;

import io.camunda.connector.generator.java.annotation.TemplateProperty;
import jakarta.validation.constraints.NotNull;


public record SimpleInboundProperties(
    @NotNull
    @TemplateProperty(
        group = "settings",
        label = "Colors",
        description = "This is a sample dropdown",
        type = TemplateProperty.PropertyType.Dropdown,
        choices = {
            @TemplateProperty.DropdownPropertyChoice(
                value = "orange",
                label = "Orange"),
            @TemplateProperty.DropdownPropertyChoice(
                value = "blue",
                label = "Blue"),
            @TemplateProperty.DropdownPropertyChoice(
                value = "red",
                label = "red"),
            @TemplateProperty.DropdownPropertyChoice(
                value = "purple",
                label = "purple"),
        })
    Colors colors
  ) {

  public enum Colors {
    orange, blue, red, purple
  }
}