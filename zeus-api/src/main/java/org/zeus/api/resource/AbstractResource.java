package org.zeus.api.resource;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class AbstractResource {
    private Gson gson = new Gson();
}
