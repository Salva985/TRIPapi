package com.tripapi.config;

import com.tripapi.enums.ActivityType;
import com.tripapi.enums.BudgetCategory;
import com.tripapi.enums.CurrencyCode;
import com.tripapi.enums.TripType;
import com.tripapi.model.*;
import com.tripapi.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@Profile("!test") // don’t run during tests
public class DataInitializer implements CommandLineRunner {

    private final DestinationRepository destinationRepository;
    private final TripRepository tripRepository;
    private final ItineraryDayRepository itineraryDayRepository;
    private final BudgetRepository budgetRepository;
    private final ActivityRepository activityRepository;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            DestinationRepository destinationRepository,
            TripRepository tripRepository,
            ItineraryDayRepository itineraryDayRepository,
            BudgetRepository budgetRepository,
            ActivityRepository activityRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.destinationRepository = destinationRepository;
        this.tripRepository = tripRepository;
        this.itineraryDayRepository = itineraryDayRepository;
        this.budgetRepository = budgetRepository;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        // ---------- USER Demo  ----------
        if (userRepository.count() == 0) {
            User demo = User.builder()
                    .username("Sal")
                    .fullName("Salvatore Marchese")
                    .email("salva@example.com")
                    .passwordHash(passwordEncoder.encode("secret123"))
                    .dob(LocalDate.of(1995, 3, 20))
                    .build();
            userRepository.save(demo);
        }

        if (tripRepository.count() > 0 || destinationRepository.count() > 0) return;

        // ---------------- Destinations ----------------
        Destination barcelona = Destination.builder()
                .city("Barcelona").country("Spain")
                .timezone("Europe/Madrid").currencyCode(CurrencyCode.EUR).build();

        Destination rome = Destination.builder()
                .city("Rome").country("Italy")
                .timezone("Europe/Rome").currencyCode(CurrencyCode.EUR).build();

        Destination london = Destination.builder()
                .city("London").country("United Kingdom")
                .timezone("Europe/London").currencyCode(CurrencyCode.GBP).build();

        Destination newYork = Destination.builder()
                .city("New York").country("USA")
                .timezone("America/New_York").currencyCode(CurrencyCode.USD).build();

        destinationRepository.saveAll(List.of(barcelona, rome, london, newYork));

        // ---------------- Trips (covering all TripType enums) ----------------
        Trip leisureTrip = Trip.builder()
                .name("Barcelona Summer Escape")
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 7, 10))
                .destination(barcelona)
                .tripType(TripType.LEISURE)
                .notes("Family leisure trip in Spain")
                .build();

        Trip businessTrip = Trip.builder()
                .name("Conference Rome")
                .startDate(LocalDate.of(2025, 9, 5))
                .endDate(LocalDate.of(2025, 9, 8))
                .destination(rome)
                .tripType(TripType.BUSINESS)
                .notes("Tech business trip")
                .build();

        Trip adventureTrip = Trip.builder()
                .name("London Adventure Week")
                .startDate(LocalDate.of(2025, 3, 10))
                .endDate(LocalDate.of(2025, 3, 17))
                .destination(london)
                .tripType(TripType.ADVENTURE)
                .notes("Sports and exploration")
                .build();

        Trip otherTrip = Trip.builder()
                .name("NY Miscellaneous Trip")
                .startDate(LocalDate.of(2025, 11, 20))
                .endDate(LocalDate.of(2025, 11, 25))
                .destination(newYork)
                .tripType(TripType.OTHER)
                .notes("Catch-all activities")
                .build();

        tripRepository.saveAll(List.of(leisureTrip, businessTrip, adventureTrip, otherTrip));

        // ---------------- Budgets ----------------
        Budget flights = Budget.builder()
                .trip(leisureTrip)
                .category(BudgetCategory.TRANSPORT)
                .plannedAmount(new BigDecimal("800.00"))
                .spentAmount(new BigDecimal("760.45"))
                .currencyCode(CurrencyCode.EUR)
                .notes("Round trip flights")
                .build();

        Budget food = Budget.builder()
                .trip(leisureTrip)
                .category(BudgetCategory.FOOD)
                .plannedAmount(new BigDecimal("350.00"))
                .spentAmount(new BigDecimal("90.00"))
                .currencyCode(CurrencyCode.EUR)
                .notes("Tapas budget")
                .build();

        budgetRepository.saveAll(List.of(flights, food));

        // ---------------- Activities (covering all ActivityType enums) ----------------

        // LEISURE trip (Barcelona) → SIGHTSEEING + CULTURAL
        ActivitySightseeing sagrada = ActivitySightseeing.builder()
                .trip(leisureTrip).title("Sagrada Família Tour")
                .date(LocalDate.of(2025, 7, 4)).notes("Pre-book tickets")
                .landmarkName("Sagrada Família").location("Barcelona").build();
        sagrada.setType(ActivityType.SIGHTSEEING);

        ActivityCultural opera = ActivityCultural.builder()
                .trip(leisureTrip).date(LocalDate.of(2025, 7, 7))
                .title("Opera Night").notes("Formal dress code")
                .eventName("La Traviata").organizer("Gran Teatre del Liceu").build();
        opera.setType(ActivityType.CULTURAL);

        // BUSINESS trip (Rome) → CULTURAL + OTHER
        ActivityCultural keynote = ActivityCultural.builder()
                .trip(businessTrip).title("Conference Keynote")
                .date(LocalDate.of(2025, 9, 6)).eventName("TechConf 2025")
                .organizer("TechConf Org").notes("Arrive early").build();
        keynote.setType(ActivityType.CULTURAL);

        Activity otherBusiness = new Activity();
        otherBusiness.setTrip(businessTrip);
        otherBusiness.setTitle("Networking Drinks");
        otherBusiness.setDate(LocalDate.of(2025, 9, 7));
        otherBusiness.setNotes("Evening casual event");
        otherBusiness.setType(ActivityType.OTHER);

        // ADVENTURE trip (London) → ADVENTURE + SIGHTSEEING
        ActivityAdventure climbing = ActivityAdventure.builder()
                .trip(adventureTrip).title("Climbing Gym Challenge")
                .date(LocalDate.of(2025, 3, 12)).notes("Indoor climbing")
                .difficultyLevel("MEDIUM").equipmentRequired("Shoes, chalk").build();
        climbing.setType(ActivityType.ADVENTURE);

        ActivitySightseeing tower = ActivitySightseeing.builder()
                .trip(adventureTrip).title("Tower of London Visit")
                .date(LocalDate.of(2025, 3, 14))
                .landmarkName("Tower of London").location("London").build();
        tower.setType(ActivityType.SIGHTSEEING);

        // OTHER trip (NY) → OTHER + ADVENTURE
        Activity otherNy = new Activity();
        otherNy.setTrip(otherTrip);
        otherNy.setTitle("Central Park Picnic");
        otherNy.setDate(LocalDate.of(2025, 11, 21));
        otherNy.setNotes("Weather permitting");
        otherNy.setType(ActivityType.OTHER);

        ActivityAdventure surf = ActivityAdventure.builder()
                .trip(otherTrip).title("Hudson River Kayak")
                .date(LocalDate.of(2025, 11, 23))
                .difficultyLevel("BEGINNER").equipmentRequired("Life vest").build();
        surf.setType(ActivityType.ADVENTURE);

        activityRepository.saveAll(List.of(sagrada, opera, keynote, otherBusiness, climbing, tower, otherNy, surf));
    }
}