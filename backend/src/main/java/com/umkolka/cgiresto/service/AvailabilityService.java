package com.umkolka.cgiresto.service;

import com.umkolka.cgiresto.entity.RestoTable;
import com.umkolka.cgiresto.entity.Zone;
import com.umkolka.cgiresto.repository.RestoTableRepository;
import com.umkolka.cgiresto.repository.ZoneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class AvailabilityService {

    public record RecommendationView(
            Long tableId,
            String tableNumber,
            Long zoneId,
            String zoneName,
            Integer nSeats,
            Boolean nextToWindow,
            Integer privacyScore,
            Integer gridX,
            Integer gridY) {
    }

    public record RecommendationBuckets(
            RecommendationView mostRecommended,
            List<RecommendationView> otherAvailable,
            List<RecommendationView> enoughSeatsAnotherTime,
            List<RecommendationView> tooFewSeats) {
    }

    private final RestoTableRepository tableRepository;
    private final ZoneRepository zoneRepository;
        private final ZoneHoursPolicy zoneHoursPolicy;

        public AvailabilityService(
                        RestoTableRepository tableRepository,
                        ZoneRepository zoneRepository,
                        ZoneHoursPolicy zoneHoursPolicy) {
        this.tableRepository = tableRepository;
        this.zoneRepository = zoneRepository;
                this.zoneHoursPolicy = zoneHoursPolicy;
    }

    public RecommendationBuckets recommend(
            LocalDateTime startTime,
            Integer durationMinutes,
            Integer partySize,
            Long zoneId,
            Boolean windowPreferred,
            Boolean privacyPreferred) {

        LocalDateTime endTime = startTime.plusMinutes(durationMinutes);
        Zone zone = validateRangeAndZone(startTime, endTime, zoneId);
        zoneHoursPolicy.validateBookingWindow(zone, startTime, endTime);

        List<RecommendationView> availableEnoughSeats = toRecommendationViews(
                tableRepository.findAvailableTablesByZoneAndMinSeats(startTime, endTime, zoneId, partySize));

        RecommendationView mostRecommended = availableEnoughSeats.stream()
                .max(Comparator.comparingInt(view -> scoreFor(view, partySize, windowPreferred, privacyPreferred)))
                .orElse(null);

        List<RecommendationView> otherAvailable = availableEnoughSeats.stream()
                .filter(view -> mostRecommended == null || !Objects.equals(view.tableId(), mostRecommended.tableId()))
                .toList();

        List<RecommendationView> enoughSeatsAnotherTime = toRecommendationViews(
                tableRepository.findUnavailableTablesByZoneAndMinSeats(startTime, endTime, zoneId, partySize));

        List<RecommendationView> tooFewSeats = toRecommendationViews(
                tableRepository.findTablesByZoneAndMaxSeatsExclusive(zoneId, partySize));

        return new RecommendationBuckets(
                mostRecommended,
                otherAvailable,
                enoughSeatsAnotherTime,
                tooFewSeats);
    }

        private Zone validateRangeAndZone(LocalDateTime startTime, LocalDateTime endTime, Long zoneId) {
        if (!endTime.isAfter(startTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Broneeringu lõpp peab olema peale algust");
        }

                return zoneRepository.findById(zoneId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Tsooni ID-ga " + zoneId + " ei leitud"));
    }

    private List<RecommendationView> toRecommendationViews(List<RestoTable> tables) {
        return tables.stream()
                .map(table -> new RecommendationView(
                        table.getId(),
                        table.getTableNumber(),
                        table.getZone().getId(),
                        table.getZone().getName(),
                        table.getNSeats(),
                        table.getIsNextToWindow(),
                        table.getPrivacyScore(),
                        table.getGridX(),
                        table.getGridY()))
                .toList();
    }

    // see meetod on keelemudeli poolt
    private int scoreFor(
            RecommendationView view,
            Integer partySize,
            Boolean windowPreferred,
            Boolean privacyPreferred) {
        // TODO: lõpus võiks sorteerida istekohtade järgi, eelistada efektiivsus
        int score = Math.max(0, 100 - (view.nSeats() - partySize) * 15);

        if (Boolean.TRUE.equals(windowPreferred) && Boolean.TRUE.equals(view.nextToWindow())) {
            score += 20;
        }

        if (Boolean.TRUE.equals(privacyPreferred)) {
            int privacyBonus = view.privacyScore() == null ? 0 : view.privacyScore() / 4;
            score += privacyBonus;
        }

        return score;
    }
}
