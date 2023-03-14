package cherhy.soloProject.Util.scrollDto;

import java.io.Serializable;

public record ScrollRequest(Long key) implements Serializable { //커서키는 인덱스가 있어야 하고 정렬도 가능해야 하며 중복된 데이터가 존재 x
    public static final Long NONE_KEY = -1L; //마지막 페이지일 경우, 키값이 -1일 수 없기 때문에
    public static final int size = 3;

    public Boolean hasKey() {
        return key != null && !key.equals(NONE_KEY);
    }

    public ScrollRequest next(Long key) {
        return new ScrollRequest(key);
    }

}
