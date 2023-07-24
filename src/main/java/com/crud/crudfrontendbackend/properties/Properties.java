package com.crud.crudfrontendbackend.properties;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Properties {
    Date TODAY = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

}
