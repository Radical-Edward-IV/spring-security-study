package edward.iv.restapi.payload.response;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

@Data
@Accessors(chain = true)
public class ResponseForm<T> {

    private int totalCount;

    private String errorCode, message;

    private T data;

    private Page<T> page;
}
