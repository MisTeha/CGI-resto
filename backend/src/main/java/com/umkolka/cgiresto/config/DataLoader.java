package com.umkolka.cgiresto.config;

import com.umkolka.cgiresto.entity.Reservation;
import com.umkolka.cgiresto.entity.RestoTable;
import com.umkolka.cgiresto.entity.Zone;
import com.umkolka.cgiresto.repository.ReservationRepository;
import com.umkolka.cgiresto.repository.RestoTableRepository;
import com.umkolka.cgiresto.repository.ZoneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

// järgnev klass on tehtud keelemudeli poolt,
// v.a. random meetodid.

@Component
public class DataLoader implements CommandLineRunner {

    private final ZoneRepository zoneRepository;
    private final RestoTableRepository restoTableRepository;
    private final ReservationRepository reservationRepository;

    public DataLoader(ZoneRepository zoneRepository, RestoTableRepository restoTableRepository,
            ReservationRepository reservationRepository) {
        this.zoneRepository = zoneRepository;
        this.restoTableRepository = restoTableRepository;
        this.reservationRepository = reservationRepository;
    }
    // JÄRGNEVAD MEETODID ON MINU ENDA TEHTUD
    private void reserveTableRandom(RestoTable table) {
        LocalDateTime now = LocalDateTime.now();
        Zone zone = table.getZone();

        for (int i = 0; i < 7; i++) {
            LocalDateTime day = now.plusDays(i);
            LocalDateTime openFrom = day.with(zone.getOpeningTime());
            LocalDateTime openUntil = day.with(zone.getClosingTime());
            LocalDateTime start = i == 0 && now.isAfter(openFrom) ? now : openFrom;

            if (!start.isBefore(openUntil)) {
                continue;
            }

            reserveTableRandom(table, start, openUntil, 5);
        }
        
    }   

    private void reserveTableRandom(RestoTable table, LocalDateTime openFrom, LocalDateTime openUntil, int atMost) {
        LocalDateTime last = LocalDateTime.from(openFrom);
        for (int i = 0; i < atMost; i++) {
            int delay = ((int) (Math.random() * 20)) * 15;
            int duration = 30 + ((int) (Math.random() * 3)) * 30;

            LocalDateTime start = last.plusMinutes(delay);
            LocalDateTime end = start.plusMinutes(duration);
            if (end.isAfter(openUntil)) {
                break;
            }
            
            last = end;

            int nPeople = 1 + (int) (Math.random() * table.getNSeats());
            Reservation res = new Reservation(start, end, table, nPeople, "Test User", "555-1234");
            reservationRepository.save(res);
        }
    }

    

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (zoneRepository.count() > 0) {
            return;
        }

        // Create zones
        Zone mainZone = new Zone("Main Hall", "Central dining area", LocalTime.of(11, 0), LocalTime.of(22, 0));
        Zone outdoorZone = new Zone("Outdoor Patio", "Garden seating", LocalTime.of(12, 0), LocalTime.of(21, 0));
        Zone vipZone = new Zone("VIP Lounge", "Private dining room", LocalTime.of(17, 0), LocalTime.of(23, 0));

        zoneRepository.save(mainZone);
        zoneRepository.save(outdoorZone);
        zoneRepository.save(vipZone);

        // Create tables for main zone
        RestoTable table1 = new RestoTable(mainZone, 2, false, "1", 60, 1, 1);
        RestoTable table2 = new RestoTable(mainZone, 4, true, "2", 55, 3, 1);
        RestoTable table3 = new RestoTable(mainZone, 6, false, "3", 50, 2, 3);

        // Create tables for outdoor zone
        RestoTable table4 = new RestoTable(outdoorZone, 4, true, "4", 40, 1, 1);
        RestoTable table5 = new RestoTable(outdoorZone, 8, true, "5", 45, 4, 2);

        // Create tables for VIP zone
        RestoTable table6 = new RestoTable(vipZone, 10, false, "6", 95, 2, 2);

        restoTableRepository.save(table1);
        restoTableRepository.save(table2);
        restoTableRepository.save(table3);
        restoTableRepository.save(table4);
        restoTableRepository.save(table5);
        restoTableRepository.save(table6);

        // JÄRGNEV OSA ON MINU ENDA TEHTUD
        List<RestoTable> tables = List.of(table1, table2, table3, table4, table5, table6);
        tables.forEach(this::reserveTableRandom);

    }
}
