package edward.iv.restapi.payload.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edward.iv.restapi.base.BasePageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"totalElement", "data", "totalPage", "page", "size", "sorted", "first", "last"})
public class PageResponse<T> extends BasePageDto {

    private Long totalElement;

    private List<T> data;

    public PageResponse(int totalPage, int page, int size, boolean sorted, boolean first, boolean last, Long totalElement, List<T> data) {
        this.totalPage = totalPage;
        this.page = page;
        this.size = size;
        this.sorted = sorted;
        this.first = first;
        this.last = last;
        this.totalElement = totalElement;
        this.data = data;
    }
}