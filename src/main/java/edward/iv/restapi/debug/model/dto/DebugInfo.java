package edward.iv.restapi.debug.model.dto;

import edward.iv.restapi.base.Utils;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequestScope
@Getter
@Setter
@Accessors(chain = true)
public class DebugInfo {

    private String debugId;

    private List<String> endpoint;

    private List<String> classNameAndMethod;

    private Map<String, String> fields;

    @PostConstruct
    public void init() {

        String debugId           = Utils.getRandomString();

        this.debugId             = debugId;

        this.endpoint            = new ArrayList<>();

        this.classNameAndMethod  = new ArrayList<>();

        this.fields              = new HashMap<>();
    }
}
