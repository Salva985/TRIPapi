package com.tripapi.config;

import com.tripapi.enums.ActivityType;
import com.tripapi.enums.BudgetCategory;
import com.tripapi.enums.CurrencyCode;
import com.tripapi.enums.TripType;
import com.tripapi.model.*;
import com.tripapi.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@Profile("!test") // don’t run during tests
public class DataInitializer implements CommandLineRunner {

    private final DestinationRepository destinationRepository;
    private final TripRepository tripRepository;
    private final ItineraryDayRepository itineraryDayRepository;
    private final BudgetRepository budgetRepository;
    private final ActivityRepository activityRepository;

    public DataInitializer(
            DestinationRepository destinationRepository,
            TripRepository tripRepository,
            ItineraryDayRepository itineraryDayRepository,
            BudgetRepository budgetRepository,
            ActivityRepository activityRepository
    ) {
        this.destinationRepository = destinationRepository;
        this.tripRepository = tripRepository;
        this.itineraryDayRepository = itineraryDayRepository;
        this.budgetRepository = budgetRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Avoid reseeding if already present
        if (tripRepository.count() > 0 || destinationRepository.count() > 0) {
            return;
        }

        // ---- Destinations (ENUM CURRENCY) ----
        Destination barcelona = Destination.builder()
                .city("Barcelona")
                .country("Spain")
                .timezone("Europe/Madrid")
                .currencyCode(CurrencyCode.EUR)
                .build();

        Destination rome = Destination.builder()
                .city("Rome")
                .country("Italy")
                .timezone("Europe/Rome")
                .currencyCode(CurrencyCode.EUR)
                .build();

        destinationRepository.save(barcelona);
        destinationRepository.save(rome);


        // ---- Trip (ENUM TRIP TYPE) ----
        Trip summer = Trip.builder()
                .name("Summer Escape")
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 7, 10))
                .destination(barcelona)
                .tripType(TripType.LEISURE)       // <— enum
                .notes("Family trip")
                .build();

        Trip workTrip = Trip.builder()
                .name("Conference Rome")
                .startDate(LocalDate.of(2025, 9, 5))
                .endDate(LocalDate.of(2025, 9, 8))
                .destination(rome)
                .tripType(TripType.BUSINESS)
                .notes("Tech conference")
                .build();

        tripRepository.save(summer);
        tripRepository.save(workTrip);


        // ---- Itinerary Days ----
        ItineraryDay d1 = ItineraryDay.builder()
                .trip(summer)
                .date(LocalDate.of(2025, 7, 2))
                .dayNumber(2)
                .title("Old Town Walk")
                .notes("Gothic Quarter + tapas")
                .build();

        ItineraryDay d2 = ItineraryDay.builder()
                .trip(summer)
                .date(LocalDate.of(2025, 7, 3))
                .dayNumber(3)
                .title("Beach Day")
                .notes("Barceloneta in the morning")
                .build();

        itineraryDayRepository.save(d1);
        itineraryDayRepository.save(d2);

        // ---- Budgets (ENUM CATEGORY & CURRENCY) ----
        Budget flights = Budget.builder()
                .trip(summer)
                .category(BudgetCategory.TRANSPORT)
                .plannedAmount(new BigDecimal("800.00"))
                .spentAmount(new BigDecimal("760.45"))
                .currencyCode(CurrencyCode.EUR)
                .notes("Round trip flights")
                .build();

        Budget food = Budget.builder()
                .trip(summer)
                .category(BudgetCategory.FOOD)
                .plannedAmount(new BigDecimal("350.00"))
                .spentAmount(new BigDecimal("90.00"))
                .currencyCode(CurrencyCode.EUR)
                .notes("Tapas + paella budget")
                .build();

        budgetRepository.save(flights);
        budgetRepository.save(food);

        // ---- Activities (build CONCRETE subclasses; base Activity is abstract) ----
        ActivitySightseeing sagrada = ActivitySightseeing.builder()
                .trip(summer)
                .title("Sagrada Família Tour")
                .date(LocalDate.of(2025, 7, 4))
                .notes("Pre-book tickets")
                .landmarkName("Sagrada Família")
                .location("Barcelona")
                .build();
        sagrada.setType(ActivityType.SIGHTSEEING);

        ActivityAdventure kayak = ActivityAdventure.builder()
                .trip(summer)
                .title("Kayak in Costa Brava")
                .date(LocalDate.of(2025, 7, 6))
                .notes("Bring sunscreen")
                .difficultyLevel("LOW")
                .equipmentRequired("Life vest")
                .build();
        kayak.setType(ActivityType.ADVENTURE);

        ActivityCultural opera = ActivityCultural.builder()
                .trip(summer)
                .date(LocalDate.of(2025, 7, 7))
                .title("Opera Night")
                .notes("Formal dress code recommended")
                .type(ActivityType.CULTURAL)
                .eventName("La Traviata")
                .organizer("Palau Sant Jordi")
                .build();


        activityRepository.save(sagrada);
        activityRepository.save(kayak);
        activityRepository.save(opera);
    }
}