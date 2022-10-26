package com.mcarrental.billingservice.enums;

import lombok.Getter;

@Getter
public enum SwiftCode {
    ABLTBY22("ЗАО \"Абсолютбанк\""),
    AEBKBY2X("ЗАО \"БТА Банк\""),
    AKBBBY2X("ОАО \"АСБ Беларусбанк\""),
    ALFABY2X("ЗАО \"Альфа-Банк\""),
    BAPBBY2X("ОАО \"Белагропромбанк\""),
    BBTKBY2X("ЗАО \"ТК Банк\""),
    BELBBY2X("ОАО \"Банк БелВЭБ\""),
    BLBBBY2X("ОАО \"Белинвестбанк\""),
    BLNBBY2X("ОАО \"БНБ Банк\""),
    BPSBBY2X("ОАО \"БПС-Сбербанк\""),
    BRRBBY2X("ОАО \"Банк развития Республики Беларусь\""),
    GTBNBY22("ОАО \"Франсабанк\""),
    IRJSBY22("ОАО \"СтатусБанк\""),
    MMBNBY22("ОАО \"Банк Дабрабыт\""),
    MTBKBY22("ЗАО \"МТБанк\""),
    OLMPBY2X("ОАО \"Белгазпромбанк\""),
    PJCBBY2X("ОАО \"Приорбанк\""),
    POISBY2X("ОАО \"Паритетбанк\"\""),
    REDJBY22("ЗАО \"РРБ–Банк\""),
    RSHNBY2X("ЗАО \"Банк \"Решение\""),
    SLANBY22("ЗАО Банк ВТБ (Беларусь)"),
    TECNBY22("ОАО \"Технобанк\""),
    UNBSBY2X("ЗАО \"БСБ Банк\""),
    ZEPTBY2X("ЗАО \"Цептер Банк\"");

    private final String bankName;

    SwiftCode(String bankName) {
        this.bankName = bankName;
    }
}
