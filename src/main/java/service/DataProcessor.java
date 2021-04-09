package service;

import exception.NullDataException;
import model.Entity;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataProcessor {

    public static Map<String, Double> findRelativeFrequencyForUser(List<Entity> entities, String eventName) throws NullDataException{
        Map<String, Double> relativeFrequencies = new HashMap<>();
        int listSize = filterData(entities, eventName).size();
        Map<String, Long> userMap = findAbsFrequencyForUser(entities, eventName);
        for (Map.Entry<String, Long> entry: userMap.entrySet()) {
            relativeFrequencies.put(entry.getKey(), ((double)entry.getValue() / listSize) * 100);
        }
        return relativeFrequencies;
    }

    public static Map<String, Long> findAbsFrequencyForUser(List<Entity> entities, String eventName) throws NullDataException {
        if(entities == null || eventName == null) {
            throw new NullDataException("Params are null");
        }
        Map<String, Long> usersMap = new HashMap<>();
        List<Entity> filteredEntities = filterData(entities, eventName);
        for (Entity entity: filteredEntities) {
            String userId = extractUserId(entity.getDescription());
            if (userId != null && usersMap.putIfAbsent(userId, 1L) != null) {
                usersMap.put(userId, usersMap.get(userId)+1);
            }
        }
        return usersMap;
    }

    public static double findMedian(List<Entity> entities, String eventName) throws NullDataException{
        Map<String, Long> usersMap = findAbsFrequencyForUser(entities, eventName);
        List<Map.Entry<String, Long>> entries = new LinkedList<>(usersMap.entrySet());
        Collections.sort(entries, Comparator.comparing(Map.Entry::getValue));
        if (entries.size() % 2 == 0) {
            return (entries.get((entities.size() / 2)).getValue() + entries.get((entries.size() / 2) + 1).getValue())/ 2;
        }
        return entries.get(entries.size()/2).getValue();
    }

    public static long findScope(List<Entity> entities, String eventName) throws NullDataException {
        Map<String, Long> usersMap = findAbsFrequencyForUser(entities, eventName);
        List<Map.Entry<String, Long>> entries = new LinkedList<>(usersMap.entrySet());
        Collections.sort(entries, Comparator.comparing(Map.Entry::getValue));
        return entries.get(entries.size() - 1).getValue() - entries.get(0).getValue();
    }

    private static List<Entity> filterData(List<Entity> entities, String eventName) {
        return entities.stream().filter(e -> e.getEventName().equals(eventName)).collect(Collectors.toList());
    }

    private static String extractUserId(String description) {
        String userId = null;
        String regex = "(\\d{4})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(description);
        if(matcher.find()) {
            userId = matcher.group();
        }
        return userId;
    }
}
