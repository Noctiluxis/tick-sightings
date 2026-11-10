package tick_sightings.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import tick_sightings.model.TickSighting;
import tick_sightings.service.TickSightingService;

@RestController
public class TickSightingController {

    private final TickSightingService service;

    public TickSightingController(TickSightingService service) {
        this.service = service;
    }

    @GetMapping("/api/sightings")
    public TickSighting[] getAllSightings() {
        return service.getAllSightings();
    }

    
    @GetMapping("/api/sightings/{id}")
    public TickSighting getSightingById(@PathVariable String id) {
        return service.getSightingById(id);
    }


    @GetMapping("/api/sightings/city/{city}")
    public TickSighting[] getSightingsByCity(
        @PathVariable String city) {
        return service.getSightingsByCity(city);
    }

    @GetMapping("/api/sightings/species/{species}")
    public TickSighting[] getSightingsBySpecies(
        @PathVariable String species) {
        return service.getSightingsBySpecies(species);
    }

    @GetMapping("/api/sightings/date-range")
    public List<TickSighting> getSightingsByDateRange(
        @RequestParam LocalDateTime from,
        @RequestParam LocalDateTime to) {
        return service.getSightingsByDateRange(from, to);
    }

    @GetMapping("/api/reports/regional")
    public Map<String, Integer> getRegionalReport() {
        return service.getSightingsByLocation();
    }

    @GetMapping("/api/reports/monthly")
    public Map<String, Integer> getMonthlyReport() {
        return service.getMonthlyReport();
    }

    @GetMapping("/api/reports/weekly")
    public Map<String, Integer> getWeeklyReport() {
        return service.getWeeklyReport();
    }
    
    @GetMapping("/api/sightings/filter")
    public List<TickSighting> getSightingsByCityAndDateRange(
        @RequestParam String city,
        @RequestParam LocalDateTime from,
        @RequestParam LocalDateTime to) {

        return service.getSightingsByCityAndDateRange(
            city, from, to
        );
    }

    
    @GetMapping("/api/sightings/page")
    public Map<String, Object> getPaginatedSightings(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {      
        return service.getPaginatedSightings(page, size);
    }
}

/*    ---- For Testing ----

http://localhost:8080/api/sightings || All sightings
http://localhost:8080/api/sightings/Kq3x9ZfV2mTn8aL0pYwB || Retrieve sight by ID
http://localhost:8080/api/sightings/city/London || Fiter by city
http://localhost:8080/api/sightings/date-range?from=2022-08-01T00:00:00&to=2022-08-31T23:59:59 || Filter by date range
http://localhost:8080/api/sightings/filter?city=London&from=2022-08-01T00:00:00&to=2022-08-31T23:59:59 || Date + City filtering
http://localhost:8080/api/sightings/page?page=0&size=20 || Pagination

http://localhost:8080/api/reports/regional || Regional report
http://localhost:8080/api/reports/monthly || Monthly report
http://localhost:8080/api/reports/weekly || Weekly report

http://localhost:8080/api/sightings/invalid-id-123456 || Error test: ID
http://localhost:8080/api/sightings/page?page=-1&size=20 || Error test: Page
http://localhost:8080/api/sightings/date-range?from=2022-08-31T00:00:00&to=2022-08-01T23:59:59 || Error test: Date

*/