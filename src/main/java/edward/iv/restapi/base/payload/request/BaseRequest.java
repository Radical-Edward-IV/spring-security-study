package edward.iv.restapi.base.payload.request;

import lombok.Data;
import org.springframework.data.domain.PageRequest;

@Data
public class BaseRequest<T> {

    private PageRequest page;
}
