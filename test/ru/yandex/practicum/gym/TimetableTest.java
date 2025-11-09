package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySession = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySession.size());

        //Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySession = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySession.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySession = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySession.size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdaySession = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySession.size());

        assertEquals(new TimeOfDay(13, 0), thursdaySession.get(0).getTimeOfDay());
        assertEquals(new TimeOfDay(20, 0), thursdaySession.get(1).getTimeOfDay());

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySassion = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySassion.isEmpty());

    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> mondaySession = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, mondaySession.size());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        mondaySession = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(mondaySession.isEmpty());
    }

    //проверить,что график тренировок пуст
    @Test
    void testGetCountByCoaches_EmptyTimetable() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> coachCounts = timetable.getCountByCoaches();

        assertTrue(coachCounts.isEmpty());
    }

    //Проверить,что метод корректно работает
    @Test
    void testGetCountByCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Петров", "Николай", "Иванович");
        Coach coach2 = new Coach("Иванов", "Иван", "Иванович");

        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Фитнес", Age.ADULT, 90);

        TrainingSession session1 = new TrainingSession(group1, coach1, DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2, DayOfWeek.TUESDAY, new TimeOfDay(20, 0));
        TrainingSession session3 = new TrainingSession(group1, coach1, DayOfWeek.THURSDAY, new TimeOfDay(15, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);

        List<CounterOfTrainings> coachCounts = timetable.getCountByCoaches();

        assertEquals(2, coachCounts.size());
        CounterOfTrainings counter1 = coachCounts.get(0);
        CounterOfTrainings counter2 = coachCounts.get(1);

        assertTrue(counter1.getCoachName().equals(coach1.getName()) || counter1.getCoachName().equals(coach2.getName()));
        assertTrue(counter2.getCoachName().equals(coach1.getName()) || counter2.getCoachName().equals(coach2.getName()));

        if (counter1.getCoachName().equals(coach1.getName())) {
            assertEquals(2, counter1.getNumberOfTrainings());
            assertEquals(1, counter2.getNumberOfTrainings());

        } else {
            assertEquals(1, counter1.getNumberOfTrainings());
            assertEquals(2, counter2.getNumberOfTrainings());
        }
    }
        //проверка на добавление тренера с одинаковыми данными на одно и тоже время
    @Test
    void testAddMultipleTrainingsSameCoachSameTime() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Акробатика для детей", Age.CHILD, 45);

        TrainingSession session1 = new TrainingSession(group, coach, DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession session2 = new TrainingSession(group, coach, DayOfWeek.THURSDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<CounterOfTrainings> coachCounts = timetable.getCountByCoaches();

        assertEquals(1, coachCounts.size());
        CounterOfTrainings counter = coachCounts.get(0);
        assertEquals(coach.getName(), counter.getCoachName());
        assertEquals(2, counter.getNumberOfTrainings());
    }

}

