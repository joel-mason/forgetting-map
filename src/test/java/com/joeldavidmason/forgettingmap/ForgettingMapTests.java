package com.joeldavidmason.forgettingmap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ForgettingMap Tests")
class ForgettingMapTests {

    @Test
    void createTwoForgettingMaps_bothMapsHaveSameEntrySetAndMaxCapacity_returnsTrue() {
        var forgettingMap1 = new ForgettingMap<Integer, UUID>(5);
        var forgettingMap2 = new ForgettingMap<Integer, UUID>(5);
        for(var i = 0; i < 5; i++) {
            var cacheWrapper = new CacheWrapper<>(UUID.randomUUID());
            forgettingMap1.put(i, cacheWrapper);
            forgettingMap2.put(i, cacheWrapper);
        }
        assertThat(forgettingMap1)
                .isEqualTo(forgettingMap2)
                .hasSameHashCodeAs(forgettingMap2);
    }

    @Test
    void createTwoForgettingMaps_bothMapsHaveSameEntrySetAndDifferentMaxCapacity_returnsFalse() {
        var forgettingMap1 = new ForgettingMap<Integer, UUID>(5);
        var forgettingMap2 = new ForgettingMap<Integer, UUID>(6);
        for(var i = 0; i < 5; i++) {
            var cacheWrapper = new CacheWrapper<>(UUID.randomUUID());
            forgettingMap1.put(i, cacheWrapper);
            forgettingMap2.put(i, cacheWrapper);
        }
        assertThat(forgettingMap1)
                .isNotEqualTo(forgettingMap2)
                .doesNotHaveSameHashCodeAs(forgettingMap2);
    }

    @Test
    void createTwoForgettingMaps_bothMapsHaveDifferentEntrySetAndSameMaxCapacity_returnsFalse() {
        var forgettingMap1 = new ForgettingMap<Integer, UUID>(5);
        var forgettingMap2 = new ForgettingMap<Integer, UUID>(5);
        for(var i = 0; i < 5; i++) {
            forgettingMap1.put(i, new CacheWrapper<>(UUID.randomUUID()));
            forgettingMap2.put(i, new CacheWrapper<>(UUID.randomUUID()));
        }
        assertThat(forgettingMap1)
                .isNotEqualTo(forgettingMap2)
                .doesNotHaveSameHashCodeAs(forgettingMap2);
    }

    @Test
    void createTwoForgettingMaps_bothMapsHaveDifferentEntrySetAndDifferentMaxCapacity_returnsFalse() {
        var forgettingMap1 = new ForgettingMap<Integer, UUID>(5);
        var forgettingMap2 = new ForgettingMap<Integer, UUID>(6);
        for(var i = 0; i < 5; i++) {
            forgettingMap1.put(i, new CacheWrapper<>(UUID.randomUUID()));
            forgettingMap2.put(i, new CacheWrapper<>(UUID.randomUUID()));
        }
        assertThat(forgettingMap1)
                .isNotEqualTo(forgettingMap2)
                .doesNotHaveSameHashCodeAs(forgettingMap2);
    }

    @Test
    void createForgettingMapWith5Values_retrieveSomeValues_countsShouldIncrementCorrectly() {
        var forgettingMap = new ForgettingMap<Integer, UUID>(5);
        var mapToCheck = new HashMap<Integer, UUID>();
        for(var i = 0; i < 5; i++) {
            var uuid = UUID.randomUUID();
            forgettingMap.put(i, new CacheWrapper<>(uuid));
            mapToCheck.put(i, uuid);
        }

        assertThat(forgettingMap).hasSize(5);
        assertThat(forgettingMap.keySet()).hasToString("[0, 1, 2, 3, 4]");

        var cacheWrapper = forgettingMap.get(0);
        assertThat(forgettingMap.keySet()).hasToString("[1, 2, 3, 4, 0]");
        assertThat(cacheWrapper.getCount()).isOne();
        assertThat(cacheWrapper.getContent()).isEqualTo(mapToCheck.get(0));

        var cacheWrapper2 = forgettingMap.get(1);
        assertThat(forgettingMap.keySet()).hasToString("[2, 3, 4, 0, 1]");
        assertThat(cacheWrapper2.getCount()).isOne();
        assertThat(cacheWrapper2.getContent()).isEqualTo(mapToCheck.get(1));

        var cacheWrapper3 = forgettingMap.get(0);
        assertThat(forgettingMap.keySet()).hasToString("[2, 3, 4, 1, 0]");
        assertThat(cacheWrapper3.getCount()).isEqualTo(2);
        assertThat(cacheWrapper3.getContent()).isEqualTo(mapToCheck.get(0));
    }

    @Test
    void addFiveValues_retrieveAllValues_OrderedAsTheyWereEntered() {
        var forgettingMap = new ForgettingMap<Integer, UUID>(5);
        for(var i = 0; i < 5; i++) {
            forgettingMap.put(i, new CacheWrapper<>(UUID.randomUUID()));
        }
        assertThat(forgettingMap).hasSize(5);
        assertThat(forgettingMap.keySet().toString()).hasToString("[0, 1, 2, 3, 4]");
    }

