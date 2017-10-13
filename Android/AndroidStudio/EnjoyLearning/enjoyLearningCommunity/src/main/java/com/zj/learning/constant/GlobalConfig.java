package com.zj.learning.constant;

/**
 * 全局敞亮定义
 */
public interface GlobalConfig {

    public static final String SEPERATOR = "/";
    public static final String COMMA = ",";
    public static final int INVALID = -1;

    /**
     * 项目本地根目录
     **/
    public static final String DIR_ROOT = "/EnjoyLearning";

    /**
     * 课程资源包相关定义
     */

    public static final String DIR_RESOURES = DIR_ROOT + "/Res";
    /**
     * 资源根目录
     **/
    public static final String DIR_VIDEO = DIR_RESOURES + "/videos";
    /**
     * 视频资源目录
     **/
    public static final String DIR_IMG = DIR_RESOURES + "/images";
    /**
     * 图片资源目录
     **/
    public static final String DIR_COURSE = DIR_RESOURES + "/courses";
    /**
     * 课程章节目录
     **/
    public static final String DIR_EXAM = DIR_RESOURES + "/exams";
    /**
     * 考试目录
     **/
    public static final String DIR_HOMEWORK = DIR_RESOURES + "/homeworks";
    /**
     * 作业目录
     **/
    public static final String PATH_RESOURES = DIR_RESOURES + "/resourceses.xml";    /** 资源文件路径 **/

    /**
     * 项目下载缓存目录
     **/
    public static final String DIR_DOWNLOAD = DIR_ROOT + "/Download";
    public static final int MAX_LOAD = 5;


    /**
     * API url
     */
    public static final String URL = "http://192.168.0.171:9090/mdot";
    public static final String URL_LOGIN = URL + "/user/login";
    public static final String URL_REGISTER = URL + "/user/register";
    public static final String URL_COURSE_ME = URL + "/course/getMycourse";
    public static final String URL_COURSE_FOCUS = URL + "/course/focusCourse";
    public static final String URL_COURSE_ADD = URL + "/course/addMycourse";
    public static final String URL_COURSE_FOCUS_ME = URL + "/course/getMyfocus";
    public static final String URL_COURSE_ALL = URL + "/course/allCourse";
    public static final String URL_COURSE_ADD_COMMENT = URL
            + "/course/addComment";
    public static final String URL_COURSE_GET_COMMENT = URL
            + "/course/getComment";
    public static final String URL_COURSE_SECTIONS = URL
            + "/course/detail/data";
    public static final String URL_COURSE_EXAM_LIST = URL + "/exam/list";
    public static final String URL_COURSE_EXAM_LIST_DETAIL = URL
            + "/exam/list/detailData";
    public static final String URL_COURSE_VIDEO_RECORD_ADD = URL
            + "/course/addViewrecord";
    public static final String URL_COURSE_EXAM_COMMIT = URL + "/exam/doExam";
    public static final String URL_CERTIFICATE_COURSE = URL
            + "/certificate/getCourseCertificate";
    public static final String URL_CERTIFICATE_COMMUNITY = URL
            + "/certificate/getCommunityCertificate";
    /**
     * 课程缩略图url
     **/
    public static final String URL_PIC_COURSE_THUMB = URL + "/shared/getData/2";
    /**
     * 问答缩略图url
     **/
    public static final String URL_PIC_ASKS_THUMB = URL + "/shared/getData/1";
    /**
     * 头像缩略图url
     **/
    public static final String URL_PIC_AVATOR_THUMB = URL + "/shared/getData/0";
    // 添加问题
    public static final String URL_QUESTION_ADD = URL + "/ask/ques/save";
    // 获取问题列表
    public static final String URL_QUESTION_LIST = URL + "/ask/ques/list";
    // 获取回答列表
    public static final String URL_ANSWER_LIST = URL + "/ask/ans/list";
    // 添加答案
    public static final String URL_ANSWER_ADD = URL + "/ask/ans/save";
    // 回答评论列表
    public static final String URL_ANSWER_COMMENT_LIST = URL
            + "/ask/ans/comments/list";
    // 添加回答评论
    public static final String URL_ANSWER_COMMENT_ADD = URL
            + "/ask/ans/comments/save";

    public static final String URL_FORUM = URL + "/bbs";
    public static final String URL_FORUM_GETTAG = URL_FORUM + "/tags";
    public static final String URL_FORUM_POSTS_ADD = URL_FORUM + "/addMainPost";
    public static final String URL_FORUM_POSTS_GET = URL_FORUM
            + "/getMainPosts";
    public static final String URL_FORUM_POSTS_COMMENT_NEW = URL_FORUM
            + "/getNewFollowPost";
    public static final String URL_FORUM_Comment_Add = URL_FORUM
            + "/addFollowPost";
    public static final String URL_FORUM_Comment_GET = URL_FORUM
            + "/getFollowPosts";
    public static final String URL_FORUM_POSTS_LOVE = URL_FORUM + "/love";

    /**
     * 缩略图尺寸
     */
    public static final class ImageSize {
        public static final String SIZE30 = "30x30";
        public static final String SIZE60 = "60x60";
        public static final String SIZE90 = "90x90";
        public static final String SIZE180 = "180x180";
        public static final String SIZE170 = "170x110";
    }

    public static final class Limit {
        public static final int MAX_LENGTH_USERNAME = 20;
        public static final int MAX_LENGTH_PSWD = 20;
        public static final int MIN_LENGTH_USERNAME = 2;
        public static final int MIN_LENGTH_PSWD = 6;

        public static final int MIN_LENGTH_COURSE_REVIEW = 10;
        public static final int MAX_LENGTH_COURSE_REVIEW = 1000;
    }

    public static final class Operator {
        public static final int RESOURCE_FETCH = 1;
        public static final int COURSE_DETAIL = RESOURCE_FETCH + 1;
        public static final int COURSE_REVIEW_GET = COURSE_DETAIL + 1;
        public static final int COURSE_FOCUS = COURSE_REVIEW_GET + 1;
        public static final int COURSE_ADD = COURSE_FOCUS + 1;
        public static final int COURSE_EXAM_QUESTIONS_FETCH = COURSE_ADD + 1;
        public static final int COURSE_EXAM_QUESTIONS_COMMIT = COURSE_EXAM_QUESTIONS_FETCH + 1;
        public static final int TAG_FETCH = COURSE_EXAM_QUESTIONS_COMMIT + 1;
        public static final int LIST_FETCH = TAG_FETCH + 1;
        public static final int OBJECT_ADD = LIST_FETCH + 1;
        public static final int OBJECT_FETCH = OBJECT_ADD + 1;
        public static final int GOOD_ADD = OBJECT_FETCH + 1;
    }

    /**
     * SharePreference常量
     */
    public static final class SharedPreferenceDao {
        public static final String FILENAME_USER = "filename_user";
        public static final String FILENAME_VIDEO_RECORD = "filename_video_record";
        public static final String KEY_LOGIN = "key_login";
        public static final String KEY_USER = "key_user";
        public static final String KEY_LOGIN_NAME = "key_username";
        public static final String KEY_USER_PIC = "key_user_pic";
        public static final String KEY_USER_INTEGRATION = "key_integration";
        public static final String KEY_USER_GROUP = "key_group";
        public static final String KEY_USERHELP = "key_user_help";
    }

}
