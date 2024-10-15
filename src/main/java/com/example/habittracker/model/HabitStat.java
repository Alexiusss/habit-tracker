package com.example.habittracker.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Simple JavaBean domain object representing a habitstat.
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
public class HabitStat extends BaseEntity{
    Integer userId;
    Integer habitId;
}