package com.example.habittracker.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Period;

/**
 * Simple JavaBean domain object representing a habit.
 *
 * @author Alexey Boyarinov
 */
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
    private Integer userId;
}