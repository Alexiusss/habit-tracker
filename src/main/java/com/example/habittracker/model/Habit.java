package com.example.habittracker.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Period;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Habit extends NamedEntity implements HasId {
    private Period frequency;
    private boolean isActive;
}