package com.qimeixun.constant;

public interface SystemConstant {

    /**
     * 充值金额
     */
    String USER_CHARGE_MONEY = "0";
    /**
     * 充值积分
     */
    String USER_CHARGE_POINTS = "1";
    /**
     * 充值
     */
    String USER_CHARGE_ADD = "0";
    /**
     * 扣减
     */
    String USER_CHARGE_SUB = "1";

    /**
     * 管理员用户登录名验证规则正则表达式
     * 以字母、数字开头的5-20位字符，只能包含字母、数字、下划线'_'
     */
    String LOGIN_NAME_REGEX = "^[a-zA-Z0-9][a-zA-Z0-9_]{4,19}$";

    /**
     * Redis中存储的用户对象信息字段的key值
     */
    String REDIS_USER_INFO_KEY = "userInfo";

    /**
     * Redis中存储的用于区分会员用户、后台管理员用户字段的key值
     */
    String REDIS_USER_TYPE_KEY = "userType";

    /**
     * 管理员用户Redis缓存类型名称，用于区分会员用户与后台管理员用户
     */
    String REDIS_SYS_USER_TYPE = "adminUser";

    /**
     * 会员用户Redis缓存类型名称，用于区分会员用户与后台管理员用户
     */
    String REDIS_MEMBER_USER_TYPE = "memberUser";

    /**
     * 用户权限Redis key
     */
    String REDIS_PERMISSION_LIST_KEY = "permissionUrl";

    String successMessage = "成功";

    /**
     * 产品列表默认排序
     */
    String PRODUCT_DEFAULT_SORT_TYPE = "0";

    /**
     * 产品列表最新排序
     */
    String PRODUCT_TIME_SORT_TYPE = "1";

    /**
     * 产品列表价格升序排序
     */
    String PRODUCT_PRICE_ASC_SORT_TYPE = "2";

    /**
     * 产品列表价格降序排序
     */
    String PRODUCT_PRICE_DESC_SORT_TYPE = "3";

    /**
     * 产品列表销量排序
     */
    String PRODUCT_SALES_SORT_TYPE = "4";

    /**
     * 微信小程序登录
     */
    String USER_LOGIN_TYPE = "0";

    String SYS_CONFIG_HEAD = "sys_config:";

    /**
     * 阿里短信key
     */
    String ACCESS_KEY_ID = "access_key_id";

    /**
     * 阿里短信密钥
     */
    String ACCESS_KEY_SECRET = "access_key_secret";

    /**
     * 微信小程序登录方式
     */
    String LOGIN_TYPE_WX_MP = "wxMp";

    String WEIXIN_PAY_SERVICE = "WEIXIN_PAY_SERVICE";

    String WEIXIN_MINI_PAY_SERVICE = "WEIXIN_MINI_PAY_SERVICE";

    String WEIXIN_MP_SERVICE = "WEIXIN_MP_SERVICE";

    String WECHAT_APPID = "wxapp_appId";

    String WECHAT_SECRET = "wxapp_secret";

    String WXPAY_MCHID = "wxpay_mchId";

    String WXPAY_MCHKEY = "wxpay_mchKey";

    String API_URL = "api_url";

    /**
     * 加库存
     */
    String STOCK_TYPE_ADD = "add";

    /**
     * 减库存
     */
    String STOCK_TYPE_SUB = "sub";


    /**
     * 订单超时取消 key
     */
    String SECKILL_PRODUCT = "seckill:product:";

    /**
     * 下订单 超时取消订单 时间阈值
     */
    String CANCEL_ORDER_TIME = "cancel_order_time";

    /**
     * 自动确认收货 时间阈值
     */
    String AUTO_RECEIVE_TIME = "auto_receving";


    /**
     * 订单超时取消 key
     */
    String REDIS_CANCEL_KEY = "notice:cancel:order:";


    /**
     * 自动确认收货 时间阈值
     */
    String AUTO_RECEIVE_KEY = "notice:receive:order:";

}
