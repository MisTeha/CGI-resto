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

// järgnev klass on tehtud keelemudeli poolt.
// TODO: broneeringud suvaliselt

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

        // Create sample reservations
        LocalDateTime now = LocalDateTime.now();

        Reservation res1 = new Reservation(
                now.plusHours(1),
                now.plusHours(2),
                table1,
                2,
                "John Doe",
                "+372 123 4567");

        Reservation res2 = new Reservation(
                now.plusHours(3),
                now.plusHours(5),
                table2,
                4,
                "Jane Smith",
                "+372 987 6543");

        Reservation res3 = new Reservation(
                now.plusDays(1).withHour(19).withMinute(0),
                now.plusDays(1).withHour(21).withMinute(0),
                table6,
                8,
                "Corporate Event",
                "+372 555 1234");

        reservationRepository.save(res1);
        reservationRepository.save(res2);
        reservationRepository.save(res3);
    }
}
