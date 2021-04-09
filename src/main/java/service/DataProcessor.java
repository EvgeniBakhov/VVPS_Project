package service;

import model.Entity;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataProcessor {

    public static Map<Integer, Double> findRelativeFrequencyForUser(List<Entity> entities, String eventName) {
        Map<Integer, Double> relativeFrequencies = new HashMap<>();
        int listSize = filterData(entities, eventName).size();
        Map<Integer, Long> userMap = findAbsFrequencyForUser(entities, eventName);
        for (Map.Entry<Integer, Long> entry: userMap.entrySet()) {
            relativeFrequencies.put(entry.getKey(), ((double)entry.getValue() / listSize) * 100);
        }
        return relativeFrequencies;
    }

    public static Map<Integer, Long> findAbsFrequencyForUser(List<Entity> entities, String eventName) {
        Map<Integer, Long> usersMap = new HashMap<>();
        List<Entity> filteredEntities = filterData(entities, eventName);
        for (Entity entity: filteredEntities) {
            int userId = extractUserId(entity.getDescription());
            if (usersMap.putIfAbsent(userId, 1L) != null) {
                usersMap.put(userId, usersMap.get(userId)+1);
            }
        }
        return usersMap;
    }

    public static double findMedian(List<Entity> entities, String eventName) {
        Map<Integer, Long> usersMap = findAbsFrequencyForUser(entities, eventName);
        List<Map.Entry<Integer, Long>> entries = new LinkedList<>(usersMap.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<Integer, Long>>() {
            @Override
            public int compare(Map.Entry<Integer, Long> o1, Map.Entry<Integer, Long> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        if (entries.size() % 2 == 0) {
            return (entries.get((entities.size() / 2)).getValue() + entries.get((entries.size() / 2) + 1).getValue())/ 2;
        }
        return entries.get(entries.size()/2).getValue();
    }

    public static void findScope(List<Entity> entities, String eventName) {

    }

    private static List<Entity> filterData(List<Entity> entities, String eventName) {
        return entities.stream().filter(e -> e.getEventName().equals(eventName)).collect(Collectors.toList());
    }

    private static int extractUserId(String description) {
        String regex = "\'\\d{4}\'";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            System.out.println(matcher.group());
        } else {
            return -1;
        }
        return Integer.parseInt(matcher.group());
    }
}
