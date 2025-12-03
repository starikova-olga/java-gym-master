package ru.yandex.practicum.gym;


import java.util.*;

public class Timetable {

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>>  timetable;

    public Timetable() {
        timetable = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
      DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
      TimeOfDay timeOfDay = trainingSession.getTimeOfDay();

      if (!timetable.containsKey(dayOfWeek)) {
          timetable.put(dayOfWeek, new TreeMap<>()); //сохраняем занятие в расписании
      }
      TreeMap<TimeOfDay, List<TrainingSession>> dayTimetable = timetable.get(dayOfWeek);
      if (!dayTimetable.containsKey(timeOfDay)) {
          dayTimetable.put(timeOfDay, new ArrayList<>());
      }
      dayTimetable.get(timeOfDay).add(trainingSession);
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> trainingForDay = timetable.getOrDefault(dayOfWeek, new TreeMap<>());
        if (trainingForDay == null) {
            return new TreeMap<>(); //как реализовать, тоже непонятно, но сложность должна быть О(1)
        }
        return trainingForDay;
    }


    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
       TreeMap<TimeOfDay, List<TrainingSession>> dayTimetable = timetable.getOrDefault(dayOfWeek, new TreeMap<>());
       return dayTimetable.getOrDefault(timeOfDay, Collections.emptyList()); //как реализовать, тоже непонятно, но сложность должна быть О(1)
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachTrainingsCount = new HashMap<>();
        for (Map.Entry<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> entry : timetable.entrySet()) {
            TreeMap<TimeOfDay, List<TrainingSession>> dayTimetable = entry.getValue();
            for (List<TrainingSession> sessions : dayTimetable.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachTrainingsCount.put(coach, coachTrainingsCount.getOrDefault(coach, 0) + 1);
                }
            }
        }
        List<CounterOfTrainings> coachesCount = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachTrainingsCount.entrySet()) {
            coachesCount.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }
            coachesCount.sort(Comparator.comparingInt(CounterOfTrainings::getNumberOfTrainings).reversed());
            return coachesCount;
        }

    }