    @Test
    void addFiveValues_retrieveValueThatDoesNotExist_OrderStaysTheSame() {
        var forgettingMap = new ForgettingMap<Integer, UUID>(5);
        for(var i = 0; i < 5; i++) {
            forgettingMap.put(i, new CacheWrapper<>(UUID.randomUUID()));
        }
        forgettingMap.get(10);
        assertThat(forgettingMap.keySet().toString()).hasToString("[0, 1, 2, 3, 4]");
        assertThat(forgettingMap.get(10)).isNull();
    }

    @Test
    void addFiveValues_retrieveThirdValue_thirdValueIsLast() {
        var forgettingMap = new ForgettingMap<Integer, UUID>(5);
        var mapToCheck = new HashMap<Integer, UUID>();
        for(var i = 0; i < 5; i++) {
            var uuid = UUID.randomUUID();
            forgettingMap.put(i, new CacheWrapper<>(uuid));
            mapToCheck.put(i, uuid);
        }

        //retrieve third value
        retrieveAndAssert(forgettingMap, 2, "[0, 1, 3, 4, 2]", mapToCheck.get(2), 1);
    }


    @Test
    void addFiveValues_retrieveThirdFourthAndFifthValue_fifthValueIsLast() {
        var forgettingMap = new ForgettingMap<Integer, UUID>(5);
        var mapToCheck = new HashMap<Integer, UUID>();
        for(var i = 0; i < 5; i++) {
            var uuid = UUID.randomUUID();
            forgettingMap.put(i, new CacheWrapper<>(uuid));
            mapToCheck.put(i, uuid);
        }

        //retrieve third value
        retrieveAndAssert(forgettingMap, 2, "[0, 1, 3, 4, 2]", mapToCheck.get(2), 1);

        //retrieve fourth value
        retrieveAndAssert(forgettingMap, 3, "[0, 1, 4, 2, 3]", mapToCheck.get(3), 1);

        //retrieve fifth value
        retrieveAndAssert(forgettingMap, 4, "[0, 1, 2, 3, 4]", mapToCheck.get(4), 1);
    }

    @Test
    void addFiveValues_retrieveThirdFourthAndFifthValueAndAddValue_fifthValueIsLastAndZeroIsRemoved() {
        var forgettingMap = new ForgettingMap<Integer, UUID>(5);
        var mapToCheck = new HashMap<Integer, UUID>();
        for(var i = 0; i < 5; i++) {
            var uuid = UUID.randomUUID();
            forgettingMap.put(i, new CacheWrapper<>(uuid));
            mapToCheck.put(i, uuid);
        }

        //retrieve third value
        retrieveAndAssert(forgettingMap, 2, "[0, 1, 3, 4, 2]", mapToCheck.get(2), 1);

        //retrieve fourth value
        retrieveAndAssert(forgettingMap, 3, "[0, 1, 4, 2, 3]", mapToCheck.get(3), 1);

        //retrieve fifth value
        retrieveAndAssert(forgettingMap, 4, "[0, 1, 2, 3, 4]", mapToCheck.get(4), 1);

        //add sixth value
        addAndAssert(forgettingMap, 5, UUID.randomUUID(), "[1, 5, 2, 3, 4]");
    }

    @Test
    void addFiveValues_addFiveMoreValues_initialValuesAreRemoved() {
        var forgettingMap = new ForgettingMap<Integer, UUID>(5);
        var mapToCheck = new HashMap<Integer, UUID>();
        for(var i = 0; i < 5; i++) {
            var uuid = UUID.randomUUID();
            forgettingMap.put(i, new CacheWrapper<>(uuid));
            mapToCheck.put(i, uuid);
        }

        //add sixth value
        addAndAssert(forgettingMap, 5, UUID.randomUUID(), "[1, 2, 3, 4, 5]");

        //add seventh value
        addAndAssert(forgettingMap, 6, UUID.randomUUID(), "[2, 3, 4, 5, 6]");

        //add eighth value
        addAndAssert(forgettingMap, 7, UUID.randomUUID(), "[3, 4, 5, 6, 7]");

        //add ninth value
        addAndAssert(forgettingMap, 8, UUID.randomUUID(), "[4, 5, 6, 7, 8]");

        //add tenth value
        addAndAssert(forgettingMap, 9, UUID.randomUUID(), "[5, 6, 7, 8, 9]");
    }

    @Test
    void addFiveValues_retrieveFirstKeyFourTimesAndSecondOnce_firstValueIsLast() {
        var forgettingMap = new ForgettingMap<Integer, UUID>(5);
        var mapToCheck = new HashMap<Integer, UUID>();
        for(var i = 0; i < 5; i++) {
            var uuid = UUID.randomUUID();
            forgettingMap.put(i, new CacheWrapper<>(uuid));
            mapToCheck.put(i, uuid);
        }

        //retrieve 4 times
        retrieveAndAssert(forgettingMap, 0, "[1, 2, 3, 4, 0]", mapToCheck.get(0), 1);
        retrieveAndAssert(forgettingMap, 0, "[1, 2, 3, 4, 0]", mapToCheck.get(0), 2);
        retrieveAndAssert(forgettingMap, 0, "[1, 2, 3, 4, 0]", mapToCheck.get(0), 3);
        retrieveAndAssert(forgettingMap, 0, "[1, 2, 3, 4, 0]", mapToCheck.get(0), 4);

        //retrieve
        //retrieve third value
        retrieveAndAssert(forgettingMap, 1, "[2, 3, 4, 1, 0]", mapToCheck.get(1), 1);


        //retrieve
        retrieveAndAssert(forgettingMap, 2, "[3, 4, 1, 2, 0]", mapToCheck.get(2), 1);
    }

