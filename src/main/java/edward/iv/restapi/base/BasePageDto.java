package edward.iv.restapi.base;

import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class BasePageDto {

    protected int totalPage;

    protected int page;

    protected int size;

    protected boolean sorted;

    protected boolean first;

    protected boolean last;
}
