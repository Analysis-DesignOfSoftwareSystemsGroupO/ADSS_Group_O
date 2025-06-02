package TransportModule;

import TransportModule.DTO.TransportDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TransportTestUtils {

    private static final List<LocalDate> dates = Arrays.asList(LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 2), LocalDate.of(2025, 6, 3), LocalDate.of(2025, 10, 21), LocalDate.of(2025, 8, 18), LocalDate.of(2025, 7, 13), LocalDate.of(2025, 7, 17), LocalDate.of(2025, 7, 17), LocalDate.of(2025, 12, 18), LocalDate.of(2025, 6, 21), LocalDate.of(2025, 8, 21), LocalDate.of(2025, 12, 16), LocalDate.of(2025, 8, 24));

    private static final List<LocalTime> times = Arrays.asList(LocalTime.of(8, 0), LocalTime.of(10, 30), LocalTime.of(14, 45), LocalTime.of(18, 57), LocalTime.of(16, 9), LocalTime.of(23, 54), LocalTime.of(4, 54), LocalTime.of(22, 12), LocalTime.of(13, 36), LocalTime.of(6, 47), LocalTime.of(15, 37), LocalTime.of(22, 45), LocalTime.of(3, 32));

    private static final List<String> siteNames =  Arrays.asList("Tel Aviv", "Haifa", "Jerusalem","Eilat", "Ramat Gan", "Beer Sheba");

    private static final List<String> driverIDs = Arrays.asList("111", "222", "333","444","555","666", "777", "888", "999", "101010", "111111", "121212", "131313", "141414");

    private static final List<String> truckPNs = Arrays.asList("TRUCK111", "TRUCK222", "TRUCK333","TRUCK444", "TRUCK555", "TRUCK666", "TRUCK777", "TRUCK888", "TRUCK999", "TRUCK101010", "TRUCK111111", "TRUCK121212", "TRUCK131313", "TRUCK141414");

    private static final Random random = new Random();

    private static <T> T randomFrom(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    public static TransportDTO createDTO(int id) {
        return new TransportDTO(
                id,
                randomFrom(dates),
                false,
                2000,
                randomFrom(driverIDs),
                randomFrom(truckPNs),
                randomFrom(siteNames),
                randomFrom(times)
        );
    }

    public static TransportDTO createDTOWithoutTruck(int id) {
        return new TransportDTO(
                id,
                randomFrom(dates),
                false,
                2000,
                randomFrom(driverIDs),
                "-1",
                randomFrom(siteNames),
                randomFrom(times)
        );
    }

    public static TransportDTO createDTOWithoutDriver(int id) {
        return new TransportDTO(
                id,
                randomFrom(dates),
                false,
                2000,
                "-1",
                randomFrom(truckPNs),
                randomFrom(siteNames),
                randomFrom(times)
        );
    }
}
