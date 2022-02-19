package com.joeldavidmason.forgettingmap;

import com.joeldavidmason.forgettingmap.cache.impl.UUIDCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ForgettingMap Tests")
class UUIDCacheTests {

    @Test
    void addFiveValues_retrieveAllValues_valueShouldBeAsExpected() {
        var forgettingCache = new UUIDCache(5);
        var mapToCheck = new HashMap<Integer, UUID>();
        for(var i = 0; i < 5; i++) {
            var uuid = UUID.randomUUID();
            forgettingCache.add(i, uuid);
            mapToCheck.put(i, uuid);
        }
        assertThat(forgettingCache.retrieve(0)).isEqualTo(mapToCheck.get(0));
        assertThat(forgettingCache.retrieve(1)).isEqualTo(mapToCheck.get(1));
        assertThat(forgettingCache.retrieve(2)).isEqualTo(mapToCheck.get(2));
        assertThat(forgettingCache.retrieve(3)).isEqualTo(mapToCheck.get(3));
        assertThat(forgettingCache.retrieve(4)).isEqualTo(mapToCheck.get(4));
    }

    @Test
    void addFiveValues_retrieveValueThatDoesNotExist_OrderStaysTheSame() {
        var forgettingCache = new UUIDCache(5);
        for(var i = 0; i < 5; i++) {
            forgettingCache.add(i, UUID.randomUUID());
        }
        assertThat(forgettingCache.retrieve(10)).isNull();
    }

    @Test
    void addFiveValues_addSixth_zeroIsReplaced() {
        var forgettingCache = new UUIDCache(5);
        var mapToCheck = new HashMap<Integer, UUID>();
        for(var i = 0; i < 5; i++) {
            var uuid = UUID.randomUUID();
            forgettingCache.add(i, uuid);
            mapToCheck.put(i, uuid);
        }
        var sixthUUID = UUID.randomUUID();
        forgettingCache.add(5, sixthUUID);
        assertThat(forgettingCache.retrieve(0)).isNull();
        assertThat(forgettingCache.retrieve(5)).isEqualTo(sixthUUID);
    }


    @Test
    void addFiveValues_replaceAllValues_originalValuesAreRemoved() {
        var forgettingCache = new UUIDCache(5);
        var mapToCheck = new HashMap<Integer, UUID>();
        for(var i = 0; i < 5; i++) {
            var uuid = UUID.randomUUID();
            forgettingCache.add(i, uuid);
            mapToCheck.put(i, uuid);
        }
        for(var i = 5; i < 10; i++) {
            var uuid = UUID.randomUUID();
            forgettingCache.add(i, uuid);
            mapToCheck.put(i-5, uuid);
        }
        assertThat(forgettingCache.retrieve(0)).isNull();
        assertThat(forgettingCache.retrieve(1)).isNull();
        assertThat(forgettingCache.retrieve(2)).isNull();
        assertThat(forgettingCache.retrieve(3)).isNull();
        assertThat(forgettingCache.retrieve(4)).isNull();
        assertThat(forgettingCache.retrieve(5)).isEqualTo(mapToCheck.get(0));
        assertThat(forgettingCache.retrieve(6)).isEqualTo(mapToCheck.get(1));
        assertThat(forgettingCache.retrieve(7)).isEqualTo(mapToCheck.get(2));
        assertThat(forgettingCache.retrieve(8)).isEqualTo(mapToCheck.get(3));
        assertThat(forgettingCache.retrieve(9)).isEqualTo(mapToCheck.get(4));
    }

    @Test
    void addFiveValues_getOneValueThenAddFiveMore_originalValuesAreRemovedExcludingRetrieved() {
        var forgettingCache = new UUIDCache(5);
        var mapToCheck = new HashMap<Integer, UUID>();
        for(var i = 0; i < 5; i++) {
            var uuid = UUID.randomUUID();
            forgettingCache.add(i, uuid);
            mapToCheck.put(i, uuid);
        }
        assertThat(forgettingCache.retrieve(0)).isEqualTo(mapToCheck.get(0));
        for(var i = 5; i < 10; i++) {
            var uuid = UUID.randomUUID();
            forgettingCache.add(i, uuid);
            mapToCheck.put(i, uuid);
        }
        assertThat(forgettingCache.retrieve(0)).isEqualTo(mapToCheck.get(0));
        assertThat(forgettingCache.retrieve(1)).isNull();
        assertThat(forgettingCache.retrieve(2)).isNull();
        assertThat(forgettingCache.retrieve(3)).isNull();
        assertThat(forgettingCache.retrieve(4)).isNull();
        //5 is null because 5 was added first in the new lot, and 0's count is 1
        assertThat(forgettingCache.retrieve(5)).isNull();
        assertThat(forgettingCache.retrieve(6)).isEqualTo(mapToCheck.get(6));
        assertThat(forgettingCache.retrieve(7)).isEqualTo(mapToCheck.get(7));
        assertThat(forgettingCache.retrieve(8)).isEqualTo(mapToCheck.get(8));
        assertThat(forgettingCache.retrieve(9)).isEqualTo(mapToCheck.get(9));
    }
}
