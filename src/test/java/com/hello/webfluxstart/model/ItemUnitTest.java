package com.hello.webfluxstart.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ItemUnitTest {
    @Test
    public void 기본적인_모델_테스트() {
        Item sampleItem = new Item("TV tray", "Alf TV tray", 19.99);

        // 원래는 게터, 세터도 테스트하는편이 좋다.
        Assertions.assertThat(sampleItem.getName()).isEqualTo("TV tray");
        Assertions.assertThat(sampleItem.getDescription()).isEqualTo("Alf TV tray");
        Assertions.assertThat(sampleItem.getPrice()).isEqualTo(19.99);

        Item item2 = new Item("TV tray", "Alf TV tray", 19.99);
        Assertions.assertThat(sampleItem).isEqualTo(item2);
    }
}