package service;

import exception.NullDataException;
import model.Entity;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataProcessor {

    /**
     * Calculates relative frequency of event for each user.
     *
     * @param entities list that contains logs of events from other system.
     * @param eventName name of event to search for.
     * @return {@link Map} which contains user ids as key and relative frequency as value.
     * @throws NullDataException if any of parameters is null.
     */
    public static Map<String, Double> findRelativeFrequencyForUser(List<Entity> entities, String eventName) throws NullDataException{
        if (entities == null || eventName == null) {
            throw new NullDataException("Params are null");
        }
        Map<String, Double> relativeFrequencies = new HashMap<>();
        int listSize = filterData(entities, eventName).size();
        Map<String, Long> userMap = findAbsFrequencyForUser(entities, eventName);
        for (Map.Entry<String, Long> entry: userMap.entrySet()) {
            relativeFrequencies.put(entry.getKey(), ((double)entry.getValue() / listSize) * 100);
        }
        return relativeFrequencies;
    }

    /**
     * Calculates absolute frequency of event for each user.
     *
     * @param entities list that contains logs of events from other system.
     * @param eventName name of event to search for.
     * @return {@link Map} which contains user ids as key and absolute frequency as value.
     * @throws NullDataException if any of parameters is null.
     */
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

    /**
     * Calculates median of occurrences for each user of specific event in {@link List}.
     *
     * @param entities list of objects that representing events from other system.
     * @param eventName name of specific event to search for.
     * @return calculated median.
     * @throws NullDataException if one of the params is null.
     */
    public static double findMedian(List<Entity> entities, String eventName) throws NullDataException{
        if(entities == null || eventName == null) {
            throw new NullDataException("Params are null");
        }
        Map<String, Long> usersMap = findAbsFrequencyForUser(entities, eventName);
        List<Map.Entry<String, Long>> entries = new LinkedList<>(usersMap.entrySet());
        Collections.sort(entries, Comparator.comparing(Map.Entry::getValue));
        if (entries.size() % 2 == 0) {
            return (entries.get((usersMap.size() / 2)).getValue() + entries.get((usersMap.size() / 2) - 1).getValue())/ 2;
        }
        return entries.get(entries.size()/2).getValue();
    }


    /**
     * Calculates difference between user with max occurrences and user with min occurrences of specific event.
     *
     * @param entities {@link List} that contains events from other system.
     * @param eventName name of event to search for.
     * @return scope for the absolute frequencies of specific event for each user.
     *
     * @throws NullDataException if any of the params is null.
     */
    public static long findScope(List<Entity> entities, String eventName) throws NullDataException {
        Map<String, Long> usersMap = findAbsFrequencyForUser(entities, eventName);
        List<Map.Entry<String, Long>> entries = new LinkedList<>(usersMap.entrySet());
        Collections.sort(entries, Comparator.comparing(Map.Entry::getValue));
        return entries.get(entries.size() - 1).getValue() - entries.get(0).getValue();
    }

    /**
     * Sorts unsorted {@link Map} and returns min and max value.
     *
     * @param unsortedData {@link Map} to sort.
     * @return {@link Map} with 2 {@link java.util.Map.Entry}
     * "Minimum" as a key and min value from unsorted {@link Map} as a value;
     * "Maximum" as a key and max value from unsorted {@link Map} as a value.
     */
    public static Map<String, Long> findMinMax(Map<String, Long> unsortedData) {
        List<Map.Entry<String, Long>> entries = new LinkedList<>(unsortedData.entrySet());
        Collections.sort(entries, Comparator.comparing(Map.Entry::getValue));
        Map<String, Long> minMaxMap = new HashMap<>();
        minMaxMap.put("Minimum", entries.get(0).getValue());
        minMaxMap.put("Maximum", entries.get(entries.size() - 1).getValue());
        return minMaxMap;
    }

    /**
     * Filters data by extracting the required events from the list passed as a parameter.
     *
     * @param entities {@link List} with all events.
     * @param eventName name of event to filter by.
     * @return {@link List} of required {@link Entity}s.
     */
    private static List<Entity> filterData(List<Entity> entities, String eventName) {
        return entities.stream().filter(e -> e.getEventName().equals(eventName)).collect(Collectors.toList());
    }

    /**
     * Extracts id of the user from event description string using regex.
     *
     * @param description {@link String} with description of the event.
     * @return id of the user extract from description.
     */
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
