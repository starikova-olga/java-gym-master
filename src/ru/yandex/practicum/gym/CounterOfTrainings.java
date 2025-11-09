package ru.yandex.practicum.gym;

public class CounterOfTrainings {
    private String coachName;
    private int numberOfTrainings;


    public CounterOfTrainings(Coach key, Integer value) {
        this.coachName = key.getName();
        this.numberOfTrainings = value;
    }

    public String getCoachName() {
        return coachName;
    }

    public int getNumberOfTrainings() {
        return numberOfTrainings;
    }
}

