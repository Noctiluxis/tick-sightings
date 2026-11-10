package tick_sightings.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import tick_sightings.model.TickSighting;

@Service
public class TickSightingService {

    private final RestClient restClient;
    private TickSighting[] cachedSightings;
    private long lastFetchTime = 0;
    private static final long CACHE_DURATION = 5 * 60 * 1000;

    public TickSightingService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://dev-task.elancoapps.com/data")
                .build();
    }

    public synchronized TickSighting[] getAllSightings() {

        long currentTime = System.currentTimeMillis();
        if (cachedSightings != null &&
            currentTime - lastFetchTime < CACHE_DURATION) {

            return cachedSightings;
        }

    
        TickSighting[] rawSightings = restClient.get()
            .uri("/tick-sightings")
            .retrieve()
            .body(TickSighting[].class);

        if (rawSightings == null) {
            return new TickSighting[0];
        }

        Map<String, TickSighting> uniqueSightings =
            new LinkedHashMap<>();

        for (TickSighting sighting : rawSightings) {

            if (sighting == null) {
                continue;
            }
            String id = sighting.getId();
            if (id == null || id.isBlank()) {
                continue;
            }
        uniqueSightings.putIfAbsent(id, sighting);
        }

        cachedSightings = uniqueSightings.values()
            .toArray(new TickSighting[0]);

        lastFetchTime = currentTime;
        return cachedSightings;
    }

    public TickSighting getSightingById(String id) {
        TickSighting[] sightings = getAllSightings();
        for (TickSighting sighting : sightings) {

            if (sighting.getId().equals(id)) {
                return sighting;
            }
        }
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Sighting not found with ID: " + id
        );
    }

    public TickSighting[] getSightingsByCity(String city) {

        TickSighting[] sightings = getAllSightings();
        List<TickSighting> filtered = new ArrayList<>();

        for (TickSighting sighting : sightings) {
            if (sighting.getLocation() != null &&
                sighting.getLocation().equalsIgnoreCase(city)) {
                filtered.add(sighting);
            }
        }
        return filtered.toArray(new TickSighting[0]);
    }


    
    public TickSighting[] getSightingsBySpecies(String species) {
        TickSighting[] sightings = getAllSightings();
        List<TickSighting> filtered = new ArrayList<>();

        for (TickSighting sighting : sightings) {
            if (sighting.getSpecies() != null &&
                sighting.getSpecies().equalsIgnoreCase(species)) {
                filtered.add(sighting);
            }
        }
        return filtered.toArray(new TickSighting[0]);
    }

    public List<TickSighting> getSightingsByDateRange(
        LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException(
            "Start date cannot be after end date."
            );
        }
        TickSighting[] sightings = getAllSightings();
        List<TickSighting> filtered = new ArrayList<>();

        for (TickSighting sighting : sightings) {
            try {
                if (sighting.getDate() == null ||
                    sighting.getDate().isBlank()) {
                continue;
                }

            LocalDateTime sightingDate =
                    LocalDateTime.parse(sighting.getDate());

                if (!sightingDate.isBefore(from) &&
                    !sightingDate.isAfter(to)) {
                    filtered.add(sighting);
                }

            } catch (DateTimeParseException e) {
            
            }
        }

    return filtered;
    }

    public Map<String, Integer> getSightingsByLocation() {

        TickSighting[] sightings = getAllSightings();
        Map<String, Integer> counts = new TreeMap<>();

        for (TickSighting sighting : sightings) {
            String location = sighting.getLocation();
            if (location == null || location.isBlank()) {
                continue;
            }
            counts.put(
                location,
                counts.getOrDefault(location, 0) + 1
            );
        }
    return counts;
    }

    public Map<String, Integer> getMonthlyReport() {
        TickSighting[] sightings = getAllSightings();
        Map<String, Integer> monthlyCounts = new TreeMap<>();
        for (TickSighting sighting : sightings) {

            if (sighting.getDate() == null ||
                sighting.getDate().isBlank()) {
                continue;
            }

            try {
                LocalDateTime date =
                    LocalDateTime.parse(sighting.getDate());
                String month = YearMonth.from(date).toString();

                monthlyCounts.put(
                    month,
                    monthlyCounts.getOrDefault(month, 0) + 1
                );

            } 
            catch (DateTimeParseException e) {
            
            }
        }
        return monthlyCounts;
    }

    public Map<String, Integer> getWeeklyReport() {
        TickSighting[] sightings = getAllSightings();
        Map<String, Integer> weeklyCounts = new TreeMap<>();

        for (TickSighting sighting : sightings) {
            if (sighting.getDate() == null ||
                sighting.getDate().isBlank()) {
                continue;
            }

            try {
                LocalDateTime date =
                    LocalDateTime.parse(sighting.getDate());

                int week = date.get(
                    WeekFields.ISO.weekOfWeekBasedYear()
                );
                int year = date.get(
                    WeekFields.ISO.weekBasedYear()
                );

                String weekKey = String.format(
                    "%d-W%02d", year, week
                );

                weeklyCounts.put(
                    weekKey,
                    weeklyCounts.getOrDefault(weekKey, 0) + 1
                );

            } 
            catch (DateTimeParseException e) {
            
            }
        }
        return weeklyCounts;
    }
    
    public List<TickSighting> getSightingsByCityAndDateRange(
        String city, LocalDateTime from, LocalDateTime to) {

        if (from.isAfter(to)) {
            throw new IllegalArgumentException(
                "Start date cannot be after end date."
            );
        }
        TickSighting[] sightings = getAllSightings();
        List<TickSighting> filtered = new ArrayList<>();

        for (TickSighting sighting : sightings) {

            if (sighting.getLocation() == null ||
                sighting.getDate() == null ||
                sighting.getDate().isBlank()) {
                continue;
            }
            try {
                LocalDateTime sightingDate =
                    LocalDateTime.parse(sighting.getDate());

                boolean matchesCity =
                    sighting.getLocation().equalsIgnoreCase(city);
                boolean matchesDate =
                    !sightingDate.isBefore(from) &&
                    !sightingDate.isAfter(to);

                if (matchesCity && matchesDate) {
                    filtered.add(sighting);
                }
            } 
            catch (DateTimeParseException e) {
            }
        }
        return filtered;
    }

        public Map<String, Object> getPaginatedSightings(
        int page, int size) {
        if (page < 0 || size <= 0 || size > 100) {
            throw new IllegalArgumentException(
                "Page must be non-negative and size must be between 1 and 100."
            );
        }
        TickSighting[] sightings = getAllSightings();
        int totalRecords = sightings.length;
        long start = (long) page * size;
        int end = (int) Math.min(start + size, totalRecords);
        List<TickSighting> pageData = new ArrayList<>();
        if (start < totalRecords) {
            for (int i = (int) start; i < end; i++) {
                pageData.add(sightings[i]);
            }
        }
        int totalPages = (int) Math.ceil(
            (double) totalRecords / size
        );

        return Map.of(
            "page", page,
            "size", size,
            "totalRecords", totalRecords,
            "totalPages", totalPages,
            "data", pageData
        );
    }
}