    @Test
    void addFiveValues_retrieveFirstKeyFourTimesAndAddNewValue_firstValueIsLastAndNewValueIsSecondToLast() {
        var forgettingMap = new ForgettingMap<Integer, UUID>(5);
        var mapToCheck = new HashMap<Integer, UUID>();
        for(var i = 0; i < 5; i++) {
            var uuid = UUID.randomUUID();
            forgettingMap.put(i, new CacheWrapper<>(uuid));
            mapToCheck.put(i, uuid);
        }

        //retrieve
        retrieveAndAssert(forgettingMap, 0, "[1, 2, 3, 4, 0]", mapToCheck.get(0), 1);
        retrieveAndAssert(forgettingMap, 0, "[1, 2, 3, 4, 0]", mapToCheck.get(0), 2);
        retrieveAndAssert(forgettingMap, 0, "[1, 2, 3, 4, 0]", mapToCheck.get(0), 3);
        retrieveAndAssert(forgettingMap, 0, "[1, 2, 3, 4, 0]", mapToCheck.get(0), 4);

        //add
        addAndAssert(forgettingMap, 5, UUID.randomUUID(), "[2, 3, 4, 5, 0]");
    }

    @Test
    void addFiveValues_retrieveFirstKeyFourTimesAndTwoNewValuesAndGetFirstAddedValueThenSecond_firstValueIsLastAndNewValueIsSecondToLast() {
        var forgettingMap = new ForgettingMap<Integer, UUID>(5);
        var mapToCheck = new HashMap<Integer, UUID>();
        for(var i = 0; i < 5; i++) {
            var uuid = UUID.randomUUID();
            forgettingMap.put(i, new CacheWrapper<>(uuid));
            mapToCheck.put(i, uuid);
        }

        //retrieve
        retrieveAndAssert(forgettingMap, 0, "[1, 2, 3, 4, 0]", mapToCheck.get(0), 1);
        retrieveAndAssert(forgettingMap, 0, "[1, 2, 3, 4, 0]", mapToCheck.get(0), 2);
        retrieveAndAssert(forgettingMap, 0, "[1, 2, 3, 4, 0]", mapToCheck.get(0), 3);
        retrieveAndAssert(forgettingMap, 0, "[1, 2, 3, 4, 0]", mapToCheck.get(0), 4);
        assertThat(forgettingMap.keySet().toString()).hasToString("[1, 2, 3, 4, 0]");

        //add
        var uuid5 = UUID.randomUUID();
        addAndAssert(forgettingMap, 5, uuid5, "[2, 3, 4, 5, 0]");

        var uuid6 = UUID.randomUUID();
        addAndAssert(forgettingMap, 6, uuid6, "[3, 4, 5, 6, 0]");

        retrieveAndAssert(forgettingMap, 6, "[3, 4, 5, 6, 0]", uuid6, 1);

        retrieveAndAssert(forgettingMap, 5, "[3, 4, 6, 5, 0]", uuid5, 1);
    }

    @Test
    void addFiveValues_getKeyThreeOnThreeDifferentThreads_countReturns1000() throws Exception {
        var forgettingMap = new ForgettingMap<Integer, UUID>(5);
        var mapToCheck = new HashMap<Integer, UUID>();
        for(var i = 0; i < 5; i++) {
            var uuid = UUID.randomUUID();
            forgettingMap.put(i, new CacheWrapper<>(uuid));
            mapToCheck.put(i, uuid);
        }
        ExecutorService service = Executors.newFixedThreadPool(3);

        IntStream.range(0, 1000)
                .forEach(count -> service.submit(() -> forgettingMap.get(3)));
        service.awaitTermination(5000, TimeUnit.MILLISECONDS);
        //1001 because I am getting again
        assertThat(forgettingMap.get(3).getCount()).isEqualTo(1001);
    }

    private void retrieveAndAssert(ForgettingMap<Integer, UUID> forgettingMap, int key, String expectedOrder, UUID expectedValue, int expectedCount) {
        var cacheWrapper = forgettingMap.get(key);
        assertThat(forgettingMap.keySet().toString()).hasToString(expectedOrder);
        assertThat(cacheWrapper.getContent()).isEqualTo(expectedValue);
        assertThat(cacheWrapper.getCount()).isEqualTo(expectedCount);
    }

    private void addAndAssert(ForgettingMap<Integer, UUID> forgettingMap, int key, UUID value, String expected) {
        forgettingMap.put(key, new CacheWrapper<>(value));
        assertThat(forgettingMap.keySet().toString()).hasToString(expected);
    }
}
