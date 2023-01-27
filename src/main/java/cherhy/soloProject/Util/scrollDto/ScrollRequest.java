package cherhy.soloProject.Util.scrollDto;

public record ScrollRequest(Long key, int size) { //커서키는 인덱스가 있어야 하고 정렬도 가능해야 하며 중복된 데이터가 존재하면 안된다!
    public static final Long NONE_KEY = -1L; //마지막 페이지일 경우, 키값이 -1일 수 없기 때문에

    public Boolean hasKey() {
        return key != null && !key.equals(NONE_KEY);
    } // 리팩토링 대상 ,,, boolean 말고 -1, 0, 1로...
    // -1 = 마지막 페이지, 0 = 시작페이지, 1보다 크면 조회

    public ScrollRequest next(Long key) {
        return new ScrollRequest(key, size);
    }

}
