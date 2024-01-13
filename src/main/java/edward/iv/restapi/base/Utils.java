package edward.iv.restapi.base;

import edward.iv.restapi.exception.BadRequestException;
import edward.iv.restapi.base.payload.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Function;

/**
 * 정적 변수와 정적 메소드가 정의된 Utility Class입니다.
 *
 * @author Edward Se Jong Pepelu Tivrusky IV
 */
public class Utils {

    /**
     * @param page 페이지 번호
     * @param function Entity를 Dto로 매핑하는 함수
     * @return : 페이징 처리된 응답 데이터
     */
    public static <T, R> PageResponse<R> getPageResponse(Page page, Function<T, List<R>> function) {

        T data = (T) page.getContent();
        List<R> payload = function.apply(data);
        return new PageResponse<R>(
                page.getTotalPages(), page.getNumber() + 1, page.getSize(),
                page.getSort().isSorted(),
                page.isFirst(), page.isLast(),
                page.getTotalElements(), payload
        );
    }

    /**
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return Pageable
     */
    public static Pageable getPageable(int page, int size) {

        validatePageNumberAndSize(page, size);

        return PageRequest.of(page, size);
    }

    /**
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param sort 데이터 정렬
     * @param property 정렬 대상 속성
     * @return Pageable
     * @throws BadRequestException
     */
    public static Pageable getPageable(int page, int size, Sort.Direction sort, String... property) throws BadRequestException {

        validatePageNumberAndSize(page, size);

        return PageRequest.of(page - 1, size, sort, property);
    }

    /**
     * 페이지 번호와 크기의 유효성 체크
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     */
    private static void validatePageNumberAndSize(int page, int size) {

        if (page < 1 || size < 1) throw new BadRequestException("Page number or size cannot be less than one.");
    }
}
