package org.bistu.garbageclassification.utils;

public class ServerUrl {

    public static final String URL = "http://192.168.1.131:8000"; //IP端口号

    public static final String GET_CITY_LIST = URL + "/city/getCityList";

    public static final String GET_QUESTION = URL + "/question/getQuestion";
    public static final String RECORD_ANSWER_TIMES = URL + "/question/answer";

    public static final String GET_TOPS_GARBAGE_LIST = URL + "/garbage/getTops";
    public static final String GET_SIMILAR_GARBAGE = URL + "/garbage/getSimilar";
    public static final String GET_GLIST_BY_TYPE = URL + "/garbage/gListByType";
    public static final String EXACT_QUERY = URL + "/garbage/exactQuery";

    public static final String TYPE_DETAIL = URL + "/type/getTypeDetail";
    public static final String GET_TYPES_4 = URL + "/type/typePre";

    public static final String GUIDE_DETAIL = URL + "/guide/getGuide";
    public static final String GET_GUIDE_LIST = URL + "/guide/getGuides";

    public static final String GET_POLICY = URL + "/policy/getPolicyByCity";

}
