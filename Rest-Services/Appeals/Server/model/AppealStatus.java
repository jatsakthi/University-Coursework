package com.appealing.model;

import javax.xml.bind.annotation.XmlEnumValue;


public enum AppealStatus {
    @XmlEnumValue(value="unreviewed")
    UNREVIEWED,
    @XmlEnumValue(value="reviewing")
    REVIEWING, 
    @XmlEnumValue(value="positive")
    POSITIVE, 
    @XmlEnumValue(value="negative")
    NEGATIVE,
    @XmlEnumValue(value="reviewed")
    REVIEWED
}
