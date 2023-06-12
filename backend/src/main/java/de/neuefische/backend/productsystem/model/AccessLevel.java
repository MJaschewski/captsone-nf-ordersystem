package de.neuefische.backend.productsystem.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum AccessLevel {
    ALL("All"),
    PURCHASE("Purchase"),
    LEAD("Lead");
    private String level;
}